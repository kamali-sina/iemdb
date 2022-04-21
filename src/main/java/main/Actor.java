package main;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import exception.CommandException;
import exception.ErrorType;

public class Actor {

    private Integer id;

    private String name;

    private String birthDate;

    private String nationality;

    private String image;

    @JsonCreator
    public Actor(@JsonProperty("id") Integer id,
                 @JsonProperty("name") String name,
                 @JsonProperty("birthDate") String birthDate,
                 @JsonProperty("nationality") String nationality,
                 @JsonProperty("image") String image) throws CommandException {
        if (id == null ||
                name == null ||
                birthDate == null ||
                nationality == null ||
                image == null) {
            throw new CommandException(ErrorType.InvalidCommand);
        }
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.nationality = nationality;
        this.image = image;
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
