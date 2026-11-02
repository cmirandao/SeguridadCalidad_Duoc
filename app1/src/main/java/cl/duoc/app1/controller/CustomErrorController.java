package cl.duoc.app1.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        
        if (statusCode != null) {
            if (statusCode == 401 || statusCode == 403) {
                redirectAttributes.addFlashAttribute("alertaPopup", "Debes iniciar sesión para acceder a este contenido.");
                return "redirect:/login"; 
            } else if (statusCode == 404) {
                model.addAttribute("alertaPopup", "La página o evento que buscas no existe.");
                return "error";
            }
        }
        
        model.addAttribute("alertaPopup", "Ha ocurrido un error inesperado.");
        return "error"; 
    }
}