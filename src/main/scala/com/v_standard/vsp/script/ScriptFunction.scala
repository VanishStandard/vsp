package com.v_standard.vsp.script

import com.v_standard.vsp.utils.StringUtil


/**
 * エスケープ無し文字列保持クラス。
 */
case class Raw(str: String)


/**
 * スクリプトファンクションクラス。
 */
class ScriptFunction {
  /**
   * エスケープ無し文字列保持オブジェクト取得。
   *
   * @param str 文字列
   * @return Raw オブジェクト
   */
  def raw(str: String): Raw = Raw(str)

  /**
   * エスケープ文字列取得。
   *
   * @param str 文字列
   * @return 文字列
   */
  def escape(str: String): String = StringUtil.htmlEscape(str)

  /**
   * エスケープ無し文字列取得。
   *
   * @param raw エスケープ無し文字列保持オブジェクト
   * @return 文字列
   */
  def escape(raw: Raw): String = raw.str
}
