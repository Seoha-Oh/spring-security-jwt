package com.cos.security1.config.auth;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인을 진행이 완료가 되면 시큐리티 session을 만들어준다. (Securiry ContextHolder)에 시큐리티만의 세션을 만들어 저장
// 세션에 저장되는 객체는 저장되어있다. 오브젝트 =Authentication 타입객체
// Authenticaton 안에 user 정보가 있어야함.
// User 오브젝트 타입 -> UserDatauls 타입객체

import com.cos.security1.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

//Security Session -> Authentication -> UserDetails
public class PrincipalDetails implements UserDetails {

    private User user;

    public PrincipalDetails(User user){
        this.user = user;
    }

    //해당 유저의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collet = new ArrayList<GrantedAuthority>();
        collet.add(()->{ return user.getRole();});
        return collet;
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
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        //우리 사이트에서 1년동안 회원이 로그인을 안하면, 휴면 계정으로 바뀌어야함.
        //현재 시간- 로긴시간 -> 1년을 초과하면 return false;
        return true;
    }

}
