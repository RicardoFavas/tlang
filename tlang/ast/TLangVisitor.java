package tlang.ast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.antlr.v4.misc.CharSupport;
import org.antlr.v4.misc.EscapeSequenceParsing;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import tlang.objects.TLangBoolean;
import tlang.objects.TLangConstants;
import tlang.objects.TLangNumber;
import tlang.objects.TLangObject;
import tlang.objects.TLangText;
import tlang.parser.TLangParser.*;
import tlang.utils.RUtils;
import tlang.utils.tuples.Pair;
public class TLangVisitor extends tlang.parser.TLangBaseVisitor {

	@Override
	public ASTNode visit(ParseTree tree) {
		try {
			return (ASTNode)super.visit(tree);
		} catch (Exception e) {
			System.out.println("."+tree.getText());
		}
		return null;
//		return (ASTNode)super.visit(tree);
	}
	
	@Override
	public Object visitProgram(ProgramContext ctx) {
		ASTNode[] block = ctx.exp().stream().map(exp -> this.visit(exp)).toArray(ASTNode[]::new);
		return new ASTBlock(ctx,block);
	}
	

	@Override
	public Object visitClosedBlock(ClosedBlockContext ctx) {
		ASTNode[] block = ctx.exp().stream().map(exp -> this.visit(exp)).toArray(ASTNode[]::new);
		return new ASTBlock(ctx,block);
	}
	
	@Override
	public Object visitBlock(BlockContext ctx) {
		ASTNode[] block = ctx.exp().stream().map(exp -> this.visit(exp)).toArray(ASTNode[]::new);
		return new ASTBlock(ctx,block);
	}
	
	public ParserRuleContext[] getBlocks(List<BlockContext> blocks, List<ClosedBlockContext> cblocks) {
		int size = Math.max(blocks.size(),cblocks.size());
		ParserRuleContext[] b = new ParserRuleContext[size];
		for (int i = 0; i < size; i++) {
			if (blocks != null && blocks.size() > i && blocks.get(i) != null)
				b[i] = blocks.get(i);
			else if (cblocks != null && cblocks.size() > i && cblocks.get(i) != null)
				b[i] = cblocks.get(i);
		}
		return b;
	}
	
