package com.v_standard.vsp

import com.typesafe.scalalogging.slf4j.Logging
import com.v_standard.vsp.compiler.{ScriptCompiler, ScriptData, TokenParseConfig}
import com.v_standard.vsp.utils.{ClassUtil, ResourceUtil}
import com.v_standard.vsp.utils.ResourceUtil.using
import java.io.{File, FileNotFoundException}
import java.util.Date
import java.util.concurrent.ConcurrentHashMap
import scala.io.Source


/**
 * テンプレートデータ管理クラス。
 */
case class TemplateDataManager(config: TemplateConfig) extends Logging {
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
		val key = file.getAbsolutePath
		logger.debug("Template data key: " + key)
		val td = templates.get(key)
		val newTd = if (td == null || shouldCheckModify(td.lastCheckDate, config.checkPeriod)) {
			if (td == null || shouldReCompile(file, td.scriptData.compileDate)) {
				TemplateData(using(ResourceUtil.getSource(file)) { r =>
					ScriptCompiler.compile(r, TokenParseConfig(config.templateDir, config.sign))
				}, new Date)
			} else TemplateData(td.scriptData, new Date)
		} else td

		templates.put(key, newTd)
		newTd.scriptData
	}


	/**
	 * 更新日付チェックが必要か。
	 *
	 * @param checkDate 最終チェック日時
	 * @param checkPeriod チェック期間
	 * @return 必要なら true
	 */
	private def shouldCheckModify(checkDate: Date, checkPeriod: Int): Boolean =
		new Date().getTime > checkDate.getTime + checkPeriod * 1000

	/**
	 * 再コンパイルが必要か。
	 *
	 * @param file テンプレートファイル
	 * @param コンパイル日時
	 * @return 必要なら true
	 */
	private def shouldReCompile(file: File, compiledDate: Date): Boolean = file.lastModified > compiledDate.getTime
}
