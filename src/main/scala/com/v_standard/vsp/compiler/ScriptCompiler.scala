package com.v_standard.vsp.compiler

import com.v_standard.utils.ResourceUtil.using
import java.io.{ByteArrayOutputStream, File, OutputStreamWriter}
import java.util.Date
import javax.script.{Compilable, CompiledScript, ScriptEngine, ScriptEngineManager}
import scala.io.Source


/**
 * スクリプトデータ。
 */
case class ScriptData(cscript: Option[CompiledScript], text: Option[String], compileDate: Date, includeFiles: Set[File])


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
		var jsSrc: String = ""
		try {
			val (script, textOnly, includeFiles) = ScriptConverter.convert(source, config)
			jsSrc = script
			val cscript = compileEngine.compile(script)
			if (textOnly) {
				val out = new ByteArrayOutputStream()
				val context = createScriptEngine().getContext()
				context.setWriter(new OutputStreamWriter(out))
				cscript.eval(context)
				ScriptData(None, Option(out.toString()), new Date(), includeFiles.toSet)
			}
			else ScriptData(Option(cscript), None, new Date(), includeFiles.toSet)
		} catch {
			case e: Exception =>
				ScriptData(None, Option(e.getMessage +
					using(Source.fromString(jsSrc)) { r =>
						var count = 0
						val sb = new StringBuilder("\n")
						r.getLines.foreach { l =>
							count += 1
							sb.append("%04d".format(count)).append(": ").append(l).append("\n")
						}
						sb.toString
					}
				), new Date(), Set.empty[File])
		}
	}
}
