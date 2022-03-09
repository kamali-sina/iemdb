<%@ page import="manager.UserManager" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home</title>
</head>
<body>
<ul>
    <li id="email">Email: <%=UserManager.getLoggedInUser().getEmail()%></li>
    <li>
        <a href="/movies">Movies</a>
    </li>
    <li>
        <a href="/watchlist">Watch List</a>
    </li>
    <li>
        <a href="/logout">Log Out</a>
    </li>
</ul>
</body>
</html>