	@Override
	public Object visitExp(ExpContext ctx) {

		//
		if (ctx.cpriority != null) {
			return this.visit(ctx.exp(0));
		}

		//const id = exp
		if (ctx.cconst != null) {
			TLangText left = new TLangText(ctx.ID(0).getText());
			ASTNode right = this.visit(ctx.exp(0));
			return new ASTConst(ctx, left,right);
		}
		//var id (= exp)?
		if (ctx.cvar != null) {
			TLangText left = new TLangText(ctx.ID(0).getText());
			ASTNode right = new ASTValue(ctx,TLangConstants.NULL);
			if ( ctx.exp().size() == 1 ) {
				right = this.visit(ctx.exp(0));
			}
			return new ASTVar(ctx, left,right);
		}
		
		//async exp
		if (ctx.casync != null) {
			ASTNode right = this.visit(ctx.exp(0));
			
			return new ASTPromise(ctx,right); 	
		}

		//await (exp,exp)
		if (ctx.cawait != null) {
			ASTNode[] block = new ASTNode[ctx.exp().size()];
			for (int i = 0; i < ctx.exp().size(); i++) {
				ASTNode exp = this.visit(ctx.exp(i));
				block[i] = new ASTInvoke(ctx, exp);
			}
			return new ASTBlock(ctx, block);
		}
		
		

		if (ctx.clambda != null) {
			Pair<TLangText, ASTNode>[] params = new Pair[ctx.param().size()];

			for (int i = 0; i < ctx.param().size(); i++) {
				TLangText name = new TLangText(ctx.param(i).ID().getText());
				ASTNode value = new ASTValue(ctx, TLangConstants.NULL);
				if (ctx.param(i).exp() != null)
					value = this.visit(ctx.param(i).exp());
				params[i] = new Pair<TLangText, ASTNode>(name,value);
			}
			
			ParserRuleContext[] blocks = getBlocks(ctx.block(),ctx.closedBlock());
			ASTBlock block = (ASTBlock)this.visit(blocks[0]);
			
			return new ASTLambda(ctx,params,new ASTBlock(ctx,block)); 			
		}

		//exp ( exp,exp,exp )
		if (ctx.cinvoke != null) {
			ASTNode exp = this.visit(ctx.exp(0));
			
			ASTNode[] args = new ASTNode[ctx.exp().size()];
			for (int i = 0; i < ctx.exp().size(); i++) {
				args[i] = this.visit(ctx.exp(i));
			}
			return new ASTInvoke(ctx, exp, args);
		}
		//exp ( exp,exp,exp )
		if (ctx.cinvoke2 != null) {
			ASTId id = new ASTId(ctx, new TLangText(ctx.ID(0).getText()));
			
			ASTNode[] args = new ASTNode[ctx.exp().size()];
			for (int i = 0; i < ctx.exp().size(); i++) {
				args[i] = this.visit(ctx.exp(i));
			}
			return new ASTInvoke(ctx, id, args);
		}

		//exp '?''.'ID (':' exp)?
		if (ctx.safenav != null) {
			ASTNode exp = this.visit(ctx.exp(0));
	
			TLangText id = new TLangText(ctx.ID(0).getText());
			ASTNode fallback = new ASTValue(ctx,TLangConstants.NULL);
			if (ctx.exp().size() == 2) {
				fallback = this.visit(ctx.exp(1));
			}
			return new ASTIfDoElse(ctx,
				new ASTInvoke(ctx, new ASTValue(ctx,TLangConstants.NULL), exp),
				fallback, 
				new ASTInvoke(ctx, exp, new ASTValue(ctx,id)));
		}
		
		
		//exp '.' id = exp
		if ( ctx.cset != null) {
			TLangText id = new TLangText(ctx.ID(0).getText());
			ASTNode exp0 = this.visit(ctx.exp(0));
			ASTNode exp1 = this.visit(ctx.exp(1));
			
			new ASTSet(ctx, exp0, exp1);
		}
		

		//exp . ID
		if (ctx.cget != null) {
			ASTNode exp0 = this.visit(ctx.exp(0));
			TLangText id = new TLangText(ctx.ID(0).getText());
			return new ASTGet(ctx, exp0, new ASTValue(ctx,id));
		}
		
		//id = exp
		if ( ctx.cassign != null) {
			ASTValue value = new ASTValue(ctx,new TLangText(ctx.ID(0).getText()));
			ASTNode exp = this.visit(ctx.exp(0));
			return new ASTAssign(ctx, value, exp);
		}
		//
		if ( ctx.cassignThis != null) {
			ASTNode value = this.visit(ctx.literal());
			ASTNode exp = this.visit(ctx.exp(0));
			return new ASTAssignThis(ctx, value, exp);
		}		
		//exp == exp
		if ( ctx.cequals != null ) {
			ASTNode left = this.visit(ctx.exp(0));
			ASTNode right = this.visit(ctx.exp(1));			
			return new ASTOperation(ctx,left,right,"==");
		}
		// exp === exp
		if ( ctx.crefequals != null) {
			ASTNode left = this.visit(ctx.exp(0));
			ASTNode right = this.visit(ctx.exp(1));			
			return new ASTOperation(ctx,left,right,"===");
		}
		//exp != exp
		if ( ctx.cnequals != null ) {
			ASTNode left = this.visit(ctx.exp(0));
			ASTNode right = this.visit(ctx.exp(1));			
			return new ASTOperation(ctx,left,right,"!=");
		}
		//exp != exp
		if ( ctx.crefnequals != null ) {
			ASTNode left = this.visit(ctx.exp(0));
			ASTNode right = this.visit(ctx.exp(1));			
			return new ASTOperation(ctx,left,right,"!==");
		}
		//exp + exp
		if (ctx.cadd != null) {
			ASTNode left = this.visit(ctx.exp(0));
			ASTNode right = this.visit(ctx.exp(1));			
			return new ASTOperation(ctx,left,right,"+");
		}		
		
		//exp - exp
		if (ctx.csub != null) {
			ASTNode left = this.visit(ctx.exp(0));
			ASTNode right = this.visit(ctx.exp(1));			
			return new ASTOperation(ctx,left,right,"-");
		}
		//exp * exp
		if (ctx.cmultiply != null) {
			ASTNode left = this.visit(ctx.exp(0));
			ASTNode right = this.visit(ctx.exp(1));			
			return new ASTOperation(ctx,left,right,"*");
		}
		//exp / exp
		if (ctx.cdivide != null) {
			ASTNode left = this.visit(ctx.exp(0));
			ASTNode right = this.visit(ctx.exp(1));			
			return new ASTOperation(ctx,left,right,"/");
		}
		//exp % exp
		if (ctx.cmod != null) {
			ASTNode left = this.visit(ctx.exp(0));
			ASTNode right = this.visit(ctx.exp(1));			
			return new ASTOperation(ctx,left,right,"%");
		}
		//exp^exp
		if (ctx.cpow != null) {
			ASTNode left = this.visit(ctx.exp(0));
			ASTNode right = this.visit(ctx.exp(1));			
			return new ASTOperation(ctx,left,right,"^");
		}
		//exp < exp
		if (ctx.clesserThan != null) {
			ASTNode left = this.visit(ctx.exp(0));
			ASTNode right = this.visit(ctx.exp(1));			
			return new ASTOperation(ctx,left,right,"<");
		}
		//exp > exp
		if (ctx.cgreaterThan != null) {
			ASTNode left = this.visit(ctx.exp(0));
			ASTNode right = this.visit(ctx.exp(1));			
			return new ASTOperation(ctx,left,right,">");
		}
		
		//exp contains exp
		if (ctx.ccontains != null) {
			ASTNode left = this.visit(ctx.exp(0));
			ASTNode right = this.visit(ctx.exp(1));			
			return new ASTOperation(ctx,left,right,"contains");
		}
		//exp implements exp
		if (ctx.cimplements != null) {
			ASTNode left = this.visit(ctx.exp(0));
			ASTNode right = this.visit(ctx.exp(1));			
			return new ASTOperation(ctx,left,right,"implements");
		}
		
		//exp match exp
		if (ctx.cmatch != null) {
			ASTNode left = this.visit(ctx.exp(0));
			ASTNode right = this.visit(ctx.exp(1));			
			return new ASTOperation(ctx,left,right,"match");
		}
		//exp like exp
		if (ctx.clike != null) {
			ASTNode left = this.visit(ctx.exp(0));
			ASTNode right = this.visit(ctx.exp(1));			
			return new ASTOperation(ctx,left,right,"like");
		}

		
		//exp in exp
		if (ctx.cin != null) {
			ASTNode left = this.visit(ctx.exp(0));
			ASTNode right = this.visit(ctx.exp(1));			
			return new ASTOperation(ctx,left,right,"in");
		}
		
		//exp is exp
		if (ctx.cis != null) {
			ASTNode left = this.visit(ctx.exp(0));
			ASTNode right = this.visit(ctx.exp(1));			
			return new ASTOperation(ctx,left,right,"is");
		}
		

		
		
		//if exp then block (elif exp then block)* (else block)?
		if (ctx.cif != null) {			
			ParserRuleContext[] blocks = getBlocks(ctx.block(),ctx.closedBlock());
			ASTNode n = ctx.celse == null ? null : this.visit(blocks[blocks.length-1]);
			for (int i = ctx.exp().size()-1; i >= 0; i--) {
				ASTNode exp = this.visit(ctx.exp(i));
				ASTNode block = this.visit(blocks[i]);
				n = new ASTIfDoElse(ctx, exp, block, n);
			}
			return n;
		}
				
		//break
		if (ctx.cbreak != null) {
			ASTNode exp = new ASTValue(ctx,TLangConstants.NULL);
			if (ctx.exp().size() == 1)
				exp = this.visit(ctx.exp(0));
			return new ASTBreak(ctx);
		}
		//next
		if (ctx.cnext != null) {
			return new ASTNext(ctx);
		}
		
		//while exp do block
		if (ctx.cwhile != null) {
			ASTNode exp = this.visit(ctx.exp(0));
			
			ASTNode block = this.visit( getBlocks(ctx.block(),ctx.closedBlock())[0] );
			
			return new ASTWhile(ctx,exp,block);
		}
		
		//do block while exp
		if (ctx.cdowhile != null) {
			ASTNode exp = this.visit(ctx.exp(0));
			ASTNode block = this.visit( getBlocks(ctx.block(),ctx.closedBlock())[0] );
			return new ASTDoWhile(ctx,exp,block);
		}
		
		
		
		//for ID in exp do block ;
		if (ctx.cfor != null) {
			TLangText id = new TLangText(ctx.ID(0).getText());
			ASTNode iterable = this.visit(ctx.exp(0));
			ASTNode block = this.visit(ctx.block(0));
			return new ASTFor(ctx,id,iterable,block);
		}
		
		//return exp?
		if (ctx.creturn != null) {
			ASTNode exp = new ASTValue(ctx,TLangConstants.NULL);
			if (ctx.exp().size() == 1)
				exp = this.visit(ctx.exp(0));
			return new ASTReturn(ctx,exp);
		}
		
		// try block (catch block)? (finally block)?
		if (ctx.ctry != null) {
			ParserRuleContext[] blocks = getBlocks(ctx.block(),ctx.closedBlock());
			ASTNode tryBlock = this.visit( blocks[0] );
			ASTNode catchBlock = null;
			if (ctx.ccatch != null) {
				catchBlock = this.visit( blocks[1] );
			}
			TLangText exceptionname = null;
			if (ctx.ID().size() > 0) {
				exceptionname = new TLangText(ctx.ID(0).getText());
			}
			ASTNode finallyBlock = null;
			if (ctx.cfinally != null) {
				finallyBlock = this.visit( blocks[blocks.length-1] );
			}
			return new ASTTryCatch(ctx,tryBlock,catchBlock,exceptionname,finallyBlock);
		}
		// throw exp?
		if (ctx.cthrow != null) {
			ASTNode throwValue = new ASTValue(ctx,TLangConstants.NULL);
			if (ctx.exp().size() > 0)
				throwValue = this.visit(ctx.exp(0));
			return new ASTThrow(ctx,throwValue);
		}
		//this
		if (ctx.cthis != null) {	
			return new ASTThis(ctx);
		}


		if (ctx.cframe != null) {	
			return new ASTFrame(ctx);
		}
		

		if (ctx.cnop != null) {	
			return new ASTNop(ctx);
		}
		
		//assert(exp)
		if (ctx.cassert != null) {
			ASTNode exp0 = this.visit(ctx.exp(0));
			return new ASTAssert(ctx,exp0);
		}
		
		
		//java(exp)
		if (ctx.cjava != null) {
			ASTNode exp0 = this.visit(ctx.exp(0));
			return new ASTJava(ctx,exp0);
		}
		
		
		//print(exp)
		if (ctx.cprint != null) {
			ASTNode exp0 = this.visit(ctx.exp(0));
			return new ASTPrint(ctx,exp0);
		}
		
		
		
		//import(path)
		if (ctx.cimport != null) {
			ASTNode exp = this.visit(ctx.exp(0));
			return new ASTImport(ctx,exp);
		}
		
		if (ctx.cmerge != null) {
			ASTNode exp0 = this.visit(ctx.exp(0));
			return new ASTInvoke(ctx, new ASTThis(ctx),exp0);
		}
		
		return super.visitExp(ctx);
	}
	
