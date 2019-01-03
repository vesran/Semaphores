import java.io.*;
import java.util.*;

class Data {
    private List<String> text;                  // Contient chaque ligne du fichier sans espaces ni commentaires
    private List<Semaphore> semaphores;         // Liste des semaphores declarees dans le fichier
    private int nbSimulations = -1;             // Nombre de simulations
    int nbReading;                              // Nombre de processus en lecture
    int nbWriting;                              // Nombre de processus en ecriture
    int nbExecutions;                           // Nombre de processus en execution

    // Getter pour le nombre de simulations
    public int getNbSimulations() {
        return this.nbSimulations;
    }

    // Getter pour le texte
    public List<String> getText() {
        return this.text;
    }

    // Getter pour la liste des semaphores recense
    public List<Semaphore> getSemaphores() {
        return this.semaphores;
    }

    @Override
    public String toString() {
        String str = "";
        str += "---> Donnees bruts : " + text + "\n";
        str += "---> Listes des semaphores : " + semaphores + "\n";
        str += "---> Nombre de processus en lecture : " + nbReading + "\n";
        str += "---> Nombre de processus en ecriture : " + nbWriting + "\n";
        str += "---> Nombre de processus en execution : " + nbExecutions + "\n";
        str += "---> Nombre de simulations : " + nbSimulations + "\n";
        return str;
    }

    // Constructeur principal
    public Data(String filename) {
        File file = new File(filename);
        Scanner sc = null;

        try {
            sc = new Scanner(file);
            String fileText = extractText(sc);
            if (fileText == null) {
                System.out.println("L'EXTRACTION DES DONNEES A ECHOUE");
                return;
            }
            this.setText(toDataList(fileText.split("\n")));
            this.setSemaphores();
            this.setParameters();

        } catch (IOException e) {
            System.out.println("PROBLEME AVEC LE FICHIER " + filename);
            e.printStackTrace();

        } finally {
            if (sc != null) {   sc.close(); }
            System.out.println(this.toString());
        }
    }

    // Convertie un tableau de String en une liste de String
    private static List<String> toDataList(String [] array) {
        List<String> list = new ArrayList<>();

        for (String line : array) {
            if (isCorrectStatement(line)) {
                list.add(line);
            } else {
                return null;
            }
        }
        return list;
    }

    // Setter pour la liste de semaphores
    private void setSemaphores() {
        List<String> listExp = this.extractPart("%IN");
        if (listExp == null) {  return;    }
        this.semaphores = new ArrayList<>();

        for (String expression : listExp) {
            if (this.getSemaphore(String.valueOf(expression.charAt(0))) != null) {      // On n'ajoute pas le semaphore si le nom a deja ete repertorie
                System.out.println("SEMAPHORES DE MEME NOM : " + expression.charAt(0));
                this.semaphores = null;
                return;
            } else if (!Data.insertSemaphore(this.semaphores, new Semaphore(expression))) { // Le semaphore est insere ici !!!
                this.semaphores = null;
                return;
            }
        }
    }

    // Introduit un semaphore s'il est correct dans la liste en parametre
    private static boolean insertSemaphore(List<Semaphore> listSem, Semaphore s) {
        if (s.tickets == -1) {
            return false;
        } else {
            listSem.add(s);
            return true;
        }
    }

    // Setter pour les parametres : nbReading, nbWriting, nbExecutions et nbSimulations
    private void setParameters() {
        List<String> listExp = this.extractPart("%PA");
        if (listExp == null) {  return; }

        boolean initReading = false;
        boolean initWriting = false;
        boolean initExecutions = false;
        boolean initSimulations = false;

        for (String exp : listExp) {
            if (exp.charAt(0) == 'L' && !initReading) {
                this.nbReading = Data.extractNumber(exp);
                initReading = true;
            } else if (exp.charAt(0) == 'E' && !initWriting) {
                this.nbWriting = Data.extractNumber(exp);
                initWriting = true;
            } else if (exp.charAt(0) == 'X' && !initExecutions) {
                this.nbExecutions = Data.extractNumber(exp);
                initExecutions = true;
            } else if (exp.charAt(0) == 'N' && !initSimulations) {
                this.nbSimulations = Data.extractNumber(exp);
                initSimulations = true;
            } else {
                System.out.println("EXPRESSION INATTENDUE DANS LES PARAMETRES: " + exp);
                this.nbSimulations = -1;
                return;
            }
        }
    }

