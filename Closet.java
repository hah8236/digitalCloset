import java.util.*;

// Class representing a closet
public class Closet {

    private final Scanner console = new Scanner(System.in);
    public Map<String, TreeSet<Garment>> closet;
    private final String userName;
    private double totalValue;

    public Closet(String userName) {
        this.userName = userName;
        totalValue = 0;
        closet = new TreeMap<>();
        closet.put("tops", new TreeSet<>());
        closet.put("bottoms", new TreeSet<>());
        closet.put("full body", new TreeSet<>());
        closet.put("outerwear", new TreeSet<>());
        closet.put("shoes", new TreeSet<>());
        closet.put("accessories", new TreeSet<>());
    }

    // Adds a new garment to the closet
    public void add() {
        System.out.println("Please select the type of garment you would like to add:");
        String type = typeSelection();
        while (!closet.containsKey(type)) {
            incorrectTypeSelection();
            type = typeSelection();
        }

        System.out.println("Please enter the name of your item.");
        System.out.println("NOTE: Please make sure to give this item a UNIQUE name. ");
        System.out.println("Adding an item with the same name as a previously added " +
                "item will result in errors. Names are not case sensitive.");
        System.out.print("Name: ");
        String name = console.nextLine();
        System.out.println("Please enter the price of this item. " +
                "Please type a valid number value (decimals accepted).");
        System.out.print("Price: ");
        double cost = console.nextDouble();
        console.nextLine();
        totalValue += cost;
        System.out.println("Please write any notes you have about the item. Press enter when you are done.");
        System.out.print("Notes: ");
        String notes = console.nextLine();

        Garment newItem = new Garment(name, type, cost, notes);

        closet.get(type).add(newItem);
        System.out.println("Success. Item has been added to " + userName + "'s closet.");
        System.out.print("Press Enter to return to the main window.");
        console.nextLine();
        mainWindow();
    }

    // Removes a garment from the closet
    public void remove() {
        System.out.println("Please select the type of garment you would like to remove:");
        String type = typeSelection();
        while(!closet.containsKey(type)) {
            incorrectTypeSelection();
            type = typeSelection();
        }

        System.out.println("Please write the name of the item you would like to remove.");
        System.out.println("NOTE: not case sensitive, be careful to check for whitespace");
        System.out.print("Name: ");
        String name = console.nextLine();
        Garment item = findGarment(type, name);
        while(item == null) {
           name = nameNotFound();
           if (name == null) {
               mainWindow();
           } else {
               item = findGarment(type, name);
           }
        }
        totalValue -= item.getCost();
        closet.get(type).remove(item);
        System.out.println("Success. Item has been removed from");
        System.out.print("Press Enter to return to the main window.");
        console.nextLine();
        mainWindow();
    }

    // Logs a new instance of a garment being worn. Updates the number of times
    // that garment has been worn and allows the user to input the last date it was worn.
    public void logWear() {
        System.out.println("Please select the type of garment you would like to log.");
        String type = typeSelection();
        while(!closet.containsKey(type)) {
            incorrectTypeSelection();
            type = typeSelection();
        }

        System.out.println("Please write the name of the item you would like to log.");
        System.out.print("Name: ");
        String name = console.nextLine();
        Garment item = findGarment(type, name);
        while(item == null) {
            name = nameNotFound();
            if (name == null) {
                mainWindow();
            } else {
                item = findGarment(type, name);
            }
        }

        System.out.println("Please enter the date in which this item was last worn, " +
                "following the format MO/DA/YEAR");
        System.out.print("Date: ");
        String date = console.nextLine();
        item.increaseWear(date);
        System.out.println("Success. Item has been logged.");
        System.out.print("Press Enter to return to the main window.");
        console.nextLine();
        mainWindow();
    }

    // Allows the user to search for a garment within their closet.
    // The user can either search by name or by notes.
    // Prints the items within the selected category that contain the inputted keyword.
    public void search() {
        System.out.println("Please select the type of garment you would like to search.");
        String type = typeSelection();
        while(!closet.containsKey(type)) {
            incorrectTypeSelection();
            type = typeSelection();
        }

        System.out.println("Choose how you would like to search: ");
        System.out.println("1) By name");
        System.out.println("2) By notes");
        System.out.print("Enter your choice: ");
        int choice = console.nextInt();
        if (choice > 2 || choice < 1) {
            validSelection(2);
        }
        if (choice == 1) {
            searchNames(type);
        } else if (choice == 2) {
            searchNotes(type);
        }
        System.out.print("Press Enter when you are ready to return to the main window.");
        console.nextLine();
        mainWindow();
    }

