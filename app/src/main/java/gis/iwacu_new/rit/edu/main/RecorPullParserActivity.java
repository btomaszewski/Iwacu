//*** IS THIS CLASS STILL NEEDED? IF NOT, REMOVE *** 

package gis.iwacu_new.rit.edu.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RecorPullParserActivity extends Activity implements OnItemClickListener {
    private ListView listView;
    private FileManager fileManager;
    private RecorDocument recorDocument = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parser_main);

        listView = (ListView) findViewById(R.id.list);


        try {
            fileManager = new FileManager(this);

            //get the learning content from external storage, should be updated based on checks done when the app is opening
            File learningContentFile = fileManager.getFile(R.string.learning_file_name);

            //http://developer.android.com/reference/java/io/FileInputStream.html
            FileInputStream in = new FileInputStream(learningContentFile);
            recorDocument = RecorDocument.parse(in);
            in.close();

            //https://github.com/thecodepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
            //http://developer.android.com/guide/topics/ui/declaring-layout.html
            //http://developer.android.com/training/implementing-navigation/lateral.html

            //still needed?
            ArrayAdapter<RecorContent> adapter = new ArrayAdapter<RecorContent>(this, R.layout.list_item, recorDocument.getContent());
            listView.setAdapter(adapter);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        switch (position) {
            case 0:
                Intent RecorPullParserActivity = new Intent(this, RecorPullParserActivity.class);
                startActivity(RecorPullParserActivity);
                break;
            case 1:
                Intent GeometrySampleActivity = new Intent(this, GeometrySampleActivity.class);
                startActivity(GeometrySampleActivity);
                break;
            case 2:
                Intent GPS = new Intent(this, RecorPullParserActivity.class);
                startActivity(GPS);
                break;
        }


    }
}