    // Renvoie le nombre dans une expression de type E:20 ou 0 en cas de nombre negatif ou expression non numerique (nombre entier)
    public static int extractNumber(String exp) {
        String [] array = exp.split(":");

        if (array.length != 2 || !Data.isNumber(array[1])) {
            System.out.println("EXPRESSION INCORRECTE: " + exp);
            return -1;
        } else if (Integer.parseInt(array[1]) < 0) {
            System.out.println("VALEUR NEGATIVE DANS " + exp);
            return -1;
        } else {
            return Integer.parseInt(array[1]);
        }
    }

    // Verifie si une chaine de caracteres represente un nombre entier
    private static boolean isNumber(String str) {
        for (int i = 0; i < str.length(); i++ ) {
            if (!Character.isDigit(str.charAt(i)) && str.charAt(i) != '-') {    // Accepte les nombres negatifs
                return false;
            }
        }
        return true;
    }

    // Verifie si partName est le nom de la partie Initialisation, Parametres ou %FI
    private static boolean isNecessary(String partName) {
        String [] ref = {"%IN", "%PA", "%FI"};

        for (String exp : ref) {
            if (exp.equals(partName)) {
                return true;
            }
        }
        return false;
    }

    // Verifie si la chaine passee en argument est le nom d'une partie non obligatoire
    private static boolean isOptional(String partName) {
        String [] ref = {"%PL", "%EL", "%PE", "%EE", "%PX", "%EX"};

        for (String exp : ref) {
            if (exp.equals(partName)) {
                return true;
            }
        }
        return false;
    }

    // Verifie si une chaine de caracteres correspond a un nom de partie
    private static boolean isPartName(String expression) {
        return isNecessary(expression) || isOptional(expression);
    }

    // Verifie si l'expression est unique dans this.text
    private boolean isUnique(String partName) {
        int cpt = 0;

        for (String exp : this.text) {
            if (exp.equals(partName)) {
                cpt++;
            }
        }
        if (cpt <= 1 ) {        // 1 ou 0 occurrence, OK
            return true;
        } else {                // des doublons...
            return false;
        }
    }

    // Extrait la partie qui a pour nom partName et retourne tous les String concerne en une liste
    public List<String> extractPart(String partName) {
        List<String> listExp = new ArrayList<>();
        int start = this.text.indexOf(partName);

        if (!this.isUnique(partName)) {
            System.out.println("L'EXPRESSION " + partName + " N'EST PAS UNIQUE. ");
            return null;

        } else if (start == -1 && Data.isNecessary(partName)) {
            System.out.println(partName + " N'EST PAS DANS LE FICHIER ALORS QU'IL EST OBLIGATOIRE");
            return null;

        } else if (start == -1 && Data.isOptional(partName)) {
            return listExp;     // est vide

        } else {
            for (int i = start + 1; i < this.text.size() && !Data.isPartName(this.text.get(i)); i++) {  // On continue a lire jusqu'au prochain nom de partie
                listExp.add(this.text.get(i));
            }
            return listExp;     // est remplie
        }
    }

    // Setter pour this.text
    private void setText(List<String> list) {
        this.text = list;
    }

    // Verifie si une expression du fichiers n'est pas un commentaire, ni une chaine vide, ni "%FI"
    private static boolean isCorrectStatement(String line) {
        if (line.equals("") || line.charAt(0) == '#' || line.equals("%FI")) {
            return false;
        } else if (line.length() >= 3 && (Data.isPartName(line) || line.charAt(1) == ':' || (line.length() >= 4 && line.charAt(1) == '(' && line.charAt(3) == ')') ))
            return true;
        else {
            System.out.println("FORME INCONNUE DETECTEE : " + line);
            return false;
        }
    }

