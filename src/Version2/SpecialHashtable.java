package Version2;

import common.Employee;
import DataStructures.AdvancedIterator;

import java.util.Iterator;

/**
 * Created by Jacky on 2019-01-14.
 */


public class SpecialHashtable extends Item {
    
    /*
     * (Note: SpecialHashtable will be referred to as SH for short, and TreeContainer as TC.)
     *
     * This is a unique data structure tailored for this program, but it does have similarities with a hash table. An SH
     * is essentially a HashSet with levels (bins can contain not only containers of Employees, TCs, but also more SHs.)
     *
     * The type field below identifies which piece of information of an Employee this DS is associated with (either
     * their idNum or their name).
     * In the case of the former, each array has a length of 10, each index corresponding to a different digit.
     * In the case of the latter, each array has a length of 26, each index corresponding to a different letter.
     *
     * The id/name (field) of an Employee itself determines which index of pointers it should go in. Until it is put
     * into an empty bin or a mini-tree (explained below), the code will traverse through each character in the field
     * sequentially. A collision (the bin isn't empty) will result in either a binary search tree being formed or, if a
     * tree already exists and is large enough, the creation of a SH in that bin containing all Employees in that TC
     * tree as well as the new Employee (replaces the existing tree).
     * (A HashSet treats collisions in a bin by forming small red-black trees with a size limit with the objects that
     * have been hashed there before distributing those objects.)
     * The benefit? Prevents large amounts of memory being taken up by SHs containing very few items.
     *
     * Example: (type = true)
     * add(Employee(id: "712-712-712")) -> Check index 7. If nothing is there, place the new Employee there. Done.
     *                                   If there's a TC, add the new Employee to the tree. If large, create SH. Done.
     *                                   If there's a SH, go into that SH and place in index 1 (next character). Repeat.
     *
     * The remove method uses each character of the appropriate field of the Employee (sequentially) as indices, like
     * the add() method above, to search for a given Employee. If a SH should be reach a small enough size to treeify,
     * a resize operation converting all Items in the SH to a tree of TCs.
     */
    
    /*----- Fields -----*/
    
    private final Item[] pointers;
    private final boolean type; // true: nums; false: names
    int filled = 0;
    
    
    /*----- Constructor -----*/
    
    public SpecialHashtable(boolean forNum) {
        type = forNum;
        
        pointers = new Item[forNum ? 10 : 26];
        size = 0;
    }
    
    
    /*----- Methods -----*/
    
    /**
     * Returns true if given Employee didn't already exist in the data structure (successful add).
     */
    protected boolean insert(Employee employee) {
        return insert(employee, 0);
    }
    
    private boolean insert(Employee employee, int characterIndex) {
        String token = type ? employee.getPrintId().replaceAll("-", "") + "" : employee.getRawName();
        
        int index = token.charAt(characterIndex) - (type ? 48 : 97); // line up '0' or 'a' with index 0
        Item current = pointers[index];
        if (current == null) {
            pointers[index] = new TreeContainer(employee, type);
            filled++;
        } else if (current instanceof TreeContainer) {
            if (!current.insert(employee)) return false;
            if (current.size >= (type ? 4 : 9)) { // the threshold for expanding the tree
                Employee[] employees = current.toArray();
                SpecialHashtable newTable = new SpecialHashtable(type);
                for (Employee e : employees) newTable.insert(e, characterIndex + 1);
                pointers[index] = newTable;
            }
        } else if (pointers[index] instanceof SpecialHashtable) {
            if (!((SpecialHashtable) current).insert(employee, characterIndex + 1)) return false;
        }
        size++;
        return true;
    }
    
    
    /**
     * Returns true if the specified Employee(s) was/were found and removed.
     */
    protected Employee remove(Employee employee) {
        return remove(employee, 0);
    }
    
    protected void removeAll(SpecialHashtable employees) {
        for (Employee e : employees.toArray()) if (remove(e) == null)
            throw new IllegalStateException("Inconsistency! Employee existed in Categories but not " +
                    (type ? "Nums" : "Names") + ".\n" + e); // should be impossible anyways, but just in case
    }
    
