package project04;

/*
 * lWC
 * 04/26/2017
 */


public class LinkedList {
	
	public int size;
	public Node head;
	
	//constructor
	public LinkedList() {
		head = new Node();
		size = 0;
	}
	
	public int size() {
		return size;
	}
	
	public double findCost(Intersection int2) {

		Edge temp2 = head.edge;
	
		while(temp2 != null) {
			
			if(temp2.road.intersect1.equals(int2.IntersectionID) || temp2.road.intersect2.equals(int2.IntersectionID)) {
				return temp2.road.distance;
			}
			
			temp2 = temp2.next;
		}
		
		return -1;
		
	}
	
	//Inset method
	public void insert(Intersection intersect) {
		
		if(head.intersection == null) {
			head.intersection = intersect;
		}
		
		size++;
	}
	

	public boolean connected(Intersection int2) {
		
		Edge temp2 = head.edge;
		
		while(temp2 != null) {
			
			if(temp2.road.intersect1.equals(int2.IntersectionID) || temp2.road.intersect2.equals(int2.IntersectionID)) {
				return true;
			}
			
			temp2 = temp2.next;
		}
		
		return false;
		
	}
	
	//Contains method
	public boolean contains(Intersection i) {
		
		Node temp = head;
		
		while(temp != null) {
			
			if(temp.intersection.equals(i)) {
				return true;
			}
			
			temp = temp.next;
		}
		
		return false;
		
	}
	
	//Insert method
	public void insert(Road road) {
		
		Edge tempEdge = new Edge();
		tempEdge.road = road;
		
		tempEdge.next = head.edge;
		head.edge = tempEdge;
		

	}

}