package com.cos.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터(SecurityConfig)가 스프링 필터 체인에 등록이 된다
// securedEnabled = true -> Secured 어노테이션 활성화(특정 주소 접근시 권한 및 인증을 위한 어노테이션 활성화)
// prePostEnabled = true -> preAuthorize, postAuthorize 어노테이션 활성화
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated() // 인증만 되면 들어갈 수 있는 주소!
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')") // 권한이 필요하다
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll() // 나머지 주소는 모두 허에
            .and()
                .formLogin()
                .loginPage("/loginForm")
//                .usernameParameter("username2") // username이 다른 경우
                .loginProcessingUrl("/login") // /login 주소가 호출이 되면 시큐리티가 낚아채서 로그인을 진행해준다
                .defaultSuccessUrl("/"); // 로그인되면 원래 있던 주소로!
    }
}
