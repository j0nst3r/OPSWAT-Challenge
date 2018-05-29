package main.java.com;

public class Result {

	private String engine;
	private String threats;
	private String scanResult;
	private String defTime;
	
	public Result(String engine, String threats, String scan, String time) {
		this.engine = engine;
		this.threats = threats == "" ? "Clean" : threats;
		this.scanResult = scan;
		this.defTime = time;
	}
	
	public String toString() {
		
		StringBuilder strBuilder = new StringBuilder();
		
		strBuilder.append("engine: " + this.engine + "\n");
		strBuilder.append("threat_found: " + this.threats + "\n");
		strBuilder.append("scan_result: " + this.scanResult + "\n");
		strBuilder.append("def_time: " + this.defTime + "\n");
		
		return strBuilder.toString();
	}
}
