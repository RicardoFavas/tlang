package tlang.objects;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;

import org.antlr.v4.runtime.ParserRuleContext;

import tlang.ast.ASTNode;
import tlang.ast.Frame;
import tlang.utils.tuples.Pair;

public class TLangPromise extends TLangObject {
		
	FutureTask<TLangObject> task;
	
	public TLangPromise(ASTNode lambda, Frame frame) throws TLangException {
		super(new Pair[0], lambda, frame);

		TLangPromise thiz = this;
		task = new FutureTask<>(new Callable<TLangObject>() {
			@Override
			public TLangObject call() throws TLangException {
				TLangObject value = TLangConstants.NULL;
				try {
					thiz.$set(TLangConstants.state, TLangConstants.RUNNING);
					value = thiz.$call(thiz, TLangConstants.$call);
					thiz.$set(TLangConstants.state, TLangConstants.DONE);
				} catch (TLangException e) {
					thiz.$set(TLangConstants.state, TLangConstants.FAILED);
					throw e;
				}
				return value;
			}
		});
		
		this.$set(TLangConstants.state, TLangConstants.PENDING);
		TLangConstants.EXECUTOR.execute(this.task);
	}
	
	
	
	@Override
	public TLangObject $resolve() throws TLangException {
		try {
			return task.get();
		} catch (ExecutionException e) {
			
			throw (TLangException)e.getCause();
		} catch (InterruptedException e) {
			throw new TLangException(e);
		}
	}
	
	@Override
	public String toString() {
		String s = this.attributes.toString();
		return s;
	}




	
	
	
	

}
