import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class CookieEncryption {
    public static String encryptCookie(String cookie, SecretKey secretKey) throws Exception {
        // Genera una chiave di 16 byte (128 bit) dalla chiave fornita
        //SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

        // Genera un vettore di inizializzazione casuale di 16 byte
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        // Crea un oggetto Cipher con l'algoritmo AES/CBC/PKCS5Padding
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // Inizializza il cifratore con la chiave e il vettore di inizializzazione
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

        // Cifra il cookie
        byte[] encryptedBytes = cipher.doFinal(cookie.getBytes(StandardCharsets.UTF_8));

        // Combina il vettore di inizializzazione con i dati cifrati
        byte[] combinedBytes = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, combinedBytes, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, combinedBytes, iv.length, encryptedBytes.length);

        // Codifica i dati combinati in Base64
        String encodedData = Base64.getEncoder().encodeToString(combinedBytes);

        return encodedData;
    }
    
    public static String decryptCookie(String encodedData, SecretKey  secretKey) throws Exception {
        // Decodifica i dati Base64
        byte[] combinedBytes = Base64.getDecoder().decode(encodedData);

        // Estrapola il vettore di inizializzazione e i dati cifrati
        byte[] iv = new byte[16];
        byte[] encryptedBytes = new byte[combinedBytes.length - iv.length];
        System.arraycopy(combinedBytes, 0, iv, 0, iv.length);
        System.arraycopy(combinedBytes, iv.length, encryptedBytes, 0, encryptedBytes.length);

        // Genera una chiave di 16 byte (128 bit) dalla chiave fornita
        //SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

        // Crea un oggetto Cipher con l'algoritmo AES/CBC/PKCS5Padding
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // Inizializza il cifratore con la chiave e il vettore di inizializzazione
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

        // Decifra i dati cifrati
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        // Converti i dati decifrati in una stringa
        String decryptedCookie = new String(decryptedBytes, StandardCharsets.UTF_8);

        return decryptedCookie;
    }

    public static SecretKey generateRandomKey() throws NoSuchAlgorithmException {
        // Genera una chiave AES casuale di 128 bit
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128); // Imposta la dimensione della chiave a 128 bit
        return keyGenerator.generateKey();
    }

    public static boolean storeKey(String ec,SecretKey k) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException  {
    	
    	KeyStore keyStore = KeyStore.getInstance("JCEKS"); // JCEKS è un tipo comune di keystore per chiavi non certificate
    	char[] keystorePassword = "Q1aW2sE3dR4fT5gY6".toCharArray();
    	// Carica il keystore esistente se presente

        try (InputStream keystoreInputStream = new FileInputStream("C://Key/keystore.jceks")) {
            keyStore.load(keystoreInputStream, keystorePassword);
        } catch (FileNotFoundException ex) {
            // Il file non esiste ancora, crealo
            keyStore.load(null, keystorePassword);
        }

    	// Imposta la chiave nel keystore
    	KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(k);
    	KeyStore.ProtectionParameter protectionParameter = new KeyStore.PasswordProtection("Q1aW2sE3dR4fT5gY6".toCharArray());
    	keyStore.setEntry(ec, secretKeyEntry, protectionParameter);

    	// Salva il keystore su disco
    	try (OutputStream keystoreOutputStream = new FileOutputStream("C://Key/keystore.jceks")) {
    	    keyStore.store(keystoreOutputStream, keystorePassword);
    	    return true;
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	 return false;
    }

    public static SecretKey getKey(String ec) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException  {
    	System.out.print(ec);
    	KeyStore keyStore = KeyStore.getInstance("JCEKS");
    	char[] keystorePassword = "Q1aW2sE3dR4fT5gY6".toCharArray();
    	try (InputStream keystoreInputStream = new FileInputStream("C://Key/keystore.jceks")) {
    	    keyStore.load(keystoreInputStream, keystorePassword);

    	    KeyStore.ProtectionParameter protectionParameter = new KeyStore.PasswordProtection("Q1aW2sE3dR4fT5gY6".toCharArray());
    	    KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(ec, protectionParameter);
    	     
    	    SecretKey secretKey = secretKeyEntry.getSecretKey();
    	    return secretKey;
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	 return null;
    }
}
