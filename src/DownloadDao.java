import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadDao {

	
	public static boolean downloadPropose(int id, HttpServletRequest request, HttpServletResponse response) {
		boolean status = false;
		//
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1:3306/test";
			Connection con = DriverManager.getConnection (url, "root", "root");
			
			PreparedStatement ps = con.prepareStatement("SELECT propose FROM proposals where idProposal=?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			status = rs.next();
			
			Blob blob = rs.getBlob("propose");
			InputStream inputStream = blob.getBinaryStream();
			
			PrintWriter outStream = response.getWriter();
			
            int bytesRead;
             
            while ((bytesRead = inputStream.read()) != -1) {
                outStream.write(bytesRead);
            }
             
            inputStream.close();
            outStream.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
}
