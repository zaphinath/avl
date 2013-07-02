package cs235.avl;

import java.util.Iterator;
import java.util.Stack;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.lang.Comparable;

public class setImpl2 implements java.util.Set {
	private Node root = null;
	private int theSize = 0;
	
	private static Node deletedNode;
	private static Node lastNode;

	private static Node nullNode;
	static {
		nullNode = new Node( "NULLNODE" );
		nullNode.left = nullNode.right = nullNode;
		nullNode.level = -1;
	}
	
	public boolean add(Object o) {
		Comparable t = (Comparable)o;
    int oldSize = size(); 
		if (!contains(o)) {
      //System.out.println("foo" + theSize + " " + printTree(root));
      root = insert(t, root);
      theSize++;
     // return true;
    }
   // else { return false;}
		return size() != oldSize;
	}

	public Node insert(Comparable x, Node t) {
    /*if(t == nullNode) {
			t = new Node(x);
		//	theSize++;
		}
		else if (x.compareTo(t.element) < 0) {
			t.left = insert(x, t.left);
    }
		else if (x.compareTo(t.element) > 0) {
			t.right = insert(x, t.right);
    }
		else 
			return t;
		t = skew(t);
  	t = split(t);
		*/
    if( t == nullNode ) {
      t = new Node(x);
    }
    else if( x.compareTo( t.element ) < 0 ) {
      t.left = insert( x, t.left );
      if( height( t.left ) - height( t.right ) >= 2 )
        if( x.compareTo( t.left.element ) < 0 )
          t = rotateRight( t );
        else
          t = doubleRotateRight( t );
    }
    else if( x.compareTo( t.element ) > 0 ) {
      t.right = insert( x, t.right );
      if( height( t.right ) - height( t.left ) >= 2 )
        if( x.compareTo( t.right.element ) > 0 )
          t = rotateLeft( t );
        else
          t = doubleRotateLeft( t );
    }
    else
    ;  // Duplicate; do nothing
    t.level = max( height( t.left ), height( t.right ) ) + 1;
    return t;
	}
  
  private static int height( Node t )
  { 
    return t == nullNode ? -1 : t.level;
  }
  private static int max( int lhs, int rhs )
  {
    return lhs > rhs ? lhs : rhs;
  }
	
  public Node rotateRight(Node k2) {
    Node k1 = k2.left;
    k2.left = k1.right;
    k1.right = k2;
    k2.level = max( height( k2.left ), height( k2.right ) ) + 1;
    k1.level = max( height( k1.left ), k2.level ) + 1;
    return k1;
  }

  public Node rotateLeft(Node k1) {
    Node k2 = k1.right;
    k1.right = k2.left;
    k2.left = k1;
    k1.level = max( height( k1.left ), height( k1.right ) ) + 1;
    k2.level = max( height( k2.right ), k1.level ) + 1;
    return k2;
  }

	public Node doubleRotateRight(Node k3) {
		k3.left = rotateLeft(k3.left);
		return rotateRight(k3);
	}

	public Node doubleRotateLeft(Node k1) {
		k1.right = rotateRight(k1.right);
		return rotateLeft(k1);
	}/*
	public boolean add(Object o) {
		Comparable t = (Comparable)o;
		int oldSize = size(); 
		root = insert(t, root);
		return size() != oldSize;
	}

	public Node insert(Comparable f, Node t) {
		//Comparable f = (Comparable)x;
		if(t == nullNode) {
			t = new Node(f);
			theSize++;
		}
		else if (f.compareTo(t.element) < 0)
			t.left = insert(f, t.left);
		else if (f.compareTo(t.element) > 0)
			t.right = insert(f, t.right);
		else 
			return t;
		t = skew(t);
		t = split(t);
		
	/*	if (isBalanced(t)) 
			return t;
		else if(t.left.level > t.right.level) {
			rotateRight(t);
			return t;
		}
		else if(t.left.level < t.right.level) {
			rotateLeft(t);
			return t;
		}
		//else return t;
		return t;
	}
	
	public Node rotateRight(Node k2) {
		Node k1 = k2.left;
		k2.left = k1.right;
		k1.right = k2;
		return k1;
	}

	public Node rotateLeft(Node k2) {
		Node k1 = k2.right;
		k2.right = k1.left;
		k1.left = k2;
		return k1;
	}

	public Node doubleRotateRight(Node k3) {
		k3.left = rotateLeft(k3.left);
		return rotateRight(k3);
	}

	public Node doubleRotateLeft(Node k3) {
		k3.right = rotateLeft(k3.right);
		return rotateLeft(k3);
	}*/

	public void clear() {
		theSize = 0;
		root = nullNode;
	}

	public boolean contains(Object o) {
		return getMatch(o) != nullNode;
	}
	
