package fact.it.restaurantapp.model;

import javax.persistence.*;

@Entity
public class Keukenpersoneel extends Personeel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;
    public Keukenpersoneel() {
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
        System.out.println("Ik ben "+this.getNaam()+" en ik begin " +
                "onmiddellijk met het maken van "+ IngangTeller.getAantal()+" amuse-gueules!");
    }
}
