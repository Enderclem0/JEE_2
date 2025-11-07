<%--
  Created by IntelliJ IDEA.
  User: bapti
  Date: 07/11/2025
  Time: 15:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Opérations</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/JOperations" method="POST">
    <input type="radio" id="Crédit" name="Opération" value="Crédit"/>
    <label for="Crédit">Crédit</label>
    <input type="radio" id="Débit" name="Opération" value="Débit"/>
    <label for="Débit">Débit</label>
    <br>
    <label for="Valeur">Valeur:</label>
    <input type="text" id="Valeur" name="Valeur">
    <input type="text" id="Euro" name="Euro">
    <br>
    <label for="Euro"> Euro</label>
    <br>
    <button type="submit">Traiter</button>
    <br>
    <button type="submit">Fin du Traitement</button>
</form>
</body>
</html>
