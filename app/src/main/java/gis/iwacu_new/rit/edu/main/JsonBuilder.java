package gis.iwacu_new.rit.edu.main;

import android.util.JsonReader;
import android.util.JsonToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Reader;

/**
 * Reads JSON from a reader and returns a resulting Object.
 *
 * This is similar to JsonTokener however it reads from a stream instead of from just a String.
 */
public class JsonBuilder {

    /**
     * The reader to read from.
     */
    private JsonReader reader;

    /**
     * Construct a new JsonBuilder.
     * @param in - Source reader to get data from
     */
    public JsonBuilder(Reader in) {
        reader = new JsonReader(in);
    }

    /**
     * Parse the JSON and return a new object from the input.
     *
     * @return An Object representing the parsed content of the input
     * @throws IOException - thrown when reading from input stream fails
     * @throws JSONException - thrown when parsing of JSON fails
     */
    public Object parse() throws IOException, JSONException {
        Object result = parseNext();
        return result;
    }


    /**
     * Internal function for determining the type of object and parsing it.
     *
     * @return An Object corresponding to the parsed data
     * @throws IOException - thrown when reading from input stream fails
     * @throws JSONException - thrown when parsing of JSON fails
     */
    private Object parseNext() throws IOException, JSONException {
        switch(reader.peek()) {
            case BEGIN_OBJECT:
                return parseJsonObject();
            case BEGIN_ARRAY:
                return parseJsonArray();
            case STRING:
                return reader.nextString();
            case BOOLEAN:
                return reader.nextBoolean();
            case NUMBER:
                return reader.nextDouble();
            case NULL:
                reader.nextNull();
                return null;
            default:
                throw new JSONException("Invalid JSON detected");
        }
    }

    /**
     * Internal function for reading a JSONObject
     *
     * @return A parsed JSONObject
     * @throws IOException - thrown when reading from input stream fails
     * @throws JSONException - thrown when parsing of JSON fails
     */
    private JSONObject parseJsonObject() throws IOException, JSONException {
        JSONObject result = new JSONObject();
        reader.beginObject();
        while(reader.peek() != JsonToken.END_OBJECT) {
            if(reader.peek() == JsonToken.NAME) {
                String name = reader.nextName();
                Object value = parseNext();
                result.put(name, value);
            }
        }
        reader.endObject();
        return result;
    }

    /**
     * Internal function for reading a JSONArray
     *
     * @return A parsed JSONArray
     * @throws IOException - thrown when reading from input stream fails
     * @throws JSONException - thrown when parsing of JSON fails
     */
    private JSONArray parseJsonArray() throws IOException, JSONException {
        JSONArray result = new JSONArray();
        reader.beginArray();
        while(reader.peek() != JsonToken.END_ARRAY) {
            result.put(parseNext());
        }
        reader.endArray();
        return result;
    }
}
