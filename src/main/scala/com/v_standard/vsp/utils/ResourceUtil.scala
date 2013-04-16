package com.v_standard.vsp.utils

import java.io.{File, FileNotFoundException}
import scala.io.Source
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

	/**
	 * ファイルソース取得。<br />
	 * 実ファイルがなければクラスパスより探索。
	 *
	 * @param file ファイル
	 * @return ファイルソース
	 */
	def getSource(file: File): Source = {
		if (file.exists) Source.fromFile(file)
		else {
			val stream = ClassUtil.classLoader.getResourceAsStream(file.getPath.replace("""\""", "/").replaceAll("^./", ""))
			if (stream == null) throw new FileNotFoundException(file.getPath)
			Source.fromInputStream(stream)
		}
	}
}
