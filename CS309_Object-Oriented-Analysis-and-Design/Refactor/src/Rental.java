class Rental {
//    private final Movie movie = new Movie("movie_title", Movie.REGULAR);
    private final Movie _movie;

    private int _daysRented;
    public Rental(Movie movie, DateRange dateRange) {
//        this.movie.setTitle(title);
//        this.movie.setPriceCode(priceCode);
        _movie = movie;
        _daysRented = (int)((dateRange.getEnd().getTime() - dateRange.getStart().getTime()) / (1000 * 60 * 60 * 24));
    }
    public int getDaysRented() {
        return _daysRented;
    }

    public String getTitle() {
        return _movie.getTitle();
    }
    
    public int getPriceCode() {
        return _movie.getPriceCode();
    }

    public Movie getMovie() {
        return _movie;
    }

    public double getCharge() {
//        double thisAmount = 0;
//        switch (getPriceCode()) {
//        case Movie.REGULAR:
//            thisAmount += 2;
//            if (getDaysRented() > 2) {
//                thisAmount += (getDaysRented() - 2) * 1.5;
//            }
//            break;
//        case Movie.NEW_RELEASE:
//            thisAmount += getDaysRented() * 3;
//            break;
//        case Movie.CHILDRENS:
//            thisAmount += 1.5;
//            if (getDaysRented() > 3) {
//                thisAmount += (getDaysRented() - 3) * 1.5;
//            }
//            break;
//        }
//        return thisAmount;
        return _movie.getCharge(_daysRented);
    }

    public int getFrequentRenterPoints() {
//        int frequentRenterPoints = 0;
//        // add frequent renter points
//        frequentRenterPoints++;
//        // add bonus for a two day new release rental
//        if ((getPriceCode() == Movie.NEW_RELEASE)
//                && getDaysRented() > 1) frequentRenterPoints++;
//        return frequentRenterPoints;
        return _movie.getFrequentRenterPoints(_daysRented);
    }

}

