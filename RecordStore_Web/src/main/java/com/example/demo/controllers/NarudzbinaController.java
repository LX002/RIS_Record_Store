package com.example.demo.controllers;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.exceptions.OutOfUnitsException;
import com.example.demo.services.AlbumService;
import com.example.demo.services.IsporukaService;
import com.example.demo.services.KorisnikService;
import com.example.demo.services.NarudzbinaService;

import jakarta.servlet.http.HttpSession;
import model.Album;
import model.Isporuka;
import model.Korisnik;
import model.Narudzbina;
import model.Stavkanarudzbina;

@Controller
public class NarudzbinaController {
	
	@Autowired
	KorisnikService ks;
	
	@Autowired
	NarudzbinaService ns;
	
	@Autowired
	AlbumService as;
	
	@Autowired
	IsporukaService is;
	
	@GetMapping("/users/order") 
	public String handleOrderItems(Model m, HttpSession session) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		m.addAttribute("order", new Narudzbina());
		//newOrder.setKorisnik2(ks.getKorisnik(auth.getName()));
		
		m.addAttribute("idK", ks.getKorisnik(auth.getName()).getIdKorisnik());
		List<Album> cartSession = (List<Album>) session.getAttribute("cartSession");
		Map<Album, Integer> cartMap = new HashMap<Album, Integer>();
		if(cartSession == null) {
			m.addAttribute("cart", null);
		} else {
			for(Album a : cartSession) {
				if(!cartMap.containsKey(a)) {
					cartMap.put(a, 1);
				} else {
					int count = cartMap.get(a);
					cartMap.put(a, ++count);
				}
			}
			m.addAttribute("cart", cartMap);
			double price = 0.0;
			for(Album a : cartSession) {
				price += a.getCena();
			}
			m.addAttribute("price", price);
		}
		return "order";
	}
	
	@PostMapping("/users/saveOrder")
	public String saveOrder(@RequestParam String rokVazenja, @ModelAttribute Narudzbina order, HttpSession session, Model m) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		order.setKorisnik2(ks.getKorisnik(auth.getName()));
		if(order.getBrKartice() == "" || order.getCvv() == "" || order.getImeNaKartici() == "" || rokVazenja == "") {
			m.addAttribute("success", false);
			m.addAttribute("message", "All fields must be filled!");
			return handleOrderItems(m, session);
		} 
		
		String cardNumberReg = "[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{4}";
		Pattern pattern = Pattern.compile(cardNumberReg);
		Matcher matcher = pattern.matcher(order.getBrKartice());
		if(!matcher.find()) {
			m.addAttribute("success", false);
			m.addAttribute("message", "Invalid card number format!<br>Correct one is xxxx-xxxx-xxxx-xxxx (x is digit)");
			return handleOrderItems(m, session);
		}
		
		String format = "yyyy-MM-dd";
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		dateFormat.setLenient(false);
		Date parsedExpDate; 
		try {
			parsedExpDate = dateFormat.parse(rokVazenja);
		} catch (Exception e) {
			// TODO: handle exception
			m.addAttribute("success", false);
			m.addAttribute("message", "Invalid card expiration date format!<br>Correct one is yyyy-MM-dd");
			return handleOrderItems(m, session);
		}
		LocalDate today = LocalDate.now();
		Date todayDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
		if(todayDate.compareTo(parsedExpDate) > 0) {
			m.addAttribute("success", false);
			m.addAttribute("message", "Your card has expired! Please try again with another!");
			return handleOrderItems(m, session);
		}
		
		if(order.getCvv().length() != 3) {
			m.addAttribute("success", false);
			m.addAttribute("message", "CVV length must be 3!");
			return handleOrderItems(m, session);
		}
		String cvv = order.getCvv();
		for(int i = 0; i < order.getCvv().length(); i++) {
			if(!Character.isDigit(cvv.charAt(i))) {
				m.addAttribute("success", false);
				m.addAttribute("message", "CVV must contain digits only!");
				return handleOrderItems(m, session);
			}
		}
			
		
		@SuppressWarnings("unchecked")
		List<Album> cartSession = (List<Album>) session.getAttribute("cartSession");
		Map<Album, Integer> cartMap = new HashMap<Album, Integer>();
		double price = 0.0;
		for(Album a : cartSession) {
			if(!cartMap.containsKey(a)) {
				cartMap.put(a, 1);
			} else {
				int count = cartMap.get(a);
				cartMap.put(a, ++count);
			}
			price += a.getCena();
		}
		List<Stavkanarudzbina> items = new ArrayList<Stavkanarudzbina>();
		for(Album a : cartMap.keySet()) {
			Stavkanarudzbina sn = new Stavkanarudzbina();
			sn.setAlbum(a);
			sn.setNarudzbina(order);
			sn.setKolicina(cartMap.get(a));
			items.add(sn);
		}
		
		order.setStavkanarudzbinas(items);
		order.setIznos(price);
		order.setRokVazenjaKartice(parsedExpDate);
		int orderId = ns.saveOrder(order);
		if(orderId == -1) {
			m.addAttribute("success", false);
			m.addAttribute("message", "Error: Order was not successfully saved!");
			return handleOrderItems(m, session);
		}
		
		boolean success = false;
		try {
			success = ns.saveOrderItems(items);
		} catch (OutOfUnitsException e) {
			// TODO Auto-generated catch block
			ns.removeOrder(order);
			m.addAttribute("success", false);
			m.addAttribute("message", "Error: " + e.getMessage());
			return handleOrderItems(m, session);
		}
		if(!success) {
			ns.removeOrder(order);
			m.addAttribute("success", false);
			m.addAttribute("message", "Error: Order items were not successfully saved!");
			return handleOrderItems(m, session);
		}
		
		try {
			for(Stavkanarudzbina sn : items) {
				as.updateAlbum(sn.getAlbum().getBrKomNaStanju() - sn.getKolicina(), sn.getAlbum().getIdAlbum());
			}
		} catch (Exception e) {
			// TODO: handle exception
			ns.removeOrder(order);
			m.addAttribute("success", false);
			m.addAttribute("message", "Error: " + e.getMessage());
			return handleOrderItems(m, session);
		}
		session.setAttribute("cartSession", null);
		m.addAttribute("succMessage", "Order and its items are saved successfully!");
		m.addAttribute("idNar", orderId);
		return "order";
	}
	
	@GetMapping("/admin/viewOrders")
	public String viewOrders(Model m) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Korisnik k = ks.getKorisnik(auth.getName());
		m.addAttribute("idKorisnik", k.getIdKorisnik());
		List<Narudzbina> unprocessed =  ns.getUnprocessedOrders();
		m.addAttribute("orders", unprocessed);
		return "viewOrders";
	}
	
	@GetMapping("/admin/viewOrderDetails/{idNarudzbina}")
	public String viewOrderDetails(@PathVariable Integer idNarudzbina, Model m) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Korisnik k = ks.getKorisnik(auth.getName());
		m.addAttribute("idKorisnik", k.getIdKorisnik());
		Isporuka existing = is.getIsporuka(idNarudzbina);
		if(existing != null) {
			m.addAttribute("orders", ns.getUnprocessedOrders());
	        m.addAttribute("success", false);
	        m.addAttribute("message", "Error: the order (ID: " + idNarudzbina + ") was already prepared for delivery!");
	        return "viewOrders";
		}
		m.addAttribute("process", true);
		m.addAttribute("o", ns.getNarudzbina(idNarudzbina));
		List<Stavkanarudzbina> orderItems = ns.getItems(idNarudzbina);
		String[] albumArts = new String[orderItems.size()];
		for(int i = 0; i < albumArts.length; i++) {
			Album a = orderItems.get(i).getAlbum();
			String base64AlbumArt = Base64.getEncoder().encodeToString(a.getAlbumArt());
			albumArts[i] = base64AlbumArt;
		}
		m.addAttribute("albumArts", albumArts);
		m.addAttribute("orderItems", orderItems);
		return viewOrders(m);
	}
}
