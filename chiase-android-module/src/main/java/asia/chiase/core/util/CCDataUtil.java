package asia.chiase.core.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import asia.chiase.core.exception.CCException;

public class CCDataUtil{

	private static int	TOKEN_LENGTH	= 16;	// 16*2=32byte

	public static String makeToken(){

		byte token[] = new byte[TOKEN_LENGTH];
		StringBuffer buf = new StringBuffer();
		SecureRandom random = null;

		try{
			random = SecureRandom.getInstance("SHA1PRNG");
			random.nextBytes(token);

			for(int i = 0; i < token.length; i++){
				buf.append(String.format("%02x", token[i]));
			}

		}catch(Exception ex){
			new CCException(ex);
			return "";
		}

		return buf.toString();

	}

	public static String makeHash(Integer baseKey){

		String key = CCStringUtil.toString(baseKey);
		if(CCStringUtil.isEmpty(key)){
			return "";
		}else{
			MessageDigest md;
			try{
				md = MessageDigest.getInstance("SHA");
			}catch(Exception ex){
				new CCException(ex);
				return "";
			}

			// ハッシュ値を計算
			md.update(key.getBytes());
			byte[] digest = md.digest();

			// 16進数文字列に変換
			StringBuffer buffer = new StringBuffer();
			for(int i = 0; i < digest.length; i++){
				String tmp = Integer.toHexString(digest[i] & 0xff);
				if(tmp.length() == 1){
					buffer.append('0').append(tmp);
				}else{
					buffer.append(tmp);
				}
			}

			return buffer.toString();
		}
	}

	public static String generateActiveCode(){
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	public static Boolean matches256(String str, String encodeString){
		if(PasswordEncoder.sha256(str).equals(encodeString)){
			return true;
		}
		return false;
	}

	public static Boolean matches512(String str, String encodeString){
		if(PasswordEncoder.sha512(str).equals(encodeString)){
			return true;
		}
		return false;
	}

	public static String generatePassword(){
		return RandomStringUtils.randomAlphanumeric(10);
	}

	//
	// public static String generateToken(Integer length){
	// return RandomStringUtils.randomAlphabetic(length);
	// }
	//
	public static String endcode256(String normalString){
		return PasswordEncoder.sha256(normalString);
	}

	public static String endcode512(String normalString){
		return PasswordEncoder.sha512(normalString);
	}

	//
	// public static Boolean matches(String str, String endcodeString){
	// return endcoder.matches(str, endcodeString);
	// }

}