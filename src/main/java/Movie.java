import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import exception.CommandException;
import exception.ErrorType;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
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
    private Double imdbRate;
    private Integer duration;
    private Integer ageLimit;
    private HashMap<String, Rating> ratings = new HashMap<>();
    private Integer ratingCount = 0;
    private HashMap<String, ArrayList<Comment>> comments = new HashMap<>();
    private Double averageRating;

    @JsonCreator
    public Movie(@JsonProperty("id") Integer id,
                 @JsonProperty("name") String name,
                 @JsonProperty("summary") String summary,
                 @JsonProperty("releaseDate") String releaseDate,
                 @JsonProperty("director") String director,
                 @JsonProperty("writers") ArrayList<String> writers,
                 @JsonProperty("genres") ArrayList<String> genres,
                 @JsonProperty("cast") ArrayList<Integer> cast,
                 @JsonProperty("imdbRate") Double imdbRate,
                 @JsonProperty("duration") Integer duration,
                 @JsonProperty("ageLimit") Integer ageLimit) throws CommandException {
        if (id == null ||
                name == null ||
                summary == null ||
                releaseDate == null ||
                director == null ||
                writers == null ||
                genres == null ||
                cast == null ||
                imdbRate == null ||
                duration == null ||
                ageLimit == null) {
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
    }

    private static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
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

    public Double getImdbRate() {
        return imdbRate;
    }

    public void setImdbRate(Double imdbRate) {
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

    public Double getAverageRatingRate() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
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

    public Double calculateAverageRating() {
        Double averageRating = 0.0;
        for (Rating rating : this.ratings.values()) {
            averageRating += rating.getScore();
        }
        averageRating /= this.ratings.values().size();
        this.averageRating = round(averageRating, 1);
        return this.averageRating;
    }

    public String getSerializedMovieSummary() throws IOException {
        JsonFactory factory = new JsonFactory();
        StringWriter jsonObjectWriter = new StringWriter();
        JsonGenerator jsonGenerator = factory.createGenerator(jsonObjectWriter);

        jsonGenerator.writeStartObject();

        jsonGenerator.writeNumberField("movieId", this.getId());
        jsonGenerator.writeStringField("name", this.getName());
        jsonGenerator.writeStringField("director", this.getDirector());
        jsonGenerator.writeFieldName("genres");
        jsonGenerator.writeArray(this.getGenres().toArray(new String[0]), 0, this.getGenres().size());
        if (this.ratingCount == 0) {
            jsonGenerator.writeNullField("rating");
        } else {
            jsonGenerator.writeNumberField("rating", this.calculateAverageRating());
        }

        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        return jsonObjectWriter.toString();
    }

    public String getSerializedMovieWithDetails() throws IOException, CommandException {
        JsonFactory factory = new JsonFactory();
        StringWriter jsonObjectWriter = new StringWriter();
        JsonGenerator jsonGenerator = factory.createGenerator(jsonObjectWriter);

        jsonGenerator.writeStartObject();

        jsonGenerator.writeNumberField("movieId", this.getId());
        jsonGenerator.writeStringField("name", this.getName());
        jsonGenerator.writeStringField("summary", this.getSummary());
        jsonGenerator.writeStringField("releaseDate", this.getReleaseDate());
        jsonGenerator.writeStringField("director", this.getDirector());
        jsonGenerator.writeFieldName("writers");
        jsonGenerator.writeArray(this.getWriters().toArray(new String[0]), 0, this.getWriters().size());
        jsonGenerator.writeFieldName("genres");
        jsonGenerator.writeArray(this.getGenres().toArray(new String[0]), 0, this.getGenres().size());

        jsonGenerator.writeArrayFieldStart("cast");
        for (Integer actorId : this.getCast()) {
            jsonGenerator.writeRawValue(ActorManager.getActor(actorId).getSerializedActorSummary());
        }
        jsonGenerator.writeEndArray();

        if (this.ratingCount == 0) {
            jsonGenerator.writeNullField("rating");
        } else {
            jsonGenerator.writeNumberField("rating", this.calculateAverageRating());
        }

        jsonGenerator.writeNumberField("duration", this.getDuration());
        jsonGenerator.writeNumberField("ageLimit", this.getAgeLimit());

        jsonGenerator.writeArrayFieldStart("comments");
        for (ArrayList<Comment> userComments : this.getComments().values()) {
            for (Comment comment : userComments) {
                jsonGenerator.writeRawValue(comment.getSerializedCommentWithDetails());
            }
        }
        jsonGenerator.writeEndArray();

        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        return jsonObjectWriter.toString();
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
}
