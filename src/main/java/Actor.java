import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;

public class Actor {
    private final int id;
    private final String name;
    private final Date birthDate;
    private final String nationality;

    @JsonCreator
    public Actor(
            @JsonProperty("id") int id,
            @JsonProperty("name") String name,
            @JsonProperty("birthDate") Date birthDate,
            @JsonProperty("nationality") String nationality) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.nationality = nationality;
    }

    public void printData() {
        System.out.println(this.id);
        System.out.println(this.name);
        System.out.println(this.birthDate);
        System.out.println(this.nationality);
    }
}
