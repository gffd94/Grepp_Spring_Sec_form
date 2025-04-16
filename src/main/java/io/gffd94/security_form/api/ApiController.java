package io.gffd94.security_form.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping("/users")
    public String users() {
        return "User!";
    }

    @GetMapping("/manger")
    public String manager() {
        return "Manager!";
    }
    @GetMapping("/admin")
    public String admin() {
        return "Admin!";
    }

}
