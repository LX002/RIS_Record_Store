package com.example.demo.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.services.AlbumService;
import com.example.demo.services.KorisnikService;
import com.example.demo.services.NarudzbinaService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import model.Album;
import model.Korisnik;
import model.Stavkanarudzbina;

@Controller
public class AlbumController {
	
	@Autowired
	KorisnikService ks;
	
	@Autowired
	AlbumService as;
	
	@Autowired
	NarudzbinaService ns;
	
	@GetMapping("/users/redirectToStore/{idKorisnik}")
	public String redirectToStore(Model m, HttpSession session, @PathVariable Integer idKorisnik) {
		m.addAttribute("idKorisnik", idKorisnik);
		return getStore(m, session);
	}
	
	@GetMapping("/users/store")
	public String getStore(Model m, HttpSession session) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Korisnik k = ks.getKorisnik(auth.getName());
		m.addAttribute("idKorisnik", k.getIdKorisnik());
		List<Album> albums;
		if(k.getRole().getNaziv().equals("KUPAC")) {
			albums = as.getAllAvailable();
		} else {
			albums = as.getAllAlbums();
		}
		//primenjivanje sortiranja albuma
		if(m.containsAttribute("sortType")) {
			String typeOfSort = (String) m.getAttribute("sortType");
			albums = as.getAllAlbumsSortedBy(typeOfSort);
		}
		
		//primenjaivanje pretrage
		//prolazak kroz svaki if i ako je uslov tacan filtrtra po datom parametru listu albuma
		//koja je prethodno sortirana ili ne
		List<Album> filteredAlbums = new ArrayList<>();
		filteredAlbums.addAll(albums);
		if (m.containsAttribute("nameSearch")) {
		    String nameSearch = (String) m.getAttribute("nameSearch");
		    filteredAlbums = filteredAlbums.stream()
		            .filter(album -> album.getNaziv().toLowerCase().equals(nameSearch.toLowerCase()))
		            .collect(Collectors.toList());
		}

		if (m.containsAttribute("artistSearch")) {
		    String artistSearch = (String) m.getAttribute("artistSearch");
		    filteredAlbums = filteredAlbums.stream()
		            .filter(album -> album.getIzvodjac().toLowerCase().equals(artistSearch.toLowerCase()))
		            .collect(Collectors.toList());
		    System.out.println("filter for adele: " + filteredAlbums);
		}

		if (m.containsAttribute("genreSearch")) {
		    String genreSearch = (String) m.getAttribute("genreSearch");
		    filteredAlbums = filteredAlbums.stream()
		            .filter(album -> album.getZanr().toLowerCase().equals(genreSearch.toLowerCase()))
		            .collect(Collectors.toList());
		}

		if (m.containsAttribute("yearSearch")) {
		    int yearSearch = (int) m.getAttribute("yearSearch");
		    filteredAlbums = filteredAlbums.stream()
		            .filter(album -> album.getGodIzdanja() == yearSearch)
		            .collect(Collectors.toList());
		}

		if (!filteredAlbums.isEmpty()) {
		    albums.clear();
		    albums.addAll(filteredAlbums);
		} 
		m.addAttribute("albums", albums);
		String[] albumArts = new String[albums.size()];
		for(int i = 0; i < albums.size(); i++) {
			Album a = albums.get(i);
			String base64AlbumArt = Base64.getEncoder().encodeToString(a.getAlbumArt());
			albumArts[i] = base64AlbumArt;
		}
		m.addAttribute("albumArts", albumArts); 
		
