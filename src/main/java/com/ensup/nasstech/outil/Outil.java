package com.ensup.nasstech.outil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Outil {
	public static String hashMdpSha256(String password) throws NoSuchAlgorithmException  {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(password.getBytes());
		byte byteData[] = md.digest();

		 //convertir le tableau de bits en une format hexadécimal - méthode 1
		 StringBuffer sb = new StringBuffer();
		 for (int i = 0; i < byteData.length; i++) {
			 sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		 }

		System.out.println("En format hexa : " + sb.toString());
		return sb.toString();
	}
}
