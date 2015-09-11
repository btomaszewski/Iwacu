package gis.iwacu_new.rit.edu.main.gps.map;

import gis.iwacu_new.rit.edu.main.gps.map.RawTile;

public interface ILocalStorage {

	
	public abstract void clear();

	public abstract boolean isExists(RawTile tile);

	
	public abstract void put(RawTile tile, byte[] data);

	
	public abstract byte[] get(RawTile tile);

}