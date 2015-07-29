import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.LinkedList;

import org.junit.Test;


public class SuperMarioTest {
	
	@Test
	public void getTest() {
		Node n = new Node(0,1,0,0);
		Node m = new Node(1,1,1,0);

		HashMap<Node, LinkedList<Node>> g = new HashMap<>();
		
		LinkedList<Node> l = new LinkedList<>();
		l.add(m);
		
		
		g.put(n, l);
		
		System.out.println(g.toString());
		
		System.out.println(new Node(0,1,0,0).hashCode());
		System.out.println(n.hashCode());
		System.out.println(new Node(0,1,0,0).equals(n));
		System.out.println("Can I kick it?: " + g.get(n));
		System.out.println("Can I get it?: " + g.get(new Node(0,1,0,0)));;
	
	}

	@Test
	public void testHashCodeChanges() {
		Node n1 = new Node(1,1,1,0);
		Node n2 = new Node(0,1,0,0);
		
		String[][] test_track = new String[3][3];
		
		String[] row1 = new String[] {"O", "O", "O"};
		String[] row2 = new String[] {"S", " ", "F"};
		String[] row3 = new String[] {"O", "O", "O"};
		
		test_track[0] = row1;
		test_track[1] = row2;
		test_track[2] = row3;
		
		int n1hashcode = n1.hashCode();
		int n2hashcode = n2.hashCode();
		
		assertTrue(n1.hashCode() == n1hashcode);
		assertTrue(n2.hashCode() == n2hashcode);
		
		n1.equals(n2);
		
		assertTrue(n1.hashCode() == n1hashcode);
		assertTrue(n2.hashCode() == n2hashcode);
		
		n1.toString();
		
		assertTrue(n1.hashCode() == n1hashcode);
		assertTrue(n2.hashCode() == n2hashcode);
		
		SuperMario.getNextMoves(n1, test_track);
		
		assertTrue(n1.hashCode() == n1hashcode);
		assertTrue(n2.hashCode() == n2hashcode);
		
		SuperMario.isLegalMove(n1, test_track);
		
		assertTrue(n1.hashCode() == n1hashcode);
		assertTrue(n2.hashCode() == n2hashcode);
		
		SuperMario.isInGoal(n1, test_track);
		
		assertTrue(n1.hashCode() == n1hashcode);
		assertTrue(n2.hashCode() == n2hashcode);
		
		HashMap<Node, LinkedList<Node>> g = SuperMario.graphConstructor(test_track, 0, 1);
		
		assertTrue(n1.hashCode() == n1hashcode);
		assertTrue(n2.hashCode() == n2hashcode);
		
//		SuperMario.BFS(g, n2, test_track);
		
		g.get(new Node(0,1,0,0));
		
		assertTrue(n1.hashCode() == n1hashcode);
		assertTrue(n2.hashCode() == n2hashcode);
		
		
	}
	@Test
	public void testNodeEquals() {
		Node n1 = new Node(1,1,1,0);
		Node n2 = new Node(1,1,1,0);
		
		assertTrue(n1.equals(n2));
		
		
	}
	
	@Test
	public void testNodeHashCode() { 
		Node n1 = new Node(1,1,1,0);
		Node n2 = new Node(1,1,1,0);
		Node n4 = new Node(1,1,1,0);
		
		Node n5 = new Node(1,1,1,-1);
		Node n6 = new Node(1,1,1,-1);
		
		assertTrue(n4.hashCode()==n1.hashCode());
		assertTrue(n1.hashCode()==n2.hashCode());
		assertTrue(n4.hashCode()==n1.hashCode());
		
		assertTrue(n5.hashCode() == n6.hashCode());
	}
	
	@Test
	public void hashMapNodeTest() {
		HashMap<Node, LinkedList<Node>> g = new HashMap<Node, LinkedList<Node>>();
		
		Node n1 = new Node(1,1,1,0);
		Node n2 = new Node(1,1,1,0);
		
		g.put(n1, null);
		
		assertTrue(g.containsKey(n1));
		
		n1.hashCode();
		
		assertTrue(g.containsKey(n1));
		
		n1.equals(n2);
		
		assertTrue(g.containsKey(n1));
	}

}
