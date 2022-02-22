import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class User {
    private final String email;
    private final String password;
    private final String nickname;
    private final String name;
    private final Date birthDate;

    @JsonCreator
    public User(
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("nickname") String nickname,
            @JsonProperty("name") String name,
            @JsonProperty("birthDate") Date birthDate) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.birthDate = birthDate;
    }

    public void printData() {
        System.out.println(this.email);
        System.out.println(this.password);
        System.out.println(this.nickname);
        System.out.println(this.name);
        System.out.println(this.birthDate);
    }
}
