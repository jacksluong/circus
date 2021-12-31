package DataStructures;

/**
 * An iterator that is capable of two-way iteration.
 */

public interface AdvancedIterator<E> extends java.util.Iterator<E> {

    public boolean hasPrevious();

    public E previous();

}
