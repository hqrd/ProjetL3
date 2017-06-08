package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "OBJET")
public class Objet implements Serializable {
	private static final long	serialVersionUID	= 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Integer				id;
	@Column(name = "INTITULE")
	private String				intitule;
	@Column(name = "QTITEREST")
	private Integer				qtiterest;
	@OneToMany(mappedBy = "objet")
	private List<Emprunt>		emprunts;

	public Objet(Integer id, String intitule, Integer qtiterest) {
		super();
		this.id = id;
		this.intitule = intitule;
		this.qtiterest = qtiterest;
	}

	public Objet() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public Integer getQtiterest() {
		return qtiterest;
	}

	public void setQtiterest(Integer qtiterest) {
		this.qtiterest = qtiterest;
	}

	public List<Emprunt> getEmprunts() {
		return emprunts;
	}

	public void setEmprunts(List<Emprunt> emprunts) {
		this.emprunts = emprunts;
	}

}