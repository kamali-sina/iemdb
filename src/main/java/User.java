import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.CommandException;
import exception.ErrorType;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

public class User {
    private String email;
    private String password;
    private String nickname;
    private String name;
    private String birthDate;
    private HashMap<Integer, Movie> watchList = new HashMap<>();

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

    public void printData() {
        System.out.println(this.email);
        System.out.println(this.password);
        System.out.println(this.nickname);
        System.out.println(this.name);
        System.out.println(this.birthDate);
    }

    public String getSerializedWatchlist() throws IOException {
        JsonFactory factory = new JsonFactory();
        StringWriter jsonObjectWriter = new StringWriter();
        JsonGenerator jsonGenerator = factory.createGenerator(jsonObjectWriter);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeArrayFieldStart("WatchList");
        Collection<Movie> movies = watchList.values();
        for (Movie movie: movies) {
            jsonGenerator.writeRawValue(movie.getSerializedMovieSummary());
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        return jsonObjectWriter.toString();
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
}
