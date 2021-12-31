package Version1;

import common.Employee;

import java.util.Comparator;

/**
 * Created by Jacks on 5/25/18.
 */

public class IdComparator implements Comparator<Employee> {
    @Override
    public int compare(Employee o1, Employee o2) {
        return o1.getPrintId().replaceAll("-", "").compareTo(o2.getPrintId().replaceAll("-", ""));
    }
}
