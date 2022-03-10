<%@ page import="manager.UserManager" %>
<%@ page import="main.User" %>
<%@ page import="java.util.Collection" %>
<%@ page import="main.Movie" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>WatchList</title>
    <style>
        li, td, th {
            padding: 5px;
        }
    </style>
</head>
<body>
<%
    Collection<Movie> watchlist = UserManager.getLoggedInUser().getWatchList().values();
%>
<a href="/">Home</a>
<p id="email">email: <%=UserManager.getLoggedInUser().getEmail()%></p>
<ul>
    <li id="name">name: <%=UserManager.getLoggedInUser().getName()%></li>
    <li id="nickname">nickname: @<%=UserManager.getLoggedInUser().getNickname()%></li>
</ul>
<h2>Watch List</h2>
<table>
    <tr>
        <th>Movie</th>
        <th>releaseDate</th>
        <th>director</th>
        <th>genres</th>
        <th>imdb Rate</th>
        <th>rating</th>
        <th>duration</th>
        <th></th>
        <th></th>
    </tr>
    <% for(Movie movie : watchlist) { %>
    <tr>
        <th><%=movie.getName()%></th>
        <th><%=movie.getReleaseDate()%></th>
        <th><%=movie.getDirector()%></th>
        <th><%=movie.getGenresPretty()%></th>
        <th><%=String.valueOf(movie.getImdbRate())%></th>
        <th><%=String.valueOf(movie.getAverageRatingRate())%></th>
        <th><%=String.valueOf(movie.getDuration())%></th>
        <td><a href=<%="\"/movies/" + String.valueOf(movie.getId()) + "\""%>>Link</a></td>
        <td>
            <form action="" method="POST" >
                <input id="form_movie_id" type="hidden" name="movie_id" value=<%="\"" + String.valueOf(movie.getId()) + "\""%>>
                <button type="submit">Remove</button>
            </form>
        </td>
    </tr>
    <% } %>
</table>
<%List<Movie> recommendations = UserManager.getLoggedInUser().getWatchlistRecommendations();%>
<h2>Recommendation List</h2>
<table>
    <tr>
        <th>Movie</th>
        <th>imdb Rate</th>
        <th></th>
    </tr>
    <% for(Movie movie : recommendations) { %>
    <tr>
        <th><%=movie.getName()%></th>
        <th><%=String.valueOf(movie.getImdbRate())%></th>
        <td><a href=<%="\"/movies/" + String.valueOf(movie.getId()) + "\""%>>Link</a></td>
    </tr>
    <% } %>
</table>
</body>
</html>