package com.v_standard.vsp

import com.v_standard.vsp.compiler.{ScriptCompiler, ScriptData, TokenParseConfig}
import com.v_standard.vsp.utils.ResourceUtil.using
import java.io.{File, FileNotFoundException}
import java.util.Date
import java.util.concurrent.ConcurrentHashMap
import scala.io.Source


/**
 * テンプレートデータ管理クラス。
 */
case class TemplateDataManager(config: TemplateConfig) {
	/** テンプレートデータマップ */
	private val templates = new ConcurrentHashMap[String, TemplateData]()


	/**
	 * スクリプトデータ取得。
	 *
	 * @param fileName ファイル名
	 * @return スクリプトデータ
	 */
	def getScriptData(fileName: String): ScriptData = {
		val file = new File(config.templateDir, fileName)
		if (!file.exists) throw new FileNotFoundException(fileName)
		val key = file.getAbsolutePath
		val td = templates.get(key)
		val newSd = if (td == null ||
			shouldReCompile(td.lastCheckDate, config.checkPeriod, file, td.scriptData.compileDate)) {
			using(Source.fromFile(file)) { r =>
				ScriptCompiler.compile(r, TokenParseConfig(config.templateDir, config.sign))
			}
		} else td.scriptData
		templates.put(key, TemplateData(newSd, new Date))
		newSd
	}


	/**
	 * 更新日付チェックが必要か。
	 *
	 * @return 必要なら true
	 */
	private def shouldCheckModify(checkDate: Date, checkPeriod: Int): Boolean =
		new Date().getTime > checkDate.getTime + checkPeriod * 1000

	/**
	 * 再コンパイルが必要か。
	 *
	 * @return 必要なら true
	 */
	private def shouldReCompile(checkDate: Date, checkPeriod: Int, file: File, compiledDate: Date): Boolean =
		if (!shouldCheckModify(checkDate, checkPeriod)) false else file.lastModified > compiledDate.getTime
}
