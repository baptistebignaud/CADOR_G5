import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class modeleDeux {
	static Model model = new Model("modele2");
	static Solver solver = model.getSolver();
	
	public static int horizon(int[] horizons) {
		return 0;	
	}
	
	// Tableau représentant les créneaux
	// Les valeurs entières se réfèrent chacune à un créneau 
	// selon la répartition suivante :
	// 0 : Matin
	// 1 : Jour
	// 2 : Soir
	// 3 : Nuit
	// 4 : JCA
	// 5 : Vide
	public static final List<Integer> creneaux = new ArrayList<Integer>(Arrays.asList(0,1,2,3,4,5));
	
	public static IntVar kronecker(ArrayList<Integer> integer,IntVar Plannif) {
		IntVar kroneck=model.intVar("kroneck",0,1,true);
		if(integer.contains(Plannif.getValue())) model.arithm(kroneck, "=", 1);
		else model.arithm(kroneck, "=", 0);
	return kroneck;	
	}
	
		
	public static void main(String[] args) {
		int[] horizons= {1,2,3};
		
		//Paramètres
		
		// Horizon sur lequel on souhaite planifier
		//int H = horizon(horizons);
		int H=6;
		int nbAgents=10;
		
		// Données fournies sur les besoins en personnel
		// Maquette[i][j] = besoin en personnel sur le créneau
		// i du jour j
		int [][] maquette = {
				{1,2,2,2,2,0,0},
				{1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1},
				{1,1,1,1,1,0,0},
		};
		
		// Renvoie le nombre de personnes à chaque type de contrat
		int[] contrats = {6,1,3,1,0,0,0};
		
		// Pourcentage de JCA souhaités par jour
		double nbJCA = 0.2;
		
		// Temps maximal de travail hebdomadaire 
		int nbTpsTravailMax = 45;
		
		// Données sur le nombre de dimanche devant être
		// travaillés selon le type de contrat des employés
		int[][] nbDimancheTravailles = {
				{1,1},
				{1,1},
				{1,1},
				{3,4},
				{3,4},
				{3,5},
				{3,5}
		};
		
		// Nombre maximal de créneaux travaillés sur une
		// fenêtre glissante de 7j
		int nbMaxGlissant=8;
		
		// Nombre de jours de repos nécessaires de manière
		// bi-hebdomadaire
		int tempsReposJ=4;
		
		// Nombre de créneaux consécutifs de repos nécessaires
		// de manière hebdomadaire
		int reposHebdo=6;
		
		
		//Variables
		IntVar[][] Plannifs = model.intVarMatrix("Plannification", nbAgents, H, 0,6);
		IntVar[] contrat_agent = model.intVarArray(nbAgents, 0, 6);
		
		//Contraintes
		
		//Contrainte 3.2
		
		for (int p=0;p<(int)(H/7);p++) {
			for (int k=0; k<=nbAgents; k++) {
				IntVar[] tabKron= new IntVar[7];
				for (int i=0;i<7;i++) {
					tabKron[i]=kronecker(new ArrayList<Integer>(Arrays.asList(0,1,2,3,4)), Plannifs[k][7*p+i]);
				}
				
				model.sum(tabKron,"<=",5).post();
			}
		}
		
		
		//Contrainte 4.1
			for (int p=0;p<(int)(H-6);p++) {
				for (int k=0; k<=nbAgents; k++) {
					IntVar[] tabKron= new IntVar[7];
					for (int i=0;i<7;i++) {
						tabKron[i]=kronecker(new ArrayList<Integer>(Arrays.asList(0,1,2,3,4)), Plannifs[k][p+i]);
					}
						
					model.sum(tabKron,"<=",8).post();//
				}
			}
				
		// Contrainte 4.2
			
		for(int k=0;k<nbAgents;k++) {
			for(int j=0;j<H-1;j++) {
				IntVar[][] tabKron = new IntVar[2][2];
				tabKron[0][0] = kronecker(new ArrayList<Integer>(Arrays.asList(3)), Plannifs[k][j]);
				tabKron[1][0] = kronecker(new ArrayList<Integer>(Arrays.asList(0,1)), Plannifs[k][j+1]);
				tabKron[0][1] = kronecker(new ArrayList<Integer>(Arrays.asList(2)), Plannifs[k][j]);
				tabKron[1][1] = kronecker(new ArrayList<Integer>(Arrays.asList(0)), Plannifs[k][j+1]);

				model.arithm(tabKron[0][0],"*",tabKron[0][1],"=",0 ).post();
				model.arithm(tabKron[1][0],"*",tabKron[1][1],"=",0 ).post();
			}
		}
			
		//Contrainte 4.3
		for (int k=0; k<nbAgents; k++){
			for (int p=0; p<H/7; p++) {
				for (int j=7*p; j<7*p+6;j++){
					IntVar[][] tabKron = new IntVar[5][3];
					tabKron[0][0] = kronecker(new ArrayList<Integer>(Arrays.asList(5)), Plannifs[k][j]);
					tabKron[0][1] = kronecker(new ArrayList<Integer>(Arrays.asList(2, 3, 5)), Plannifs[k][j + 1]);

					tabKron[1][0] = kronecker(new ArrayList<Integer>(Arrays.asList(0)), Plannifs[k][j]);
					tabKron[1][1] = kronecker(new ArrayList<Integer>(Arrays.asList(3, 5)), Plannifs[k][j + 1]);

					tabKron[2][0] = kronecker(new ArrayList<Integer>(Arrays.asList(1)), Plannifs[k][j]);
					tabKron[2][2] = kronecker(new ArrayList<Integer>(Arrays.asList(3, 5)), Plannifs[k][j + 1]);

					tabKron[3][0] = kronecker(new ArrayList<Integer>(Arrays.asList(2)), Plannifs[k][j]);
					tabKron[3][1] = kronecker(new ArrayList<Integer>(Arrays.asList(5)), Plannifs[k][j + 1]);
					tabKron[3][2] = kronecker(new ArrayList<Integer>(Arrays.asList(0, 5)), Plannifs[k][j + 2]);

					tabKron[4][0] = kronecker(new ArrayList<Integer>(Arrays.asList(3)), Plannifs[k][j]);
					tabKron[4][1] = kronecker(new ArrayList<Integer>(Arrays.asList(5)), Plannifs[k][j + 1]);
					tabKron[4][2] = kronecker(new ArrayList<Integer>(Arrays.asList(0, 1, 5)), Plannifs[k][j + 2]);

					IntVar[] vars = new IntVar[5];

					vars[0] = model.intVar(0, 1, true);
					model.arithm(tabKron[0][0], "*", tabKron[0][1], "=", vars[0]).post();

					vars[1] = model.intVar(0, 1, true);
					model.arithm(tabKron[1][0], "*", tabKron[1][1], "=", vars[1]).post();

					vars[2] = model.intVar(0, 1, true);
					model.arithm(tabKron[2][0], "*", tabKron[2][2], "=", vars[2]).post();

					IntVar var33 = model.intVar(0, 1, true);
					model.arithm(tabKron[3][1], "+", tabKron[3][2], "=", var33).post();
					vars[3] = model.intVar(0, 1, true);
					model.arithm(tabKron[3][0], "*", var33, "=", vars[3]).post();


					IntVar var44 = model.intVar(0, 1, true);
					model.arithm(tabKron[4][1], "+", tabKron[4][2], "=", var44).post();
					vars[4] = model.intVar(0, 1, true);
					model.arithm(tabKron[4][0], "*", var33, "=", vars[4]).post();

					model.sum(vars, ">=", 1).post();


				}
			}

		}

		//Contrainte 4.4
		for (int p=0;p<((int)H/7)-1;p++) {
			for (int k=0; k<=nbAgents; k++) {
				IntVar[] tabKron= new IntVar[14];
				for (int i=0;i<14;i++) {
					tabKron[i]=kronecker(new ArrayList<Integer>(Arrays.asList(5)), Plannifs[k][7*p+i]);
				}
				
				model.sum(tabKron,">=",4).post();
			}
		}
		
		for (int p=0;p<(int)H/7;p++) {
			for (int k=0; k<=nbAgents; k++) {
				IntVar[] tabKron = new IntVar[14];
				for (int i=0;i<14;i++) {
					ArrayList<Integer> Domaine = new ArrayList<Integer>();
					Domaine.add(6);
					tabKron[i] = kronecker(Domaine, Plannifs[k][7*p+i]);
				}
				
				model.sum(tabKron,">=",4).post();
			}
		}
		
		// Contrainte 5.3.2
		for (int j=0;j<H-1;j++) {
			for (int k=0;k<nbAgents;k++) {
				ArrayList<Integer> domaine = new ArrayList<Integer>();
				domaine.add(5);
				IntVar kron1 = kronecker(domaine,Plannifs[k][j]);
				IntVar kron2 = kronecker(domaine,Plannifs[k][j+1]);
				model.arithm(kron1,"*", kron2, "=", 0).post();;
			}
		}
		

		// Contrainte 9.1
		
		for (int k=0; k<nbAgents;k++){
			IntVar[] vars = new IntVar[2*nbDimancheTravailles[k][1]];
			for (int p=0; p<H-2*nbDimancheTravailles[k][1]-1;p++){
				for(int i=0; i<2*nbDimancheTravailles[k][1];i++){
					vars[i] = kronecker(new ArrayList<Integer>(Arrays.asList(5)), Plannifs[k][7*(p+i)+6]);
				}
			}
			model.sum(vars,">=",nbDimancheTravailles[k][1]).post();
		}
		
		// Contrainte 9.2
		for(int i=0; i<7;i++) {
			IntVar[] tabKron = new IntVar[nbAgents];
			for(int k=0; k<nbAgents; k++) {
				ArrayList<Integer> Domaine = new ArrayList<Integer>();
				Domaine.add(i);
				tabKron[k] = kronecker(Domaine, contrat_agent[k]);
			}
		}
		
		solver.findSolution();
		//Solution mySolution = model.getSolver().findSolution();
		for (int i=0; i<nbAgents; i++){
			for (int j=0; j<H; j++){
				System.out.print(Plannifs[i][j].getValue()+"  ");
			}
			System.out.println();
		}
		solver.showStatistics();

		
		
	}
	

}
