import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;

public class Codage {
	
	private Noeud racine;
	
	/*
	 * Calcule la fréquence des caractères ASCII dans la chaine str
	 */
	public static char[] getFrequence(String str){
		
		char[] c = new char[255];
		
		char[] chaine = str.toCharArray();
		for(int i = 0; i < chaine.length; i++){
			c[chaine[i]]++;
		}
		
		return c;
	}
	
	/*
	 * Créé une liste de noeuds à partir des fréquences contenus dans le tableau c
	 */
	public ArrayList<Noeud> creerNoeud(char[] c){
		
		ArrayList<Noeud> arr = new ArrayList<Noeud>();
		for(int i = 0; i < 255; i++){
			if(c[i] > 0)
				arr.add(new Noeud((char)i, c[i]));
		}
		
		return arr;
	}
	
	/*
	 * Créé un arbre de HuffMan à partir de la liste de noeud
	 */
	public void creerArbre(ArrayList<Noeud> a){
		a.sort(new ValueCompare());
		int i = 0;
		while(a.size() > 1){
			Noeud n1 = a.get(0);
			Noeud n2 = a.get(1);
			
			a.remove(n1);
			a.remove(n2);
			Noeud n3 = new Noeud('$', n1.getFrequence() + n2.getFrequence());
			n1.addCode("1");
			n2.addCode("0");
			
			n3.setGauche(n1);
			n3.setDroit(n2);
			
			int pos = Collections.binarySearch(a, n3);
			
			a.add(pos < 0 ? (-1*(pos+1)) : pos, n3);
			
			i++;
		}
		
		this.racine = a.get(0);
		
	}
	
	/*
	 * Constructeur pour codage avec fréquence dynamique
	 */
	public Codage(String str){
		
		ArrayList<Noeud> a = this.creerNoeud(Codage.getFrequence(str));

		this.creerArbre(a);
		
	}
	
	/*
	 * Constructeur pour codage avec fréquence statique
	 */
	public Codage(ArrayList<Noeud> a){

		this.creerArbre(a);
		
	}
	
	/*
	 * Code la chaine str avec l'arbre de HuffMan
	 * Retourne une chaine de caractère composée d'une succession de 0 et de 1 
	 */
	public String coder(String str){
		char[] chars = str.toCharArray();
		
		String res = "";
		
		for(int i = 0; i < chars.length; i++){
			res += this.racine.searchChar(chars[i]);
		}
		
		return res;
	}
	
	/*
	 * Décode le code contenu dans la chaine str pour obtenir la phrase d'origine
	 */
	public String decoder(String str){
		
		int i = 0;
		String res = "";
		Noeud n = this.racine;
		while(i < str.length()){
			
			//Gauche
			if(str.charAt(i) == '1'){
				n = n.getNGauche();
			}else{
				n = n.getNDroit();
			}
			
			if(n.isFeuille()){
				res += n.getCaractere();
				n = this.racine;
			}
			
			i++;
			
		}
		
		return res;
	}
	
	public static void main(String[] args) {
		
		String str = "VOICI UN ARBRE DE CODAGE POSSIBLE POUR LA DEMONSTRATION";
		
		Codage c= new Codage(str);
		System.out.println("Arbre" + "\n" + c.racine.toString(""));
		
		System.out.println("Codage de : " + str);
		
		
		String codage = c.coder(str);
		System.out.println(codage);
		
		double chaineBase = str.length()*8;
		double chaineCodee = codage.length();
		
		System.out.println("Taille en octet de la chaine de base : "+chaineBase);
		System.out.println("Taille en octet de la chaine codée : "+chaineCodee);
		System.out.println("Taux de compression : " +  (chaineCodee/chaineBase)*100 + "%");
		
		System.out.println("Decodage de : " + codage);
		System.out.println(c.decoder(codage));
	
	}

}
