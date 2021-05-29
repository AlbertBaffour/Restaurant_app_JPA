package fact.it.restaurantapp.model;

public class NormaleBetaling implements BetaalStrategie{
    @Override
    public Double getToegepastePrijs(Double actuelePrijs) {
        return actuelePrijs;
    }
}
