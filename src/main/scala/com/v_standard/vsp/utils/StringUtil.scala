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
		str.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").
			replaceAll("\"", "&quot;").replaceAll("'", "&#39;")
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
