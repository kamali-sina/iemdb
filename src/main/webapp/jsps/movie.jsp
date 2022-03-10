<%@ page import="manager.MovieManager" %>
<%@ page import="main.Movie" %>
<%@ page import="exception.CommandException" %>
<%@ page import="main.Comment" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="manager.UserManager" %>
<%@ page import="main.Actor" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Movie</title>
  <style>
    li, td, th {
      padding: 5px;
    }
  </style>
</head>
<body>
<%
  Integer movieId = Integer.valueOf(request.getParameter("movieId"));
  Movie movie = MovieManager.getMovie(movieId);
%>
<a href="/">Home</a>
<p id="email">email: <%=UserManager.getLoggedInUser().getEmail()%></p>
<ul>
  <li id="name">name: <%=movie.getName()%></li>
  <li id="summary">
    summary: <%=movie.getSummary()%>
  </li>
  <li id="releaseDate">releaseDate: <%=movie.getReleaseDate()%></li>
  <li id="director">director: <%=movie.getDirector()%></li>
  <li id="writers">writers: <%=movie.getWritersPretty()%></li>
  <li id="genres">genres: <%=movie.getGenresPretty()%></li>
  <li id="cast">cast: <%=movie.getCastPretty()%></li>
  <li id="imdbRate">imdb Rate: <%=movie.getImdbRate()%></li>
  <li id="rating">rating: <%=movie.getAverageRatingRate()%></li>
  <li id="duration">duration: <%=movie.getDuration()%> minutes</li>
  <li id="ageLimit">ageLimit: <%=movie.getAgeLimit()%></li>
</ul>
<h3>Cast</h3>
<table>
  <tr>
    <th>name</th>
    <th>age</th>
  </tr>
  <% for(Actor actor : movie.getActors()) { %>
  <tr>
    <td><%=actor.getName()%></td>
    <td><%=actor.getAge()%></td>
    <td><a href="actors/<%=actor.getId()%>">Link</a></td>
  </tr>
  <% } %>
</table>
<br>
<form action="" method="POST">
  <label>Rate(between 1 and 10):</label>
  <input type="number" id="quantity" name="quantity" min="1" max="10">
  <input type="hidden" id="form_action" name="action" value="rate">
  <input type="hidden" id="form_movie_id" name="movie_id" value=<%=movie.getId()%>>
  <button type="submit">rate</button>
</form>
<br>
<form action="" method="POST">
  <input type="hidden" id="form_action" name="action" value="add">
  <input type="hidden" id="form_movie_id" name="movie_id" value=<%=movie.getId()%>>
  <button type="submit">Add to WatchList</button>
</form>
<br />
<table>
  <tr>
    <th>nickname</th>
    <th>comment</th>
    <th></th>
    <th></th>
  </tr>
  <%for (ArrayList<Comment> userComment : movie.getComments().values()) {
    for (Comment comment : userComment) {
  %>
  <tr>
    <td>@<%=UserManager.getUser(comment.getUserEmail()).getNickname()%></td>
    <td><%=comment.getText()%></td>
    <td>
      <form action="" method="POST">
        <label><%=comment.getNumberOfLikes()%></label>
        <input type="hidden" id="form_action" name="action" value="like">
        <input type="hidden" id="form_comment_id" name="comment_id" value=<%=comment.getId()%>>
        <button type="submit">like</button>
      </form>
    </td>
    <td>
      <form action="" method="POST">
        <label><%=comment.getNumberOfDislikes()%></label>
        <input type="hidden" id="form_action" name="action" value="dislike">
        <input type="hidden" id="form_comment_id" name="comment_id" value=<%=comment.getId()%>>
        <button type="submit">dislike</button>
      </form>
    </td>
  </tr>
  <%  }
  }%>
</table>
<br><br>
<form action="" method="POST">
  <label>Your Comment:</label>
  <input type="text" name="comment" value="">
  <input type="hidden" id="form_action" name="action" value="comment">
  <input type="hidden" id="form_movie_id" name="movie_id" value=<%=movie.getId()%>>
  <button type="submit">Add Comment</button>
</form>
</body>
</html>

