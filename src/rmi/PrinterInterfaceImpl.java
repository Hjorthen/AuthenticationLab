package rmi;

public class PrinterInterfaceImpl implements PrinterInterface {

	private void Log(String text, Object... args)
	{
		System.out.printf(text, args);
	}
	
	@Override
	public void print(String filename, String printer) {
		Log("Printing %s on %s", filename, printer);
	}

	@Override
	public String queue() {
		Log("Sending print queue");
		return "QUEUE";
	}

	@Override
	public void topQueue(int job) {
		Log("Moving %d to top of queue", job);

	}

	@Override
	public void start() {
		Log("Starting server..");
	}

	@Override
	public void stop() {
		Log("Stopping server..");
	}

	@Override
	public void restart() {
		Log("Restarting server..");
	}

	@Override
	public String status() {
		Log("Sending status");
		return "STATUS";
	}

	@Override
	public String readConfig(String parameter) {
		Log("Sending config par (%s)", parameter);
		return "CONFIG";
	}

	@Override
	public void setConfig(String parameter, String value) {
		Log("Setting config par (%s) to %s", parameter, value);
	}

}
