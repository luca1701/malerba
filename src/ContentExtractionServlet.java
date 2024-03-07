

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.tika.exception.TikaException;
import org.apache.tika.io.FilenameUtils;
import org.checkerframework.checker.regex.qual.Regex;
import org.xml.sax.SAXException;

/**
 * Servlet implementation class ContentExtractionServlet
 */
@WebServlet("/ContentExtractionServlet")
@MultipartConfig
public class ContentExtractionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ContentExtractionServlet() {
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
		//doGet(request, response);
		
        Part file = request.getPart("file");
        String filename = null;
        Collection<String> headers = file.getHeaders("content-disposition");
        for(String header : headers){
        	//System.out.println(header);
        	if(header.contains("filename")) {
        		filename = header;
        		filename = filename.split("filename=\"")[1];
        		filename = filename.split("\\.")[0];
        		//System.out.println("namefile->"+filename);
        	}
        }
       
        System.out.println(file.getSize());
        if(file.getSize()>0 && file.getSize()<65535) {
        	try {
        		if(ContentExtraction.checkFileType(file) == 1){
        			//formato non accettato
					System.out.println("Errore formato file");
		        	response.sendRedirect("uploadFile.jsp");
				}else {
					//formato accettato
					//!!!!ContentExtractionDao.checkUserID();
					HttpSession session = request.getSession(false);
					
					String id = session.getAttribute("id").toString();
					System.out.println(id);
					if(ContentExtractionDao.uploadPropose(id, file, filename)) {
						System.out.println("Caricamento file effettuato");
		        		response.sendRedirect("proposeView.jsp");
					}else {
						System.out.println("Errore caricamento?");
					}
					}
			} catch (IOException | TikaException | SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }else if(file.getSize()>65535) {
			System.out.println("Dimensione file troppo grande");
	}else {
        	//immagine non caricata
			System.out.println("Proposta non caricata");
        	response.sendRedirect("uploadFile.jsp");}

	}

}
