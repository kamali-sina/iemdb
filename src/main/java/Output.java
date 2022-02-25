import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.io.StringWriter;

public class Output {
    private String success;
    private String data;

    public Output(String success, String data) {
        this.success = success;
        this.data = data;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSerializedOutput() throws IOException {
        JsonFactory factory = new JsonFactory();
        StringWriter jsonObjectWriter = new StringWriter();
        JsonGenerator jsonGenerator = factory.createGenerator(jsonObjectWriter);

        jsonGenerator.writeStartObject();

        jsonGenerator.writeFieldName("success");
        jsonGenerator.writeBoolean(this.success.equals("true"));

        jsonGenerator.writeFieldName("data");
        jsonGenerator.writeRawValue(this.data);

        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        return jsonObjectWriter.toString();
    }
}
