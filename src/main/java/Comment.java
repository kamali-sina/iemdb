import java.time.LocalDate;

public class Comment {
    private static Integer count = 1;

    private String userEmail;
    private Integer movieId;
    private String text;
    private Integer id;
    private LocalDate date;

    public static Integer getCount() {
        return count++;
    }

    public static void setCount(Integer count) {
        Comment.count = count;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String print() {
        String value = "";
        value += this.userEmail;
        value += " " + this.movieId.toString();
        value += " " + this.text;
        return value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
