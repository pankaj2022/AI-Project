package chebbyshevdistance;

import java.awt.*;


public class Node implements Comparable<Node> {

	private Node prev = null;		 				// Recent/Previous Node on which action was taken to keep track of the Path.
	private final int x, y, width, height; 			// Specifies the x-coordinate,y-coordinate , Width and Height of the Node/Square.
	private int f, g, h;							// F: Total cost. G: Actual cost. H: Estimated cost to reach end
	private boolean isObstacle = false;				// If Any obstacle is between the path or not
	
	// This Defines the Color Given to Each of the Node/Square depending upon its State( i.e Selected,obstacle,default node/square)
	private final static Color  defaultFill = new Color(0, 0, 255),  //blue
								obstacleFill = new Color(255,0,0), // red
								selectedFill = new Color(165,42,42); //
	private Color fill = defaultFill;
	private final Font font = new Font("verdana", Font.BOLD, 11);

	
	// To initialize the Node with its dimensions and f,g,h values per Node/Square.
	public Node(int x, int y, int width, int height) {     
	
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		// Set the values to MAX initially to all the Nodes.
		this.f = this.g = this.h = Integer.MAX_VALUE;
	}
	
	public void draw(Graphics g, boolean isSelected) {
		g.setColor(isSelected ? selectedFill : fill);
		g.fillRect(x + 1, y + 1, width - 1, height - 1);
		g.setColor(Color.WHITE);
		g.setFont(font);
		if(this.f != Integer.MAX_VALUE) {
			g.drawString(("F: " + f), x + 15, y + 20);
			g.drawString(("G: " + this.g), x + 15, y + 35);
			g.drawString(("H: " + h), x + 15, y + 50);
		}
	}
	
	public void print() {
		System.out.println("(" + x / 60 + ", " + y / 60 + ")" +
				" F: " + f + " G: " + g + " H: " + h);
		if(prev != null) {
			System.out.print("\tPrev: ");
			prev.print();
		}	
	}
	
	/**
	 * Resets all of the node values for calculating a new path
	 */
	public void reset() {
		f = g = h = Integer.MAX_VALUE;
		prev = null;
		if(!isObstacle) {
			fill = defaultFill;
		}
	}
	
	
	public int chebyshevdistance(Node node)   // For diagonal moves it is considered.
	{
		     int i=0; 
	     	 i = Math.max(Math.abs(x - node.x)/60,Math.abs(y - node.y)/60);   // Returns the current node's estimated distance(chebyshevdistance).
			 return i;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getF() {
		return f;
	}

	public void setF(int f) {
		this.f = f;
	}
	
	public void setF(Node node) {
		h = chebyshevdistance(node);	// Calculate estimated distance to the end node
		f = g + h;	// Set F equal to the sum of steps taken (G) and remaining steps estimated (H)
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getH() {
		return h;
	}
	
	public void setH(int h) {
		this.h = h;
	}

	public Node getPrev() {
		return prev;
	}

	public void setPrev(Node prev) {
		this.prev = prev;
	}
	
	public boolean isObstacle() {
		return isObstacle;
	}

	/**
	 * Toggles a node as an impassable obstacle
	 */
	public void toggleObstacle() {
		isObstacle = !isObstacle;
		if(isObstacle) {
			fill = obstacleFill;
		} else {
			fill = defaultFill;
		}
	}
	
		public void setFill(Color fill) {
		this.fill = fill;
	}
	
	@Override
	// Compares F values to keep nodes with lowest F at the top of the queue
	public int compareTo(Node node) {
		if(f < node.getF()) {
			return -1;
		} else if(f == node.getF()) {
			return 0;
		} else {
			return 1;
		}
	}

}
