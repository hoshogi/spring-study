package com.cos.security.controller;

import com.cos.security.model.User;
import com.cos.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // View를 리턴하겠다
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // localhost:8080/
    // localhost:8080
    @GetMapping({"", "/"})
    public String index() {
        // mustache 기본 폴더: src/main/resources/
        // ViewResolver 설정: templates (prefix), .mustache (suffix) -> 생략 가능!
        return "index"; // src/main/resources/templates/index.mustache -> 이것을 찾는다.
    }

    @GetMapping("/user")
    public @ResponseBody String user() {
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    // 스프링시큐리티가 해당 주소를 낚아채버린다! - SecurityConfig 파일 생성 후 작동 안함
    @GetMapping("/loginForm")
    public String loginFrom() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        System.out.println(user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);

        // 회원가입 잘 됨.
        // but 비밀번호 : 1234 -> 시큐리티 로그인을 할 수 없음.
        // 이유는 패스워드가 암호화가 안되었기 때문에
        userRepository.save(user);
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN") // 특정 메서드에 간단하게 걸고 싶을때!
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    @PreAuthorize("hasROLE('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // data라는 메서드가 실행되기 직전에 실행된다
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터정보";
    }
}
