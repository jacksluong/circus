package DataStructures.Trees;

/**
 * Created by Jacks on 5/25/18.
 * A data structure made to be as similar to an orthodox AVL tree as possible
 * (with the addition of a reference in each node to its parent to make life a lot easier).
 */

public class AVLTree<E extends Comparable<E>> extends BinarySearchTree<E> {

    public AVLTree() {
        c = null;
        root = null;
    }

    @SuppressWarnings("unchecked")
    public AVLTree(java.util.Comparator<E> c) { this((E[]) new Object[0], c); }

    public AVLTree(E[] es) {
        this(es, null);
    }

    public AVLTree(E[] es, java.util.Comparator<E> c) {
        this.c = c;
        for (E e : es) if(insert(e)) rebalance(search(e));
    }

    public AVLTree(java.util.Collection<E> es) {
        this(es, null);
    }

    public AVLTree(java.util.Collection<E> es, java.util.Comparator<E> c) {
        this.c = c;
        for (E e : es) if(insert(e)) rebalance(search(e));
    }


    /**
     * Same as BST, except it rebalances if necessary.
     */
    public boolean insert(E e) {
        boolean result = super.insert(e);
        if (result) // added
            rebalance(search(e));
        return result;
    }

    protected AVLNode<E> node(E e) { return new AVLNode<>(e); } // creates a new node


    /**
     * Same as BST, except it rebalances if necessary.
     */
    public boolean remove(E e) { // easiest remove method for the client to use
        boolean result = super.remove(e);
        if (result) // removed
            rebalance(search(e)); // super.search(e) will return the path to the parent of what used to be e
        return result;
    }


    /**
     * Rebalances the tree along the path of the newly added node by updating heights and moving nodes around.
     */
    private void rebalance(Path p) {
        // note: first p is the node that is added or
        if (p.n == null) p = p.next; // rebalancing for a path after removal of an element
        for (Path current = p; current != null; current = current.next) {
            Path parent = current.next;
            AVLNode<E> n = (AVLNode<E>) current.n;
            updateHeight(n);
            switch (balanceFactor(n)) {
                case -2:
                    if (balanceFactor((AVLNode<E>) n.left) <= 0)
                        fixLL(parent, n);
                    else
                        fixLR(parent, n);
                    break;
                case 2:
                    if (balanceFactor((AVLNode<E>) n.right) >= 0)
                        fixRR(parent, n);
                    else
                        fixRL(parent, n);
                    break;
            }
        }
    }
    private void fixLL(Path parent, AVLNode<E> a) { // visually, it all worked out, but in code, it looks like a mess
        AVLNode<E> b = (AVLNode<E>) a.left;
        if (parent == null)
            root = b;
        else if (parent.n.left == a)
            parent.n.left = b;
        else
            parent.n.right = b;

        a.left = b.right;
        b.right = a;

        updateHeight(a);
        updateHeight(b);
    }
    private void fixRR(Path parent, AVLNode<E> a) {
        AVLNode<E> b = (AVLNode<E>) a.right;
        if (parent == null)
            root = b;
        else if (parent.n.left == a)
            parent.n.left = b;
        else
            parent.n.right = b;

        a.right = b.left;
        b.left = a;

        updateHeight(a);
        updateHeight(b);
    }
    private void fixLR(Path parent, AVLNode<E> a) {
        AVLNode<E> b = (AVLNode<E>) a.left, c = (AVLNode<E>) b.right;
        if (parent == null)
            root = c;
        else if (parent.n.left == a)
            parent.n.left = c;
        else
            parent.n.right = c;

        b.right = c.left;
        c.left = b;
        a.left = c.right;
        c.right = a;

        updateHeight(a);
        updateHeight(b);
        updateHeight(c);
    }
    private void fixRL(Path parent, AVLNode<E> a) {
        AVLNode<E> b = (AVLNode<E>) a.right, c = (AVLNode<E>) b.left;
        if (parent == null)
            root = c;
        else if (parent.n.left == a)
            parent.n.left = c;
        else
            parent.n.right = c;

        b.left = c.right;
        c.right = b;
        a.right = c.left;
        c.left = a;

        updateHeight(a);
        updateHeight(b);
        updateHeight(c);
    }


    /**
     *  Updates the height of a node and its two children.
     */
    private void updateHeight(AVLNode<E> node) {
        if (node.left == null) {
            if (node.right != null)
                node.height = ((AVLNode<E>) node.right).height + 1;
            else
                node.height = 1;
        } else {
            if (node.right != null)
                node.height = Math.max(((AVLNode<E>) node.left).height, ((AVLNode<E>) node.right).height) + 1;
            else
                node.height = ((AVLNode<E>) node.left).height + 1;
        }
    }


    /**
     * Same as BST, except also prints the extra information about nodes in AVL trees.
     */
    @Override
    protected void analyze(Node<E> n) {
        super.analyze(n);
        System.out.println("height: " + ((AVLNode<E>) n).height + ", balanceFactor: " + balanceFactor((AVLNode<E>) n));
    }


    /**
     * Same as BST, except with different starting message.
     */
    public void printSideways() {
        System.out.println("Printing AVL tree: ");
        if (root != null) printSideways(root, 0);
        else System.out.println("null");
    }

    /**
     * Returns the difference in height of a node's two subtrees.
     */
    private int balanceFactor(AVLNode<E> node) {
        if (node.left == null) {
            if (node.right != null)
                return ((AVLNode<E>) node.right).height;
            else
                return 0;
        } else {
            if (node.right != null)
                return ((AVLNode<E>) node.right).height - ((AVLNode<E>) node.left).height;
            else
                return -((AVLNode<E>) node.left).height;
        }
    }


    /**
     * The AVL node class that includes an extra field: height.
     */
    protected static class AVLNode<E extends Comparable<E>> extends BinarySearchTree.Node<E> {

        protected int height = 1; // height from the bottom up

        protected AVLNode(E e) {
            super(e);
        }

    }

}
