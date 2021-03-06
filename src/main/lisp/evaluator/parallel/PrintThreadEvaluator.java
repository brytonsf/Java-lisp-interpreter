package main.lisp.evaluator.parallel;

import main.lisp.evaluator.AbstractEvaluator;
import main.lisp.evaluator.Environment;
import main.lisp.evaluator.Evaluator;
import main.lisp.parser.terms.NilAtom;
import main.lisp.parser.terms.SExpression;
import main.lisp.parser.terms.StringAtom;
import main.lisp.scanner.tokens.StringToken;
import main.lisp.scanner.tokens.TokenFactory;
import main.lisp.scanner.tokens.TokenType;

public class PrintThreadEvaluator extends AbstractEvaluator implements Evaluator {
	public PrintThreadEvaluator() {
		setName("printThread");
	}
	@Override
	public SExpression eval(SExpression expr, Environment environment) {
		if (!(expr.getTail() instanceof NilAtom)) {
			throw new IllegalStateException("Too many arguments for operator 'printThread'");
		}
		String thread = Thread.currentThread().toString();
		System.out.println(thread);
		StringToken tok = (StringToken)TokenFactory.newInstance(TokenType.STRING, thread);
		SExpression result = new StringAtom(tok);
		evaled(expr, result);
//		return new StringAtom(tok);
		return result;

	}

}
