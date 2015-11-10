package relop;

public class HashJoin extends Iterator {

	protected FileScan outer;
	protected FileScan inner;
	
	private boolean startJoin = true;


	public HashJoin(FileScan left, FileScan right, int a, int b) {
	}

	public HashJoin(HashJoin hj, IndexScan is, int a, int b) {
	}

	public void explain(int depth) {
	}

	public void restart() {
	}

	public boolean isOpen() {
		return false;
	}

	public void close() {
	}
	
	public boolean hasNext() {
		return false;
	}

	public Tuple getNext() {
		return null;
	}
}
