package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repositories.IsporukaRepository;

import model.Isporuka;

@Service
public class IsporukaService {

	@Autowired
	IsporukaRepository ir;
	
	public int saveIsporuka(Isporuka isporuka) {
		try {
			Isporuka i = ir.save(isporuka);
			return i.getIdIsporuka();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return -1;
		}
	}
	
	public Isporuka getIsporuka(Integer id) {
		return ir.getIsporuka(id);
	}
}
