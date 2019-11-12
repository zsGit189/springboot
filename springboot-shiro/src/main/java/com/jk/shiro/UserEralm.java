package com.jk.shiro;

import com.jk.model.UserBean;
import com.jk.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

public class UserEralm extends AuthorizingRealm {

    @Autowired
    private UserService userService;


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
        System.out.println("授权");
        Subject subject = SecurityUtils.getSubject();
        UserBean user = (UserBean)subject.getPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addStringPermission(user.getAuthority());
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0)
            throws AuthenticationException {
        System.out.println("认证");
        UsernamePasswordToken user = (UsernamePasswordToken)arg0;
        System.out.println("验证密码--------------"+String.copyValueOf(user.getPassword()));
        UserBean userBean = new UserBean();
        userBean.setUsername(user.getUsername());
        userBean.setPassword(String.copyValueOf(user.getPassword()));
        UserBean u = userService.queryUser(userBean.getUsername());
        if(u==null){

            return null;
        }
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(u,u.getPassword(),"");
        SecurityUtils.getSubject().getSession().setAttribute("login",u.getUsername());
        return simpleAuthenticationInfo;
    }
}
