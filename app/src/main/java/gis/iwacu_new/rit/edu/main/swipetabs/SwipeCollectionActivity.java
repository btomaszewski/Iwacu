/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gis.iwacu_new.rit.edu.main.swipetabs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import gis.iwacu_new.rit.edu.main.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import gis.iwacu_new.rit.edu.main.GPS;
import gis.iwacu_new.rit.edu.main.GeometrySampleActivity;
import gis.iwacu_new.rit.edu.main.RecorContent;
import gis.iwacu_new.rit.edu.main.MainScreen;
import gis.iwacu_new.rit.edu.main.RecorDocument;
import gis.iwacu_new.rit.edu.youtube.PlayerViewDemoActivity;

public class SwipeCollectionActivity extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments representing
     * each object in a collection. We use a {@link android.support.v4.app.FragmentStatePagerAdapter}
     * derivative, which will destroy and re-create fragments as needed, saving and restoring their
     * state in the process. This is important to conserve memory and is a best practice when
     * allowing navigation between objects in a potentially large collection.
     */
    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    List<RecorContent> current_learning_content = null;
    
    public final static String LEARNING_CONTENT = "gis.iwacu.rit.edu.main.swipetabs.LEARNING_CONTENT";
    
    public final static String HEADING = "HEADING";
    public final static String ABOUT = "ABOUT";
    public final static String ACTIVITY = "ACTIVITY";
    public final static String IMAGE = "IMAGE";
    public final static String ACTIVITY_TOOL = "ACTIVITY_TOOL";
    public final static String ACTIVITY_DATA = "ACTIVITY_DATA";
    public final static String VIDEO_TEXT = "VIDEO_TEXT";
    public final static String VIDEO_ID = "VIDEO_ID";
    public final static String QUIZ_URL = "QUIZ_URL";

    static RecorDocument recor_doc = null;

    /**
     * The {@link android.support.v4.view.ViewPager} that will display the object collection.
     */
    ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_demo);
        
        //parse the learning content needed for various tasks and settings
        try {
            //parse the XML learning content for display on the tabs
            //CHECK IF WE BE TOO MEMORY INTENSIVE - tried passing object
            //get the learning content from external storage, should be updated based on checks done when the app is opening
            File SDCardRoot = Environment.getExternalStorageDirectory();
            File IwacuDir = new File(SDCardRoot,getResources().getString((R.string.Iwacu_Directory)));
            File Learning_Content_File = new File(IwacuDir,getResources().getString((R.string.learning_file_name)));

            //http://developer.android.com/reference/java/io/FileInputStream.html
            InputStream in = new FileInputStream(Learning_Content_File);
            recor_doc = RecorDocument.parse(in);

            // Create an adapter that when requested, will return a fragment representing an object in
            // the collection.
            //
            // ViewPager and its adapters use support library fragments, so we must use
            // getSupportFragmentManager.
            mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getSupportFragmentManager());

            // Set up action bar.
            final ActionBar actionBar = getActionBar();

            // Specify that the Home button should show an "Up" caret, indicating that touching the
            // button will take the user one step up in the application's hierarchy.
            actionBar.setDisplayHomeAsUpEnabled(true);

            // Set up the ViewPager, attaching the adapter.
            mViewPager = (ViewPager) findViewById(R.id.pager);
            mViewPager.setAdapter(mDemoCollectionPagerAdapter);

        } catch (IOException e) {
            e.printStackTrace();
            TextView textView = new TextView(this);
            textView.setText(getResources().getText(R.string.error_loading_content));
            setContentView(textView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
                Intent upIntent = new Intent(this, SwipeTabsMainActivity.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is not part of the application's task, so create a new task
                    // with a synthesized back stack.
                    TaskStackBuilder.from(this)
                            // If there are ancestor activities, they should be added here.
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } else {
                    // This activity is part of the application's task, so simply
                    // navigate up to the hierarchical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void onBackPressed() {
        // perhaps not the most elegant way, but jump back to the opening menu screen if the back button is pressed
    	//ideally, eventually fix the problem of the screen with two button that come up
    	Intent myBackScreenIntent = new Intent(this, MainScreen.class);
    	startActivity(myBackScreenIntent);
        super.onBackPressed();
    }

    /**
     * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment
     * representing an object in the collection.
     */
    public static class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {

        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new DemoObjectFragment();
            Bundle args = new Bundle();
           
            //args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1); // Our object is just an integer :-P
            
            //use i value to get learning content baseed on the current index
            RecorContent temp =  recor_doc.get(i);
            
            //get the various pieces and pass them in as arguments in the bundle
            args.putString(HEADING, temp.getHeading());
            args.putString(ACTIVITY, temp.getActivity());
            args.putString(ACTIVITY_TOOL, temp.getActivityName());
            args.putString(ACTIVITY_DATA, temp.getActivityData());
            args.putString(ABOUT, temp.getAbout());
            args.putString(IMAGE, temp.getImageUrl());
            args.putString(VIDEO_ID, temp.getVideoId() );
            args.putString(VIDEO_TEXT, temp.getVideoText());
            args.putString(QUIZ_URL, temp.getQuizURL() );

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
        	return recor_doc.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
        	RecorContent temp =  recor_doc.get(position);
        	return temp.getHeading();
        }
    }

    /**
     * 
     * where specific learning content is displayed
     * remove static class designation
     */
    @SuppressLint("ValidFragment")
	public static  class DemoObjectFragment extends Fragment  {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.learning_content_fragment, container, false);
            Bundle args = getArguments();
           
        	
        	((TextView) rootView.findViewById(android.R.id.text1)).setText(
        	    		args.getString(HEADING)); 
            
        	((TextView) rootView.findViewById(android.R.id.content)).setText(
    	    		args.getString(ABOUT)); 
        	
            
            //set the image
        	//String file_name = args.getString(IMAGE);
        	
        	String file_name = (args.getString(IMAGE) == null) ? "" : args.getString(IMAGE) ;
        	
        	ImageView qImageView = (ImageView) rootView.findViewById(R.id.imageView1);
        	
        	
        	
        	if (!file_name.equalsIgnoreCase("")) {
        	
        		//make sure it appears
        		qImageView.setVisibility(View.VISIBLE); 
	        	//get the actual image name
	        	//http://www.e-nature.ch/tech/saving-loading-bitmaps-to-the-android-device-storage-internal-external/
	        	
	        	
	        	//go get the bitmap
	        	String bitmap_path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/" + getResources().getString((R.string.Iwacu_Images_Directory)) ;
	        	Bitmap current_bitmap = BitmapFactory.decodeFile(bitmap_path  +  file_name);
	        	qImageView.setImageBitmap(current_bitmap);
        	
        	} else {
        		//hide if no image
        		qImageView.setVisibility(View.INVISIBLE); 
        	}
        	
        	
        	
        	//activity button
        	 
        	String activty_name = (args.getString(ACTIVITY) == null) ? "" : args.getString(ACTIVITY) ;
        	
        	Button ActivityButton = (Button) rootView.findViewById(R.id.activityButton);
        	
        	if (!activty_name.equalsIgnoreCase("")) {
        	
        		ActivityButton.setVisibility(View.VISIBLE); 
        	
	        	ActivityButton.setText(activty_name);
	        	
	        	
	        	//determine which tool to call when clicked
	        	
	        	//keep coordinated with <string-array name="sample_names">
	        	//String [] GIS_tools = getResources().getStringArray(R.array.sample_names);
	        	final String activty_tool_name = args.getString(ACTIVITY_TOOL);
	        	final String activty_tool_data = args.getString(ACTIVITY_DATA);
	        	
	        	
	        	ActivityButton.setOnClickListener(new View.OnClickListener() {
	     	        public void onClick(View view) {
                        //set up the incoming arguments
                        Bundle tool_args = new Bundle();
                        tool_args.putString(getResources().getString((R.string.tool_argument_tool_name)),activty_tool_name);
                        tool_args.putString(getResources().getString((R.string.tool_argument_tool_data)),activty_tool_data);

                        //determine which activity to launch
                        Intent myIntent;
                        if (activty_tool_name.equalsIgnoreCase(getResources().getString((R.string.gps_activity)))) {
                            myIntent = new Intent(view.getContext(), GPS.class);
                        } else {
                            myIntent = new Intent(view.getContext(),GeometrySampleActivity.class); //entry point to the tools, arguments passed will determine which one to open
                        }
                        myIntent.putExtra(getResources().getString((R.string.tool_argument_key_name)), tool_args);
                        startActivity(myIntent);
	     	        }
	
	        	});
	        	
        	} else {
        		//hide the activity button
        		ActivityButton.setVisibility(View.INVISIBLE);
        		
        	}
	        	
        	//** couldn't get this to work - wanted to launch a video right on the tab, so just try clicking a button to launch a video 
           // YouTubePlayerFragment youTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
    	   // youTubePlayerFragment.initialize(DeveloperKey.DEVELOPER_KEY, (OnInitializedListener) this);
          
        	final String video_id = args.getString(VIDEO_ID);
        	
        	
        	String video_text = (args.getString(VIDEO_TEXT) == null) ? "" : args.getString(VIDEO_TEXT) ;
        	
        	Button VideoButton = (Button) rootView.findViewById(R.id.VideoButton);
        	
        	if (!video_text.equalsIgnoreCase("")) {
        	
        		VideoButton.setVisibility(View.VISIBLE);
        	
	        	VideoButton.setText(video_text);
	        	
	        	VideoButton.setOnClickListener(new View.OnClickListener() {
	     	        public void onClick(View view) {
	     	        	
		 	        	//set up the incoming arguments
			        	Bundle video_args = new Bundle();
			        	video_args.putString(getResources().getString((R.string.video_id_argument_name)),video_id);
			        	
		    	        	 
		 	        	Intent myVideoIntent = new Intent(view.getContext(), PlayerViewDemoActivity.class); 
		 	        	myVideoIntent.putExtra(getResources().getString((R.string.video_argument_key_name)), video_args);
		 	        	
		 	        	try {
		 	        	
		 	        		startActivity(myVideoIntent);
		 	        	
		 	        	} catch (Exception e) {
		 	        		e.printStackTrace();
		 	        	}
	     	            
	     	        }
	
	        	}); //VideoButton.setOnClickListener(new View.OnClickListener() {
	        	
        	} else {
        		VideoButton.setVisibility(View.INVISIBLE);
        	}
        	
        	
        	//quiz button
        	
        	final String quiz_text = (args.getString(QUIZ_URL) == null) ? "" : args.getString(QUIZ_URL) ;
        	
        	Button QuizButton = (Button) rootView.findViewById(R.id.QuizButton);
        	
        	if (!quiz_text.equalsIgnoreCase("")) {
        		
        		QuizButton.setVisibility(View.VISIBLE);
        		
        		QuizButton.setOnClickListener(new View.OnClickListener() {
	     	        public void onClick(View view) {
	     	        	
	     	        	try {
	     	        		
		     	        	Uri uri = Uri.parse(quiz_text);
		     	        	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		     	        	startActivity(intent);
		     	        	
		 	        	
		 	        	
		 	        	} catch (Exception e) {
		 	        		e.printStackTrace();
		 	        	}
	     	            
	     	        }
	
	        	}); 
        		
        		
        	} else {
        		QuizButton.setVisibility(View.INVISIBLE);
        	}
        	
        	
        	
        	
        	/* not sure if this was needed, but perhaps if specific things need to happen for specific tools
        	
        	for (int x = 0; x < GIS_tools.length; x++) {
        		
        		
        		
        		if (GIS_tools[x].equalsIgnoreCase(activty_tool_name)) {
        			
        			switch (x) {
        			case 0: //projection
        				
        				//set the buttons click listener
        	        	ActivityButton.setOnClickListener(new View.OnClickListener() {
        	     	        public void onClick(View view) {
        	     	           Intent myIntent = new Intent(view.getContext(),GeometrySampleActivity.class);
        	     	        	startActivity(myIntent);
        	     	            
        	     	        }

        	        	});
        				
        				
        			case 1: //Buffer
        				
        				
        				
        			case 2: //Union and Difference
        			case 3: //Spatial Relationships
        			case 4: //Measure
        				
        			default:
        				break;
        				
        			}
        			
        			
        			
        		} //if (GIS_tools[x].equalsIgnoreCase(activty_tool_name)) {
        		
        	} //for (int x = 0; x < GIS_tools.length; x++) {
        	
        	
        	*/
        	        
            return rootView;
        }
    }
}
