import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/*
 * Cette classe permet de lire bit à bit dans un fichier
 * Les bits sont lus dans l'ordre
 */
public class BitReader extends FileInputStream {
	
	int buffer = -1;
	int offset = 7;
	
	public BitReader(String s) throws FileNotFoundException{
		super(s);
	}
	
	/*
	 * Lis le prochain bit du flux
	 */
	public int readBit() throws IOException{
		if(buffer == -1 || offset == -1){
			offset = 7;
			buffer = this.read();
			if(buffer == -1) return -1;
		}
		int res = ((buffer & (1 << offset)) >> offset) & 1;
		offset--;
		return res;
	}
	
	public static void main(String[] args){
		
		try {
			
			BitReader br = new BitReader("test");
			System.out.println(br.readBit());
			System.out.println(br.readBit());
			System.out.println(br.readBit());
			System.out.println(br.readBit());
			System.out.println(br.readBit());
			System.out.println(br.readBit());
			System.out.println(br.readBit());
			System.out.println(br.readBit());
			br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
