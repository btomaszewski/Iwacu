package gis.iwacu_new.rit.edu.main;

import gis.iwacu_new.rit.edu.main.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;


import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;


public class GISToolsChoice extends Activity {
	
	Point p;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gischoice);
		
		
		
		Button btnspat = (Button) findViewById(R.id.btnspat);
		btnspat.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View view) {
	            Intent myIntent = new Intent(view.getContext(), GeometrySampleActivity.class);
	            startActivityForResult(myIntent, 0);
	        }

	    });
		
		Button btngps = (Button) findViewById(R.id.btngps);
		btngps.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View view) {
	            Intent myIntent = new Intent(view.getContext(), GPS.class);
	            startActivityForResult(myIntent, 0);
	        }

	    });
		Button btnskip = (Button) findViewById(R.id.btnskip);
		btnskip.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View view) {
	            Intent myIntent = new Intent(view.getContext(), XMLorPDF.class);
	            startActivityForResult(myIntent, 0);
	        }

	    });
		
		Button btnsync = (Button) findViewById(R.id.btnsync);
		btnsync.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View arg0) {
	        	
	        	 if (p != null)
	        	       showPopup(GISToolsChoice.this, p);
	            
	        }
	       
	    });
		
		
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
	 
	   int[] location = new int[2];
	   Button button = (Button) findViewById(R.id.btnsync);
	 
	   // Get the x, y location and store it in the location[] array
	   // location[0] = x, location[1] = y.
	   button.getLocationOnScreen(location);
	 
	   //Initialize the Point with x, and y positions
	   p = new Point();
	   p.x = location[0];
	   p.y = location[1];
	}
	

	// The method that displays the popup.
	private void showPopup(final Activity context, Point p) {
	   int popupWidth = 400;
	   int popupHeight = 300;
	 
	   // Inflate the popup_layout.xml
	   LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
	   LayoutInflater layoutInflater = (LayoutInflater) context
	     .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	   View layout = layoutInflater.inflate(R.layout.popup_window, viewGroup);
	 
	   // Creating the PopupWindow
	   final PopupWindow popup = new PopupWindow(context);
	   popup.setContentView(layout);
	   popup.setWidth(popupWidth);
	   popup.setHeight(popupHeight);
	   popup.setFocusable(true);
	 
	   // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
	   int OFFSET_X = 30;
	   int OFFSET_Y = 30;
	 
	   // Clear the default translucent background
	   popup.setBackgroundDrawable(new BitmapDrawable());
	 
	   // Displaying the popup at the specified location, + offsets.
	   popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);
	 
	   // Getting a reference to Close button, and close the popup when clicked.
	   Button close = (Button) layout.findViewById(R.id.close);
	   close.setOnClickListener(new OnClickListener() {
	 
	     //@Override
	     public void onClick(View v) {
	       popup.dismiss();
	     }
	   });
	}

		
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}