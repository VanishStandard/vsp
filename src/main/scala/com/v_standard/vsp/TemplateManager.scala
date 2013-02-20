package com.v_standard.vsp

import com.v_standard.vsp.compiler.{ScriptCompiler, ScriptData, TokenParseConfig}
import com.v_standard.vsp.utils.ResourceUtil.using
import java.io.{File, FileNotFoundException}
import java.nio.file.{FileSystems, FileVisitResult, Files, Path, SimpleFileVisitor}
import java.nio.file.attribute.BasicFileAttributes
import java.util.Date
import java.util.concurrent.ConcurrentHashMap
import scala.actors.Actor.actor
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.io.Source


/**
 * テンプレート管理クラス。
 */
case class TemplateManager(config: TemplateConfig) {
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


/**
 * テンプレート管理オブジェクト。
 */
object TemplateManager {
	/** デフォルト設定ファイル */
	val DEFAULT_CONFIG = "vsp.xml"

	/** 拡張 */
	private val extensions: ArrayBuffer[TemplateExtension] = ArrayBuffer(new DefaultTemplateExtension)


	/** テンプレートマネージャマップ */
	private val managers = mutable.Map.empty[String, TemplateManager]


	/**
	 * 初期化。
	 *
	 * @param configName 設定ファイル名
	 */
	def init(configName: String = DEFAULT_CONFIG) {
		val conf = TemplateConfig(configName)
		val manager = TemplateManager(conf)
		managers += (configName -> manager)
		if (!conf.initCompileFilter.isEmpty) initCompileFiles(manager)
	}

	/**
	 * テンプレート管理クラス取得。
	 *
	 * @param configName 設定ファイル名
	 * @return テンプレート
	 */
	def apply(configName: String = DEFAULT_CONFIG): Template = {
		if (!managers.contains(configName)) throw new IllegalStateException("Not initialize \"" + configName + "\".")
		extensions.last.template(managers(configName))
	}

	/**
	 * 拡張追加。
	 */
	def addExtension(extension: TemplateExtension) = extensions += extension


	/**
	 * 初期化時コンパイル。
	 *
	 * @param manager テンプレート管理
	 */
	private def initCompileFiles(manager: TemplateManager) {
		actor {
			val root = manager.config.templateDir.toPath
			val matcher = FileSystems.getDefault().getPathMatcher("regex:" + manager.config.initCompileFilter)

			Files.walkFileTree(root, new SimpleFileVisitor[Path]() {
				override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
					if (matcher.matches(file)) {
						actor {
							manager.getScriptData(root.relativize(file).toString)
						}
					}
					return FileVisitResult.CONTINUE
				}
			})
		}
	}
}
