package com.example.demo.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exceptions.OutOfUnitsException;
import com.example.demo.repositories.AlbumRepository;
import com.example.demo.repositories.NarudzbinaRepository;
import com.example.demo.repositories.StavkaNarudzbinaRepository;

import model.Album;
import model.Narudzbina;
import model.Stavkanarudzbina;

@Service
public class NarudzbinaService {
	
	@Autowired
	NarudzbinaRepository nr;
	
	@Autowired
	StavkaNarudzbinaRepository snr;
	
	@Autowired
	AlbumRepository ar;
	
	public int saveOrder(Narudzbina narudzbina) {
		try {
			Narudzbina n = nr.save(narudzbina);
			return n.getIdNarudzbina();
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("Error during insertion of order:\n" + e.getMessage());
			return -1;
		}
	}
	
	// vraca stavku na novoj narudzbini koja je prekoracila broj komada albuma na stanju
	// (prvu na koju se naidje da je takva)
	private Stavkanarudzbina checkAvailability(List<Stavkanarudzbina> items) {
		for(int i = 0; i < items.size(); i++) {
			int quantity = items.get(i).getKolicina();
			if(items.get(i).getAlbum().getBrKomNaStanju() - quantity < 0)
				return items.get(i);
		}
		return null;
	}
	
	public boolean saveOrderItems(List<Stavkanarudzbina> items) throws OutOfUnitsException {
		Stavkanarudzbina err = checkAvailability(items);
		if(err != null)
			throw new OutOfUnitsException("Error: This item in cart: " + err.getAlbum().getNaziv() + " by " + err.getAlbum().getIzvodjac() 
										   + " has stock state of " + err.getAlbum().getBrKomNaStanju() + " units but you've added " + err.getKolicina() + "!");
		
		boolean success = false;
		try {
			for(Stavkanarudzbina s : items) {
				snr.save(s);
			}
			success = true;
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("Error during insertion of order items:\n" + e.getMessage());
		}
		return success;
	}
	
	public void removeOrder(Narudzbina order) {
		nr.delete(order);
	}
	
	public Long countOrderItems() {
		return snr.count();
	}
	
	public List<Stavkanarudzbina> getItems(Integer idNarudzbina) {
		return snr.getItems(idNarudzbina);
	}
	
	public Narudzbina getNarudzbina(Integer id) {
		return nr.findById(id).get();
	}
	
	public List<Narudzbina> getUnprocessedOrders() {
		return nr.getUnprocessedOrders();
	}
	
	public List<Narudzbina> getProcessedOrders(Integer idKorisnik, Date today) {
		return nr.getProcessedOrders(idKorisnik, today);
	}
	
	@Transactional
	public void updateNarudzbina(Integer employee, Integer id) {
		nr.markOrderAsProcessed(employee, id);
	}
	
	public List<Stavkanarudzbina> getItemsWithAlbum(Integer idAlbum) {
		return snr.getItemsWithAlbum(idAlbum);
	}
	
	public void deleteItems(List<Stavkanarudzbina> itemsToDelete) {
		for(Stavkanarudzbina sn : itemsToDelete) {
			snr.delete(sn);
		}
	}
}
