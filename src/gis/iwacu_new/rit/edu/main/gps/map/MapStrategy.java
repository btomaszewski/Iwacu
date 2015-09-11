package gis.iwacu_new.rit.edu.main.gps.map;

public abstract class MapStrategy {

	public abstract String getURL(int x, int y, int z, int layout);

	public abstract String getDescription();
}
