package com.example.demo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import model.Narudzbina;

public interface NarudzbinaRepository extends JpaRepository<Narudzbina, Integer> {
	
	@Query("select n from Narudzbina n where n.korisnik1 is null")
	List<Narudzbina> getUnprocessedOrders();
	
	@Modifying
	@Query("update Narudzbina n set n.korisnik1.idKorisnik =:employee where n.idNarudzbina =:id")
	void markOrderAsProcessed(@Param("employee") Integer employee, @Param("id") Integer id);

	@Query("select n from Narudzbina n inner join n.isporukas is where n.korisnik1.idKorisnik =:id and is.datumPripreme =:dat")
	List<Narudzbina> getProcessedOrders(@Param("id") Integer idKorisnik, @Param("dat") Date today);
}
