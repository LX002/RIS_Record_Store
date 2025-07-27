package com.example.demo.controllers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.services.IsporukaService;
import com.example.demo.services.KorisnikService;
import com.example.demo.services.NarudzbinaService;

import model.Isporuka;
import model.Korisnik;
import model.Narudzbina;

@Controller
public class IsporukaController {

	@Autowired
	IsporukaService is;
	
	@Autowired
	NarudzbinaService ns;
	
	@Autowired
	KorisnikService ks;
	
	// In IsporukaController
	@PostMapping("/admin/processOrder/{idNarudzbina}")
	public String processOrder(Model m, @PathVariable Integer idNarudzbina) {
		Isporuka existing = is.getIsporuka(idNarudzbina);
		if(existing != null) {
			m.addAttribute("orders", ns.getUnprocessedOrders());
	        m.addAttribute("success", false);
	        m.addAttribute("message", "Error: the order (ID: " + idNarudzbina + ") was already prepared for delivery!");
	        return "viewOrders";
		}
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    Narudzbina order = ns.getNarudzbina(idNarudzbina);
	    Korisnik customer = order.getKorisnik2();
	    String[] addressInfo = customer.getAdresa().split(",");
	    Korisnik employee = ks.getKorisnik(auth.getName());
	    LocalDate today = LocalDate.now();
	    Date todayDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
	    m.addAttribute("idKorisnik", employee.getIdKorisnik());
	    //promeni ovo kad promenis tabelu!
	    Isporuka isporuka = new Isporuka();
	    isporuka.setNarudzbina(order);
	    //isporuka.setIdNarudzbina(order.getIdNarudzbina());
	    isporuka.setDatumPripreme(todayDate);
	    isporuka.setAdresa(addressInfo[0].trim());
	    isporuka.setMesto(addressInfo[2].trim());
	    isporuka.setPostBr(addressInfo[1].trim());

	    int isporukaId = is.saveIsporuka(isporuka);
	    if (isporukaId == -1) {
	    	m.addAttribute("orders", ns.getUnprocessedOrders());
	        m.addAttribute("success", false);
	        m.addAttribute("message", "Error: the order was not successfully prepared for delivery!");
	        return "viewOrders";
	    }

	    ns.updateNarudzbina(employee.getIdKorisnik(), order.getIdNarudzbina());
	    m.addAttribute("process", false);
	    m.addAttribute("success", true);
	    m.addAttribute("idN", idNarudzbina);

	    List<Narudzbina> unprocessed = ns.getUnprocessedOrders();
	    if (unprocessed != null && !unprocessed.isEmpty()) {
	        m.addAttribute("orders", unprocessed);
	    }

	    return "viewOrders";
	}


}
