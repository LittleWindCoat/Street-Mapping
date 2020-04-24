package project04;

/*
 * lWC
 * 04/26/2017
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFrame;

public class Test {
	
	public static void main(String [] args) throws FileNotFoundException {
		
		//Timer
		long startTime = System.currentTimeMillis();
		
		//name of file
		File mapData = new File(args[0]);
		
		//thicker the line of ur
		if(args[0].equals("ur.txt")) {
			GUI.thickLines = true;
		}
		
		
		//find the intersections
		Scanner scan = new Scanner(mapData);
		
		int numIntersects = 0;
		
		while(scan.nextLine().startsWith("i")) {
			numIntersects++;
		}

		scan.close();
		
		String intersectionID;
		double lat, longitude;
		Intersection v;
		
		//scan all data
		Scanner scan2 = new Scanner(mapData);
		
		//create the Map
		Map map = new Map(numIntersects);
		
		String currentLine = scan2.nextLine();
		
		String [] info;

		//Insert
		while(currentLine.startsWith("i")) {
			
			info = currentLine.split("\t");
			
			intersectionID = info[1];
			lat = Double.parseDouble(info[2]);
			longitude = Double.parseDouble(info[3]);
			
			v = new Intersection();
			v.distance = Integer.MAX_VALUE;
			v.IntersectionID = intersectionID;
			v.latitude = lat;
			v.longitude = longitude;
			v.known = false;
			
			currentLine = scan2.nextLine();
			
			map.insert(v);
		}
		
		String roadID, int1, int2;
		
		Intersection w, x;
		
		double distance;
		
		while(currentLine.startsWith("r")) {
			
			info = currentLine.split("\t");
			
			roadID = info[1];
			
			int1 = info[2];
			int2 = info[3];
			
			w = Map.intersectLookup(int1);
			x = Map.intersectLookup(int2);
			
			distance = Map.roadDist(w, x);
			
			map.insert(new Road(roadID, int1, int2, distance));
			
			if(scan2.hasNextLine() == false) {
				break;
			}
			
			currentLine = scan2.nextLine();
			
		}
		

		String fileName;
		
		//title
		if(args[0].equals("ur.txt")) {
			fileName = "U of R";
		}
		else {
			if(args[0].equals("monroe.txt")) {
				fileName = "Monroe County";
			}
			else {
				if(args[0].equals("nys.txt")) {
					fileName = "New York State";
				}
				else {
					fileName = "Map";
				}
			}
		}
		
		boolean showMap = false;
		boolean dijkstras = false;
		boolean mwst = false;
		
		//default directions
		String directionsStart = "i0";
		String directionsEnd = "i1";
		
		//checks the command line arguments
		for(int i = 0; i < args.length; i++) {
			if(args[i].equals("-show")) {
				showMap = true;
			}
			
			if(args[i].equals("-directions")) {
				dijkstras = true;
				
				directionsStart = args[i+1];
				directionsEnd = args[i+2];
			}
			
			if(args[i].equals("-meridianmap")) {
				mwst = true;
			}
			
		}
		
		//if directions are needed
		if(dijkstras == true) {
			
			map.dijkstra(directionsStart);
			
			System.out.println("\nThe shortest path from " + directionsStart + " to " + directionsEnd + " is: ");
			System.out.println(Map.formPath(directionsEnd));
			
			System.out.println("Length of the path from " + directionsStart + " to " + directionsEnd + " is: " + Map.dijkstraPathLength() + " miles.");
		}
		
		//if the meridian map is needed
		if(mwst == true) {
			
			map.kruskals();
			
			System.out.println("\nRoads Taken to Create Minimum Weight Spanning Tree for " + fileName + ":\n");
			
			for(Road r : Map.minWeightSpanTree) {
				System.out.println(r.roadID);
			}
			
		}
		
		if(showMap == true) {
		
			JFrame frame = new JFrame("Map");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			frame.getContentPane().add(new GUI(Map.roads, Map.intersectionMap, Map.minLat, Map.maxLat, Map.minLong, Map.maxLong));
			frame.pack();
			frame.setVisible(true);
		}
		
		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime-startTime;
		
		System.out.println("\n\nTime required to do everything: " + elapsedTime/1000 + " seconds.");
		
		
		scan2.close();
	}

}