    private Employee remove(Employee employee, int characterIndex) {
        String token = type ? employee.getPrintId().replaceAll("-", "") + "" : employee.getRawName();
        int index = token.charAt(characterIndex) - (type ? 48 : 97);
        Employee removed = null;
        Item current = pointers[index];
        if (current instanceof TreeContainer) {
            if (((TreeContainer) current).compareTo(employee) == 0) { // remove root node of this tree
                removed = ((TreeContainer) current).employee;
                if (current.size == 1) {
                    pointers[index] = null;
                    filled--;
                } else { // adjusting size is built into add/remove methods of TreeContainer
                    if (((TreeContainer) current).left == null) pointers[index] = ((TreeContainer) current).right;
                    else {
                        if (((TreeContainer) current).right != null)
                            ((TreeContainer) current).left.insert(((TreeContainer) current).right);
                        pointers[index] = ((TreeContainer) current).left;
                    }
                }
            } else removed = current.remove(employee);
        } else if (current instanceof SpecialHashtable) {
            if ((removed = ((SpecialHashtable) current).remove(employee, characterIndex + 1)) != null) {
                if (current.size <= (type ? 2 : 6)) // reclaim memory if SH is small enough (treeify the Items)
                    if (((SpecialHashtable) current).filled < 2) {
                        for (int i = 0; i < (type ? 10 : 26); i++)
                            if (((SpecialHashtable) current).pointers[i] != null)
                                pointers[index] = ((SpecialHashtable) current).pointers[i];
                    } else {
                        TreeContainer newTree = null;
                        Employee[] employees = current.toArray();
                        int i = employees.length / 2;
                        do {
                            if (newTree == null) newTree = new TreeContainer(employees[i], type);
                            else newTree.insert(employees[i]);
                            i = (i + 1) % employees.length;
                        } while (i != employees.length / 2);
                        pointers[index] = newTree;
                    }
            }
        }
        if (removed != null) size--;
        return removed;
    }
    
    
    /**
     * Prints each Employee and all of its information on a line of its own, sorted.
     */
    protected void print() {
        printRecur(this);
    }
    
    private void printRecur(SpecialHashtable current) {
        for (Item i : current.pointers) if (i != null) i.print();
    }
    
    
    /**
     * Prints the data structure in a more visual way.
     */
    protected void printDebug() {
        printDebugRecur("", "Start");
    }
    
    private void printDebugRecur(String spaces, String id) {
        System.out.print(spaces + id + " (" + size + "): [");
        for (int i = 0; i < (type ? 10 : 26); i++) {
            Item item = pointers[i];
            if (item == null) System.out.print("X");
            else if (item instanceof TreeContainer) {
                if (item.size == 1)
                    System.out.print(type
                            ? ((TreeContainer) item).employee.getPrintId().replaceAll("-", "")
                            : ((TreeContainer) item).employee.getRawName());
                else System.out.print("<.>");
            }
            else System.out.print("[...]");
            if (i != (type ? 9 : 25)) System.out.print(", ");
        }
        System.out.println("]");
        for (int i = 0; i < (type ? 10 : 26); i++) {
            Item item = pointers[i];
            if (item == null);
            else {
                char newId = (char) (type ? i + 48 : ('a' + i));
                if (item instanceof TreeContainer && item.size > 1) {
                    System.out.print(spaces + "  " + newId + ": ");
                    item.printDebug();
                    System.out.println();
                }
                else if (item instanceof SpecialHashtable)
                    ((SpecialHashtable) item).printDebugRecur(spaces + "  ",
                            "" + newId);
            }
        }
    }
    
    
    /**
     * Returns an array containing all the Employees in the SH.
     */
    public Employee[] toArray() {
        Employee[] employees = new Employee[size];
        insertToArray(employees,0);
        if (employees[size - 1] == null) {
            printDebug();
        }
        return employees;
    }
    
    private void insertToArray(Employee[] employees, int index) {
        for (Item i : pointers) {
            if (i == null);
            else if (i instanceof TreeContainer) for (Employee e : i.toArray()) employees[index++] = e;
            else {
                ((SpecialHashtable) i).insertToArray(employees, index);
                index += i.size;
            }
        }
    }
    
    
    
    /*
     * The TreeContainer class acts as a node of a binary search tree. As mentioned above, it is used as an alternative
     * to creating a whole new SH to store only a few Employees to save memory, with almost no cost to performance. It
     * is not a re-balancing tree only because the cost of re-balancing might outweigh the benefits of its purpose,
     * since the threshold for resizing is very small (3 for nums, 8 for names).
     *
     * Each TC contains a pointer to an Employee as well as a type field inherited from the outer class that tells it
     * how to compare its Employee with one to add/remove.
     */
    private class TreeContainer extends Item {
        
        private final boolean type; // true: nums; false: names
        private final Employee employee;
        private TreeContainer left = null, right = null;
        
        private TreeContainer(Employee e, boolean type) {
            employee = e;
            this.type = type;
            size = 1;
        }
        
        
        /**
         * Returns true if the given Employee didn't already exist in the tree (successful add).
         */
        protected boolean insert(Employee e) {
            return insert(new TreeContainer(e, type));
        }
        
