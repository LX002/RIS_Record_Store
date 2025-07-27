package com.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.repositories.AlbumRepository;

import model.Album;

@Service
public class AlbumService {
	
	@Autowired
	AlbumRepository ar;
	
	public int saveAlbum(Album a) {
		try {
			Album saved = ar.save(a);
			return saved.getIdAlbum();
		} catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public List<Album> getAllAlbums() {
		return ar.findAll();
	}
	
	public List<Album> getAllAvailable() {
		return ar.findAllAvailable();
	}
	
	public List<Album> getAllAlbumsSortedBy(String type) {
		switch(type) {
			case "priceAsc":
				return ar.findAllSortedByPriceAsc();
			case "priceDesc":
				return ar.findAllSortedByPriceDesc();
			case "genre":
				return ar.findAllSortedByGenre();
			case "artist":
				return ar.findAllSortedByArtist();
			case "name":
				return ar.findAllSortedByName();
			default:
				return ar.findAll();
		}
	}
	
	public Album getAlbum(Integer id) {
		return ar.findById(id).get();
	}
	
	@Transactional
	public void updateAlbum(int kom, int idAlbum) {
		ar.updateAlbumUnits(kom, idAlbum);
	}
	
	@Transactional
	public void updateAlbumInfo(Album update) {
		ar.updateAlbumInfo(update.getNaziv(), update.getGodIzdanja(), update.getIzvodjac(), update.getBrKomNaStanju(), update.getZanr(), update.getCena(), update.getIdAlbum());
	}
	
	public void deleteAlbum(Album a) {
		ar.delete(a);
	}
}
