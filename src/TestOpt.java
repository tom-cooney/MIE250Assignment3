import java.io.File;

import opt.Minimizer;
import poly.Polynomial;
import poly.Term;
import util.Vector;

/**
 * This is a small example of test cases. To test correctness of your
 * implementation, see if the output on your tests matches the results of the
 * same tests on the solution in TestOptSoln. Note the imports in this file!
 * 
 * @author ssanner@mie.utoronto.ca
 *
 */
public class TestOpt {

	public static void main(String[] args) throws Exception {


		  // You must run more test cases than this! 
			RunMinimizer("files/poly1.txt", 0.001, 200, 0.10, "{ x=1.0 }"); 
			RunMinimizer("files/poly2.txt", 0.001, 200, 0.10, "{ x=1.0 y=1.0 }");
			//RunMinimizer("files/pol3.txt", 0.001, 200, 0.10, "{ x=2.0 y=3.0 }");
			//RunMinimizer("files/poly4.txt", 0.001, 200, 0.10, "{ x=2.0 y=3.0 }");
			
			Term t1 = new Term("2*x*y^2");
			Vector vec1 =new Vector();
			Double d = null;
			vec1.set("x", 1.0);
			vec1.set("y", 2.0);
			vec1.set("z", 3.0);
			String s = null;
			
			vec1.scalarMult(d);
		    System.out.println(t1.differentiate(s));
		 

		

	}

	public static void RunMinimizer(String polyfile, double eps, int max_iter, double alpha, String sx0)
			throws Exception {

		Minimizer m = new Minimizer();

		// If the following file does not load, verify that it exists,
		// and check that it is the correct path relative to your
		// NetBeans/Eclipse project settings for working directory
		Polynomial p = Polynomial.ReadPolynomial(new File(polyfile));

		m.setEps(eps);
		m.setMaxIter(max_iter);
		m.setStepSize(alpha);
		m.setX0(new Vector(sx0));

		System.out.println("========================================");
		System.out.println("OPTIMIZING: " + p);
		System.out.println("========================================");
		m.printParams(System.out);
		m.minimize(p);
		m.printResults(System.out);
	}
}
