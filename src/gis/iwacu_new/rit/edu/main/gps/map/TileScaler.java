package gis.iwacu_new.rit.edu.main.gps.map;

import gis.iwacu_new.rit.edu.main.gps.map.BitmapCacheWrapper;
import gis.iwacu_new.rit.edu.main.gps.map.LocalStorageWrapper;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


public class TileScaler{

	public static void get(final RawTile tile, final Handler handler){
		Bitmap bitmap = getScaler(tile).scale();
		handler.handle(tile, bitmap, true);
	}

	public static Bitmap get(final RawTile tile){
		return getScaler(tile).scale();
	}
	
	private static Scaler getScaler(final RawTile tile){
		Scaler scaler = new Scaler(tile);
		return scaler;
	}
	
	private static class Scaler{
		
		private RawTile tile;
		
		public Scaler(RawTile tile){
			this.tile=tile;
		}
		
		public Bitmap scale(){
			Bitmap bitmap =  findTile(tile.x, tile.y, tile.z, tile.s);
			return bitmap;
		}
		
		
		private int getTileSize(int zoom) {
			return (int) (256 / Math.pow(2, zoom));
		}

		private Bitmap findTile(int x, int y, int z, int s) {
			Bitmap bitmap = null;
			int offsetX;
			int offsetY;
			int offsetParentX;
			int offsetParentY;
			int parentTileX;
			int parentTileY;
			
			offsetX = x * 256; 
			offsetY = y * 256; 
			int tmpZ = z;
			while (bitmap == null && tmpZ <= 17) {
				tmpZ++;

				
				offsetParentX = (int) (offsetX / Math.pow(2, tmpZ - z));
				offsetParentY = (int) (offsetY / Math.pow(2, tmpZ - z));

				
				parentTileX = offsetParentX / 256;
				parentTileY = offsetParentY / 256;

				
				
				RawTile tmpTile = new RawTile(parentTileX,
						parentTileY, tmpZ, s);
				
				if (bitmap == null) {
					bitmap = BitmapCacheWrapper.getInstance().getTile(tmpTile);	
				}
				
				if (bitmap == null) {
					bitmap = LocalStorageWrapper.get(tmpTile);
				}
				
				if (bitmap == null) {
				} else { 
					offsetParentX = offsetParentX - parentTileX * 256;
					offsetParentY = offsetParentY - parentTileY * 256;

					
					int scale = tmpZ - z;
					int tileSize = getTileSize(scale);

					
					int[] pixels = new int[tileSize * tileSize];
					if (offsetParentY >= 0 && offsetParentX >= 0 && tileSize>0) {
						bitmap.getPixels(pixels, 0, tileSize, offsetParentX,
								offsetParentY, tileSize, tileSize);
						bitmap = Bitmap.createBitmap(pixels, tileSize, tileSize,
								Config.ARGB_8888);
						pixels = null;
						return Bitmap.createScaledBitmap(bitmap, 256, 256, false);
					}
				}
			}
			return null;
		}
		
	}
	
}
