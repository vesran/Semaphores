import java.util.*;
import java.io.*;
import java.math.*;


class Simulation {
	Data data;
    Set<String> situations = new HashSet<>();

    public Simulation(Data data) {
        this.data = data;
    }

	public String toString() {
		return this.situations.toString();
	}

	public static boolean isAllBlocked(List<Processus> proc) {
		for (Processus p : proc) {
			if (p.blockedBy == null) {
				return false;
			}
		}
		return ((proc.size() == 0) ? false : true);
	}

    public void simulate(List<Processus> proc) {
    	Processus currentP = ((proc.size() > 0) ? proc.get(0) : new Processus());	// l'elu
		Random rand = new Random();
		Result res = new Result();

    	while (proc.size() > 0 && !Simulation.isAllBlocked(proc)) {
			do {
    			currentP = proc.get(rand.nextInt(proc.size()));		// Tire un processus au hasard
			} while (currentP.blockedBy != null);

    		res = currentP.execute(rand.nextInt(5) + 1);
			situations.add(res.toString());
			this.update(proc);
    	}
		if (Simulation.isAllBlocked(proc)) {
			Simulation.display(proc);
			System.out.println("Tous les processus sont bloques. La simulation a ete arretee. Appuyer sur Entree pour passer a la prochaine simulation.");
          	new java.util.Scanner(System.in).nextLine();
		}
    }

	public static void display(List<Processus> proc) {
		for (Processus p : proc) {
			System.out.println(p);
		}
	}

	private void update(List<Processus> proc) {
		Iterator<Processus> it = proc.iterator();
		while (it.hasNext()) {
			Processus p = it.next();
			if (p.end) {														// Le processus a terminer sa tache
				it.remove();
			} else if (p.blockedBy != null && p.blockedBy.tickets > 0) {		// Le processus a au moins une instruction a faire
				p.blockedBy = null;
			}
		}
	}

    public static void main(String [] args) {
    	System.out.println("Recuperation des donnees...\n");
		if (args.length <= 0) {
			System.out.println("Pas de fichier en entree.");
			return;

		} else if (args.length >= 2) {
			System.out.println("Trop de fichier en entree.");

		} else {
			Data data = new Data(args[0]);
			Simulation S = new Simulation(data);
			List<Processus> proc = Processus.getAllProcessus(data);
	    	int nbSimulations = S.data.getNbSimulations();

			if (proc == null) {
				System.out.println("ERREUR");
			} else {
		    	System.out.println("Debut de la simulation");
		    	for (int i = 1; i <= nbSimulations; i++) {
					proc = Processus.getAllProcessus(data);
		    		System.out.println("\tsimulation "+ i +" en cours...");
		    		S.simulate(proc);
		    	}
		    	System.out.println("Fin de la simulation. Voici les resultats :\n");
		    	System.out.println(S.toString());
			}
		}
    }
}
