package com.v_standard.vsp.script

import com.v_standard.vsp.utils.StringUtil
import java.io.ByteArrayOutputStream
import java.text.{DecimalFormat, SimpleDateFormat}
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
	 * 数値フォーマット。
	 *
	 * @param pattern フォーマットパターン
	 * @param num 数値
	 * @return フォーマット済み文字列
	 */
	def format(pattern: String, num: Long): String = new DecimalFormat(pattern).format(num)

	/**
	 * 数値フォーマット。
	 *
	 * @param pattern フォーマットパターン
	 * @param num 数値
	 * @return フォーマット済み文字列
	 */
	def format(pattern: String, num: Double): String = new DecimalFormat(pattern).format(num)

	/**
	 * フォーマット。
	 *
	 * @param pattern フォーマットパターン
	 * @param opt 値
	 * @return フォーマット済み文字列
	 */
	def format(pattern: String, opt: Option[_]): String = (opt: @unchecked) match {
		case None => ""
		case Some(v: Date) => format(pattern, v)
		case Some(v: Calendar) => format(pattern, v)
		case Some(v: Int) => format(pattern, v)
		case Some(v: Long) => format(pattern, v)
		case Some(v: Float) => format(pattern, v)
		case Some(v: Double) => format(pattern, v)
	}
}
