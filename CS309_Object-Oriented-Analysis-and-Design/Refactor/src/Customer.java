import java.util.Vector;

class Customer {
    private String _name;
    private Vector<Rental> _rentals = new Vector<Rental>();

    public Customer(String name) {
        _name = name;
    }

    public void addRental(Rental arg) {
        _rentals.addElement(arg);
    }

    public String getName() {
        return _name;
    }

    public String statement() {
//        double totalAmount = 0;
        int frequentRenterPoints = 0;
        StringBuilder result = new StringBuilder("Rental Record for " + getName() + "\n");

        for (Rental each : _rentals) {

            //determine amounts for each line
//            double thisAmount = each.getCharge();
            // add frequent renter points
            frequentRenterPoints ++;
            // add bonus for a two day new release rental
            if ((each.getPriceCode() == Movie.NEW_RELEASE)
                    && each.getDaysRented() > 1) frequentRenterPoints++;

            //show figures for this rental
            result.append("\t").append(each.getTitle());
//            result.append("\t").append(String.valueOf(thisAmount);
            result.append("\t").append(String.valueOf(each.getCharge()));
            result.append("\n");

//            totalAmount += thisAmount;
//            totalAmount += each.getCharge();
        }

        //add footer lines
//        result.append("Amount owed is ").append(String.valueOf(totalAmount));
        result.append("Amount owed is ").append(String.valueOf(getTotalCharge()));
        result.append("\n");
        result.append("You earned ").append(String.valueOf(frequentRenterPoints));
        result.append(" frequent renter points");
        return result.toString();
    }

    public double getTotalCharge() {
        double result = 0;
        for (Rental each : _rentals) {
            result += each.getCharge();
        }
        return result;
    }

    public int getTotalFrequentRenterPoints() {
        int frequentRenterPoints = 0;
        for (Rental each : _rentals) {
            frequentRenterPoints += each.getFrequentRenterPoints();
        }
        return frequentRenterPoints;
    }

    public String htmlStatement() {
        StringBuilder result = new StringBuilder("<H1>Rentals for <EM>").append(getName());
        result.append("</EM></H1><P>\n");
        for (Rental each : _rentals) {
        // show figures for each rental
            result.append(each.getMovie().getTitle()).append(": ");
            result.append(String.valueOf(each.getCharge())).append("<BR>\n");
        }
        // add footer lines
        result.append("<P>You owe <EM>" + String.valueOf(getTotalCharge()));
        result.append("</EM><P>\n");
        result.append("On this rental you earned <EM>");
        result.append(String.valueOf(getTotalFrequentRenterPoints()));
        result.append("</EM> frequent renter points<P>");
        return result.toString();
    }

}