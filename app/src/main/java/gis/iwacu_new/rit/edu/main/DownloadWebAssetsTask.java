package gis.iwacu_new.rit.edu.main;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Environment;

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

/**
 * Downloads assets from the web.
 *
 * TODO Refactor this class such that it is not coupled with Activity if possible.
 * TODO Document this class.
 * TODO Use a standard data format such as XML or JSON instead of whatever this is.
 */
public class DownloadWebAssetsTask extends AsyncTask<URL, Void, Void> {

    public static final String SPLIT_CHAR_1 = "~";
    public static final String SPLIT_CHAR_2 = "_";
    public static final String LEARNING_CONTENT = "learning_content";

    // TODO change this to a Context
    private Activity context;

    /**
     * Constructor.
     * @param ctx - a reference to the activity so we can access the Android system
     */
    public DownloadWebAssetsTask(Activity ctx) {
        context = ctx;
    }

    // Do the long-running work in here
    protected Void doInBackground(URL... urls) {
        URLConnection connection = null;
        try {
            connection = urls[0].openConnection();

            InputStream WebAsset_ContentStream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(WebAsset_ContentStream));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
                //inspect each line of the input file and take action accordingly
                String[] temp_split = line.split(SPLIT_CHAR_1);

                if (temp_split[0].equalsIgnoreCase(LEARNING_CONTENT)) {
                    //this is XML content, check version and download and store locally if need be

                    //http://developer.android.com/training/basics/data-storage/shared-preferences.html
                    SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);
                    String learning_version = getResources().getString((R.string.learning_version));
                    String learningVersionDefault = getResources().getString((R.string.learning_version_default));
                    String current_version = sharedPref.getString(learning_version, learningVersionDefault);

                    //check the current version stored on the device against the web version
                    String XMLVersion[] = temp_split[1].split(SPLIT_CHAR_2); //will come back with [1] being like v1.xml
                    String WebVersion = XMLVersion[1].substring(1, 2);


                    if (!WebVersion.equalsIgnoreCase(current_version)) {
                        //new version was found, download, store locally and update current version

                        writeLocalContent(temp_split[1]);

                        //update internal version number after writing contents locally
                        //SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getResources().getString((R.string.learning_version)), WebVersion);
                        editor.commit();
                    }
                } else if (temp_split[0].equalsIgnoreCase(getResources().getString((R.string.offline_database)))) {
                    // TODO Why are you using the file name for versioning? Why? Are you TRYING to kill me?
                    SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);
                    String offline_database_version = getResources().getString((R.string.offline_database_version));
                    String offline_database_version_default = getResources().getString((R.string.offline_database_version_default));
                    String current_version = sharedPref.getString(offline_database_version, offline_database_version_default);


                    //check the current version stored on the device against the web version
                    String DatabaseVersion[] = temp_split[1].split(SPLIT_CHAR_2); //will come back with [1] being like v1.xml
                    String DatabaseWebVersion = DatabaseVersion[1].substring(1, 2);

