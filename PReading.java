class PReading extends Processus {      // processus en lecture
    static Instruction [] reading;      // suite d'instructions a executer par tous les processus en lecture

    public String toString() {
        return "Processus lecture \n" + super.toString();
    }
    public Instruction [] getInst() {
        return PReading.reading;
    }

    public PReading(Instruction [] reading) {
        PReading.reading = reading;
        toDo = reading.length;
    }

    public Result execute(int quantum) {
        int inSC = 0;
        int start = done;
        int end = done + quantum;
        //System.out.println("quantum a " + quantum);
        for (int i = start; i < end && i < PReading.reading.length && this.blockedBy == null; i++) {
            inSC = PReading.reading[i].execute(this);
            Processus.SC.inReading += inSC;
            //this.position();
            if (this.blockedBy == null) {
                this.done++;
            }
        }
        if (this.done == PReading.reading.length) {
            this.end = true;
        }
        return Processus.SC.copy();
    }

    private void position() {
        for (int i = 0; i < PWriting.writing.length; i++) {
            if (i != this.done) System.out.print(PWriting.writing[i] + ", ");
            else System.out.print("( L )");
        }
        System.out.println();
    }
}
