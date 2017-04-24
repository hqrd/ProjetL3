package beans;

public class Utilisateur  {
	private String	nom;
	private String	prenom;
	private String	email;
    private String motDePasse;
   


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

	public String toString()
	{
		return "Nom : "+ nom+ " PSWD : "+motDePasse;
	}
}
