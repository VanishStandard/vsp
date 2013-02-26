package com.v_standard.vsp.script

import com.v_standard.vsp.utils.StringUtil
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}


/**
 * エスケープ無し文字列保持クラス。
 */
case class Raw(str: String)


/**
 * スクリプトファンクションクラス。
 */
class ScriptFunction(val out: ByteArrayOutputStream) {
	/**
	 * エスケープ無し文字列保持オブジェクト取得。
	 *
	 * @param str 文字列
	 * @return Raw オブジェクト
	 */
	def raw(str: String): Raw = Raw(str)

	/**
	 * エスケープ文字列取得。
	 *
	 * @param str 文字列
	 * @return 文字列
	 */
	def escape(str: String): String = StringUtil.htmlEscape(str)

	/**
	 * エスケープ無し文字列取得。
	 *
	 * @param raw エスケープ無し文字列保持オブジェクト
	 * @return 文字列
	 */
	def escape(raw: Raw): String = raw.str


	/**
	 * 日時フォーマット。
	 *
	 * @param pattern フォーマットパターン
	 * @param dt 日時
	 * @return フォーマット済み文字列
	 */
	def format(pattern: String, dt: Date): String = if (dt == null) "" else new SimpleDateFormat(pattern).format(dt)

	/**
	 * 日時フォーマット。
	 *
	 * @param pattern フォーマットパターン
	 * @param cal カレンダー
	 * @return フォーマット済み文字列
	 */
	def format(pattern: String, cal: Calendar): String = if (cal == null) "" else format(pattern, cal.getTime)

	/**
	 * 日時フォーマット。
	 *
	 * @param pattern フォーマットパターン
	 * @param opt 日時
	 * @return フォーマット済み文字列
	 */
	def format(pattern: String, opt: Option[_]): String = (opt: @unchecked) match {
		case None => ""
		case Some(dt: Date) => format(pattern, dt)
		case Some(c: Calendar) => format(pattern, c)
	}
}
