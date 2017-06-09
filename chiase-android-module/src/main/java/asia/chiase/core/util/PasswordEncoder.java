package asia.chiase.core.util;

import java.security.NoSuchAlgorithmException;

public class PasswordEncoder{

	public static String sha256(String pass){
		StringBuilder sb = new StringBuilder();
		try{
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");

			md.update(pass.getBytes());
			byte[] mb = md.digest();
			for(byte m : mb){
				String hex = String.format("%02x", m);
				sb.append(hex);
			}
		}catch(NoSuchAlgorithmException ex){
			ex.printStackTrace();
			return null;
		}
		return sb.toString();
	}

	public static String sha512(String pass){
		StringBuilder sb = new StringBuilder();
		try{
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-512");

			md.update(pass.getBytes());
			byte[] mb = md.digest();
			for(byte m : mb){
				String hex = String.format("%02x", m);
				sb.append(hex);
			}
		}catch(NoSuchAlgorithmException ex){
			ex.printStackTrace();
			return null;
		}
		return sb.toString();
	}
}
