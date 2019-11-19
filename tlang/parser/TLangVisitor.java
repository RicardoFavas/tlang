// Generated from TLang.g4 by ANTLR 4.7.1

	package tlang.parser;
	import org.antlr.v4.misc.CharSupport;
	import com.yuvalshavit.antlr4.DenterHelper.DenterHelper;
	import com.yuvalshavit.antlr4.DenterHelper.DenterOptions;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link TLangParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface TLangVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link TLangParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(TLangParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link TLangParser#closedBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClosedBlock(TLangParser.ClosedBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link TLangParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(TLangParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link TLangParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp(TLangParser.ExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link TLangParser#param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam(TLangParser.ParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link TLangParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(TLangParser.LiteralContext ctx);
}