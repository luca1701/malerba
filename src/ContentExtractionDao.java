import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.catalina.connector.Response;
import org.apache.commons.io.IOUtils;

public class ContentExtractionDao {
	public static void checkUserID() {
		
	}
	
	public static boolean uploadPropose(String id, Part file, String filename) {
		boolean status = false;
		//
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1:3306/test";
			Connection con = DriverManager.getConnection (url, "root", "root");
			
			/*Cookie[] cookies = request.getCookies();
			for(Cookie c2: cookies) {
				String tname = c2.getName();
				if(tname.equals("username")) {
					String name = c2.getValue();
				
					PreparedStatement ps = con.prepareStatement("SELECT * FROM client where id=?");
					ps.setString(1, id);
					ResultSet rs = ps.executeQuery();
					status = rs.next();
					*/
			//if(status) {
						//
						PreparedStatement ps = con.prepareStatement("INSERT into proposals(idClient, propose, propose_name) values(?,?,?)");
									
						InputStream img2 = file.getInputStream();
						//byte[] byteArray = IOUtils.toByteArray(img2); 
									
						//Reader rdr = new InputStreamReader(img2, StandardCharsets.ISO_8859_1);
								    
						//ps.setCharacterStream(1, rdr);
						//Blob blob = con.createBlob();
						//blob.setBytes(1, byteArray);
						//ps.setBlob(1, blob);
						ps.setString(1, id);
						ps.setBlob(2, img2);
						ps.setString(3, filename);
						ps.executeUpdate();						

			//}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return !status;
	}
}
