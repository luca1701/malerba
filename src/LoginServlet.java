

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.SecretKey;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Map<String, String> rememberMeMap = new ConcurrentHashMap<>();

    public LoginServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
		String nomeUtente = request.getParameter("username");
		byte[] password= request.getParameter("pass").getBytes();
		//verifica password inserita non sia vuota
        byte[] empty = new byte[password.length];
        
        String str = new String(password);
        //cacnella System.out.println(str);
        
		try
		{
			if(!(Arrays.equals(password, empty)) && !nomeUtente.isEmpty()) {
				
								if(!UserDao.isUserAvailable(nomeUtente)) {
									System.out.println("!!verifica correttezza hashing di psw!!");
									byte[] salt = HashingPasswordDao.saltRecovery(nomeUtente, password);
									byte[] hash = HashingPasswordDao.hashRecovery(nomeUtente, password);
									
									if(HashingPasswordDao.checkHash(password, hash, salt))
									{		
										System.out.println("1-accesso corretto");
										Cookie c = new Cookie("username", nomeUtente);
										response.addCookie(c);
										//getServletContext().getNamedDispatcher("ProposeViewServlet").forward(request, response);
										ResultSet rs = ProposeViewDao.getProposals();
										if(rs != null) {
											Cookie(request, response);
											response.sendRedirect("proposeView.jsp");
											
										}else{
											response.sendRedirect("uploadFile.jsp");
											}
									}
									else 
									{
										System.out.println("1a-password errata");
										response.sendRedirect("login.jsp");
									}
								}
								else
								{
									System.out.println("2-Nome utente non registrato!");
									response.sendRedirect("login.jsp");
								}
				/*
				 			else if(loginDao.isUserAvailable(nomeUtente) && request.getParameter("register").toString().equals("register") && request.getParameter("pass").toString().equals(request.getParameter("pass_c").toString())) 
							{
								System.out.println("3-registrazione con successo");
								//rewgistrazione su db
								loginDao.userRegistration(nomeUtente, password);
								response.sendRedirect("index.jsp");
							}
				
							else if(!loginDao.isUserAvailable(nomeUtente) && request.getParameter("register").toString().equals("register"))
							{
								System.out.println("4-nome utente occupato");
								//throw new ServletException("nome utente occupato");
								response.sendRedirect("errore.jsp");
							}
				
							else if(loginDao.isUserAvailable(nomeUtente) && request.getParameter("register").toString().equals("register") && !(request.getParameter("pass").toString().equals(request.getParameter("pass_c").toString())))
							{
								System.out.println("5-Password non coincidenti!");
								//throw new ServletException("nome utente occupato");
								response.sendRedirect("errore.jsp");
							}	*/			
			}
			else
			{
				System.out.println("Nome utent o password vuote!");
				response.sendRedirect("login.jsp");
			}	

			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	protected void Cookie(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		String email = request.getParameter("username");
		session.setAttribute("username", email);
		
		if (request.getParameter("remember") != null) {
			String randomString = generateRandomString();
			rememberMeMap.put(email, randomString);
			
			try {
				String cookie = email + ":" + randomString;
				SecretKey key = CookieEncryption.generateRandomKey();
				
				String encryptedCookie = CookieEncryption.encryptCookie(cookie, key);
				CookieEncryption.storeKey(encryptedCookie,key);
				// Crea il cookie "remember me" e imposta la sua durata
				Cookie rememberMeCookie = new Cookie("remember", encryptedCookie);
				//Cookie rememberMeCookie = new Cookie("remember", email + ":" + randomString);
				rememberMeCookie.setMaxAge(15 * 60); // 15 MIN

				// Aggiungi il cookie alla risposta
				response.addCookie(rememberMeCookie);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		session.setMaxInactiveInterval(15*60);
		
	}
	protected String generateRandomString() {
		SecureRandom secureRandom = new SecureRandom();

		int TOKEN_LENGTH = 32;
		byte[] bytes = new byte[TOKEN_LENGTH];
		secureRandom.nextBytes(bytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
	}
}
