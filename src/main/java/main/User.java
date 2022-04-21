package main;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import exception.CommandException;
import exception.ErrorType;
import manager.MovieManager;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

public class User {
    public static final int RECOMMENDATION_COUNT = 3;
    private String email;
    private String password;
    private String nickname;
    private String name;
    private String birthDate;
    private HashMap<Integer, Movie> watchList = new HashMap<>();

    @JsonCreator
    public User(@JsonProperty("email") String email,
                @JsonProperty("password") String password,
                @JsonProperty("nickname") String nickname,
                @JsonProperty("name") String name,
                @JsonProperty("birthDate") String birthDate) throws CommandException {
        if (email == null ||
                password == null ||
                nickname == null ||
                name == null ||
                birthDate == null) {
            throw new CommandException(ErrorType.InvalidCommand);
        }
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public HashMap<Integer, Movie> getWatchList() {
        return watchList;
    }

    public void setWatchList(HashMap<Integer, Movie> watchList) {
        this.watchList = watchList;
    }

    public void printData() {
        System.out.println(this.email);
        System.out.println(this.password);
        System.out.println(this.nickname);
        System.out.println(this.name);
        System.out.println(this.birthDate);
    }

    public int getAge() {
        LocalDate dateOfBirth = LocalDate.parse(getBirthDate());
        LocalDate currentDate = LocalDate.now();
        return Period.between(dateOfBirth, currentDate).getYears();
    }

    public void addToWatchList(Movie movie) throws CommandException {
        if (getAge() < movie.getAgeLimit()) {
            throw new CommandException(ErrorType.AgeLimitError);
        }
        if (watchList.containsKey(movie.getId())) {
            throw new CommandException(ErrorType.MovieAlreadyExists);
        }
        watchList.put(movie.getId(), movie);
    }

    public void removeFromWatchList(Movie movie) throws CommandException {
        if (!watchList.containsKey(movie.getId())) {
            throw new CommandException(ErrorType.MovieNotFound);
        }
        watchList.remove(movie.getId());
    }

    private Double getMovieGenreSimilarity(Movie movie) {
        Double score = movie.getImdbRate();
        for (Movie watchlist_movie : watchList.values()) {
            score += MovieManager.countNumberOfSameGenres(movie, watchlist_movie);
        }
        return score;
    }

    private Double getMovieRecommendationScore(Movie movie) {
        Double score = movie.getImdbRate();
        if (movie.getAverageRatingRate() != null) {
            score += movie.getAverageRatingRate();
        }
        score += 3 * getMovieGenreSimilarity(movie);
        return score;
    }

    public List<Movie> getWatchlistRecommendations() {
        ArrayList<Movie> movies_not_in_watchlist = new ArrayList<>();
        for (Movie movie : MovieManager.movies.values()) {
            if (!watchList.containsValue(movie)) {
                movies_not_in_watchlist.add(movie);
            }
        }
        for (Movie movie : movies_not_in_watchlist) {
            movie.setRecommendationScore(getMovieRecommendationScore(movie));
        }
        movies_not_in_watchlist.sort(Comparator.comparing(Movie::getRecommendationScore).reversed());
        if (movies_not_in_watchlist.size() < RECOMMENDATION_COUNT) {
            return  movies_not_in_watchlist;
        }
        return movies_not_in_watchlist.subList(0, RECOMMENDATION_COUNT);
    }
}
