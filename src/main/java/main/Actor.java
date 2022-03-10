package main;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import exception.CommandException;
import exception.ErrorType;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Actor {

    private Integer id;

    private String name;

    private String birthDate;

    private String nationality;

    @JsonCreator
    public Actor(@JsonProperty("id") Integer id,
                 @JsonProperty("name") String name,
                 @JsonProperty("birthDate") String birthDate,
                 @JsonProperty("nationality") String nationality) throws CommandException {
        if (id == null ||
                name == null ||
                birthDate == null ||
                nationality == null) {
            throw new CommandException(ErrorType.InvalidCommand);
        }
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.nationality = nationality;
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

    public void printData() {
        System.out.println(this.id);
        System.out.println(this.name);
        System.out.println(this.birthDate);
        System.out.println(this.nationality);
    }

    public String getSerializedActorSummary() throws IOException {
        JsonFactory factory = new JsonFactory();
        StringWriter jsonObjectWriter = new StringWriter();
        JsonGenerator jsonGenerator = factory.createGenerator(jsonObjectWriter);

        jsonGenerator.writeStartObject();

        jsonGenerator.writeNumberField("actorId", this.getId());
        jsonGenerator.writeStringField("name", this.getName());

        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        return jsonObjectWriter.toString();
    }

    public Integer getAge() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(this.getBirthDate(), format);
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
