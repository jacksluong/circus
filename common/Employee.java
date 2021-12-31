package common;

/**
 * Created by Jacks on 5/25/18.
 */

public class Employee implements Comparable<Employee> {

    final String first, mid, last, fullName, printId, title, category;
    final int idNum;

    public Employee(String first, String mid, String last, String title, String printId, String category) {
        this.first = !first.equals("") ? first.substring(0,1).toUpperCase() + first.substring(1) : ""; // makes sure first name is always capitalized
        this.mid = mid;
        this.last = last;
        this.fullName = (last.equals("") ? "" : (last + ", ")) + first + " " + (mid.equals("#") ? "" : mid); // l, f m
        this.printId = printId; // the id with hyphens
        this.title = title;
        this.category = category;
        this.idNum = Integer.parseInt(printId.replaceAll("-", "")); // nine digit integer
    }

    @Override
    public int compareTo(Employee e) {
        if (e == null) throw new IllegalArgumentException("Cannot compare to null");
        int compare;
        if ((compare = getRawName().compareToIgnoreCase(e.getRawName())) != 0) return compare;
        else return idNum - e.idNum; // same name, and if same ID, then same person
    }

    @Override
    public String toString() { return String.format("%-25s%-19s%-18s", fullName, printId, category) + title; }

    /*********************** Getters **************************/
    public String getFirst() {
        return first;
    }

    public String getMid() {
        return mid;
    }

    public String getLast() {
        return last;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRawName() { return (last + first + (mid.equals("#") ? "" : mid)).toLowerCase().replaceAll("[^a-z]", ""); }

    public String getPrintId() { return printId; }
    
    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getReverseInitials() { return "" + (last.equals("") ? "" : last.charAt(0)) + first.charAt(0) + (mid.charAt(0) == '#' ? "" : mid.charAt(0)); }
}
