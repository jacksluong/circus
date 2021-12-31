package DataStructures.Trees;

import DataStructures.AdvancedIterator;

/**
 * Created by Jacks on 5/26/18.
 * A binary search tree data structure.
 */

@SuppressWarnings("ConstantConditions")
public class BinarySearchTree<E extends Comparable<E>> implements Tree<E> {

    protected java.util.Comparator<E> c;
    protected Node<E> root;

    private int size = 0;

    public BinarySearchTree() {
        c = null;
        root = null;
    }

    @SuppressWarnings("unchecked")
    public BinarySearchTree(java.util.Comparator<E> c) {
        this((E[]) new Object[0], c);
    }

    public BinarySearchTree(E[] es) {
        this(es, null);
    }

    public BinarySearchTree(E[] es, java.util.Comparator<E> c) {
        this.c = c;
        for (E e : es)
            insert(e);
    }

    public BinarySearchTree(java.util.Collection<E> es) {
        this(es, null);
    }

    public BinarySearchTree(java.util.Collection<E> es, java.util.Comparator<E> c) {
        this.c = c;
        for (E e : es)
            insert(e);
    }


    /**
     * Adds an element into the tree if it doesn't already exist and return true (if it does, return false).
     */
    public boolean insert(E e) {
        if (root == null) { // tree is empty
            root = node(e);
            size++;
            return true;
        }

        // locate parent node
        Node<E> parent = null, current = root;
        while (current != null) {
            if (c == null ? e.compareTo(current.e) < 0 : c.compare(e, current.e) < 0) {
                parent = current;
                current = current.left;
            } else if (c == null ? e.compareTo(current.e) > 0 : c.compare(e, current.e) > 0) {
                parent = current;
                current = current.right;
            } else { // e = current.e, element already exists in the tree
                return false;
            }
        }

        // insert node
        if (c == null ? e.compareTo(parent.e) < 0 : c.compare(e, parent.e) < 0)
            parent.left = node(e);
        else
            parent.right = node(e);
        size++;
        return true;
    }
    // Adds all the elements from a(n) collection/array into the tree that don't already exist in it
    public void insertAll(java.util.Collection<E> es) { for (E e : es) insert(e); }
    public void insertAll(E[] es) { for (E e : es) insert(e); }
    public void insertAll(BinarySearchTree<E> es) { for (E e : es) insert(e); }

    protected Node<E> node(E e) { return new Node<>(e); } // creates a new node


    /**
     * Removes the specified element from the tree if it exists and return true (if it doesn't, return false).
     */
    public boolean remove(E e) {
        if (root == null) return false;

        // locate node containing element and parent of it
        Node<E> parent = null, current = root;
        while (current != null && (c == null ? e.compareTo(current.e) != 0 : c.compare(e, current.e) != 0)) {
            if (c == null ? e.compareTo(current.e) < 0 : c.compare(e, current.e) < 0) {
                parent = current;
                current = current.left;
            } else {
                parent = current;
                current = current.right;
            }
        }

        if (current == null) return false; // not found

        if (current.left == null) { // case 1: no left child
            if (parent == null) { // current is root
                root = current.right;
                size--;
                return true;
            } else if (parent.left == current)
                parent.left = current.right;
            else
                parent.right = current.right;
        } else {
            // locate rightmost node of current's left subtree and its parent
            Node<E> rightMost = current.left, pOfRightMost = current;
            while (rightMost.right != null) {
                pOfRightMost = rightMost;
                rightMost = rightMost.right;
            }

            // replace current with rightmost node's content and remove rightmost node from tree
            current.e = rightMost.e;
            if (pOfRightMost != current) pOfRightMost.right = rightMost.left;
            else pOfRightMost.left = rightMost.left;
        }

        size--;
        return true;
    }
    // Removes all the elements from a(n) collection/array in the tree if they exist
    public void removeAll(java.util.Collection<E> es) { for (E e : es) remove(e); }
    public void removeAll(E[] es) { for (E e : es) remove(e); }
    public void removeAll(BinarySearchTree<E> es) { for (E e : es) remove(e); }


    /**
     * Returns the path to a target element if it is found, otherwise null
     */
    protected Path search(E target) {
        if (root == null) return new Path(null);

        // locate target element
        Path p = new Path(root);
        Node<E> current = root;
        while (current != null && (c == null ? target.compareTo(current.e) != 0 : c.compare(target, current.e) != 0)) {
            if (c == null ? target.compareTo(current.e) < 0 : c.compare(target, current.e) < 0) {
                current = current.left;
            } else if (c == null ? target.compareTo(current.e) > 0 : c.compare(target, current.e) > 0) {
                current = current.right;
            }
            p = new Path(current, p);
        }

        // if not found, p.n should be null
        return p;
    }


    /**
     * Returns whether an element exists in the tree or not.
     */
    public boolean contains(E e) {
        return search(e).n != null;
    }


    /**
     * Returns whether the tree is empty or not.
     */
    public boolean isEmpty() {
        return size == 0;
    }


    /**
     * Returns the number of nodes in the tree.
     */
    public int size() {
        return size;
    }


    /**
     * Removes all elements from the tree.
     */
    public void clear() {
        size = 0;
        root = null;
    }


