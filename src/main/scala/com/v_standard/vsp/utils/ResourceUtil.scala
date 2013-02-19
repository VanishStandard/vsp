package com.v_standard.vsp.utils

import scala.language.reflectiveCalls


/**
 * リソースユーティリティオブジェクト。
 */
object ResourceUtil {
	/**
	 * ローンパターン。
	 *
	 * @param r close() を実装するリソースオブジェクト。
	 * @param f リソース処理
	 */
	def using[Result, T <% { def close(): Unit }](r: T)(f: T => Result): Result = {
		try f(r) finally if (r != null) r.close()
	}
}
