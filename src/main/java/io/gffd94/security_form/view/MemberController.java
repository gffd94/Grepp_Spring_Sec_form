package io.gffd94.security_form.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {

    @GetMapping("/singup")
    public String showSingupForm(){
        return "sign_up";
    }

    @GetMapping("/signin")
    public String showSignInForm(){
        return "sign_in";
    }

}
