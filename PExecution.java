class PExecution extends Processus {    // processus en execution
    static Instruction [] execution;    // suite d'instructions a executer par tous les processus en execution

    @Override
    public String toString() {
        return "Processus execution \n" + super.toString();
    }

    public Instruction [] getInst() {
        return PExecution.execution;
    }

    public PExecution(Instruction [] execution) {
        PExecution.execution = execution;
        toDo = execution.length;
    }

    public Result execute(int quantum) {
        int inSC = 0;
        int start = done;
        int end = done + quantum;

        for (int i = start; i < end && i < PExecution.execution.length && this.blockedBy == null; i++) {
            inSC = PExecution.execution[i].execute(this);
            Processus.SC.inExecution += inSC;
            //this.position();
            if (this.blockedBy == null) {
                this.done++;
            }
        }
        if (this.done == PExecution.execution.length) {
            this.end = true;
        }
        return Processus.SC.copy();
    }

    private void position() {
        for (int i = 0; i < PWriting.writing.length; i++) {
            if (i != this.done) System.out.print(PWriting.writing[i] + ", ");   // -> creer une nouvelle fonction avec tableau et chaine de caracteres en parametre pour generaliser la methode position
            else System.out.print("( X )");
        }
        System.out.println();
    }
}
