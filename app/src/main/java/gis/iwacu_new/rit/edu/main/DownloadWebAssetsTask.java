package gis.iwacu_new.rit.edu.main;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Downloads assets from the web.
 */
public class DownloadWebAssetsTask extends AsyncTask<URL, Void, Void> {

    private static final String TAG = DownloadWebAssetsTask.class.getName();
    private static final int BUFFER_SIZE = 1024;

    private Activity context;

    /**
     * File which holds the current information about versions of web assets.
     */
    private File webAssetsFile;
    private FileManager fileManager;

    /**
     * Constructor.
     * @param ctx - a reference to the activity so we can access the Android system
     */
    public DownloadWebAssetsTask(Activity ctx) {
        context = ctx;
        fileManager = new FileManager(context);
        webAssetsFile = fileManager.getFile("web_assets.json");
    }

    protected Void doInBackground(URL... urls) {
        try {
            String webJsonString = readStream(urls[0].openStream());
            JSONObject webData = (JSONObject) new JSONTokener(webJsonString).nextValue();
            JSONObject localData = loadCurrentWebAssetData();

            // now we handle the different things and download them if necessary
            JSONObject learningContent = webData.getJSONObject("learning_content");
            if (hasNewVersion(learningContent, localData.getJSONObject("learning_content"))) {
                downloadLearningContent(learningContent.getString("url"));

                // We save ONLY the things we know are updated just in case someone publishes
                // a new version of the JSON file and the app isn't updated we don't want to
                // just copy over all of webData.
                localData.put("learning_content", learningContent);
            }

            JSONObject offlineDatabase = webData.getJSONObject("offline_database");
            if (hasNewVersion(offlineDatabase, localData.getJSONObject("offline_database"))) {
                //location on device
                File file = fileManager.getFile(R.string.offline_map_database_name);
                downloadFile(new URL(offlineDatabase.getString("url")), file);
                localData.put("offline_database", offlineDatabase);
            }
            // now we save the updated localData back to web_assets.json
            saveCurrentWebAssetData(localData);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    /**
     * Read an input stream and convert it into a String object. readStream will call close() on
     * the provided input stream.
     *
     * @param input - input stream to read
     * @return A string containing the content of the input stream
     * @throws IOException - Occurs when things go wrong.
     */
    private static String readStream(InputStream input) throws IOException {
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(input, "UTF-8");
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[BUFFER_SIZE];
            int length;
            while((length = isr.read(buffer)) > 0) {
                sb.append(buffer, 0, length);
            }
            return sb.toString();
        } finally {
            if(isr != null) {
                isr.close();
            }
        }
    }

    /**
     * Reads the current version of web_assets.json
     * @return The current web asset data
     */
    private JSONObject loadCurrentWebAssetData() throws IOException, JSONException {
        // Make sure the web_assets.json file exists before we try to read it and if it doesn't
        // then we write out the default built-in copy of it.
        if (!webAssetsFile.exists()) {
            writeDefaultConfig();
        }
        FileInputStream input = new FileInputStream(webAssetsFile);
        return (JSONObject) new JSONTokener(readStream(input)).nextValue();
    }

    private void saveCurrentWebAssetData(JSONObject obj) throws IOException {
        FileWriter output = new FileWriter(webAssetsFile);
        output.write(obj.toString());
        output.close();
    }

    /**
     * Save the default JSON config.
     */
    private void writeDefaultConfig() throws IOException {
        InputStream input = context.getResources().openRawResource(R.raw.web_assets);
        FileOutputStream output = new FileOutputStream(webAssetsFile);
        streamCopy(input, output);
        input.close();
        output.close();
    }

    /**
     * Copy data from the input to the output streams. Note that the caller is responsible for
     * calling close() on the provided streams.
     *
     * @param input - Stream to read from
     * @param output - Stream to write to
     * @throws IOException - in case an IO problem happens
     */
    private static void streamCopy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int length;
        while((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
    }

    private boolean hasNewVersion(JSONObject next, JSONObject current) throws JSONException {
        return !next.getString("version").equals(current.getString("version"));
    }

    /**
     * Downloads the file at the given url and saves it to the output file.
     * @param url - the url to download from
     * @param file - the file to which we will save the content
     */
    private static void downloadFile(URL url, File file) throws IOException {
        File directory = file.getParentFile();
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }

        //this will be used to write the downloaded data into the file we created
        FileOutputStream fileOutput = null;
        //this will be used in reading the data from the internet
        InputStream inputStream = null;
        try {
            fileOutput = new FileOutputStream(file);
            inputStream = url.openStream();
            // now simply do a stream copy
            streamCopy(inputStream, fileOutput);
        } finally {
            // ... and close the files
            if (fileOutput != null) {
                fileOutput.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /*
     * Download the learning content file and required files for said learning content.
     */
    private void downloadLearningContent(String contentUrl) throws IOException {
        //http://developer.android.com/training/basics/data-storage/files.html
        //http://stackoverflow.com/questions/16333145/save-xml-from-url-and-read-it
        URL url = new URL(contentUrl);
        //location on device
        File learningContentFile = fileManager.getFile(R.string.learning_file_name);
        downloadFile(url, learningContentFile);

        // parse the XML file to get the image references
        FileInputStream in = new FileInputStream(learningContentFile);
        RecorDocument recorDocument = RecorDocument.parse(in);
        in.close();

        // get the learning content from external storage, should be updated based on checks done
        // when the app is opening create the directory.
        File iwacuImgDir = fileManager.ensureDirectory("Images");

        // iterate through the XML images references and download them
        for (int i = 0; i < recorDocument.size(); i++) { // each image reference...
            //get the image reference from the XML
            RecorContent content = recorDocument.get(i);
            String imageName = content.getImageUrl();
            //get a local file ready
            File imageFile = new File(iwacuImgDir, imageName);
            //get the images URL ready
            URL imageUrl = new URL(recorDocument.getBaseImageURL() + imageName);
            downloadFile(imageUrl, imageFile);
        } //end for each image reference...
    }
}
