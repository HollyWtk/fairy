package com.fairy.user.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 *   用户实体类
 * </p>
 *
 * @author yhh
 * @since 2019-12-19
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FairyUser implements UserDetails{

    private static final long serialVersionUID = 6023736392825666588L;

    @TableId(value = "fld_id", type = IdType.AUTO)
    private Integer fldId;

    /**
     * 账户
     */
    private String fldAccount;

    /**
     * 用户姓名
     */
    private String fldAccountName;

    /**
     * 密码
     */
    private String fldPassword;

    /**
     * 上次登录时间
     */
    private String fldLastLoginTime;

    /**
     * 用户状态
     */
    private Integer fldActive;

    /**
     * 错误次数
     */
    private Integer fldWrongTimes;

    /**
     * 角色ID
     */
    private String fldRoleName;

    /**
     * 联系方式
     */
    private String fldMobile;

    /**
     * 部门ID
     */
    private String fldDepartment;

    /**
     * 邮箱
     */
    private String fldEmail;

    /**
     * 创建时间
     */
    private String fldCreateTime;

    /**
     * 创建人
     */
    private String fldCreater;

    /**
     * 最后更新人
     */
    private String fldUpdater;

    /**
     * 最后更新时间
     */
    private String fldUpdateTime;
    
    @TableField(exist = false)
    private Set<GrantedAuthority> authorities = new HashSet<>();

    public FairyUser(String username, String password, Collection<? extends GrantedAuthority> authorities2) {
        this.fldAccount = username;
        this.fldPassword = password;
        this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return this.authorities;
    }

    @Override
    public String getPassword() {

        return this.fldPassword;
    }

    @Override
    public String getUsername() {

        return this.fldAccount;
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return fldActive == 1 ? true : false;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }

    private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(new AuthorityComparator());

        for (GrantedAuthority grantedAuthority : authorities) {
            Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }

        return sortedAuthorities;
    }

    private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {

        private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
            if (g2.getAuthority() == null) {
                return -1;
            }

            if (g1.getAuthority() == null) {
                return 1;
            }

            return g1.getAuthority().compareTo(g2.getAuthority());
        }
    }


}
