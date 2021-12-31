package Version1;

import common.Employee;
import DataStructures.Trees.*;

import java.util.Comparator;

/**
 * Created by Jacks on 5/30/18.
 */

public class IdTree extends AVLTree<Employee> {

    public IdTree(Comparator<Employee> c) {
        this.c = c;
    }

    public Employee search(String id) {
        Path p = search(new Employee("","","","", id,""));
        if (p.getN() != null) return p.getN().getE();
        else return null;
    }

    @Override
    public void print() {
        System.out.println("All employees, printed by their ids in numerical order.");
        super.print();
    }

}
