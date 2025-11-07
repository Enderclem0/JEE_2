package fr.uge.jee_td2.servlets;

import fr.uge.jee_td2.JSPEnum;
import fr.uge.jee_td2.MessageDErreurs;
import fr.uge.jee_td2.TraitementException;
import fr.uge.jee_td2.javaBeans.BOperations;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet("/GestionsOperations")
public class SOperations extends HttpServlet {

    private Optional<String> getNoDeCompte(HttpServletRequest req) {
        var NoDeCompte = req.getParameter("NoDeCompte");
        if (NoDeCompte == null || NoDeCompte.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(NoDeCompte);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var bean = new BOperations();
        var noDeCompte = getNoDeCompte(req);
        if (noDeCompte.isEmpty()) {
            RequestDispatcher dispatcher = req.getRequestDispatcher(JSPEnum.JSaisiNoDeCompte.getJspPath());
            dispatcher.forward(req, resp);
            return;
        }
        TraitementException exception = null;
        try {
            bean.ouvrirConnexion();
            bean.setNoDeCompte(noDeCompte.get());
            bean.consulter();
            bean.fermerConnexion();
        } catch (TraitementException e) {
            exception = e;
        }
        resp.setContentType("text/html");
        try (PrintWriter out = resp.getWriter()) {
            if (exception == null) {
                out.println("<html><body>");
                out.println("<h1>NCompte: "+bean.getNoDeCompte()+"</h1>");
                out.println("<h1>Nom: "+bean.getNom()+"</h1>");
                out.println("<h1>Prenom: "+bean.getPrenom()+"</h1>");
                out.println("<h1>Solde: "+bean.getSolde()+"</h1>");
                out.println("</body></html>");
            }
            else {
                String msgErreur = MessageDErreurs.getMessageDerreur(exception.getMessage());
                out.println("<html><body>");
                out.println("<h1>Erreur lors de la consultation du compte: "+msgErreur+"</h1>");
                out.println("</body></html>");

            }

        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        processRequest(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        processRequest(request, response);
    }
}