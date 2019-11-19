package tlang.objects;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TLangConstants {

	public static ExecutorService EXECUTOR = Executors.newFixedThreadPool(4);

	public static TLangBoolean TRUE = new TLangBoolean(true);
	public static TLangBoolean FALSE = new TLangBoolean(false);
	
	public static final TLangText THIS = new TLangText("this");
	
	public static final TLangText $equals = new TLangText("$equals");
	public static final TLangText $lequals = new TLangText("$lequals");
	public static final TLangText $get = new TLangText("$get");
	public static final TLangText $set = new TLangText("$set");
	public static final TLangText $text = new TLangText("$text");
	public static final TLangText $hash = new TLangText("$hash");
	public static final TLangText $nequals = new TLangText("$nequals");
	
	public static final TLangText $hasNext = new TLangText("$hasNext");
	public static final TLangText $next = new TLangText("$next");

	
	public static final TLangText $add = new TLangText("$add");
	public static final TLangText $sub = new TLangText("$sub");
	public static final TLangText $multiply = new TLangText("$multiply");
	public static final TLangText $divide = new TLangText("$divide");
	public static final TLangText $mod = new TLangText("$mod");
	public static final TLangText $pow = new TLangText("$pow");
	public static final TLangText $in = new TLangText("$in");
	public static final TLangText $contains = new TLangText("$contains");
	public static final TLangText $lesserThan = new TLangText("$lesserThan");
	public static final TLangText $lesserEqualsThan = new TLangText(" $lesserEqualsThan");
	public static final TLangText $greaterThan = new TLangText("$greaterThan");
	public static final TLangText $greaterEqualsThan = new TLangText("$greaterEqualsThan");
	public static final TLangText $is = new TLangText("$is");	
	public static final TLangText $extends = new TLangText("$extends");
	public static final TLangText $instance = new TLangText("$instance");
	public static final TLangText $instances = new TLangText("$instances");
	public static final TLangText $negate = new TLangText("$negate");
	public static final TLangText $call = new TLangText("$call");
	public static final TLangText $resolve = new TLangText("$resolve");
	public static final TLangText $implements = new TLangText("$implements");
	public static final TLangText $iterator = new TLangText("$iterator");

	
	public static final TLangText BOOLEAN_INSTANCE = new TLangText("[boolean]");
	public static final TLangText NUMBER_INSTANCE = new TLangText("[number]");
	public static final TLangText TEXT_INSTANCE = new TLangText("[text]");
	public static final TLangText PROMISE_INSTANCE = new TLangText("[promise]");
	public static final TLangText ARRAY_INSTANCE = new TLangText("[array]");	
	public static final TLangText LIST_INSTANCE = new TLangText("[list]");
	public static final TLangText MAP_INSTANCE = new TLangText("[map]");
	public static final TLangText LAMBDA_INSTANCE = new TLangText("[lambda]");
	public static final TLangText FRAME_INSTANCE = new TLangText("[frame]");
	public static final TLangObject NULL = TLangNull.NULL;
	
	public static final MathContext MC = MathContext.DECIMAL128;
	public static final TLangNumber ZERO = new TLangNumber(BigDecimal.ZERO);
	public static final TLangNumber ONE = new TLangNumber(BigDecimal.ONE);
	public static final TLangNumber TEN = new TLangNumber(BigDecimal.TEN);
	public static final TLangNumber MINUS_ONE = new TLangNumber(BigDecimal.ONE.negate());
	public static final TLangNumber MIN = new TLangNumber(new BigDecimal("-10000000000000"));
	public static final TLangNumber MAX = new TLangNumber(new BigDecimal("10000000000000"));
	
	public static final TLangText state = new TLangText("state");

	public static final TLangText PENDING = new TLangText("PENDING");
	public static final TLangText WAITING = new TLangText("WAITING");
	public static final TLangText RUNNING = new TLangText("RUNNING");
	public static final TLangText FAILED = new TLangText("FAILED");
	public static final TLangText DONE = new TLangText("DONE");
	
	public static final TLangText key = new TLangText("key");
	public static final TLangText value = new TLangText("value");
	public static final TLangText index = new TLangText("index");
	
	
	

	
}
