<%@ page import="manager.UserManager" %>
<%@ page import="main.Actor" %>
<%@ page import="manager.ActorManager" %>
<%@ page import="manager.MovieManager" %>
<%@ page import="main.Movie" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Actor</title>
    <style>
        li, td, th {
            padding: 5px;
        }
    </style>
</head>
<body>
<%
    Integer actorId = Integer.valueOf(request.getParameter("actorId"));
    Actor actor = ActorManager.getActor(actorId);
    ArrayList<Movie> actorMovies = MovieManager.getActorMovies(actorId);
%>
<a href="/">Home</a>
<p id="email">email: <%=UserManager.getLoggedInUser().getEmail()%></p>
<ul>
    <li id="name">name:  <%=actor.getName()%></li>
    <li id="birthDate">birthDate: <%=actor.getBirthDate()%></li>
    <li id="nationality">nationality: <%=actor.getNationality()%></li>
    <li id="tma">Total movies acted in: <%=String.valueOf(actorMovies.size())%></li>
</ul>
<table>
    <tr>
        <th>Movie</th>
        <th>imdb Rate</th>
        <th>rating</th>
        <th>page</th>
    </tr>
    <% for(Movie movie : actorMovies) { %>
    <tr>
        <td><%=movie.getName()%></td>
        <td><%=String.valueOf(movie.getImdbRate())%></td>
        <td><%=String.valueOf(movie.getAverageRatingRate())%></td>
        <td><a href=<%="\"/movies/" + String.valueOf(movie.getId()) + "\""%>>Link</a></td>
    </tr>
    <% } %>

</table>
</body>
</html>