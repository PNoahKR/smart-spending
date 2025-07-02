package com.smartspending.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // templates/login.html
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup"; // templates/signup.html
    }

    @GetMapping("/")
    public String indexPage() {
        return "index"; // templates/index.html
    }

    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "dashboard"; // templates/dashboard.html
    }

    @GetMapping("/oauth2/success")
    public String oauth2Success() {
        return "oauth2-success"; // => templates/oauth2-success.html
    }

    @GetMapping("/find-password")
    public String findPassword() {
        return "find-password";
    }

    @GetMapping("/transaction")
    public String transactionPage() {
        return "transaction";
    }

    @GetMapping("/budget")
    public String budgetPage() {
        return "budget";
    }
}
