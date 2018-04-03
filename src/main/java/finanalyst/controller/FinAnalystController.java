package finanalyst.controller;

import finanalyst.model.User;
import finanalyst.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FinAnalystController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "/registration";
    }

    @PostMapping("/registration")
    public String createNewUser(User user, Model model) {
        User existingUser = userService.findUserByEmail(user.getEmail());

        if (existingUser == null) {
            userService.saveUser(user);
            model.addAttribute(user);
            return "/dashboard";
        }

        return "/registration";
    }

    @GetMapping("/")
    public String index(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        model.addAttribute(user);
        return "/dashboard";
    }

    @GetMapping("/admin/cpanel")
    public String cpanel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        model.addAttribute(user);
        return "admin/cpanel";
    }

    @GetMapping("/403")
    public String error403() {
        return "/403";
    }

}
