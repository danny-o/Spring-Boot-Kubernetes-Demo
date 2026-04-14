package com.digitalskies.kubernetes_demo.auth;

import com.digitalskies.kubernetes_demo.auth.dto.AuthRequest;
import com.digitalskies.kubernetes_demo.auth.dto.RegisterResponse;
import com.digitalskies.kubernetes_demo.auth.dto.TokenPair;
import com.digitalskies.kubernetes_demo.auth.dto.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RequestMapping("/auth")
@Controller
public class AuthController {

    AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    Logger logger = Logger.getLogger(AuthController.class.getName());

    @GetMapping("/login")
    public String login(ModelMap modelMap) {
        User user = new User();

        modelMap.put("user",user);
        return "login";
    }

    @GetMapping("/register")
    public String register(ModelMap modelMap) {
        User user = new User();

        modelMap.put("user",user);
        return "register";
    }


    @PostMapping(path = "/register")
    String register(@Valid User user, BindingResult result, Model model){

        if (result.hasErrors()) {
            logger.info("Model: "+model);
            return "register";
        }

        logger.info("Registering user: "+user);

        authService.register(user);
        return "redirect:/login";
    }

    @PostMapping(path = "/login")
    String login(@Valid User user,BindingResult result, Model model, HttpServletResponse response) {
        logger.info("Logging in user: "+user);
        TokenPair tokenPair=authService.login(user.getEmail(),user.getHashedPassword());

        if(tokenPair==null){
            return "redirect:/auth/login?error=Invalid credentials";
        }

        Cookie cookie = new Cookie("accessToken", tokenPair.accessToken());
        cookie.setHttpOnly(true);   // Prevents JS access
        cookie.setSecure(true);     // Only sends over HTTPS (use false for local dev without SSL)
        cookie.setPath("/");        // Available for all routes
        cookie.setMaxAge(3600);     // 1 hour expiry (match your JWT expiry)

        response.addCookie(cookie);

        return "redirect:/";
    }

    @RequestMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("accessToken", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // Deletes the cookie
        response.addCookie(cookie);
        return "redirect:/auth/login?logout";
    }

    void refreshToken(){

    }
}
