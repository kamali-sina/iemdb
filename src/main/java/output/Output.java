package output;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import exception.CommandException;
import exception.ErrorType;

public class Output {
    private Integer status;
    private Object data;

    @JsonCreator
    public Output(@JsonProperty("status") Integer status,
                 @JsonProperty("data") Object data) throws CommandException {
        if (status == null ||
                data == null) {
            throw new CommandException(ErrorType.InvalidCommand);
        }
        this.status = status;
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
