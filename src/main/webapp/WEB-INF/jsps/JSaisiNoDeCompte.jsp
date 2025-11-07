<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>NoDeCompteFormulaire</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/GestionsOperations" method="POST">
    <label for="NoDeCompte">noDeCompte</label>
    <input type="text" id="NoDeCompte" name="NoDeCompte">
    <button type="submit">Trouver</button>
</form>
</body>
</html>
