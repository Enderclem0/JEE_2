<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="fr.uge.jee_td2.javaBeans.BOperations" %>
<%@ page import="fr.uge.jee_td2.MessageDErreurs" %>

<%
    String codeAffichage = (String) request.getAttribute("CodeAffichage");
    BOperations bean = (BOperations) request.getAttribute("beanOperations");

    String message = null;
    String messageColor = "red";

    if (codeAffichage != null) {
        switch (codeAffichage) {
            case "10":
                message = "La consultation s'est bien passée";
                messageColor = "blue";
                break;
            case "20":
                message = "Opération effectuée avec succès.";
                messageColor = "blue";
                break;

            case "6":
            case "7":
            case "24":
            case "3":
            case "21":
            case "22":
            default:
                message = MessageDErreurs.getMessageDerreur(codeAffichage);
                messageColor = "red";
                break;
        }
    }
    boolean isEcriture = request.isUserInRole("ecriture");

    String valPrecedente = "";
    String opPrecedente = "Crédit";

    if (bean != null) {
        try { valPrecedente = bean.getValeur(); } catch(Exception e) {}
        if (bean.getOp() != null) opPrecedente = bean.getOp();
    }
%>

<html>
<head>
    <title>Opérations sur le compte</title>
</head>
<body>

<%-- 4. Afficher le message (succès ou erreur) --%>
<% if (message != null) { %>
<h3 style="color: <%= messageColor %>;"><%= message %></h3>
<% } %>

<hr>

<%-- 5. Afficher les détails (si le bean existe) --%>
<% if (bean != null) { %>
<h3>Détails du compte</h3>
<p>
    <strong>Numéro de compte :</strong> <%= bean.getNoDeCompte() %><br>
    <strong>Nom :</strong> <%= bean.getNom() %><br>
    <strong>Prénom :</strong> <%= bean.getPrenom() %><br>
    <strong>Solde :</strong> <%= bean.getSolde() %> Euros
</p>

<hr>

<% if (isEcriture) { %>

<h3>Opération à effectuer :</h3>
<form action="${pageContext.request.contextPath}/Compte/GestionOperations" method="POST">

    <%-- On renvoie toujours le N° de compte --%>
    <input type="hidden" name="NoDeCompte" value="<%= bean.getNoDeCompte() %>">

    <%-- ================================================================= --%>
    <%-- BLOC RÉSERVÉ AU RÔLE "ECRITURE" (Crédit / Débit / Traiter)        --%>
    <%-- ================================================================= --%>
    <% if (isEcriture) { %>

    <h3>Opération à effectuer :</h3>

    <input type="radio" id="Crédit" name="Opération" value="Crédit" <%= opPrecedente.equals("Crédit") ? "checked" : "" %> />
    <label for="Crédit">Crédit</label>

    <input type="radio" id="Débit" name="Opération" value="Débit" <%= opPrecedente.equals("Débit") ? "checked" : "" %> />
    <label for="Débit">Débit</label>

    <br>

    <label for="Valeur">Valeur:</label>
    <input type="text" id="Valeur" name="Valeur" value="<%= valPrecedente %>">
    <label for="Valeur"> Euro</label>
    <br><br>

    <button type="submit" name="Demande" value="Traiter">Traiter</button>

    <hr>
    <% } else { %>
    <%-- Message pour les lecteurs (Optionnel, pour faire propre) --%>
    <p><em>Mode Consultation Seule : Vous ne pouvez pas effectuer d'opérations.</em></p>
    <hr>
    <% } %>
    <%-- ================================================================= --%>


    <%-- BLOC VISIBLE PAR TOUS (Historique + Fin) --%>
    <h3>Historique et Fin</h3>

    <%
        String dInf = (bean.getDateInf() != null) ? bean.getDateInf() : "";
        String dSup = (bean.getDateSup() != null) ? bean.getDateSup() : "";
    %>

    <label for="DateInf">Du :</label>
    <input type="date" id="DateInf" name="DateInf" value="<%= dInf %>"> <%-- Retirez required si le bouton FinTraitement bloque --%>

    <label for="DateSup">Au :</label>
    <input type="date" id="DateSup" name="DateSup" value="<%= dSup %>">

    <button type="submit" name="Demande" value="Lister" formnovalidate>Afficher la liste</button>

    <br><br>

    <%-- Le bouton Fin doit être accessible à TOUS --%>
    <button type="submit" name="Demande" value="FinTraitement" formnovalidate>Fin du Traitement</button>

</form>

<% } %>

<% } else { %>
<%-- Ce bloc s'affiche si on arrive ici sans bean (ex: erreur grave) --%>
<p>Aucune donnée de compte à afficher. Veuillez recommencer la consultation.</p>
<% } %>

</body>
</html>