package gis.iwacu_new.rit.edu.main;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * This is the entry point for the app. This Activity displays a logo and downloads content before
 * moving on to the main menu Activity (currently named XMLorPDF).
 */
public class LogoSplash extends Activity {

    private static final String TAG = LogoSplash.class.getName();
    private static final long SLEEP_SECONDS = 5;    // number of seconds to display the logo

    private DownloadWebAssetsTask downloadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);    // Removes title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);    // Removes notification bar

        setContentView(R.layout.logo_splash);

        //start processing things needed when the app loads like new content offline databaseses etc.
        //open from a web URL, not as a local asset
        //URL is stored in an asset file to make flexible
        //later will want to make this an outside file
        //need to be aware of this issue though
        //http://developer.android.com/training/articles/perf-anr.html


        //http://stackoverflow.com/questions/16333145/save-xml-from-url-and-read-it

        // Start timer to ensure we display the splash screen long enough
        // do all work AFTER this timer is started
        Thread sleepThread = new Thread() {
            public void run() {
                try {
                    Thread.sleep(SLEEP_SECONDS * 1000);
                    downloadTask.get();
                    // It's perfectly fine to not download the content if they have no internet
                    // there is an issue though if they are downloading content for the first time
                    // in which case we need to fail gracefully.
                    // We should also if possible perform some sort of check to ensure all the
                    // requisite files are in place and not just assume the user hasn't touched
                    // them. Alternately we could use the App's private directory instead of
                    // the SD card for storage which would probably be more appropriate.
                } catch (InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                } catch (ExecutionException e) {
                    Log.e(TAG, e.getMessage(), e);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                // now we start the activity for the main screen and finish this splash screen activity
                Intent intent = new Intent(LogoSplash.this, MainScreen.class);
                LogoSplash.this.startActivity(intent);
                LogoSplash.this.finish();
            }
        };
        sleepThread.start();

        // check for updates and stuff while we wait
        // TODO actually move that code here

        try {
            URL url = new URL(getResources().getString(R.string.web_assets_url));
            downloadTask = new DownloadWebAssetsTask(this);
            downloadTask.execute(url);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