        private boolean insert(TreeContainer t) {
            if (compareTo(t.employee) > 0) {
                if (left == null) {
                    left = t;
                    size += t.size;
                    return true;
                } else {
                    boolean successful = left.insert(t);
                    if (successful) size += t.size;
                    return successful;
                }
            } else if (compareTo(t.employee) < 0) {
                if (right == null) {
                    right = t;
                    size += t.size;
                    return true;
                } else {
                    boolean successful = right.insert(t);
                    if (successful) size += t.size;
                    return successful;
                }
            }
            return false; // current container has the same employee as the new one
        }
        
        
        /**
         * Returns true if the specified Employee was found/removed.
         */
        protected Employee remove(Employee e) {
            Employee removed = null;
            if (compareTo(e) > 0) {
                if (left == null);
                else if (left.compareTo(e) == 0) { // remove the left
                    removed = left.employee;
                    if (left.left == null) left = left.right;
                    else {
                        if (left.right != null) {
                            TreeContainer current = left.left; // relocate the right children of the node to remove
                            while (current.right != null) {
                                current = current.right;
                                current.size += left.right.size;
                            }
                            current.right = left.right;
                        }
                        left = left.left;
                        left.size = (left.left == null ? 0 : left.left.size) +
                                (left.right == null ? 0 : left.right.size) + 1;
                    }
                } else {
                    removed = left.remove(e); // remove from left
                }
            } else if (compareTo(e) < 0) {
                if (right == null);
                else if (right.compareTo(e) == 0) { // remove the right
                    removed = right.employee;
                    if (right.right == null) right = right.left;
                    else {
                        if (right.left != null) {
                            TreeContainer current = right.right; // relocate the left children of the node to remove
                            while (current.left != null) {
                                current.size += right.left.size;
                                current = current.left;
                            }
                            current.left = right.left;
                        }
                        right = right.right;
                        right.size = (right.right == null ? 0 : right.right.size) +
                                (right.left == null ? 0 : right.left.size) + 1;
                    }
                } else {
                    removed = right.remove(e); // remove from right
                }
            }
            return removed;
        }
        
        
        /**
         * Compares the contained Employee with the given one using the appropriate compareTo method.
         */
        private int compareTo(Employee e) { // true: num; false: name
            return type ? employee.getPrintId().replaceAll("-", "").compareTo(
                    e.getPrintId().replaceAll("-", "")) : employee.compareTo(e);
        }
        
        
        /**
         * Prints each Employee and all of its information on a line of its own, sorted. For spaces, use the lines for
         * debugging below.
         */
        protected void print() {
            print(0);
        }
        
        private void print(int spaces) {
            if (left != null) left.print(spaces + 1);
            
            // also for debugging
//            for (int i = 0; i < spaces; i++) System.out.print("    ");
//            System.out.print("(" + size + ") ");
            
            System.out.println(employee.toString());
            if (right != null) right.print(spaces + 1);
        }
        
        
        /**
         * Prints the data structure in a more visual way.
         */
        protected void printDebug() {
            System.out.print("(");
            if (left != null) {
                left.printDebug();
                System.out.print(" < ");
            }
            System.out.print(type ? employee.getPrintId().replaceAll("-", "") : employee.getRawName());
            if (right != null) {
                System.out.print(" > ");
                right.printDebug();
            }
            System.out.print(")");
        }
        
        
        /**
         * Returns an array containing all the Employees in the tree and distributes it into a new SH. New TCs will be
         * created in the process, so the old TCs will be removed from memory.
         */
        protected Employee[] toArray() {
            Employee[] employees = new Employee[size];
            insertToArray(this, employees, 0);
            if (employees[size - 1] == null) {
                throw new IllegalStateException("Array not completed in toArray() call by " + employee.getRawName());
            }
            return employees;
        }
        
        private int insertToArray(TreeContainer e, Employee[] array, int index) {
            if (e.left != null) index = insertToArray(e.left, array, index);
            array[index] = e.employee;
            index++;
            if (e.right != null) index = insertToArray(e.right, array, index);
            return index;
        }
    }
    
    
    /**
     * Returns an iterator (not fail-safe nor fail-fast) of an array that contains all Employees, sorted appropriately.
     */
    public Iterator iterator() {
        return new SHIterator();
    }
    
    private class SHIterator implements AdvancedIterator {
        protected final Employee[] employees;
        private int current = 0;
        private int removeOK = 0;
        
        public SHIterator() {
            employees = SpecialHashtable.this.toArray();
        }
        
        @Override
        public boolean hasNext() {
            return current < employees.length;
        }
        
        @Override
        public Object next() {
            if (hasNext()) {
                removeOK = 1;
                return employees[current++];
            } else {
                throw new IllegalStateException("End of the line!");
            }
        }
        
        @Override
        public boolean hasPrevious() {
            return current > 0;
        }
        
        @Override
        public Object previous() {
            if (hasPrevious()) {
                removeOK = 2;
                return employees[--current];
            } else {
                throw new IllegalStateException("End of the line!");
            }
        }
        
        @Override
        public void remove() {
            if (removeOK == 0) {
                throw new IllegalStateException("Did not call previous() or next()");
            } else {
                current -= removeOK % 2;
                SpecialHashtable.this.remove(employees[current]);
                
                if (employees.length - current >= 0)
                    System.arraycopy(employees, current + 1,
                            employees, current, employees.length - current);
                
                removeOK = 0;
            }
        }
    }
    
}
