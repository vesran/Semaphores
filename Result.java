class Result {
    int inReading;
    int inWriting;
    int inExecution;

    public String toString() {
        return "En lecture : " + inReading + " || En ecriture : " + inWriting + " || En execution " + inExecution + "\n";
    }

    public Result copy() {
        Result res = new Result();
        res.inReading = this.inReading;
        res.inWriting = this.inWriting;
        res.inExecution = this.inExecution;
        return res;
    }

    public boolean equals(Result res) {
        return (this.inReading == res.inReading && this.inWriting == res.inWriting && this.inExecution == res.inExecution);
    }
}
