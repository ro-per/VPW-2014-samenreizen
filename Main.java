import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Main {
	private static ArrayList<int[]>[][] uitvoerMatrix;
	private static int cSTAD, STAD;
	private static int[] tempRij, cultuur_lijst;

	public static void checkLink(int s, int d) {
		if (uitvoerMatrix[s][cSTAD - 1] != null) {
			if (uitvoerMatrix[d][cSTAD] == null) {
				uitvoerMatrix[d][cSTAD] = new ArrayList<int[]>();
			}
			vulNieuweStadIn(d);
		}
	}

	public static void vulNieuweStadIn(int stad) {
		for (int[] rij : uitvoerMatrix[STAD][cSTAD - 1]) {
			tempRij = rij.clone();
			if (cultuur_lijst[cSTAD] == stad) {
				tempRij[0]++;
			}
			tempRij[cSTAD + 1] = stad;
			uitvoerMatrix[stad][cSTAD].add(tempRij);
		}
	}

	public static void main(String[] args) {
		// _______________________________ VARIABELEN _______________________________
		// UTILITIES
		Scanner sc = new Scanner(System.in);
		int MIN = Integer.MIN_VALUE;
		// ITERATIE VAN AANTAL REIZEN
		int n, geval, x;
		// STEDEN
		int aantalSteden, startStad, stopStad, cultuur_aantal, voetbal_aantal;
		// VOETBAL
		HashSet<Integer> voetbal_lijst, copy_voetbal;
		// WEGENNET
		int verbindingen_aantal;
		int[][] wegenKaart;

		// UITVOER
		int overnachtingenSamen;
		int link;
		List<Integer> oplossingen = new ArrayList<Integer>();
		boolean mogelijk;
		int[] temp;

		// _______________________________ INVOER _______________________________
		n = sc.nextInt();// **** REGEL 0 ****
		for (geval = 0; geval < n; geval++) {
			// STEDEN _______________________________________________
			aantalSteden = sc.nextInt();// **** REGEL 1 ****
			startStad = sc.nextInt() - 1; // **** REGEL 2 ****
			stopStad = sc.nextInt() - 1; // **** REGEL 3 ****

			// CULTUURSTEDEN_________________________________________
			cultuur_aantal = sc.nextInt() + 2;// **** REGEL 4 ****
			cultuur_lijst = new int[cultuur_aantal];
			cultuur_lijst[0] = startStad; // VOEG STARTSTAD TOE
			cultuur_lijst[cultuur_aantal - 1] = stopStad;// VOEG STOP TOE
			// VOEG CULTUURSTEDEN AAN DE LIJST TOE
			for (int i = 1; i < cultuur_aantal - 1; i++) {
				cultuur_lijst[i] = sc.nextInt() - 1;// **** REGEL 5 ****
			}

			// VOETBALSTADIA_________________________________________
			voetbal_aantal = sc.nextInt();// **** REGEL 6 ****
			voetbal_lijst = new HashSet<Integer>();
			for (int i = 0; i < voetbal_aantal; i++) {
				voetbal_lijst.add(sc.nextInt() - 1);// **** REGEL 7 ****
			}

			// WEGENNET in kaart brengen_____________________________
			verbindingen_aantal = sc.nextInt();// **** REGEL 8 ****
			wegenKaart = new int[verbindingen_aantal][2]; // GRAAF ZONDER GEWICHT
			for (x = 0; x < verbindingen_aantal; x++) {
				wegenKaart[x][0] = sc.nextInt() - 1; // SOURCE
				wegenKaart[x][1] = sc.nextInt() - 1; // DEST
			}

			// INIT BEREKENEN VAN UITVOER ___________________________
			uitvoerMatrix = new ArrayList[aantalSteden][cultuur_aantal];
			uitvoerMatrix[0][0] = new ArrayList<int[]>();
			temp = new int[cultuur_aantal + 1];
			temp[0] = 0;
			temp[1] = startStad;
			uitvoerMatrix[0][0].add(temp);
			// BEREKEN UITVOER ______________________________________
			for (cSTAD = 1; cSTAD < cultuur_aantal; cSTAD++) {
				for (STAD = 0; STAD < aantalSteden; STAD++) {
					if (uitvoerMatrix[STAD][cSTAD - 1] != null) {
						uitvoerMatrix[STAD][cSTAD] = new ArrayList<int[]>();
						// VUL EEN NIEUWE IN (INDIEN CULTUURSTAD == Huidige STAD)
						vulNieuweStadIn(STAD);
					}
				}
				// CONTROLEER ELKE LINK INDIEN DE STAD AANWEZIG IS IN DE LINK
				for (STAD = 0; STAD < aantalSteden; STAD++) {
					for (link = 0; link < verbindingen_aantal; link++) {
						// STAD IS DE SOURCE VAN DE LINK
						if (wegenKaart[link][0] == STAD) {
							checkLink(STAD, wegenKaart[link][1]);
						}
						// STAD IS DE DEST VAN DE LINK
						if (wegenKaart[link][1] == STAD) {
							checkLink(STAD, wegenKaart[link][0]);
						}
					}
				}
			}

			// INIT CONTROLE OP UITVOER _____________________________
			oplossingen = new ArrayList<Integer>();
			overnachtingenSamen = MIN;
			mogelijk = false; // GA ER VAN UIT DAT ER GEEN OPLOSSING IS

			// CONTROLE OP UITVOER __________________________________
			if (uitvoerMatrix[stopStad][cultuur_aantal - 1] != null) {
				// OVERLOOP ALLE BEREKENDE RESULTATEN
				for (int[] resultaat : uitvoerMatrix[stopStad][cultuur_aantal - 1]) {
					copy_voetbal = new HashSet<Integer>(voetbal_lijst);
					for (int i = 0; i < cultuur_aantal; i++) {
						copy_voetbal.remove(resultaat[i + 1]); // = BEZOEK AAN VOETBALSTAD MOGELIJK
					}
					// INDIEN ALLE VOETBALSTEDEN BEZOCHT ZIJN HEBBEN WE MINIMUM ÉÉN OPLOSSING
					if (copy_voetbal.size() == 0) {
						oplossingen.add(resultaat[0]);
						mogelijk = true;
					}
				}

			}
			// MAXIMALISEER OPLOSSING ________________________________
			for (int opl : oplossingen) {
				overnachtingenSamen = Math.max(overnachtingenSamen, opl);// MAXIMALISEREN
			}
			// OUTPUT PRINTEN _______________________________________
			if (mogelijk) {
				System.out.println((geval + 1) + " " + (overnachtingenSamen - 1));

			} else {
				System.out.println((geval + 1) + " onmogelijk");
			}
		}
		sc.close();
	}
}