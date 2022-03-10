<%@ page import="manager.UserManager" %>
<%@ page import="manager.MovieManager" %>
<%@ page import="main.Movie" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Movies</title>
    <style>
      li, td, th {
        padding: 5px;
      }
    </style>
</head>
<body>
    <a href="/">Home</a>
    <p id="email">email: <%=UserManager.getLoggedInUser().getEmail()%></p>
    <br><br>
    <form action="" method="POST">
        <label>Search:</label>
        <input type="text" name="search" value="">
        <button type="submit" name="action" value="search">Search</button>
        <button type="submit" name="action" value="clear">Clear Search</button>
    </form>
    <br><br>
    <form action="" method="POST">
        <label>Sort By:</label>
        <button type="submit" name="action" value="sort_by_imdb">imdb Rate</button>
        <button type="submit" name="action" value="sort_by_date">releaseDate</button>
    </form>
    <br>
    <table>
        <tr>
            <th>name</th>
            <th>summary</th> 
            <th>releaseDate</th>
            <th>director</th>
            <th>writers</th>
            <th>genres</th>
            <th>cast</th>
            <th>imdb Rate</th>
            <th>rating</th>
            <th>duration</th>
            <th>ageLimit</th>
            <th>Links</th>
        </tr>
        <% for(Movie movie : MovieManager.getSearchMoviesResultSorted(MovieManager.getQuery(), MovieManager.getFilter())) { %>
        <tr>
            <td><%=movie.getName()%></td>
            <td><%=movie.getSummary()%></td>
            <td><%=movie.getReleaseDate()%></td>
            <td><%=movie.getDirector()%></td>
            <td><%=movie.getWritersPretty()%></td>
            <td><%=movie.getGenresPretty()%></td>
            <td><%=movie.getCastPretty()%></td>
            <td><%=movie.getImdbRate()%></td>
            <td><%=movie.getAverageRatingRate()%></td>
            <td><%=movie.getDuration()%></td>
            <td><%=movie.getAgeLimit()%></td>
            <td><a href="/movies/<%=movie.getId()%>">Link</a></td>
        </tr>
        <% } %>
    </table>
</body>
</html>
