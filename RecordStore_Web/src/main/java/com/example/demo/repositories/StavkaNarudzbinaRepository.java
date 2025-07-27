package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import model.Stavkanarudzbina;

public interface StavkaNarudzbinaRepository extends JpaRepository<Stavkanarudzbina, Integer> {
	
	@Query ("select count(sn) from Stavkanarudzbina sn")
	int countStavkanarudzbinas();

	@Query("select sn from Stavkanarudzbina sn where sn.narudzbina.idNarudzbina =:id")
	List<Stavkanarudzbina> getItems(@Param("id") Integer id);

	@Query("select sn from Stavkanarudzbina sn where sn.album.idAlbum =:id")
	List<Stavkanarudzbina> getItemsWithAlbum(@Param("id") Integer idAlbum);
}
