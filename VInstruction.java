class VInstruction extends Instruction {
	public VInstruction(Semaphore s) {
		super(s);
	}

	public String toString() {
		return "V(" + this.semaphore + ")";
	}

	public int execute(Processus proc) {
		if (this.semaphore.tickets < this.semaphore.max) {		// Strictement inférieur car on va lui ajouter 1
			this.semaphore.tickets++;
		} else {
			System.out.println("Le semaphore " + this.semaphore + " depasse sa taille limite");
		}
		return 0;
	}
}
