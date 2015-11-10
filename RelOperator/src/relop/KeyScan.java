package relop;

import global.SearchKey;
import heap.HeapFile;
import index.HashIndex;

/**
 * Wrapper for hash scan, an index access method.
 */
public class KeyScan extends Iterator {

	private Schema schema;
	private HashIndex index;
	private SearchKey key;
	private HeapFile file;

	private boolean start = true;
	private boolean isOpen = false;

  /**
   * Constructs an index scan, given the hash index and schema.
   */
  public KeyScan(Schema schema, HashIndex index, SearchKey key, HeapFile file) {
		this.schema = schema;
		this.index = index;
		this.key = key;
		this.file = file;
  }

  /**
   * Gives a one-line explaination of the iterator, repeats the call on any
   * child iterators, and increases the indent depth along the way.
   */
  public void explain(int depth) {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * Restarts the iterator, i.e. as if it were just constructed.
   */
  public void restart() {
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
		isOpen = false;
  }

  /**
   * Returns true if there are more tuples, false otherwise.
   */
  public boolean hasNext() {
		return false;
  }

  /**
   * Gets the next tuple in the iteration.
   * 
   * @throws IllegalStateException if no more tuples
   */
  public Tuple getNext() {
    isOpen = true;

		if (start) {
			start = false;
		}

		return null;
  }

}
