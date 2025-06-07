package com.ensup.nasstech;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.ensup.nasstech.entity.Commande;
import com.ensup.nasstech.entity.Marque;
import com.ensup.nasstech.entity.Ordinateur;
import com.ensup.nasstech.entity.Utilisateur;
import com.ensup.nasstech.outil.Outil;
import com.ensup.nasstech.repository.CommandeRepository;
import com.ensup.nasstech.repository.MarqueRepository;
import com.ensup.nasstech.repository.OrdinateurRepository;
import com.ensup.nasstech.repository.UtilisateurRepository;
import com.ensup.nasstech.service.EmailServiceImpl;

@SpringBootApplication
public class NassTechApplication {
	private  static OrdinateurRepository  ordinateurRepository  =  null;
	private static UtilisateurRepository utilisateurRepository = null;
	private static CommandeRepository commandeRepository = null;
	private static MarqueRepository marqueRepository = null;
	private static EmailServiceImpl emailService = null;
	public static void main(String[] args) {
		ApplicationContext  ctx  = SpringApplication.run(NassTechApplication.class, args);
		ordinateurRepository  =  ctx.getBean(OrdinateurRepository.class);
		utilisateurRepository = ctx.getBean(UtilisateurRepository.class);
		commandeRepository = ctx.getBean(CommandeRepository.class);
		marqueRepository = ctx.getBean(MarqueRepository.class);
		emailService = ctx.getBean(EmailServiceImpl.class);
		initialiser();
	}
	public  static  void  initialiser()  {
		
		Marque apple = new Marque("Apple");
        Marque packardBell = new Marque("Packard Bell");
        Marque huawei = new Marque("Huawei");
        Marque acer = new Marque("Acer");
        Marque hp = new Marque("HP");
        Marque lenovo = new Marque("Lenovo");
        Marque msi = new Marque("MSI");

        marqueRepository.save(apple);
        marqueRepository.save(packardBell);
        marqueRepository.save(huawei);
        marqueRepository.save(acer);
        marqueRepository.save(hp);
        marqueRepository.save(lenovo);
        marqueRepository.save(msi);
		
		 Ordinateur ordinateur1 = new Ordinateur("Ordinateur Apple MACBOOK CTO Pro 13 New M1 16 256 iGris sideral",
	                1549.99, "Puce Apple M1", 13.3, 16, "A.png",
	                "https://www.boulanger.com/ref/1156449", 5, apple);

	        Ordinateur ordinateur2 = new Ordinateur("PACKARD BELL PB314-35-P53X", 449,
	                "Intel Pentium Silver N6000", 14, 8, "B.png",
	                "https://www.boulanger.com/ref/1164052", 5, packardBell);

	        Ordinateur ordinateur3 = new Ordinateur("Matebook 14s 2021 I7 16Go 512 Touch", 1049,
	                "Intel Core i7 11370H", 14.2, 16, "C.png",
	                "https://www.boulanger.com/ref/1171445", 4, huawei);

	        Ordinateur ordinateur4 = new Ordinateur("ACER Aspire A317-52-54QM Noir", 699,
	                "Intel Core i7 11370H", 17.3, 8, "04.png",
	                "https://www.boulanger.com/ref/1148125", 8, acer);

	        Ordinateur ordinateur5 = new Ordinateur("ACER Aspire Vero AV15-51-78H5", 899,
	                "Intel Core i7 1195G7", 15.6, 16, "05.png",
	                "https://www.boulanger.com/ref/1170405", 9, acer);

	        Ordinateur ordinateur6 = new Ordinateur("PC Hybride HP ENVY x360 13-ay0034nf", 799,
	                "AMD Ryzen 5 4500U", 13.3, 8, "06.png",
	                "https://www.boulanger.com/ref/1163913", 6, hp);

	        Ordinateur ordinateur7 = new Ordinateur("PC Hybride LENOVO Flex 5 14ITL05-257", 936.17,
	                "Intel Core i5 1135G7", 14, 16, "07.png",
	                "https://www.boulanger.com/ref/1164775", 2, lenovo);

	        Ordinateur ordinateur8 = new Ordinateur("HP Envy 17-ch0039nf", 1499.39,
	                "Intel Core i7 1165G7", 17.3, 16, "08.png",
	                "https://www.boulanger.com/ref/1161111", 10, hp);

	        Ordinateur ordinateur9 = new Ordinateur("HP 17-cp0054", 449,
	                "AMD Athlon Silver 3050U", 17, 8, "09.png",
	                "https://www.boulanger.com/ref/1169654", 4, hp);

	        Ordinateur ordinateur10 = new Ordinateur("ACER Aspire A517-52G-757L", 1099,
	                "Intel Core i7 1165G7", 17.3, 16, "10.png",
	                "https://www.boulanger.com/ref/1164115", 7, acer);

	        Ordinateur ordinateur11 = new Ordinateur("PC Gamer MSI GS66 Stealth 11UG-289FR", 3199,
	                "Intel Core i7 11800H- 32 Go", 15.6, 32, "11.png",
	                "https://www.boulanger.com/ref/1164182", 5, msi);

	        Ordinateur ordinateur12 = new Ordinateur("ACER Swift SF514-55T-73TS Vert", 1199,
	                "Intel Core i7 11800H- 32 Go", 14, 16, "12.png",
	                "https://www.boulanger.com/ref/1164097", 2, acer);
		
		ordinateurRepository.save(ordinateur1);
		ordinateurRepository.save(ordinateur2);
		ordinateurRepository.save(ordinateur3);
		ordinateurRepository.save(ordinateur4);
		ordinateurRepository.save(ordinateur5);
		ordinateurRepository.save(ordinateur6);
		ordinateurRepository.save(ordinateur7);
		ordinateurRepository.save(ordinateur8);
		ordinateurRepository.save(ordinateur9);
		ordinateurRepository.save(ordinateur10);
		ordinateurRepository.save(ordinateur11);
		ordinateurRepository.save(ordinateur12);
		String hashPassword;
		Utilisateur utilisateur = null;
		try {
			hashPassword = Outil.hashMdpSha256("nass");
			utilisateur = new Utilisateur("nass", hashPassword, "nass@gmail.com", "abonne", "18 Avenue du Prado, 13008 Marseille");
			 utilisateur.setVerified(true);
			utilisateurRepository.save(utilisateur);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Impossible de créer l'utilisateur nass");
		}
		 try {
			 hashPassword = Outil.hashMdpSha256("admin");
			 utilisateur = new Utilisateur("admin", hashPassword, "admin@gmail.com", "administrateur", "1 rue de la République, 13002 Marseille");
			 utilisateur.setVerified(true);
			 utilisateurRepository.save(utilisateur);
		} catch (NoSuchAlgorithmException e) {
			 System.out.println("Impossible de créer l'utilisateur admin");
		}
	/*	Commande commande = new Commande(ordinateur1, new Date()); 
		commandeRepository.save(commande);
		utilisateur.commanderOrdinateur(commande);
		utilisateurRepository.save(utilisateur); */
		
	//	 emailService.sendSimpleMessage("zoubeirnassim@gmail.com", "Email Test NassTech", "Bonjour Nassim ce mail vient de ton application NassTech");
	}
	
}
