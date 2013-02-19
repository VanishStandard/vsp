package com.v_standard.vsp.utils

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers


/**
 * StringUtil テストスペッククラス。
 */
class StringUtilSpec extends FunSpec with ShouldMatchers {
	describe("htmlEscape") {
		describe("対象文字を与えた場合") {
			it("HTML エスケープされる") {
				StringUtil.htmlEscape("""<img src="01.jpg" alt='logo' />&amp;""") should be ("&lt;img src=&quot;01.jpg&quot; alt=&#39;logo&#39; /&gt;&amp;amp;")
			}
		}

		describe("対象外の文字列を与えた場合") {
			it("そのまま返されるされる") {
				StringUtil.htmlEscape("abcdefg") should be ("abcdefg")
			}
		}
	}

	describe("trimWide") {
		it("全角スペースもトリムされる") {
			val str = " \t　abc\t 　"
			StringUtil.trimWide(str) should be ("abc")
		}
	}
}
