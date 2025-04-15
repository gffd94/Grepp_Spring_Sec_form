package io.gffd94.security_form.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration // Spring 설정의 클래스 ( Bean들을 정의함 )
public class SecurityConfig {

    @Bean // Bcrypt 방식으로 암호화해줌 Spring Security에서 비밀번호 비교 시 passwordEncoder가 실행
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean // 새로운 암호화 알고리즘을 등록해줌 ( 실무에서 DelegatingPasswordEncoder 많이 씀 )
//    public PasswordEncoder passwordEncoder2() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // CSRF 보호르 끔, REST API개발이나 테스트시 주로 끔
                .csrf(csrf -> csrf.disable())
                // 로그인 폼
//                .formLogin(form -> { // 직접 커스터마이징한 로그인 설정방식
//                    form.failureForwardUrl("/login?error=true") // 주소창을 안바꾸고 실패 페이지로 url의 메세지를 보여주고 싶을때
//                            .successForwardUrl("/") // 로그인 성공시 page는 변경하는데 주소url값은 그대로
////                            .defaultSuccessUrl("/happy",true) // 로그인 성공시 해당 url로 명확하게 이동하고 싶을 때
//                            .usernameParameter("loginId") // parameter name = "loginId"로 설정한 값을 가져옴
//                            .passwordParameter("loginPwd") // parameter name = "loginPwd"로 설정한 값을 가져옴
//                            .loginProcessingUrl("/signin") // post방식
//                            .loginPage("/signin")
//                            .permitAll(); // login 실행
//                })
                .formLogin(Customizer.withDefaults()) // 기본 로그인 방식
//                .logout(logout -> { // 커스터마이징한 로그아웃 방식
//                    logout.logoutUrl("/signout") // 해당 url을 입력하면 로그아웃
//                            .logoutSuccessUrl("/")
//                            .clearAuthentication(true)
//                            .invalidateHttpSession(true)
//                            .deleteCookies("JSESSIONID"); // 쿠키와 세션을 지워서 로그아웃 시키기
//                })

                .logout(Customizer.withDefaults()) // 기본 로그아웃 방식

//                .cors(cors -> cors.disable())
                // 인가부분
                .authorizeHttpRequests(
                        auth -> {
                            // 내부에 URL을 줘서 인가검사를 어떻게 할것인지
                            auth.requestMatchers("/signup", "/signin")
                                    // 로그인 하지 않는 사용자만 접근 가능 anonymous
                                    .anonymous()
                                    // /user/*로 시작하는 모든 요청은 Role_MEMBER 권한이 있어야한다.
                                    .requestMatchers("/users/**")
                                    .hasRole("MEMBER")
                                    // 그 외 요청은 로그인만 되어있으면 OK
                                    .anyRequest()
                                    .authenticated();
//                                    .denyAll();
                        }

                )
                .build();
    }

        // 사용자 등록
    @Bean
    public UserDetailsService userDetailsService() {
        // InMemory 방식 : 메모리 내에 사용자 정보를 저장하는 객체
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        String targetPwd = "1234";
        String encode = passwordEncoder().encode(targetPwd);

        log.info("encode = {}", encode);

        manager.createUser(
                // user 라는 이름으로 비밀번호는 BCrypt방식으로 저장
                // 사용자에게 role을 지정하기 않았기 떄문에 /user/** 요청은 거절됨
                // withUsername(...).password(...).roles("MEMBER")로 바꾸면 권한 부여 가능
                User.withUsername("user")
                        .password(encode)
                        .build()
        );

        return manager;
    }



}
