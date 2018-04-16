package finanalyst.controller;

import finanalyst.model.User;
import finanalyst.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class FinAnalystController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        model.addAttribute(user);
        return "/dashboard";
    }

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
        User existingUser = userService.findUserByUsername(user.getUsername());

        if (existingUser == null) {
            userService.createUser(user);
            model.addAttribute(user);
            return "/dashboard";
        }

        return "/registration";
    }

    @GetMapping("/settings")
    public String settings(User user, Model model) {

        model.addAttribute(user);
        return "/settings";
    }

    @PutMapping("/settings")
    public String updateUser(User user, Model model) {

        // update user
        if (userService.updateUser(user)) {
            System.out.println("password changed successfully");
        }

        return "/dashboard";
    }

    @DeleteMapping("/settings")
    public String deleteUser(Model model) {

        // delete user
        if (userService.deleteUser()) {
            System.out.println("user deleted successfully");
        }

        return "/login";
    }

    @GetMapping("/admin/cpanel")
    public String cpanel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        model.addAttribute(user);
        return "admin/cpanel";
    }

    @GetMapping("/403")
    public String error403() {
        return "/403";
    }

}
