package fact.it.restaurantapp.model;

abstract class ExtraTaak extends Personeel{

    private Personeel personeel;

    public Personeel getPersoneel() {
        return personeel;
    }

    public void setPersoneel(Personeel personeel) {
        this.personeel = personeel;
    }
    public void update(){
        personeel.update();
    }
}
