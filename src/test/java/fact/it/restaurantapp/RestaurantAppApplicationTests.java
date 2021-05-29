package fact.it.restaurantapp;

import fact.it.restaurantapp.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class RestaurantAppApplicationTests {

    @Test
    public void a_testSingletonWerking_1() {
        IngangTeller ingangTeller = IngangTeller.getInstance();
        IngangTeller ingangTeller1 = IngangTeller.getInstance();
        assertTrue(ingangTeller == ingangTeller1);
    }

    @Test
    public void b_testObserverPattern_attach_1() {
        IngangTeller ingangTeller = IngangTeller.getInstance();
        ingangTeller.getObservers().clear();
        //een paar personeelsleden
        Zaalpersoneel jan = new Zaalpersoneel();
        jan.setNaam("Jan");
        Zaalpersoneel piet = new Zaalpersoneel();
        piet.setNaam("Piet");
        Keukenpersoneel serge = new Keukenpersoneel();
        serge.setNaam("Serge");
        Keukenpersoneel jeroen = new Keukenpersoneel();
        jeroen.setNaam("Jeroen");

        //we koppelen het zaalpersoneel en het keukenpersoneel als observer aan de ingangTeller
        ingangTeller.attachObserver(jan);
        ingangTeller.attachObserver(piet);
        ingangTeller.attachObserver(serge);
        ingangTeller.attachObserver(jeroen);
        PrintStream defaultSO = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String result;
        System.setOut(new PrintStream(baos));
        try {
            //5 klanten komen binnen
            ingangTeller.setAantal(5);
            BufferedReader br = new BufferedReader(new StringReader(baos.toString()));
            result = br.readLine();
            assertEquals("Ik ben Jan en ga het nodige doen om voor 5 klanten een tafel klaar te maken.", result);
            result = br.readLine();
            assertEquals("Ik ben Piet en ga het nodige doen om voor 5 klanten een tafel klaar te maken.", result);
            result = br.readLine();
            assertEquals("Ik ben Serge en ik begin onmiddellijk met het maken van 5 amuse-gueules!", result);
            result = br.readLine();
            assertEquals("Ik ben Jeroen en ik begin onmiddellijk met het maken van 5 amuse-gueules!", result);
            br.close();
        } catch (Exception e) {
            System.setOut(defaultSO);
            System.out.println("Error while redirection System.out");
        }
        System.setOut(defaultSO);

    }

    @Test
    public void c_testObserverPattern_detach_2() {
        IngangTeller ingangTeller = IngangTeller.getInstance();
        ingangTeller.getObservers().clear();
        //een paar personeelsleden
        Zaalpersoneel jan = new Zaalpersoneel();
        jan.setNaam("Jan");
        Zaalpersoneel piet = new Zaalpersoneel();
        piet.setNaam("Piet");
        Keukenpersoneel serge = new Keukenpersoneel();
        serge.setNaam("Serge");
        Keukenpersoneel jeroen = new Keukenpersoneel();
        jeroen.setNaam("Jeroen");
        //we koppelen het zaalpersoneel en het keukenpersoneel als observer aan de ingangTeller
        ingangTeller.attachObserver(jan);
        ingangTeller.attachObserver(piet);
        ingangTeller.attachObserver(serge);
        ingangTeller.attachObserver(jeroen);
        //we ontkoppelen piet en serge
        ingangTeller.detachObserver(piet);
        ingangTeller.detachObserver(serge);

        PrintStream defaultSO = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String result;
        System.setOut(new PrintStream(baos));
        try {
            //3 klanten komen binnen
            ingangTeller.setAantal(3);
            BufferedReader br = new BufferedReader(new StringReader(baos.toString()));
           result = br.readLine();
            assertEquals("Ik ben Jan en ga het nodige doen om voor 3 klanten een tafel klaar te maken.", result);
            result = br.readLine();
            assertEquals("Ik ben Jeroen en ik begin onmiddellijk met het maken van 3 amuse-gueules!", result);
            result = br.readLine();
            br.close();
        } catch (Exception e) {
            System.setOut(defaultSO);
            System.out.println("Error while redirection System.out");
        }
        System.setOut(defaultSO);

    }

    @Test
    public void d_testStrategyPatternZonderBetaalStrategie_1(){
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
        bestelling.setDatum(new GregorianCalendar());
        bestelling.addItem(videe, 2);
        bestelling.addItem(croque, 3);
        assertEquals(60.0,bestelling.getTotaal(),0.1);


    }
    @Test
    public void e_testStrategyPatternMetBetaalStrategie_2(){
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
        bestelling.addItem(croque, 3);

        //HAPPYHOUR
        bestelling.setBetaalStrategie(happyHourBetaling);
        bestelling.addItem(videe, 5);
        bestelling.addItem(croque, 1);

        //NORMAAL
        bestelling.setBetaalStrategie(normaleBetaling);
        bestelling.addItem(videe, 2);
        bestelling.addItem(croque, 2);

        assertEquals(178.0,bestelling.getTotaal(),0.1);


    }
    @Test
    public void f_testStrategyPatternMetBetaalStrategie_3(){
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
        //HAPPYHOUR
        bestelling.setBetaalStrategie(happyHourBetaling);
        bestelling.addItem(videe, 5);
        bestelling.addItem(croque, 6);
        //NORMAAL
        bestelling.setBetaalStrategie(normaleBetaling);
        bestelling.addItem(videe, 2);
        bestelling.addItem(croque, 3);

        assertEquals(168.0,bestelling.getTotaal(),0.1);
    }

    @Test
    public void g_testDecoratorPattern_Zaal_Admin_1(){
        IngangTeller ingangTeller = IngangTeller.getInstance();
        ingangTeller.getObservers().clear();
        // een nieuw zaalpersoneelslid toevoegen
        Zaalpersoneel manu = new Zaalpersoneel();
        manu.setNaam("ZaalManu");
        //we geven hem extra admin-verantwoordelijkheid
        Administrator administrator = new Administrator();
        administrator.setPersoneel(manu);
        ingangTeller.attachObserver(administrator);

        PrintStream defaultSO = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String result;
        System.setOut(new PrintStream(baos));
        try {
            //5 klanten komen binnen
            ingangTeller.setAantal(7);
            BufferedReader br = new BufferedReader(new StringReader(baos.toString()));
            result = br.readLine();
            assertEquals("Ik ben ZaalManu en ga het nodige doen om voor 7 klanten een tafel klaar te maken.", result);
            result = br.readLine();
            assertEquals("Vervolgens registreer ik de 7 klanten in het klantenbestand.", result);
            br.close();
        } catch (Exception e) {
            System.setOut(defaultSO);
            System.out.println("Error while redirection System.out");
        }
        System.setOut(defaultSO);

    }

    @Test
    public void h_testDecoratorPattern_Keuken_Admin_2(){
        IngangTeller ingangTeller = IngangTeller.getInstance();
        ingangTeller.getObservers().clear();
        // een nieuw keukenpersoneelslid toevoegen
        Keukenpersoneel rob = new Keukenpersoneel();
        rob.setNaam("KeukenRob");
        //we geven hem extra admin-verantwoordelijkheid
        Administrator administrator = new Administrator();
        administrator.setPersoneel(rob);
        ingangTeller.attachObserver(administrator);

        PrintStream defaultSO = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String result;
        System.setOut(new PrintStream(baos));
        try {
            //5 klanten komen binnen
            ingangTeller.setAantal(6);
            BufferedReader br = new BufferedReader(new StringReader(baos.toString()));
            result = br.readLine();
            assertEquals("Ik ben KeukenRob en ik begin onmiddellijk met het maken van 6 amuse-gueules!", result);
            result = br.readLine();
            assertEquals("Vervolgens registreer ik de 6 klanten in het klantenbestand.", result);
            br.close();
        } catch (Exception e) {
            System.setOut(defaultSO);
            System.out.println("Error while redirection System.out");
        }
        System.setOut(defaultSO);

    }

    @Test
    public void i_testDecoratorPattern_Zaal_Poets_3(){
        IngangTeller ingangTeller = IngangTeller.getInstance();
        ingangTeller.getObservers().clear();
        // een nieuw zaalpersoneelslid toevoegen
        Zaalpersoneel jozef = new Zaalpersoneel();
        jozef.setNaam("ZaalJozef");
        //we geven hem een extra taak
        Poetspersoon poetspersoon = new Poetspersoon();
        poetspersoon.setPersoneel(jozef);
        ingangTeller.attachObserver(poetspersoon);

        PrintStream defaultSO = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String result;
        System.setOut(new PrintStream(baos));
        try {
            //7 klanten komen binnen
            ingangTeller.setAantal(7);
            poetspersoon.schoonMaken();
            BufferedReader br = new BufferedReader(new StringReader(baos.toString()));
            result = br.readLine();
            assertEquals("Ik ben ZaalJozef en ga het nodige doen om voor 7 klanten een tafel klaar te maken.", result);
            result = br.readLine();
            assertEquals("Ik ben ZaalJozef en ik ga nu ook schoonmaken", result);
            br.close();
        } catch (Exception e) {
            System.setOut(defaultSO);
            System.out.println("Error while redirection System.out");
        }
        System.setOut(defaultSO);

    }

    @Test
    public void j_testDecoratorPattern_Keuken_Poets_4(){
        IngangTeller ingangTeller = IngangTeller.getInstance();
        ingangTeller.getObservers().clear();
        // een nieuw zaalpersoneelslid toevoegen
        Keukenpersoneel bram = new Keukenpersoneel();
        bram.setNaam("KeukenBram");
        //we geven hem een extra taak
        Poetspersoon poetspersoon = new Poetspersoon();
        poetspersoon.setPersoneel(bram);
        ingangTeller.attachObserver(poetspersoon);

        PrintStream defaultSO = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String result;
        System.setOut(new PrintStream(baos));
        try {
            //7 klanten komen binnen
            ingangTeller.setAantal(8);
            poetspersoon.schoonMaken();
            BufferedReader br = new BufferedReader(new StringReader(baos.toString()));
            result = br.readLine();
            assertEquals("Ik ben KeukenBram en ik begin onmiddellijk met het maken van 8 amuse-gueules!", result);
            result = br.readLine();
            assertEquals("Ik ben KeukenBram en ik ga nu ook schoonmaken", result);
            br.close();
        } catch (Exception e) {
            System.setOut(defaultSO);
            System.out.println("Error while redirection System.out");
        }
        System.setOut(defaultSO);

    }




}
