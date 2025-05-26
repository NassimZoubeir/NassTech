package com.ensup.nasstech.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.ensup.nasstech.service.EmailServiceImpl;


@Controller
public  class  ContactController  {
	
	  @Autowired
	    private EmailServiceImpl emailService;

	    @PostMapping("/contact")
	    public String envoyerMessage(
	            @RequestParam String nom,
	            @RequestParam String email,
	            @RequestParam String message,
	            RedirectAttributes redirectAttributes
	    ) {
	        // Contenu de l'email
	        String subject = "Message de contact - NassTech";
	        String content = "Nom : " + nom + "\nEmail : " + email + "\n\nMessage :\n" + message;

	        // Envoi à ta propre adresse
	        emailService.sendSimpleMessage("zoubeirnassim@gmail.com", subject, content);

	        redirectAttributes.addFlashAttribute("successMessage", "Merci pour votre message ! Nous vous répondrons bientôt.");
	        return "accueil";
	    }

}
