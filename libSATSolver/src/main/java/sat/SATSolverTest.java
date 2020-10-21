package sat;

/*
import static org.junit.Assert.*;

import org.junit.Test;
*/


import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.IOException;

import sat.env.*;
import sat.formula.*;


public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();


    /*
    read in and parse a .cnf file and construct the corresponding Formula instance as input to your SAT solver.
    Print out “satisfiable” or “not satisfiable” depending on the result.
    If the Formula is satisfiable, output your variable assignments to a result file “BoolAssignment.txt”,
    with format of one variable per line: <variable>:<assignment>,
    e.g.,
    1:TRUE
    2:FALSE

    Report the execution time of your SAT solver by including the following code surrounding your SATSolver.solve():
     */
	// TODO: add the main method that reads the .cnf file and calls SATSolver.solve to determine the satisfiability
//    public static void main(String[] args) {
//        Formula f2 = new Formula(); //create instance of formula
//        System.out.println("SAT solver starts!!!");
//        long started = System.nanoTime();
//        Environment e = SATSolver.solve(f2);
//        long time = System.nanoTime();
//        long timeTaken= time - started;
//        System.out.println("Time:" + timeTaken/1000000.0 + "ms");
//
//    }

    public static void main(String[] args) {
        System.out.println("Reading File");
        long readTime = System.nanoTime();
        File fin = null;
        Scanner bin = null;
        try {
            fin=new File("test_2020.cnf"); //import file
            bin=new Scanner(fin);

            String line;
            boolean commentCheck=true;
            while(commentCheck!=false){
                String[] commentRemove=bin.nextLine().split(" ");
                if(commentRemove[0]!="c"||commentRemove[0]!="C"){
                    commentCheck=false;
                }


            }
            String[] format=bin.nextLine().split(" ");
            int NumberOfClauses=Integer.parseInt(format[3]);//get the number of clauses
            Formula f = new Formula(); //create and instance of the formula
            while (f.getSize()!=NumberOfClauses) {

                line=bin.nextLine();
                if(line.length()>0){
                    String[] tempLine=line.split(" ");

                    Clause c = new Clause();

                    for(String i:tempLine){
                        if(Integer.parseInt(i)==0){
                            break;
                        }
                        Literal literal = PosLiteral.make(Integer.toString(Math.abs(Integer.parseInt(i))));//makes literal instance


                        if((Integer.parseInt(i))<0){ //add the negated Integer to the clause if string is negative
                            c=c.add(literal.getNegation());
                        }
                        else if ((Integer.parseInt(i))>0){ //add the postitive Integer to the clause if string is positive
                            c=c.add(literal);
                        }
                        if (c == null){
                            c = new Clause();
                        }
                    }
                    f=f.addClause(c); //add the clauses to the formula
                }
            }
            String fileName = "D:/School/ISTD/Java/2D/BoolAssignment.txt";
            PrintWriter write = new PrintWriter(fileName, "UTF-8");
            long endReadTime = System.nanoTime();
            long tReadTime = endReadTime - readTime;
            System.out.println("Reading Time: " + tReadTime/1000000000.0 + "s");
            System.out.println("SAT Solver starts");
            long started = System.nanoTime();
            Environment env = SATSolver.solve(f);
            long time = System.nanoTime();
            long timeTaken = time - started;
            System.out.println("Solving Time: " + timeTaken/1000000.0 + "ms");
            System.out.println("Total Time: " + (timeTaken+tReadTime)/1000000000.0 + "s");
            if (env == null){
                System.out.println("Formula Unsatisfiable");
            }else{
                System.out.println("Formula Satisfiable");
                String bindings = env.toString();
                System.out.println(bindings);
                bindings = bindings.substring(bindings.indexOf("[")+1, bindings.indexOf("]"));
                String[] bindingNew = bindings.split(", ");
                for (String binding : bindingNew){
                    String[] bind = binding.split("->");
                    write.println(bind[0] + ":" + bind[1]);
                }


            }
            write.close();
        }finally {
            if (bin != null) {
                bin.close();

            }
        }
    }

	
    public void testSATSolver1(){
    	// (a v b)
    	Environment e = SATSolver.solve(makeFm(makeCl(a,b))	);
/*
    	assertTrue( "one of the literals should be set to true",
    			Bool.TRUE == e.get(a.getVariable())  
    			|| Bool.TRUE == e.get(b.getVariable())	);
    	
*/    	
    }
    
    
    public void testSATSolver2(){
    	// (~a)
    	Environment e = SATSolver.solve(makeFm(makeCl(na)));
        /*
    	assertEquals( Bool.FALSE, e.get(na.getVariable()));
        */
    }
    
    private static Formula makeFm(Clause... e) {
        Formula f = new Formula();
        for (Clause c : e) {
            f = f.addClause(c);
        }
        return f;
    }
    
    private static Clause makeCl(Literal... e) {
        Clause c = new Clause();
        for (Literal l : e) {
            c = c.add(l);
        }
        return c;
    }
    
    
    
}