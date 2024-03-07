import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import com.optimaize.langdetect.frma.IOUtils;
/**
 * Servlet implementation class RegistrationServlet
 */
@WebServlet("/RegistrationServlet")
@MultipartConfig
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Map<String, String> rememberMeMap = new ConcurrentHashMap<>();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistrationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub		
		doGet(request, response);
		final long  MAXSIZEIMG= 65535;

		String nomeUtente = request.getParameter("username").toString();
		
		byte[] password =  request.getParameter("pass").getBytes();
		
		byte[] password_c =  request.getParameter("pass_c").getBytes();
		
		//verifica password inserita non sia vuota
        byte[] empty = new byte[password.length];
        byte[] empty_c = new byte[password_c.length];
        
        Part img = request.getPart("profile_img");
        long img_size = img.getSize();
        
        
        if(img_size>0 && img_size < MAXSIZEIMG){
        	System.out.println(img.getSize());
        	try {
				ContentExtraction.getSize(img);
			} catch (IOException | SAXException | TikaException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        try
			{
				if(!(Arrays.equals(password, empty)) && !nomeUtente.isEmpty() && !(Arrays.equals(password_c, empty_c))) {
					if(UserDao.isUserAvailable(nomeUtente) && Arrays.equals(password, password_c))
					{
						if(ContentExtraction.checkImagetType(img) == 1){
							System.out.println("Errore formato file");
				        	response.sendRedirect("registration.jsp");
						}else {
							byte[] profileImage = new byte[img.getInputStream().available()];
							img.getInputStream().read(profileImage);
							RegistrationDao.userRegistration(nomeUtente, profileImage);//salva su db
							
							HttpSession session = request.getSession(true);
							session.setAttribute("id", LoginDao.getUserId(nomeUtente));

							byte[] salt = HashingPassword.saltGeneration();
							byte[] hash = HashingPassword.hashGeneration(password, salt);
							HashingPasswordDao.hashSaving(salt, hash, nomeUtente);
							
							System.out.println("3-registrazione con successo");
							response.sendRedirect("proposeView.jsp");
			        	}
					}
					else if(!UserDao.isUserAvailable(nomeUtente) && (Arrays.equals(password, password_c) || !Arrays.equals(password, password_c)))
					{
						System.out.println("4-nome utente occupato");
						//throw new ServletException("nome utente occupato");
						response.sendRedirect("Registration.jsp");
					}
					else if(UserDao.isUserAvailable(nomeUtente) && !(Arrays.equals(password, password_c)))
					{
						System.out.println("5-Password non coincidenti!");
						//throw new ServletException("nome utente occupato");
						response.sendRedirect("Registration.jsp");
					}
			}
			else
			{
				System.out.println("Nome utent o password vuote!");
				response.sendRedirect("Registration.jsp");
			}
		}catch(Exception e)
	        {
				e.printStackTrace();
			}
        }else if(img_size==0){
        	System.out.println("Immagine non caricata");
        	response.sendRedirect("Registration.jsp");
        }else {
        	System.out.println("Dimensione immagine caricata eccessiva! Si prega di cambiarla.");
        	response.sendRedirect("Registration.jsp");
        }
    }
	


}
