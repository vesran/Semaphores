import java.util.*;

class Semaphore {
    String name;
    int tickets;
    int max;
    List<Processus> waiting = new ArrayList<>();

    public String toString() {
        String str = name + "-" + tickets;
        return str;
    }

    // Construit un semaphore suivant une expression de type : "S:20"
    public Semaphore(String expression) {
        String [] array = expression.split(":");
        if (!isLetter(array[0])) {
            System.out.println("NOM DE SEMAPHORE INCORRECTE : " + expression);
            this.tickets = -1;
        } else {
            this.tickets = Data.extractNumber(expression);
            this.max = this.tickets;
            this.name = array[0];
        }
    }

    // Verifie si l'expression est une lettre de l'alphabet en majuscule
    private static boolean isLetter(String expression) {
        String [] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        for (String letter : alphabet) {
            if (expression.equals(letter)) {
                return true;
            }
        }
        return false;
    }
}
