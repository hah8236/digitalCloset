// Class representing an article of clothing.
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

// Class representing an article of clothing.
public class Garment implements Comparable<Garment> {


    // name of item
    private final String name;
    // type of clothing
    private final String type;
    // original cost
    private final double cost;
    // number of times worn
    private int timesWorn;
    // most recent date worn
    private String lastWorn;
    // notes
    private final String notes;

    // Constructs the Garment class
    public Garment(String name, String type, double cost, String notes) {
        this.name = name;
        this.type = type;
        this.cost = cost;
        this.timesWorn = 0;
        this.lastWorn = "N/A";
        this.notes = notes;
    }

    // Returns the name of the garment
    public String getName() {
        return name;
    }

    // Returns the notes of the garment
    public String getNotes() {
        return notes;
    }

    // Returns the number of times the item has been worn
    public int getTimesWorn() {
        return timesWorn;
    }

    // Returns the original cost of the item
    public double getCost() {
        return cost;
    }

    // Increases the number of times the garment has been worn by 1
    // and saves the new date in which the item was worn
    public void increaseWear(String date) {
        timesWorn++;
        this.lastWorn = date;
    }

    // Prints the garment information in the following format:
    // Name | Type | Price Per Wear | Times Worn | Last Worn | Notes
    // If the item has never been worn, prints the  PPW as the original cost
    public void printGarment() {
        double ppw;
        if (timesWorn != 0) {
            ppw = pricePerWear();
        } else {
            ppw = cost;
        }
        System.out.printf("%-20s | %-12s | %-14.2f | %-10d | %-10s | %-50s\n",
                name, type, ppw, timesWorn, lastWorn, notes);
    }

    // Calculates and returns the price per wear of the garment
    // PPW = original cost / number of times worn
    private double pricePerWear() {
        return Math.round((cost / timesWorn) * 100) / 100.0;
    }

    // Compares a Garment object against another Garment object.
    // The comparison is based on the following rules:
    // 1) Unworn garments will come before worn garments
    // 2) For two unworn garments, the garment with the highest cost will come first
    // 3) For worn garments, the garment with the most recent lastWorn date will come first
    // 4) If the two garments have the same lastWorn date, the garment with the lower
    //      timesWorn will come first
    // 5) If the two garments have been worn the same number of times, the object
    //      with the highest cost will come first
    @Override
    public int compareTo(Garment other) {
        // 1 unworn
        if (this.timesWorn == 0 && other.timesWorn != 0) {
            return -1;
        } else if (this.timesWorn != 0 && other.timesWorn == 0) {
            return 1;
        }
        // 0 unworn garments
        if (this.timesWorn != 0 && other.timesWorn != 0) {
            if (!this.lastWorn.equals(other.lastWorn)) {
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                Date thisDate = format.parse(this.lastWorn, new ParsePosition(0));
                Date otherDate = format.parse(other.lastWorn, new ParsePosition(0));
                return thisDate.compareTo(otherDate);
            } else if (this.timesWorn != other.timesWorn) {
                return Integer.compare(this.timesWorn, other.timesWorn);
            }
        }
        // Either tie between 2 items worn the same number of times or 2 unworn garments
        return Double.compare(this.cost, other.cost);
    }
}
