package gis.iwacu_new.rit.edu.main;

import android.util.JsonReader;
import android.util.JsonToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * RecorDocument represents
 */
public class RecorDocument {

    private String baseImageURL;
    private List<RecorContent> content;

    /**
     * Construct a new RecorDocument.
     *
     * @param baseImageURL - image
     * @param content - list of RecorContent objects (specified by <document> tags in the xml
     *                document
     */
    public RecorDocument(String baseImageURL, List<RecorContent> content) {
        this.baseImageURL = baseImageURL;
        this.content = content;
    }

    public String getBaseImageURL() {
        return baseImageURL;
    }

    /**
     * Convenience method to retrieve the recor content at the specified index.
     *
     * @param i - index of recor content to retrieve.
     * @return the recor content at the given index.
     */
    public RecorContent get(int i) {
        return content.get(i);
    }

    /**
     * Convenience method to retrieve the number of recor content objects contained in this
     * document.
     * @return the number of recor content objects in this document.
     */
    public int size() {
        return content.size();
    }

    public List<RecorContent> getContent() {
        return content;
    }


    /**
     * This is the most common way of getting an instance of a RecorDocument.
     *
     * @param in - input stream from which to read when parsing a RecorDocument
     * @return new RecorDocument instance created from the input stream
     * @throws IOException
     */
    public static RecorDocument parse(InputStream in) throws IOException {
        return parseXML(in);
    }

    private static RecorDocument parseJSON(InputStream in) throws IOException {
        try {
            List<RecorContent> recor_doc = new ArrayList<RecorContent>();

            String baseImageUrl = null;

            Object value = new JsonBuilder(new InputStreamReader(in)).parse();
            JSONObject root = (JSONObject)value;
            baseImageUrl = root.getString("image_base_url");
            JSONArray documents = root.getJSONArray("documents");
            for(int i = 0; i < documents.length(); i++) {
                JSONObject obj = documents.getJSONObject(i);
                RecorContent content = new RecorContent();
                recor_doc.add(content);

                content.setAbout(obj.optString("about"));
                content.setHeading(obj.optString("heading"));
                content.setImageUrl(obj.optString("image"));
                content.setQuizURL(obj.optString("quiz"));

                JSONObject videoObject = obj.optJSONObject("video");
                if(videoObject != null) {
                    content.setVideoId(videoObject.getString("id"));
                    content.setVideoText(videoObject.getString("text"));
                }
            }
            return new RecorDocument(baseImageUrl, recor_doc);
        } catch (JSONException e) {
            throw new IOException(e);
        }
    }

    private static RecorDocument parseXML(InputStream in) throws IOException {
        try {
            XmlPullParserFactory factory = null;
            XmlPullParser parser = null;
            String text = null;
            List<RecorContent> recor_doc = new ArrayList<RecorContent>();
            RecorContent document = null;
            String baseImageUrl = null;

            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();

            parser.setInput(in, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    // use start tag, better for accessing attributes
                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase("document")) {
                            // add document object to list
                            document = new RecorContent();
                            recor_doc.add(document);
                        } else if (tagName.equalsIgnoreCase("heading")) {
                            document.setHeading(parser.nextText());
                        } else if (tagName.equalsIgnoreCase("about")) {
                            document.setAbout(parser.nextText());
                        } else if (tagName.equalsIgnoreCase("activity")) {
                            //get attributes from the tag
                            //http://xjaphx.wordpress.com/2011/10/16/android-xml-adventure-parsing-xml-data-with-xmlpullparser/

                            //get the attributes first as the parse seems to miss them if the text is grabbed first
                            document.setActivityName(parser.getAttributeValue(null, "name"));
                            document.setActivityData(parser.getAttributeValue(null, "map_coordinates"));
                            document.setActivity(parser.nextText());
                        } else if (tagName.equalsIgnoreCase("video")) {
                            //get attributes from the tag
                            //http://xjaphx.wordpress.com/2011/10/16/android-xml-adventure-parsing-xml-data-with-xmlpullparser/

                            //get the attributes first as the parse seems to miss them if the text is grabbed first
                            document.setVideoId(parser.getAttributeValue(null, "id"));
                            document.setVideoText(parser.nextText());
                        } else if (tagName.equalsIgnoreCase("image")) {
                            document.setImageUrl(parser.nextText());
                        } else if (tagName.equalsIgnoreCase("quiz")) {
                            document.setQuizURL(parser.nextText());
                        } else if (tagName.equalsIgnoreCase("image_base_url")) {
                            baseImageUrl = parser.nextText();
                            if (!baseImageUrl.endsWith("/")) {
                                baseImageUrl += "/";
                            }
                        }
                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }
            return new RecorDocument(baseImageUrl, recor_doc);
        } catch (XmlPullParserException e) {
            // because I'm lazy we're going to wrap XmlPullParseExcpetions as IOExceptions
            throw new IOException(e);
        }
    }
}
