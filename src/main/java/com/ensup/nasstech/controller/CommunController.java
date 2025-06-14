package com.ensup.nasstech.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CommunController {

	@RequestMapping("/")
	public  String  entree()  {
		System.out.println("==== / ====");
		return  "redirect:/accueil";
	}
	  // CGU
    @RequestMapping("/cgu")
    public String afficherCGU() {
        return "cgu";
    }    
    @RequestMapping("/cgv")
    public String afficherCGV() {
        return "cgv";
    }
	@RequestMapping("/mentions-legales")
	public String mentionsLegales() {
		return "mentions-legales";
	}
	@RequestMapping("/cookies")
	public String cookies() {
		return "cookies";
	}
	@RequestMapping("/donnees-personnelles")
	public String donneesPersonnelles() {
		return "donnees-personnelles";
	}
}
