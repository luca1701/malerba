
public class User {
	private String username;
	private Object id;
	
	public User(String u, Object object) {
		username = u ;
		this.id = object;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Object getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	
	/*public int getId(){
		return UserDao.getIdUser(username);
	}*/
}
