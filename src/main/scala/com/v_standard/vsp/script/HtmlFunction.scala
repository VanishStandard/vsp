package com.v_standard.vsp.script

import com.v_standard.utils.HtmlUtil
import java.io.ByteArrayOutputStream
import java.text.DecimalFormat
import sun.org.mozilla.javascript.internal.NativeArray
import sun.org.mozilla.javascript.internal.NativeObject
import scala.collection.JavaConverters._


/**
 * HTML ファンクションクラス。
 */
class HtmlFunction(val out: ByteArrayOutputStream, val isXhtml: Boolean) {
	/** パラメータ：値 */
	val PARAM_VALUE = "value"
	/** パラメータ：デフォルト */
	val PARAM_DEFAULT = "_default"
	/** パラメータ：リスト */
	val PARAM_LIST = "_list"


	/**
	 * チェックボックスタグ出力。
	 *
	 * @param obj 現在値オブジェクト
	 * @param パラメータ(必須: value, オプション: _xhtml)
	 */
	def checkbox(obj: Any, param: NativeObject): Unit = out.write(checkboxTag(obj, param).getBytes)

	/**
	 * ラジオボタンタグ出力。
	 *
	 * @param obj 現在値オブジェクト
	 * @param パラメータ(必須: value, オプション: _xhtml, _default)
	 */
	def radio(obj: Any, param: NativeObject): Unit = out.write(radioTag(obj, param).getBytes)

	/**
	 * セレクトボックスタグ出力。
	 *
	 * @param obj 現在値オブジェクト
	 * @param パラメータ(必須: _list, オプション: _xhtml, _default)
	 */
	def select(obj: Any, param: NativeObject): Unit = out.write(selectTag(obj, param).getBytes)


	/**
	 * 改行を <br /> に変換。
	 *
	 * @param str 文字列
	 * @return Raw オブジェクト
	 */
	def br(str: String): Raw = {
	 Raw(HtmlUtil.crlf2br(HtmlUtil.escape(str)))
	}

	/**
	 * 改行を <br /> に変換。
	 *
	 * @param str 文字列
	 * @return Raw オブジェクト
	 */
	def br(oc: OutputConverter): Raw = {
	 Raw(HtmlUtil.crlf2br(oc.mkString))
	}


	/**
	 * チェックボックスタグ生成。
	 *
	 * @param obj 現在値オブジェクト
	 * @param パラメータ
	 * @return タグ
	 */
	protected def checkboxTag(obj: Any, param: NativeObject): String = {
		checkRequiredParam(param, List(PARAM_VALUE))
		val tag = new StringBuilder("<input type=\"checkbox\"").append(createAttrByParam(param))
		val xhtml = isXhtml
		val currentValue = convertValue(obj)

		if (convertValue(param.get(PARAM_VALUE)) == currentValue) {
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
	 * @param obj 現在値オブジェクト
	 * @param パラメータ
	 * @return タグ
	 */
	protected def radioTag(obj: Any, param: NativeObject): String = {
		checkRequiredParam(param, List(PARAM_VALUE))
		val tag = new StringBuilder("<input type=\"radio\"").append(createAttrByParam(param))
		val xhtml = isXhtml
		val currentValue = convertValue(obj)

		if ((currentValue == null || currentValue == "") && getBoolean(param, PARAM_DEFAULT, false) ||
			convertValue(param.get(PARAM_VALUE)) == currentValue) {
			if (xhtml) tag.append(" checked=\"checked\"")
			else tag.append(" checked")
		}

		if (xhtml) tag.append(" />")
		else tag.append(">")
		tag.toString
	}

	/**
	 * セレクトタグ生成。
	 *
	 * @param obj 現在値オブジェクト
	 * @param パラメータ
	 * @return タグ
	 */
	protected def selectTag(obj: Any, param: NativeObject): String = {
		checkRequiredParam(param, List(PARAM_LIST))
		val tag = new StringBuilder("<select").append(createAttrByParam(param)).append(">")
		val xhtml = isXhtml
		val currentValue = convertValue(obj)

		if (param.containsKey(PARAM_DEFAULT)) {
			val entries = param.get(PARAM_DEFAULT).asInstanceOf[NativeObject].entrySet
			val it = entries.iterator
			if (it.hasNext) {
				val e = it.next
				tag.append(optionTag(e.getKey.toString, e.getValue.toString, currentValue, xhtml))
			}
		}

		convertOptionList(param).foreach(lv => tag.append(optionTag(lv._1, lv._2, currentValue, xhtml)))

		tag.append("</select>")
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
				attr.append(" "). append(key).append("=\"").append(HtmlUtil.escape(convertValue(e.getValue))).append("\"")
		}
		attr.toString
	}

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

	/**
	 * 値を文字列に変換
	 *
	 * @param 値
	 * @return 変換後の文字列
	 */
	private def convertValue(obj: Any): String = obj match {
		case null => null
		case v: Option[_] => v.map(x => convertValue(x)).getOrElse(null)
		case v: Double => new DecimalFormat("0.############").format(v)
		case v => v.toString
	}

	/**
	 * オプションのリスト変換。
	 *
	 * @param param パラメータ
	 * @return 変換後のリスト
	 */
	private def convertOptionList(param: NativeObject): Seq[(String, String)] = {
		param.get(PARAM_LIST) match {
			case lst: NativeArray => lst.asScala.map { l =>
				val e = l.asInstanceOf[NativeObject].entrySet.asScala.head
				(e.getKey.toString, e.getValue.toString)
			}.toSeq
			case lst: Seq[_] => lst.map {
				case (v, l) => (v.toString, l.toString)
			}
			case lst: Array[_] => lst.map {
				case (v, l) => (v.toString, l.toString)
			}
		}
	}

	/**
	 * <option> タグ生成。
	 *
	 * @param value 値
	 * @param label ラベル
	 * @param currentValue 現在値
	 * @param xhtml XHTML なら true
	 * @return タグ
	 */
	private def optionTag(value: String, label: String, currentValue: String, xhtml: Boolean): String = {
		val tag = new StringBuilder(s"""<option value="$value"""")
		if (value == currentValue) {
			if (xhtml) tag.append(" selected=\"selected\"")
			else tag.append(" selected")
		}
		tag.append(s">$label</option>")
		tag.toString
	}
}
