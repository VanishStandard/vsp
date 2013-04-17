package com.v_standard.vsp.compiler

import com.typesafe.scalalogging.slf4j.Logging
import scala.collection.mutable.ArrayBuffer
import scala.io.Source


/**
 * スクリプト変換オブジェクト。
 */
object ScriptConverter extends Logging {
	val FUNC_FORSEQ = """
function forseq(list, func) {
	var len = list.length();
	for (var i = 0; i < len; ++i) func(list.apply(i), i);
}
"""

	/**
	 * 変換。
	 *
	 * @param source テンプレートソース
	 * @param config トークン解析設定
	 * @param deep 深さ
	 * @return (スクリプト文字列, テキストのみフラグ)
	 */
	def convert(source: Source, config: TokenParseConfig, deep: Int = 0): (String, Boolean) = {
		val context = ScriptConverterContext(config, deep)
		TokenParser.exec(source, context)

		if (context.buffer.size > 0) {
			if (context.currentToken.isEmpty) context.buffer.foreach(context.addStringToken(_))
			else context.buffer.foreach(context.currentToken.get += _)
		}

		if (!context.currentToken.isEmpty) context.tokens += context.currentToken.get

		val sb = new StringBuilder
		context.tokens.foreach(t => sb.append(t.toScript))
		if (!context.textOnly && deep == 0) sb.append(FUNC_FORSEQ)
		val res = sb.toString
		logger.trace("Script converted\n" + res)
		(res, context.textOnly)
	}
}
