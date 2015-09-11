package gis.iwacu_new.rit.edu.main;

import android.os.Parcel;
import android.os.Parcelable;

public class RecorContent 
{

	private String heading;
	private String about;
	private String activity;
	private String activity_name;
	private String activity_data;
	private String imageUrl;
	private String video_text;
	private String video_id;
	private String quiz_url;

 

public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}
	
	//the gis tool to call
	public void setActivityName(String activityName) {
		this.activity_name = activityName;
	}
	
	public String getActivityName() {
		return activity_name;
	}

	//data to pass to the GIS tool, like map  coordinates
	public void setActivityData(String activityData) {
		this.activity_data = activityData;
	}
	
	public String getActivityData() {
		return activity_data;
	}
	
	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public String getVideoId() {
		return video_id;
	}

	public void setVideoId(String VideoId) {
		this.video_id = VideoId;
	}
	
	public String getVideoText() {
		return video_text;
	}

	public void setVideoText(String VideoText) {
		this.video_text = VideoText;
	}
	
	public String getQuizURL() {
		return quiz_url;
	}

	public void setQuizURL(String QuizURL) {
		this.quiz_url = QuizURL;
	}

	public String toString()
	 {
		return heading + "\n" + about;
		 
	 }


}