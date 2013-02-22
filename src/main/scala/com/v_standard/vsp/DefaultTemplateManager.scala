package com.v_standard.vsp


/**
 * デフォルトテンプレート管理オブジェクト。
 */
object DefaultTemplateManager extends TemplateManager[Template] {
	/**
	 * テンプレート作成。
	 *
	 * @param manager テンプレートデータ管理クラス
	 * @return テンプレート
	 */
	protected def createTemplate(manager: TemplateDataManager): Template = new Template(manager)
}
