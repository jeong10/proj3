package relop;

/**
 * The projection operator extracts columns from a relation; unlike in
 * relational algebra, this operator does NOT eliminate duplicate tuples.
 */
public class Projection extends Iterator {

	private Iterator it;
	private Integer[] fields;

	private Schema originalSchema;
	private Schema schema;


  /**
   * Constructs a projection, given the underlying iterator and field numbers.
   */
  public Projection(Iterator iter, Integer... fields) {
		this.it = iter;
		this.fields = fields;

		originalSchema = it.getSchema();
		Schema projectedSchema = new Schema(originalSchema.getCount() - fields.length);

		//	reserve original schema before projecting
		for (int i=0; i<originalSchema.getCount(); i++) {
			originalSchema.initField(i, originalSchema, i);
		}


		//	project schema
		int index = 0;
		for (int i=0; i<originalSchema.getCount(); i++) {

			//	check if current column is to be projected
			boolean isProjected = false;
			for (int j=0; j<fields.length; j++) {
				int colToProject = fields[j];

				if (i == colToProject) {
					isProjected = true;
					break;
				}
			}

			if (!isProjected) {
				projectedSchema.initField(index, originalSchema, i);
				index++;
			}
		}

		this.schema = projectedSchema;
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
		currTuple.schema = originalSchema;
		Tuple retTuple = new Tuple(this.schema);


		//	project tuple's field
		int index = 0;
		for (int i=0; i<currTuple.schema.getCount(); i++) {

			//	check if current column is to be projected
			boolean isProjected = false;
			for (int j=0; j<fields.length; j++) {
				int colToProject = fields[j];

				if (i == colToProject) {
					isProjected = true;
					break;
				}
			}

			if (!isProjected) {
				retTuple.setField(index, currTuple.getField(i));
				index++;
			}
		}

		return retTuple;
  }

	public Schema getSchema() {
		return schema;
	}

	public void setSchema(Schema schema) {
		this.schema = schema;
	}
}
