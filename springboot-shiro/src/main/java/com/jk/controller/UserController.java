package com.jk.controller;

import com.jk.model.UserBean;
import com.jk.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping("toLogin")
    public String toLogin() {

        return "login";
    }
    @RequestMapping("login")
    public String toLogin(UserBean user, Model model){
        Subject subject=SecurityUtils.getSubject();
        UsernamePasswordToken userToken=new UsernamePasswordToken(user.getUsername(),user.getPassword());
        try{subject.login(userToken);

            return "redirect:/index";
        }catch (UnknownAccountException e){

            model.addAttribute("msg","用户名不存在");
            return "login";
        }catch (IncorrectCredentialsException e){

            model.addAttribute("msg","密码错误");
            return "login";
        }
    }
}




/*    @RequestMapping("login")
    public String login(UserBean ub, Model model) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken userToken = new UsernamePasswordToken(ub.getUsername(), ub.getPassword());
        try {
            subject.login(userToken);
        } catch (UnknownAccountException e) {

            model.addAttribute("suc", "用户名不存在");
            return "login";

        } catch (IncorrectCredentialsException q){

            return null;
        }

    }
}*/

