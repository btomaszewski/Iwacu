package gis.iwacu_new.rit.edu.main.gps.map;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.ZoomControls;


public class ZoomPanel extends RelativeLayout {

	private ZoomControls zoomControls;

	public ZoomPanel(Context context) {
		super(context);
		zoomControls = new ZoomControls(getContext());
		addView(zoomControls);
	}

	
	public void setIsZoomInEnabled(boolean isEnabled) {
		zoomControls.setIsZoomInEnabled(isEnabled);
	}

	
	public void setIsZoomOutEnabled(boolean isEnabled) {
		zoomControls.setIsZoomOutEnabled(isEnabled);
	}

	/**
	 * 
	 * @param onClickListener
	 */
	public void setOnZoomInClickListener(OnClickListener onClickListener) {
		zoomControls.setOnZoomInClickListener(onClickListener);
	}

	/**
	 * 
	 * @param onClickListener
	 */
	public void setOnZoomOutClickListener(OnClickListener onClickListener) {
		zoomControls.setOnZoomOutClickListener(onClickListener);
	}

}
