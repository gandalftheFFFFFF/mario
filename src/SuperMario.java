import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Stack;


public class SuperMario {
	
	/*
	 * Group BJ
	 * Niels Schrøder Pedersen & Nicklas Frederiksen
	 * 
	 * Assignment: Super Vector Mario!
	 */
	
	public static void main(String[] args) {
		
		// Import the race track through StdIn of the format: x \n y \n row1 \n row2 ... row_x
		String[][] track = importTrack();
		
		// Uncomment line below to print out the game board in question
//		printTrack(track);
		
		/* 
		 * General assignment notes:
		 * 
		 *  Each node in the graph is represented by an int array og length 4:
		 *  
		 *  Node(position_x, position_y, momentum_x, momentum_y)
		 *  
		 *  The momentum decides the available nodes from the current node. E.g. if Mario travels from
		 *  (x,y,a,b), without any changes, the next node will be (x+a, y+b, a, b). All other available
		 *  nodes are determined by the acceleration changes to values a and b (+/-1). The  available
		 *  changes to a and b are:
		 *  ( 0, +1) (+1,  0) (+1, +1)
		 *  ( 0, -1) ( 0, -1) (-1, -1)
		 *  (+1, -1) ( 0,  0)
		 */
		
		int minimumPath = Integer.MAX_VALUE;
		
		for (int i = 0; i < track.length; i++) {
			for (int j = 0; j < track[i].length; j++) {
				if (track[i][j].equals("S")) {
					
					// Start building directed graph for this start field and do BFS after building it
					HashMap<Node, LinkedList<Node>> g = graphConstructor(track, j, i);
					
					Node start = new Node(j, i, 0, 0);
					
					int localMinimumPath = BFS(g, start, track);
					if (localMinimumPath < minimumPath) {
						minimumPath = localMinimumPath;	
					}
				}
			}
		}
		
		System.out.println(minimumPath);
	}
	
	public static int BFS(HashMap<Node, LinkedList<Node>> graph, Node start, String[][] track) {
		
		/*
		 * This methods searches for the shortest path from start to finish in any graph.
		 * It uses a HashMap to keep track of already visited nodes, in order to not expand these
		 * twice. It uses another HashMap, path, in order to be able to backtrack any path and
		 * figure out the path from any node, to the start node.
		 */
		
		// Queue to maintain nodes that are to be expanded
		Queue<Node> q = new LinkedList<Node>();
		
		HashMap<Node, Boolean> visited = new HashMap<Node, Boolean>();
		HashMap<Node, Node> path = new HashMap<Node, Node>();
		
		q.add(start);
		
		while (!q.isEmpty()) {
			
			Node current = q.poll();
			visited.put(current, true);
			
			LinkedList<Node> children = graph.get(current);
			
			for (Node child : children) {
				
				path.put(child, current);
				
				if (isInGoal(child, track)) {
					Node backTrackNode = child;
					Stack<Node> s = new Stack<Node>();
					s.push(child);
					while (!backTrackNode.equals(start)) {
						backTrackNode = path.get(backTrackNode);
						s.push(backTrackNode);
					}
					return s.size();
				}
				if (graph.get(child) != null && !q.contains(child)) {
					q.add(child);
				}
			}
			
		}
		
		// If the queue is empty, there are no more nodes to be expanded, and a finish node is not found. Return 0!
		return 0;
		
	}
	
	public static HashMap<Node, LinkedList<Node>> graphConstructor(String[][] track, int x, int y) {
		
		/*
		 * This method creates a directed graph from the starting point x, y, 
		 * using the helper method "getNextMoves". It uses a HashMap to maintain
		 * the edges in the graph. Another HashMap to keep track of which nodes 
		 * have already been visited, so these nodes are not rebuilt (which would
		 * be unnecessary.
		 */
		
		HashMap<Node, LinkedList<Node>> graph = new HashMap<Node, LinkedList<Node>>();
		HashMap<Node, Boolean> visited = new HashMap<Node, Boolean>();
		
		// Starting node: x, y with no velocity
		Node start = new Node(x, y, 0, 0);
		
		/*
		 *  Queue to maintain which paths needs to be further expanded. If a node is added to the
		 *  queue, it's children are to be further built upon as part of the graph.
		 */
		Queue<Node> q = new LinkedList<Node>(); 
		
		q.add(start);
		
		// Keep "growing a path" / adding the path to the queue unless it ends up in a finish, or out of bounds
		while (!q.isEmpty()) {
			Node current = q.poll();
			LinkedList<Node> nextMoves = getNextMoves(current, track);
			visited.put(current, true);
			if (!isInGoal(current, track)) {
				for (Node move : nextMoves) {
					if (!current.equals(move) && visited.get(move) == null && !q.contains(move)) {
						// Add move to queue IF it's not equal to the parent AND its not been visited before
						q.add(move);

						// Map or update mapping from current --> move
						if (!graph.containsKey(current)) {
							LinkedList<Node> l = new LinkedList<Node>();
							l.add(move);
							graph.put(current, l);
						} else {
							LinkedList<Node> l = graph.get(current);
							l.add(move);
							graph.put(current, l);
							
						}
					}
				}
			} 
		}
		return graph;
	}
	
