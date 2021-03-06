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

	//	HeapFile objects storing tables
	private static HeapFile emp_file;	
	private static HeapFile dep_file;

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
	 * Runs all tests.
	 */
	public static void main(String args[]) {

		//	create DB
		QEPTest qep = new QEPTest();
		qep.create_minibase();
		emp_file = new HeapFile(null);
		dep_file = new HeapFile(null);


		//	initialize schema info
		emp_attr_types = new int[] {AttrType.INTEGER, AttrType.STRING, AttrType.INTEGER, AttrType.FLOAT, AttrType.INTEGER};
		dep_attr_types = new int[] {AttrType.INTEGER, AttrType.STRING, AttrType.FLOAT, AttrType.FLOAT};

		emp_attr_size = new int[] {4, 30, 4, 10, 4};
		dep_attr_size = new int[] {4, 30, 10, 10};


		//	read tables
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

								//	read/create Employee schema
								if (numLine == 0) {
									String[] schemas = line.split(", ");
									s_emp_size = schemas.length;

									emp_attr_name = new String[s_emp_size];									
									for (int j=0; j<s_emp_size; j++) {
										emp_attr_name[j] = schemas[j];
									}

									s_emp = new Schema(s_emp_size);
									for (int i=0; i<s_emp_size; i++) {
										s_emp.initField(i, emp_attr_types[i], emp_attr_size[i], emp_attr_name[i]);
									}
								}

								//	read/create Employee data
								else {
									String[] dataArray = line.split(", ");
									Tuple tuple = new Tuple(s_emp);

									for (int j=0; j<dataArray.length; j++) {
										if (j == 0 || j == 2 || j == 4) {
											int data = Integer.parseInt(dataArray[j]);
											tuple.setField(j, data);
										}
										else if (j == 3) {
											float data = Float.parseFloat(dataArray[j]);
											tuple.setField(j, data);
										}
										else {
											tuple.setField(j, dataArray[j]);
										}
									}

									tuple.insertIntoFile(emp_file);
								}
							}
							else if (table.getName().equals("Department.txt")) {

								//	read/create Department schema
								if (numLine == 0) {
									String[] schemas = line.split(", ");
									s_dep_size = schemas.length;

									dep_attr_name = new String[s_dep_size];
									for (int j=0; j<s_dep_size; j++) {
										dep_attr_name[j] = schemas[j];
									}

									s_dep = new Schema(s_dep_size);
									for (int i=0; i<s_dep_size; i++) {
										s_dep.initField(i, dep_attr_types[i], dep_attr_size[i], dep_attr_name[i]);
									}
								}

								//	read/create Department data
								else {
									String[] dataArray = line.split(", ");
									Tuple tuple = new Tuple(s_dep);

									for (int j=0; j<dataArray.length; j++) {
										if (j == 0) {
											int data = Integer.parseInt(dataArray[j]);
											tuple.setField(j, data);
										}
										else if (j == 2 || j == 3) {
											float data = Float.parseFloat(dataArray[j]);
											tuple.setField(j, data);
										}
										else {
											tuple.setField(j, dataArray[j]);
										}
									}

									tuple.insertIntoFile(dep_file);
								}
							}

							numLine++;
						}
				}
				catch (IOException e) {}
			}
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
	 * SELECT EmpId, Name, Age
	 * FROM Employee
	 */
	protected boolean test1() {
		try {

			FileScan scan = new FileScan(s_emp, emp_file);
			Projection pro = new Projection(scan, 3, 4);
			pro.execute();


			//	clean data
			pro = null;
			scan = null;

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
	 * SELECT Name
	 * FROM Department
	 * WHERE MinSalary = MaxSalary
	 */
	protected boolean test2() {
		try {

			Predicate preds = new Predicate(AttrOperator.EQ, AttrType.FIELDNO, 2, AttrType.FIELDNO, 3);

			FileScan scan = new FileScan(s_dep, dep_file);
			Selection sel = new Selection(scan, preds);
			Projection pro = new Projection(sel, 0, 2, 3);
			pro.execute();


			//	clean data
			pro = null;
			sel = null;
			scan = null;

			System.out.print("\n\nTest 2 completed without exception.");
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
	 * SELECT e.Name, d.Name, d.MaxSalary
	 * FROM Employee e, Department d
	 * WHERE e.DeptId = d.DeptId
	 */
	protected boolean test3() {
		try {

			//	create scanning objects
			FileScan emp_scan = new FileScan(s_emp, emp_file);
			FileScan dep_scan = new FileScan(s_dep, dep_file);

			//	join Department and Employee
			Predicate[] preds = new Predicate[] { new Predicate(AttrOperator.EQ,
					AttrType.FIELDNO, 4, AttrType.FIELDNO, s_emp_size) };
			SimpleJoin join = new SimpleJoin(emp_scan, dep_scan, preds);

			//	project final result
			Integer[] projColumns = new Integer[] {0,2,3,4,5,7};
			Projection pro = new Projection(join, projColumns);
			pro.execute();


			//	clean data
			pro = null;
			join = null;
			dep_scan = null;
			emp_scan = null;

			System.out.print("\n\nTest 3 completed without exception.");
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
	 * SELECT e.Name FROM Employee e, Department d
	 * WHERE e.DeptId = d.DeptId AND e.Salary > d.MaxSalary
	 */
	protected boolean test4() {
		try {

			//	create scanning objects
			FileScan emp_scan = new FileScan(s_emp, emp_file);
			FileScan dep_scan = new FileScan(s_dep, dep_file);

			//	join Department and Employee
			Predicate[] preds = new Predicate[] {
				new Predicate(AttrOperator.EQ, AttrType.FIELDNO, 4, AttrType.FIELDNO, s_emp_size) };
			SimpleJoin join = new SimpleJoin(emp_scan, dep_scan, preds);

			//	select employees with e.Salary > d.MaxSalary
			preds = new Predicate[] {
				new Predicate(AttrOperator.GT, AttrType.FIELDNO, 3, AttrType.FIELDNO, s_emp_size+3) };
			Selection sel = new Selection(join, preds);

			//	project final result
			Integer[] projColumns = new Integer[] {0,2,3,4,5,6,7,8};
			Projection pro = new Projection(sel, projColumns);
			pro.execute();


			//	clean data
			pro = null;
			sel = null;
			join = null;
			emp_scan = null;
			dep_scan = null;


			System.out.print("\n\nTest 4 completed without exception.");
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
