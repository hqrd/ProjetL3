package entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

@Entity
@Table(name = "EMPRUNT")
public class Emprunt implements Serializable {
	private static final long	serialVersionUID	= 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Integer				id;
	@ManyToOne
	@JoinColumn(name = "ID_USER")
	private Utilisateur			user;
	@ManyToOne
	@JoinColumn(name = "ID_OBJET")
	private Objet				objet;
	@Column(name = "QTITE_EMPRUNT")
	private Integer				qtiteEmprunt;
	@Column(name = "RENDU")
	private Boolean				rendu				= false;
	@Column(name = "DATE_EMPRUNT")
	private Timestamp			dateEmprunt;
	@Column(name = "DATE_RENDU")
	private Timestamp			dateRendu;

	public Emprunt(Integer id, Utilisateur user, Objet objet, Integer qtiteEmprunt, Boolean rendu,
			Timestamp dateEmprunt, Timestamp dateRendu) {
		super();
		this.id = id;
		this.user = user;
		this.objet = objet;
		this.qtiteEmprunt = qtiteEmprunt;
		this.rendu = rendu;
		this.dateEmprunt = dateEmprunt;
		this.dateRendu = dateRendu;
	}

	public Emprunt() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Utilisateur getUser() {
		return user;
	}

	public void setUser(Utilisateur user) {
		this.user = user;
	}

	public Objet getObjet() {
		return objet;
	}

	public void setObjet(Objet objet) {
		this.objet = objet;
	}

	public Integer getQtiteEmprunt() {
		return qtiteEmprunt;
	}

	public void setQtiteEmprunt(Integer qtiteEmprunt) {
		this.qtiteEmprunt = qtiteEmprunt;
	}

	public Boolean getRendu() {
		return rendu;
	}

	public void setRendu(Boolean rendu) {
		this.rendu = rendu;
	}

	public Timestamp getDateEmprunt() {
		return dateEmprunt;
	}

	public void setDateEmprunt(Timestamp dateEmprunt) {
		this.dateEmprunt = dateEmprunt;
	}

	public Timestamp getDateRendu() {
		return dateRendu;
	}

	public void setDateRendu(Timestamp dateRendu) {
		this.dateRendu = dateRendu;
	}

}