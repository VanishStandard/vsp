package com.v_standard.vsp.script

import com.v_standard.vsp.utils.StringUtil
import java.io.ByteArrayOutputStream
import sun.org.mozilla.javascript.internal.NativeObject
import scala.collection.JavaConverters._


/**
 * HTML ファンクションクラス。
 */
class HtmlFunction(val out: ByteArrayOutputStream) {
	/** パラメータ：XHTML フラグ */
	val PARAM_XHTML = "_xhtml"
	/** パラメータ：デフォルト */
	val PARAM_DEFAULT = "_default"


	/**
	 * チェックボックスタグ出力。
	 *
	 * @param obj 現在の値オブジェクト
	 * @param パラメータ(必須: value, オプション: _xhtml)
	 */
	def checkbox(obj: Any, param: NativeObject): Unit = out.write(checkboxTag(obj, param).getBytes)

	/**
	 * ラジオボタンタグ出力。
	 *
	 * @param obj 現在の値オブジェクト
	 * @param パラメータ(必須: value, オプション: _xhtml, _default)
	 */
	def radio(obj: Any, param: NativeObject): Unit = out.write(radioTag(obj, param).getBytes)


	/**
	 * チェックボックスタグ生成。
	 *
	 * @param obj 現在の値オブジェクト
	 * @param パラメータ
	 * @return タグ
	 */
	protected def checkboxTag(obj: Any, param: NativeObject): String = {
		checkRequiredParam(param, List("value"))
		val tag = new StringBuilder("<input type=\"checkbox\"").append(createAttrByParam(param))
		val xhtml = isXhtml(param)
		if (param.get("value") == obj) {
			if (xhtml) tag.append(" checked=\"checked\"")
			else tag.append(" checked")
		}

		if (xhtml) tag.append(" />")
		else tag.append(">")
		tag.toString
	}

	/**
	 * ラジオボタンタグ生成。
	 *
	 * @param obj 現在の値オブジェクト
	 * @param パラメータ
	 * @return タグ
	 */
	protected def radioTag(obj: Any, param: NativeObject): String = {
		checkRequiredParam(param, List("value"))
		val tag = new StringBuilder("<input type=\"radio\"").append(createAttrByParam(param))
		val xhtml = isXhtml(param)

		val currentValue = obj match {
			case None => null
			case Some(v) => v
			case v => v
		}

		if ((currentValue == null || currentValue.toString == "") && getBoolean(param, PARAM_DEFAULT, false) ||
			param.get("value") == currentValue) {
			if (xhtml) tag.append(" checked=\"checked\"")
			else tag.append(" checked")
		}

		if (xhtml) tag.append(" />")
		else tag.append(">")
		tag.toString
	}


	/**
	 * 必須パラメータのチェック。
	 *
	 * @param param パラメータ
	 * @param names 名前のリスト
	 */
	private def checkRequiredParam(param: NativeObject, names: Iterable[String]) {
		names.foreach(n => if (!param.containsKey(n)) throw new IllegalArgumentException(s""""$n" is required."""))
	}

	/**
	 * パラメータより属性生成。
	 *
	 * @param param パラメータ
	 * @return 属性タグ
	 */
	private def createAttrByParam(param: NativeObject) = {
		val attr = new StringBuilder
		param.entrySet.asScala.foreach { e =>
			val key = e.getKey.toString
			if (!key.startsWith("_"))
				attr.append(" "). append(key).append("=\"").append(StringUtil.htmlEscape(e.getValue.toString)).append("\"")
		}
		attr.toString
	}

	/**
	 * XHTML かどうか。
	 *
	 * @param param パラメータ
	 * @return XHTML なら true
	 */
	private def isXhtml(param: NativeObject) = getBoolean(param, PARAM_XHTML, false)

	/**
	 * bool 値取得。
	 *
	 * @param param パラメータ
	 * @param name 名前
	 * @param default キーが無かった場合の値
	 * @return bool 値
	 */
	private def getBoolean(param: NativeObject, name: String, default: Boolean) = {
		if (!param.containsKey(name)) default
		else param.get(name).asInstanceOf[Boolean]
	}
}
