package gis.iwacu_new.rit.edu.main.gps.map;

import android.graphics.Bitmap;


public class BitmapCacheWrapper {

	
	public final static int CACHE_SIZE = (int) (50*BigPlanet.density);
	private BitmapCache cache = new BitmapCache(CACHE_SIZE);

	private BitmapCache scaledCache = new BitmapCache(CACHE_SIZE);

	private static BitmapCacheWrapper instance;
	
	public static BitmapCacheWrapper getInstance(){
		if(instance == null){
			instance = new BitmapCacheWrapper();
		}
		return instance;
	}
	
	private BitmapCacheWrapper(){
		
	}
	
	
	public Bitmap getScaledTile(RawTile tile) {
		return scaledCache.get(tile);
	}

	
	public void putToScaledCache(RawTile tile, Bitmap bitmap) {
		scaledCache.put(tile, bitmap);
	}

	
	public Bitmap getTile(RawTile tile) {
		return cache.get(tile);
	}

	
	public void putToCache(RawTile tile, Bitmap bitmap) {
		cache.put(tile, bitmap);
	}
	
	public void clear() {
		scaledCache.clear();
		cache.clear();
	}

}
