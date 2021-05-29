package fact.it.restaurantapp.model;

import javax.persistence.*;

@Entity
public class Zaalpersoneel extends Personeel{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Zaalpersoneel() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public void update(){
        System.out.println("Ik ben "+this.getNaam()+" en ga het nodige doen om voor " +
                + IngangTeller.getAantal()+" klanten een tafel klaar te maken.");
    }
}
