package com.cos.jwt.config;

import com.cos.jwt.config.jwt.JwtAuthenticationFilter;
import com.cos.jwt.config.jwt.JwtAuthorizationFilter;
import com.cos.jwt.filter.MyFilter1;
import com.cos.jwt.filter.MyFilter3;
import com.cos.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity // Security 활성화 -> 기본 스프링 필터 체인에 등록
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;
    private final UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new MyFilter3(), SecurityContextPersistenceFilter.class);
        http
                    // 모든 요청은 이 filter를 거친다, CORS 정책에서 벗어날 수 있다
                    // 인증 X -> @CrossOrigin, 인증 O -> Security Filter에 등록
                    .addFilter(corsFilter)
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않겠다
                .and()
                    .formLogin().disable() // form 로그인 방식 안쓴다
                    .httpBasic().disable() // 기본적인 http 로그인 방식 아예 안쓴다
                    .addFilter(new JwtAuthenticationFilter(authenticationManager())) // AuthenticationManager -> WebSecurityAdapter가 들고 있음
                    .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository)) // AuthenticationManager -> WebSecurityAdapter가 들고 있음
                    .authorizeRequests()
                    .antMatchers("/api/v1/user/**")
                        .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                    .antMatchers("/api/v1/manager/**")
                        .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                    .antMatchers("/api/v1/admin/**")
                        .access("hasRole('ROLE_ADMIN')")
                    .anyRequest().permitAll(); // 이외의 요청은 권한없이 들어갈 수 있다.
    }
}