import java.util.Base64.Encoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

//arriva psw, genero sale, faccio (psw+sale) hasho coppia [psw,sale] salvo in db1 (sale,user) indb2 (hash, user)
public class HashingPassword {
	
	public static byte[] saltGeneration() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		/*
	    Encoder encoder = Base64.getUrlEncoder().withoutPadding();
	    String token = encoder.encodeToString(salt);
		System.out.println("password0-"+token);
		*/
		return salt;
	}

	public static byte[] hashGeneration(byte[] psw, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		//psw chiara
		String str = new String(psw);
		//System.out.println("password-"+str);
		
		//sale chiaro
	    Encoder encoder = Base64.getUrlEncoder().withoutPadding();
	    String token = encoder.encodeToString(salt);

		str = str.concat(token);		
		
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(str.getBytes());
	    token = encoder.encodeToString(hash);
	
		return hash;
	}

}