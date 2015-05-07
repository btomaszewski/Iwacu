package gis.iwacu_new.rit.edu.main;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by bindernews on 4/27/2015.
 */
public class FileManager {

    private Context context;
    private File rootDirectory;

    public FileManager(Context ctx) {
        context = ctx;
        rootDirectory = new File(Environment.getExternalStorageDirectory(), "Iwacu");
    }

    public File ensureDirectory(String path) {
        File dir = new File(rootDirectory, path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public File getFile(String path, String name) {
        File dir = ensureDirectory(path);
        return new File(dir, name);
    }

    public File getFile(String pathAndName) {
        File file = new File(rootDirectory, pathAndName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }

    public File getFile(int id) {
        return getFile(context.getResources().getString(id));
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
