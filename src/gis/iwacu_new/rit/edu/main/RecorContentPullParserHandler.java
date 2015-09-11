package gis.iwacu_new.rit.edu.main;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import gis.iwacu_new.rit.edu.main.RecorContent;

public class RecorContentPullParserHandler 
{
	List <RecorContent> recor_doc;
	
	private RecorContent document;
	private String text;
	
	public RecorContentPullParserHandler ()
	{
		recor_doc = new ArrayList<RecorContent>();
	}
	
	public List <RecorContent>getRecor()
	{
		return recor_doc;
	}
	public List<RecorContent> parse(InputStream is)
	{
		XmlPullParserFactory factory = null;
		XmlPullParser parser = null;
		
		try
		{
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			parser = factory.newPullParser();
			
			parser.setInput(is, null);
			
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT)
			{
				String tagname = parser.getName();
				switch(eventType)
				{
				case XmlPullParser.START_DOCUMENT:
					//if (tagname.equalsIgnoreCase("document"))
					//{
						// create a new instance of document
						//? document = new RecorContent();				
					//}
					break;
					
				case XmlPullParser.TEXT:
					text = parser.getText();
					break;
					
				//use start tag, better for accessing attributes
				//*** TODO - find better way then using hard-coded strings for tag names
				case XmlPullParser.START_TAG:
					if (tagname.equalsIgnoreCase("document"))
					{
						// add document object to list
						document = new RecorContent();
						recor_doc.add(document);
					}
					else if (tagname.equalsIgnoreCase("heading"))
					{
						//document.setHeading(text);
						
						document.setHeading(parser.nextText());
						
					}
					else if (tagname.equalsIgnoreCase("about"))
					{
						//document.setAbout(text);
						document.setAbout(parser.nextText());
					}
					else if (tagname.equalsIgnoreCase("activity"))
					{
						
						
						//get attributes from the tag
						//http://xjaphx.wordpress.com/2011/10/16/android-xml-adventure-parsing-xml-data-with-xmlpullparser/
						
						//get the attributes first as the parse seems to miss them if the text is grabbed first
						document.setActivityName(parser.getAttributeValue(null, "name"));
						document.setActivityData(parser.getAttributeValue(null, "map_coordinates"));
						document.setActivity(parser.nextText());
						
					}
					else if (tagname.equalsIgnoreCase("video"))
					{
						
						
						//get attributes from the tag
						//http://xjaphx.wordpress.com/2011/10/16/android-xml-adventure-parsing-xml-data-with-xmlpullparser/
						
						//get the attributes first as the parse seems to miss them if the text is grabbed first
						document.setVideoId(parser.getAttributeValue(null, "id"));
						document.setVideoText(parser.nextText());
						
					}
					else if (tagname.equalsIgnoreCase("image"))
					{
					
						document.setImageUrl(parser.nextText());
						
						
					}
					else if (tagname.equalsIgnoreCase("quiz"))
					{
						document.setQuizURL(parser.nextText());
						//
					}
					break;
					
					default:
						break;
				}
				eventType = parser.next();
			}
		}
		catch (XmlPullParserException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return recor_doc;
	}
	
	
}