    // Prompts the user to select which of the 4 display options they would like to select.
    // 1) Prints all unworn garments in a single category
    // 2) Prints the garments worn least recently (not unworn) within a single category
    // 3) Prints all the garments within a single category
    // 4) Prints every garment inside the closet
    public void display() {
        System.out.println("Please choose how you would like to display the items within your closet.");
        System.out.println("1) Display unworn items within a category");
        System.out.println("2) Display the items worn least recently within a category");
        System.out.println("3) Display all items within a category");
        System.out.println("4) Display all items within the closet");
        System.out.print("Enter your choice: ");
        int choice = console.nextInt();
        console.nextLine();
        if (choice > 4 || choice <= 0) {
            validSelection(4);
        }

        if (choice == 4) {
            printCloset();
        } else {
            String type = typeSelection();
            while(!closet.containsKey(type)) {
                incorrectTypeSelection();
                type = typeSelection();
            }
            if (choice == 1) {
                printUnworn(type);
            } else if (choice == 2) {
                printLeastRecentFive(type);
            } else if (choice == 3) {
                printCategory(type, false);
            }
        }
        System.out.print("Press Enter when you are ready to return to the main window.");
        console.nextLine();
        mainWindow();
    }

    // Prints the top of the table
    private void printHeader() {
        System.out.printf("%-20s | %-12s | %-14s | %-10s | %-10s | %-50s\n",
                "Name", "Category", "Price Per Wear", "Times Worn", "Last Worn", "Notes");
        for (int i = 131; i > 0; i--) {
            System.out.print("-");
        }
        System.out.println(" ");
    }

    // Searches for and prints garments with a user generated name of a given type.
    private void searchNames(String type) {
        System.out.println("Please enter the name you would like to search for.");
        System.out.println("Not case sensitive.");
        System.out.print("Name:");
        console.nextLine();
        String name = console.nextLine();
        name = name.toLowerCase();

        boolean found = false;
        TreeSet<Garment> closetSection = new TreeSet<>(closet.get(type));
        for (Garment item : closetSection) {
            String itemName = item.getName().toLowerCase();
            if (itemName.contains(name)) {
                if (!found) {
                    printHeader();
                }
                found = true;
                item.printGarment();
            }
        }
        if (!found) {
            System.out.println("Sorry, no items found with the inputted name.");
        }
    }

    // Searches for and prints garments with a user generated
    // keyword within its notes of a given type
    private void searchNotes(String type) {
        System.out.println("Please enter the keyword you would like to search for within the notes.");
        System.out.println("Not case sensitive.");
        System.out.print("Keyword:");
        console.nextLine();
        String keyword = console.nextLine();
        keyword = keyword.toLowerCase();

        boolean found = false;
        TreeSet<Garment> closetSection = new TreeSet<>(closet.get(type));
        for (Garment item : closetSection) {
            String itemNotes = item.getNotes().toLowerCase();
            if (itemNotes.contains(keyword)) {
                if (!found) {
                    printHeader();
                }
                found = true;
                item.printGarment();
            }
        }
        if (!found) {
            System.out.println("Sorry, no notes found with the inputted keyword.");
        }
    }

    // Prints all the garments within the given category
    // If the category is empty and the user is not printing their entire closet,
    // this will print a message stating that there are no items of that type
    private void printCategory(String type, boolean entireCloset) {
        TreeSet<Garment> closetSection = new TreeSet<>(closet.get(type));
        if (!entireCloset && closetSection.isEmpty()) {
            System.out.println("There are no items of the type: " + type + " in your closet.");
        } else {
            if (!entireCloset) {
                printHeader();
            }
            for (Garment item : closetSection) {
                item.printGarment();
            }
        }
    }

    // Prints every garment within the closet, separated by the type of garment
    private void printCloset() {
        printHeader();
        printCategory("tops", true);
        printCategory("bottoms", true);
        printCategory("full body", true);
        printCategory("outerwear", true);
        printCategory("shoes", true);
        printCategory("accessories", true);
        System.out.println(" ");
        System.out.println("Total closet value: $" + totalValue);
    }

