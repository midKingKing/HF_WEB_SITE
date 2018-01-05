package com.hf.controllers;

import com.hf.dto.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by fjm on 2017/12/25.
 */
@Controller
public class LoginController {

    private static final String VIEW_LOGIN = "/login";
    private static final String VIEW_INDEX = "/index";

    @RequestMapping(value = {"/login"},method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request, String username, String password) {
        HttpSession session = request.getSession(false);
        UsernamePasswordToken usernamePasswordToken=new UsernamePasswordToken(username,password);
        //所有Subject都绑定到SecurityManager，与Subject的所有交互都会委托给SecurityManager；可以把Subject认为是一个门面；SecurityManager才是实际的执行者
        Subject subject = SecurityUtils.getSubject();
        ModelAndView modelAndView = new ModelAndView();
        try {
            subject.login(usernamePasswordToken);   //完成登录
            User user=(User) subject.getPrincipal();
            session.setAttribute("user", user);
            modelAndView.setViewName("redirect:"+VIEW_INDEX+".html");
        } catch(Exception e) {
            modelAndView.setViewName(VIEW_LOGIN);
            modelAndView.addObject("message","账号或密码错误");
        }
        return modelAndView;
    }

    @RequestMapping("/logout")
    public String logOut(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "login";
    }
}
