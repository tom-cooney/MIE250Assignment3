package opt;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeSet;

import poly.Polynomial;
import util.Vector;
import util.VectorException;

/** The main optimization package: see project handout for functionality and definitions
 *  of terminology defined here.
 * 
 * @author ssanner@mie.utoronto.ca
 *
 */
public class Minimizer {
	
	private double _eps;			// tolerance
	private int    _maxIter;		// maximum number of iterations
	private double _stepSize;		// step size alpha
	private Vector _x0;	   		// starting point
	private Vector _lastx; 		// last point found
	private double _lastObjVal;		// last obj fn value found
	private double _lastGradNorm;   // last gradient norm found
	private long _compTime;			// computation time needed
	private int _nIter;				// no. of iterations needed

	private HashMap<String,Polynomial> _var2gradp; // cached Polynomials for gradient expressions

	/** Default constructor -- you may want to add to it, but you don't have to
	 * 
	 */
	public Minimizer() {
		_eps = 0.001;
		_maxIter = 100;
		_stepSize = 0.05;
		_x0 = new Vector();
		_lastx = new Vector();
	}
	
	// Getters -- we've filled these in since they are trivial
	public double getEps()      { return _eps; }
	public int getMaxIter()     { return _maxIter; }
	public double getStepSize() { return _stepSize; }
	public Vector getX0() 		{ return _x0; }
	public double getLastObjVal()   { return _lastObjVal; }
	public double getLastGradNorm() { return _lastGradNorm; }
	public Vector getLastPoint() { return _lastx; }
	public int getNIter()       { return _nIter; }
	public long getCompTime()   { return _compTime; }
	
	// Setters -- we've filled these in since they are trivial
	public void setEps(double e)      { _eps = e; }
	public void setMaxIter(int m)     { _maxIter = m; }
	public void setStepSize(double s) { _stepSize = s; }
	
	/** Set the initial starting point of gradient descent
	 * 
	 * @param x0
	 * @throws VectorException 
	 */
	public void setX0(Vector x0) throws VectorException { 
		_x0.clear();
		_x0.setAll(x0);
	}
	
	///////////////////////////////////////////////////////////////////////////////
	// TODO: Your methods here!  You should add some helper methods that facilitate
	//       the implementation of the methods below.
	///////////////////////////////////////////////////////////////////////////////
	
	 public boolean areWeFarFromMinimum(){
         return _lastGradNorm > _eps;
     }
     
     public void buildPartialDerivatives(Polynomial p) throws Exception{
         _var2gradp = new HashMap<String, Polynomial>();
         TreeSet<String> allVars = p.getAllVars();
         Iterator<String> iterate = allVars.iterator();
         while(iterate.hasNext()){
             String var = iterate.next();
             _var2gradp.put(var, p.differentiate(var));
         }
     }
     
     public Vector calculateGradient(Vector lastpointFound) throws Exception{
         Vector v = new Vector();
         for(Entry<String, Polynomial> enter: _var2gradp.entrySet())
             v.set(enter.getKey(), enter.getValue().evaluate(lastpointFound));
         return v;
             
     }
     
     

	/** Run the steepest descent algorithm -- see handout though some pseudocode is provided below.
	 * 
	 * @param p Polynomial to minimize
	 * @throws Exception
	 */
	public void minimize(Polynomial p) throws Exception {
         
            buildPartialDerivatives(p);
         
             _nIter = 0;
             long start = System . currentTimeMillis ();
             
             _lastx.clear();
             _lastx.setAll(_x0);
             _lastObjVal = p.evaluate(_x0);
             
             _lastGradNorm = Double.MAX_VALUE;
             
             while(getNIter() < getMaxIter() && areWeFarFromMinimum()){
                 _nIter++;
                 Vector gradientAtLastPoint = calculateGradient(_lastx);
                 _lastGradNorm = gradientAtLastPoint.computeL2Norm();
                 _lastx = _lastx.sum(gradientAtLastPoint.scalarMult(-_stepSize));
                 _lastObjVal = p.evaluate(_lastx);
                 System.out.format("At iteration %d: %s objective value = %.3f\n", _nIter, _lastx, _lastObjVal);
             }
			
		// Record the end time
		_compTime = System.currentTimeMillis()-start;
	}
	
	/** Print minimization result details
	 * 
	 * @param ps - usually System.out
	 */
	public void printResults(PrintStream ps) {
		ps.println();
		ps.println("Number of iterations: " + getNIter());
		ps.println("Last gradient norm:   " + String.format("%6.4f", getLastGradNorm()));
		ps.println("Point at termination: " + getLastPoint());
		ps.println("Time elapsed:         " + getCompTime() + " ms");
		ps.println();
	}

	/** Print parameter settings
	 * 
	 * @param ps - usually System.out
	 */
	public void printParams(PrintStream ps) {
		ps.println();
		ps.println("Tolerance (epsilon): " + getEps());
		ps.println("Maximum iterations:  " + getMaxIter());
		ps.println("Step size (alpha):   " + getStepSize());
		ps.println("Starting point (x0): " + getX0());
		ps.println();
	}

	/** An example testing the Minimizer with expected output.  Make sure this works!
	 *  
	 * @param args - unused
	 * @throws Exception if any errors occur (when implemented correctly, there should
	 *         be no Exceptions/errors below)
	 */
	public static void main(String[] args) throws Exception {
		
		// Assign starting point {x=1.0}
		Vector x0 = new Vector();
		x0.set("x", 1.0);

		// Initialize polynomial and minimizer
		Polynomial p = new Polynomial("10*x^2 + -40*x + 40");
		Minimizer  m = new Minimizer();		
		m.setX0(x0);

		System.out.println("Polynomial: " + p);

		// Run minimizer and view result at termination
		m.minimize(p);
		System.out.format("At termination: %s objective value = %.3f\n", m._lastx, m._lastObjVal);

		// Output of the test case above should read:
		// ===============================================
		// Polynomial: 10.000*x^2 + -40.000*x + 40.000
		// At iteration 1: { x=2.0000 } objective value = 0.000
		// At iteration 2: { x=2.0000 } objective value = 0.000
		// At termination: { x=2.0000 } objective value = 0.000
		// ===============================================
		
		// [Optional] View the cached gradient expressions if you've cached them
		// for (String var : p.getAllVars())
		//	  System.out.println("Gradient of " + var + " is " + m._var2gradp.get(var));
	}
}
