package model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;


/**
 * The persistent class for the korisnik database table.
 * 
 */
@Entity
@NamedQuery(name="Korisnik.findAll", query="SELECT k FROM Korisnik k")
public class Korisnik implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idKorisnik;

	private String adresa;

	private String ime;

	private String password;

	private String prezime;

	private String username;

	//bi-directional many-to-one association to Role
	@ManyToOne
	@JoinColumn(name="role_fk")
	private Role role;

	//bi-directional many-to-one association to Narudzbina
	@OneToMany(mappedBy="korisnik1")
	private List<Narudzbina> narudzbinas1;

	//bi-directional many-to-one association to Narudzbina
	@OneToMany(mappedBy="korisnik2")
	private List<Narudzbina> narudzbinas2;

	public Korisnik() {
	}
	
	public Korisnik(String ime, String password, String prezime, String username, Role role, String adresa) {
		// TODO Auto-generated constructor stub
		this.adresa = adresa;
		this.ime = ime;
		this.prezime = prezime;
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public int getIdKorisnik() {
		return this.idKorisnik;
	}

	public void setIdKorisnik(int idKorisnik) {
		this.idKorisnik = idKorisnik;
	}

	public String getAdresa() {
		return this.adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public String getIme() {
		return this.ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPrezime() {
		return this.prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<Narudzbina> getNarudzbinas1() {
		return this.narudzbinas1;
	}

	public void setNarudzbinas1(List<Narudzbina> narudzbinas1) {
		this.narudzbinas1 = narudzbinas1;
	}

	public Narudzbina addNarudzbinas1(Narudzbina narudzbinas1) {
		getNarudzbinas1().add(narudzbinas1);
		narudzbinas1.setKorisnik1(this);

		return narudzbinas1;
	}

	public Narudzbina removeNarudzbinas1(Narudzbina narudzbinas1) {
		getNarudzbinas1().remove(narudzbinas1);
		narudzbinas1.setKorisnik1(null);

		return narudzbinas1;
	}

	public List<Narudzbina> getNarudzbinas2() {
		return this.narudzbinas2;
	}

	public void setNarudzbinas2(List<Narudzbina> narudzbinas2) {
		this.narudzbinas2 = narudzbinas2;
	}

	public Narudzbina addNarudzbinas2(Narudzbina narudzbinas2) {
		getNarudzbinas2().add(narudzbinas2);
		narudzbinas2.setKorisnik2(this);

		return narudzbinas2;
	}

	public Narudzbina removeNarudzbinas2(Narudzbina narudzbinas2) {
		getNarudzbinas2().remove(narudzbinas2);
		narudzbinas2.setKorisnik2(null);

		return narudzbinas2;
	}

}