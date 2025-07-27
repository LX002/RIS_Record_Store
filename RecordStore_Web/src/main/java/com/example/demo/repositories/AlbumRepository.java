package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import model.Album;

public interface AlbumRepository extends JpaRepository<Album, Integer> {
	
	@Query("select a from Album a where a.brKomNaStanju > 0")
	List<Album> findAllAvailable();
	
	@Query("select a from Album a where a.brKomNaStanju > 0 order by a.naziv asc")
	List<Album> findAllSortedByName();
	
	@Query("select a from Album a where a.brKomNaStanju > 0 order by a.izvodjac asc")
	List<Album> findAllSortedByArtist();
	
	@Query("select a from Album a where a.brKomNaStanju > 0 order by a.zanr asc")
	List<Album> findAllSortedByGenre();
	
	@Query("select a from Album a where a.brKomNaStanju > 0 order by a.cena asc")
	List<Album> findAllSortedByPriceAsc();
	
	@Query("select a from Album a where a.brKomNaStanju > 0 order by a.cena desc")
	List<Album> findAllSortedByPriceDesc();
	
	@Modifying
	@Query("update Album a set a.brKomNaStanju =:kom where a.idAlbum =:id")
	void updateAlbumUnits(@Param("kom") int kom, @Param("id") int idAlbum);
	
	@Modifying
	@Query("update Album a set a.naziv =:naziv, " +
		   "a.godIzdanja =:godIzdanja, " +
		   "a.izvodjac =:izvodjac, " +
		   "a.brKomNaStanju =:brKomNaStanju, " +
		   "a.zanr =:zanr, " +
		   "a.cena =:cena " +
	       "where a.idAlbum =:id")
	void updateAlbumInfo(@Param("naziv") String naziv, @Param("godIzdanja") int godIzdanja, 
			             @Param("izvodjac") String izvodjac, @Param("brKomNaStanju") int brKomNaStanju, 
			             @Param("zanr") String zanr, @Param("cena") double cena, @Param("id") int idAlbum);
}
