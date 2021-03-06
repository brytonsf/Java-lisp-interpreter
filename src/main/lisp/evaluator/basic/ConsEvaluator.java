package main.lisp.evaluator.basic;

import main.lisp.evaluator.AbstractEvaluator;
import main.lisp.evaluator.Environment;
import main.lisp.evaluator.Evaluator;
import main.lisp.parser.terms.ExpressionFactory;
import main.lisp.parser.terms.NilAtom;
import main.lisp.parser.terms.SExpression;

public class ConsEvaluator extends AbstractEvaluator implements Evaluator {

	public ConsEvaluator() {
		setName("cons");
	}
	
	@Override
	public SExpression eval(SExpression expr, Environment environment) {
		SExpression inputExpr = expr;
		expr = expr.getTail();
		if (expr instanceof NilAtom || expr.getTail() instanceof NilAtom) {
			throw new IllegalStateException("Missing arguments for operator 'cons'");
		}
		if (!(expr.getTail().getTail() instanceof NilAtom)) {
			throw new IllegalStateException("Too many arguments for operator 'cons'");
		}
		
		SExpression firstEvaled = expr.getHead().eval(environment);
		SExpression secondEvaled = expr.getTail().getHead().eval(environment);
		
		SExpression ret = ExpressionFactory.newInstance(firstEvaled, secondEvaled);

		evaled(inputExpr, ret);
		
		return ret;
	}

}
