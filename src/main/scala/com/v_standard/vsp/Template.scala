package com.v_standard.vsp

import com.v_standard.vsp.compiler.ScriptCompiler
import com.v_standard.vsp.script.ScriptDefine
import com.v_standard.vsp.script.ScriptFunction
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter


/**
 * テンプレートクラス。
 */
case class Template(manager: TemplateManager) {
	/** スクリプトエンジン */
	lazy val engine = ScriptCompiler.createScriptEngine()


	/**
	 * 変数追加。
	 *
	 * @param v 変数
	 */
	def addVar(v: (String, Any)) {
		engine.put(v._1, v._2)
	}

	/**
	 * 構築。
	 *
	 * @param fileName 元ファイル
	 * @param vars 変数
	 */
	def build(fileName: String, vars: (String, Any)*): String = {
		vars.foreach(v => engine.put(v._1, v._2))
		val sd = manager.getScriptData(fileName)
		sd.text.getOrElse {
			val out = new ByteArrayOutputStream()
			engine.put(ScriptDefine.SCRIPT_OBJ_NAME, new ScriptFunction())
			val context = engine.getContext()
			context.setWriter(new OutputStreamWriter(out))
			sd.cscript.get.eval(context)
			out.toString()
		}
	}
}
