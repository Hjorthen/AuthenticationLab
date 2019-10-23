package Server;

public class Job {
	int jobNumber;
	String fileName;
	String printer;
	
	public Job(int jobNumber, String fileName, String printer) {
		this.jobNumber = jobNumber;
		this.fileName = fileName;
		this.printer = printer;
	}
	
	public String toString() {
		return jobNumber + " - " + fileName;
	}
}
