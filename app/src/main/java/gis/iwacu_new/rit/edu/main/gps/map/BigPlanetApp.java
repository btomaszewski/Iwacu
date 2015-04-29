package gis.iwacu_new.rit.edu.main.gps.map;

import android.app.Application;

public class BigPlanetApp extends Application {

	public static boolean isDemo = false;
	
	public BigPlanetApp() {
		super();
	}

	@Override
	public void onCreate() {
		Preferences.init(this);
	}

}
