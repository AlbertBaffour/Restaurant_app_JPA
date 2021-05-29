package fact.it.restaurantapp.model;

public interface BetaalStrategie {
abstract Double getToegepastePrijs(Double actuelePrijs);
}
