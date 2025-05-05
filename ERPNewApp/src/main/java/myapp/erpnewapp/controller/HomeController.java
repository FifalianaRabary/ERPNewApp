package myapp.erpnewapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import myapp.erpnewapp.model.ErpNextSessionInfo;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        ErpNextSessionInfo info = (ErpNextSessionInfo) session.getAttribute("info");
        if (info == null) return "redirect:/login";

        model.addAttribute("username", info.getFullName());
        return "home";
    }
}