	public Object getMatch(Object x) {
		Node p = find(x);
		if (p == nullNode)
			return nullNode;
		else
			return p.element;
	}

	private Node find(Object x) {
		Comparable a = (Comparable)x;
		Node current = root;
		nullNode.element = (Comparable)x;
		for( ; ; ) {
			if (a.compareTo(current.element) < 0)
				current = current.left;
			else if (a.compareTo(current.element) > 0)
				current = current.right;
			else if (current != nullNode)
				return current;
			else 
				return nullNode;
		}
	}
	
	public boolean isBalanced(Node a) {
		if ((a.left.level - a.right.level) >= 2)
			return false;
		else 
			return true;
	}

	public boolean isEmpty() {
		return true;
	}

	public Iterator iterator() {
		return new TreeSetIterator(root);
	}

	public Node skew(Node t) {
		if (t.left.level == t.level)
			t = rotateRight(t);
		return t;
	}
	public Node split(Node t) {
		if (t.right.right.level == t.level) {
			t = rotateLeft(t);
			t.level++;
		}
		return t;
	}

	public boolean remove(Object o) {
		int oldSize = size();
		deletedNode = nullNode;
		Comparable x = (Comparable)o;
		root = remove(x, root);
		return oldSize != size();
	}

	public Node remove(Comparable x, Node t) {
		if( t != nullNode) {
			lastNode = t;
			if (x.compareTo(t.element) < 0)
				t.left = remove(x, t.left);
			else {
				deletedNode = t;
				t.right = remove(x, t.right);
			}

			if (t == lastNode) {
				if (deletedNode == nullNode || x.compareTo(deletedNode.element) != 0)
					return t;
				deletedNode.element = t.element;
				t = t.right;
				theSize--;
			}
			else if (t.left.level < t.level - 1 || t.right.level < t.level - 1) {
				if (t.right.level > --t.level)
					t.right.level = t.level;
				t = skew(t);
				t.right = skew(t.right);
				t.right.right = skew(t.right.right);
				t = split(t);
				t.right = split(t.right);
			}
		}
		return t;
	}

	public int size() {
		return theSize;
	}

	public Object[] toArray() {
		return null;
	}
	
	public int hashCode() {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(Collection c) {
		throw new UnsupportedOperationException();
	}

	public boolean containsAll(Collection c) {
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(Collection c) {
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(Collection c) {
		throw new UnsupportedOperationException();
	}

	public Object[] toArray(Object[] array) {
		throw new UnsupportedOperationException();
	}
	
	private static class Node {
		private Node(Comparable x) {
			element = x;
			left = right = nullNode;
			level = 1;
		}
		private Comparable element;
		private int level;
		private Node left;
		private Node right;
	}

	private class TreeSetIterator implements Iterator {
		private Stack<Node> fries = new Stack<Node>();

    public Node preOrder(Node t) {
      if (t != nullNode) {
        System.out.println(t.element);
        if (t.left !=nullNode) {
          fries.push(t);
          preOrder(t.left);
        }
        if (t.right != nullNode) {
          fries.push(t);
          preOrder(t.right);
        }
      } 
      return t;
    }
    public TreeSetIterator(Node t) {
      //if (t != nullNode) {
      //  fries.push(t);
      preOrder(t);
    }

    public boolean hasNext() {
      return !fries.empty();
    }

    public Object next() {
		//	if (!hasNext())
			//	throw new NoSuchElementException();
      Node pop = fries.pop();
    /*  if (pop.right != nullNode) {
        fries.push(pop.right);
      }
      if (pop.left != nullNode) {
        fries.push(pop.left);
      }*/
      return (Object)pop;
    }  

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
/*
	private class TreeSetIterator implements Iterator {
		private int visited = 0;
		private Stack<Node> path = new Stack<Node>();
		private Node current = nullNode;
		private Node lastVisited = nullNode;
			
		public TreeSetIterator() {
		//	if (isEmpty())
		//		return;

			Node p = nullNode;
			for ( p = root; p.left != nullNode; p = p.left)
				path.push(p);
			current = p;
		}

		public boolean hasNext() {
			return !path.empty();//visited < size();
		}

		public Object next() {
			if (!hasNext())
				throw new NoSuchElementException();
			Object value = current.element;
			lastVisited = current;
			if (current.right != nullNode) {
				path.push(current);
				current = current.right;
				while (current.left != nullNode) {
					path.push(current);
					current = current.left;
				}
			}
			else {
				Node parent;
				for ( ; !path.isEmpty(); current = parent) {
					parent = (Node) path.pop();
					if (parent.left == current) {
						current = parent;
						break;
					}
				}
			}	
			visited++;
			return value;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}*/

