import java.io.*;

public class QRC {

    FiniteField ffp;
    FiniteField ffl;
    FiniteField ffm;
    int[] quadraticResidues;
    int[] quadraticNonResidues;
    
    public QRC(File f, int p, int l, int m) throws Exception {

	ffp = new FiniteField(p);
	ffl = new FiniteField(l);
	ffm = new FiniteField(m);

	quadraticResidues = new int[];
    }
}
