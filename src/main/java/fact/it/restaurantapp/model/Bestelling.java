package fact.it.restaurantapp.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

@Entity
public class Bestelling {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private GregorianCalendar datum;
    private Boolean betaald;

    @ManyToOne
    private Zaalpersoneel zaalpersoneel;

    @ManyToOne
    private Tafel tafel;

    @OneToMany(mappedBy = "bestelling")
    private List<BesteldItem> besteldItems = new ArrayList<>();

    @Transient
    private BetaalStrategie betaalStrategie;

    public Bestelling() {
        betaalStrategie =new NormaleBetaling();
    }

    public Zaalpersoneel getZaalpersoneel() {
        return zaalpersoneel;
    }

    public void setZaalpersoneel(Zaalpersoneel zaalpersoneel) {
        this.zaalpersoneel = zaalpersoneel;
    }

    public Tafel getTafel() {
        return tafel;
    }

    public void setTafel(Tafel tafel) {
        this.tafel = tafel;
    }

    public List<BesteldItem> getBesteldItems() {
        return besteldItems;
    }

    public void setBesteldItems(List<BesteldItem> besteldItems) {
        this.besteldItems = besteldItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BetaalStrategie getBetaalStrategie() {
        return betaalStrategie;
    }

    public void setBetaalStrategie(BetaalStrategie betaalStrategie) {
        this.betaalStrategie = betaalStrategie;
    }

    public GregorianCalendar getDatum() {
        return datum;
    }

    public void setDatum(GregorianCalendar datum) {
        this.datum = datum;
    }

    public Boolean getBetaald() {
        return betaald;
    }

    public void setBetaald(Boolean betaald) {
        this.betaald = betaald;
    }

    public String getBetaalstatus() {
        if (betaald){
        return "betaald";}
        else{return "niet betaald";}
    }

    public void addItem(Gerecht gerecht, Integer aantal){
        BesteldItem besteldItem=new BesteldItem();
        besteldItem.setGerecht(gerecht);
        besteldItem.setAantal(aantal);
        besteldItem.setBestelling(this);
        besteldItem.setToegepastePrijs(betaalStrategie.getToegepastePrijs(gerecht.getActuelePrijs()));
        besteldItems.add(besteldItem);
    }
    public Double getTotaal(){
        Double totaal=0.0;
        for (BesteldItem b:besteldItems) {
        totaal += b.getAantal()*b.getToegepastePrijs();
        }
        return totaal;
    }
}
