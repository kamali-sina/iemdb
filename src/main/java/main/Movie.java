package main;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import exception.CommandException;
import exception.ErrorType;
import manager.ActorManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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
    private String image;
    private String coverImage;
    private HashMap<String, Rating> ratings = new HashMap<>();
    private Integer ratingCount = 0;
    private HashMap<String, ArrayList<Comment>> comments = new HashMap<>();
    private Float averageRating;
    private Float recommendationScore;

    @JsonCreator
    public Movie(@JsonProperty("id") Integer id,
                 @JsonProperty("name") String name,
                 @JsonProperty("summary") String summary,
                 @JsonProperty("releaseDate") String releaseDate,
                 @JsonProperty("director") String director,
                 @JsonProperty("writers") ArrayList<String> writers,
                 @JsonProperty("genres") ArrayList<String> genres,
                 @JsonProperty("cast") ArrayList<Integer> cast,
                 @JsonProperty("imdbRate") Float imdbRate,
                 @JsonProperty("duration") Integer duration,
                 @JsonProperty("ageLimit") Integer ageLimit,
                 @JsonProperty("image") String image,
                 @JsonProperty("coverImage") String coverImage) throws CommandException {
        if (id == null ||
                name == null ||
                summary == null ||
                releaseDate == null ||
                director == null ||
                imdbRate == null ||
                duration == null ||
                ageLimit == null ||
                image == null ||
                coverImage == null) {
            throw new CommandException(ErrorType.InvalidCommand);
        }
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.releaseDate = releaseDate;
        this.director = director;
        this.writers = writers;
        this.genres = genres;
        this.cast = cast;
        this.imdbRate = imdbRate;
        this.duration = duration;
        this.ageLimit = ageLimit;
        this.image = image;
        this.coverImage = coverImage;
    }

    private static Float round(Float value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (float) Math.round(value * scale) / scale;
    }

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

    public HashMap<String, ArrayList<Comment>> getComments() {
        return comments;
    }

    public void setComments(HashMap<String, ArrayList<Comment>> comments) {
        this.comments = comments;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Float getRecommendationScore() {
        return recommendationScore;
    }

    public void setRecommendationScore(Float recommendationScore) {
        this.recommendationScore = recommendationScore;
    }

    public String getWritersPretty() {
        return getListPretty(this.getWriters());
    }

    public String getGenresPretty() {
        return getListPretty(this.getGenres());
    }

    public String getCastPretty() {
        try {
            String cast = "";
            String delimiter = "";
            for (Integer actorId : this.getCast()) {
                cast += delimiter;
                cast += ActorManager.getActor(actorId).getName();
                delimiter = ", ";
            }

            return cast;
        } catch (CommandException  commandException) {
            return commandException.getMessage();
        }
    }

    public static String getListPretty(ArrayList<String> list) {
        String prettyList = "";
        String delimiter = "";
        for (String item : list) {
            prettyList += delimiter;
            prettyList += item;
            delimiter = ", ";
        }
        return prettyList;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Float getAverageRatingRate() {
        return averageRating;
    }

    public void setAverageRating(Float averageRating) {
        this.averageRating = averageRating;
    }

    public Integer addComment(Comment comment) {
        comment.setId(Comment.getCount());
        comment.setDate(LocalDate.now());

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
        return comment.getId();
    }

    public void addRating(Rating rating) throws CommandException {
        Integer ratingScore = rating.getScore();
        if (!(Rating.minScore <= ratingScore && ratingScore <= Rating.maxScore)) {
            throw new CommandException(ErrorType.InvalidRateScore);
        }

        if (!this.ratings.containsKey(rating.getUserEmail())) {
            this.ratingCount += 1;
        }

        this.ratings.put(rating.getUserEmail(), rating);
        this.calculateAverageRating();
    }

    public Float calculateAverageRating() {
        Float averageRating = 0.0f;
        for (Rating rating : this.ratings.values()) {
            averageRating += rating.getScore();
        }
        averageRating /= this.ratings.values().size();
        this.averageRating = round(averageRating, 1);
        return this.averageRating;
    }

    public Comment findComment(Integer commentId) {
        for (ArrayList<Comment> userComments : this.getComments().values()) {
            for (Comment comment : userComments) {
                if (Objects.equals(comment.getId(), commentId)) {
                    return comment;
                }
            }
        }
        return null;
    }

    public LocalDate calculateLocalReleaseDate() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return LocalDate.parse(this.getReleaseDate(), format);
    }

    public Integer calculateReleaseYear() {
        if (this.getReleaseDate().equals("-")) {
            return -1;
        }
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate date = LocalDate.parse(this.getReleaseDate(), format);
        return date.getYear();
    }
}