	public static boolean isInGoal(Node move, String[][] track) {
		
		/*
		 * This method evaluates whether or not a move terminates on a goal node
		 */
		
		if (track[move.getY()][move.getX()].equals("F")) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isLegalMove(Node move, String[][] track) {
		
		/*
		 * Evaluates the position of a given move. The move is deemed illegal if it meets any of the following conditions:
		 * 1. The x-coordinate is less than zero - the move is out bounds
		 * 2. The y-coordinate is less than zero - the move is out of bounds
		 * 3. The x-coordinate is greater than the horizontal length of the track - the move is out of bounds
		 * 4. The y-coordinate is greater than the vertical length of the track - the move is out of bounds
		 * 5. The move has landed on a tile that contains the letter "O" - the Mario has hit an obstacle and died!
		 */
		
		if (move.getX() < 0 || move.getY() < 0 || move.getX() >= track[0].length || move.getY() >= track.length || track[move.getY()][move.getX()].equals("O")) {
			return false;
		} else {
			return true;
		}
	}
	
	public static LinkedList<Node> getNextMoves(Node start, String[][] track) {
		
		/*
		 * This method takes any node and returns the next legal moves.
		 * This includes moves that allow Mario to "turn around" (e.g. spend two
		 * turns on the same x and y coordinate, but at different velocity).
		 */
		
		LinkedList<Node> nextMoves = new LinkedList<Node>();
		int acceleration_x = 1;
		
		// Create permutations with repetition
		for (int i = 0; i < 3; i++) {
			int acceleration_y = 1;
			for (int j = 0; j < 3; j++) {
				Node move = new Node(start.getX() + start.getMX() + acceleration_x,
						start.getY() + start.getMY() + acceleration_y,
						start.getMX() + acceleration_x,
						start.getMY() + acceleration_y
						);
				// If the move is legal, add it to the list of moves.
				if (isLegalMove(move, track)) {
					nextMoves.add(move);
				}
				acceleration_y--;
			}
			acceleration_x--;
		}
		
		return nextMoves;
	}
	
	public static String[][] importTrack() {
		
		/*
		 * This method transforms input of a specific format to a double String array
		 * that represents the game board. The method reads two integers that are the 
		 * board dimensions, rows and columns. It then reads as many lines as there 
		 * are rows and puts these into the double array. Lastly, it returns this.
		 * 
		 * The format is:
		 * row (int)
		 * column (int)
		 * row1 (String)
		 * row2(String)
		 * ...
		 */
		
		// Read board dimensions
		int rows = StdIn.readInt();
		StdIn.readLine(); // Read excess line
		int cols = StdIn.readInt();
		StdIn.readLine(); // Read excess line
		
		// Instantiate game board with appropriate rows and columns
		String[][] board = new String[rows][cols];
		
		// Read board content - one line at a time (memory trick?: one String object per row. Substrings refer to the original line String!)
		for (int i = 0; i < rows; i++) {
			String line = StdIn.readLine();
			for (int j = 0; j < cols; j++) {
				board[i][j] = line.substring(j, j+1);
			}
		}
		return board;
	}
	
	public static void printTrack(String[][] track) {
		
		/*
		 * Method for printing the race track. Used only for understanding purposes.
		 */
		
		System.out.print(" ");
		for (int i = 0; i < track.length; i++) {
			System.out.printf("%2d", i);
		}
		System.out.println();
		for (int i = 0; i < track.length; i++) {
			System.out.printf("%2d", i);
			for (int j = 0; j < track[i].length; j++) {
				System.out.printf("%2s", track[i][j]);
			}
			System.out.println();
		}
	}

}

class Node {
	
	/*
	 * This class represents a move by Mario. Each move has an x and y coordinate
	 *  integer that represents a tile on the board (e.g. 0,0 is the most top 
	 *  left corner), as well as velocity/momentum integer that represents the
	 *  current speed Mario has at the given move.
	 *  
	 *  The starting move is always (x, y, 0, 0), where x and y are the coordinates
	 *  of an "S" in the double game array
	 */
	
	private int x;
	private int y;
	private int momentum_x;
	private int momentum_y;
	
	public Node(int x, int y, int mx, int my) {
		this.x = x;
		this.y = y;
		this.momentum_x = mx;
		this.momentum_y = my;
	}
	
	public int getX() { return this.x; }
	
	public int getY() { return this.y; }
	
	public int getMX() { return this.momentum_x; }
	
	public int getMY() { return this.momentum_y; }
	
	public boolean equals(Node that) {
		if (that == null) return false;
		if (this == that) return true;
		if (this.x == that.x && this.y == that.y && this.momentum_x == that.momentum_x && this.momentum_y == that.momentum_y) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean equals(Object that) {
		if (!(that instanceof Node)) {
			return false;
		} else {
			return this.equals((Node)that);
		}
	}
	
	public String toString() {
		return "(" + x + ", " + y + ", " + momentum_x + ", " + momentum_y + ")";
	}
	
	@Override
	public int hashCode() {
		
		/*
		 * The hashCode method returns a unique hashcode for a given array of 4 integers.
		 * It does so by inserting the values of a node object in to a simple integer
		 * array, and uses the Arrays.hashCode function.
		 */
		
		int[] placeholder = new int[] {this.x, this.y, this.momentum_x, this.momentum_y};
		return Arrays.hashCode(placeholder);
	}
	
}

