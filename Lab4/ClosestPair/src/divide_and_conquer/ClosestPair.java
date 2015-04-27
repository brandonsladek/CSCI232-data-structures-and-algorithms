package divide_and_conquer;

// Imports
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ClosestPair {
	
	// Nested Point class
	public static class Point {
		
		// Each instance of point has an x and a y coordinate
		private double x;
		private double y;
		
		// Constructor
		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}

		// Getter and setter methods
		public double getX() { return x; }

		public void setX(double x) { this.x = x; }
		
		public double getY() { return y; }

		public void setY(double y) { this.y = y; }
		
	} // End Point
	
	// Nested Pair class
	public static class Pair {
		
		// A Pair consists of two points and the distance between them
		private Point p1;
		private Point p2;
		private double distance;
		
		// Empty constructor
		public Pair() {}
		
		// Constructor
		public Pair(Point p1, Point p2, double distance) {
			this.p1 = p1;
			this.p2 = p2;
			this.distance = distance;
		}
		
		// Getter methods
		public Point getP1() { return p1; }
		
		public Point getP2() { return p2; }
		
		public double getDistance() { return distance; }
		
		// NOTE: There's no need for setter methods since points are always determined at object instantiation
		
	} // End Pair
	
	// Declare new arraylist for point objects sorted by x-value
	static ArrayList<Point> pointsX = new ArrayList<Point>();
	
	
	// Main method
	public static void main(String[] args) {
				
		// Hardcode points into x-sorted arraylist
		pointsX.add(new Point(2.0, 7.0));
		pointsX.add(new Point(4.0, 13.0));
		pointsX.add(new Point(5.0, 8.0));
		pointsX.add(new Point(10.0, 5.0));
		pointsX.add(new Point(14.0, 9.0));
		pointsX.add(new Point(15.0, 5.0));
		pointsX.add(new Point(17.0, 7.0));
		pointsX.add(new Point(19.0, 10.0));
		pointsX.add(new Point(22.0, 7.0));
		pointsX.add(new Point(25.0, 10.0));
		pointsX.add(new Point(29.0, 14.0));
		pointsX.add(new Point(30.0, 2.0));
		
		
		// Sort the points by x coordinate
		Collections.sort(pointsX, new Comparator<Point>() {
			public int compare(Point one, Point two) {
				return Double.compare(one.getX(), two.getX());
			}
		});
		
		// Print input points
		printInputPoints( pointsX );
		
		System.out.println("-------------------------------------------------------------------");
		
		// Get closest pair of points
		Pair closest = closestPair( pointsX );
		
		System.out.println("-------------------------------------------------------------------");
		
		// Print final result
		System.out.println("Final result: P1: ("+closest.p1.getX()+","+closest.p1.getY()+"), ("
			+closest.p2.getX()+","+closest.p2.getY()+"), Distance: "+String.format("%.1f",closest.distance));
		
	} // End main
	
	
	// Method to print input points
	public static void printInputPoints(ArrayList<Point> points) {
		
		System.out.println("Input points:");
		
		// Print each input pair
		for(int i = 0; i < points.size(); i++) {
			// Print newline after six points
			if ((i+1)%6 == 0) {
			System.out.println("(" + points.get(i).getX() + "," + points.get(i).getY() + ")");
			} else {
				System.out.print("(" + points.get(i).getX() + "," + points.get(i).getY() + ")");
			}
		} // End for
	} // End printInputPoints
	
	
	// Method to compute distance between two points
	public static double distance(Point one, Point two) {
		double distance;
		distance = Math.sqrt(Math.pow(one.getX() - two.getX(), 2) + Math.pow(one.getY() - two.getY(), 2));
		return distance;
	} // End distance
	
	
	// Method to find closest pair between all points
	public static Pair closestPair(ArrayList<Point> subset) {

		// Get subset indices with respect to original points arraylist
		int start = pointsX.indexOf(subset.get(0));
		int finish = pointsX.indexOf(subset.get((subset.size()-1)));
		
		System.out.println("Solving problem: Point[" + start + "]...Point[" + finish + "]");
		
		// If there is only 1 point
		if (subset.size() == 1) {
			System.out.println("Found result: INF");
			// Return new pair of null points with "infinite" distance
			return new Pair(null, null, Integer.MAX_VALUE);
		}
		
		// If there is only 2 points
		if (subset.size() == 2) {
			double distance = distance(subset.get(0), subset.get(1));
			System.out.println("Found result: P1: ("+subset.get(0).getX()+","+subset.get(0).getY()+"), ("
					+subset.get(1).getX()+","+subset.get(1).getY()+"), Distance: "+String.format("%.1f",distance));
			// Return new pair with two points and the distance between them
			return new Pair(subset.get(0), subset.get(1), distance);
		}
		
		// Find median to split points into two subsets
		int median = (int) Math.ceil(subset.size()/2);
		int divide = start + median;
		
		System.out.println("Dividing at Point[" + divide + "]");
		
		// Create new arraylist for first half of points
		ArrayList<Point> firstHalf = new ArrayList<Point>();
		
		// Create new arraylist for second half of points
		ArrayList<Point> secondHalf = new ArrayList<Point>();
		
		// Add first half points to new arraylist
		for (int i = 0; i < median; i++) {
			firstHalf.add(subset.get(i));
		}
		
		// Add second half points to new arraylist
		for(int i = median; i < subset.size(); i++) {
			secondHalf.add(subset.get(i));
		}
		
		// Recursively call closestPair on two subsets
		Pair d1 = closestPair(firstHalf);
		Pair d2 = closestPair(secondHalf);
		
		// Check for pairs of points across split
		Pair d3 = closestSplitPair(subset.get(median).getX(), subset, Math.min(d1.distance, d2.distance));
		
		// Get indices for two subsets (with respect to original points arraylist)
		int index1 = pointsX.indexOf(firstHalf.get(0));
		int index2 = pointsX.indexOf(firstHalf.get(firstHalf.size()-1));
		int index3 = pointsX.indexOf(secondHalf.get(0));
		int index4 = pointsX.indexOf(secondHalf.get(secondHalf.size()-1));
		
		// Print conquer status
		System.out.println("Combining Problems: Point["+index1+"]...Point["+index2+"] and Point["+index3+"]...Point["+index4+"]");
		
		// If d1 is the closest pair
		if (d1.distance < d2.distance && d2.distance < d3.distance) {
			
			// Print found result
			System.out.println("Found result: P1: ("+d1.p1.getX()+","+d1.p1.getY()+"), ("
				+d1.p2.getX()+","+d1.p2.getY()+"), Distance: "+String.format("%.1f",d1.distance));
			
			// And return new pair
			return new Pair(d1.p1, d1.p2, d1.distance);
		}
		// If d2 is the closest pair
		else if (d2.distance < d3.distance && d2.distance < d1.distance) {
			
			// Print found result
			System.out.println("Found result: P1: ("+d2.p1.getX()+","+d2.p1.getY()+"), ("
				+d2.p2.getX()+","+d2.p2.getY()+"), Distance: "+String.format("%.1f",d2.distance));
			
			// And return new pair
			return new Pair(d2.p1, d2.p2, d2.distance);
		}
		// Else, d3 must be closest pair
		else {
			
			// Print found result
			System.out.println("Found result: P1: ("+d3.p1.getX()+","+d3.p1.getY()+"), ("
				+d3.p2.getX()+","+d3.p2.getY()+"), Distance: "+String.format("%.1f",d3.distance));
			
			// And return new pair
			return new Pair(d3.p1, d3.p2, d3.distance);
		}
		
	} // End closestPair
	
	
	// Method to find closest pair across split
	public static Pair closestSplitPair(double split, ArrayList<Point> points, double d) {
				
		// Declare arraylist for points within d range of split
		ArrayList<Point> pointsWithinRange = new ArrayList<Point>();
		
		// Add points within d range of split to pointsWithinRange arraylist
		for (int i = 0; i < points.size(); i++) {
			if (Math.abs(points.get(i).getX() - split) < d) {
				pointsWithinRange.add(points.get(i));
			}
		}
				
		// Create new closestSplitPair (not updated)
		Pair closestSplitPair = new Pair(null, null, Integer.MAX_VALUE);
		
		// The inner loop only runs at most six times because the difference in the y-values of the points 
		// must be less than the closest pair distance of the two subsets if the distance is to be computed
		for (int i = 0; i < pointsWithinRange.size(); i++) {
			for (int j = i+1; j < pointsWithinRange.size() && Math.abs(pointsWithinRange.get(j).getY() - pointsWithinRange.get(i).getY()) < d; j++) {
				if (distance(pointsWithinRange.get(i), pointsWithinRange.get(j)) < d) {
					// Update closestSplitPair
					d = distance(pointsWithinRange.get(i), pointsWithinRange.get(j));
					closestSplitPair.distance = d;
					closestSplitPair.p1 = pointsWithinRange.get(i);
					closestSplitPair.p2 = pointsWithinRange.get(j);
				}
			}
		}		
		return closestSplitPair;
	} // End closestSplitPair
	
	 
} // End class
