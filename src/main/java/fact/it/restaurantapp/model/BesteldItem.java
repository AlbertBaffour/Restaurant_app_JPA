package fact.it.restaurantapp.model;

import javax.persistence.*;

@Entity
public class BesteldItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer aantal;
    private Double toegepastePrijs;

    @ManyToOne
    private Bestelling bestelling;

    @ManyToOne
    private Gerecht gerecht;

    public BesteldItem() {
    }

    public Bestelling getBestelling() {
        return bestelling;
    }

    public void setBestelling(Bestelling bestelling) {
        this.bestelling = bestelling;
    }

    public Gerecht getGerecht() {
        return gerecht;
    }

    public void setGerecht(Gerecht gerecht) {
        this.gerecht = gerecht;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAantal() {
        return aantal;
    }

    public void setAantal(Integer aantal) {
        this.aantal = aantal;
    }

    public Double getToegepastePrijs() {
        return toegepastePrijs;
    }

    public void setToegepastePrijs(Double toegepastePrijs) {
        this.toegepastePrijs = toegepastePrijs;
    }
}
