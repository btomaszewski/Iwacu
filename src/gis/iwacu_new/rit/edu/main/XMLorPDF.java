package gis.iwacu_new.rit.edu.main;



import gis.iwacu_new.rit.edu.main.R;
import gis.iwacu_new.rit.edu.main.swipetabs.SwipeTabsMainActivity;
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

public class XMLorPDF extends Activity {

	Point p;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xml_or_pdf);
		
		Button xmlbtn = (Button) findViewById(R.id.xmlbtn);
	    xmlbtn.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View view) {
	           // Intent myIntent = new Intent(view.getContext(), RecorPullParserActivity.class);
	        	//startActivityForResult(myIntent, 0);
	        	
	        	Intent myIntent = new Intent(view.getContext(), SwipeTabsMainActivity.class);
	        	startActivity(myIntent);
	            
	        }

	    });
	    
	    /* Do not need a PDF button
	    
	    Button pdfbtn = (Button) findViewById(R.id.pdfbtn);
	    pdfbtn.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View arg0) {
	        	 if (p != null)
	        	       showPopup(XMLorPDF.this, p);

	        }

	    });
	    */
	    
	    Button skipbtn = (Button) findViewById(R.id.skipbtn);
	    skipbtn.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View view) {
	            Intent myIntent = new Intent(view.getContext(), GISToolsChoice.class);
	            startActivityForResult(myIntent, 0);
	        }

	    });

	}
	
	@Override
	
	
	
	public void onWindowFocusChanged(boolean hasFocus) {
	 
		
		/* really needed for the PDF button?
		
	   int[] location = new int[2];
	   Button button1 = (Button) findViewById(R.id.pdfbtn);
	 
	   // Get the x, y location and store it in the location[] array
	   // location[0] = x, location[1] = y.
	   button1.getLocationOnScreen(location);
	 
	   //Initialize the Point with x, and y positions
	   p = new Point();
	   p.x = location[0];
	   p.y = location[1];
	   
	   */
	   
	}
	
	

	// The method that displays the popup.
	private void showPopup(final Activity context, Point p) {
	   int popupWidth = 400;
	   int popupHeight = 300;
	 
	   // Inflate the popup_layout.xml
	   LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
	   LayoutInflater layoutInflater = (LayoutInflater) context
	     .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	   View layout = layoutInflater.inflate(R.layout.popup_pdf, viewGroup);
	 
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