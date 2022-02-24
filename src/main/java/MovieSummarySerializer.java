import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class MovieSummarySerializer extends StdSerializer<Movie> {
    public MovieSummarySerializer() {
        this(null);
    }

    public MovieSummarySerializer(Class<Movie> t) {
        super(t);
    }

    @Override
    public void serialize(
            Movie movie, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("movieId", movie.getId());
        jsonGenerator.writeStringField("name", movie.getName());
        jsonGenerator.writeStringField("director", movie.getDirector());
        jsonGenerator.writeFieldName("genres");
        jsonGenerator.writeArray(movie.getGenres().toArray(new String[0]), 0, movie.getGenres().size());
        jsonGenerator.writeNumberField("rating", movie.calculateAverageRating());
        jsonGenerator.writeEndObject();
    }
}
