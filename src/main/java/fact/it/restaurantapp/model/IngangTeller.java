package fact.it.restaurantapp.model;

import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

public class IngangTeller extends Subject{
    private static Integer aantal;
    private static IngangTeller ingangTeller;

    private ArrayList<Personeel> observers;


    private IngangTeller() {

        System.out.println("Er wordt een singleton-object aangemaakt.");
        ////////////////// Deze nooit meer vergeten!!!!!!!!!!!!!!!!!!!!!!!!
        observers =new ArrayList<>();
    }

    public static IngangTeller getInstance() {
        if (ingangTeller == null) {
            ingangTeller = new IngangTeller();
        }
        return ingangTeller;
    }

    public static Integer getAantal() {
        return aantal;
    }

    public void setAantal(Integer aantal) {
        IngangTeller.aantal = aantal;
        notifyObserver();
    }

    @Override
    public ArrayList<Personeel> getObservers() {
        return observers;
    }

    @Override
    public void setObservers(ArrayList<Personeel> observers) {
        this.observers = observers;
    }

    @Override
    public void attachObserver(Personeel newObserver) {
        // Adds a new observer to the ArrayList
        observers.add(newObserver);
    }
    @Override
    public void detachObserver(Personeel deleteObserver) {
        // Get the index of the observer to delete
        int observerIndex = observers.indexOf(deleteObserver);
        observers.remove(observerIndex);
    }
    @Override
    public void notifyObserver() {
        for(Personeel observer : observers){
            observer.update();
        }
    }
}
