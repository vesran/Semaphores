class PWriting extends Processus {      // processus en ecriture
    static Instruction [] writing;      // suite d'instructions a executer par tous les processus en ecriture

    public String toString() {
        return "Processus ecriture \n" + super.toString();
    }
    public Instruction [] getInst() {
        return PWriting.writing;
    }

    public PWriting(Instruction [] writing) {
        PWriting.writing = writing;
        toDo = writing.length;
    }

    public Result execute(int quantum) {
        int inSC = 0;
        int start = done;
        int end = done + quantum;
        //System.out.println("quantum a " + quantum);
        for (int i = start; i < end && i < PWriting.writing.length && this.blockedBy == null; i++) {
            inSC = PWriting.writing[i].execute(this);
            Processus.SC.inWriting += inSC;
            //this.position();
            if (this.blockedBy == null) {
                this.done++;
            }
        }
        if (this.done == PWriting.writing.length) {
            this.end = true;
        }
        return Processus.SC.copy();
    }

    private void position() {
        for (int i = 0; i < PWriting.writing.length; i++) {
            if (i != this.done) System.out.print(PWriting.writing[i] + ", ");
            else System.out.print("( E )");
        }
        System.out.println();
    }
}
