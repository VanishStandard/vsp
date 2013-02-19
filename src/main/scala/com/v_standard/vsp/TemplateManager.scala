package com.v_standard.vsp

import com.v_standard.vsp.compiler.ScriptCompiler
import com.v_standard.vsp.compiler.ScriptData
import com.v_standard.vsp.compiler.TokenParseConfig
import com.v_standard.vsp.utils.ResourceUtil.using
import java.io.File
import java.io.FileNotFoundException
import java.util.Date
import java.util.concurrent.ConcurrentHashMap
import scala.collection.mutable
import scala.io.Source


/**
 * テンプレート管理クラス。
 */
case class TemplateManager(config: TemplateConfig) {
	/** テンプレートデータマップ */
	val templates = new ConcurrentHashMap[String, TemplateData]()


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


/**
 * テンプレート管理オブジェクト。
 */
object TemplateManager {
	/** デフォルト設定ファイル */
	val DEFAULT_CONFIG = "vsp.xml"


	/** テンプレートマネージャマップ */
	val managers = mutable.Map.empty[String, TemplateManager]


	/**
	 * 初期化。
	 *
	 * @param configName 設定ファイル名
	 */
	def init(configName: String = DEFAULT_CONFIG) {
		managers += (configName -> TemplateManager(TemplateConfig(configName)))
	}

	/**
	 * テンプレート管理クラス取得。
	 *
	 * @param configName 設定ファイル名
	 * @return テンプレート
	 */
	def apply(configName: String = DEFAULT_CONFIG): Template = {
		if (!managers.contains(configName)) throw new IllegalStateException("Not initialize \"" + configName + "\".")
		Template(managers(configName))
	}
}
