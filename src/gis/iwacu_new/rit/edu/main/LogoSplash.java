package gis.iwacu_new.rit.edu.main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import gis.iwacu_new.rit.edu.main.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
 
public class LogoSplash extends Activity {

	private static String TAG = SplashActivity.class.getName();
	private static long SLEEP_TIME = 5;	// Sleep for some time

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
      	this.requestWindowFeature(Window.FEATURE_NO_TITLE);	// Removes title bar
      	this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);	// Removes notification bar
        
      	setContentView(R.layout.logo_splash);
      	
      	//start processing things needed when the app loads like new content offline databaseses etc.
    	//open from a web URL, not as a local asset
    	//URL is stored in an asset file to make flexible
    	//later will want to make this an outside file
    	//need to be aware of this issue though
    	//http://developer.android.com/training/articles/perf-anr.html
    	
    	
    	//http://stackoverflow.com/questions/16333145/save-xml-from-url-and-read-it
    	
    	
    	try {
	    	//get the text file that has the URL for the other project assets
    		InputStream Project_Web_Assets_Location = getAssets().open("project_web_assets_URL.txt");
	    	
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(Project_Web_Assets_Location));
	        StringBuilder out = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            out.append(line);
	        }
	       
	        reader.close();
    	
       
        	URL url = new URL(out.toString());
        	new DownloadWebAssetsTask().execute(url);
	    	
      	
    	} catch (IOException IOe) {
    		IOe.printStackTrace();
    	}
      	
        // Start timer and launch main activity
        IntentLauncher launcher = new IntentLauncher();
        launcher.start();
	}
	
	private class IntentLauncher extends Thread {
    
		@Override
    	/**
    	 * Sleep for some time and than start new activity.
    	 */
		public void run() {
    		try {
            	// Sleeping
    			Thread.sleep(SLEEP_TIME*1000);
            } catch (Exception e) {
            	Log.e(TAG, e.getMessage());
            }
            
            // Start main activity
          	Intent intent = new Intent(LogoSplash.this, XMLorPDF.class);
          	LogoSplash.this.startActivity(intent);
          	LogoSplash.this.finish();
    	}
    }
	

	private class DownloadWebAssetsTask extends AsyncTask<URL,Void, Void> {
	    // Do the long-running work in here
	    protected Void doInBackground(URL...urls) {
	    	
	    	
	    	 	//URL input_url =  new URL(urls[0]);
	        	URLConnection connection = null;
				try {
					connection = urls[0].openConnection();
					
					InputStream WebAsset_ContentStream =  connection.getInputStream();
					
					BufferedReader reader = new BufferedReader(new InputStreamReader(WebAsset_ContentStream));
			        StringBuilder out = new StringBuilder();
			        String line;
			        while ((line = reader.readLine()) != null) {
			            out.append(line);
			            //inspect each line of the input file and take action accordingly
			            String [] temp_split = line.split( getResources().getString((R.string.split_character1)));
			            
			            if (temp_split[0].equalsIgnoreCase(getResources().getString((R.string.learning_content)))) {
			            //this is XML content, check version and download and store locally if need be
			            	
			            	
			            	//http://developer.android.com/training/basics/data-storage/shared-preferences.html
			            	SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
			            	String learning_version = getResources().getString((R.string.learning_version));
			            	String learningVersionDefault = getResources().getString((R.string.learning_version_default));
			            	String current_version = sharedPref.getString(learning_version, learningVersionDefault);
			            	
			            	//check the current version stored on the device against the web version
			            	String XMLVersion [] = temp_split[1].split(getResources().getString((R.string.split_character2))); //will come back with [1] being like v1.xml
			            	String WebVersion = XMLVersion [1].substring(1, 2);
			            	
			            	
			            	if (!WebVersion.equalsIgnoreCase(current_version)){ 
			            		//new version was found, download, store locally and update current version
			            		
			            		WriteLocalContent(temp_split[1]);
			            		
			            		//update internal version number after writing contents locally
			            		//SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
			            		SharedPreferences.Editor editor = sharedPref.edit();
			            		editor.putString(getResources().getString((R.string.learning_version)), WebVersion);
			            		editor.commit();
			            		
			            		
			            		//Log.e(current_version,"yo");
			            		
			            	}
			            	
			            	
			            	
			            } else if (temp_split[0].equalsIgnoreCase(getResources().getString((R.string.offline_database)))) {
			            	//todo - follow similar procedure as XML-based learning content
			            	SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
			            	String offline_database_version = getResources().getString((R.string.offline_database_version));
			            	String offline_database_version_default = getResources().getString((R.string.offline_database_version_default));
			            	String current_version = sharedPref.getString(offline_database_version, offline_database_version_default);
			            	
			            	
			            	//check the current version stored on the device against the web version
			            	String DatabaseVersion [] = temp_split[1].split(getResources().getString((R.string.split_character2))); //will come back with [1] being like v1.xml
			            	String DatabaseWebVersion = DatabaseVersion [1].substring(1, 2);
			            	
			            	if (!DatabaseWebVersion.equalsIgnoreCase(current_version)){ 
			            		//new version was found, download, store locally and update current version
			            		
			            		//more redundant code..
			            		if (isExternalStorageWritable()) {
			            			
			            			try {
			            				
			            				
			            				 URL url = new URL(temp_split[1]);
			            				 //create the new connection
			            			     HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			            			     urlConnection.connect();
			            			     
			            				//location on device
			            				
			            			    File SDCardRoot = Environment.getExternalStorageDirectory();
			            			    //create the directory 
			            			    File DatabaseDir = new File(SDCardRoot,getResources().getString((R.string.Iwacu_Directory)) + getResources().getString((R.string.offline_map_database_directory)));
			            			    if (!DatabaseDir.exists()) {
			            			    	DatabaseDir.mkdirs();
			            			    }
			            			    
			            			    
			            			    File file = new File(DatabaseDir,getResources().getString((R.string.offline_map_database_name)));
			            			    
			            			    
			            				//this will be used to write the downloaded data into the file we created
			            		        FileOutputStream fileOutput = new FileOutputStream(file);

			            		        //this will be used in reading the data from the internet
			            		        InputStream inputStream = urlConnection.getInputStream();
			            		        
			            		      //create a buffer...
			            		        byte[] buffer = new byte[2048];
			            		        int bufferLength = 0; //used to store a temporary size of the buffer

			            		        //now, read through the input buffer and write the contents to the file
			            		        while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
			            		                //add the data in the buffer to the file in the file output stream (the file on the sd card
			            		                fileOutput.write(buffer, 0, bufferLength);
			            		    
			            		        }
			            		        //close the output stream when done
			            		        fileOutput.close();
			            		        urlConnection.disconnect();
			            		        
			            		        //update internal version number after writing contents locally
					            		//SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
					            		SharedPreferences.Editor editor = sharedPref.edit();
					            		editor.putString(getResources().getString((R.string.offline_database_version)), DatabaseWebVersion);
					            		editor.commit();
			            				
			            				
			            				
			            			} catch (Exception e) {
			            				e.printStackTrace();
			            				
			            			}
			            			
			            		}
			            		
			            		
			            		
			            		
			           
			            		
			            		//Log.e(current_version,"yo");
			            		
			            	}
			            	
			            	
			            	
			            }
			            
			            
			            
			        }
			        //System.out.println(out.toString());   //Prints the string content read from input stream
			        reader.close();
					
					
					
					//parseXML(XML_ContentStream);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//add feedback if not connected to internet
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
	        	
				
				return null;
	        	
	    	
	        
	    }

	    // This is called each time you call publishProgress()
	    protected void onProgressUpdate(Integer... progress) {
	       // setProgressPercent(progress[0]);
	    }

	    // This is called when doInBackground() is finished
	    protected void onPostExecute(Long result) {
	       // showNotification("Downloaded " + result + " bytes");
	    	String done = "done";
	    	
	    }
	    
	}
	
	/*
	 * Does the version checking and reading and writing of external learning content
	 * 
	 * todo - make updates for different file storage locations and types
	 * 
	 */
	private void WriteLocalContent (String InputContent) {
		
		//first make sure  external storage is writable
		//http://developer.android.com/training/basics/data-storage/files.html
		//http://stackoverflow.com/questions/16333145/save-xml-from-url-and-read-it
		if (isExternalStorageWritable()) {
			
			try {
			
			 URL url = new URL(InputContent);
			 //create the new connection
		     HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		     urlConnection.connect();
		     
			//location on device
			
		    File SDCardRoot = Environment.getExternalStorageDirectory();
		    //create the directory 
		    File IwacuDir = new File(SDCardRoot,getResources().getString((R.string.Iwacu_Directory)));
		    if (!IwacuDir.exists()) {
		    	IwacuDir.mkdirs();
		    }
		    
		    
		    File file = new File(IwacuDir,getResources().getString((R.string.learning_file_name)));
		    
		    
			//this will be used to write the downloaded data into the file we created
	        FileOutputStream fileOutput = new FileOutputStream(file);

	        //this will be used in reading the data from the internet
	        InputStream inputStream = urlConnection.getInputStream();
	        
	      //create a buffer...
	        byte[] buffer = new byte[1024];
	        int bufferLength = 0; //used to store a temporary size of the buffer

	        //now, read through the input buffer and write the contents to the file
	        while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
	                //add the data in the buffer to the file in the file output stream (the file on the sd card
	                fileOutput.write(buffer, 0, bufferLength);
	    
	        }
	        //close the output stream when done
	        fileOutput.close();
	        urlConnection.disconnect();
	        
	        
	        //*** now that the XML file has been downloaded, go and download all the images referenced in the XML file
	        //todo - much of this code is redundant with previous code, consolidate into one function
	        
	        //first parse the newly downloaded XML
	        
	      //get the learning content from external storage, should be updated based on checks done when the app is opening
	      //create the directory 
		    File IwacuImgDir = new File(SDCardRoot,getResources().getString((R.string.Iwacu_Images_Directory)));
		    if (!IwacuImgDir.exists()) {
		    	IwacuImgDir.mkdirs();
		    }
		    
        	
        	//parse the XML file to get the image references
        	 RecorContentPullParserHandler parser = new RecorContentPullParserHandler();
        	 List<RecorContent> recor_doc = null;
        	 InputStream in = null;
        	 File Learning_Content_File = new File(IwacuDir,getResources().getString((R.string.learning_file_name)));
        	 in = new BufferedInputStream(new FileInputStream(Learning_Content_File));
        	 recor_doc = parser.parse(in);
	        
	        
	        //assumes images are a directory off from where the XML learning file is located
        	//example - http://geoapps64.main.ad.rit.edu/rwanda/Iwacu/recor_v4.xml
        	//http://geoapps64.main.ad.rit.edu/rwanda/Iwacu/Img/
        	 
        	 Integer IMG_URL_index = InputContent.lastIndexOf("/");
        	 String IMG_URL_base = InputContent.substring(0, IMG_URL_index + 1 ) + getResources().getString(R.string.Iwacu_Images_Web_Directory);
        	 
        	 
        	 //*** iterate through the XML images references and download them 
        	
        	 
        	 for (int i = 0; i < recor_doc.size();i++) { // each image reference...
        	 
	        	 //get the image reference from the XML
        		 RecorContent temp =  recor_doc.get(i);
	        	 String currentIMG = temp.getImageUrl();
	        	 
	        	 //continue if images is not referenced
	        	 if (currentIMG.length() < 5) {
	        		 continue;
	        	 }
	        	
	        	//get a local file ready
	         	File ImgFile = new File(IwacuImgDir,currentIMG);
	     
	        	 
	        	 //get the images URL ready
	        	 URL urlImg = new URL(IMG_URL_base + currentIMG);
	        	 
				 //create the new connection
			     HttpURLConnection urlImgConnection = (HttpURLConnection) urlImg.openConnection();
			     urlImgConnection.connect();
			     
	        	 
	 	        FileOutputStream fileImgOutput = new FileOutputStream(ImgFile);
	
	 	        //this will be used in reading the data from the internet
	 	        InputStream inputImgStream = urlImgConnection.getInputStream();
	 	        
	 	      //create a buffer...
	 	        byte[] Imgbuffer = new byte[2048];
	 	        int bufferImgLength; //used to store a temporary size of the buffer
	
	 	        //now, read through the input buffer and write the contents to the file
	 	        while ( (bufferImgLength = inputImgStream.read(Imgbuffer)) != -1 ) {
	 	                //add the data in the buffer to the file in the file output stream (the file on the sd card
	 	        	fileImgOutput.write(Imgbuffer, 0, bufferImgLength);
	 	    
	 	        }
	 	        //close the output stream when done
	 	        fileImgOutput.close();
	 	        urlImgConnection.disconnect();
	        	 
	 	      
	        	
        	 } //end for each image reference...
			
			
			} catch (MalformedURLException e) {
		        e.printStackTrace();
		} catch (IOException e) {
		        e.printStackTrace();
		}
			
			//Creating an internal dir;
		    //System.out.println("dir1: " + dir1);
			
			
			
			
			
			
		} else {
			
		}
		
		
		
		
		
	}
	
	
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	

}
