package com.example.demo.controllers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.repositories.KorisnikRepository;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.services.KorisnikService;
import com.example.demo.services.RoleService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Korisnik;

@Controller
public class KorisnikController {
	
	@Autowired
	RoleService rs;
	
	@Autowired
	KorisnikService ks;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@GetMapping("/admin/test")
	public String getAdminTest() {
		return "adminTest";
	}
	
	@GetMapping("/users/test2")
	public String getUserTest() {
		return "userTest";
	}
	
	@GetMapping("/getRegisterPage")
	public String getRegisterPage(Model m) {
		m.addAttribute("registered", false);
		return "register";
	}
	
	@PostMapping("/register") 
	public String register(@RequestParam String ime, @RequestParam String prezime, 
						   @RequestParam String username, @RequestParam String password,
						   @RequestParam String repeatedPassword, @RequestParam Integer role, 
						   @RequestParam String adresa, Model m)
	{
		String addressReg = "[A-Za-zŽžŠšĐđĆćČč\\s0-9]+,\\s[0-9]+,\\s[A-Za-zŽžŠšĐđĆćČč\\s0-9]+";
		Pattern pattern = Pattern.compile(addressReg);
		Matcher matcher = pattern.matcher(adresa); 
		if(ime.equals("") || prezime.equals("") || username.equals("") || password.equals("") || repeatedPassword.equals("") || role == 0 || adresa.equals("")) {
			m.addAttribute("validData", false);
			m.addAttribute("message", "All fields must be filled!");
		} else if(ks.getKorisnik(username) != null) {
			m.addAttribute("validData", false);
			m.addAttribute("message", "User with entered username (" + username + ") already exists!");
		} else if(!password.equals(repeatedPassword)) {
			m.addAttribute("validData", false);
			m.addAttribute("message", "Repeated password doesn't match the password!");
		} else if(!matcher.find()) {
			m.addAttribute("validData", false);
			m.addAttribute("message", "Invalid format of user's address!");
		} else {
			Korisnik k = new Korisnik(ime, passwordEncoder.encode(password), prezime, username, rs.findById(role), adresa);
			int id = ks.saveKorisnik(k);
			if(id == -1) {
				m.addAttribute("validData", false);
				m.addAttribute("message", "Database error: registration failed!");
			}
			m.addAttribute("validData", true);
			if(role == 1) {
				m.addAttribute("message", "You're now successfuly registered customer, " + k.getIme());
			} else {
				m.addAttribute("message", "You're now successfuly registered employee, " + k.getIme());
			}
			m.addAttribute("registered", true);
		}
		return "register";
	}
	
	@GetMapping("/users/profile/{idKorisnik}")
	public String getProfile(Model m, @PathVariable Integer idKorisnik) {
		Korisnik korisnik = ks.findById(idKorisnik);
		m.addAttribute("user", korisnik);
		m.addAttribute("idKorisnik", korisnik.getIdKorisnik());
		return "profile";
	}

	@PostMapping("/users/updateProfile/{idKorisnik}")
	public String updateProfile(@PathVariable Integer idKorisnik, Model m, @RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String username, @RequestParam String address) {
		Korisnik korisnik = ks.findById(idKorisnik);
		String addressReg = "[A-Za-zŽžŠšĐđĆćČč\\s0-9]+,\\s[0-9]+,\\s[A-Za-zŽžŠšĐđĆćČč\\s0-9]+";
		Pattern pattern = Pattern.compile(addressReg);
		Matcher matcher = pattern.matcher(address);
		if(address == "" && username == "") {
			m.addAttribute("success", false);
			m.addAttribute("message", "Error: username and address have to be filled!");
			return getProfile(m, idKorisnik);
		}
		
		if(!matcher.find()) {
			m.addAttribute("success", false);
			m.addAttribute("message", "Error: invalid format of address!\nCorrect one is: [street and number], [zip-code], [city]");
			return getProfile(m, idKorisnik);
		}
		
		Korisnik updated = new Korisnik();
		updated.setIdKorisnik(korisnik.getIdKorisnik());
		updated.setAdresa(address);
		updated.setIme(korisnik.getIme());
		updated.setPrezime(korisnik.getPrezime());
		updated.setUsername(username);
		updated.setRole(korisnik.getRole());
		if(oldPassword == "" && newPassword == "") {
			updated.setPassword(korisnik.getPassword());
			if(updated.getUsername().equals(korisnik.getUsername()) && updated.getAdresa().equals(korisnik.getAdresa())) {
				m.addAttribute("success", false);
				m.addAttribute("message", "Error: No changes were made, nothing to update 1!");
				//return "redirect:/users/profile/" + idKorisnik;
				//return getProfile(m, idKorisnik);
				return getProfile(m, idKorisnik);
			}
		} else {
			if(oldPassword != "" && newPassword == "") {
				m.addAttribute("success", false);
				m.addAttribute("message", "Error: empty new password field!");
				return getProfile(m, idKorisnik);
			}
			if(oldPassword == "" && newPassword != "") {
				m.addAttribute("success", false);
				m.addAttribute("message", "Error: empty old password field!");
				return getProfile(m, idKorisnik);
			}
			System.out.println(oldPassword);
			if(!passwordEncoder.matches(oldPassword, korisnik.getPassword())) {
				m.addAttribute("success", false);
				m.addAttribute("message", "Error: wrong old password!");
				return getProfile(m, idKorisnik);
			}
			updated.setPassword(passwordEncoder.encode(newPassword));
			if(updated.getUsername().equals(korisnik.getUsername()) && updated.getAdresa().equals(korisnik.getAdresa()) && updated.getPassword().equals(korisnik.getPassword())) {
				m.addAttribute("success", false);
				m.addAttribute("message", "Error: No changes were made, nothing to update!");
				return getProfile(m, idKorisnik);
			}
		}
		
		if(ks.getKorisnik(username) != null) {
			m.addAttribute("success", false);
			m.addAttribute("message", "Error: user with entered username already exists!");
			return getProfile(m, idKorisnik);
		}
		
		ks.updateKorisnik(updated);
		m.addAttribute("success", true);
		return getProfile(m, idKorisnik);
	}
}
