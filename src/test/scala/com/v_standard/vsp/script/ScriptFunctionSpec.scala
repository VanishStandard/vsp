package com.v_standard.vsp.script

import java.util.{Calendar, Date}
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers


/**
 * ScriptFunction テストスペッククラス。
 */
class ScriptFunctionSpec extends FunSpec with ShouldMatchers {
	describe("row") {
		val sf = new ScriptFunction(null)
		describe("文字を与えた場合") {
			it("Row を返す") {

				sf.raw("abcd") should be (Raw("abcd"))
			}
		}

		describe("escape") {
			val sf = new ScriptFunction(null)
			describe("文字を与えた場合") {
				it("HTML エスケープされた文字列を返す") {

					sf.escape("<body>'A&B'") should be ("&lt;body&gt;&#39;A&amp;B&#39;")
				}
			}

			describe("Row を与えた場合") {
				it("HTML エスケープ無しの文字列を返す") {

					sf.escape(sf.raw("<body>'A&B'")) should be ("<body>'A&B'")
				}
			}
		}
	}

	describe("format") {
		val sf = new ScriptFunction(null)
		val cal = Calendar.getInstance
		cal.set(2000, 2, 9, 2, 3, 1)

		describe("Date 型") {
			describe("値を与えた場合") {
				it("フォーマットされた文字列を返す") {
					sf.format("yyyy/MM/dd hh:mm:ss", cal.getTime) should be ("2000/03/09 02:03:01")
				}
			}

			describe("null を与えた場合") {
				it("フォーマットされた文字列を返す") {
					val dt: Date = null
					sf.format("yyyy/MM/dd hh:mm:ss", dt) should be ("")
				}
			}
		}

		describe("Calendar 型") {
			describe("値を与えた場合") {
				it("フォーマットされた文字列を返す") {
					sf.format("yyyy/MM/dd hh:mm:ss", cal) should be ("2000/03/09 02:03:01")
				}
			}

			describe("null を与えた場合") {
				it("フォーマットされた文字列を返す") {
					val dt: Calendar = null
					sf.format("yyyy/MM/dd hh:mm:ss", dt) should be ("")
				}
			}
		}

		describe("Option 型") {
			describe("Option[Date] を与えた場合") {
				it("フォーマットされた文字列を返す") {
					sf.format("yyyy/MM/dd hh:mm:ss", Some(cal.getTime)) should be ("2000/03/09 02:03:01")
				}
			}

			describe("Option[Calendar] を与えた場合") {
				it("フォーマットされた文字列を返す") {
					sf.format("yyyy/MM/dd hh:mm:ss", Some(cal)) should be ("2000/03/09 02:03:01")
				}
			}

			describe("None を与えた場合") {
				it("フォーマットされた文字列を返す") {
					val dt: Date = null
					sf.format("yyyy/MM/dd hh:mm:ss", None) should be ("")
				}
			}

			describe("Option[String] を与えた場合") {
				it("フォーマットされた文字列を返す") {
					evaluating { sf.format("yyyy/MM/dd hh:mm:ss", Some("2000/03/09 02:03:01")) } should produce [MatchError]
				}
			}
		}
	}
}
