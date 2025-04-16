package io.gffd94.security_form.view;

import io.gffd94.security_form.app.MemberService;
import io.gffd94.security_form.dao.MemberRepository;
import io.gffd94.security_form.dto.SignUpForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ViewController {

    private final MemberService memberService;

    @GetMapping("/signup")
    public String showSingupForm(){
        return "sign_up";
    }

    @PostMapping("/signup")
    public String doSignup(SignUpForm signUpForm) {
        memberService.save(signUpForm);
        log.info("signUpForm = {}", signUpForm);
        // redirect:/ -> signup이 끝나면 redirect:/ 다음 주소로 페이지를 옮김
        // 일반적으로 클라이언트가 특정 URL요청을 보내면 서버는 새로운 URL로 이동하도록 응답 이럴 때 사용
        return "redirect:/";
    }

    @GetMapping("/signin")
    public String showSignInForm(){
        return "sign_in";
    }

}
