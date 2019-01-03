import java.util.*;

class Processus {
    boolean end = false;
    int done = 0;		// nombre d'operations deja effectuees
    int toDo;
    Semaphore blockedBy;
    static Result SC = new Result();

    public String toString() {
        String str = "done : " + done;
        str += " -- " + "blocked by: " + blockedBy;
        return str;
    }

    public Instruction [] getInst() {
        System.out.println("Pas de tableau d'instructions");
        return null;
    }

    // Cree une liste avec tous les processus mentionnes dans le fichier
    public static List<Processus> getAllProcessus(Data data) {
        if (data.getText() == null || data.getSemaphores() == null || data.getNbSimulations() == -1 ||
            data.nbReading == -1 || data.nbWriting == -1 || data.nbExecutions == -1) {
            System.out.println("DONNEES CORROMPUES");
            return null;
        }

        for (Semaphore sem : data.getSemaphores()) { sem.tickets = sem.max;  }  // Reinitialisation pour que chaque semaphore garde la meme valeur d'une simulation a l'autre

        List<Processus> proc = new ArrayList<>();
        Instruction [] reading = data.getInstructions(data.extractPart("%PL"), data.extractPart("%EL"));
        Instruction [] writing = data.getInstructions(data.extractPart("%PE"), data.extractPart("%EE"));
        Instruction [] execution = data.getInstructions(data.extractPart("%PX"), data.extractPart("%EX"));

        if (reading == null || writing == null || execution == null) {      // Cas d'erreur dans la recuperation des tableaux d'instructions
            return null;
        }

        for (int i = 0; i < data.nbReading; i++) {      // Processus lecture
            proc.add(new PReading(reading));
        }
        for (int i = 0; i < data.nbWriting; i++) {      // Processus ecriture
            proc.add(new PWriting(writing));
        }
        for (int i = 0; i < data.nbExecutions; i++) {    // Processus execution
            proc.add(new PExecution(execution));
        }
        return proc;
    }

    public Result execute(int quamtum) {
        return new Result();
    }

}
