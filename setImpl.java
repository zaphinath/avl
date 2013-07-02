package cs235.avl;

import java.util.Iterator;
import java.util.Stack;
import java.util.Collection;
import java.util.NoSuchElementException;

public class setImpl implements java.util.Set,Tree {
	private Node root = nullNode;
	private int theSize = 0;
	
	private static Node deletedNode;
	private static Node lastNode;

	private static Node nullNode;
	static {
		nullNode = new Node( "NULLNODE" );
		nullNode.left = nullNode;
    nullNode.right = nullNode;
		nullNode.level = -1;
	}
	public BinaryTreeNode getRootNode() {
    return root;
  }
  public Node printTree( Node t )
  {
    if( t != nullNode) {
     // System.out.println( t.element );
      if (t.left != nullNode)
        printTree( t.left );
      if (t.right != nullNode)
        printTree( t.right );
      }
    return t;
  }
	public boolean add(Object o) {
		Comparable t = (Comparable)o;
    int oldSize = size(); 
		if (!contains(o)) {
    //  System.out.println("foo" + theSize + " " + preOrder(root));
      root = insert(t, root);
      theSize++;
     // return true;
    }
   // else { return false;}
		return size() != oldSize;
	}

	public Node insert(Comparable x, Node t) {
    if( t == nullNode ) {
      t = new Node(x);
    }
    else if( x.compareTo( t.element ) < 0 ) {
      t.left = insert( x, t.left );
      if( height( t.left ) - height( t.right ) >= 2 )
       t = (x.compareTo(t.left.element) < 0) ? rotateRight(t) : doubleRotateRight(t);
    }
    else if( x.compareTo( t.element ) > 0 ) {
      t.right = insert( x, t.right );
      if( height( t.right ) - height( t.left ) >= 2 )
        t = ( x.compareTo( t.right.element ) > 0 ) ? rotateLeft(t) : doubleRotateLeft(t);
    }
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
	}

	public void clear() {
		theSize = 0;
		root = nullNode;
	}

	public boolean contains(Object o) {
    Comparable s = (Comparable)o;
    return contains( s, root );
	}

    public boolean contains( Comparable x, Node t )
    {
        if( t == nullNode )
            return false;
			
		int compareResult = x.compareTo( t.element );
			
        if( compareResult < 0 )
            return contains( x, t.left );
        else if( compareResult > 0 )
            return contains( x, t.right );
        else
            return true;  
    }
	

    public Node preOrder(Node t) {
      if (t != nullNode) {
       // System.out.println(t.element);
        if (t.left !=nullNode) {
          //System.out.println(t.left.element);
          preOrder(t.left);
        }
        if (t.right != nullNode) {
          //System.out.println(t.right.element);
          preOrder(t.right);
        }
      } 
      return t;
    }
  private Node find( Comparable x, Node t ) {
    if (x.equals(t)) {
      System.out.println(t.element);
      return t;
    }
    else {
    if ( t != nullNode ) {
      if( x.compareTo( t.element ) < 0 )
        find(x,t.left);
       //t = t.left;
      else if( x.compareTo( t.element ) > 0 )
        find(x,t.right); 
       // t = t.right;
    }
    }
    if (!x.equals(t))
      return nullNode;   // No match
    else 
    return t;
  }

	public boolean isEmpty() {
		return root == nullNode;
	}

	public Iterator iterator() {
		return new TreeSetIterator(root);
	}

  public Node findMin(Node t) {
    if (t.left == nullNode)
      return t;
    return findMin(t.left);
  }

	public boolean remove(Object o) {
		if (contains(o)) {
    Comparable x = (Comparable)o;
    root = remove(x, root);
      theSize--;
    return true;
    }
    else
      return false;
	}

	public Node remove(Comparable x, Node t) {
    if( x.compareTo( t.element ) < 0 ) {
      t.left = remove( x, t.left );
    }
    else if( x.compareTo( t.element ) > 0 ) {
      t.right = remove( x, t.right );
    }
    //else if ( t.left == nullNode && t.right == nullNode)
    else if (foo1(t))
      return nullNode;
  //  else if( t.left != nullNode && t.right != nullNode) {
    else if(foo2(t)) {
      t.element = findMin(t.right).element;
      t.right = remove(t.element , t.right);
    }
    else {
      t = (t.left != nullNode) ? t.left : t.right;
    }
    t.level = max( height( t.left ), height( t.right ) ) + 1;

    if( height( t.left ) - height( t.right ) >= 2 )
      t = (t.left.left.level >= t.left.right.level) ? rotateRight(t) : doubleRotateRight(t);
    if( height( t.right ) - height( t.left ) >= 2 )
      t = (t.right.right.level >= t.right.left.level) ? rotateLeft(t) : doubleRotateLeft(t);
    return t;
	}
  public boolean foo1(Node t) {
    if ( t.left == nullNode && t.right == nullNode)
      return true;
    return false;
  }
  public boolean foo2(Node t) {
    if( t.left != nullNode && t.right != nullNode) 
      return true;
    return false;
  } 

	public int size() {
		return theSize;
	}

	public Object[] toArray() {
		Object[] foo = new Object[theSize];
    Iterator fidler = new TreeSetIterator(root);
    for (int i = 0; i < theSize; i++) {
      foo[i] = fidler.next();
    }
    return foo;
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
	
	private static final class Node implements BinaryTreeNode{
		private Node(Comparable x) {
			element = x;
			left = nullNode;
      right = nullNode;
			level = 0;
		}
		private Comparable element;
		private int level;
		private Node left;
		private Node right;

    public Object getData() {
      return this.element;
    }
    public BinaryTreeNode getLeftChild() {
    if (this.left != nullNode)
      return this.left;
    else 
      return null;
    }
    public BinaryTreeNode getRightChild() {
    if (this.right != nullNode)
      return this.right;
    else
      return null;
    }
    public int getHeight() {
      return this.level;
    }
  }

	private class TreeSetIterator implements Iterator {
		private Stack<Node> fries = new Stack<Node>();

    public Node preOrderPush(Node t) {
      if (t != nullNode) {
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
      if (t != nullNode) {
        fries.push(t);
      }
    }

    public boolean hasNext() {
      return !fries.empty();
    }

    public Object next() {
			if (!hasNext())
				throw new NoSuchElementException();
      Node pop = fries.pop();
      if (pop.right != nullNode) {
        fries.push(pop.right);
      }
      if (pop.left != nullNode) {
        fries.push(pop.left);
      }
      return (Object)pop.element;
    }  
    
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}

