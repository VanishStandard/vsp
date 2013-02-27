package com.v_standard.vsp.utils


/**
 * 文字列ユーティリティオブジェクト。
 */
object StringUtil {
	/**
	 * HTML エスケープ。
	 *
	 * @param str 文字列
	 * @return エスケープ文字列
	 */
	def htmlEscape(str: String): String = {
		assert(str != null, "str is null")
		val res = new StringBuilder
		str.foreach {
			case '&' => res.append("&amp;")
			case '<' => res.append("&lt;")
			case '>' => res.append("&gt;")
			case '"' => res.append("&quot;")
			case '\'' => res.append("&#39;")
			case ch => res.append(ch)
		}
		res.toString
	}

	/**
	 * 改行を <br /> に変換。
	 *
	 * @param str 変換対象文字列
	 * @return 変換後文字列
	 */
	def crlf2br(str: String): String = {
		assert(str != null, "str is null")
		var preCr = false
		val res = new StringBuilder
		str.foreach {
			case '\r' => res.append("<br />"); preCr = true
			case '\n' => if (!preCr) res.append("<br />"); preCr = false
			case ch => res.append(ch); preCr = false
		}
		res.toString
	}

	/**
	 * トリム。
	 * 2 バイトスペースもトリム。
	 *
	 * @param str 文字列
	 * @return トリムされた文字列
	 */
	def trimWide(str: String): String = {
		assert(str != null, "str is null")
		var len = str.length
		var start = 0
		val chars = str.toCharArray()

		while (start < len && (chars(start) <= ' ' || chars(start) == '　')) start += 1
		while (start < len && (chars(len - 1) <= ' ' || chars(len - 1) == '　')) len -= 1
		if (start > 0 || len < str.length()) str.substring(start, len)
		else str
	}
}