	@Override
	public Object visitLiteral(LiteralContext ctx) {
		if (ctx.cnull != null) {
			return new ASTValue(ctx, TLangConstants.NULL);
		}
		
		if (ctx.ctrue != null) {
			return new ASTValue(ctx, TLangConstants.TRUE);			
		}
		if (ctx.cfalse != null) {
			return new ASTValue(ctx, TLangConstants.FALSE);			
		}
		
		if (ctx.cid != null) {
			return new ASTId(ctx,new TLangText(ctx.getText()));
		}
		
		if (ctx.cdecimal != null) {
			String text = ctx.cdecimal.getText();
			text = text.replaceAll("_","");
			BigDecimal bd = new BigDecimal(text,TLangConstants.MC);
			TLangNumber value = new TLangNumber(bd);
			return new ASTValue(ctx, value);
		}
		
		if (ctx.cobject != null) {
			ASTNode block = this.visit(ctx.closedBlock());
			return new ASTObject(ctx,block);
		}
		
		if (ctx.cnegate != null) {
			ASTNode exp = this.visit(ctx.exp(0));
			return new ASTInvoke(ctx,new ASTGet(ctx, exp, new ASTValue(ctx,TLangConstants.$negate)));
		}

		if (ctx.cregular_string != null) {
	    	String s = ctx.cregular_string.getText(); 
	    	s = CharSupport.getStringFromGrammarStringLiteral(s);
			return new ASTValue(ctx,new TLangText(s));
		}
		if (ctx.cverbatium_string != null) {
	    	String s = ctx.cverbatium_string.getText();
	    	s = s.substring(2,s.length()-1);
	    	return new ASTValue(ctx,new TLangText(s));
		}
		
		if (ctx.clist != null) {
			ASTNode[] exps = new ASTNode[ctx.exp().size()];
			for (int i = 0; i < ctx.exp().size(); i++) {
				exps[i] = this.visit(ctx.exp(i));
			}
			return new ASTList(ctx, exps);
		}
		
		return super.visitLiteral(ctx);
	}
	

		
	
	
	
}
