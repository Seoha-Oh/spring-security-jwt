package com.cos.security1.controller;

import com.cos.security1.Repository.UserRepository;
import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication,
                                          @AuthenticationPrincipal PrincipalDetails userDetails){ // DI(의존성 주입)
        System.out.println("/test/login ================");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication : " + principalDetails.getUser());

        System.out.println("UserDetail : " + userDetails.getUser());
        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOauthLogin(Authentication authentication,
                                               @AuthenticationPrincipal OAuth2User oauth){ // DI(의존성 주입)
        System.out.println("/test/oauth/login ================");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication : " + oAuth2User.getAttributes());
        System.out.println("oauth2User :" + oauth.getAttributes());
        return "oauth 세션 정보 확인하기";
    }

    @GetMapping({"","/"})
    public @ResponseBody String index(){
        //머스테치 : 템플릿! => 스피링이 권장함
        // 머스테치 기본 폴더: src/main/resources/
        // 뷰리졸버 설정 : template (prefix), .mustache(suffix) => 이미 의존성 해줬기 때문에 yml 파일 설정하지 않아도 된다.
        return "index"; // src/main/resources/templates/index.mustache 를 찾게 됨
    }

    //oauth 로그인을 해도 principaldetail로 받을 수 있다.
    //일반 로그인을 해도 principaldetail로 받을 수 있다.
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails)
    {
        System.out.println("principaleDetails :" + principalDetails.getUser());
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }

    // 스프링시큐리티 해당주소를 낚아채버림 - securityconfig 파일 생성 후 작동안함
    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user){
        System.out.println(user);
//        user.setRole("ROLE_USER");
//        userRepository.save(user); // 회원가입 잘됨. 비밀번호 1234 -> 시큐리티로 로그인할 수 없음. 이유는 패스워드가 암호화가 안되어있기 때문!

        //암호화가 되어있어서 시큐리티 로그인이 된다.
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user);
        return "redirect:/loginForm"; // rediect가 붙으면 loginform함수실행
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") //함수가 실행 직전에 실행되서, 권한 먼저 확인하는 것!
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "데이터정보";
    }
}
