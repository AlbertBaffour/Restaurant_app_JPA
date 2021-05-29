package fact.it.restaurantapp.model;

public class HappyHourBetaling implements BetaalStrategie {
    @Override
    public Double getToegepastePrijs(Double actuelePrijs) {
        return actuelePrijs*0.8;
    }
}
