package com.fairy.configure;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.alibaba.fastjson.JSONObject;
import com.fairy.cache.CacheService;
import com.fairy.serurity.ApiTokenAuthFilter;
import com.fairy.serurity.PasswordAuthenticationFilter;
import com.fairy.serurity.SecretAuthenticationFilter;
import com.fairy.user.entity.FairyUser;
import com.fairy.user.service.IFairyUserService;
import com.fairy.utils.commons.DateTimeUtils;
import com.fairy.utils.data.MngResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    CacheService tokenSvr;
    
    @Autowired
    private IFairyUserService iFairyUserService;

    @Autowired
    FairyProperties fairyProperties;

    // 设置 HTTP 验证规则
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/fairy/**/**").authenticated()
        .and().formLogin();

        http.logout().logoutUrl("/fairy/user/logout").addLogoutHandler((request, response, authentication) -> {
        }).logoutSuccessHandler((request, response, authentication) -> {
            tokenSvr.deleteToken(request.getHeader("username"));
        }).invalidateHttpSession(true).clearAuthentication(true);

        http.exceptionHandling()
                .accessDeniedHandler((request, response, accessDeniedException) -> responseText(response,
                        MngResponse.ERROR(104, accessDeniedException.getMessage())))
                .authenticationEntryPoint((request,response,authException)-> {
                        String uri = request.getRequestURI();
                        log.warn("Checked illegal request, the uri: " + uri);
                        responseText(response, MngResponse.ERROR(103, authException.getMessage()));
                });

        http.addFilterBefore(secretAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(passwordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(apiTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);

        auth.authenticationProvider(new AuthenticationProvider() {

            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String username = authentication.getName();
                String token = (String) authentication.getCredentials();
                if (tokenSvr.verifyToken(username, token)) {
                    return new PreAuthenticatedAuthenticationToken(username, token);
                } else {
                    log.info("User: " + username + " verify token: " + token + " failure.");
                    throw new BadCredentialsException("access deney");
                }
            }

            @Override
            public boolean supports(Class<?> arg0) {
                return arg0.equals(PreAuthenticatedAuthenticationToken.class);
            }
        });

        auth.authenticationProvider(new AuthenticationProvider() {

            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String username = authentication.getName();
                MngResponse mngResponse = new MngResponse();
                FairyUser user = iFairyUserService.queryUserByNameOrEmail(username);
                Integer wrongTimes = user.getWrongTimes();
                if (wrongTimes != null && wrongTimes >= 5) {
                    mngResponse.code = 201;
                    mngResponse.message = "账户登录错误次数过多,请联系管理员!";
                    throw new DisabledException(JSONObject.toJSONString(mngResponse));
                }
                Integer userStatus = user.getActive();
                if(userStatus != null && userStatus != 1) {
                    mngResponse.code = 202;
                    mngResponse.message = "账户属于锁定状态,请联系管理员!";
                    throw new DisabledException(JSONObject.toJSONString(mngResponse));
                }
                String password = (String) authentication.getCredentials();
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                boolean flag = iFairyUserService.checkUserPwd(username, password);
                if(!flag) {
                    iFairyUserService.increaseWrongTimes(username); 
                    mngResponse.code = 102;
                    mngResponse.message = "账户或密码错误";
                    throw new DisabledException(JSONObject.toJSONString(mngResponse));
                }

                Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
                return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), authorities);

            }

            @Override
            public boolean supports(Class<?> arg0) {
                return arg0.equals(UsernamePasswordAuthenticationToken.class);
            }
        });

    }

    private PasswordAuthenticationFilter passwordAuthenticationFilter() throws Exception {
        PasswordAuthenticationFilter filter = new PasswordAuthenticationFilter();
        filter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            FairyUser user = iFairyUserService.queryUserByNameOrEmail(authentication.getName());
            UserResp usp = new UserResp();
            if (user != null) {
                usp.username = authentication.getName();
                usp.accountName = user.getAccountName();
                usp.mobile = user.getMobile();
                usp.email = user.getEmail();
                usp.department = user.getDepartment();
                usp.role_name = user.getRoleName();
                usp.token = tokenSvr.updateToken(authentication.getName());
            }
            user.setLastLoginTime(DateTimeUtils.getStringToday());
            FairyUser updateUser = new FairyUser();
            updateUser.setAccount(user.getAccount());
            updateUser.setLastLoginTime(DateTimeUtils.getStringToday());
            updateUser.setWrongTimes(0);
            iFairyUserService.updateUserByNameOrEmail(updateUser);
			responseText(response, MngResponse.SUCESS_RESULT(usp));
		});
		filter.setAuthenticationFailureHandler((request, response, exception) -> responseText(response,
				MngResponse.ERROR(102, exception.getMessage())));
		filter.setAuthenticationManager(authenticationManager());
		filter.setFilterProcessesUrl("/fairy/user/login");
		return filter;
	}

	private ApiTokenAuthFilter apiTokenAuthenticationFilter() throws Exception {
		ApiTokenAuthFilter filter = new ApiTokenAuthFilter("/fairy/admin/*");
		filter.setAuthenticationManager(authenticationManager());
		return filter;
	}

	private SecretAuthenticationFilter secretAuthenticationFilter() throws Exception {
		SecretAuthenticationFilter filter = new SecretAuthenticationFilter("/fairy/getSecretKey");
		filter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            log.info("sucess authed:" + request.getRequestURI());
			String publicKeystr = fairyProperties.getRsaPublicKey();
			responseText(response, MngResponse.SUCESS_RESULT(publicKeystr));
		});
		filter.setAuthenticationFailureHandler((request, response, exception) -> responseText(response,
				MngResponse.ERROR(102, exception.getMessage())));
		filter.setAuthenticationManager(authenticationManager());
		return filter;
	}
	
	private static void responseText(HttpServletResponse response, Object content) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		String strContent = mapper.writeValueAsString(content);

		response.setContentType("application/json;charset=UTF-8");
		byte[] bytes = strContent.getBytes(StandardCharsets.UTF_8);
		response.setContentLength(bytes.length);
		response.getOutputStream().write(bytes);
		response.flushBuffer();
	}
	
	@Data
	public static class UserResp {
		String username;

		String token;
		
		String mobile;
		
		String email;
		
		String accountName;
		
		String role_name;
		
		String department;
		
	}

}
