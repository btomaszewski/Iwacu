package gis.iwacu_new.rit.edu.main.gps.map;


import android.graphics.Bitmap;

public class MarkerImage {
    private Bitmap image;
    private int offsetX;
    private int offsetY;

    public MarkerImage(Bitmap bmp, int offsetX, int offsetY) {
        this.image = bmp;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public Bitmap getImage() {
        return image;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }
}
