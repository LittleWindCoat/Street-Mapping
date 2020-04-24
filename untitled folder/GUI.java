package project04;

/*
 * lWC
 * 04/26/2017
 */

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

public class GUI extends JPanel{
	
	
	//private static MapGUI temp;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static ArrayList<Road> roads;
	public static HashMap<String, Intersection> intersectionMap;
	public static boolean thickLines = false;
	
	public static double minLat, minLong, maxLat, maxLong;
	public static double x, y;
	
	public GUI(ArrayList<Road> roads, HashMap<String, Intersection> intersectMap, double minimumLat, double maximumLat, double minimumLong, double maximumLong) {
		
		GUI.roads = roads;
		GUI.intersectionMap = intersectMap;
		
		minLat = minimumLat;
		maxLat = maximumLat;
		minLong = minimumLong;
		maxLong = maximumLong;
		
		setPreferredSize(new Dimension(800, 800));
		
	}
	
	//paint component
	public void paintComponent(Graphics page) {
		
		Graphics2D page2 = (Graphics2D) page;
		super.paintComponent(page2);
		
		page2.setColor(Color.BLACK);
		
		if(thickLines) {
			page2.setStroke(new BasicStroke(3));
		}
		
		//Set the scales
		x = this.getWidth() / (maxLong - minLong);
		y = this.getHeight() / (maxLat - minLat);
		
		Intersection int1, int2;
		
		double x1, y1, x2, y2;
		
		//Graphing
		for(Road r : roads) {
			
			scale();
			
			int1 = intersectionMap.get(r.intersect1);
			int2 = intersectionMap.get(r.intersect2);
			
			x1 = int1.longitude;
			y1 = int1.latitude;
			x2 = int2.longitude;
			y2 = int2.latitude;
		
			page2.draw(new Line2D.Double((x1-minLong) * x, getHeight() - ((y1 - minLat) * y), 
					(x2-minLong) * x, getHeight() - ((y2 - minLat) * y)));
			
		}
		
		//The direction
		if(Map.dijkstraPath != null) {
			
			page2.setColor(Color.PINK);
			
			for(int i = 0; i < Map.dijkstraPath.length - 1; i++) {
				
				x1 = Map.dijkstraPath[i].longitude;
				y1 = Map.dijkstraPath[i].latitude;
				x2 = Map.dijkstraPath[i+1].longitude;
				y2 = Map.dijkstraPath[i+1].latitude;
				
				page2.draw(new Line2D.Double((x1-minLong) * x, getHeight() - ((y1 - minLat) * y), 
						(x2-minLong) * x, getHeight() - ((y2 - minLat) * y)));

			}
			
		}
		
		//The Meridian Map
		if(Map.minWeightSpanTree != null) {
			for(Road r : Map.minWeightSpanTree) {
				
				page2.setColor(Color.BLUE);
				
				int1 = intersectionMap.get(r.intersect1);
				int2 = intersectionMap.get(r.intersect2);
				
				x1 = int1.longitude;
				y1 = int1.latitude;
				x2 = int2.longitude;
				y2 = int2.latitude;
			
				page2.draw(new Line2D.Double((x1-minLong) * x, getHeight() - ((y1 - minLat) * y), 
						(x2-minLong) * x, getHeight() - ((y2 - minLat) * y)));
			}
		}
	}
	
	//Re-scale
	public void scale() {
		
		x = this.getWidth() / (maxLong - minLong);
		y = this.getHeight() / (maxLat - minLat);
		
	}
	
	

}