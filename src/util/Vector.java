package util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import util.VectorException;

/** Implements a vector with *named* indices.  For example { x=1.0 y=2.0 } is a 2D
 *  vector with the first dimension named "x" and the second dimension named "y"
 *  and having respective values 1.0 and 2.0 in these dimensions.
 *  
 *  TODO: Implement all methods required to support the functionality of the project
 *        and that described in Vector.main(...) below.
 * 
 * @author ssanner@mie.utoronto.ca
 *
 */
public class Vector {

	private HashMap<String,Double> _hmVar2Value; // This maps dimension variable names to values
	
	/** Constructor of an initially empty Vector
	 * 
	 */
	public Vector() {
		_hmVar2Value = new HashMap<String,Double>();
	}

	/** Constructor that parses a String s like "{ x=-1 y=-2.0 z=3d }" into 
	 *  the internal HashMap representation of the Vector.  See usage in main().
	 * 
	 * @param s
	 */
	public Vector(String s) throws VectorException{		
		// Initialize this Vector
		_hmVar2Value = new HashMap<String,Double>();
		s = s.trim();
		//split up vars and vals, store them separately
		String[] _sVarsAndVals = s.split("\\s|=");
		//checking leading/ending chars are present
		if(!(_sVarsAndVals[0].equals("{")) || !(_sVarsAndVals[_sVarsAndVals.length - 1].equals("}"))) {
			throw new VectorException("I needa see a curly brace to know we are dealing with a vector homes");
		}
		
		for (int i = 1 ; i < _sVarsAndVals.length - 1 ; i+=2 ){
            _hmVar2Value.put(_sVarsAndVals[i], Double.parseDouble(_sVarsAndVals[i + 1]));
        }
		              
				 
	}
	
	/** Constructs a String representation of this Matrix
	 * 
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		NumberFormat f = new DecimalFormat("#.0000");
		sb.append("{ ");
		for(String s : this._hmVar2Value.keySet()) {
			sb.append(s + "=" + f.format(this._hmVar2Value.get(s)) + " ");
		}
		sb.append("}");
		return sb.toString();
	}

	/** Tests whether another Object o (most often a matrix) is a equal to *this*
	 *  (i.e., are the dimensions the same and all elements equal each other?)
	 * 
	 * @param o the object to compare to
	 */
	@Override
	public boolean equals(Object o){
		if(o instanceof Vector) {
			Vector v = (Vector)o; //make sure we have a vector
			if(checkSameVars(this, v)) { //if same vars
				for(String s : v._hmVar2Value.keySet()) {
					if(!this._hmVar2Value.get(s).equals(v._hmVar2Value.get(s))) {
						return false;
					}
				}
			}
		return true;	
		}
		else
			return false;
	}
	
	
	/** Removes (clears) all (key,value) pairs from the Vector representation
	 * 
	 */
	public void clear() {
		_hmVar2Value.clear();
	}

	/** Sets a specific var to the value val in *this*, i.e., var=val
	 * 
	 * @param var - label of Vector index to change
	 * @param val - value to change it to
	 */
	public void set(String var, double val) throws VectorException{
		if(var != null) {
			_hmVar2Value.put(var, val);
		}
		else {
			throw new VectorException("cant set to null homes");
		}
	}

	/** Sets all entries in *this* Vector to match entries in x
	 *  (if additional variables are in *this*, they remain unchanged) 
	 * 
	 * @param x
	 */
	public void setAll(Vector x) throws VectorException{
		if(x != null) {
			for(String s : x._hmVar2Value.keySet()) {
				this._hmVar2Value.put(s, x._hmVar2Value.get(s));
			}
		}
		else {
			throw new VectorException("cant set everything to a null vector hoimes");
		}
	}

	///////////////////////////////////////////////////////////////////////////////
	// TODO: Add your methods here!  You'll need more than those above to make   //
	//       main() work below.                                                  //
	///////////////////////////////////////////////////////////////////////////////
	
	/** Returns the keyset of a vector
	 * 
	 */
	public double getValAt(String s) throws VectorException{
		if(s != null) {
			return this._hmVar2Value.get(s);
		}
		else {
			throw new VectorException("cant get the val at a null point homes");
		}
	}
	
	
	/** Returns the keyset of a vector
	 * 
	 */
	public Set<String> getKeySet() {
		return this._hmVar2Value.keySet();
	}
	
