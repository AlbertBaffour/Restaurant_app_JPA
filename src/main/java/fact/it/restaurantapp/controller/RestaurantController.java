package fact.it.restaurantapp.controller;

import fact.it.restaurantapp.model.*;
import fact.it.restaurantapp.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class RestaurantController {


    private PersoneelRepository personeelRepository;
    private GerechtRepository gerechtRepository;
    private TafelRepository tafelRepository;
    private BestellingRepository bestellingRepository;
    private BestelItemRepository bestelItemRepository;
    private Bestelling currentBestelling;
    private List<BesteldItem> currentBesteldItems =new ArrayList<>();

    public RestaurantController(PersoneelRepository personeelRepository,GerechtRepository gerechtRepository,
                                TafelRepository tafelRepository,BestellingRepository bestellingRepository,
                                BestelItemRepository bestelItemRepository){
        this.personeelRepository=personeelRepository;
        this.gerechtRepository=gerechtRepository;
        this.tafelRepository=tafelRepository;
        this.bestellingRepository=bestellingRepository;
        this.bestelItemRepository=bestelItemRepository;
    }

    @RequestMapping
    public String index(Model model){
        return "index";
    }

    @RequestMapping("/personeelsleden")
    public String personeelsleden (Model model){
        List<Personeel> list =personeelRepository.findAll();
        model.addAttribute("personeels",list);
        return "personeelsleden";
    }

  @RequestMapping("/addPersoneel")
    public String addPersoneel (Model model){
      return "addPersoneel";
    }

    @RequestMapping("/processAddPersoneel")
    public String processAddPersoneel(Model model, HttpServletRequest request){
        String naam,personeelstype;
        try{ naam=request.getParameter("naam");
            if(naam.trim().equals(""))throw new Exception();
        } catch (Exception exc){
        model.addAttribute("message","U hebt geen naam ingegeven!");
        return "error";
        }
        try { personeelstype = request.getParameter("personeelstype");
            if(personeelstype.trim().equals(""))throw new Exception();
        }catch (Exception exc){
            model.addAttribute("message", "U hebt geen personeelstype gekozen!");
            return "error";
        }
       createPersoneel(personeelstype,naam);
        List<Personeel> list =personeelRepository.findAll();
        model.addAttribute("personeels",list);
        return "personeelsleden";
    }

    void createPersoneel(String personeelstype,String naam){
        if (personeelstype.equals("Zaalpersoneel")){
            Personeel personeel =new Zaalpersoneel();
            personeel.setNaam(naam);
            personeelRepository.save(personeel);
        }

        if (personeelstype.equals("Keukenpersoneel")){
            Personeel personeel =new Keukenpersoneel();
            personeel.setNaam(naam);
            personeelRepository.save(personeel);
        }
    }

    @RequestMapping("/bestellingen")
    public String bestellingen (Model model){
        List<Bestelling> list =bestellingRepository.findAll();
        model.addAttribute("bestellingen",list);
        return "bestellingen";
    }
    @RequestMapping("/bestelling")
    public String bestelling (HttpServletRequest request, Model model){
        Long bestellingId = Long.parseLong(request.getParameter("bestellingId"));
        Bestelling bestelling = bestellingRepository.getOne(bestellingId);
        List<BesteldItem> besteldItems= bestelItemRepository.findAllByBestellingId(bestellingId);
        model.addAttribute("bestelling",bestelling);
        model.addAttribute("besteldeItems",besteldItems);
        return "bestelling";
    }
    @RequestMapping("/addBestelling")
    public String addBestelling (Model model){
        List<Personeel> zaalpersoneels= personeelRepository.findAllZaalPersoneels();
        List<Tafel> tafels =tafelRepository.findAll();
        model.addAttribute("zaalpersoneels",zaalpersoneels);
        model.addAttribute("tafels",tafels);
        return "addBestelling";
    }
    @RequestMapping("/processAddBestelling")
    public String processAddBestellling(Model model, HttpServletRequest request){
        Long personeelId,tafelId;
        try{ personeelId = Long.parseLong(request.getParameter("zaalpersoneel"));
        } catch (Exception exc){
            model.addAttribute("message","U hebt geen zaalpersoneel geselecteerd!");
            return "error";
        }
        try { tafelId = Long.parseLong(request.getParameter("tafel"));
        } catch (Exception exc){
            model.addAttribute("message","U hebt geen tafel geselecteerd!");
            return "error";
        }
        createBestelling(personeelId,tafelId);
        List<Gerecht> gerechts =gerechtRepository.findAll();
        model.addAttribute("gerechts",gerechts);
        return "addBesteldItem";
    }

    private void createBestelling(Long personeelId, Long tafelId) {
        Zaalpersoneel zaalpersoneel= (Zaalpersoneel) personeelRepository.findById(personeelId).get();
        Tafel tafel= tafelRepository.getOne(tafelId);
        Bestelling bestelling=new Bestelling();
        bestelling.setZaalpersoneel(zaalpersoneel);
        bestelling.setTafel(tafel);
        bestelling.setDatum(new GregorianCalendar());
        bestelling.setBetaald(false);
        currentBestelling = bestelling;
    }
    @RequestMapping("/addBesteldItem")
    public String addBesteldItem(Model model, HttpServletRequest request){
        Long gerechtId; Integer aantal;
        try{
            gerechtId = Long.parseLong(request.getParameter("gerecht"));
        } catch (Exception exc){
            model.addAttribute("message","U hebt geen gerecht geselecteerd!");
            return "error";
        }
        try {
            aantal = Integer.parseInt(request.getParameter("aantal"));
        } catch (NumberFormatException exc){
            model.addAttribute("message","U hebt geen hoeveelheid ingegeven!");
            return "error";
        }
        Boolean happyHour = (request.getParameter("happyHour"))!=null;
        createBesteldItem(gerechtId,aantal,happyHour);
        List<Gerecht> gerechts =gerechtRepository.findAll();
        model.addAttribute("gerechts",gerechts);
        model.addAttribute("bestelling",currentBestelling);
        model.addAttribute("currentBesteldItems",currentBesteldItems);
        return "addBesteldItem";
    }
    private void createBesteldItem(Long gerechtId,Integer aantal,Boolean happyHour){
        Gerecht gerecht= gerechtRepository.getOne(gerechtId);
        BesteldItem besteldItem=new BesteldItem();
        besteldItem.setGerecht(gerecht);
        besteldItem.setAantal(aantal);
        if (happyHour){
            BetaalStrategie betaalStrategie =new HappyHourBetaling();
            currentBestelling.setBetaalStrategie(betaalStrategie);
            besteldItem.setToegepastePrijs(betaalStrategie.getToegepastePrijs(gerecht.getActuelePrijs()));
        }else {
            besteldItem.setToegepastePrijs(gerecht.getActuelePrijs());
        }
        currentBesteldItems.add(besteldItem);
        besteldItem.setBestelling(currentBestelling);
        currentBestelling.setBesteldItems(currentBesteldItems);
    }
    @RequestMapping("/bestellingAfronden")
    public String bestellingAfronden (Model model){
        bestellingRepository.save(currentBestelling);
        for (BesteldItem besteldItem:currentBesteldItems) {
            bestelItemRepository.save(besteldItem);
        }
        currentBestelling = new Bestelling();
        currentBesteldItems =new ArrayList<>();
        List<Bestelling> list =bestellingRepository.findAll();
        model.addAttribute("bestellingen",list);
        return "bestellingen";
    }
    @RequestMapping("/betaling")
    public String betaling (HttpServletRequest request, Model model){
        Long bestellingId = Long.parseLong(request.getParameter("bestellingId"));
        Bestelling bestelling = bestellingRepository.getOne(bestellingId);
        bestelling.setBetaald(true);
        bestellingRepository.save(bestelling);
        List<Bestelling> bestellingen =bestellingRepository.findAll();
        model.addAttribute("bestellingen",bestellingen);
        return "bestellingen";
    }
    @RequestMapping("/zoeken")
    public String zoeken (Model model){
        List<Tafel> tafels =tafelRepository.findAll();
        model.addAttribute("tafels",tafels);
        return "zoekpagina";
    }
    @RequestMapping("/zoekactie")
    public String zoekactie (Model model, HttpServletRequest request) throws ParseException {
        String tafel=request.getParameter("tafel");
        String datum=request.getParameter("datum");
        Double bedrag =null ;
        if (!(request.getParameter("totaal")).trim().equals("")){
            try {
               bedrag = Double.parseDouble(request.getParameter("totaal"));
            }catch (NumberFormatException exc){
                model.addAttribute("message","Bedrag moet een getal zijn!");
                return "error";
            }
        }
        List<Bestelling> bestellingen =bestellingRepository.findAll();
        bestellingen = filterBestellingen(bestellingen,tafel,datum,bedrag);
        if (bestellingen.size()<1)model.addAttribute("feedbacktekst","Geen bestellingen gevonden voor deze zoekcriteria");
        model.addAttribute("bestellingen",bestellingen);
        return "bestellingen";
    }

    private List<Bestelling> filterBestellingen(List<Bestelling> bestellingen,
                                                String tafel, String datum,Double min_bedrag) throws ParseException {
        if (!tafel.equals("")){
        bestellingen = bestellingen.stream()
                .filter(b-> b.getTafel().getId().equals(Long.parseLong(tafel))).collect(Collectors.toList());}
        if (!datum.equals("")) {
        //Datum omzetten naar Gregorian formaat
         DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        GregorianCalendar cal =new GregorianCalendar();
        cal.setTime(format.parse(datum));
            bestellingen = bestellingen.stream()
                    .filter(b -> b.getDatum().after(cal)).collect(Collectors.toList());
        }
        if (min_bedrag!=null) {
            bestellingen = bestellingen.stream()
                    .filter(b -> b.getTotaal() >= min_bedrag).collect(Collectors.toList());
        }
    return bestellingen;}

    @RequestMapping("/addDummyData")
    public String addDummyData(Model model){

        //een paar personeelsleden
        Zaalpersoneel jan = new Zaalpersoneel();
        jan.setNaam("Jan Jann");
        Zaalpersoneel piet = new Zaalpersoneel();
        piet.setNaam("Piet Merceles");
        Keukenpersoneel serge = new Keukenpersoneel();
        serge.setNaam("Serge Gillis");
        Keukenpersoneel jeroen = new Keukenpersoneel();
        jeroen.setNaam("Jeroen Lauwers");
        Keukenpersoneel joeri = new Keukenpersoneel();
        joeri.setNaam("Joeri Maes");
        personeelRepository.save(jan);
        personeelRepository.save(piet);
        personeelRepository.save(serge);
        personeelRepository.save(jeroen);
        personeelRepository.save(joeri);
        //Tafels
        Tafel t1 = new Tafel();
        t1.setCode("t1");
        Tafel t2 = new Tafel();
        t2.setCode("t2");
        Tafel t3 = new Tafel();
        t3.setCode("t3");
        Tafel t4 = new Tafel();
        t4.setCode("t4");
        Tafel t5 = new Tafel();
        t5.setCode("t5");
        tafelRepository.save(t1);
        tafelRepository.save(t2);
        tafelRepository.save(t3);
        tafelRepository.save(t4);
        tafelRepository.save(t5);
        //Gerechten
        Gerecht gerecht1 = new Gerecht();
        gerecht1.setNaam("Mezze");
        gerecht1.setActuelePrijs(30.99);
        Gerecht gerecht2 = new Gerecht();
        gerecht2.setNaam("Zuiders");
        gerecht2.setActuelePrijs(24.99);
        Gerecht gerecht3 = new Gerecht();
        gerecht3.setNaam("Wereldhapjes ");
        gerecht3.setActuelePrijs(24.75);
        Gerecht gerecht4 = new Gerecht();
        gerecht4.setNaam("Pizza");
        gerecht4.setActuelePrijs(28.00);
        Gerecht gerecht5 = new Gerecht();
        gerecht5.setNaam("Pasta");
        gerecht5.setActuelePrijs(15.99);
        gerechtRepository.save(gerecht1);
        gerechtRepository.save(gerecht2);
        gerechtRepository.save(gerecht3);
        gerechtRepository.save(gerecht4);
        gerechtRepository.save(gerecht5);
        //Bestelling

        GregorianCalendar d1 = new GregorianCalendar();
        d1.set(2020,1,21);
        Bestelling b1 = new Bestelling();
        b1.setBetaald(false);
        b1.setDatum(d1);
        b1.setTafel(t1);
        b1.setZaalpersoneel(jan);

        GregorianCalendar d2 = new GregorianCalendar();
        d2.set(2020,2,23);
        Bestelling b2 = new Bestelling();
        b2.setBetaald(true);
        b2.setDatum(d2);
        b2.setTafel(t2);
        b2.setZaalpersoneel(piet);

        GregorianCalendar d3 = new GregorianCalendar();
        d3.set(2020,4,25);
        Bestelling b3 = new Bestelling();
        b3.setBetaald(false);
        b3.setDatum(d3);
        b3.setTafel(t3);
        b3.setZaalpersoneel(jan);

        GregorianCalendar d4 = new GregorianCalendar();
        d4.set(2020,6,30);
        Bestelling b4 = new Bestelling();
        b4.setBetaald(true);
        b4.setDatum(d4);
        b4.setTafel(t4);
        b4.setZaalpersoneel(piet);

        GregorianCalendar d5 = new GregorianCalendar();
        d5.set(2020,9,2);
        Bestelling b5 = new Bestelling();
        b5.setBetaald(false);
        b5.setDatum(d5);
        b5.setTafel(t5);
        b5.setZaalpersoneel(jan);
        bestellingRepository.save(b1);
        bestellingRepository.save(b2);
        bestellingRepository.save(b3);
        bestellingRepository.save(b4);
        bestellingRepository.save(b5);


        BesteldItem s1 = new BesteldItem();
        s1.setAantal(1);
        s1.setToegepastePrijs(gerecht1.getActuelePrijs());
        s1.setBestelling(b1);
        s1.setGerecht(gerecht1);
        BesteldItem s2 = new BesteldItem();
        s2.setAantal(2);
        b2.setBetaalStrategie(new HappyHourBetaling());
        s2.setToegepastePrijs(b2.getBetaalStrategie().getToegepastePrijs(gerecht2.getActuelePrijs()));
        s2.setBestelling(b2);
        s2.setGerecht(gerecht2);
        BesteldItem s3 = new BesteldItem();
        s3.setAantal(3);
        s3.setToegepastePrijs(gerecht3.getActuelePrijs());
        s3.setBestelling(b3);
        s3.setGerecht(gerecht3);
        BesteldItem s4 = new BesteldItem();
        s4.setAantal(4);
        s4.setToegepastePrijs(gerecht4.getActuelePrijs());
        s4.setBestelling(b4);
        s4.setGerecht(gerecht4);
        BesteldItem s5 = new BesteldItem();
        s5.setAantal(5);
        s5.setToegepastePrijs(gerecht5.getActuelePrijs());
        s5.setBestelling(b5);
        s5.setGerecht(gerecht5);
        BesteldItem s6 = new BesteldItem();
        s6.setAantal(3);
        s6.setToegepastePrijs(gerecht2.getActuelePrijs());
        s6.setBestelling(b1);
        s6.setGerecht(gerecht2);
        bestelItemRepository.save(s1);
        bestelItemRepository.save(s2);
        bestelItemRepository.save(s3);
        bestelItemRepository.save(s4);
        bestelItemRepository.save(s5);
        bestelItemRepository.save(s6);


        String feedbacktekst = "Tabellen zijn opgevuld!";
    model.addAttribute("feedbacktekst", feedbacktekst);
        return "index";
    }



}
