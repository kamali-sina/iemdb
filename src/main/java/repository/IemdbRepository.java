package repository;

import input.DataReader;
import main.Actor;
import main.Comment;
import main.Movie;
import main.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class IemdbRepository {
    private static IemdbRepository instance;

    public static IemdbRepository getInstance() {
        if (instance == null) {
            try {
                instance = new IemdbRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in IemdbRepository.create query.");
            }
        }
        return instance;
    }

    private IemdbRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        con.setAutoCommit(false);
        Statement stmt = con.createStatement();
        stmt.addBatch("CREATE TABLE IF NOT EXISTS Actors (\n" +
                "    id INT,\n" +
                "    name VARCHAR(100),\n" +
                "    birthDate VARCHAR(100),\n" +
                "    nationality VARCHAR(100),\n" +
                "    image VARCHAR(500),\n" +
                "    PRIMARY KEY(id)\n" +
                ");");
        stmt.addBatch("CREATE TABLE IF NOT EXISTS Movies (\n" +
                "    id INT,\n" +
                "    name VARCHAR(100),\n" +
                "    summery VARCHAR(1000),\n" +
                "    releaseDate VARCHAR(100),\n" +
                "    director VARCHAR(100),\n" +
                "    writers VARCHAR(100),\n" +
                "    imdbRate FLOAT,\n" +
                "    duration INT,\n" +
                "    ageLimit INT,\n" +
                "    image VARCHAR(500),\n" +
                "    coverImage VARCHAR(500),\n" +
                "    PRIMARY KEY(id)\n" +
                ");");
        stmt.addBatch("CREATE TABLE IF NOT EXISTS Users (\n" +
                "    email VARCHAR(100),\n" +
                "    password VARCHAR(100),\n" +
                "    name VARCHAR(100),\n" +
                "    nickname VARCHAR(100),\n" +
                "    birthDate VARCHAR(100),\n" +
                "    PRIMARY KEY(email)\n" +
                ");");
        stmt.addBatch("CREATE TABLE IF NOT EXISTS Ratings (\n" +
                "    userEmail VARCHAR(100),\n" +
                "    movieId INT,\n" +
                "    rate INT,\n" +
                "    PRIMARY KEY(userEmail, movieId),\n" +
                "    FOREIGN KEY (movieId) REFERENCES Movies(id),\n" +
                "    FOREIGN KEY (userEmail) REFERENCES Users(email)\n" +
                ");");
        stmt.addBatch("CREATE TABLE IF NOT EXISTS Comments (\n" +
                "    commentId INT auto_increment,\n" +
                "    userEmail VARCHAR(100),\n" +
                "    movieId INT,\n" +
                "    text VARCHAR(500),\n" +
                "    PRIMARY KEY(commentId),\n" +
                "    FOREIGN KEY (movieId) REFERENCES Movies(id),\n" +
                "    FOREIGN KEY (userEmail) REFERENCES Users(email)\n" +
                ") AUTO_INCREMENT=1;");
        stmt.addBatch("CREATE TABLE IF NOT EXISTS Votes (\n" +
                "    commentId INT,\n" +
                "    userEmail VARCHAR(100),\n" +
                "    vote INT,\n" +
                "    PRIMARY KEY(userEmail, commentId),\n" +
                "    FOREIGN KEY (commentId) REFERENCES Comments(commentId),\n" +
                "    FOREIGN KEY (userEmail) REFERENCES Users(email)\n" +
                ");");
        stmt.addBatch("CREATE TABLE IF NOT EXISTS WatchlistItems (\n" +
                "    userEmail VARCHAR(100),\n" +
                "    movieId INT,\n" +
                "    PRIMARY KEY (userEmail, movieId),\n" +
                "    FOREIGN KEY (movieId) REFERENCES Movies(id),\n" +
                "    FOREIGN KEY (userEmail) REFERENCES Users(email)\n" +
                ");");
        stmt.addBatch("CREATE TABLE IF NOT EXISTS ActorMovies (\n" +
                "    actorId INT,\n" +
                "    movieId INT,\n" +
                "    PRIMARY KEY (actorId, movieId),\n" +
                "    FOREIGN KEY (movieId) REFERENCES Movies(id),\n" +
                "    FOREIGN KEY (actorId) REFERENCES Actors(id)\n" +
                ");");
        stmt.addBatch("CREATE TABLE IF NOT EXISTS Genres (\n" +
                "    name VARCHAR(100),\n" +
                "    PRIMARY KEY(name)\n" +
                ");");
        stmt.addBatch("CREATE TABLE IF NOT EXISTS MovieGenres (\n" +
                "    genre VARCHAR(100),\n" +
                "    movieId INT,\n" +
                "    PRIMARY KEY (genre, movieId),\n" +
                "    FOREIGN KEY (movieId) REFERENCES Movies(id),\n" +
                "    FOREIGN KEY (genre) REFERENCES Genres(name)\n" +
                ");");
        int[] updateCounts = stmt.executeBatch();
        stmt.close();
        con.close();
//        TODO: Complete this
        fillTables();
    }

    private void fillTables() throws SQLException {
        fillActors();
        fillUsers();
        fillMovies();
        fillComments();
    }

    private void fillComments() throws SQLException {
        List<Comment> comments = null;
        try {
            comments = DataReader.readComments();
        }
        catch (Exception e) {
        }
        Connection con = ConnectionPool.getConnection();
        PreparedStatement stmt1 = con.prepareStatement("INSERT INTO Comments (userEmail, movieId, text) VALUES (?, ?, ?) on duplicate key update commentId = commentId");
        assert comments != null;
        comments.forEach(comment -> {
            try {
                stmt1.setString(1, comment.getUserEmail());
                stmt1.setInt(2, comment.getMovieId());
                stmt1.setString(3, comment.getText());
                stmt1.addBatch();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        int[] result1 = stmt1.executeBatch();
        stmt1.close();
        con.close();
    }
    private void fillMovies() throws SQLException {
        List<Movie> movies = null;
        try {
            movies = DataReader.readMovies();
        }
        catch (Exception e) {
        }

        Connection con = ConnectionPool.getConnection();
        PreparedStatement stmt1 = con.prepareStatement("INSERT INTO Movies VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) on duplicate key update id = id");
        PreparedStatement stmt2 = con.prepareStatement("INSERT INTO ActorMovies VALUES (?, ?) on duplicate key update actorId = actorId, movieId = movieId");
        assert movies != null;
        movies.forEach(movie -> {
            try {
                stmt1.setInt(1, movie.getId());
                stmt1.setString(2, movie.getName());
                stmt1.setString(3, movie.getSummary());
                stmt1.setString(4, movie.getReleaseDate());
                stmt1.setString(5, movie.getDirector());
                stmt1.setString(6, movie.getWritersPretty());
                stmt1.setFloat(7, movie.getImdbRate());
                stmt1.setInt(8, movie.getDuration());
                stmt1.setInt(9, movie.getAgeLimit());
                stmt1.setString(10, movie.getImage());
                stmt1.setString(11, movie.getCoverImage());
                stmt1.addBatch();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            movie.getCast().forEach( actorId -> {
                try {
                    stmt2.setInt(1, actorId);
                    stmt2.setInt(2, movie.getId());
                    stmt2.addBatch();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
        });
        int[] result1 = stmt1.executeBatch();
        int[] result2 = stmt2.executeBatch();
        stmt1.close();
        stmt2.close();
        con.close();
    }

    private void fillUsers() throws SQLException {
        List<User> users = null;
        try {
            users = DataReader.readUsers();
        }
        catch (Exception e) {
        }

        Connection con = ConnectionPool.getConnection();
        PreparedStatement stmt1 = con.prepareStatement("INSERT INTO Users VALUES (?, ?, ?, ?, ?) on duplicate key update email = email");
        assert users != null;
        users.forEach(user -> {
            try {
                stmt1.setString(1, user.getEmail());
                stmt1.setString(2, user.getPassword());
                stmt1.setString(3, user.getName());
                stmt1.setString(4, user.getNickname());
                stmt1.setString(5, user.getBirthDate());
                stmt1.addBatch();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        int[] result1 = stmt1.executeBatch();
        stmt1.close();
        con.close();
    }

    private void fillActors() throws SQLException {
        List<Actor> actors = null;
        try {
            actors = DataReader.readActors();
        }
        catch (Exception e) {
        }

        Connection con = ConnectionPool.getConnection();
        PreparedStatement stmt1 = con.prepareStatement("INSERT INTO Actors VALUES (?, ?, ?, ?, ?) on duplicate key update id = id");
        assert actors != null;
        actors.forEach(actor -> {
            try {
                stmt1.setInt(1, actor.getId());
                stmt1.setString(2, actor.getName());
                stmt1.setString(3, actor.getBirthDate());
                stmt1.setString(4, actor.getNationality());
                stmt1.setString(5, actor.getImage());
                stmt1.addBatch();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        int[] result1 = stmt1.executeBatch();
        stmt1.close();
        con.close();
    }
}
