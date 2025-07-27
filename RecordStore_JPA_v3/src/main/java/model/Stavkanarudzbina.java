package model;

import java.io.Serializable;
import jakarta.persistence.*;


/**
 * The persistent class for the stavkanarudzbina database table.
 * 
 */
@Entity
@NamedQuery(name="Stavkanarudzbina.findAll", query="SELECT s FROM Stavkanarudzbina s")
public class Stavkanarudzbina implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idStavkaNarudzbina;

	private int kolicina;

	//bi-directional many-to-one association to Album
	@ManyToOne
	@JoinColumn(name="album_fk")
	private Album album;

	//bi-directional many-to-one association to Narudzbina
	@ManyToOne
	@JoinColumn(name="narudzbina_fk")
	private Narudzbina narudzbina;

	public Stavkanarudzbina() {
	}

	public int getIdStavkaNarudzbina() {
		return this.idStavkaNarudzbina;
	}

	public void setIdStavkaNarudzbina(int idStavkaNarudzbina) {
		this.idStavkaNarudzbina = idStavkaNarudzbina;
	}

	public int getKolicina() {
		return this.kolicina;
	}

	public void setKolicina(int kolicina) {
		this.kolicina = kolicina;
	}

	public Album getAlbum() {
		return this.album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	public Narudzbina getNarudzbina() {
		return this.narudzbina;
	}

	public void setNarudzbina(Narudzbina narudzbina) {
		this.narudzbina = narudzbina;
	}

}