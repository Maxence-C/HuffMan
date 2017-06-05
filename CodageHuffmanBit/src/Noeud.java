
public class Noeud implements Comparable<Noeud>{
	
	private char caractere;
	private int frequence;
	private Noeud nGauche;
	private Noeud nDroit;
	private int codeEntier;
	private int tailleCode;
	
	public Noeud(char c, int freq){
		this.caractere = c;
		this.frequence = freq;
		this.nGauche = null;
		this.nDroit = null;
		this.codeEntier = 0;
		this.tailleCode = 0;
	}
	
	public char getCaractere(){
		return this.caractere;
	}
	
	public int getFrequence(){
		return this.frequence;
	}

	@Override
	public int compareTo(Noeud n) {
		
		if(this.frequence > n.frequence)
			return 1;
		else if(this.frequence < n.frequence)
			return -1;
		else 
			return 0;
	}
	
	public void setGauche(Noeud n){
		this.nGauche = n;
	}
	
	public void setDroit(Noeud n){
		this.nDroit = n;
	}
	
	/*
	 * Ajouter un caractère au code du noeud et à ses enfants
	 */
	public void addCode(int i){
		
		if(i == 1)
			this.codeEntier = (this.codeEntier | (1 << this.tailleCode));
		this.tailleCode++;
		
		if(this.nDroit != null)
			this.nDroit.addCode(i);
		
		if(this.nGauche != null)
			this.nGauche.addCode(i);
		
	}
	
	/*
	 * Méthode informative, permet de récupérer le code sous forme entière du noeud en gérant les 0 au début
	 */
	public String codeEntierString(){
		int code = this.codeEntier;
		int taille = this.tailleCode;
		int log = code == 0 ? 0 : (int)(Math.log(code)/Math.log(2));
		
		String res = "";
		for(int i = 0; i < taille - (log+1); i++){
			res += "0";
		}
		res += Integer.toBinaryString(code);
		return res;
	}
	
	public String toString(String s){
		String res = "";
		res += s + this.caractere + " : " + this.frequence + " : " + " (" + this.codeEntier + " : " + this.codeEntierString() + " )" + "\n";
		
		if(this.nDroit != null)
			res += s + "   " + this.nDroit.toString(s + "   ");
		
		if(this.nGauche != null)
			res += s + "   "+ this.nGauche.toString(s + "   ");
		
		return res;
	}
	
	/*
	 * Recherche le code du caractère c
	 * Si le caractère est introuvable retourne -1
	 */
	public int searchCharEntier(char c){
		int res = -1;
		if(this.caractere == c && this.isFeuille())
			return this.codeEntier;
		
		if(this.nDroit != null)
			res = this.nDroit.searchCharEntier(c);
		
		if(res == -1 && this.nGauche != null)
			res = this.nGauche.searchCharEntier(c);
		
		return res;
	}
	
	/*
	 * Recherhce la taille du code du caractère c
	 * Si le caractère est introuvable retourne -1
	 */
	public int searchTailleCodeChar(char c){
		int res = -1;
		if(this.caractere == c && this.isFeuille())
			return this.tailleCode;
		
		if(this.nDroit != null)
			res = this.nDroit.searchTailleCodeChar(c);
		
		if(res == -1 && this.nGauche != null)
			res = this.nGauche.searchTailleCodeChar(c);
		
		return res;
	}

	public boolean isFeuille() {
		return this.nDroit == null && this.nGauche == null;
	}

	public Noeud getNGauche() {
		return this.nGauche;
	}

	public Noeud getNDroit() {
		return this.nDroit;
	}

}
