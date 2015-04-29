package gis.iwacu_new.rit.edu.main;


import gis.iwacu_new.rit.edu.main.swipetabs.SwipeTabsMainActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

/**
 * This is the main activity screen.
 * There really isn't much to say about it. It pretty much is just the main menu.
 */
public class MainScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        Button xmlButton = (Button) findViewById(R.id.xmlbtn);
        xmlButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), SwipeTabsMainActivity.class);
                startActivity(myIntent);
            }

        });

        Button skipButton = (Button) findViewById(R.id.skipbtn);
        skipButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), GISToolsChoice.class);
                startActivity(myIntent);
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