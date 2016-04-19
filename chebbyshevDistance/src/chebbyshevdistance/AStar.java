package chebbyshevdistance;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.*;
import javax.swing.Timer;


public class AStar extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static JFrame frame = new JFrame("AI Project 3 using A* Algorithm with chebyshevdistance");    // Frame Heading is Given.
	
	public static class AStarPanel extends JPanel {
	
		private static final long serialVersionUID = 1L;
	    private static LinkedList<Node> path = new LinkedList<Node>(),
				adjacentnodes = new LinkedList<Node>();
		PriorityQueue<Node> queue = new PriorityQueue<Node>();
		private Node[][] nodes;                         	// Creating an array of objects of type Node to store the attributtes of each node with class Node as datatype.
		private Node startNode = null, endNode = null, selectedNode, bestNode;
		// This Defines the Color Given to Each of the Node/Square depending upon its State( i.e Selected,start,end,default node/square)
		private final Color defaultFill = new Color(0, 0, 255), //Blue color
				startFill = new Color(255,20,147),  // Pink color
				endFill = new Color(0,128,0),  // green color
				selectedFill = new Color(165,42,42); // brown color
		private Timer timer;
		private final int DELAY = 1000;
		private boolean isRunning = false, isCtrlDown;
		
		/**
		 * Creates the grid used for path calculations
		 */
		public AStarPanel() {
			nodes = new Node[8][8];
			for(int y = 0; y < nodes.length; y++) {
				for(int x = 0; x < nodes[0].length; x++) {
					nodes[x][y] = new Node(x * 60, y * 60, 60, 60);
				}
			}
			
			setBackground(Color.WHITE);
			addMouseListener(new MouseAction());
			addKeyListener(new KeyboardAction());
			setFocusable(true);
			timer = new Timer(DELAY, new Update());
			timer.start();
		}
		
		/**
		 * Initializes the path finder by setting values for start & end node sand
		 * adds the start node to the queue
		 */
		private void run() {
			startNode.setG(0);
			startNode.setF(endNode);
			endNode.setH(0);
			
			// A priority queue keeps node with lowest F value on top
			queue.add(startNode);	// Begin by adding the start node
			findShortestPath();	// Calculate shortest path
		}
		
		/**
		 * Continuously called by the Timer to update the path and display progress.
		 * This can be condensed to a single while loop for most practical uses.
		 */
		private void findShortestPath() {
			if(queue.contains(endNode)) {
				selectedNode = null;
				
				// Creates the path by tracing back from the end node
				path.add(endNode);
				while(path.peek().getPrev() != null) {
					path.addFirst(path.peek().getPrev());
				}
				
				// Give color to the PATH which is selected from A*
				for(Node node : path) {
					if(node != startNode && node != endNode) {
						node.setFill(selectedFill);        // Fill the node with Path color
					}
				}
				isRunning = false;
			} else {
				// Continue until end is found.  Can result in infinite loop if 
				// the end node is not accessible since the problem is NP-hard.
				updatePath();
			}
		}
		
		/**
		 * Executes a step in the calculation of the path. When a node is examined, its
		 * adjacentnodes are added to a list and each adjacentnode is checked to see if moving
		 * to it from the current node will result in a more efficient path than one
		 * that has already been established.
		 */
		private void updatePath() {
			if(adjacentnodes.isEmpty()) {
				bestNode = queue.poll();			               // Grab the best node and remove from queue
				selectedNode = bestNode;			               // Highlights the selected node in GUI
				adjacentnodes = getadjacentnodes(bestNode);        // Find the node's adjacentnodes
			}
			
			int stepCost = bestNode.getG() + 1;	               // G value is incremented i.e a cost of moving to adjacent node is added to the previous value.
			
			Node adjacentnode = adjacentnodes.pop();	           // Check for an adjacentnode
			if(!adjacentnode.isObstacle() && stepCost < adjacentnode.getG()) {	// If new G is lower and not an obstacle.
				adjacentnode.setG(stepCost);		               // Update new G cost
				adjacentnode.setPrev(bestNode);		      	       // Update previous node to reflect best path
				adjacentnode.setF(endNode);			               // Re-calculate F cost
				queue.add(adjacentnode);	   		               // Add adjacentnode to queue
			} else {
				updatePath();	                                   // Check another adjacentnode if there is no change. This is 
								                                   // only done to make the animation smoother.
			}
			
		}
		
		/**
		 * Adds all  Adjacentnodes to list
		 */
		private LinkedList<Node> getadjacentnodes(Node node) {
			LinkedList<Node> adjacentnodes = new LinkedList<Node>();
			int x = node.getX() / node.getWidth();     								// take the current node x and y.
			int y = node.getY() / node.getHeight();
			
			
			if(x - 1 >= 0 && y - 1 >= 0) {											// left upper diagonal node from current node is checked if it is there.	
				adjacentnodes.add(nodes[x - 1][y - 1]);			    				 // append it if it there
			}
			if(x - 1 >= 0 && y + 1 < nodes.length) {								// left bottom diagonal node from current node is checked if it is there.	
				adjacentnodes.add(nodes[x - 1][y + 1]);								// append it if it there
			}
			if(x + 1 < nodes[0].length && y - 1 >= 0) {								// Right upper diagonal node from current node is checked if it is there.	
				adjacentnodes.add(nodes[x + 1][y - 1]);								// append it if it there
			}
			if(x + 1 < nodes[0].length && y + 1 < nodes.length) {					// Right bottom diagonal from current node is checked if it is there.	
				adjacentnodes.add(nodes[x + 1][y + 1]);								// append it if it there
			}
			
			
			if(x - 1 >= 0) {								// left node from current node is checked if it is there.	
				adjacentnodes.add(nodes[x - 1][y]);			// append it if it there
			}
			if(y - 1 >= 0) {								// Node below(vertically lower) the current node is checked if it is there. 
				adjacentnodes.add(nodes[x][y - 1]);			// append it if it there
			}
			if(x + 1 < nodes[0].length) {					// Right node from current node is checked if it is there.	
				adjacentnodes.add(nodes[x + 1][y]);			// append it if it there
			}
			if(y + 1 < nodes.length) {						// Upper node from current node is checked if it is there.
				adjacentnodes.add(nodes[x][y + 1]);			// append it if it there
			}
			
			return adjacentnodes;
		}

		@Override
		protected void paintComponent(final Graphics gr) {
			super.paintComponent(gr);
			Graphics2D g = (Graphics2D)gr;
			for(Node[] row : nodes) {	// Draws nodes in the GUI
				for(Node node : row) {
					if(node == selectedNode) {
						node.draw(g, true);
					} else {
						node.draw(g, false);
					}
				}
			}

			g.setColor(Color.BLUE);
			g.drawString("Starting Point: Left Click | Obstruction:Ctrl&Click | Goal Point:Right Click", 15, 493);
			g.setColor(Color.RED);
			g.drawString("CHEBYSHEVDISTANCE AND DIAGONAL MOVES CAN BE MADE TO REACH GOAL STATE", 1, 520);
		}
		
		public class Update implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(isRunning && path.isEmpty()) {
					findShortestPath();
				}
				repaint();
			}
		}
		
		// Controls selection of start end nodes. Nodes are referenced [x][y].
		// X origin is on the left and Y origin is at the top of the grid.
		private class MouseAction extends MouseAdapter {
			@Override
			public void mouseClicked(MouseEvent e) {
				Node node = nodes[e.getX() / 60][e.getY() / 60];      // Take the position of the Node to be selected.
				if(e.getButton() == MouseEvent.BUTTON1 && isCtrlDown && !isRunning) {
					if(node != startNode && node != endNode) node.toggleObstacle();
				} else if(e.getButton() == MouseEvent.BUTTON1 && !isRunning && node != endNode) {
					if(!path.isEmpty()) {
						resetNodes();
					} else if(startNode != null) {
						startNode.setFill(defaultFill);
					}
					startNode = node;
					startNode.setFill(startFill);
				} else if(e.getButton() == MouseEvent.BUTTON3 && !isRunning && node != startNode) {
					if(!path.isEmpty()) {
						resetNodes();
					} else if(endNode != null) {
						endNode.setFill(defaultFill);
					}
					endNode = node;
					endNode.setFill(endFill);
				}
				
				repaint();
				if(!isRunning && startNode != null && endNode != null && path.isEmpty()) {
					run();					
					isRunning = true;
					timer.restart();
				}
			}
			
			/**
			 * Resets all of the node values for calculating a new path.
			 */
			private void resetNodes() {
				isRunning = false;
				startNode = endNode = selectedNode = null;
				path.clear();
				queue.clear();
				adjacentnodes.clear();
				for(Node[] row : nodes) {
					for(Node node : row) {
						node.reset();
					}
				}
			}
		}
		
		private class KeyboardAction extends KeyAdapter {    // This function is to set obstacle in between the start node and goal node.
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_CONTROL) isCtrlDown = true;
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_CONTROL) isCtrlDown = false;
			}
		}
	}
	
	public static void main(String[] args) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		AStarPanel panel = new AStarPanel();
		frame.add(panel);
		// width + 7, height + 45
		frame.setSize(487, 550);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	public static void close() {
		frame.dispose();
	}
}