	/** checks if the variables in 2 vectors are the same 
	 * 
	 * @param v1
	 * @param v2
	 */
	public boolean checkSameVars(Vector v1, Vector v2){
		if(v1._hmVar2Value.keySet().equals(v2._hmVar2Value.keySet())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/** returns a new vector that is the sum of the 2 vectors called with
	 * 
	 * @param v
	 */
	public Vector sum(Vector v)throws VectorException {
		if(checkSameVars(this, v)) {
			Vector vNew = new Vector();
			for(String s : this._hmVar2Value.keySet()) {
				vNew._hmVar2Value.put(s, this._hmVar2Value.get(s) + v._hmVar2Value.get(s));
			}
			return vNew;
		}
		else {
			throw new VectorException ("I cant add 2 vectors with different variables homes");
		}
	}
	
	/** returns a new vector where every entry from the original vector has had
	 * its length chanced by a factor of d
	 * 
	 * @param d
	 */
	public Vector scalarMult(Double d) throws VectorException{
		if(d == null) {
			throw new NullPointerException("Ayo homes i cant make this bigger by a factor of null");
		}
		else {
			Vector vNew = new Vector();
			for(String s : this._hmVar2Value.keySet()) {
				vNew._hmVar2Value.put(s, d * this._hmVar2Value.get(s));
			}
			return vNew;
		}
	}
	
	/** returns the length of the vector called upon
	 * 
	 */
	public double computeL2Norm() {
		double length = 0;
		for(String s : this._hmVar2Value.keySet()) {
			length += this._hmVar2Value.get(s) * this._hmVar2Value.get(s);
		}
		return Math.sqrt(length);
	}
	
	/** Your Vector class should implement the core functionality below and produce
	 *  **all** of the expected outputs below.  **These will be tested for grading.**
	 * 
	 *  When initially developing the code, comment out lines below that you have
	 *  not implemented yet.  This will allow your code to compile for incremental
	 *  testing.
	 *  
	 * @param args (unused -- ignore)
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// Make vector: vec1[x y z] = [1 2 3]
		Vector vec1 = new Vector();
		vec1.set("x", 1.0);
		vec1.set("y", 2.0);
		vec1.set("z", 3.0);
		
		// Make vector: vec2[x y z] = [-3 -2 -1]
		Vector vec2 = new Vector();
		vec2.set("x", -3.0);
		vec2.set("y", -2.0);
		vec2.set("z", -1.0);
		
		// Make vector: vec3[x y z] = vec4[x y z] = [-1 -2 -3]
		Vector vec3 = new Vector("{ x -1 y=-2.0 z=3d }");
		Vector vec4 = new Vector(vec3.toString());
		
		// Hint: all numbers below are formatted with String.format("%s=%6.4f ", var, val)
		//       ... you may want to use this in your Vector.toString() implementation!
		
		// Test cases: 
		System.out.println(vec1); // Should print: { x=1.0000 y=2.0000 z=3.0000 }
		System.out.println(vec2); // Should print: { x=-3.0000 y=-2.0000 z=-1.0000 }
		System.out.println(vec3); // Should print: { x=-1.0000 y=-2.0000 z=3.0000 }
		System.out.println(vec4); // Should print: { x=-1.0000 y=-2.0000 z=3.0000 }
		System.out.println(vec1.sum(vec1));        // Should print: { x=2.0000 y=4.0000 z=6.0000 }
		System.out.println(vec1.sum(vec2));        // Should print: { x=-2.0000 y=0.0000 z=2.0000 }
		System.out.println(vec1.sum(vec3));        // Should print: { x=0.0000 y=0.0000 z=6.0000 }
		System.out.println(vec1.scalarMult(0.5));  // Should print: { x=0.5000 y=1.0000 z=1.5000 }
		System.out.println(vec2.scalarMult(-1.0)); // Should print: { x=3.0000 y=2.0000 z=1.0000 }
		System.out.println(vec1.sum(vec2.scalarMult(-1.0))); // Should print: { x=4.0000 y=4.0000 z=4.0000 }
		System.out.format("%01.3f\n", vec1.computeL2Norm());           // Should print: 3.742
		System.out.format("%01.3f\n", vec2.sum(vec3).computeL2Norm()); // Should print: 6.000
		
		// If the following don't work, did you override equals()?  See Project 2 Vector and Matrix.
		System.out.println(vec3.equals(vec1)); // Should print: false
		System.out.println(vec3.equals(vec3)); // Should print: true
		System.out.println(vec3.equals(vec4)); // Should print: true
		System.out.println(vec1.sum(vec2).equals(vec2.sum(vec1))); // Should print: true
		
		
		vec1.setAll(vec2);
		
		System.out.println(vec1);
	}	
}
