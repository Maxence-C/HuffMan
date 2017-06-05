
public class Noeud implements Comparable<Noeud>{
	
	private char caractere;
	private int frequence;
	private Noeud nGauche;
	private Noeud nDroit;
	private String code;
	
	public Noeud(char c, int freq){
		this.caractere = c;
		this.frequence = freq;
		this.nGauche = null;
		this.nDroit = null;
		this.code = "";
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
	 * Ajoute un caractère au code du noeud et de ses enfants
	 */
	public void addCode(String s){
		
		this.code = s + this.code;
		
		
		if(this.nDroit != null)
			this.nDroit.addCode(s);
		
		if(this.nGauche != null)
			this.nGauche.addCode(s);
		
	}
	
	public String toString(String s){
		String res = "";
		res += s + this.caractere + " : " + this.frequence + " : " + this.code + "\n";
		
		if(this.nDroit != null)
			res += s + "   " + this.nDroit.toString(s + "   ");
		
		if(this.nGauche != null)
			res += s + "   "+ this.nGauche.toString(s + "   ");
		
		return res;
	}

	/*
	 * Recherche le code d'un caractère
	 */
	public String searchChar(char c) {
		String res = "";
		if(this.caractere == c && this.isFeuille())
			return this.code;
		
		if(this.nDroit != null)
			res = this.nDroit.searchChar(c);
		
		if(res.equals("") && this.nGauche != null)
			res = this.nGauche.searchChar(c);
		
		return res;
	}
	
	/*
	 * Recherche le caractère du noeud dont le code est str
	 */
	public String searchCode(String str){
		
		int i = 0;
		Noeud n = this;
		while(i < str.length()){
			
			//Gauche
			if(str.charAt(i) == '1'){
				n = this.nGauche;
			}else{
				n = this.nDroit;
			}
			
			if(n.isFeuille()){
				return n.caractere + "";
			}
			
		}
		
		return "";
		
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
