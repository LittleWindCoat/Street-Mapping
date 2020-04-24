package project4;

import java.util.HashMap;

public class InterSection {
	String pointID ;
	double longitude;
	double latitude;
	
	int x ;  // for panel 
	int y ;  // for panel 
	
	double distance ;
	HashMap<Road,InterSection> rd_points;   
	Road validRoad ;    // DijkstraALG 

	
	public InterSection(String pointID, double longitude, double latitude) {
		super();
		this.pointID = pointID;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	
	public String toString(){
		return pointID ;
	}
}
