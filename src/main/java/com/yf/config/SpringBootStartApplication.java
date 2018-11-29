package com.yf.config;

import com.yf.App;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 按照pom文件的配置，需要配置一个SpringBootStartApplication的类来启动这个war包，
 * 因为我们在pom文件中没有使用内嵌的tomcat容器
 */
public class SpringBootStartApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        //这里要指向原先用main方法只想的application启动类
        return builder.sources(App.class);
    }
}
