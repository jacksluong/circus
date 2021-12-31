package Version2;

import common.Employee;
import DataStructures.AdvancedIterator;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by Jacks on 2019-01-19.
 */

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class Categories {
    
    /*
     * Categories is basically a ArrayList<SpecialHashtables>, a set of SHs put together into one class with overarching
     * methods that can act on all of them together. The thing with these SHs is that each of them, alphabetically
     * sorted, contain the employees of only a single and unique category (hence their name).
     *
     * The following methods are made only for use in CircusV:
     * - size()
     * - get(int)
     * - allCategories()
     * - employeeCount()
     * - iterator()
     * - categoryIterator(String)
     */
    
    Category[] categories;
    private int numCats;
    
    public Categories() {
        categories = new Category[10];
        numCats = 0;
    }
    
    /**
     * Inserts an employee into the corresponding Category (SH). Creates a new Category if category doesn't exist yet.
     */
    public boolean insert(Employee e) {
        int i;
        for (i = 0; i < numCats; i++) if (categories[i].category.equalsIgnoreCase(e.getCategory())) break;
        if (i == numCats) {
            (categories[i] = new Category(e.getCategory())).insert(e);
            numCats++;
            
            // if array is getting full
            if (i >= categories.length * 0.8) categories = Arrays.copyOf(categories, categories.length * 2);
            return true;
        }
        return categories[i].insert(e);
    }
    
    
    /**
     * Removes an employee from its Category if it exists.
     */
    public void remove(Employee e) {
        int i;
        for (i = 0; i < numCats; i++) if (categories[i].category.equalsIgnoreCase(e.getCategory())) {
            categories[i].remove(e);
            return;
        }
    }
    
    
    /**
     * Returns the number of existing categories.
     */
    public int size() {
        return numCats;
    }
    
    
    /**
     * Returns a category name.
     */
    public String get(int index) {
        if (numCats == 0) return "";
        return categories[index < numCats ? index : numCats - 1].category;
    }
    
    
    /**
     * Adds a Category into the array if it doesn't already exist.
     */
    public void createCategory(String name) {
        int i;
        for (i = 0; i < numCats; i++) if (categories[i].category.equalsIgnoreCase(name)) {
            System.out.println("Category already exists.");
            return;
        }
        categories[i] = new Category(name);
        if (i >= categories.length * 0.8) categories = Arrays.copyOf(categories, categories.length * 2);
        numCats++;
    }
    
    
    /**
     * Removes the category and all its employees.
     */
    public Category deleteCategory(String cat) {
        for (int i = 0; i < numCats; i++) if (categories[i].category.equalsIgnoreCase(cat)) {
            Category catTree = categories[i];
            for (int j = i + 1; j < numCats; j++) categories[i] = categories[j];
            numCats--;
            return catTree;
        }
        // not found
        return null;
    }
    
    
    /**
     * Prints all Employees of a category alphabetically.
     */
    public void printCategory(String category) {
        for (int i = 0; i < numCats; i++) {
            if (i == 0) System.out.println("All " + category + "s, printed in alphabetical order.");
            if (categories[i].category.equals(category)) {
                categories[i].print();
                return;
            }
        }
        System.out.println(category + " is not an existing category.");
    }
    
    
    /**
     * Prints all Employees alphabetically, grouped by category.
     */
    public void printAll() {
        for (int i = 0; i < numCats; i++) categories[i].print();
    }
    
    
    /**
     * Returns an array containing all existing categories.
     */
    public String[] allCategories() {
        String[] cats = new String[numCats];
        for (int i = 0; i < numCats; i++) cats[i] = categories[i].category;
        return cats;
    }
    
    
    /**
     * Returns the number of employees under a given category.
     */
    public int employeeCount(String category) {
        for (int i = 0; i < numCats; i++) {
            if (categories[i].category.equals(category)) {
                return categories[i].size;
            }
        }
        return 0;
    }
    
    protected class Category extends SpecialHashtable {
        
        final String category;
        
        protected Category(String name) {
            super(false);
            category = name;
        }
    }
    
    
    /**
     * Returns an iterator (not fail-safe nor fail-fast) of an array that contains Employees of either all categories,
     * sorted categorically and alphabetically, or a single category, sorted alphabetically.
     */
    public Iterator iterator() {
        return new Categories.CategoriesIterator();
    }
    
    
    public Iterator categoryIterator(String cat) {
        for (int i = 0; i < numCats; i++) if (categories[i].category.equals(cat) && categories[i].size > 0) return categories[i].iterator();
        return null;
    }
    
    
    private class CategoriesIterator implements AdvancedIterator {
        protected final Employee[] employees;
        private int current = 0;
        private int removeOK = 0;
        
        public CategoriesIterator() {
            int size = 0;
            for (int i = 0; i < numCats; i++) {
                size += categories[i].size;
            }
            employees = new Employee[size];
            size = 0;
            for (int i = 0; i < numCats; i++) {
                Category cat = categories[i];
                if (cat.size > 0) System.arraycopy(cat.toArray(), 0, employees, size, cat.size);
                size += cat.size;
            }
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
                Categories.this.remove(employees[current]);
                
                if (employees.length - current >= 0)
                    System.arraycopy(employees, current + 1, employees, current, employees.length - current);
                
                removeOK = 0;
            }
        }
    }
    
}

