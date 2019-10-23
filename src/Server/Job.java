package Server;

public class Job {
	int jobNumber;
	String fileName;
	
	public Job(int jobNumber, String fileName) {
		this.jobNumber = jobNumber;
		this.fileName = fileName;
	}
	
	public String toString() {
		return jobNumber + " - " + fileName;
	}
}
