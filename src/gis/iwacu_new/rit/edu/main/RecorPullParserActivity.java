//*** IS THIS CLASS STILL NEEDED? IF NOT, REMOVE *** 

package gis.iwacu_new.rit.edu.main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


import gis.iwacu_new.rit.edu.main.R;
import gis.iwacu_new.rit.edu.main.RecorContent;
import gis.iwacu_new.rit.edu.main.RecorContentPullParserHandler;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RecorPullParserActivity extends Activity implements OnClickListener, OnItemClickListener
{
	ListView listView;
	
	String classNames [] = {"GeometrySampleActivity","GPS"};
	
	RecorContentPullParserHandler parser = new RecorContentPullParserHandler();
	
	List<RecorContent> recor_doc = null;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.parser_main);

        listView = (ListView) findViewById(R.id.list); 
        
        
        
    
       
        try
        {
        	
        	
        	//get the learning content from external storage, should be updated based on checks done when the app is opening
        	File SDCardRoot = Environment.getExternalStorageDirectory();
        	File IwacuDir = new File(SDCardRoot,getResources().getString((R.string.Iwacu_Directory)));
        	File Learning_Content_File = new File(IwacuDir,getResources().getString((R.string.learning_file_name)));
    
        	//http://developer.android.com/reference/java/io/FileInputStream.html
        	 InputStream in = null;
        	 in = new BufferedInputStream(new FileInputStream(Learning_Content_File));
        	 recor_doc = parser.parse(in);
 		    
        	
        	
        	//recor_doc = parser.parse(getAssets().open("project.xml"));
        	
        	//https://github.com/thecodepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
        	//http://developer.android.com/guide/topics/ui/declaring-layout.html 
        	//http://developer.android.com/training/implementing-navigation/lateral.html 
        	 
         
        	 //still needed?
        	ArrayAdapter<RecorContent> adapter = new ArrayAdapter<RecorContent>(this, R.layout.list_item, recor_doc);
     	    listView.setAdapter(adapter);
        	
        }
        catch (IOException e)
        {
        	e.printStackTrace();
        }
	}
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position)
		{
		case 0:  Intent RecorPullParserActivity = new Intent(this, RecorPullParserActivity.class);     
        startActivity(RecorPullParserActivity);
        break;
		case 1:  Intent GeometrySampleActivity = new Intent(this, GeometrySampleActivity.class);     
        startActivity(GeometrySampleActivity);
        break;
		case 2:  Intent GPS = new Intent(this, RecorPullParserActivity.class);     
        startActivity(GPS);
        break;
		}
		
		
	}
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
