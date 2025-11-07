<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="fr.uge.jee_td2.MessageDErreurs" %> <%-- 1. Importer la classe des messages --%>

<%
    String codeAffichage = (String) request.getAttribute("CodeAffichage");

    String messageErreur = null;
    if (codeAffichage != null && !codeAffichage.equals("0")) {
        messageErreur = MessageDErreurs.getMessageDerreur(codeAffichage);
    }

    String noCompteSaisi = (String) request.getAttribute("noDeCompte");
    if (noCompteSaisi == null) {
        noCompteSaisi = "";
    }
%>

<html>
<head>
    <title>Saisie du N° de compte</title>
</head>
<body>

<h3>Saisie du N° de compte:</h3>

<form action="${pageContext.request.contextPath}/Compte/GestionOperations" method="POST">

    Entrez le N° de compte:
    <input type="text" id="NoDeCompte" name="NoDeCompte" value="<%= noCompteSaisi %>"> <%-- 3. Réafficher la saisie --%>

    <%-- 4. Le bouton envoie le paramètre "Demande" --%>
    <button type="submit" name="Demande" value="Consulter">Consulter</button>

    <br><br>

    <% if (messageErreur != null) { %>
    <p style="color: red; font-weight: bold;">
        <%= messageErreur %>
    </p>
    <% } %>

</form>
</body>
</html>