                    if (!DatabaseWebVersion.equalsIgnoreCase(current_version)) {
                        //new version was found, download, store locally and update current version

                        //more redundant code..
                        if (isExternalStorageWritable()) {
                            try {
                                URL url = new URL(temp_split[1]);

                                //location on device
                                File SDCardRoot = Environment.getExternalStorageDirectory();
                                File DatabaseDir = new File(SDCardRoot, getResources().getString((R.string.Iwacu_Directory)) + "offline_database");
                                File file = new File(DatabaseDir, getResources().getString((R.string.offline_map_database_name)));

                                downloadFile(url, file);

                                //update internal version number after writing contents locally
                                //SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString(getResources().getString((R.string.offline_database_version)), DatabaseWebVersion);
                                editor.commit();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Downloads the file at the given url and saves it to the output file.
     * @param url - the url to download from
     * @param file - the file to which we will save the content
     */
    private void downloadFile(URL url, File file) throws IOException {
        final int BUFFER_SIZE = 1024 * 4;

        File directory = file.getParentFile();
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }

        //this will be used to write the downloaded data into the file we created
        FileOutputStream fileOutput = new FileOutputStream(file);

        //this will be used in reading the data from the internet
        InputStream inputStream = url.openStream();

        //create a buffer...
        byte[] buffer = new byte[BUFFER_SIZE];
        int bufferLength = 0; //used to store a temporary size of the buffer

        try {
            //now, read through the input buffer and write the contents to the file
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                //add the data in the buffer to the file in the file output stream (the file on the sd card
                fileOutput.write(buffer, 0, bufferLength);
            }
        } finally {
            //close the output stream when done
            fileOutput.close();
            inputStream.close();
        }
    }

    private Resources getResources() {
        return context.getResources();
    }

    /*
     * Does the version checking and reading and writing of external learning content
     *
     * todo - make updates for different file storage locations and types
     *
     */
    private void writeLocalContent(String InputContent) {

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
                File IwacuDir = new File(SDCardRoot, getResources().getString((R.string.Iwacu_Directory)));
                if (!IwacuDir.exists()) {
                    IwacuDir.mkdirs();
                }


                File file = new File(IwacuDir, getResources().getString((R.string.learning_file_name)));


                //this will be used to write the downloaded data into the file we created
                FileOutputStream fileOutput = new FileOutputStream(file);

                //this will be used in reading the data from the internet
                InputStream inputStream = urlConnection.getInputStream();

                //create a buffer...
                byte[] buffer = new byte[1024];
                int bufferLength = 0; //used to store a temporary size of the buffer

                //now, read through the input buffer and write the contents to the file
                while ((bufferLength = inputStream.read(buffer)) > 0) {
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
                File IwacuImgDir = new File(SDCardRoot, getResources().getString((R.string.Iwacu_Images_Directory)));
                if (!IwacuImgDir.exists()) {
                    IwacuImgDir.mkdirs();
                }


                //parse the XML file to get the image references
                RecorContentPullParserHandler parser = new RecorContentPullParserHandler();
                List<RecorContent> recor_doc = null;
                InputStream in = null;
                File Learning_Content_File = new File(IwacuDir, getResources().getString((R.string.learning_file_name)));
                in = new BufferedInputStream(new FileInputStream(Learning_Content_File));
                recor_doc = parser.parse(in);


                //assumes images are a directory off from where the XML learning file is located
                //example - http://geoapps64.main.ad.rit.edu/rwanda/Iwacu/recor_v4.xml
                //http://geoapps64.main.ad.rit.edu/rwanda/Iwacu/Img/

                Integer IMG_URL_index = InputContent.lastIndexOf("/");
                String IMG_URL_base = InputContent.substring(0, IMG_URL_index + 1) + "img";


                //*** iterate through the XML images references and download them


                for (int i = 0; i < recor_doc.size(); i++) { // each image reference...

                    // get the image reference from the XML
                    RecorContent temp = recor_doc.get(i);
                    String currentIMG = temp.getImageUrl();

                    // continue if images is not referenced
                    if (currentIMG.length() < 5) {
                        continue;
                    }

                    // get a local file ready
                    File ImgFile = new File(IwacuImgDir, currentIMG);


                    // get the images URL ready
                    URL urlImg = new URL(IMG_URL_base + currentIMG);

                    // create the new connection
                    HttpURLConnection urlImgConnection = (HttpURLConnection) urlImg.openConnection();
                    urlImgConnection.connect();


                    FileOutputStream fileImgOutput = new FileOutputStream(ImgFile);

                    // this will be used in reading the data from the internet
                    InputStream inputImgStream = urlImgConnection.getInputStream();

                    // create a buffer...
                    byte[] Imgbuffer = new byte[2048];
                    int bufferImgLength; //used to store a temporary size of the buffer

                    // now, read through the input buffer and write the contents to the file
                    while ((bufferImgLength = inputImgStream.read(Imgbuffer)) != -1) {
                        // add the data in the buffer to the file in the file output stream
                        // (the file on the sd card
                        fileImgOutput.write(Imgbuffer, 0, bufferImgLength);
                    }
                    // close the output stream when done
                    fileImgOutput.close();
                    urlImgConnection.disconnect();


                } // end for each image reference...


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
