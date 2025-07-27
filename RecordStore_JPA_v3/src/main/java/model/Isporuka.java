package model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.Date;


/**
 * The persistent class for the isporuka database table.
 * 
 */
@Entity
@NamedQuery(name="Isporuka.findAll", query="SELECT i FROM Isporuka i")
public class Isporuka implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idIsporuka;

	private String adresa;

	@Temporal(TemporalType.DATE)
	private Date datumPripreme;

	private String mesto;

	private String postBr;

	//bi-directional many-to-one association to Narudzbina
	@ManyToOne
	@JoinColumn(name="narudzbina_fk")
	private Narudzbina narudzbina;

	public Isporuka() {
	}

	public int getIdIsporuka() {
		return this.idIsporuka;
	}

	public void setIdIsporuka(int idIsporuka) {
		this.idIsporuka = idIsporuka;
	}

	public String getAdresa() {
		return this.adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public Date getDatumPripreme() {
		return this.datumPripreme;
	}

	public void setDatumPripreme(Date datumPripreme) {
		this.datumPripreme = datumPripreme;
	}

	public String getMesto() {
		return this.mesto;
	}

	public void setMesto(String mesto) {
		this.mesto = mesto;
	}

	public String getPostBr() {
		return this.postBr;
	}

	public void setPostBr(String postBr) {
		this.postBr = postBr;
	}

	public Narudzbina getNarudzbina() {
		return this.narudzbina;
	}

	public void setNarudzbina(Narudzbina narudzbina) {
		this.narudzbina = narudzbina;
	}

}