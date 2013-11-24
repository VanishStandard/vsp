package com.v_standard.vsp.compiler

import com.typesafe.scalalogging.slf4j.Logging
import com.v_standard.vsp.script.ScriptDefine
import com.v_standard.utils.ResourceUtil
import com.v_standard.utils.ResourceUtil.using
import java.io.File


/**
 * トークントレイト。
 */
trait Token {
	/** 文字列表現 */
	protected val tokenStr = new StringBuilder


	/**
	 * 文字追加。
	 *
	 * @param ch 文字
	 */
	def +=(ch: Char) = tokenStr.append(ch)

	/**
	 * 文字列追加。
	 *
	 * @param str 文字列
	 */
	def +=(str: String) = tokenStr.append(str)

	/**
	 * スクリプト表現取得。
	 *
	 * @return スクリプト表現
	 */
	def toScript: String
}


/**
 * 文字列トークンクラス。
 */
class StringToken() extends Token {
	override def toScript: String = {
		val sb = new StringBuilder
		tokenStr.foreach {
			case '\t' => sb.append("\\t")
			case '\r' => sb.append("\\r")
			case '\n' => sb.append("\\n")
			case '\\' => sb.append("\\\\")
			case '"' => sb.append("\\\"")
			case c => sb.append(c)
		}
		"print(\"" + sb.toString + "\");\n"
	}
}


/**
 * 出力トークンクラス。
 */
class PrintToken extends Token {
	override def toScript: String = {
		"print(" + ScriptDefine.SCRIPT_OBJ_NAME + ".escape((" + tokenStr.toString + " == null) ? \"\" : " +
		tokenStr.toString + "));\n"
	}
}


/**
 * 構文トークンクラス。
 */
class SyntaxToken extends Token {
	override def toScript: String = {
		tokenStr.toString + "\n"
	}
}


/**
 * インクルードトークンクラス。
 */
class IncludeToken(context: ScriptConverterContext) extends Token with Logging {
	override def toScript: String = {
		if (ScriptDefine.MAX_INCLUDE <= context.deep)
			throw new IllegalStateException("Failed to include. count(" + context.deep + ")")

		val f = new File(context.config.baseDir.getPath, tokenStr.toString.trim)
		logger.debug("Include file: " + f.getAbsolutePath)
		using(ResourceUtil.getSource(f)) { r =>
			val res = ScriptConverter.convert(r, context.config, context.deep + 1)
			if (!res._2) context.textOnly = false
			context.includeFiles += f.getCanonicalFile
			context.includeFiles ++= res._3
			res._1
		}
	}
}
