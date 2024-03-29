import java.sql.*;

	public class UserDao {
		public static boolean isUserValid(String name) {
			boolean status = false;
			try {
				// inizializza il driver per comunicare con il db
				Class.forName("com.mysql.cj.jdbc.Driver");
				// stringa di connessione: indirizzo - porta - nome db
				String url = "jdbc:mysql://127.0.0.1:3306/test";
				// oggetto connessione al db tramite inserimento di credenziali: stringa di connessione - nome utente - password
				Connection con = DriverManager.getConnection (url, "root", "root");
				// oggetto prepared statement che consente di eseguire una query al db...
				PreparedStatement ps = con.prepareStatement("SELECT * FROM client where name=?");
				// ... a partire dal nome e ...
				ps.setString(1, name);
				// ... password date in input dall'utente alla jsp, dalla jsp alla servlet e dalla servlet al DAO
				//ps.setBytes(2, pass);
				// esegue effettivamente la query ed ottiene un oggetto ResultSet che contiene la risposta del db
				ResultSet rs = ps.executeQuery();
				// il next() prende la prima riga del risultato della query
				// restituisce true se c'� almeno una riga altrimenti false
				status = rs.next();				
				// svuoto la password
				//Arrays.fill(pass, (byte)0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			return status;
		}
		
		/*public static int getIdUser(String name) {
			int id = 0;
			try {
				// inizializza il driver per comunicare con il db
				Class.forName("com.mysql.cj.jdbc.Driver");
				// stringa di connessione: indirizzo - porta - nome db
				String url = "jdbc:mysql://127.0.0.1:3306/test";
				// oggetto connessione al db tramite inserimento di credenziali: stringa di connessione - nome utente - password
				Connection con = DriverManager.getConnection (url, "root", "root");
				// oggetto prepared statement che consente di eseguire una query al db...
				PreparedStatement ps = con.prepareStatement("SELECT * FROM client where name=?");
				// ... a partire dal nome e ...
				ps.setString(1, name);
				// ... password date in input dall'utente alla jsp, dalla jsp alla servlet e dalla servlet al DAO
				//ps.setBytes(2, pass);
				// esegue effettivamente la query ed ottiene un oggetto ResultSet che contiene la risposta del db
				ResultSet rs = ps.executeQuery();
				// il next() prende la prima riga del risultato della query
				// restituisce true se c'� almeno una riga altrimenti false
				while(rs.next()) {
					id = rs.getInt("id");
				}				
				// svuoto la password
				//Arrays.fill(pass, (byte)0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			return id;
		}*/
		
		public static boolean isUserAvailable(String name) {
			boolean status = false;
			try {
				// inizializza il driver per comunicare con il db
				Class.forName("com.mysql.cj.jdbc.Driver");
				// stringa di connessione: indirizzo - porta - nome db
				String url = "jdbc:mysql://127.0.0.1:3306/test";
				// oggetto connessione al db tramite inserimento di credenziali: stringa di connessione - nome utente - password
				Connection con = DriverManager.getConnection (url, "root", "root");
				// oggetto prepared statement che consente di eseguire una query al db...
				PreparedStatement ps = con.prepareStatement("SELECT * FROM client where name=?");
				// ... a partire dal nome e ...
				ps.setString(1, name);
				// esegue effettivamente la query ed ottiene un oggetto ResultSet che contiene la risposta del db
				ResultSet rs = ps.executeQuery();
				// il next() prende la prima riga del risultato della query
				// restituisce true se c'� almeno una riga altrimenti false
				status = rs.next();
				// svuoto la password
				} catch (Exception e) {
					e.printStackTrace();
				}
			return !status;
		}
		
		/*public static boolean userRegistration(String name, byte[] img) {
			boolean status = false;
			try {
				// inizializza il driver per comunicare con il db
				Class.forName("com.mysql.cj.jdbc.Driver");
				// stringa di connessione: indirizzo - porta - nome db
				String url = "jdbc:mysql://127.0.0.1:3306/test";
				// oggetto connessione al db tramite inserimento di credenziali: stringa di connessione - nome utente - password
				Connection con = DriverManager.getConnection (url, "root", "root");
				// oggetto prepared statement che consente di eseguire una query al db...
				PreparedStatement ps = con.prepareStatement("INSERT INTO client(name, profileImage) values(?,?)");
				// ... a partire dal nome e ...
				ps.setString(1, name);
				// password
				//Blob blob = new SerialBlob(pass);
				//ps.setBlob(2, blob);
				// immagine profilo
			
				Blob blob2 = con.createBlob();
				blob2.setBytes(1, img);
				ps.setBlob(2, blob2);

				// esegue effettivamente la query ed ottiene un oggetto ResultSet che contiene la risposta del db
				ps.executeUpdate();
				// svuoto la password
				} catch (Exception e) {
					e.printStackTrace();
				}
			return !status;
		}*/
	}