    // Prints the 5 least recently worn items from a given category.
    // If there are less than 5 items that have been worn, it will print every item.
    // Does not print unworn items.
    private void printLeastRecentFive(String type) {
        TreeSet<Garment> closetSection = new TreeSet<>(closet.get(type));
        if (closetSection.isEmpty()) {
            System.out.println("There are no unworn items of the type " + type + " in your closet.");
        } else {
            int remPrints = closetSection.size();
            if (remPrints > 5) {
                remPrints = 5;
            }
            for (Garment item : closetSection) {
                if (item.getTimesWorn() > 0 && remPrints > 0) {
                    item.printGarment();
                    remPrints--;
                }
            }
        }
    }

    // Prints every unworn item of the given type and the number of unworn items.
    private void printUnworn(String type) {
        boolean unworn = true;
        int totUnworn = 0;
        TreeSet<Garment> closetSection = new TreeSet<>(closet.get(type));
        if (!closetSection.isEmpty()) {
            Iterator<Garment> items = closetSection.iterator();
            while (items.hasNext() && unworn) {
                Garment item = items.next();
                if (item.getTimesWorn() == 0) {
                    item.printGarment();
                    totUnworn++;
                } else {
                    unworn = false;
                }
            }
        }
        if (closetSection.isEmpty() || totUnworn == 0) {
            System.out.println("You have no unworn garments of this category.");
        } else {
            System.out.println("You have " + totUnworn + " unworn items in this category.");
        }
    }

    // Allows the user to select which type of garment they would like to
    // Return type in the closet
    private String typeSelection() {
        System.out.println("Write the type from the list below (not case sensitive):");
        System.out.println("Tops");
        System.out.println("Bottoms");
        System.out.println("Full Body");
        System.out.println("Outerwear");
        System.out.println("Shoes");
        System.out.println("Accessories");
        System.out.println(" ");
        System.out.print("Type: ");

        String type = console.nextLine();
        type = type.toLowerCase();
        return type;
    }

    // After the user inputs an invalid type, prints tips to help the user with their next input.
    private void incorrectTypeSelection() {
        System.out.print(" ");
        System.out.println("Invalid type. Please try again.");
        System.out.println("TIP: To avoid errors, type your selection exactly as shown below.");
        System.out.println("Be careful of extra whitespace before, after, " +
                "or in the middle of your response.");
        System.out.println(" ");
    }

    // Returns the garment of the given type with the given name
    // Returns null if there is not a garment of the given type with the given name
    private Garment findGarment(String type, String name) {
        name = name.toLowerCase();
        TreeSet<Garment> closetSection = new TreeSet<>(closet.get(type));
        for (Garment item : closetSection) {
            String itemName = item.getName();
            if (itemName.toLowerCase().equals(name)) {
                return item;
            }
        }
        return null;
    }

    // Occurs if the item the user wants to remove does not exist,
    // allows the user to either try again or return to main menu.
    private String nameNotFound() {
        System.out.println("Name not found.");
        System.out.println("NOTE: You must enter the exact name of the item you would like to select.");
        System.out.println("Be careful to check for whitespace, not case sensitive.");
        System.out.println("Please type the name of the item you would like to select, or " +
                "type 'EXIT' to return to main menu");
        String response = console.nextLine();
        if (!response.equalsIgnoreCase("EXIT")) {
            return response;
        }
        return null;
    }

    // Makes sure the user makes a valid choice during number selection
    public int validSelection(int max) {
        System.out.println("Invalid choice. Please try again.");
        System.out.println("Enter your choice: ");
        int choice = console.nextInt();
        console.nextLine();
        if (choice > max || choice <= 0) {
            choice = validSelection(max);
        }
        return choice;
    }

    // The Digital Closet program's main menu.
    // Allows the user to choose which action they would like to do.
    public void mainWindow() {
        System.out.println("Welcome to the main window.");
        System.out.println(" ");

        System.out.println("Please select from the following actions:");
        System.out.println(" ");

        System.out.println("1) Add Item");
        System.out.println("2) Log Item"); // log an item as worn
        System.out.println("3) Search Items");
        System.out.println("4) Remove Item");
        System.out.println("5) Display Items");
        System.out.println("6) Quit");
        System.out.print("Your choice: ");

        int choice = console.nextInt();
        console.nextLine();
        if (choice > 6 || choice <= 0) {
            validSelection(6);
        }

        if (choice == 1) {
            add();
        } else if (choice == 2) {
            logWear();
        } else if (choice == 3) {
            search();
        } else if (choice == 4) {
            remove();
        } else if (choice == 5) {
            display();
        } else if (choice == 6) {
            System.out.println("Have a nice day!");
            console.close();
        }
    }
}
