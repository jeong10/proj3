package relop;

/**
 * The selection operator specifies which tuples to retain under a condition; in
 * Minibase, this condition is simply a set of independent predicates logically
 * connected by OR operators.
 */
public class Selection extends Iterator {

	private Iterator it;
	private Predicate[] preds;

	private Schema schema;


  /**
   * Constructs a selection, given the underlying iterator and predicates.
   */
  public Selection(Iterator iter, Predicate... preds) {
		this.it = iter;
		this.preds = preds;
		this.schema = iter.getSchema();
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
		it.restart();
  }

  /**
   * Returns true if the iterator is open; false otherwise.
   */
  public boolean isOpen() {
    return it.isOpen();
  }

  /**
   * Closes the iterator, releasing any resources (i.e. pinned pages).
   */
  public void close() {
		it.close();
  }

  /**
   * Returns true if there are more tuples, false otherwise.
   */
  public boolean hasNext() {
		return it.hasNext();
  }

  /**
   * Gets the next tuple in the iteration.
   * 
   * @throws IllegalStateException if no more tuples
   */
  public Tuple getNext() {
		Tuple currTuple = it.getNext();

		for (int i=0; i<preds.length; i++) {
			Predicate currPred = preds[i];
			if (currPred.evaluate(currTuple)) {
				return currTuple;
			}
		}

		return new Tuple(it.getSchema());
  }

	public Schema getSchema() {
		return schema;
	}

	public void setSchema(Schema schema) {
		this.schema = schema;
	}
}
