package fact.it.restaurantapp.model;

public class Administrator extends ExtraTaak{
    @Override
    public void update() {
        super.update();
        System.out.println("Vervolgens registreer ik de "+IngangTeller.getAantal()+" klanten in het klantenbestand.");
    }
}
