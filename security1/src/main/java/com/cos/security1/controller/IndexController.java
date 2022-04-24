package com.cos.security1.controller;

import com.cos.security1.Repository.UserRepository;
import com.cos.security1.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @GetMapping({"","/"})
    public @ResponseBody String index(){
        //머스테치 : 템플릿! => 스피링이 권장함
        // 머스테치 기본 폴더: src/main/resources/
        // 뷰리졸버 설정 : template (prefix), .mustache(suffix) => 이미 의존성 해줬기 때문에 yml 파일 설정하지 않아도 된다.
        return "index"; // src/main/resources/templates/index.mustache 를 찾게 됨
    }

    @GetMapping("/user")
    public @ResponseBody String user(){
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
        user.setRole("ROLE_USER");
        userRepository.save(user);
        return "redirect:/loginForm"; // rediect가 붙으면 loginform함수실행
    }
}
