package Version1;

import common.Employee;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Jacks on 5/29/18.
 * A program that uses the Circus program.
 */

public class Circus_client {
    public static void main(String[] args) throws IOException {
        /*
         * (1)  Print list alphabetically                                           O(N) - minimal
         * (2)  Print list by IdNum                                                 O(N) - minimal
         * (3) 	Insert new Employee                                                 O(log N)
         * (4) 	Delete an Employee                                                  O(log N)
         * (5) 	Print only a particular category list of employees alphabetically   O(N) - minimal
         * (6) 	Print entire list of all employees by category alphabetically       O(N) - minimal
         * (7) 	Add a category                                                      O(1) - minimal
         * (8) 	Delete a category (and ALL corresponding employees)                 O(N log N)
         * (9) 	Quit
         */

//        PrimaryDS primary = new PrimaryDS();

        /* The current primary data structure is the name tree, but the only drawback with that (and distinguishes it from Marques's algorithm)
           is that each of the corresponding nodes (from other trees) to an employee don't point back to it, so each data structure remains
           separate. However, locating each node will be quicker in my algorithm because there is no O(N) time complexity.
           Problem is, because I'm using a premade AVLTree class I made, I can't edit the inner workings of the insert
           method to link each Employee to its corresponding nodes when they're added to the trees, so I don't think there's
           much that can be done to improve this particular algorithm.
         */
        NameTree names = new NameTree();
        IdTree ids = new IdTree(new IdComparator());
        Categories cats = new Categories();

        System.out.println("Welcome, Welcome, Welcome ladies and gentlemen and children of all ages!  The circus is in town and wow are there some" +
                "\ndoozies in it!  We have everything to thrill and amaze you from the bearded woman to the flying –olini’s.  Come one and all as they" +
                "\ndefy gravity in the heavens, tame ferocious lions, dive into glasses of water, sort multiple arrays, and even use pointers in an" +
                "\noctopus style to simulate an alphabetic and numeric sorted list!  Yes, it is truly amazing.");

        File data = new File("src/Circus/Employees.txt");
        Scanner input = new Scanner(data), line;

        // initializes the data structures to have all given Employees in Marques's list
        while (input.hasNextLine()) {
            String last = input.next(), first = input.next(), mid = input.next(), rawId = input.next(), category = input.next(), title = input.next();
            Employee employee = new Employee(first, mid, last, title, rawId, category);

//            primary.add(employee);
            names.insert(employee);
            ids.insert(employee);
            cats.insert(employee);
        }

        input = new Scanner(System.in);
        int choice = 0;
        while (choice != 9) {
            System.out.println("\nYour options: \n" +
                    "(1)  Print list alphabetically\n" +
                    "(2)  Print list by IdNum\n" +
                    "(3)  Insert new Employee\n" +
                    "(4)  Delete an Employee\n" +
                    "(5)  Print only a particular category list of employees alphabetically\n" +
                    "(6)  Print entire list of all employees by category alphabetically\n" +
                    "(7)  Add a category\n" +
                    "(8)  Delete a category (and ALL corresponding employees)\n" +
                    "(9)  Quit");
            choice = input.nextInt();
            input.nextLine();
            switch (choice) {
                case (1):
                    names.print();
                    break;
                case (2):
                    ids.print();
                    break;
                case (3):
                    System.out.print("Full name? ");
                    line = new Scanner(input.nextLine());
                    String first, middle = "", last, title, id, category;
                    while (!line.hasNext()) {
                        System.out.println("No input received. Full name? ");
                        line = new Scanner(input.nextLine());
                    }
                    first = line.next();
                    if (line.hasNext()) middle = line.next();
                    if (line.hasNext()) last = line.next(); else { last = middle; middle = "#"; }
                    System.out.print("Title? ");
                    title = input.nextLine();
                    System.out.print("Id? (use the hyphen separators) ");
                    id = input.nextLine();
                    while (id.length() != 11 || id.charAt(3) != '-' || id.charAt(7) != '-') {
                        System.out.println("Invalid input. Id? ");
                        id = input.nextLine();
                    }
                    System.out.print("Category? ");
                    category = input.nextLine();

                    Employee e = new Employee(first, middle, last, title, id, category);

                    if (ids.insert(e)) {
                        names.insert(e);
                        cats.insert(e);
                        System.out.println("Employee successfully added!");
                        System.out.println(e);
                    } else {
                        System.out.println("Failed to add Employee: ID already exists!");
                    }
                    break;
                case (4):
                    System.out.print("Id? (use the hyphen separators) ");
                    id = input.nextLine();
                    while (id.length() != 11 || id.charAt(3) != '-' || id.charAt(7) != '-') {
                        System.out.println("Invalid input. Id? ");
                        id = input.nextLine();
                    }

                    if ((e = ids.search(id)) != null) {
                        names.remove(e);
                        cats.remove(e);
                        System.out.println("Employee successfully removed!");
                    } else
                        System.out.println("Failed to remove Employee: Employee doesn't exist.");
                    break;
                case (5):
                    System.out.println("Category? ");
                    cats.printCategory(input.nextLine());
                    break;
                case (6):
                    cats.printAll();
                    break;
                case (7):
                    cats.bornCat(input.nextLine());
                    System.out.println("Category added!");
                    break;
                case (8):
                    System.out.println("Category? ");
                    String cat = input.nextLine();
                    Categories.CategoryTree tree = cats.killCat(cat);
                    if (tree != null) {
//                        primary.removeAll(tree);
                        names.removeAll(tree);
                        ids.removeAll(tree);
                        System.out.println("Category " + cat + "and all corresponding Employees successfully removed!");
                    } else {
                        System.out.println("Failed to remove Category: Category doesn't exist.");
                    }
            }
        }
        System.out.println("\n\nThank you for joining us today! We hope to see you again!");
    }
}
