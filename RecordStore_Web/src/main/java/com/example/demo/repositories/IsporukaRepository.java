package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import model.Isporuka;

public interface IsporukaRepository extends JpaRepository<Isporuka, Integer> {
	
	@Query("select i from Isporuka i where i.narudzbina.idNarudzbina =:id")
	Isporuka getIsporuka(@Param("id") Integer idNarudzbina); 
}
