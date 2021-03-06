package main.lisp.evaluator.lazy.basic;

import main.lisp.evaluator.Environment;
import main.lisp.evaluator.basic.ProductEvaluator;
import main.lisp.evaluator.lazy.Thunk;
import main.lisp.parser.terms.DecimalAtom;
import main.lisp.parser.terms.IntegerAtom;
import main.lisp.parser.terms.NilAtom;
import main.lisp.parser.terms.SExpression;

public class LazyProductEvaluator extends ProductEvaluator {

	@Override
	public SExpression lazyEval(SExpression expr, Environment environment) {
		expr = expr.getTail();
		if (expr instanceof NilAtom || expr.getHead() instanceof NilAtom || expr.getTail() instanceof NilAtom) {
			throw new IllegalStateException("Missing arguments for operator '*'");
		}
		if (!(expr.getTail().getTail() instanceof NilAtom)) {
			throw new IllegalStateException("Too many arguments for operator '*'");
		}
		
		SExpression firstEvaled = expr.getHead().lazyEval(environment);
		SExpression secondEvaled = expr.getTail().getHead().lazyEval(environment);
		
		if (firstEvaled instanceof Thunk) {
			firstEvaled = firstEvaled.eval(environment);
		}
		if (secondEvaled instanceof Thunk) {
			secondEvaled = secondEvaled.eval(environment);
		}
		
		IntegerAtom firstInt = null;
		IntegerAtom secondInt = null;
		DecimalAtom firstDec = null;
		DecimalAtom secondDec = null;
		
		int correctArgs = 0;
		
		if (firstEvaled instanceof IntegerAtom) {
			firstInt = (IntegerAtom)firstEvaled;
			correctArgs++;
		}
		if (firstEvaled instanceof DecimalAtom) {
			firstDec = (DecimalAtom)firstEvaled;
			correctArgs++;
		}
		
		if (secondEvaled instanceof IntegerAtom) {
			secondInt = (IntegerAtom)secondEvaled;
			correctArgs++;
		}
		if (secondEvaled instanceof DecimalAtom) {
			secondDec = (DecimalAtom)secondEvaled;
			correctArgs++;
		}
		
		if (correctArgs != 2) {
			throw new IllegalStateException("Arguments for operator '*' must both evaluate to numbers");
		}
		
		if (firstInt != null && secondInt != null) {
			long product = firstInt.getValue() * secondInt.getValue();
			return new IntegerAtom(product);
		} else {
			double product = 0;
			if (firstInt != null) {
				product = firstInt.getValue();
			} else if (firstDec != null) {
				product = firstDec.getValue();
			}
			if (secondInt != null) {
				product *= secondInt.getValue();
			} else if (secondDec != null) {
				product *= secondDec.getValue();
			}
			return new DecimalAtom(product);
		}
	}

}
