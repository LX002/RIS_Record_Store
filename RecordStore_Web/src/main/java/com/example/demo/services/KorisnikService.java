package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.repositories.KorisnikRepository;

import model.Korisnik;

@Service
public class KorisnikService {
	
	@Autowired
	KorisnikRepository kr;
	
	public Korisnik getKorisnik(String username) {
		return kr.findByUsername(username);
	}
	
	public Korisnik findById(Integer id) {
		return kr.findById(id).get();
	}
	
	public int saveKorisnik(Korisnik korisnik) {
		try {
			Korisnik k = kr.save(korisnik);
			return k.getIdKorisnik();
		} catch (Exception e) {
			// TODO: handle exception
			return -1;
		}
	}

	@Transactional
	public void updateKorisnik(Korisnik updated) {
		// TODO Auto-generated method stub
		kr.updateUser(updated.getIdKorisnik(), updated.getUsername(), updated.getPassword(), updated.getAdresa());
	}
}
