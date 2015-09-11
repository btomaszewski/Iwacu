package gis.iwacu_new.rit.edu.main;


import gis.iwacu_new.rit.edu.main.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
//import android.support.v4.app.NavUtils;

public class AfterSplashActivity extends Activity {

	private static String TAG = SplashActivity.class.getName();
	private static long SLEEP_TIME = 5;	// Sleep for some time

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
      	this.requestWindowFeature(Window.FEATURE_NO_TITLE);	// Removes title bar
      	this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);	// Removes notification bar
        
      	setContentView(R.layout.main_splash);
        
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
          	Intent intent = new Intent(AfterSplashActivity.this, LogoSplash.class);
          	AfterSplashActivity.this.startActivity(intent);
          	AfterSplashActivity.this.finish();
    	}
    }
	
}
