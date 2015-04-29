package gis.iwacu_new.rit.edu.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class RecorContentPullParserHandler {
    List<RecorContent> recor_doc;

    private RecorContent document;
    private String text;
    private String baseImageUrl = null;

    public RecorContentPullParserHandler() {
        recor_doc = new ArrayList<RecorContent>();
    }

    public List<RecorContent> getRecor() {
        return recor_doc;
    }

    public List<RecorContent> parse(InputStream is) {
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;

        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();

            parser.setInput(is, null);

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
                            document.setImageUrl(baseImageUrl + parser.nextText());
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
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recor_doc;
    }


}
