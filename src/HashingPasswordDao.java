import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Encoder;

public class HashingPasswordDao {
	
	public static boolean hashSaving(byte[] salt, byte[] hash, String user) throws NoSuchAlgorithmException, InvalidKeySpecException {
		//salvataggio hash in db1 e sale in db2
		boolean status = false;
		try {
			// inizializza il driver per comunicare con il db
			Class.forName("com.mysql.cj.jdbc.Driver");
			// stringa di connessione: indirizzo - porta - nome db
			String url = "jdbc:mysql://127.0.0.1:3306/test";
			// oggetto connessione al db tramite inserimento di credenziali: stringa di connessione - nome utente - password
			Connection con = DriverManager.getConnection (url, "root", "root");
			
			//prendo chiave user
			PreparedStatement ps = con.prepareStatement("SELECT * FROM client where name=?");
			ps.setString(1, user);
			ResultSet rs = ps.executeQuery();
			status = rs.next();				
			int clientId = rs.getInt("indexClient");
			
			//salvo salt
			// oggetto prepared statement che consente di eseguire una query al db...
			ps = con.prepareStatement("INSERT INTO salt(idClient, salt) values(?,?)");
			// ... a partire dal nome e ...
			ps.setInt(1, clientId);
			// ... password date in input dall'utente alla jsp, dalla jsp alla servlet e dalla servlet al DAO
			ps.setBytes(2, salt);
			// esegue effettivamente la query ed ottiene un oggetto ResultSet che contiene la risposta del db
			ps.executeUpdate();
			// il next() prende la prima riga del risultato della query
			
			//salvo hash
			// oggetto prepared statement che consente di eseguire una query al db...
			ps = con.prepareStatement("INSERT INTO hash(idClient, hash) values(?,?)");
			// ... a partire dal nome e ...
			ps.setInt(1, clientId);
			// ... password date in input dall'utente alla jsp, dalla jsp alla servlet e dalla servlet al DAO
			ps.setBytes(2, hash);
			// esegue effettivamente la query ed ottiene un oggetto ResultSet che contiene la risposta del db
			ps.executeUpdate();
			// il next() prende la prima riga del risultato della query
			// restituisce true se c'è almeno una riga altrimenti false
			status = rs.next();	
			//
			} catch (Exception e) {
				e.printStackTrace();
			}
		return status;
		}

	public static byte[] saltRecovery(String user, byte[] psw) throws NoSuchAlgorithmException, InvalidKeySpecException {
		//presa password di login per utente, recupero sale associato dal db1, faccio hash e confronto con hash di stesso utente da db2
		byte[] salt = new byte[16];
		try {
			// inizializza il driver per comunicare con il db
			Class.forName("com.mysql.cj.jdbc.Driver");
			// stringa di connessione: indirizzo - porta - nome db
			String url = "jdbc:mysql://127.0.0.1:3306/test";
			// oggetto connessione al db tramite inserimento di credenziali: stringa di connessione - nome utente - password
			Connection con = DriverManager.getConnection (url, "root", "root");
			
			//prendo chiave user
			PreparedStatement ps = con.prepareStatement("SELECT * FROM client where name=?");
			ps.setString(1, user);
			ResultSet rs = ps.executeQuery();
			rs.next();
			int clientId = rs.getInt("indexClient");

			// oggetto prepared statement che consente di eseguire una query al db...
			ps = con.prepareStatement("SELECT * FROM salt where idClient=?");
			// ... a partire dal nome e ...
			ps.setInt(1, clientId);
			// esegue effettivamente la query ed ottiene un oggetto ResultSet che contiene la risposta del db
			rs = ps.executeQuery();
			// il next() prende la prima riga del risultato della query
			// restituisce true se c'è almeno una riga altrimenti false
			rs.next();				
			salt = rs.getBytes("salt");				
			// svuoto la password
			//Arrays.fill(pass, (byte)0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		//ora che ho il sale associato all utente, ci ricalcolo hash con psw inserita in login e confronto con ciò che ho memorizzato in db
		return salt;
		}
	
	public static byte[] hashRecovery(String user, byte[] psw) throws NoSuchAlgorithmException, InvalidKeySpecException {
		//presa password di login per utente, recupero sale associato dal db1, faccio hash e confronto con hash di stesso utente da db2
		byte[] hash = new byte[16];
		try {
			// inizializza il driver per comunicare con il db
			Class.forName("com.mysql.cj.jdbc.Driver");
			// stringa di connessione: indirizzo - porta - nome db
			String url = "jdbc:mysql://127.0.0.1:3306/test";
			// oggetto connessione al db tramite inserimento di credenziali: stringa di connessione - nome utente - password
			Connection con = DriverManager.getConnection (url, "root", "root");
			
			//prendo chiave user
			PreparedStatement ps = con.prepareStatement("SELECT * FROM client where name=?");
			ps.setString(1, user);
			ResultSet rs = ps.executeQuery();
			rs.next();				
			int clientId = rs.getInt("indexClient");

			// oggetto prepared statement che consente di eseguire una query al db...
			ps = con.prepareStatement("SELECT * FROM hash where idClient=?");
			// ... a partire dal nome e ...
			ps.setInt(1, clientId);
			// esegue effettivamente la query ed ottiene un oggetto ResultSet che contiene la risposta del db
			rs = ps.executeQuery();
			// il next() prende la prima riga del risultato della query
			// restituisce true se c'è almeno una riga altrimenti false
			rs.next();				
			hash = rs.getBytes("hash");				
			// svuoto la password
			//Arrays.fill(pass, (byte)0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return hash;
		}
	
	public static boolean checkHash(byte[] psw, byte[] hash, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] hashGenerated = HashingPassword.hashGeneration(psw, salt);
		
	    Encoder encoder = Base64.getUrlEncoder().withoutPadding();
	    String token = encoder.encodeToString(hashGenerated);
		//System.out.println("hash generato da credenziali inserite->-"+token);

	    encoder = Base64.getUrlEncoder().withoutPadding();
	    String token2 = encoder.encodeToString(hash);
		//System.out.println("hash recuperato da db->-"+token2);
		
		System.out.println("hash recuperato dal db->" + token2 + " hash generato da credenziali->" + token);

		return Arrays.equals(hashGenerated, hash);
	}
}
