package project4;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;

import javax.swing.JPanel;

/**
 * 
 */
public class MapJPanel extends JPanel {
	private static final long serialVersionUID = 3136876175510584357L;

	private float stroke = 2.0f;// 

	MapFrame shortPJF = null;

	public MapJPanel(MapFrame mapFrame) {
		// TODO Auto-generated constructor stub
		shortPJF = mapFrame;

		setBackground(Color.white);
		addMouseListener(new MouseA());
		this.setPreferredSize(new Dimension(600, 600));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		if(shortPJF.roads!= null){
			Collection<Road> roadvals = shortPJF.roads.values();
			g2d.setPaint(Color.BLACK);
			
			g2d.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND,
					BasicStroke.JOIN_BEVEL));
	
			for(Road r:roadvals){
				g2d.drawLine(r.x1, r.y1, r.x2, r.y2);

			}
			g2d.setPaint(Color.RED);
			
			if(shortPJF.minroads!=null && shortPJF.minroads.size()>0){
				for(Road r:shortPJF.minroads){
					g2d.drawLine(r.x1, r.y1, r.x2, r.y2);

				}
			}
		}

	}







	
	class MouseA extends MouseAdapter {

		@Override
		public void mouseReleased(MouseEvent me) {
			if(shortPJF.btnSetState>0){
				shortPJF.setKeyPoint(me.getX(),me.getY());
			}
		}

	}

}
