/*
 * MapApp : Simple offline map application, made by Hisham Ghosheh for tutorial purposes only
 * Tutorial on my blog
 * http://ghoshehsoft.wordpress.com/2012/03/09/building-a-map-app-for-android/
 * 
 * Class tutorial:
 * http://ghoshehsoft.wordpress.com/2012/04/06/mapapp5-mapview-and-activity/
 */

package gis.iwacu_new.rit.edu.main;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.KeyEvent;

//import com.mapapp.mapapp.R;
import gis.iwacu_new.rit.edu.main.gps1.MapView;
import gis.iwacu_new.rit.edu.main.gps1.MapViewLocationListener;
import gis.iwacu_new.rit.edu.main.gps1.PointD;
import gis.iwacu_new.rit.edu.main.gps1.TilesProvider;

public class GPS extends Activity
{
    // Constant strings used in onSaveInstanceState, onRestoreInstanceState
    public final static String GPS_LON = "gpsLon";
    public final static String GPS_LAT = "gpsLAT";
    public final static String GPS_ALT = "gpsALT";
    public final static String GPS_ACC = "gpsAcc";

    // Constant strings to save settings in SharedPreferences
    // Also used for restoring settings
    public final static String SEEK_LON = "seek_lon";
    public final static String SEEK_LAT = "seek_lat";
    public final static String ZOOM = "zoom";

	// Our only view, created in code
	private MapView mapView;

	// Provides us with Tiles objects, passed to MapView
	private TilesProvider tilesProvider;

	// Updates marker location in MapView
	private MapViewLocationListener locationListener;

	private Location savedGpsLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Creating the mapView and make sure it fills the screen
        Display display = getWindowManager().getDefaultDisplay();
        mapView = new MapView(this, display.getWidth(), display.getHeight(), tilesProvider, marker);
        setContentView(mapView);
    }

	@Override
	protected void onResume() {
		// Create MapView
		initViews();

		// Restore zoom and location data for the MapView
		restoreMapViewSettings();

		// Creating and registering the location listener
		locationListener = new MapViewLocationListener(mapView);
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

		// Never ever forget this :)
		super.onResume();
	}

	void initViews() {
		// Creating the bitmap of the marker from the resources
		Bitmap marker = BitmapFactory.decodeResource(getResources(), R.drawable.marker);

		// Creating our database tilesProvider to pass it to our MapView
		String path = Environment.getExternalStorageDirectory() + "/" + getResources().getString((R.string.Iwacu_Directory)) + getResources().getString((R.string.offline_map_database_name));
		tilesProvider = new TilesProvider(path);

		// If a location was saved while pausing the app then use it.
		if (savedGpsLocation != null) mapView.setGpsLocation(savedGpsLocation);

		// Update and draw the map view
		mapView.refresh();
	}

	@Override
	protected void onPause() {
		// Save settings before leaving
		saveMapViewSettings();

		// Mainly releases the MapView pointer inside the listener
		locationListener.stop();

		// Unregistering our listener
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.removeUpdates(locationListener);

		// Closes the source of the tiles (Database in our case)
		tilesProvider.close();
		// Clears the tiles held in the tilesProvider
		tilesProvider.clear();

		super.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Zooming
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_Z) {
			mapView.zoomIn();
			return true;
		}
		// Zooming
		else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_X) {
			mapView.zoomOut();
			return true;
		}
		// Enable auto follow
		if (keyCode == KeyEvent.KEYCODE_H || keyCode == KeyEvent.KEYCODE_FOCUS) {
			mapView.followMarker();
			return true;
		}
		// Simulate being at some location, for testing only
		else if (keyCode == KeyEvent.KEYCODE_M || keyCode == KeyEvent.KEYCODE_MENU) {
			mapView.setGpsLocation(46.142578, -20.841015, 0, 182);
			mapView.invalidate();

			return false;
		}

		return super.onKeyDown(keyCode, event);
	}

	// Called manually to restore settings from SharedPreferences
	void restoreMapViewSettings() {
		SharedPreferences pref = getSharedPreferences("View_Settings", MODE_PRIVATE);

		double lon, lat;
		int zoom;

		lon = Double.parseDouble(pref.getString(SEEK_LON, "0"));
		lat = Double.parseDouble(pref.getString(SEEK_LAT, "0"));
		zoom = pref.getInt(ZOOM, 0);

		mapView.setSeekLocation(lon, lat);
		mapView.setZoom(zoom);
		mapView.refresh();
	}

	// Called manually to save settings in SharedPreferences
	void saveMapViewSettings() {
		SharedPreferences.Editor editor = getSharedPreferences("View_Settings", MODE_PRIVATE).edit();

		PointD seekLocation = mapView.getSeekLocation();
		editor.putString(SEEK_LON, Double.toString(seekLocation.x));
		editor.putString(SEEK_LAT, Double.toString(seekLocation.y));
		editor.putInt(ZOOM, mapView.getZoom());

		editor.commit();
	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		if (mapView.getGpsLocation() != null) {
			savedInstanceState.putDouble(GPS_LON, mapView.getGpsLocation().getLongitude());
			savedInstanceState.putDouble(GPS_LAT, mapView.getGpsLocation().getLatitude());
			savedInstanceState.putDouble(GPS_ALT, mapView.getGpsLocation().getAltitude());
			savedInstanceState.putFloat(GPS_ACC, mapView.getGpsLocation().getAccuracy());
		}
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		double gpsLon, gpsLat, gpsAlt;
		float gpsAcc;

		gpsLon = savedInstanceState.getDouble(GPS_LON, Double.NaN);
		gpsLat = savedInstanceState.getDouble(GPS_LAT, Double.NaN);
		gpsAlt = savedInstanceState.getDouble(GPS_ALT, Double.NaN);
		gpsAcc = savedInstanceState.getFloat(GPS_ACC, Float.NaN);

		if (gpsLon != Double.NaN && gpsLat != Double.NaN && gpsAlt != Double.NaN && gpsAcc != Float.NaN) {
			savedGpsLocation = new Location(LocationManager.GPS_PROVIDER);
			savedGpsLocation.setLongitude(gpsLon);
			savedGpsLocation.setLatitude(gpsLat);
			savedGpsLocation.setAltitude(gpsAlt);
			savedGpsLocation.setAccuracy(gpsAcc);
		}

		super.onRestoreInstanceState(savedInstanceState);
	}
}