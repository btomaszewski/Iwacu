package gis.iwacu_new.rit.edu.main.gps.map;

import android.graphics.Bitmap;


public abstract class Handler {

	public void handle(Object object) {
	};

	public void handle(RawTile tile, byte[] data) {
	}

	public void handle(RawTile tile, Bitmap bmp4scale, boolean isScaled) {
	};

}
