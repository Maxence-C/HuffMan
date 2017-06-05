import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/*
 * Cette classe permet d'écrire des bits dans un fichier pour le codage de HuffMan
 * Pour optimiser la place, le code d'un caractère de huffman n'est pas écrit sur un entier ou un octet complet pour ne pas perdre de la place (0 inutiles)
 * Seul le nombre de bit nécéssaire au codage de l'entier sera écrit
 * Exemple : 12 ne sera pas écrit 0000 1100 (un octet) mais 1100
 */
public class BitWriter extends FileOutputStream {
	
	
	int offset = 0;
	int buffer = 0;
	
	public BitWriter(String fileName) throws FileNotFoundException{
		
		super(fileName);
		
	}
	
	/*
	 * Ajoute un bit dans le buffer
	 * Cette méthode écrit dans le fichier seulement si le buffer contient 8 bits
	 */
	public void writeBit(int bit) throws IOException {

	    buffer = (buffer << 1) | bit;
	    offset++;
	    if (offset==8) {
	      write(buffer);
	      offset=0;
	      buffer=0;
	    }
	  }
	
	/*
	 * Ecrit un entier dans le fichier en écrivant chaque bit
	 * 
	 */
	public void writeInt(int i) throws IOException{
		int log = (int)(Math.log(i)/Math.log(2));
		int masque = (int)Math.pow(2, log); 
		int index = 0;
		int indexMasque = log;
		if(i <= 65535){
			while((i >> index) != 0 && index < 10){
				this.writeBit((i & masque) >> indexMasque);
				masque>>=1;
				indexMasque--;
				index++;
			}
			
		}
	}
	
	/*
	 * Ecrit un tableau d'entier et flush
	 */
	public void writeIntArray(int[] arr) throws IOException{
		for(int i = 0; i < arr.length; i++){
			this.writeInt(arr[i]);
		}
		this.flush();
	}
	
	/*
	 * Ecrit dans le fichier ce que contient le buffer en le décalant vers la droite pour que le buffer contienne 8 bits
	 */
	public void flush() throws IOException {
	    if (offset!=0) {
	      write(buffer<<(8-offset));
	      offset=0;
	    }
	    super.flush();
	  }
	
	/*
	 * Flush et ferme le flux
	 */
	public void close() throws IOException {
	    flush();
	    super.close();
	  }
	
	public static void main(String arg[]){
		
		try {
			System.out.println("go");
			BitWriter bw = new BitWriter("test");
			int[] arr = {12,150};
			bw.writeIntArray(arr);
			bw.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}

}
