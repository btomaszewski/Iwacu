package gis.iwacu_new.rit.edu.main;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;


import android.view.Menu;
import android.view.View;

import android.widget.Button;
import android.widget.Toast;

/**
 *
 */
public class GISToolsChoice extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gis_tools_choice);

        Button btnSync = (Button) findViewById(R.id.btnsync);
        btnSync.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Toast.makeText(GISToolsChoice.this, "Activity under construction.",
                        Toast.LENGTH_LONG).show();
            }
        });

		Button btnSpat = (Button) findViewById(R.id.btnspat);
		btnSpat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), GeometrySampleActivity.class);
                startActivityForResult(myIntent, 0);
            }

        });
		
		Button btnGps = (Button) findViewById(R.id.btngps);
		btnGps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), GPS.class);
                startActivityForResult(myIntent, 0);
            }

        });

        // return to the previous activity which is usually MainScreen
		Button btnBack = (Button) findViewById(R.id.btnskip);
		btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                GISToolsChoice.this.finish();
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