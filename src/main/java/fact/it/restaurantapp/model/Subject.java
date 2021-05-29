package fact.it.restaurantapp.model;

import javax.persistence.OneToMany;
import java.util.ArrayList;

abstract class Subject {

    private ArrayList<Personeel> observers;

    public ArrayList<Personeel> getObservers() {
        return observers;
    }

    public void setObservers(ArrayList<Personeel> observers) {
        this.observers = observers;
    }

    public abstract void attachObserver(Personeel p);
    public abstract void detachObserver(Personeel p);
    public abstract void notifyObserver();

}
