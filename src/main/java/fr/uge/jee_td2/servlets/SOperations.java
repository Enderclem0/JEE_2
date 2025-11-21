package fr.uge.jee_td2.servlets;

import fr.uge.jee_td2.JSPEnum;
import fr.uge.jee_td2.MessageDErreurs;
import fr.uge.jee_td2.TraitementException;
import fr.uge.jee_td2.javaBeans.BOperations;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;

public class SOperations extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("CodeAffichage", "0");

        RequestDispatcher dispatcher = request.getRequestDispatcher(JSPEnum.JSaisiNoDeCompte.getJspPath());
        dispatcher.forward(request, response);
    }

    private void verifyOperation(String valeurStr) throws TraitementException {
        if (valeurStr == null || valeurStr.isBlank()) {
            throw new TraitementException("6"); // "La valeur est obligatoire"
        }

        try {
            double valeur = Double.parseDouble(valeurStr);
            if (valeur <= 0) {
                throw new TraitementException("7"); // "La valeur doit être positive"
            }
        } catch (NumberFormatException e) {
            throw new TraitementException("6"); // "La valeur doit être un nombre"
        }
    }

    private void processTraitement(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String noDeCompte = request.getParameter("NoDeCompte");
        String operation = request.getParameter("Opération");
        String valeurStr = request.getParameter("Valeur");

        BOperations bean = new BOperations();

        try {
            verifyOperation(valeurStr);
            DataSource ds = (DataSource) getServletContext().getAttribute("banqueDataSource");
            bean.ouvrirConnexion(ds);
            bean.setNoDeCompte(noDeCompte);
            bean.setOp(operation);
            bean.setValeur(valeurStr);
            bean.traiter();

            bean.consulter();
            bean.fermerConnexion();

            request.setAttribute("beanOperations", bean);
            request.setAttribute("CodeAffichage", "20");

            RequestDispatcher dispatcher = request.getRequestDispatcher(JSPEnum.JOperations.getJspPath());
            dispatcher.forward(request, response);

        } catch (TraitementException e) {
            request.setAttribute("CodeAffichage", e.getMessage()); // ex: "6" ou "7"

            // Il faut quand même renvoyer les infos du compte !
            try {
                // On re-consulte le compte pour avoir son état actuel
                DataSource ds = (DataSource) getServletContext().getAttribute("banqueDataSource");
                bean.ouvrirConnexion(ds);
                bean.setNoDeCompte(noDeCompte);
                bean.consulter();
                bean.fermerConnexion();
            } catch (TraitementException e2) {
                // Si la re-consultation échoue aussi, on renvoie à la saisie
                request.setAttribute("CodeAffichage", e2.getMessage());
                RequestDispatcher dispatcher = request.getRequestDispatcher(JSPEnum.JSaisiNoDeCompte.getJspPath());
                dispatcher.forward(request, response);
                return;
            }

            request.setAttribute("beanOperations", bean); // Renvoyer le bean
            RequestDispatcher dispatcher = request.getRequestDispatcher(JSPEnum.JOperations.getJspPath());
            dispatcher.forward(request, response);
        }
    }

    private void processConsultation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String noDeCompte = request.getParameter("NoDeCompte");
        request.setAttribute("NoDeCompte", noDeCompte);

        try {
            BOperations bean = consultation(noDeCompte);


            request.setAttribute("beanOperations", bean);
            request.setAttribute("CodeAffichage", "10");

            RequestDispatcher dispatcher = request.getRequestDispatcher(JSPEnum.JOperations.getJspPath());
            dispatcher.forward(request, response);

        } catch (TraitementException e) {
            request.setAttribute("CodeAffichage", e.getMessage());

            RequestDispatcher dispatcher = request.getRequestDispatcher(JSPEnum.JSaisiNoDeCompte.getJspPath());
            dispatcher.forward(request, response);
        }
    }

    public void processRetour(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String noDeCompte = request.getParameter("NoDeCompte");

        // Création du bean pour restaurer l'état
        BOperations bean = new BOperations();
        bean.setNoDeCompte(noDeCompte);

        // Restauration des dates
        bean.setDateInf(request.getParameter("DateInf"));
        bean.setDateSup(request.getParameter("DateSup"));

        // Restauration Valeur et Opération
        String val = request.getParameter("Valeur");
        if (val != null && !val.isBlank()) {
            try {
                bean.setValeur(val);
            } catch (Exception e) {
            }
        }
        bean.setOp(request.getParameter("Opération"));

        // On re-consulte la BDD pour avoir le solde/nom à jour
        try {
            DataSource ds = (DataSource) getServletContext().getAttribute("banqueDataSource");
            bean.ouvrirConnexion(ds);
            bean.consulter();
            bean.fermerConnexion();
        } catch (TraitementException e) {
            // Gérer l'erreur si besoin
        }

        request.setAttribute("beanOperations", bean);
        request.setAttribute("CodeAffichage", "12"); // Code "Retour" (facultatif)

        // On renvoie vers la page principale
        RequestDispatcher dispatcher = request.getRequestDispatcher(JSPEnum.JOperations.getJspPath());
        dispatcher.forward(request, response);
    }

    private void processLister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String noDeCompte = request.getParameter("NoDeCompte");
        request.setAttribute("NoDeCompte", noDeCompte);
        BOperations bean = new BOperations();
        bean.setDateInf(request.getParameter("DateInf"));
        bean.setDateSup(request.getParameter("DateSup"));
        bean.setNoDeCompte(noDeCompte);

        String val = request.getParameter("Valeur");
        if (val != null && !val.isBlank()) {
            bean.setValeur(val);
        }
        bean.setOp(request.getParameter("Opération"));
        try {
            DataSource ds = (DataSource) getServletContext().getAttribute("banqueDataSource");
            bean.ouvrirConnexion(ds);
            bean.consulter();
            bean.listerParDates();
            bean.fermerConnexion();

            request.setAttribute("beanOperations", bean);
            request.setAttribute("CodeAffichage", "10");
            request.getRequestDispatcher(JSPEnum.JListeOperations.getJspPath()).forward(request, response);

        } catch (TraitementException e) {
            request.setAttribute("CodeAffichage", e.getMessage());
            if (e.getMessage().compareTo("31") == 0 || e.getMessage().compareTo("32") == 0) {
                request.getRequestDispatcher(JSPEnum.JOperations.getJspPath()).forward(request, response);
            }
            else {
                request.getRequestDispatcher(JSPEnum.JListeOperations.getJspPath()).forward(request, response);
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String demande = request.getParameter("Demande");

        if ("Consulter".equals(demande)) {
            processConsultation(request, response);
        } else if ("Traiter".equals(demande)) {
            processTraitement(request, response);
        } else if ("FinTraitement".equals(demande)) {
            doGet(request, response);
        } else if ("Lister".equals(demande)) {
            processLister(request, response);
        } else if ("Retour".equals(demande)) {
            processRetour(request, response);
        } else {
            doGet(request, response);
        }
    }

    private void verifNoDeCompte(String noDeCompte) throws TraitementException {
        if (noDeCompte == null || noDeCompte.isBlank()) {
            throw new TraitementException("5");
        }

        if (noDeCompte.length() != 4) {
            throw new TraitementException("4");
        }

        try {
            Integer.parseInt(noDeCompte);
        } catch (NumberFormatException e) {
            throw new TraitementException("5");
        }
    }

    private BOperations consultation(String noDeCompte) throws TraitementException {
        verifNoDeCompte(noDeCompte);

        BOperations bean = new BOperations();
        try {
            DataSource ds = (DataSource) getServletContext().getAttribute("banqueDataSource");
            bean.ouvrirConnexion(ds);
            bean.setNoDeCompte(noDeCompte);
            bean.consulter();
        } finally {
            try {
                bean.fermerConnexion();
            } catch (TraitementException e) {
                e.printStackTrace();
            }
        }
        return bean;
    }
}