package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demo.repositories.KorisnikRepository;

import model.Korisnik;

public class CustomKorisnikService implements UserDetailsService {
	
	@Autowired
	KorisnikRepository kr;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Korisnik k = kr.findByUsername(username);
		if(k == null) {
			throw new UsernameNotFoundException("Exception: user not found!");
		}
		UserDetails ud = new CustomKorisnikDetail(k);
		return ud;
	}

}
