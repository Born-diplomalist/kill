package com.born.secKill.server.config;/**
 * Created by Administrator on 2020/3/2.
 */

import com.born.secKill.server.service.CustomRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:shiro的通用化配置
 * @Author:gyk
 * @Date: 2020/3/2 17:54
 **/
@Configuration
public class ShiroConfig {

    @Bean
    public CustomRealm customRealm(){
        return new CustomRealm();
    }

    /**
    安全管理器，需要绑定Realm
     */
    //不要导成java.lang.SecurityManager
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager=new DefaultWebSecurityManager();
        securityManager.setRealm(customRealm());
        return securityManager;
    }

    /*
         * Shiro内置过滤器，可以实现权限相关的拦截器
     *    常用的过滤器：
     *       anon: 无需认证（登录）可以访问
     *       authc: 必须认证才可以访问
     *       user: 如果使用rememberMe的功能可以直接访问
     *       perms： 该资源必须得到资源权限才可以访问
     *       role: 该资源必须得到角色权限才可以访问
     */

    /**
     * Shiro过滤器
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(){
        ShiroFilterFactoryBean bean=new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager());
        //修改默认的被拦截后要跳转的登录页面
        bean.setLoginUrl("/to/login");
        //设置无需权限的连接
        bean.setUnauthorizedUrl("/unauth");

        Map<String, String> filterChainDefinitionMap=new HashMap<>();

        //anon对应的请求无需授权，authc对应的请求必须授权
        //filterChainDefinitionMap.put("/to/login","anon");
        filterChainDefinitionMap.put("/**","anon");

        //filterChainDefinitionMap.put("/kill/execute/*","authc");//为JMeter需要暂时放行
        filterChainDefinitionMap.put("/kill/detail/*","authc");

        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return bean;
    }

}




























