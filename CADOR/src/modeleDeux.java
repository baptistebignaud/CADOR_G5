import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.Set;
import java.util.TreeSet;


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
	// 4 : Vide
	
	public static final List<Integer> creneaux = new ArrayList<Integer>(Arrays.asList(0,1,2,3,4,5));
	
	public static IntVar kronecker(ArrayList<Integer> integer,IntVar Plannif) {
		IntVar kroneck=model.intVar("kroneck",0,1,true);
		if(integer.contains(Plannif.getValue())) model.arithm(kroneck, "=", 1) ;
		else model.arithm(kroneck, "=", 0);
		return kroneck;	
	}
	
	
	public static int indexage(Set<Integer> D) {

		if (D.contains(0) && D.contains(1) && D.contains(2) && D.contains(3) && D.contains(4)) {
			return 31;
		} else if (D.contains(1) && D.contains(2) && D.contains(3) && D.contains(4)){
			return 30;
		} else if (D.contains(0) && D.contains(2) && D.contains(3) && D.contains(4)) {
			return 29;
		} else if (D.contains(2) && D.contains(3) && D.contains(4)) {
			return 28;
		} else if (D.contains(0) && D.contains(1) && D.contains(3) && D.contains(4)) {
			return 27;
		} else if (D.contains(1) && D.contains(3) && D.contains(4)) {
			return 26;
		} else if (D.contains(0) && D.contains(3) && D.contains(4)) {
			return 25;
		} else if (D.contains(3) && D.contains(4)) {
			return 24;
		} else if (D.contains(0) && D.contains(1) && D.contains(2) && D.contains(4)) {
			return 23;
		} else if (D.contains(1) && D.contains(2) && D.contains(4)) {
			return 22;
		} else if (D.contains(0) && D.contains(2) && D.contains(4)) {
			return 21;
		} else if (D.contains(2) && D.contains(4)) {
			return 20;
		} else if (D.contains(0) && D.contains(1) && D.contains(4)) {
			return 19;
		} else if (D.contains(1) && D.contains(4)) {
			return 18;
		} else if (D.contains(0) && D.contains(4)) {
			return 17;
		} else if (D.contains(4)) {
			return 16;
		} else if (D.contains(0) && D.contains(1) && D.contains(2) && D.contains(3)) {
			return 15;
		} else if (D.contains(1) && D.contains(2) && D.contains(3)) {
			return 14;
		} else if (D.contains(0) && D.contains(2) && D.contains(3)) {
			return 13;
		} else if (D.contains(2) && D.contains(3)) {
			return 12;
		} else if (D.contains(0) && D.contains(1) && D.contains(3)) {
			return 11;
		} else if (D.contains(1) && D.contains(3)) {
			return 10;
		} else if (D.contains(0) && D.contains(3)) {
			return 9;
		} else if (D.contains(3)) {
			return 8;
		} else if (D.contains(0) && D.contains(1) && D.contains(2)) {
			return 7;
		} else if (D.contains(1) && D.contains(2)) {
			return 6;
		} else if (D.contains(0) && D.contains(2)) {
			return 5;
		} else if ( D.contains(2)) {
			return 4;
		} else if (D.contains(0) && D.contains(1)) {
			return 3;
		} else if (D.contains(1)) {
			return 2;
		} else if (D.contains(0)) {
			return 1;
		} else {
			return 0;
		}
	}
	
	public static int[] setToTab(Set<Integer> set ) {
		int[] res = new int[set.size()];
		int index =0;
		for (int num : set) {
			res[index]=num;
			index++;
		}
		return res;
	}
	
	public static Set<Integer> tabToSet(ArrayList<Integer> tab){
		Set<Integer> res = new TreeSet<Integer>();
		for (int value : tab) {
			res.add(value);
		}
		return res;
	}
	
	public static void main(String[] args) {
		int[] horizons= {0,1,2,3,4};
		
		//Paramètres
		
		// Horizon sur lequel on souhaite planifier
		//int H = horizon(horizons);
		int H=7*5;
		
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
		int nbAgents = 0;
		for (int val:contrats) {
			nbAgents += val;
		}
		
		// Temps de travail relatif à chaque type de contrat
		double[] pourcent_contrat = {1,0.9,0.8,0.75,0.7,0.6,0.5};
		
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
		IntVar[][] Plannifs = model.intVarMatrix("Plannification", nbAgents, H, 0,4);
		IntVar[] contrat_agent = model.intVarArray(nbAgents, 0, 6);
		
		BoolVar[][][] DeltaPlannifD = new BoolVar[nbAgents][][];
		for(int i=0; i<nbAgents;i++) {
			DeltaPlannifD[i] = model.boolVarMatrix("DeltaPlannifD["+i+"]",H,32);
		}

		Set<Integer> ints = ImmutableSet.of(0, 1, 2, 3, 4);
		Set<Set<Integer>> ensembleD = Sets.powerSet(ints);
		
		// Affichage de l'ensemble D pour la méthode indexage
		/*
		for (Set<Integer> element : ensembleD) {
			int[] tabElement = setToTab(element);
			for (int entier : tabElement) {
				System.out.println(entier);			
			}	System.out.println("\n");
			
		}*/
		
		for(int k=0; k<nbAgents; k++) {
			for (int j=0; j<H; j++) {
				for (Set<Integer> sousEnsemble : ensembleD) {
					model.member(Plannifs[k][j], setToTab(sousEnsemble)).reifyWith(DeltaPlannifD[k][j][indexage(sousEnsemble)]);
				}
			}
		}
		
		//Contraintes
		
		//Contrainte 3.2
		
		for (int p=0;p<(int)(H/7);p++) {
			for (int k=0; k<nbAgents; k++) {
				
				IntVar[] tabKron= new IntVar[7];
				for (int i=0;i<7;i++) {
					tabKron[i] = model.intVar(0, 1,true);
					model.arithm(tabKron[i], "=", DeltaPlannifD[k][7*p+i][15]).post();;
				}
				
				model.sum(tabKron,"<=",5).post();
			}
		}

		// Contrainte 4.2
			
		for(int k=0;k<nbAgents;k++) {
			for(int j=0;j<H-1;j++) {
				model.arithm(DeltaPlannifD[k][j][indexage(tabToSet(new ArrayList<Integer>(Arrays.asList(3))))],"*",DeltaPlannifD[k][j][indexage(tabToSet(new ArrayList<Integer>(Arrays.asList(2))))],"=", 0).post();
				model.arithm(DeltaPlannifD[k][j+1][indexage(tabToSet(new ArrayList<Integer>(Arrays.asList(0,1))))],"*",DeltaPlannifD[k][j+1][indexage(tabToSet(new ArrayList<Integer>(Arrays.asList(0))))],"=", 0).post();
			}
		}
		
		
		//Contrainte 4.3
		for (int k=0; k<nbAgents; k++){
            for (int p=0; p<H/7; p++) {
            	IntVar[] vars = new IntVar[5];

                for (int j=7*p; j<7*p+4;j++){
                    IntVar[][] tabKron = new IntVar[5][3];
                    tabKron[0][0] = DeltaPlannifD[k][j][indexage(tabToSet(new ArrayList<Integer>(Arrays.asList(4))))];
                    tabKron[0][1] = DeltaPlannifD[k][j+1][indexage(tabToSet(new ArrayList<Integer>(Arrays.asList(2,3,4))))];

                    tabKron[1][0] = DeltaPlannifD[k][j][indexage(tabToSet(new ArrayList<Integer>(Arrays.asList(0))))];
                    tabKron[1][1] = DeltaPlannifD[k][j+1][indexage(tabToSet(new ArrayList<Integer>(Arrays.asList(3,4))))];

                    tabKron[2][0] = DeltaPlannifD[k][j][indexage(tabToSet(new ArrayList<Integer>(Arrays.asList(1))))];
                    tabKron[2][1] = DeltaPlannifD[k][j+1][indexage(tabToSet(new ArrayList<Integer>(Arrays.asList(4))))];

                    tabKron[3][0] = DeltaPlannifD[k][j][indexage(tabToSet(new ArrayList<Integer>(Arrays.asList(2))))];
                    tabKron[3][1] = DeltaPlannifD[k][j+1][indexage(tabToSet(new ArrayList<Integer>(Arrays.asList(4))))];
                    tabKron[3][2] = DeltaPlannifD[k][j+2][indexage(tabToSet(new ArrayList<Integer>(Arrays.asList(0,4))))];

                    tabKron[4][0] = DeltaPlannifD[k][j][indexage(tabToSet(new ArrayList<Integer>(Arrays.asList(3))))];
                    tabKron[4][1] = DeltaPlannifD[k][j+1][indexage(tabToSet(new ArrayList<Integer>(Arrays.asList(4))))];
                    tabKron[4][2] = DeltaPlannifD[k][j+2][indexage(tabToSet(new ArrayList<Integer>(Arrays.asList(0,1,4))))];
                    
                    vars[0] = model.intVar(0, 1, true);
                    model.arithm(tabKron[0][0], "*", tabKron[0][1], "=", vars[0]).post();
                    vars[1] = model.intVar(0, 1, true);
                    model.arithm(tabKron[1][0], "*", tabKron[1][1], "=", vars[1]).post();
                    vars[2] = model.intVar(0, 1, true);
                    model.arithm(tabKron[2][0], "*", tabKron[2][1], "=", vars[2]).post();
                    IntVar var33 = model.intVar(0, 1, true);
                    model.arithm(tabKron[3][1], "+", tabKron[3][2], "=", var33).post();
                    vars[3] = model.intVar(0, 1, true);
                    model.arithm(tabKron[3][0], "*", var33, "=", vars[3]).post();
                    IntVar var44 = model.intVar(0, 1, true);
                    model.arithm(tabKron[4][1], "+", tabKron[4][2], "=", var44).post();
                    vars[4] = model.intVar(0, 1, true);
                    model.arithm(tabKron[4][0], "*", var33, "=", vars[4]).post();
                }
                model.sum(vars, ">=", 1).post();
            }
        }

		//Contrainte 4.4
		for (int p=0;p<(int)(H/7)-1;p++) {
			for (int k=0; k<nbAgents; k++) {
				IntVar[] tabKron= new IntVar[13];
				for (int i=0;i<13;i++) {
					tabKron[i] = DeltaPlannifD[k][7*p+i][indexage(tabToSet(new ArrayList<Integer>(Arrays.asList(4))))];
				}

				model.sum(tabKron,">=",4).post();
			}
		}
		
		/*
		//Met des zéros partout
		for (int p=0;p<(int)(H/7)-2;p++) {
			for (int k=0; k<nbAgents; k++) {
				IntVar[] tabKron= new IntVar[12];
				for (int i=0;i<12;i++) {
					tabKron[i] = model.intVar(0, 1, true);
					model.times(DeltaPlannifD[k][7*p+i][indexage(tabToSet(new ArrayList<Integer>(Arrays.asList(4))))],DeltaPlannifD[k][7*p+i+1][indexage(tabToSet(new ArrayList<Integer>(Arrays.asList(4))))],tabKron[i]).post();
				}
				
				model.sum(tabKron,">=",1).post();
			}
		}
		
		*/
		// Contrainte 5.3.2
		for (int j=0;j<H-1;j++) {
			for (int k=0;k<nbAgents;k++) {
				ArrayList<Integer> domaine = new ArrayList<Integer>();
				domaine.add(4);
				model.arithm(DeltaPlannifD[k][j][indexage(tabToSet(domaine))],"*", DeltaPlannifD[k][j+1][indexage(tabToSet(domaine))], "=", 0).post();;
			}
		}
		
		/*
		// Contrainte 9.1

		for (int k=0; k<nbAgents;k++){
			IntVar[] vars = new IntVar[2*nbDimancheTravailles[k][1]-1];
			for (int p=0; p<H-2*nbDimancheTravailles[k][1]-1;p++){
				for(int i=0; i<2*nbDimancheTravailles[k][1];i++){
				    vars[i]=model.intVar(0,1,true);
	                model.arithm(vars[i],"=",DeltaPlannifD[k][7*(p+i)+5][indexage(tabToSet(new ArrayList<Integer>(Arrays.asList(2))))]).post();
					}
				}
			model.sum(vars,">=",nbDimancheTravailles[k][1]).post();

		}*/

		//Contrainte 10
		
		for (int j=0; j<H; j++){
			
			IntVar[] occurence = new IntVar[4];
			occurence[0] = model.intVar("occurence0", 0, nbAgents,true);
			occurence[1] = model.intVar("occurence1", 0, nbAgents,true);
			occurence[2] = model.intVar("occurence2", 0, nbAgents,true);
			occurence[3] = model.intVar("occurence3", 0, nbAgents,true);

			model.globalCardinality(ArrayUtils.getColumn(Plannifs,j), new int[]{0,1,2,3},occurence, false).post();
			model.arithm(occurence[0], ">=", maquette[0][j%7]).post();
			model.arithm(occurence[1], ">=", maquette[1][j%7]).post();
			model.arithm(occurence[2], ">=", maquette[2][j%7]).post();
			model.arithm(occurence[3], ">=", maquette[3][j%7]).post();
		}

		/*
		// Contrainte 9.2
		for(int i=0; i<7;i++) {
			IntVar[] tabkron = new IntVar[nbAgents];
			for(int k=0; k<nbAgents; k++) {
				ArrayList<Integer> Domaine = new ArrayList<Integer>();
				Domaine.add(i);
				tabkron[k] = kronecker(Domaine, contrat_agent[k]);
			}
			
			model.sum(tabkron,"=",contrats[i]).post();
		}
		
		
		
		
		// Contrainte 9.3
		for(int k=0; k<nbAgents; k++) {
			IntVar[] vars = new IntVar[H];
			ArrayList<Integer> Domaine = new ArrayList<Integer>(Arrays.asList(0,1,2,3));
			 for(int p=0; p<(int)(H/7); p++) {
				for(int j=7*p; j<7*p+7; j++) {
					vars[j] = model.intVar(0,1,true);
					model.arithm(vars[j], "=", DeltaPlannifD[k][j][indexage(tabToSet(Domaine))]).post();
					//vars[j] = DeltaPlannifD[k][j][indexage(tabToSet(Domaine))];
				}
			}
			model.sum(vars,"<=",(int)(Math.floor((45/6)*pourcent_contrat[contrat_agent[k].getValue()]))).post();
		}
		*/
		//Contrainte 10
		
		for (int j=0; j<H; j++){
			
			IntVar[] occurence = new IntVar[4];
			occurence[0] = model.intVar("occurence0", 0, nbAgents,true);
			occurence[1] = model.intVar("occurence1", 0, nbAgents,true);
			occurence[2] = model.intVar("occurence2", 0, nbAgents,true);
			occurence[3] = model.intVar("occurence3", 0, nbAgents,true);
			model.globalCardinality(ArrayUtils.getColumn(Plannifs,j), new int[]{0,1,2,3},occurence, false).post();
			model.arithm(occurence[0], ">=", maquette[0][j%7]).post();
			model.arithm(occurence[1], ">=", maquette[1][j%7]).post();
			model.arithm(occurence[2], ">=", maquette[2][j%7]).post();
			model.arithm(occurence[3], ">=", maquette[3][j%7]).post();
		}
		
		solver.findSolution();
		//Solution mySolution = model.getSolver().findSolution();
		
		String resu="";
		resu+=String.format("%1$-15s","") ;
		String [] jours = {"Lundi", "Mardi", "Mercredi" , "Jeudi" , "Vendredi" , "Samedi", "Dimanche"};
		for (int j=0; j<H; j++){
			resu+=String.format("%1$-15s",jours[j%7]+ ((j/7)+1)) ;
		}
		resu+="\n";
		
		for (int k=0; k<nbAgents; k++){
			resu+=String.format("%1$-15s","Agent "+ k) ;
		
			for (int j=0; j<H; j++){
				String creneau= "";
				if (Plannifs[k][j].getValue()==0) {
					creneau="Matin";
				}
				
				if (Plannifs[k][j].getValue()==1) {
					creneau="Jour";
				}
				
				if (Plannifs[k][j].getValue()==2) {
					creneau="Soir";
				}
				
				if (Plannifs[k][j].getValue()==3) {
					creneau="Nuit";
				}
				
				if (Plannifs[k][j].getValue()==4) {
					creneau="Repos";
				}
				
				resu+=String.format("%1$-15s",creneau +"  ") ;
			}
			resu+="\n";
		} 
		System.out.println(resu);
		solver.showStatistics();
		
	}
}
