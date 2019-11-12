/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: ShiroConfig
 * Author:   zyl
 * Date:     2018/12/26 13:41
 * Description: a
 * History:
 */
package com.jk.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 〈一句话功能简述〉<br> 
 * 〈a〉
 *
 * @author zyl
 * @create 2018/12/26
 * @since 1.0.0
 */
@Configuration //声明当前类是一个配置文件类 对应普通框架中的spring.xml文件
public class ShiroConfig {

    // 认证和授权的缓存处理器 用来管理认证授权之间的bean(对象)关系
    @Bean(name = "lifecycleBeanPostProcessor")//Bean 相当于配置文件中的bean标签
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * shiro 的过滤器链
     *       shiro的核心总入口
     *
     * */
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        System.out.println("授权");
        // shiro过滤器工厂
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 必须设置 SecurityManager 如果不设置就无法完成认证和授权
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 过滤器链
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

        // 配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
        // logout shiro定义好的过滤器名字 /logout访问路径
        // 浏览器访问的地址栏路径中以/logout结尾的路径 走logout过滤器
        // logout会清除session 退出登录
        filterChainDefinitionMap.put("/logout", "logout");
        // 所有的css文件走  anon过滤器 此过滤器代表放过拦截 不需要权限也能访问
        filterChainDefinitionMap.put("/css/**", "anon");
        // 放过登录页面拦截
        filterChainDefinitionMap.put("/toLogin", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        /// **代表所有路径 除以上路径外都拦截 authc代表权限拦截过滤器
        filterChainDefinitionMap.put("/**", "authc");
        // perms权限过滤器 必须拥有某项权限才能访问对应路径
        // filterChainDefinitionMap.put("/add", "perms[user:query]");
        // 登录请求路径 登录页面提交form表单时 表单的action写此路径
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功跳转到登录成功页面
        shiroFilterFactoryBean.setSuccessUrl("/index");
        // 未授权界面;
         //shiroFilterFactoryBean.setUnauthorizedUrl("/403.html");
         shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        // 最终返回过滤器链
        return shiroFilterFactoryBean;
    }

    @Bean // 在xml文件中配置一个bean标签 相当于<bean class="
    // org.apache.shiro.mgt.SecurityManager"
    // name="securityManager"></bean>
    public SecurityManager securityManager() {

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        // 设置realm. 域(数据源 用来连接数据库完成认证和授权)
        // 把自己创建的Realm 注入到securityManager中
        securityManager.setRealm(myShiroRealm());

        // 注入缓存管理器;
        //securityManager.setCacheManager(ehCacheManager());//
        // 这个如果执行多次，也是同样的一个对象;
        // securityManager.setRememberMeManager(rememberMeManager());

        return securityManager;

    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public UserEralm myShiroRealm(){
        UserEralm myShiroRealm = new UserEralm();
        //myShiroRealm.setCacheManager(ehCacheManager());
        return myShiroRealm;

    }
    //这是用来开启springaop切面
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    //shrio与thymeleaf 整合的方言设置
    @Bean
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }

    //切换aop的动态代理把jdk动态代理切 （基于接口的）换为  cglib动态代理  （基于类的动态代理）
    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }
    //开启ehCache 缓存，shiro整合ehCache，如果想整合redis需要其他配置
    @Bean
    public EhCacheManager ehCacheManager(){
        System.out.println("ShiroConfiguration.getEhCacheManager()");
        EhCacheManager cacheManager = new EhCacheManager();
        //cacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
        return cacheManager;
    }
    //跳转未授权页面的设置
    @Bean
    public SimpleMappingExceptionResolver resolver() {
        SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
        Properties properties = new Properties();
        properties.setProperty("org.apache.shiro.authz.UnauthorizedException", "/403");
        resolver.setExceptionMappings(properties);
        return resolver;
    }
}
