package com.v_standard.vsp.script

import javax.script.{ScriptContext, ScriptEngineManager}
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import sun.org.mozilla.javascript.internal.NativeObject


/**
 * HtmlFunction テストスペッククラス。
 */
class HtmlFunctionSpec extends FunSpec with ShouldMatchers {
	class HtmlFunctionWrapper extends HtmlFunction(null) {
		override def checkboxTag(obj: Any, param: NativeObject): String = super.checkboxTag(obj, param)
		override def radioTag(obj: Any, param: NativeObject): String = super.radioTag(obj, param)
	}


	private def createParam(script: String): NativeObject = {
		val engine = new ScriptEngineManager().getEngineByName("JavaScript")
		engine.eval("param = " + script)
		engine.get("param").asInstanceOf[NativeObject]
	}


	describe("checkbox") {
		describe("パラメータ指定に value が無い") {
			it("IllegalArgumentException") {
				val html = new HtmlFunctionWrapper
				val e = evaluating { html.checkboxTag("5", createParam("{ name: 'chk' }")) } should produce
					[IllegalArgumentException]
				e.getMessage should be ("\"value\" is required.")
			}
		}

		describe("最小パラメータ") {
			describe("HTML") {
				it("HTML タグを返す") {
					val html = new HtmlFunctionWrapper
					val actual = html.checkboxTag("5", createParam("param = { value: \"1\" }"))
					actual should be ("""<input type="checkbox" value="1">""")
				}
			}

			describe("XHTML") {
				it("XHTML タグを返す") {
					val html = new HtmlFunctionWrapper
					val actual = html.checkboxTag("5", createParam("param = { value: \"1\", _xhtml: true }"))
					actual should be ("""<input type="checkbox" value="1" />""")
				}
			}
		}

		describe("値が一致") {
			describe("HTML") {
				it("HTML タグを返す") {
					val html = new HtmlFunctionWrapper
					val actual = html.checkboxTag("1", createParam("param = { value: \"1\" }"))
					actual should be ("""<input type="checkbox" value="1" checked>""")
				}
			}

			describe("XHTML") {
				it("XHTML タグを返す") {
					val html = new HtmlFunctionWrapper
					val actual = html.checkboxTag("1", createParam("param = { value: \"1\", _xhtml: true }"))
					actual should be ("""<input type="checkbox" value="1" checked="checked" />""")
				}
			}
		}

		describe("その他パラメータ") {
			describe("HTML") {
				it("HTML タグを返す") {
					val html = new HtmlFunctionWrapper
					val actual = html.checkboxTag("5",
						createParam("""param = { value: "1", name: "chk", id: "chk1", "class": "xxx" }"""))
					actual should be ("""<input type="checkbox" value="1" name="chk" id="chk1" class="xxx">""")
				}
			}
		}
	}

	describe("radio") {
		describe("パラメータ指定に value が無い") {
			it("IllegalArgumentException") {
				val html = new HtmlFunctionWrapper
				val e = evaluating { html.radioTag("5", createParam("{ name: 'chk' }")) } should produce
					[IllegalArgumentException]
				e.getMessage should be ("\"value\" is required.")
			}
		}

		describe("最小パラメータ") {
			describe("HTML") {
				it("HTML タグを返す") {
					val html = new HtmlFunctionWrapper
					val actual = html.radioTag("5", createParam("param = { value: \"1\" }"))
					actual should be ("""<input type="radio" value="1">""")
				}
			}

			describe("XHTML") {
				it("XHTML タグを返す") {
					val html = new HtmlFunctionWrapper
					val actual = html.radioTag("5", createParam("param = { value: \"1\", _xhtml: true }"))
					actual should be ("""<input type="radio" value="1" />""")
				}
			}
		}

		describe("値が一致") {
			describe("HTML") {
				it("HTML タグを返す") {
					val html = new HtmlFunctionWrapper
					val actual = html.radioTag("1", createParam("param = { value: \"1\" }"))
					actual should be ("""<input type="radio" value="1" checked>""")
				}
			}

			describe("XHTML") {
				it("XHTML タグを返す") {
					val html = new HtmlFunctionWrapper
					val actual = html.radioTag("1", createParam("param = { value: \"1\", _xhtml: true }"))
					actual should be ("""<input type="radio" value="1" checked="checked" />""")
				}
			}
		}

		describe("その他パラメータ") {
			describe("HTML") {
				it("HTML タグを返す") {
					val html = new HtmlFunctionWrapper
					val actual = html.radioTag("5",
						createParam("""param = { value: "1", name: "chk", id: "chk1", "class": "xxx" }"""))
					actual should be ("""<input type="radio" value="1" name="chk" id="chk1" class="xxx">""")
				}
			}
		}

		describe("デフォルト値") {
			describe("空文字") {
				it("チェック無し") {
					val html = new HtmlFunctionWrapper
					val actual = html.radioTag("",
						createParam("""param = { value: "1", name: "chk", id: "chk1", "class": "xxx" }"""))
					actual should be ("""<input type="radio" value="1" name="chk" id="chk1" class="xxx">""")
				}
			}

			describe("null") {
				it("チェック無し") {
					val html = new HtmlFunctionWrapper
					val actual = html.radioTag(null,
						createParam("""param = { value: "1", name: "chk", id: "chk1", "class": "xxx" }"""))
					actual should be ("""<input type="radio" value="1" name="chk" id="chk1" class="xxx">""")
				}
			}

			describe("None") {
				it("チェック無し") {
					val html = new HtmlFunctionWrapper
					val actual = html.radioTag(None,
						createParam("""param = { value: "1", name: "chk", id: "chk1", "class": "xxx" }"""))
					actual should be ("""<input type="radio" value="1" name="chk" id="chk1" class="xxx">""")
				}
			}

			describe("有り") {
				it("チェック有り") {
					val html = new HtmlFunctionWrapper
					val actual = html.radioTag("",
						createParam("""param = { value: "1", name: "chk", id: "chk1", "class": "xxx", _default: true }"""))
					actual should be ("""<input type="radio" value="1" name="chk" id="chk1" class="xxx" checked>""")
				}
			}
		}
	}
}
