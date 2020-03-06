import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class modeleDeux {
	static Model model= new Model("modele2");
	static Solver solver= model.getSolver();
	
	public static int horizon(int[] horizons) {
		return 0;	
	}
	
	public static IntVar kronecker(int [] DTilt,IntVar Plannif) {
		IntVar kroneck=model.intVar("kroneck",0,1,true);
		int N=DTilt.length;
		for (int i=0;i<=N;i++) {
			if (Plannif.getValue()==DTilt[i]) {
				model.arithm(kroneck, "=", 1);
			}
		}
		model.arithm(kroneck, "=", 0);
	return kroneck;	
	}
	
		
	public static void main(String[] args) {
		int[] horizons= {1,2,3};
		
		
		
		

		
		
		//Paramètres/
		int H= horizon(horizons);
		int nbAgents=10;
		int [][] maquette= {
				{1,2,2,2,2,0,0},
				{1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1},
				{1,1,1,1,1,0,0},
		};
		double nbJCA= 0.2;
		int nbTpsTravailMax;
		double[] nbDimancheTravaill= {1,1,1,0.75,0.75,0.6,0.6};
		int nbMaxGlissant=8;
		int tempsReposJ=4;
		int reposHebdo=6;
		int jourConge=4;
		
		
		
		//Variables
		IntVar[][] Plannif=model.intVarMatrix("Plannification", nbAgents, H, 0,6);
		
		
		
		//Contraintes
		
		//Contrainte 3.2
		for (int p=0;p<(int)H/7;p++) {
			for (int k=0; k<=nbAgents; k++) {
				IntVar[] tabKron= new IntVar[7];
				for (int i=0;i<7;i++) {
					tabKron[i]=kronecker(new int[] {1,2,3,4,5}, Plannif[k][7*p+i]);
				}
				
				model.sum(tabKron,"<=",5).post();
			}
		}
		
		
		
		//Contrainte 4.1
				for (int p=0;p<(int)H-6;p++) {
					for (int k=0; k<=nbAgents; k++) {
						IntVar[] tabKron= new IntVar[7];
						for (int i=0;i<7;i++) {
							tabKron[i]=kronecker(new int[] {1,2,3,4,5}, Plannif[k][p+i]);
						}
						
						model.sum(tabKron,"<=",8).post();//
					}
				}
				
		
			
				
		//Contrainte 4.4
		for (int p=0;p<((int)H/7)-1;p++) {
			for (int k=0; k<=nbAgents; k++) {
				IntVar[] tabKron= new IntVar[14];
				for (int i=0;i<14;i++) {
					tabKron[i]=kronecker(new int[] {0}, Plannif[k][7*p+i]);
				}
				
				model.sum(tabKron,">=",4).post();
			}
		}
		
		for (int p=0;p<(int)H/7;p++) {
			for (int k=0; k<=nbAgents; k++) {
				IntVar[] tabKron= new IntVar[14];
				for (int i=0;i<14;i++) {
					tabKron[i]=kronecker(new int[] {0}, Plannif[k][7*p+i]);
				}
				
				model.sum(tabKron,">=",4).post();
			}
		}
		
		
		
		
		
	}
	

}
