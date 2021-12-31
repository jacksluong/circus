package Version1;

import common.Employee;
import DataStructures.AdvancedIterator;
import DataStructures.Trees.*;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by Jacks on 5/30/18.
 *
 * Some of these methods are used only in CircusV (size(), get(int), allCategories(), employeeCount(), iterator(), categoryIterator(String)).
 */

public class Categories {

    private CategoryTree[] categories;

    private int numCats;

    public Categories() {
        categories = new CategoryTree[10];
        numCats = 0;
    }


    /**
     * Inserts an employee into the corresponding category tree. Creates new tree if category doesn't exist yet.
     */
    public void insert(Employee e) {
        int i;
        for (i = 0; i < numCats; i++) if (categories[i].catName.equalsIgnoreCase(e.getCategory())) break;
        if (i == numCats) {
            // create new category
            (categories[i] = new CategoryTree(e.getCategory())).insert(e);
            numCats++;

            // if array is getting full
            if (i >= categories.length * 0.8) categories = Arrays.copyOf(categories, categories.length * 2);
            return;
        }
        categories[i].insert(e);
    }


    /**
     * Removes an employee from the tree it exists in.
     */
    public void remove(Employee e) {
        int i;
        for (i = 0; i < numCats; i++) if (categories[i].catName.equalsIgnoreCase(e.getCategory())) {
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
        return categories[index < numCats ? index : numCats - 1].catName;
    }


    /**
     * Adds a category into the array if it doesn't already exist.
     */
    public void bornCat(String cat) {
        int i;
        for (i = 0; i < numCats; i++) if (categories[i].catName.equalsIgnoreCase(cat)) {
            System.out.println("Category already exists.");
            return;
        }
        categories[i] = new CategoryTree(cat);
        if (i >= categories.length * 0.8) categories = Arrays.copyOf(categories, categories.length * 2);
        numCats++;
    }


    /**
     * Removes all employees of a certain category and removes the category.
     */
    public CategoryTree killCat(String cat) {
        for (int i = 0; i < numCats; i++) if (categories[i].catName.equalsIgnoreCase(cat)) {
            CategoryTree catTree = categories[i];
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
            if (categories[i].catName.equals(category)) {
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
     * Prints all existing categories.
     */
    public String[] allCategories() {
        String[] cats = new String[numCats];
        for (int i = 0; i < numCats; i++) cats[i] = categories[i].catName;
        return cats;
    }


    /**
     * Returns the number of employees under that category.
     */
    public int employeeCount(String category) {
        for (int i = 0; i < numCats; i++) {
            if (categories[i].catName.equals(category)) {
                return categories[i].size();
            }
        }
        return 0;
    }


    /**
     * The trees of each category that are alphabetically sorted.
     */
    protected class CategoryTree extends AVLTree<Employee> {

        private final String catName;

        public CategoryTree(String cat) {
            catName = cat;
        }

    }


    /**
     * Returns an iterator of a list that contains all employees, sorted categorically then alphabetically.
     */
    public Iterator iterator() {
        return new Categories.CategoriesIterator();
    }


    public Iterator categoryIterator(String cat) {
        for (int i = 0; i < numCats; i++) if (categories[i].catName.equals(cat) && categories[i].size() > 0) return categories[i].iterator();
        return null;
    }


    private class CategoriesIterator implements AdvancedIterator {
        protected final Employee[] employees;
        private int current = 0;
        private int removeOK = 0;

        public CategoriesIterator() {
            int size = 0;
            for (int i = 0; i < numCats; i++) {
                size += categories[i].size();
            }
            employees = new Employee[size];
            size = 0;
            for (int i = 0; i < numCats; i++) {
                CategoryTree cat = categories[i];
                if (cat.size() > 0) System.arraycopy(cat.toArray(), 0, employees, size, cat.size());
                size += cat.size();
            }
        }

        @Override
        public boolean hasNext() {
            return this.current < employees.length;
        }

        @Override
        public Object next() {
            if (this.hasNext()) {
                this.removeOK = 1;
                return employees[current++];
            } else {
                throw new IllegalStateException("End of the line!");
            }
        }

        @Override
        public boolean hasPrevious() {
            return this.current > 0;
        }

        @Override
        public Object previous() {
            if (this.hasPrevious()) {
                this.removeOK = 2;
                return this.employees[--current];
            } else {
                throw new IllegalStateException("End of the line!");
            }
        }

        @Override
        public void remove() {
            if (this.removeOK == 0) {
                throw new IllegalStateException("Did not call previous() or next()");
            } else {
                this.current -= this.removeOK % 2;
                Categories.this.remove(employees[current]);
    
                if (employees.length - current >= 0)
                    System.arraycopy(employees, current + 1, employees, current, employees.length - current);

                this.removeOK = 0;
            }
        }
    }
}