    /**
     * Returns all elements in the tree contained in an array ordered inorder.
     */
    @SuppressWarnings("unchecked")
    public E[] toArray() {
        if (root == null) return (E[]) new Object[0];
        E[] a = (E[]) new Comparable[size];
        toArray(root, a, 0);
        return a;
    }
    private int toArray(Node<E> n, E[] a, int current) {
        if (n == null) return current;
        current = toArray(n.left, a, current);
        a[current] = n.e;
        current = toArray(n.right, a, current + 1);
        return current; // return the next open index
    }


    /**
     * Returns an iterator for the tree.
     */
    public java.util.Iterator<E> iterator() {
        return new TreeIterator();
    }


    /**
     * Prints all the elements in the tree in an inorder traversal, a line for each element.
     */
    public void print() {
        if (root != null) print(root);
        else System.out.println("null");
    }
    protected void print(Node<E> a) { // helper method
        if (a.left != null) print(a.left);
        System.out.println(a.e);
        if (a.right != null) print(a.right);
    }
    public void printSideways() { // an alternate method that prints the tree sideways so levels can be seen
        System.out.println("Printing binary search tree: ");
        if (root != null) printSideways(root, 0);
        else System.out.println("null");
    }
    protected void printSideways(Node<E> a, int level) { // another helper method
        if (a.right != null) printSideways(a.right, level + 1);
        for (int i = 1; i <= level; i++) System.out.print("   ");
        System.out.println(a.e);
        if (a.left != null) printSideways(a.left, level + 1);
    }


    /**
     * Prints out all information about a node. Used for debugging.
     */
    protected void analyze(Node<E> n) {
        System.out.print("(left) " + n.left + "<- (node) " + n + " -> (right)" + n.right);
        System.out.println();
    }
    
    
    /**
     * Find the lowest level common parent node of two nodes (null if at least one node doesn't exist).
     */
    protected Node<E> commonParent(Node<E> a, Node<E> b) {
        Node<E> current = root, checkLeft = null, checkRight = null;
        int comparison;
        while (Math.abs((comparison = a.e.compareTo(current.e)) + b.e.compareTo(current.e)) ==
                Math.abs(a.e.compareTo(current.e)) + Math.abs(b.e.compareTo(current.e))) { // a and b are on same side
            if (comparison < 0)
                if (current.left == a || current.left == b) { // one node is a descendent of the other, both on left
                    checkRight = current.left;
                    break;
                } else
                    current = current.left;
            else
                if (current.right == a || current.right == b) { // one node is a descendent of the other, both on right
                    checkLeft = current.right;
                    break;
                } else
                    current = current.right;
            if (current == null) return null; // nodes don't exist in tree
        }
        if (checkLeft == null) checkLeft = current.left;
        if (checkRight == null) checkRight = current.right;
        Node<E> left, right;
        if (comparison < 0) { // where node a would lie
            left = a;
            right = b;
        } else {
            left = b;
            right = a;
        }
        while (checkLeft != left) { // verify the left node exists
            if (checkLeft == null) // left doesn't exist in tree
                return null;
            else if (left.e.compareTo(checkLeft.e) > 0)
                checkLeft = checkLeft.right;
            else
                checkLeft = checkLeft.left;
        }
        while (checkRight != right) { // verify the right node exists
            if (checkRight == null) // left doesn't exist in tree
                return null;
            else if (right.e.compareTo(checkRight.e) > 0)
                checkRight = checkLeft.right;
            else
                checkRight = checkLeft.left;
        }
        return current;
    }


    /**
     * The node class that is off-limits to the client.
     */
    protected static class Node<E> {

        protected E e;
        protected Node<E> left, right;

        protected Node() { this(null); }

        protected Node(E e) { this.e = e; }

        public E getE() {
            return e;
        }

        public Node<E> getLeft() {
            return left;
        }

        public Node<E> getRight() {
            return right;
        }

        @Override
        public String toString() { return e.toString(); }

    }


    /**
     *  The linked list class that is off-limits to the client but provides the path to a certain node.
     */
    protected class Path {

        protected final Path next;
        protected final Node<E> n;

        protected Path() {
            this(null, null);
        }

        protected Path(Node<E> n) {
            this(n, null);
        }

        public Path getNext() {
            return next;
        }

        public Node<E> getN() {
            return n;
        }

        protected Path(Node<E> n, Path next) {
            this.next = next;
            this.n = n;
        }

    }


    /**
     * The iterator for this BST class. Iterates through elements in an inorder manner.
     */
    private class TreeIterator implements AdvancedIterator<E> {

        private final E[] elements;
        private int current = 0;
        private int removeOK = 0;

        protected TreeIterator() {
            elements = toArray();
        }

        public boolean hasNext() { return current < size; }

        public E next() {
            if (hasNext()) {
                removeOK = 1;
                return elements[current++];
            } else {
                throw new IllegalStateException("End of the line!");
            }
        }

        public boolean hasPrevious() { return current > 0; }

        public E previous() {
            if (hasPrevious()) {
                removeOK = 2;
                return elements[--current];
            } else {
                throw new IllegalStateException("End of the line!");
            }
        }

        public void remove() {
            if (removeOK == 0) throw new IllegalStateException("Did not call previous() or next()");
            current -= removeOK % 2;
            BinarySearchTree.this.remove(elements[current]);
            int i;
            for (i = current; i < size; i++) // manually shift all elements over by 1
                elements[i] = elements[i + 1]; // because all the elements would remain in sorted order regardless
            removeOK = 0;
        }
    }
}
