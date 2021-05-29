package fact.it.restaurantapp.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import fact.it.restaurantapp.model.*;


import javax.servlet.http.HttpServletRequest;
import java.util.GregorianCalendar;

@Controller
public class MainController {

    @RequestMapping("/start")
    public String starten(Model model, HttpServletRequest request) {
        String feedbacktekst = "";

        if (request.getParameter("singletontest") != null) {
            System.out.println("####################################################################");
            IngangTeller it1 = IngangTeller.getInstance();
            IngangTeller it2;
            it2 = IngangTeller.getInstance();
            if (it1 == it2) {
                System.out.println("De twee singletonvariabelen verwijzen naar hetzelfde object.");
            } else {
                System.out.println("Dit kan in principe niet.");
            }
            System.out.println("####################################################################");
            feedbacktekst = "Singletonpatroon is uitgevoerd. Kijk naar het outputvenster in IntelliJ";
        }
        if (request.getParameter("observertest") != null) {
            IngangTeller klantTeller = IngangTeller.getInstance();

            //een paar personeelsleden
            Zaalpersoneel jan = new Zaalpersoneel();
            jan.setNaam("Jan");
            Zaalpersoneel piet = new Zaalpersoneel();
            piet.setNaam("Piet");
            Keukenpersoneel serge = new Keukenpersoneel();
            serge.setNaam("Serge");
            Keukenpersoneel jeroen = new Keukenpersoneel();
            jeroen.setNaam("Jeroen");

            //we koppelen het zaalpersoneel en keukenpersoneel als observer aan de ingangteller
            klantTeller.attachObserver(jan);
            klantTeller.attachObserver(piet);
            klantTeller.attachObserver(serge);
            klantTeller.attachObserver(jeroen);


            System.out.println("####################################################################");
            System.out.println("Na het toevoegen van de observers zetten we de ingangteller op 5...");
            //ingangteller registreert aanwezigheden
            klantTeller.setAantal(5);
            //lege lijn
            System.out.println();
            //we doen enkele observers weg
            klantTeller.detachObserver(piet);
            klantTeller.detachObserver(serge);

            System.out.println("Na het ontkoppelen van Piet en Serge zetten we de ingangteller op 3...");
            //we veranderen het aantal aanwezigen op de ingangteller
            klantTeller.setAantal(3);
            System.out.println("####################################################################");

        }
        if (request.getParameter("strategytest") != null) {
            System.out.println("####################################################################");
            //Betalingstrategieën aanmaken
            HappyHourBetaling happyHourBetaling = new HappyHourBetaling();
            NormaleBetaling normaleBetaling = new NormaleBetaling();
            //gerechten aanmaken
            Gerecht videe = new Gerecht();
            videe.setNaam("Vidée met frietjes");
            videe.setActuelePrijs(15.0);
            Gerecht croque = new Gerecht();
            croque.setNaam("Croque Monsieur");
            croque.setActuelePrijs(10.0);


            //maak bestelling met bestelitems
            Bestelling bestelling = new Bestelling();
            //NORMAAL
            bestelling.setBetaalStrategie(normaleBetaling);
            bestelling.setDatum(new GregorianCalendar());
            bestelling.addItem(videe, 2);
            bestelling.addItem(croque, 2);
            System.out.println("De normale betalingsstrategie is van kracht: ");
            for( BesteldItem i: bestelling.getBesteldItems()){
                System.out.println(i.getAantal() + " " + i.getGerecht().getNaam() + " prijs " + i.getToegepastePrijs() );
            }


            //HAPPYHOUR
            bestelling.setBetaalStrategie(happyHourBetaling);
            bestelling.addItem(videe, 2);
            bestelling.addItem(croque, 2);
            System.out.println("De happy-hour-betalingsstrategie is van kracht: ");
            for (int i=2;i<4;i++){
                System.out.println(bestelling.getBesteldItems().get(i).getAantal() + " " + bestelling.getBesteldItems().get(i).getGerecht().getNaam() + " prijs " + bestelling.getBesteldItems().get(i).getToegepastePrijs() );
            }

            //NORMAAL
            bestelling.setBetaalStrategie(normaleBetaling);
            bestelling.addItem(videe, 2);
            bestelling.addItem(croque, 2);
            System.out.println("De normale betalingsstrategie is opnieuw van kracht: ");
            for (int i=4;i<6;i++){
                System.out.println(bestelling.getBesteldItems().get(i).getAantal() + " " + bestelling.getBesteldItems().get(i).getGerecht().getNaam() + " prijs " + bestelling.getBesteldItems().get(i).getToegepastePrijs() );
            }
            System.out.println("Het totaal van de bestelling is " + bestelling.getTotaal() + " euro");
            System.out.println("####################################################################");
        }
        if (request.getParameter("decoratortest") != null) {
            IngangTeller ingangTeller = IngangTeller.getInstance();
            // een nieuw zaalpersoneelslid toevoegen
            System.out.println("####################################################################");
            Zaalpersoneel manu = new Zaalpersoneel();
            manu.setNaam("Manu");
            ingangTeller.attachObserver(manu);
            ingangTeller.setAantal(7);
            // we gaan manu detachen en hem als poetspersoon attachen zodat hij nog altijd kan reageren op de klantenteller maar daarbij ook kan schoonmaken
            System.out.println("####################################################################");
            ingangTeller.detachObserver(manu);
            ingangTeller.setAantal(10);
            Poetspersoon poetsPersoon = new Poetspersoon();
            poetsPersoon.setPersoneel(manu);
            poetsPersoon.schoonMaken();
            // Manu moet nu ook nog de administratie erbij nemen als iemand binnenkomt
            System.out.println("####################################################################");
            Administrator administrator = new Administrator();
            administrator.setPersoneel(manu);
            ingangTeller.attachObserver(administrator);
            ingangTeller.setAantal(5);
            System.out.println("####################################################################");

        }
        return "index";
    }
}
