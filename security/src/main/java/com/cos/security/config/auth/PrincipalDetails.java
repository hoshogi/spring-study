package com.cos.security.config.auth;

import com.cos.security.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다
// 로그인을 진행이 완료가 되면 시큐리티 session을 만들어준다 (Security ContextHolder -> 이 키값에다가 세션 정보를 저장)
// Security Session에 들어갈 수 있는 오브젝트 -> Authentication 타입의 객체
// Authentication 안에 user 정보가 있어야됨.
// User 오브젝트타입 -> UserDetails 타입 객체

// Security Session -> Authentication -> UserDetails(PrinciaplDetails)

// Authentication 객체에 저장할 수 있는 유일한 타입

public class PrincipalDetails implements UserDetails {

    private User user; // 콤포지션

    public PrincipalDetails(User user) {
        this.user = user;
    }

    // 해당 User의 권한을 리턴하는 곳!
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { // 비밀번호 오래 사용했니?
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 우리 사이트에서 1년동안 회원이 로그인을 안하면 휴면 계정으로 하기로 함.
        // (현재시간 - 로그인시간)이 1년을 초과하면 false로 설정
        return true;
    }
}
