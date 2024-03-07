import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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
		HttpSession session = request.getSession(false);
		
		if(!request.getParameter("username").isEmpty())
			session.setAttribute("id", LoginDao.getUserId(request.getParameter("username")));

		System.out.println("get");
		String loginWithRememberMe = request.getParameter("remember");
		
		if (loginWithRememberMe != null && loginWithRememberMe.equals("bocchin")) {
			if (session != null && session.getAttribute("id") != null) {
				// L'utente ha effettuato l'accesso, mostra la pagina protetta
				//request.getRequestDispatcher("benvenuto.jsp").forward(request, response);
				User u = new User(request.getParameter("username"), session.getAttribute("id"));
				session.setAttribute("u", u);
				// Impostare la durata della sessione in secondi (15 minuti)
				int durataSessioneSecondi = 15 * 60;
				session.setMaxInactiveInterval(durataSessioneSecondi);
				session.setAttribute("id", LoginDao.getUserId(request.getParameter("username")));

				//request.getRequestDispatcher("benvenuto.jsp").forward(request, response);
				System.out.println("-1-");
				//response.sendRedirect("proposeView.jsp");
			} else {
				// L'utente non ha effettuato l'accesso, verifica il cookie "remember me"
				if (verifyRememberMeCookie(request)) {
					// Il cookie "remember me" è valido, crea una nuova sessione e reindirizza
					// l'utente alla pagina di benvenuto
					System.out.println("-2-");
					
					session = request.getSession(true);
					String username = getUsernameFromRememberMeCookie(request);
					session.setAttribute("id", LoginDao.getUserId(username));

					User u = new User(username, LoginDao.getUserId(username));
					session.setAttribute("u", u);
					// Impostare la durata della sessione in secondi (15 minuti)
					int durataSessioneSecondi = 15 * 60;
					session.setMaxInactiveInterval(durataSessioneSecondi);
					System.out.println("-3-");
					//response.sendRedirect("proposeView.jsp");
					//request.getRequestDispatcher("proposeView.jsp").forward(request, response);
					
				} else {
					//Il cookie "remember me" non è valido, mostra la pagina di login
				
					Cookie[] cookies = request.getCookies();
					System.out.println("-cookie-");
					if (cookies != null) {
						for (Cookie cookie : cookies) {
							if (cookie.getName().equals("remember")) {
								cookie.setMaxAge(0);
								response.addCookie(cookie);
							}
						}
					}
					//response.sendRedirect("index.jsp");
					//System.out.println("-4-");
					//request.getRequestDispatcher("login.jsp").forward(request, response);
				}
			}
		}

		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
		HttpSession session = request.getSession(true);

		System.out.println("post");
		String nomeUtente = request.getParameter("username");

		if(!request.getParameter("username").isEmpty()) {
			System.out.println("id user-->" + LoginDao.getUserId(nomeUtente));
		}

		byte[] password= request.getParameter("pass").getBytes();
		//verifica password inserita non sia vuota
        byte[] empty = new byte[password.length];
        
        String str = new String(password);
        //cacnella System.out.println(str);
        
		String loginWithRememberMe = request.getParameter("remember");
		if(loginWithRememberMe != null && verifyRememberMeCookie(request)) {
			response.sendRedirect("proposeView.jsp");
		}else{
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
																				
										session.setAttribute("username", nomeUtente);
										
										if(request.getParameter("remember") != null) {
											
											String randomString = generateRandomString();
											rememberMeMap.put(nomeUtente, randomString);
											
											try {
												String cookie = nomeUtente + ":" + randomString;
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

										//Cookie c = new Cookie("username", nomeUtente);
										//response.addCookie(c);
										//getServletContext().getNamedDispatcher("ProposeViewServlet").forward(request, response);
										//ResultSet rs = ProposeViewDao.getProposals();
										//if(rs != null) {
											//Cookie(request, response);
											System.out.println("-5-");
											response.sendRedirect("proposeView.jsp");
											
										//}else{
											//System.out.println("-6-");
											//response.sendRedirect("uploadFile.jsp");
											//}
									}
									else 
									{
										System.out.println("1a-password errata");
										System.out.println("-7-");
										response.sendRedirect("login.jsp");
									}
								}
								else
								{
									System.out.println("2-Nome utente non registrato!");
									System.out.println("-8-");
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
				System.out.println("-9-");
				response.sendRedirect("login.jsp");
			}	
			
			System.out.println("\n\n");

			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		}
	}
	
	public boolean verifyRememberMeCookie(HttpServletRequest request) {
		Cookie[] cookies = (Cookie[]) request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("remember")) {
					
					try {
						String value =  cookie.getValue();
						SecretKey key = CookieEncryption.getKey(value);
						String decryptedCookie = CookieEncryption.decryptCookie(value, key);					 
						
						String[] parts = decryptedCookie.split(":");
						System.out.println("decryptedCookie" + decryptedCookie);
						
						if (parts.length == 2) {
							String username = parts[0];
							String randomString = parts[1];
							String storedRandomString = rememberMeMap.get(username);
							System.out.println("randomString" + randomString);
							System.out.println("storedRandomString" + storedRandomString);
							if (storedRandomString != null && storedRandomString.equals(randomString)) {
								return true;
							}
						}
						
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					}
					
					
				}
			}
		}
		return false;
	}
	
	// Metodo per recuperare il nome utente dal cookie "remember me"
	public String getUsernameFromRememberMeCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("remember")) {
					
					try {
						String value =  cookie.getValue();
						SecretKey key = CookieEncryption.getKey(value);
						String decryptedCookie = CookieEncryption.decryptCookie(value, key);					 
						
						String[] parts = decryptedCookie.split(":");
						System.out.println(decryptedCookie);
						
						if (parts.length == 2) {
							String username = parts[0];
							String randomString = parts[1];
							String storedRandomString = rememberMeMap.get(username);
							return parts[0];
						}
						
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
					}
				}
			}
		}
		return null;
	}
	
	protected void Cookie(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		String username = request.getParameter("username");
		session.setAttribute("username", username);
		
		if (request.getParameter("remember") != null) {
			String randomString = generateRandomString();
			rememberMeMap.put(username, randomString);
			
			try {
				String cookie = username + ":" + randomString;
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
