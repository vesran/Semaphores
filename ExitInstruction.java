class ExitInstruction extends Instruction {
	public ExitInstruction(Semaphore s) {
		super(s);
	}

	public String toString() {
		return "sortie";
	}

	public int execute(Processus proc) {
		return 0-1;
	}
}
