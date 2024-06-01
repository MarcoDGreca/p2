package it.unisa.control;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unisa.model.ProdottoBean;
import it.unisa.model.ProdottoDao;

/**
 * Servlet implementation class HomeServlet
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Directory consentita
    private static final String BASE_DIRECTORY = "/WEB-INF/";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProdottoDao dao = new ProdottoDao();
        ArrayList<ArrayList<ProdottoBean>> categorie = new ArrayList<>();
        String redirectedPage = request.getParameter("page");

        // Validazione del percorso
        if (redirectedPage == null || !isPathAllowed(redirectedPage)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Pagina non valida");
            return;
        }
        
        if (redirectedPage.contains("WEB-INF/web.xml")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Pagina non valida");
            return;
        }

        String pagePath = BASE_DIRECTORY + redirectedPage;

        try {
            ArrayList<ProdottoBean> PS5 = dao.doRetrieveByPiattaforma("PlayStation 5");
            ArrayList<ProdottoBean> XboxSeries = dao.doRetrieveByPiattaforma("Xbox Series");
            ArrayList<ProdottoBean> Switch = dao.doRetrieveByPiattaforma("Nintendo Switch");
            ArrayList<ProdottoBean> PS4 = dao.doRetrieveByPiattaforma("PlayStation 4");
            ArrayList<ProdottoBean> Xone = dao.doRetrieveByPiattaforma("Xbox One");

            categorie.add(PS5);
            categorie.add(XboxSeries);
            categorie.add(Switch);
            categorie.add(PS4);
            categorie.add(Xone);

            request.getSession().setAttribute("categorie", categorie);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(pagePath);
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private boolean isPathAllowed(String path) {
        // Verifica che il percorso non contenga sequenze che potrebbero consentire il Path Traversal
        File file = new File(BASE_DIRECTORY, path);
        try {
            String canonicalPath = file.getCanonicalPath();
            String basePath = new File(BASE_DIRECTORY).getCanonicalPath();

            // Verifica che il percorso sia sotto la directory consentita e non contenga ".."
            // Inoltre, verifica che il percorso non punti a web.xml
            return canonicalPath.startsWith(basePath) 
                   && !canonicalPath.contains("..") 
                   && !canonicalPath.equals("/WEB-INF/web.xml");
        } catch (IOException e) {
            return false;
        }
    }
}

