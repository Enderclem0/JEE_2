<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Identification</title>
</head>
<body>
<h3>Veuillez vous identifier</h3>

<form method="POST" action="j_security_check">

    <label for="user">Identifiant :</label>
    <input type="text" id="user" name="j_username" required>
    <br><br>

    <label for="pass">Mot de passe :</label>
    <input type="password" id="pass" name="j_password" required>
    <br><br>

    <button type="submit">Authentifiez-vous</button>
</form>
</body>
</html>