package it.unisa.control;

import java.io.IOException; 

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unisa.model.ProdottoBean;
import it.unisa.model.ProdottoDao;

@WebServlet("/catalogo")
public class CatalogoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ProdottoDao prodDao = new ProdottoDao();
		ProdottoBean bean = new ProdottoBean();
		String sort = request.getParameter("sort");
		String action = request.getParameter("action");
		String redirectedPage = request.getParameter("page");;
	
		try {
			if(action!=null) {
				if(action.equalsIgnoreCase("add")) {
	                bean.setNome(sanitizeHtml(request.getParameter("nome")));
	                bean.setDescrizione(sanitizeHtml(request.getParameter("descrizione")));
					bean.setIva(sanitizeHtml(request.getParameter("iva")));
					bean.setPrezzo(Double.parseDouble(request.getParameter("prezzo")));
					bean.setQuantità(Integer.parseInt(request.getParameter("quantità")));
					bean.setPiattaforma(sanitizeHtml(request.getParameter("piattaforma")));
					bean.setGenere(sanitizeHtml(request.getParameter("genere")));
					bean.setImmagine(sanitizeHtml(request.getParameter("img")));
					bean.setDataUscita(sanitizeHtml(request.getParameter("dataUscita")));
					bean.setDescrizioneDettagliata(sanitizeHtml(request.getParameter("descDett")));
					bean.setInVendita(true);
					prodDao.doSave(bean);
				}
				
				else if(action.equalsIgnoreCase("modifica")) {
					
					bean.setIdProdotto(Integer.parseInt(request.getParameter("id")));
					bean.setNome(sanitizeHtml(request.getParameter("nome")));
	                bean.setDescrizione(sanitizeHtml(request.getParameter("descrizione")));
					bean.setIva(sanitizeHtml(request.getParameter("iva")));
					bean.setPrezzo(Double.parseDouble(request.getParameter("prezzo")));
					bean.setQuantità(Integer.parseInt(request.getParameter("quantità")));
					bean.setPiattaforma(sanitizeHtml(request.getParameter("piattaforma")));
					bean.setGenere(sanitizeHtml(request.getParameter("genere")));
					bean.setImmagine(sanitizeHtml(request.getParameter("img")));
					bean.setDataUscita(sanitizeHtml(request.getParameter("dataUscita")));
					bean.setDescrizioneDettagliata(sanitizeHtml(request.getParameter("descDett")));
					bean.setInVendita(true);
					prodDao.doSave(bean);
				}

				request.getSession().removeAttribute("categorie");

			}
			
		} catch (SQLException e) {
			System.out.println("Error:" + e.getMessage());
		}


		try {
			request.getSession().removeAttribute("products");
			request.getSession().setAttribute("products", prodDao.doRetrieveAll(sort));
		} catch (SQLException e) {
			System.out.println("Error:" + e.getMessage());
		}
		
			
			response.sendRedirect(request.getContextPath() + "/" +redirectedPage);
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}



private String sanitizeHtml(String input) {
    if (input == null) {
        return null;
    }
    return input.replaceAll("<[^>]*>", "");
  }

}