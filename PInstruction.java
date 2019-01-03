class PInstruction extends Instruction {
	public PInstruction(Semaphore s) {
		super(s);
	}

	public String toString() {
		return "P(" + this.semaphore + ")";
	}

	public int execute(Processus proc) {
		if (this.semaphore.tickets > 0) {
			this.semaphore.tickets--;
			return 0;
		} else {
			proc.blockedBy = this.semaphore;			// On bloque le processus
			return 0;
		}
	}
}
