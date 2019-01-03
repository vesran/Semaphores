import java.util.*;

class Instruction {
	Semaphore semaphore;		// non null ssi P ou V

	// Constructeur
	public Instruction(Semaphore s) {
		this.semaphore = s;
	}

	@Override
    public String toString() {
		return "";
    }

    public Semaphore getS() {
        return this.semaphore;
    }

	public int execute(Processus proc) {
		return 0;
	}
}
