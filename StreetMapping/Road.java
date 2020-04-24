package project4;

public class Road {
	String RoadID ;
	String pointID1 ;
	String pointID2 ;
	double distance ;
	
	int x1 ;  
	int y1 ;
	int x2 ;
	int y2 ;
	
	public Road(String roadID, String pointID1, String pointID2) {
		super();
		RoadID = roadID;
		this.pointID1 = pointID1;
		this.pointID2 = pointID2;
	}
	
	public String toString(){
		return RoadID+" "+pointID1+" "+pointID2 ;
	}
}
