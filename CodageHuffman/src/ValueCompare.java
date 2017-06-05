import java.util.Comparator;
import java.util.TreeMap;

public class ValueCompare implements Comparator<Noeud>{
	
		@Override
		public int compare(Noeud arg0, Noeud arg1) {
			int f1 = arg0.getFrequence();
			int f2 = arg1.getFrequence();
			if(f1 > f2)
				return 1;
			else if(f1 < f2)
				return -1;
			else
				return 0;
		}

}
