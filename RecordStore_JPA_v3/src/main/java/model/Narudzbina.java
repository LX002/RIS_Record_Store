package model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the narudzbina database table.
 * 
 */
@Entity
@NamedQuery(name="Narudzbina.findAll", query="SELECT n FROM Narudzbina n")
public class Narudzbina implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idNarudzbina;

	private String brKartice;

	private String cvv;

	private String imeNaKartici;

	private double iznos;

	@Temporal(TemporalType.DATE)
	private Date rokVazenjaKartice;

	//bi-directional many-to-one association to Isporuka
	@OneToMany(mappedBy="narudzbina")
	private List<Isporuka> isporukas;

	//bi-directional many-to-one association to Korisnik
	@ManyToOne
	@JoinColumn(name="prodavac_fk")
	private Korisnik korisnik1;

	//bi-directional many-to-one association to Korisnik
	@ManyToOne
	@JoinColumn(name="kupac_fk")
	private Korisnik korisnik2;

	//bi-directional many-to-one association to Stavkanarudzbina
	@OneToMany(mappedBy="narudzbina")
	private List<Stavkanarudzbina> stavkanarudzbinas;

	public Narudzbina() {
	}

	public int getIdNarudzbina() {
		return this.idNarudzbina;
	}

	public void setIdNarudzbina(int idNarudzbina) {
		this.idNarudzbina = idNarudzbina;
	}

	public String getBrKartice() {
		return this.brKartice;
	}

	public void setBrKartice(String brKartice) {
		this.brKartice = brKartice;
	}

	public String getCvv() {
		return this.cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public String getImeNaKartici() {
		return this.imeNaKartici;
	}

	public void setImeNaKartici(String imeNaKartici) {
		this.imeNaKartici = imeNaKartici;
	}

	public double getIznos() {
		return this.iznos;
	}

	public void setIznos(double iznos) {
		this.iznos = iznos;
	}

	public Date getRokVazenjaKartice() {
		return this.rokVazenjaKartice;
	}

	public void setRokVazenjaKartice(Date rokVazenjaKartice) {
		this.rokVazenjaKartice = rokVazenjaKartice;
	}

	public List<Isporuka> getIsporukas() {
		return this.isporukas;
	}

	public void setIsporukas(List<Isporuka> isporukas) {
		this.isporukas = isporukas;
	}

	public Isporuka addIsporuka(Isporuka isporuka) {
		getIsporukas().add(isporuka);
		isporuka.setNarudzbina(this);

		return isporuka;
	}

	public Isporuka removeIsporuka(Isporuka isporuka) {
		getIsporukas().remove(isporuka);
		isporuka.setNarudzbina(null);

		return isporuka;
	}

	public Korisnik getKorisnik1() {
		return this.korisnik1;
	}

	public void setKorisnik1(Korisnik korisnik1) {
		this.korisnik1 = korisnik1;
	}

	public Korisnik getKorisnik2() {
		return this.korisnik2;
	}

	public void setKorisnik2(Korisnik korisnik2) {
		this.korisnik2 = korisnik2;
	}

	public List<Stavkanarudzbina> getStavkanarudzbinas() {
		return this.stavkanarudzbinas;
	}

	public void setStavkanarudzbinas(List<Stavkanarudzbina> stavkanarudzbinas) {
		this.stavkanarudzbinas = stavkanarudzbinas;
	}

	public Stavkanarudzbina addStavkanarudzbina(Stavkanarudzbina stavkanarudzbina) {
		getStavkanarudzbinas().add(stavkanarudzbina);
		stavkanarudzbina.setNarudzbina(this);

		return stavkanarudzbina;
	}

	public Stavkanarudzbina removeStavkanarudzbina(Stavkanarudzbina stavkanarudzbina) {
		getStavkanarudzbinas().remove(stavkanarudzbina);
		stavkanarudzbina.setNarudzbina(null);

		return stavkanarudzbina;
	}

}