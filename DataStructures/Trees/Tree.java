package DataStructures.Trees;

import java.util.Collection;

public interface Tree<E> extends Iterable<E> {

    // inserts a new element into the tree, returns true if it doesn't already exist
    boolean insert(E e);

    // inserts all elements from a collection into the tree
    void insertAll(Collection<E> es);

    // inserts all elements from an array into the tree
    void insertAll(E[] es);

    // removes an element from the tree, returns true if successful
    boolean remove(E e);

    // removes all elements from a collection that exists in the tree
    void removeAll(Collection<E> es);

    // removes all elements from an array that exists in the tree
    void removeAll(E[] es);

    // returns whether an element is in the tree or not
    boolean contains(E e);

    // returns whether a tree is empty or not
    boolean isEmpty();

    // returns the number of nodes in the tree
    int size();

    // empties the tree
    void clear();

    // prints all elements, each on their own line
    void print();

    // prints all elements in a sideways tree
    void printSideways();

    // returns an array of all the elements in the tree
    E[] toArray();

}
