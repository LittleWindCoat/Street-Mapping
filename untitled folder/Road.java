package project04;

/*
 * lWC
 * 04/26/2017
 */

public class Road {
	
	String roadID;
	String intersect1;
	String intersect2;
	double distance;
	
	//constructor
	public Road(String road, String int1, String int2, double dist) {
		roadID = road;
		intersect1 = int1;
		intersect2 = int2;
		distance = dist;
	}

}