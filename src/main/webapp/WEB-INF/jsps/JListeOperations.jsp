<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="fr.uge.jee_td2.javaBeans.BOperations" %>
<%@ page import="java.util.ArrayList" %>

<%
    BOperations bean = (BOperations) request.getAttribute("beanOperations");
    var errorCode = (String) request.getAttribute("CodeAffichage");

%>

<html>
<head>
    <title>Historique des opérations</title>
    <style>
        /* Style simple pour le tableau */
        table {
            border-collapse: collapse;
            width: 100%;
            margin-top: 15px;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }

        th {
            background-color: #f2f2f2;
        }

        /* Style pour le bouton retour */
        .btn-retour {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 15px;
            background-color: #6c757d;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }

        .btn-retour:hover {
            background-color: #5a6268;
        }
    </style>
</head>
<body>

<% if (bean != null) {
    String dateInf = (bean.getDateInf() != null) ? bean.getDateInf() : "";
    String dateSup = (bean.getDateSup() != null) ? bean.getDateSup() : "";

    String valeur;
    try { valeur = bean.getValeur(); } catch (Exception e) { valeur = ""; }

    String operation = (bean.getOp() != null) ? bean.getOp() : "Crédit";
%>

<h2>Historique du compte N° <%= bean.getNoDeCompte() %>
</h2>
<p>Titulaire : <%= bean.getNom() %> <%= bean.getPrenom() %>
</p>

<hr>

<%-- Formulaire de sélection de dates --%>
<form action="${pageContext.request.contextPath}/Compte/GestionOperations" method="POST">
    <%-- Important : On doit renvoyer le N° de compte pour que la servlet sache qui on consulte --%>
    <input type="hidden" name="NoDeCompte" value="<%= bean.getNoDeCompte() %>">
    <input type="hidden" name="Valeur" value="<%= valeur %>">
    <input type="hidden" name="Opération" value="<%= operation %>">



    <label for="DateInf">Du :</label>
    <input type="date" id="DateInf" name="DateInf" value="<%= (bean.getDateInf() != null) ? bean.getDateInf() : "" %>"
           required>

    <label for="DateSup">Au :</label>
    <input type="date" id="DateSup" name="DateSup" value="<%= (bean.getDateSup() != null) ? bean.getDateSup() : "" %>"
           required>

    <button type="submit" name="Demande" value="Lister">Afficher</button>
</form>

<%-- Tableau des résultats --%>
<%
    ArrayList<String[]> listeOps = bean.getOperationsParDates();
    if (listeOps != null && !listeOps.isEmpty()) {
%>
<table>
    <thead>
    <tr>
        <th>Date</th>
        <th>Type</th>
        <th>Valeur</th>
    </tr>
    </thead>
    <tbody>
    <% for (String[] op : listeOps) { %>
    <tr>
        <td><%= op[0] %>
        </td>
        <td><%= op[1] %>
        </td>
        <td><%= op[2] %> €</td>
    </tr>
    <% } %>
    </tbody>
</table>
<% } else if (listeOps != null) { %>
<p style="color: orange;">Aucune opération trouvée pour cette période.</p>
<% } %>

<%-- BOUTON RETOUR form --%>
<form action="${pageContext.request.contextPath}/Compte/GestionOperations" method="POST">

    <%-- Champs cachés pour conserver l'état au retour --%>
    <input type="hidden" name="NoDeCompte" value="<%= bean.getNoDeCompte() %>">
    <input type="hidden" name="DateInf" value="<%= (bean.getDateInf() != null) ? bean.getDateInf() : "" %>">
    <input type="hidden" name="DateSup" value="<%= (bean.getDateSup() != null) ? bean.getDateSup() : "" %>">

    <%-- Gestion sécurisée de la valeur pour éviter le null --%>
    <%
        String valHidden = "";
        try { valHidden = bean.getValeur(); } catch(Exception e) {}
    %>
    <input type="hidden" name="Valeur" value="<%= valHidden %>">
    <input type="hidden" name="Opération" value="<%= (bean.getOp() != null) ? bean.getOp() : "Crédit" %>">

    <button type="submit" name="Demande" value="Retour" class="btn-retour">Retour</button>
</form>

<%
} else if (errorCode != null && !errorCode.equals("0")) {
    String messageErreur = fr.uge.jee_td2.MessageDErreurs.getMessageDerreur(errorCode);
    String noDeCompte = (String) request.getAttribute("NoDeCompte");
    String dateInf = (String) request.getAttribute("DateInf");
    String dateSup = (String) request.getAttribute("DateSup");
    String valeur = (String) request.getAttribute("Valeur");
    String operation = (String) request.getAttribute("Opération");
%>

<p style="color: red; font-weight: bold;"><%= messageErreur %>
</p>

<%-- BOUTON RETOUR form --%>
<form action="${pageContext.request.contextPath}/Compte/GestionOperations" method="POST">
    <input type="hidden" name="NoDeCompte" value="<%= noDeCompte %>">
    <input type="hidden" name="DateInf" value="<%= dateInf %>">
    <input type="hidden" name="DateSup" value="<%= dateSup %>">
    <input type="hidden" name="Valeur" value="<%= valeur %>">
    <input type="hidden" name="Opération" value="<%= operation %>">
    <button type="submit" name="Demande" value="Retour">Retour</button>
</form>

</body>
</html>

<% } %>