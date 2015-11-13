package tests;

import global.AttrOperator;
import global.AttrType;
import global.RID;
import global.SearchKey;
import heap.HeapFile;
import index.HashIndex;
import relop.FileScan;
import relop.HashJoin;
import relop.IndexScan;
import relop.KeyScan;
import relop.Predicate;
import relop.Projection;
import relop.Schema;
import relop.Selection;
import relop.SimpleJoin;
import relop.Tuple;

import java.io.*;
import java.nio.charset.Charset;



public class QEPTest extends TestDriver {

	private static final String TEST_NAME = "QEP tests";
	private static final int SUPER_SIZE = 2000;

	//	schemas
	private static Schema s_emp;
	private static Schema s_dep;

	//	number of attributes(fields) of the schema
	private static int s_emp_size;
	private static int s_dep_size;

	//	attribute names
	private static String[] emp_attr_name;
	private static String[] dep_attr_name;

	//	attribute types
	private static int[] emp_attr_types;
	private static int[] dep_attr_types;

	//	attribute sizes
	private static int[] emp_attr_size;
	private static int[] dep_attr_size;


	/**
	 * Test application entry point; runs all tests.
	 */
	public static void main(String args[]) {

		//	create DB
		QEPTest qep = new QEPTest();
		qep.create_minibase();


		//	read schemas
		String path = args[0];
		File f = new File(path);
		File[] tables = f.listFiles();

		for (File table: tables) {
			if (table.isFile()) {

				String file_path = path + "/" + table.getName();

				try (
					InputStream fis = new FileInputStream(file_path);
				  InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
				  BufferedReader br = new BufferedReader(isr);
				) {
						String line;
						int numLine = 0;

						while ((line = br.readLine()) != null) {

							if (table.getName().equals("Employee.txt")) {

								//	read Employee schema
								if (numLine == 0) {
									String[] schemas = line.split(", ");
									s_emp_size = schemas.length;
									
									for (int j=0; j<s_emp_size; j++) {
//System.out.println(schemas[j]);
									}
								}

								//	read Employee data
								else {
								}
							}
							else if (table.getName().equals("Department.txt")) {

								//	read Department schema
								if (numLine == 0) {
									//
								}
							}

							numLine++;
						}
				}
				catch (IOException e) {}
			}
		}
//System.out.println("testing: " + path);

		emp_attr_name = new String[s_emp_size];
		dep_attr_name = new String[s_dep_size];


		//	create Employee schema
		//	EmpId, Name, Age, Salary, DeptID
		s_emp = new Schema(s_emp_size);
		emp_attr_types = new int[]{AttrType.INTEGER, AttrType.STRING, AttrType.INTEGER, AttrType.FLOAT, AttrType.INTEGER};
		emp_attr_size = new int[]{4, 20, 4, 10, 4};

		for (int i=0; i<s_emp_size; i++) {
			s_emp.initField(i, emp_attr_types[i], emp_attr_size[i], emp_attr_name[i]);
		}


		//	create Department schema
		//	DeptId, Name, MinSalary, MaxSalary
		s_dep = new Schema(s_dep_size);
		dep_attr_types = new int[]{AttrType.INTEGER, AttrType.STRING, AttrType.FLOAT, AttrType.FLOAT};
		dep_attr_size = new int[]{4, 20, 10, 10};

		for (int i=0; i<s_dep_size; i++) {
			s_dep.initField(i, dep_attr_types[i], dep_attr_size[i], dep_attr_name[i]);
		}


		//	run tests
		boolean status = PASS;
		status &= qep.test1();
		status &= qep.test2();
		status &= qep.test3();
		status &= qep.test4();


		// display the final results
		System.out.println();
		if (status != PASS) {
			System.out.println("Error(s) encountered during " + TEST_NAME + ".");
		} else {
			System.out.println("All " + TEST_NAME
					+ " completed; verify output for correctness.");
		}

	}

	/**
	 * Display for each employee his ID, Name and Age
	 */
	protected boolean test1() {
		try {

			int numTuple = 0;
			Tuple tuple = new Tuple(s_emp);

			HeapFile file = new HeapFile(null);
			for (int i=0; i<numTuple; i++) {

				// create the tuple
				//tuple.setIntFld(0, i);
				//tuple.setStringFld(2, "l" + i);

				// insert the tuple in the file and index
				//RID rid = file.insertRecord(tuple.getData());

			}

			//	test projection
			//FileScan scan = new FileScan(s_emp, file);
			//Projection pro = new Projection(scan, 3, 4);
			//pro.execute();


			//	clean data
			//pro = null;
			//scan = null;
			file = null;

			System.out.print("\n\nTest 1 completed without exception.");
			return PASS;

		} catch (Exception exc) {
			exc.printStackTrace(System.out);
			System.out.print("\n\nTest 1 terminated because of exception.");
			return FAIL;

		} finally {
			System.out.println();
		}
	}


	/**
	 * Display the Name for the departments with MinSalary = MaxSalary
	 */
	protected boolean test2() {
		try {

			return PASS;

		} catch (Exception exc) {
			exc.printStackTrace(System.out);
			System.out.print("\n\nTest 2 terminated because of exception.");
			return FAIL;

		} finally {
			System.out.println();
		}
	}


	/**
	 * For each employee, display his Name and the Name of his department as well as the maximum salary of his department
	 */
	protected boolean test3() {
		try {

			return PASS;

		} catch (Exception exc) {
			exc.printStackTrace(System.out);
			System.out.print("\n\nTest 3 terminated because of exception.");
			return FAIL;

		} finally {
			System.out.println();
		}
	}


	/**
	 * Display the Name for each employee whose Salary is greater than the maximum salary of his department
	 */
	protected boolean test4() {
		try {
			
			return PASS;

		} catch (Exception exc) {
			exc.printStackTrace(System.out);
			System.out.print("\n\nTest 4 terminated because of exception.");
			return FAIL;

		} finally {
			System.out.println();
		}
	}
}
