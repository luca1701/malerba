import java.sql.*;

public class ProposeViewDao {

	public static ResultSet getProposals() {
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1:3306/test";
			Connection con = DriverManager.getConnection (url, "root", "root");
			PreparedStatement ps = con.prepareStatement("SELECT client.name, proposals.propose_name  from proposals inner join client on proposals.idClient=client.indexClient");
			rs = ps.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}
}
