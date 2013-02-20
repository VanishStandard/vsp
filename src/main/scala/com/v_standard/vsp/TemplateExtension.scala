package com.v_standard.vsp


/**
 * テンプレート拡張トレイト。
 */
trait TemplateExtension {
	/**
	 * テンプレートオブジェクト取得。
	 *
	 * @param manager テンプレート管理クラス
	 * @return
	 */
	def template(manager: TemplateManager): Template
}


/**
 * デフォルトテンプレート拡張トレイト。
 */
class DefaultTemplateExtension extends TemplateExtension {
	/**
	 * テンプレートオブジェクト取得。
	 *
	 * @param manager テンプレート管理クラス
	 * @return
	 */
	def template(manager: TemplateManager): Template = {
		new Template(manager)
	}
}
