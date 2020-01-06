package com.fairy.serurity;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.alibaba.fastjson.JSONObject;
import com.fairy.utils.data.MngResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

/**
 * 密码验证
 */
public class PasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {



	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		UsernamePasswordAuthenticationToken authRequest;
		try (InputStream is = request.getInputStream()) {
			DocumentContext context = JsonPath.parse(is);
			String username = context.read("$.username", String.class);
			String password = context.read("$.password", String.class);
			authRequest = new UsernamePasswordAuthenticationToken(username, password);
		} catch (IOException e) {
			e.printStackTrace();
			authRequest = new UsernamePasswordAuthenticationToken("", "");
		}
		setDetails(request, authRequest);
		return this.getAuthenticationManager().authenticate(authRequest);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
	    responseText(response, JSONObject.toJavaObject(JSONObject.parseObject(failed.getMessage()), MngResponse.class));
	}

	private static void responseText(HttpServletResponse response, Object content) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		String strContent = mapper.writeValueAsString(content);

		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		byte[] bytes = strContent.getBytes(StandardCharsets.UTF_8);
		response.setContentLength(bytes.length);
		response.getOutputStream().write(bytes);
		response.flushBuffer();
	}

}
