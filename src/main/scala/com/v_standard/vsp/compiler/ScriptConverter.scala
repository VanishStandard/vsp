package com.v_standard.vsp.compiler

import com.v_standard.vsp.utils.State
import java.io.File
import scala.collection.mutable.ArrayBuffer
import scala.io.Source


/**
 * スクリプト変換オブジェクト。
 */
object ScriptConverter {
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

		val res = new StringBuilder
		context.tokens.foreach(t => res.append(t.toScript))
		(res.toString, context.textOnly)
	}
}
