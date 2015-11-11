package relop;

import global.RID;
import relop.Tuple;
import heap.HeapFile;
import heap.HeapScan;


/**
 * Wrapper for heap file scan, the most basic access method. This "iterator"
 * version takes schema into consideration and generates real tuples.
 */
public class FileScan extends Iterator {

	private Schema schema;
	private HeapFile file;
	private HeapScan heapScan;

	private boolean start;
	private boolean isOpen;

	private RID lastRID;


  /**
   * Constructs a file scan, given the schema and heap file.
   */
  public FileScan(Schema schema, HeapFile file) {
		this.schema = schema;
		this.file = file;

		heapScan = file.openScan();
		start = true;
  }

  /**
   * Gives a one-line explaination of the iterator, repeats the call on any
   * child iterators, and increases the indent depth along the way.
   */
  public void explain(int depth) {
  }

  /**
   * Restarts the iterator, i.e. as if it were just constructed.
   */
  public void restart() {
		close();
		heapScan = file.openScan();
		start = true;
    isOpen = true;
  }

  /**
   * Returns true if the iterator is open; false otherwise.
   */
  public boolean isOpen() {
    return isOpen;
  }

  /**
   * Closes the iterator, releasing any resources (i.e. pinned pages).
   */
  public void close() {
		heapScan.close();
    isOpen = false;
  }

  /**
   * Returns true if there are more tuples, false otherwise.
   */
  public boolean hasNext() {
		return heapScan.hasNext();
  }

  /**
   * Gets the next tuple in the iteration.
   * 
   * @throws IllegalStateException if no more tuples
   */
  public Tuple getNext() {

		if (start) {
			lastRID = new RID();
			start = false;
		}
		
		byte[] data = heapScan.getNext(lastRID);
		return new Tuple(schema, data);
  }

  /**
   * Gets the RID of the last tuple returned.
   */
  public RID getLastRID() {
    return lastRID;
  }

	public Schema getSchema() {
		return schema;
	}

	public void setSchema(Schema schema) {
		this.schema = schema;
	}
}
