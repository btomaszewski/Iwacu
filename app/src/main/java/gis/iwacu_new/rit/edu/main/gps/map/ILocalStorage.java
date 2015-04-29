package gis.iwacu_new.rit.edu.main.gps.map;

public interface ILocalStorage {

	
	public abstract void clear();

	public abstract boolean isExists(RawTile tile);

	
	public abstract void put(RawTile tile, byte[] data);

	
	public abstract byte[] get(RawTile tile);

}