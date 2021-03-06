package gis.iwacu_new.rit.edu.main.gps.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class TileResolver {

	private TileLoader tileLoader;

	private PhysicMap physicMap;

	private BitmapCacheWrapper cacheProvider = BitmapCacheWrapper.getInstance();

	protected Handler scaledHandler;

	private Handler localLoaderHandler;

	private int strategyId = -1;

	private int loaded = 0;

	public TileResolver(final PhysicMap physicMap) {
		this.physicMap = physicMap;
		tileLoader = new TileLoader(
		
				new Handler() {
					@Override
					public void handle(RawTile tile, byte[] data) {
						LocalStorageWrapper.put(tile, data);
						Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
						cacheProvider.putToCache(tile, bmp);
						updateMap(tile, bmp);
					}
				});
		new Thread(tileLoader, "TileLoader").start();

		
		this.scaledHandler = new Handler() {

			@Override
			public synchronized void handle(RawTile tile, Bitmap bitmap,
					boolean isScaled) {
				loaded++;
				if (bitmap != null) {
					if (isScaled) {
						cacheProvider.putToScaledCache(tile, bitmap);
						updateMap(tile, bitmap);
					}
				}
			}

		};
		
		this.localLoaderHandler = new Handler() {

			@Override
			public void handle(RawTile tile, Bitmap bitmap, boolean isScaled) {
				if (tile.s == -1) {
					throw new IllegalStateException();
				}
				if (bitmap != null) { 
					loaded++;
					cacheProvider.putToCache(tile, bitmap);
					updateMap(tile, bitmap);
				} else {
					bitmap = cacheProvider.getScaledTile(tile);
					if (bitmap == null) {
						loaded++;
						updateMap(tile, MapControl.CELL_BACKGROUND);
//						TileScaler.get(tile, scaledHandler);
						//new Thread(new TileScaler(tile, scaledHandler)).start();
					} else {
						loaded++;
						updateMap(tile, bitmap);
					}
					load(tile);
				}
			}

		};

	}

	private void load(RawTile tile) {
		if (tile.s != -1) {
			tileLoader.load(tile);
		}
	}

	private void updateMap(RawTile tile, Bitmap bitmap) {
		if (tile.s == strategyId) {
			physicMap.update(bitmap, tile);
		}
	}

	
	public Bitmap loadTile(final RawTile tile){
		return cacheProvider.getTile(tile);
	}
	
	
	public void getTile(final RawTile tile) {
		if (tile.s == -1) {
//			System.out.println("6666666");
			return;
		}
		Bitmap bitmap = cacheProvider.getTile(tile);
		if (bitmap != null) {
			
			loaded++;
			updateMap(tile, bitmap);
		} else {
//			loaded++;
//			bitmap = LocalStorageWrapper.get(tile);
//			if (bitmap != null) {
//				updateMap(tile, bitmap);
//			} else {
//				updateMap(tile, MapControl.CELL_BACKGROUND);
//			}
			LocalStorageWrapper.get(tile, localLoaderHandler);
		}
	}

	public synchronized void setMapSource(int sourceId) {
		clearCache();
		MapStrategy mapStrategy = MapStrategyFactory.getStrategy(sourceId);
		this.strategyId = sourceId;
		tileLoader.setMapStrategy(mapStrategy);
	}

	public void clearCache() {
		cacheProvider.clear();
	}

	public int getMapSourceId() {
		return this.strategyId;
	}

	public void setUseNet(boolean useNet) {
		tileLoader.setUseNet(useNet);
		if (useNet) {
			physicMap.reloadTiles();
		}
	}
	
	public Bitmap[][] fillMap(RawTile tile, final int size){
		Bitmap[][] cells = new Bitmap[size][size];
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				int x, y;
				x = (tile.x + i);
				y = (tile.y + j);
				
				RawTile tmp = new RawTile(x,y,tile.z, tile.s);
				
				Bitmap bitmap;
				bitmap =  cacheProvider.getTile(tmp);
				if(bitmap==null){
					bitmap= LocalStorageWrapper.get(tmp);
					if(bitmap==null){
						bitmap = TileScaler.get(tmp);
						if(bitmap==null){
							
						}
					}
				}
				cells[i][j]=bitmap;
			}
		}
		return cells;
	}

	public synchronized void incLoaded(){
		this.loaded++;
//		System.out.println("inc " + this.loaded);
	}
	
	public synchronized int getLoaded(){
//		System.out.println("getLoaded " + this.loaded);
		return this.loaded;
	}
	
	public synchronized void resetLoaded(){
//		System.out.println("inc " + this.loaded);
		this.loaded = 0;
	}
}
