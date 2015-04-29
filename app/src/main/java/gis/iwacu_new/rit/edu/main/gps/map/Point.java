package gis.iwacu_new.rit.edu.main.gps.map;

public class Point {

	public double x;
	public double y;
	
	public Point(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public Point(){
		
	}
	
	@Override
	public String toString(){
		return x + " ^ " + y;
	}

	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
}
