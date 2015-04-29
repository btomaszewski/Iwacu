package gis.iwacu_new.rit.edu.main.gps.map;

import java.io.Serializable;


public class RawTile implements Serializable {

	
	private static final long serialVersionUID = -3536701428388595925L;
	public int x, y, z, s;


	public RawTile(int x, int y, int z, int s) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.s = s;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
		result = prime * result + s;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof RawTile)) {
            return false;
        }
		RawTile other = (RawTile) obj;
		return x == other.x && y == other.y && z == other.z && s == other.s;
	}

	@Override
	public String toString() {
		String path = ""+s+"/"+z+"/"+x+"/"+y+"/";
		return path;
	}

}
