package com.v_standard.vsp

import java.nio.file.{FileSystems, FileVisitResult, Files, Path, SimpleFileVisitor}
import java.nio.file.attribute.BasicFileAttributes
import scala.actors.Actor.actor
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


/**
 * テンプレート管理クラス。
 * テンプレート管理オブジェクトの基本クラス。
 */
abstract class TemplateManager[T] {
	/** デフォルト設定ファイル */
	val DEFAULT_CONFIG = "vsp.xml"

	/** 拡張 */
	private val extensions: ArrayBuffer[TemplateExtension] = ArrayBuffer(new DefaultTemplateExtension)


	/** テンプレートマネージャマップ */
	private val managers = mutable.Map.empty[String, TemplateDataManager]


	/**
	 * 初期化。
	 *
	 * @param configName 設定ファイル名
	 */
	def init(configName: String = DEFAULT_CONFIG) {
		val conf = TemplateConfig(configName)
		val manager = TemplateDataManager(conf)
		managers += (configName -> manager)
		if (!conf.initCompileFilter.isEmpty) initCompileFiles(manager)
	}

	/**
	 * テンプレートクラス取得。
	 *
	 * @param configName 設定ファイル名
	 * @return テンプレート
	 */
	def template(configName: String = DEFAULT_CONFIG): T = {
		if (!managers.contains(configName)) throw new IllegalStateException("Not initialize \"" + configName + "\".")
		createTemplate(managers(configName))
	}

	/**
	 * 拡張追加。
	 */
	def addExtension(extension: TemplateExtension) = extensions += extension


	/**
	 * テンプレート作成。
	 *
	 * @param manager テンプレートデータ管理クラス
	 * @return テンプレート
	 */
	protected def createTemplate(manager: TemplateDataManager): T

	/**
	 * 初期化時コンパイル。
	 *
	 * @param manager テンプレート管理
	 */
	private def initCompileFiles(manager: TemplateDataManager) {
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
