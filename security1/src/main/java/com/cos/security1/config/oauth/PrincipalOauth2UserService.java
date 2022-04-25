package com.cos.security1.config.oauth;

import com.cos.security1.Repository.UserRepository;
import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    //구글로 부터 받은 userrequest 데이터에 대한 후처리되는 함수
    // 함수 종료 시 @AuthenticationPrincipal 어노테이션이 만들어진다.

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //registrationId로 어떤 oauth로 로그인되었는가
        System.out.println("getClientRegistration : " + userRequest.getClientRegistration());
        System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        //구글 로그인 버튼 클릭 -> 구글로그인창 -> 로그인완료 -> code를 리턴하나 oauth-client가 받음 -> 엑세스 토큰 요청
        //userRequest 정보 -> loaduser 함수 호출 -> 구글로 부터 회원프로필 획득
        System.out.println("getAttributes : " + oAuth2User.getAttributes());

        // 강제로 회원가입을 진행해볼 예정
        String provider = userRequest.getClientRegistration().getClientId();
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider + "_" +providerId;
        String password = bCryptPasswordEncoder.encode("겟인데어");
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        User user = userRepository.findByUsername(username);

        if (user == null){
            System.out.println("구글 로그인이 최초입니다.");
            user = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(user);
        }else {
            System.out.println("구글 로그인을 이미 한적이있습니다. 당신은 자동 회원가입이 되어 있습니다.");
        }
        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
