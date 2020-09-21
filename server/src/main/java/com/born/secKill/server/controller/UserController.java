package com.born.secKill.server.controller;/**
 * Created by Administrator on 2020/3/2.
 */

import com.born.secKill.server.common.ConstantClass;
import com.born.secKill.server.service.IUserService;
import com.born.secKill.server.utils.JsonBean;
import com.born.secKill.server.utils.StatusCode;
import com.born.secKill.model.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * 用户controller
 * @Author:gyk
 * @Date: 2020/3/2 17:45
 **/
@Controller
public class UserController {

    private static final Logger log= LoggerFactory.getLogger(UserController.class);

    @Autowired
    private Environment env;

    @Autowired
    private IUserService userService;

    /**
     * 跳到登录页
     */
    @RequestMapping(value = {"/to/login","/unauth"})
    public String toLogin(){
        return "user/login";
    }

        //TODO:用户A登录后，用户B再次登录会直接使用A的信息登陆进去而无视用户B输入的信息？？？
        //由于登录时有缓存，多次登录会直接拿session中当前用户的userId信息来登录而不会验证用户名密码。此处删除该信息
        //session.removeAttribute("userId");

    /**
     * 登录认证
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(@RequestParam String userName, @RequestParam String password,@RequestParam String cpacha,ModelMap modelMap, HttpServletRequest request){
        //封装shiro的token
        String errorMsg="";
        HttpSession session = request.getSession();
        //验证码判断
        if (!cpacha.toUpperCase().equals(session.getAttribute("loginCpacha").toString().toUpperCase())){
            modelMap.addAttribute("errorMsg","验证码错误");
            return "user/login";
        }
        try {
            //如果认证不通过,才做登录；如果一个浏览器的某一个tab标签登录过了，其他标签无需再登录
            if (!SecurityUtils.getSubject().isAuthenticated()){
                String newPsd=new Md5Hash(password,env.getProperty("shiro.encrypt.password.salt")).toString();
                UsernamePasswordToken token=new UsernamePasswordToken(userName,newPsd);
                SecurityUtils.getSubject().login(token);//执行Shiro的CustomRealm逻辑，将值存进session
            }
        }catch (UnknownAccountException | IncorrectCredentialsException | DisabledAccountException e){
            errorMsg=e.getMessage();
            modelMap.addAttribute("userName",userName);
        } catch (Exception e){
            errorMsg="用户登录异常，请联系管理员!";
            e.printStackTrace();
        }

        Integer userId= (Integer)session.getAttribute("userId");
        User user = userService.getUserByUserId(userId);
        if (user==null){
            errorMsg="查询用户失败，用户不存在!";
        }
        //errorMsg非空说明发生错误，返回错误信息并重定向到登录页
        if (StringUtils.isNotBlank(errorMsg)){
            modelMap.addAttribute("errorMsg",errorMsg);
            return "user/login";
        }
        //判断当前用户身份是商家,跳转到后台管理页
        if (ConstantClass.MERCHANT_IDENTITY.equals(user.getIdentity())){
            modelMap.addAttribute("user", user);
            return "user/merchant";
        }
        //跳转到首页---用户可秒杀商品展示页
        return "redirect:/index";
    }


    /**
     * 判断指定用户名是否存在
     */
    @PostMapping("/judgeUsername")
    @ResponseBody
    public JsonBean<Object> isExist(@RequestParam String userName){
        if (StringUtils.isBlank(userName)){
            return new JsonBean<>(StatusCode.InvalidParams);
        }
        User userByUserName = userService.getUserByUserName(userName);
        //用户名已存在
        if (null!=userByUserName && StringUtils.isBlank(userByUserName.getUserName()) ){
            return new JsonBean<>(StatusCode.UserHasExists);
        }
        //用户名不存在
        return new JsonBean<>(StatusCode.UserNotExists);
    }

    /**
     * 跳到注册页
     * @return
     */
    @GetMapping(value = {"/toRegedit","/unauth"})
    public String toRegedit(){
        System.out.println("toRegedit方法，返回值String");

        return "user/regedit";
    }


    //public String regedit(@RequestParam User user, ModelMap modelMap, HttpServletRequest request){
    /**
     * 注册
     */
    @GetMapping("/regedit")
    public String regedit(@RequestParam String userName,@RequestParam String email,@RequestParam String phone,@RequestParam String identity,@RequestParam String isActive,ModelMap modelMap, HttpServletRequest request){
        //校验各个参数
        if (!StringUtils.isNoneBlank(userName,email,phone,identity,isActive)){
            throw new RuntimeException(StatusCode.InvalidParams.getMsg());
        }
        User user=new User();
        user.setUserName(userName);
        user.setPassword(new Md5Hash("123456",env.getProperty("shiro.encrypt.password.salt")).toString());
        user.setPhone(phone);
        user.setEmail(email);
        user.setIdentity(Byte.valueOf(identity));
        user.setIsActive(Byte.valueOf(isActive));
        user.setCreateTime(new Date());
        User resultUser = userService.regedit(user);
        if (null!=resultUser && StringUtils.isNotBlank(resultUser.getUserName())){
            request.getSession().setAttribute("user", resultUser);
            return "redirect:/index";
        }
        //失败，重定向到注册
        return "redirect:/user/toRegedit";
    }


    /**
     * 退出登录
     * @return
     */
    @RequestMapping(value = "/logout")
    public String logout(){
        SecurityUtils.getSubject().logout();
        return "redirect:to/login";
    }


}



