    // Extrait les donnees: les instructions, les noms de parties en une chaine de caracteres
    private static String extractText(Scanner sc) {
        String str = "";
        String tmpStr = "";
        boolean partNameStarted = false;

        while (sc.hasNextLine() && !tmpStr.equals("%FI")) {
            tmpStr = String.join("", sc.nextLine().split(" ")).trim();      // On se debarrasse des espaces
            tmpStr = String.join("", tmpStr.split("\t"));                   // On se debarrasse des tabulations
            if (isPartName(tmpStr) && !partNameStarted) {   partNameStarted = true; }   // On a detecte un nom de partie

            if (isCorrectStatement(tmpStr) && partNameStarted) {
                str += tmpStr + "\n";
            } else if (tmpStr != "" && tmpStr.length() > 0 && tmpStr.charAt(0) != '#' && !partNameStarted) {
                System.out.println("L'EXPRRESSION " + tmpStr + " N'APPARTIENT A AUCUNE PARTIE");
                return null;
            } else if (tmpStr != "" && tmpStr.length() > 0 && tmpStr.charAt(0) != '#' && !tmpStr.equals("%FI")) {
                System.out.println("EXPRESSION NON RECONNUE: " + tmpStr);
                return null;
            }
        }
        if (!tmpStr.equals("%FI")) {
            System.out.println("%FI MANQUANT");
            return null;
        }
        return str;
    }

    // Construit un tableau d'instructions [...prologue..., ...section critique..., ...epilogue...]
    public Instruction [] getInstructions(List<String> prologue, List<String> epilogue) {
        if (prologue == null || epilogue == null) {
            System.out.println("prologue ou epilogue null");
            return null;
        }

        List<Instruction> listInstr = new ArrayList<>();
        //System.out.println("En prologue : " + prologue);

        for (String exp : prologue) {                   // Partie prologue
            if (!putInstruction(listInstr, exp)) {  return null;    }
        }

        listInstr.add(new EntryInstruction(null));          // add Instruction type 1- Partie section critique
        for (int i = 0; i < 48; i++) {
            listInstr.add(new Instruction(null));			// add Instruction de type 0-
        }
        listInstr.add(new ExitInstruction(null));			// add Instruction de type 2-

        //System.out.println("En epilogue " + epilogue);
        for (String exp : epilogue) {                   // Partie epilogue
            if (!putInstruction(listInstr, exp)) {  return null;    }
        }
        //System.out.println(listInstr);
        return listInstr.toArray(new Instruction[0] );
    }

    // Rajoute une instruction P ou V a la liste d'instructions en fonction de l'expression passee en argument
    private boolean putInstruction(List<Instruction> listInstr, String exp) {
        Semaphore s;            // Seamphore associe a l'instruction

        // Si une expression n'a pas au moins 3 caracteres, elles n'est pas recuperee
        if (exp.charAt(0) == 'P' && exp.charAt(1) == '(' && exp.charAt(3) == ')') {         //PInstruction
            s = this.getSemaphore(String.valueOf(exp.charAt(2)));
            if (s != null) {
                listInstr.add(new PInstruction(s));		// add Instruction de type 3-
                return true;
            }
            else {
                System.out.println("LE SEMAPHORE " + exp.charAt(2) + " N'EST PAS INITIALISEE");
                return false;
            }

        } else if (exp.charAt(0) == 'V' && exp.charAt(1) == '(' && exp.charAt(3) == ')' ) { //VInstruction
            s = this.getSemaphore(String.valueOf(exp.charAt(2)));
            if (s != null) {
                listInstr.add(new VInstruction(s));		// add Instruction de type 4-
                return true;
            }
            else {
                System.out.println("LE SEMAPHORE " + exp.charAt(2) + " N'EST PAS INITIALISEE");
                return false;
            }

        } else {    // L'expression n'est pas de la forme "P(S)"
            System.out.println("EXPRESSION NON RECONNUE " + exp);
            return false;
        }
    }

    // Retourne un semaphore contenu dans l'attribut semaphores qui a pour nom name
    private Semaphore getSemaphore(String name) {
        for (Semaphore s : this.semaphores) {
            if (s.name.equals(name)) {
                return s;
            }
        }
        return null;
    }

}
