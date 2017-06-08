package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "UTILISATEUR")
public class Utilisateur implements Serializable {
	private static final long	serialVersionUID	= 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Integer				id;
	@Column(name = "NOM")
	private String				nom;
	@Column(name = "PRENOM")
	private String				prenom;
	@Column(name = "EMAIL")
	private String				email;
	@Column(name = "PSWDENC")
	private String				motDePasse;
	@Column(name = "ROLE")
	private String				role;
	@OneToMany(mappedBy = "user")
	private List<Emprunt>		emprunts;

	public Utilisateur(String nom, String prenom, String email, String motDePasse, String role) {
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.motDePasse = motDePasse;
		this.role = role;
	}

	public Utilisateur() {
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public String getPrenom() {
		return this.prenom;
	}

	public String getEmail() {
		return this.email;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getNom() {
		return this.nom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String toString() {
		return "Email : " + email + " nom : " + nom + " prenom : " + prenom;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isAdmin() {
		return (this.getRole().equals("admin"));
	}

	public List<Emprunt> getEmprunts() {
		return emprunts;
	}

	public void setEmprunts(List<Emprunt> emprunts) {
		this.emprunts = emprunts;
	}

}