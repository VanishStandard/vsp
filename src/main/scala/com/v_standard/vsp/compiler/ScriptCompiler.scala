package com.v_standard.vsp.compiler

import java.io.{ByteArrayOutputStream, OutputStreamWriter}
import java.util.Date
import javax.script.{Compilable, CompiledScript, ScriptEngine, ScriptEngineManager}
import scala.io.Source


/**
 * スクリプトデータ。
 */
case class ScriptData(cscript: Option[CompiledScript], text: Option[String], compileDate: Date)


/**
 * スクリプトコンパイラーオブジェクト。
 */
object ScriptCompiler {
	/** スクリプトエンジンマネージャ */
	val engineManager = new ScriptEngineManager()
	/** コンパイル用スクリプトエンジン */
	val compileEngine = createScriptEngine().asInstanceOf[Compilable]


	/**
	 * スクリプトエンジン生成。
	 * 同マネージャより生成のため GLOBAL_SCOPE が同じ。
	 *
	 * @return スクリプトエンジン
	 */
	def createScriptEngine(): ScriptEngine = engineManager.getEngineByName("JavaScript")


	/**
	 * 変換。
	 *
	 * @param source テンプレートソース
	 * @param config トークン解析設定
	 * @param deep 深さ
	 * @return スクリプトデータ
	 */
	def compile(source: Source, config: TokenParseConfig): ScriptData = {
		try {
			val (script, textOnly) = ScriptConverter.convert(source, config)
			val cscript = compileEngine.compile(script)
			if (textOnly) {
				val out = new ByteArrayOutputStream()
				val context = createScriptEngine().getContext()
				context.setWriter(new OutputStreamWriter(out))
				cscript.eval(context)
				ScriptData(None, Option(out.toString()), new Date())
			}
			else ScriptData(Option(cscript), None, new Date())
		} catch {
			case e: Exception => ScriptData(None, Option(e.getMessage), new Date())
		}
	}
}