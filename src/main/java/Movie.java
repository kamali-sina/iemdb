import java.util.ArrayList;
import java.util.HashMap;

public class Movie {
    private Integer id;
    private String name;
    private String summary;
    private String releaseDate;
    private String director;
    private ArrayList<String> writers;
    private ArrayList<String> genres;
    private ArrayList<Integer> cast;
    private Float imdbRate;
    private Integer duration;
    private Integer ageLimit;
    private HashMap<String, Rating> ratings = new HashMap<>();
    private Integer ratingCount = 0;
    private HashMap<String, ArrayList<Comment>> comments = new HashMap<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public ArrayList<String> getWriters() {
        return writers;
    }

    public void setWriters(ArrayList<String> writers) {
        this.writers = writers;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public ArrayList<Integer> getCast() {
        return cast;
    }

    public void setCast(ArrayList<Integer> cast) {
        this.cast = cast;
    }

    public Float getImdbRate() {
        return imdbRate;
    }

    public void setImdbRate(Float imdbRate) {
        this.imdbRate = imdbRate;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(Integer ageLimit) {
        this.ageLimit = ageLimit;
    }

    public HashMap<String, Rating> getRatings() {
        return ratings;
    }

    public void setRatings(HashMap<String, Rating> ratings) {
        this.ratings = ratings;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public HashMap<String, ArrayList<Comment>> getComments() {
        return comments;
    }

    public void setComments(HashMap<String, ArrayList<Comment>> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        // java.util.UUID.randomUUID(); TODO: ADD unique ID for each comment.
        // TODO: Add date and time for each comment.

        ArrayList<Comment> userComments;
        // Retrieve user comments
        if (!this.comments.containsKey(comment.getUserEmail())) {
            userComments = new ArrayList<>();
            this.comments.put(comment.getUserEmail(), userComments);
        } else {
            userComments = this.comments.get(comment.getUserEmail());
        }

        // Add new comment
        userComments.add(comment);

        // Update user comments
        this.comments.put(comment.getUserEmail(), userComments);
    }

    public void addRating(Rating rating) {
        if (!this.ratings.containsKey(rating.getUserEmail())) {
            this.ratingCount += 1;
        }
        this.ratings.put(rating.getUserEmail(), rating);
        // TODO: Update movie's average rating
    }
}
