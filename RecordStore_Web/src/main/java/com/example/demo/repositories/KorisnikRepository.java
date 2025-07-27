package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import model.Korisnik;

public interface KorisnikRepository extends JpaRepository<Korisnik, Integer>{
	
	@Query("select k from Korisnik k where k.username = :username")
	public Korisnik findByUsername(@Param("username") String username);

	@Modifying
	@Query("update Korisnik k set k.username =:u, k.password =:p, k.adresa =:a where k.idKorisnik =:id")
	public void updateUser(@Param("id") Integer idKorisnik, @Param("u") String username, @Param("p") String password, @Param("a") String adresa);
}