		List<Album> cartSession = (List<Album>) session.getAttribute("cartSession");
		if(cartSession != null) {
			m.addAttribute("cart", cartSession);
			double price = 0.0;
			for(Album a : cartSession) {
				price += a.getCena();
			}
			m.addAttribute("price", price);
		}
		return "store";
	}
	
	@GetMapping("/users/addToCart/{id}")
	public String addToCart(@PathVariable Integer id, Model m, HttpSession session) {
		Album album = as.getAlbum(id);
		List<Album> cartSession = (List<Album>) session.getAttribute("cartSession");
		if(cartSession == null) {
			cartSession = new ArrayList<>();
			session.setAttribute("cartSession", cartSession);
		}
		cartSession.add(album);
		session.setAttribute("cartSession", cartSession);
		//premesteno u getStore
//		session.setAttribute("cartSession", cartSession);
//		m.addAttribute("cart", cartSession);
//		double price = 0.0;
//		for(Album a : cartSession) {
//			price += a.getCena();
//		}
//		m.addAttribute("price", price);
		return "redirect:/users/store";
		
	}
	
	@GetMapping("/users/removeFromCart/{albumPos}")
	public String removeFromCart(@PathVariable Integer albumPos, Model m, HttpSession session) {
		List<Album> cartSession = (List<Album>) session.getAttribute("cartSession");
		Album toRemove = cartSession.get(albumPos);
		cartSession.remove(toRemove);
		session.setAttribute("cartSession", cartSession);
		return "redirect:/users/store";
		
	}
	
	@GetMapping("/admin/updateAlbum/{idAlbum}")
	public String updateAlbum(@PathVariable Integer idAlbum, Model m) {
		Album a = as.getAlbum(idAlbum);
		String base64AlbumArt = Base64.getEncoder().encodeToString(a.getAlbumArt());
		m.addAttribute("albumArt", base64AlbumArt);
		m.addAttribute("album", a);
		m.addAttribute("updated", new Album());
		return "update";
	}
	
	@PostMapping("/admin/saveChanges/{idAlbum}")
	public String saveChanges(@ModelAttribute Album updated, Model m, @PathVariable Integer idAlbum) {
		Album a = as.getAlbum(idAlbum);
		String base64AlbumArt = Base64.getEncoder().encodeToString(a.getAlbumArt());
//		if(a == null) {
//			m.addAttribute("success", false);
//			m.addAttribute("message", "Error: album not found for ID: " + idAlbum);
//			return "redirect:/admin/updateAlbum/" + idAlbum;
//		}
		
		updated.setIdAlbum(idAlbum);
		if(a.getNaziv().equals(updated.getNaziv()) && a.getCena() == updated.getCena() && a.getBrKomNaStanju() == updated.getBrKomNaStanju() &&
	       a.getIzvodjac().equals(updated.getIzvodjac()) && a.getGodIzdanja() == updated.getGodIzdanja() && a.getZanr().equals(updated.getZanr())) {
			m.addAttribute("albumArt", base64AlbumArt);
			m.addAttribute("success", false);
			m.addAttribute("message", "Warrning: No details were changed!");
			return "update";
			//return "redirect:/admin/updateAlbum/" + idAlbum;
		}
		
		System.out.println("id: " + idAlbum);
		m.addAttribute("albumArt", base64AlbumArt);
		m.addAttribute("success", true);
		as.updateAlbumInfo(updated);
		return "update";
	}
	
	@GetMapping("/admin/insertAlbum")
	public String insertAlbum() {
		return "insertAlbum";
	}
	
	@PostMapping("/admin/saveAlbum")
	public String saveAlbum(@RequestParam String naziv, @RequestParam  String izvodjac, @RequestParam String zanr,
							@RequestParam double cena, @RequestParam int brKomNaStanju, @RequestParam int godIzdanja,
							@RequestParam MultipartFile albumArt, Model m) {
		if(albumArt.isEmpty()) {
			m.addAttribute("success", false);
			m.addAttribute("message", "Error: album art must be selected!");
			return "insertAlbum";
		}
		
		byte[] art;
		try {
			art = albumArt.getBytes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			m.addAttribute("success", false);
			m.addAttribute("message", "Error: " + e.getMessage());
			return "insertAlbum";
		}
		
		Album a = new Album();
		a.setAlbumArt(art);
		a.setBrKomNaStanju(brKomNaStanju);
		a.setCena(cena);
		a.setGodIzdanja(godIzdanja);
		a.setIzvodjac(izvodjac);
		a.setNaziv(naziv);
		a.setZanr(zanr);
		int id = as.saveAlbum(a);
		if(id == -1) {
			m.addAttribute("success", false);
			m.addAttribute("message", "Error: album was not succesfully saved!");
			return "insertAlbum";
		}
		m.addAttribute("success", true);
		return "insertAlbum";
	}
	
	@GetMapping("/users/sortAlbums")
	public String sortAlbums(@RequestParam String sortType, Model m, HttpSession session) {
		m.addAttribute("sortType", sortType);
		return getStore(m, session);
		//return "redirect:/users/store";
	}
	
	@GetMapping("/users/search")
	public String searchAlbum(Model m, HttpSession session, @RequestParam(required = false) String nameSearch, @RequestParam(required = false) String artistSearch, 
			                  @RequestParam(required = false) String genreSearch, @RequestParam(required = false) Integer yearSearch) {
		//String strYearS = yearS.toString();
		if(nameSearch != "" && nameSearch != null) {
			m.addAttribute("nameSearch", nameSearch);
		}
		if(artistSearch != "" && artistSearch != null) {
			m.addAttribute("artistSearch", artistSearch);
		}
		if(genreSearch != "" && genreSearch != null) {
			m.addAttribute("genreSearch", genreSearch);
		}
		if(yearSearch != null) {
			m.addAttribute("yearSearch", yearSearch);
		}
		return getStore(m, session);
	}
	
	@GetMapping("/admin/deleteAlbum/{idAlbum}")
	public String deleteAlbum(@PathVariable Integer idAlbum, Model m, HttpSession session) {
		Album a = as.getAlbum(idAlbum);
		List<Stavkanarudzbina> orderItems = ns.getItemsWithAlbum(idAlbum);
		if(orderItems != null || !orderItems.isEmpty()) {
			ns.deleteItems(orderItems);
		}
		as.deleteAlbum(a);
		m.addAttribute("succDel", true);
		m.addAttribute("deleteMessage", "Album with ID: " + idAlbum + " was deleted succesfully!");
		return getStore(m, session);
	}
}