import javax.servlet.http.Cookie;

public class CookieGestor {
	public String getEmailFromCookie(Cookie cookie) {
		
		String value = cookie.getValue();
		String email = "";
		String[] parts = value.split(":");
		if (parts.length == 2) {
			email = parts[0];
			String randomString = parts[1];
			
		}

		return email;
	}
}
