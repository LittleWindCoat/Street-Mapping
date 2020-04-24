package project4;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MapFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = -1877582687108858783L;
	private String shortPath = "ShortPath";

	private JPanel nothJP = null, centerJP = null;
	private JLabel beginJLabel = null, endJLabel = null;

	private MapJPanel mapJPanel = null;
	HashMap<String, InterSection> points = new HashMap<String, InterSection>();
	HashMap<String, Road> roads = new HashMap<String, Road>();
	ArrayList<Road> minroads = new ArrayList<Road>();
	InterSection startPoint;
	InterSection endPoint;
	int btnSetState = 0; // 0 ：idle 

	private int maxMapWidth = 800; //
	private int maxMapHeight = 800;

	public MapFrame() {
		// TODO Auto-generated constructor stub

		this.setTitle(shortPath);
		// start
		nothJP = new JPanel();
		beginJLabel = new JLabel();
		beginJLabel.setText("begin:unkown ");
		nothJP.add(beginJLabel);

		// end
		endJLabel = new JLabel();
		endJLabel.setText("end:unkwon  ");
		nothJP.add(endJLabel);

		JButton readMapBtn = new JButton("readMapFile");
		final JButton setStartBtn = new JButton("setStartPoint");
		setStartBtn.setToolTipText("StartPoint");
		final JButton setEndBtn = new JButton("setEndPoint");
		setEndBtn.setToolTipText("EndPoint");
		JButton getShortPathBtn = new JButton("getShortPath");
		nothJP.add(readMapBtn);
		nothJP.add(setStartBtn);
		nothJP.add(setEndBtn);
		nothJP.add(getShortPathBtn);

		centerJP = new JPanel();
		centerJP.setPreferredSize(new Dimension(500, 500));

		mapJPanel = new MapJPanel(this);
		centerJP.add(mapJPanel, BorderLayout.CENTER);

		this.add(nothJP, BorderLayout.NORTH);
		this.add(centerJP, BorderLayout.CENTER);

		// Flowout
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		flowLayout.setHgap(5);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		Toolkit toolkit = getToolkit();
		Dimension dim = toolkit.getScreenSize();
		this.setLocation((dim.width - 600) / 2, (dim.height - 600) / 2);
		pack();

		readMapBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.showDialog(null, "please choose map data file");
				File file = jfc.getSelectedFile();
				if (file == null) {
					return;
				}
				if (!file.getName().endsWith(".txt"))
					return;

				//数据复位
				points.clear();
				roads.clear();
				minroads.clear();
				startPoint = null ;
				endPoint = null ;
				beginJLabel.setText("begin:unkown ");
				endJLabel.setText("end:unkwon  ");
				
				
				try {
					Scanner scan = new Scanner(file);
					while (scan.hasNextLine()) {
						String line = scan.nextLine();
						String[] info = line.split("\t");
						if (info.length > 3) {
							if (line.startsWith("i")) {
								String interID = info[1].trim();
								double lat = Double.parseDouble(info[2]);
								double longitude = Double.parseDouble(info[3]);
								InterSection p = new InterSection(interID, lat, longitude);
								points.put(interID, p);
							} else if (line.startsWith("r")) {
								String rid = info[1].trim();
								String interID1 = info[2].trim();
								String interID2 = info[3].trim();
								Road r = new Road(rid, interID1, interID2);
								roads.put(rid, r);
							}
						}
					}

				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if (points.size() > 0 && roads.size() > 0) {
					initMapData();
					centerJP.repaint();
				}
				btnSetState = 0;
			}

		});

		setStartBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				btnSetState = 1;
				// setStartBtn.setEnabled(false);

			}

		});

		setEndBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				btnSetState = 2;
			}

		});

		getShortPathBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				btnSetState = 0;
				dijkstraALG();
				centerJP.repaint();

			}

		});

	}

	private void initMapData() {
		double minlat = 1000;
		double maxlat = -1000;
		double minlong = 1000;
		double maxlong = -1000;
		Collection<InterSection> pointvals = points.values();
		for (InterSection p : pointvals) {
			if (p.latitude > maxlat)
				maxlat = p.latitude;
			if (p.latitude < minlat)
				minlat = p.latitude;

			if (p.longitude > maxlong)
				maxlong = p.longitude;
			if (p.longitude < minlong)
				minlong = p.longitude;
		}

		double xfix = maxMapWidth / (maxlong - minlong);
		double yfix = maxMapHeight / (maxlat - minlat);
		for (InterSection p : pointvals) {
			p.x = (int) ((p.longitude - minlong) * xfix);
			p.y = (int) ((p.latitude - minlat) * yfix);
		}

		Collection<Road> roadvals = roads.values();
		for (Road r : roadvals) {
			InterSection p1 = points.get(r.pointID1);
			InterSection p2 = points.get(r.pointID2);
			r.distance = weight(p1.latitude, p1.longitude, p2.latitude, p2.longitude);
			r.x1 = p1.x;
			r.y1 = p1.y;
			r.x2 = p2.x;
			r.y2 = p2.y;
		}
	}

	private double weight(double vlat, double vlon, double wlat, double wlon) {
		// displacement of longitude and latitude
		double distance = Math.sqrt(((vlat-wlat)*(vlat-wlat)+(vlon-wlon)*(vlon-wlon)));

		return distance;
	}

	/*
	private double weight(double vlat, double vlon, double wlat, double wlon) {
		// displacement of longitude and latitude
		double displacementLatitude = Math.toRadians(wlat - vlat);
		double displacementLongitude = Math.toRadians(wlon - vlon);
		vlat = Math.toRadians(vlat);
		wlat = Math.toRadians(wlat);
		// earth's radius (in miles)
		double earthRadius = 3961;

		double a = Math.pow((Math.sin(displacementLatitude / 2)), 2)
				+ Math.cos(vlat) * Math.cos(wlat) * Math.pow(Math.sin(displacementLongitude / 2), 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		// the distance in miles traveled
		double distance = earthRadius * c;

		return distance;
	}
	*/

	
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	private InterSection findNearPoint(int x, int y) {
		if (points == null)
			return null;
		InterSection pmin = null;
		Collection<InterSection> pointvals = points.values();
		int minval = 100000;
		for (InterSection p : pointvals) {
			int val = Math.abs(p.x - x) + Math.abs(p.y - y);
			if (val < minval) {
				minval = val;
				pmin = p;
			}
		}
		return pmin;
	}

	public void setKeyPoint(int x, int y) {
		if (btnSetState < 1)
			return;
		InterSection pkey = findNearPoint(x, y);
		if (btnSetState == 1) {
			startPoint = pkey;
			beginJLabel.setText("begin:" + startPoint.pointID);

		}
		if (btnSetState == 2) {
			endPoint = pkey;
			endJLabel.setText("end:" + endPoint.pointID);
		}

	}


	public void dijkstraALG() {
	
		Collection<InterSection> pointvals = points.values();
		Collection<Road> roadvals = roads.values();

		HashSet<InterSection> donePoints = new HashSet<InterSection>();
		HashSet<InterSection> otherPoints = new HashSet<InterSection>();

		for (InterSection p : pointvals) {
			p.distance = Double.MAX_VALUE;
			p.rd_points = new HashMap<Road, InterSection>();
			p.validRoad = null;
			// otherPoints.add(p);
		}

		for (Road r : roadvals) {
			InterSection p1 = points.get(r.pointID1);
			InterSection p2 = points.get(r.pointID2);
			p1.rd_points.put(r, p2);
			p2.rd_points.put(r, p1);
		}

		startPoint.distance = 0;
		InterSection minp = startPoint;
		donePoints.add(minp);
		otherPoints.remove(minp);
		while (minp != endPoint) {
			for (Entry<Road, InterSection> obj : minp.rd_points.entrySet()) {
				InterSection p = obj.getValue();
				if (!donePoints.contains(p)) {
					Road r = obj.getKey();
					double new_distance = minp.distance + r.distance;
					if (new_distance < p.distance) {
						p.distance = new_distance;
						p.validRoad = r;
					}

					if (!otherPoints.contains(p))
						otherPoints.add(p);
				}
			}

			minp = null;
			double min_distance = Double.MAX_VALUE;
			for (InterSection p : otherPoints) {
				if (p.distance < min_distance) {
					min_distance = p.distance;
					minp = p;
				}
			}
			if (minp == null) {
				System.out.println("something is wrong!"); 
				break;
			}
			donePoints.add(minp);
			otherPoints.remove(minp);
		}

		minroads.clear();
		InterSection cp = endPoint;
		if (cp.distance < Double.MAX_VALUE) {
			while (cp != startPoint) {
				minroads.add(cp.validRoad);
				cp = cp.rd_points.get(cp.validRoad);
			}
		}
	}

}
