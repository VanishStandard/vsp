package com.v_standard.vsp

import com.v_standard.utils.ClassUtil
import com.v_standard.utils.ResourceUtil.using
import com.v_standard.utils.StringUtil
import java.io.{File, FileNotFoundException}
import scala.xml.XML


/**
 * テンプレート設定クラス。
 */
case class TemplateConfig(templateDir: File, sign: Char, checkPeriod: Int, initCompileFilter: String)


/**
 * テンプレート設定オブジェクト。
 */
object TemplateConfig {
	/**
	 * ファイルより設定クラス生成。
	 *
	 * @param fileName 設定ファイル名
	 * @return テンプレート設定クラス
	 */
	def apply(fileName: String): TemplateConfig = {
		using(ClassUtil.classLoader.getResourceAsStream(fileName)) { r =>
			if (r == null) throw new FileNotFoundException(fileName)
			val root = XML.load(r)
			val tmplDir = StringUtil.trimWide((root \ "templateDir").text)
			TemplateConfig(
				new File(if (tmplDir.isEmpty) "./" else tmplDir),
				StringUtil.trimWide((root \ "sign").text)(0),
				StringUtil.trimWide((root \ "checkPeriod").text).toInt,
				StringUtil.trimWide((root \ "initCompileFilter").text))
		}
	}
}
