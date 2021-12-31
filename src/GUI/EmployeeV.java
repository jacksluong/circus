package GUI;

import common.Employee;

/**
 * Created by Jacks on 11/22/18.
 * V for visual.
 */

public class EmployeeV extends Employee {
    private final ECircle visual;

    public EmployeeV(String first, String mid, String last, String title, String printId, String category) {
        super(first, mid, last, title, printId, category);
        visual = new ECircle(getReverseInitials() + "\n" + category + "\n" + printId, this);
    }

    public ECircle getVisual() {
        return visual;
    }
}
