package sat;

import immutable.ImList;
import sat.env.Environment;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {
    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     * 
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */
    public static Environment solve(Formula formula) {
        // calls next function
        ImList<Clause> clauses = formula.getClauses();
        Environment env = new Environment();
        return solve(clauses, env);
    }

    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     * 
     * @param clauses
     *            formula in conjunctive normal form
     * @param env
     *            assignment of some or all variables in clauses to true or
     *            false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     *         or null if no such environment exists.
     */
    private static Environment solve(ImList<Clause> clauses, Environment env) {
        // If there are no clauses, the formula is trivially satisfiable.
        if (clauses.isEmpty()){
            return env;
        }
        //Otherwise, find the smallest clause (by number of literals)
        Clause smallestClause=clauses.first(); //init as first Clause
        for (Clause c: clauses){
            if (c.size()<smallestClause.size()){
                smallestClause=c;
            }
            if (smallestClause.isEmpty()){
                return null;
            }
        }
        //pick an arbitrary literal from this small clause
        Literal l=smallestClause.chooseLiteral();
        Environment env1=env;
        if (smallestClause.isUnit()){
            if (l instanceof sat.formula.PosLiteral){
                env1=env.putTrue(1.getVariable());
            }else{
                env1 = env.putFalse(l.getVariable());
            }
            ImList<Clause> clauses1 =  substitute(clauses, l);
            return (solve(clauses1, env1));
        }else{
            Environment envT = env.putTrue(l.getVariable()); //env now has {1:T}
            ImList<Clause> clausesT = substitute(clauses, l);
            Environment resT =solve(clausesT, envT);
        }
        if (resT==null){
            Environment envF = env.putFalse(l.getVariable()); //env{1:T} is changed to {1:F}
            ImList<Clause> clausesF = substitute(clauses, l.getNegation());
            Environment resF = solve(clausesF, envF);
            return resF;
        } else{
            return resT;
        }

        // If there is an empty clause, the clause list is unsatisfiable -- fail and backtrack (use empty
        // clause to denote a clause evaluated to FALSE based on the variable binding in the environment)

        //If the clause has only one literal, bind its variable in the environment so that the clause is satisfied,
        // substitute for the variable in all the other clauses (using the suggested substitute() method),
        // and recursively call solve().


        //First try setting the literal to TRUE, substitute for it in all the clauses, then solve() recursively.
        //If that fails, then try setting the literal to FALSE, substitute, and solve() recursively.
    }

    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     * 
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses,
            Literal l) {
        ImList<Clause> newClauses=new EmptyImList<Clause>();
        for (Clause c:clauses){
            Clause newC=c.reduce(l);
            if (newC!=null){
                newClauses=newClauses.add(newC);
            }
        }
        return newClauses;
    }
}
