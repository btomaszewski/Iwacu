package gis.iwacu_new.rit.edu.main.gps.map;

public class Marker {

    public Place place;
    public RawTile tile;
    public Point offset;
    private boolean gps;
    private MarkerImage markerImage;

    public Marker(Place place, MarkerImage markerImage, boolean isGPS){
        this.place = place;
        this.gps = isGPS;
        this.markerImage = markerImage;
    }

    public Point getOffset(){
        return this.offset;
    }

    public MarkerImage getMarkerImage(){
        return this.markerImage;
    }

    public void setMarkerImage(MarkerImage markerImage){
        this.markerImage = markerImage;
    }

    public boolean isGps() {
        return gps;
    }
}
