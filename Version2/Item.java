package Version2;

import common.Employee;

/**
 * Created by Jacks on 2019-01-18.
 */

public abstract class Item {
    
    /*
     * The only purpose of this interface is to make life easier. If it weren't for the Item abstract class, the array contained
     * in each SH is would be of type Object[]. This would result in requiring a cast each time an element is accessed (in order
     * to use that element, whether it be an SH or a TC), and while it doesn't affect the performance of the program or data
     * structures, it does make the code look cleaner and more legible.
     *
     * The names of the methods should be simple enough that their purposes are self-explanatory.
     *
     * Classes that extend this abstract class are the SpecialHashtable class and the TreeContainer class.
     */
    
    protected int size; // for SH: number of non-null elements in the array; for TC: number of nodes in the tree, root included
    
    protected abstract boolean insert(Employee e);
    
    protected abstract Employee remove(Employee e);
    
    protected abstract void print();
    
    protected abstract void printDebug();
    
    protected abstract Employee[] toArray();
    
}

