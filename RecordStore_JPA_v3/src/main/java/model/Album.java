package model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;


/**
 * The persistent class for the album database table.
 * 
 */
@Entity
@NamedQuery(name="Album.findAll", query="SELECT a FROM Album a")
public class Album implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idAlbum;

	@Lob
	private byte[] albumArt;

	private int brKomNaStanju;

	private double cena;

	private int godIzdanja;

	private String izvodjac;

	private String naziv;

	private String zanr;

	//bi-directional many-to-one association to Stavkanarudzbina
	@OneToMany(mappedBy="album")
	private List<Stavkanarudzbina> stavkanarudzbinas;

	public Album() {
	}

	public int getIdAlbum() {
		return this.idAlbum;
	}

	public void setIdAlbum(int idAlbum) {
		this.idAlbum = idAlbum;
	}

	public byte[] getAlbumArt() {
		return this.albumArt;
	}

	public void setAlbumArt(byte[] albumArt) {
		this.albumArt = albumArt;
	}

	public int getBrKomNaStanju() {
		return this.brKomNaStanju;
	}

	public void setBrKomNaStanju(int brKomNaStanju) {
		this.brKomNaStanju = brKomNaStanju;
	}

	public double getCena() {
		return this.cena;
	}

	public void setCena(double cena) {
		this.cena = cena;
	}

	public int getGodIzdanja() {
		return this.godIzdanja;
	}

	public void setGodIzdanja(int godIzdanja) {
		this.godIzdanja = godIzdanja;
	}

	public String getIzvodjac() {
		return this.izvodjac;
	}

	public void setIzvodjac(String izvodjac) {
		this.izvodjac = izvodjac;
	}

	public String getNaziv() {
		return this.naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getZanr() {
		return this.zanr;
	}

	public void setZanr(String zanr) {
		this.zanr = zanr;
	}

	public List<Stavkanarudzbina> getStavkanarudzbinas() {
		return this.stavkanarudzbinas;
	}

	public void setStavkanarudzbinas(List<Stavkanarudzbina> stavkanarudzbinas) {
		this.stavkanarudzbinas = stavkanarudzbinas;
	}

	public Stavkanarudzbina addStavkanarudzbina(Stavkanarudzbina stavkanarudzbina) {
		getStavkanarudzbinas().add(stavkanarudzbina);
		stavkanarudzbina.setAlbum(this);

		return stavkanarudzbina;
	}

	public Stavkanarudzbina removeStavkanarudzbina(Stavkanarudzbina stavkanarudzbina) {
		getStavkanarudzbinas().remove(stavkanarudzbina);
		stavkanarudzbina.setAlbum(null);

		return stavkanarudzbina;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idAlbum);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Album other = (Album) obj;
		return idAlbum == other.idAlbum;
	}
}