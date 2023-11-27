package threadWrapper;

public class Extended extends Thread {
	int from,to;
	Worker workerObj;

	public Extended(Worker _workerObj_, int _from_, int _to_) {
		from = _from_;
		to = _to_;
		workerObj = _workerObj_;
	} 

	public void run() {
		try {
			workerObj.linearSearch(from, to);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
