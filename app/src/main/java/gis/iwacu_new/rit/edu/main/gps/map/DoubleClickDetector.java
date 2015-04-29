package gis.iwacu_new.rit.edu.main.gps.map;

import android.graphics.Point;
import android.view.MotionEvent;


public class DoubleClickDetector {

	
	private static int CLICK_INTERVAL = 600;

	
	private static int CLICK_PRECISE = 12;

	
	private Point previousPoint;

	private long eventTime;
	
	public static void setInterval(int interval){
		CLICK_INTERVAL = interval;
	}
	
	
	public static void setPrecise(int precise){
		CLICK_PRECISE = precise;
	}
	
	public boolean process(MotionEvent currentEvent) {
		if (previousPoint != null
				&& (System.currentTimeMillis() - eventTime) < DoubleClickDetector.CLICK_INTERVAL
				&& isNear((int) currentEvent.getX(), (int) currentEvent.getY())) {
			eventTime = System.currentTimeMillis();
			previousPoint = null;
			return true;
		}
		previousPoint = new Point();
		previousPoint.x = (int) currentEvent.getX();
		previousPoint.y = (int) currentEvent.getY();
		eventTime = System.currentTimeMillis();
		return false;
	}

	
	 
	private boolean isNear(int x, int y) {
		boolean checkX = Math.abs(previousPoint.x - x) <= DoubleClickDetector.CLICK_PRECISE;
		boolean checkY = Math.abs(previousPoint.y - y) <= DoubleClickDetector.CLICK_PRECISE;
		return checkX == checkY && checkX == true;
	}

}
