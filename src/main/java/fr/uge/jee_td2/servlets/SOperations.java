package fr.uge.jee_td2.servlets;

import java.io.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/GestionsOperations")
public class SOperations extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        // Retrieve the NoDeCompte parameter from the request
        var NoDeCompte = request.getParameter("NoDeCompte");
        if (NoDeCompte == null || NoDeCompte.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "NoDeCompte parameter is missing or empty.");
            return;
        }
        try (PrintWriter out = response.getWriter()) {
            out.println("<html><body>");
            out.println("<h1>Gestion des opérations pour le compte: " + NoDeCompte + "</h1>");
            out.println("</body></html>");
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        processRequest(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        processRequest(request, response);
    }
}