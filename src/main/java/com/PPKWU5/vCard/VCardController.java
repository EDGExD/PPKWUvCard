package com.PPKWU5.vCard;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.util.Locale;

@RestController
public class VCardController {

    @GetMapping("/SEARCH")
    public String live(@RequestParam(defaultValue = "empty") String str) throws IOException {



        //String url = "https://panoramafirm.pl/" + str;
        Document doc = Jsoup.connect("https://panoramafirm.pl/" + str).get();
        //doc.select("ul").forEach(System.out::println);

        Elements tabela = doc.select("ul");

        Elements oferty = tabela.select("li");


//        RestTemplate restTemplate = new RestTemplate();
//        String result = restTemplate.getForObject(url, String.class);

        String result = oferty.get(3).toString();

//        Element element = new Element("<button/>");
        for(int i =2 ; i< oferty.size()-24;i++)
        {
//            Elements oferta = oferty.get(i).children();
//            oferty.get(i).append("<button onClick=\"wygeneruj()\">Wygeneruj vCard</button>");
            String link = "http://localhost:6767/vCard?name=";
            String adres = oferty.get(i).select("div.address").text();
            adres = adres.replace(" ", "%20");
//            System.out.println(adres);

            String name = oferty.get(i).select("a.company-name").text();
            name = name.replace(" ", "%20");

            link+= name+"&address=" + adres;
            oferty.get(i).append("<button><a target=\"_blank\" href=" + link +">Wygeneruj vCard</a></button>");

        }

//        oferta.add(element);

        return doc.toString();
    }

    @GetMapping("/vCard")
    public String giveVCard(@RequestParam(defaultValue = "empty") String name, @RequestParam(defaultValue = "empty") String address) throws IOException {

        String card = "BEGIN:VCA <br/>"+
                "VERSION:1.0<br/>" +
                "N:"+name+"<br/>" +
                "FN:"+name+"<br/>"+
                "ADR;WORK;"+address+"<br/>"+
                "TEL;TYPE=work:tel:"+"null"+"<br/>"+
                "EMAIL:"+"null"+"<br/>"+
                "END:VCARD";
        return card;
    }

    @GetMapping("/")
    public String help() {
        return "Aby skorzystać z zaimplementowanej funkcjonalności  jako endpoint należy podać - " +
                "\"/SEARCH?str=(tutaj podajemy poszukiwanego rodzaju działaności)";
    }

}