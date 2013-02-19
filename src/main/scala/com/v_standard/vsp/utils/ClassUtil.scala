package com.v_standard.vsp.utils


/**
 * クラスユーティリティオブジェクト。
 */
object ClassUtil {
	/**
	 * クラスローダ取得。
	 *
	 * @return クラスローダ
	 */
	def classLoader: ClassLoader = {
		val t = Thread.currentThread
		val c = t.getContextClassLoader
		if (c != null) c else t.getClass.getClassLoader
	}
}
