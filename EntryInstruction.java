class EntryInstruction extends Instruction {
	public EntryInstruction(Semaphore s) {
		super(s);
	}

	public String toString() {
		return "entree";
	}

	public int execute(Processus proc) {
		return 1;
	}
}
