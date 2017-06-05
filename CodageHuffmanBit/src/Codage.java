import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;

/*
 * Codage de HuffMan utilisant un flux de bits
 *
 * Ce code fonctionne pour les arbres dont les codes NE dépassent PAS 8 bits
 */
public class Codage {
	
	private Noeud racine;
	
	/*
	 * Obtient les fréquences de chaque caractère ASCII dans la chaine str
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
	 * Créé la liste des noeuds à partir d'un tableau de fréquence
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
	 * Création de l'arbre à partir d'une liste de noeud
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
			n1.addCode(1);
			n2.addCode(0);
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
	 * Code la chaine de caractère et écrit le flux de bit dans le fichier "test" à la racine du projet
	 */
	public void coder(String str) throws IOException{
		char[] chars = str.toCharArray();
		
		BitWriter bw = new BitWriter("test");
		
		int code, taille, log, nbZero;
		
		for(int i = 0; i < chars.length; i++){
			/*
			 * Ecriture du code dans le fichier
			 * Comme un code peut commencer par un ou plusieurs 0, il faut que ces zeros soient bien écrits dans le fichier
			 * Ce qui n'est pas le cas si on écrit directement le code dans le fichier
			 * Le nombre de 0 au début se calcul avec le log 2 du code (on obtient le nombre de bit qui compose le code sans les 0 au début)
			 * et la variable tailleCode du noeud
			 * tailleCode - log = nombre de 0 à ajouter
			 */
			code = this.racine.searchCharEntier(chars[i]);
			taille = this.racine.searchTailleCodeChar(chars[i]);
			log = code == 0 ? 0 : (int)(Math.log(code)/Math.log(2));
			
			nbZero = code == 0 ? taille : taille-(log+1);
			for(int j = 0; j < nbZero; j++){
				bw.writeBit(0);
			}
			bw.writeInt(code);
		}
		bw.close();
	}
	
	/*
	 * Décode une chaine de caractère
	 * Lit chaque bit du flux un à un et descend dans l'arbre
	 * Dès qu'un caractère (feuille de l'arbre) est ateint, on recommence depuis la racine de l'arbre
	 * 
	 * Si le dernier octet du flux ne contient pas que des informations utiles (remplissage de 0 car seuls les premiers bits sont utiles), le ou les derniers caractères du décodage peuvent être incohérents
	 */
	public String decoder() throws IOException{
		
		BitReader br = new BitReader("test");
		
		String res = "";
		Noeud n = this.racine;
		
		int bit = br.readBit();
		//Tant que des bits sont à lire
		while(bit != -1){
			
			//Gauche
			if(bit == 1){
				n = n.getNGauche();	
			//Droite
			}else{
				n = n.getNDroit();
			}
			
			if(n.isFeuille()){
				res += n.getCaractere();
				n = this.racine;
			}
			
			bit = br.readBit();
		}
		br.close();
		return res;
	}
	
	public static void main(String[] args) {
		
		try{
			
		
			String str = "VOICI UN ARBRE DE CODAGE POSSIBLE POUR LA DEMONSTRATION";
			
			Codage c= new Codage(str);
			System.out.println("Arbre" + "\n" + c.racine.toString(""));
			
			System.out.println("Codage de : " + str);
			
			c.coder(str);
			double tailleAvant = str.length();
			System.out.println("Taille avant codage (nb de caractères x un octet) : " + tailleAvant + "o");
			File f = new File("test");
			
			double tailleApres = f.length(); 
			System.out.println("Taille après codage : " + tailleApres + "o");
			System.out.println("Taux de compression : " + (tailleApres/tailleAvant*100) + "%");
			
			
			System.out.println("Decodage : " +  c.decoder());
			
			
		}catch(IOException e){
			e.printStackTrace();
		}
	
	}

}
