package com.v_standard.vsp.script

import javax.script.ScriptEngine
import javax.script.{ScriptContext, ScriptEngineManager}
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import sun.org.mozilla.javascript.internal.NativeObject


/**
 * HtmlFunction テストスペッククラス。
 */
class HtmlFunctionSpec extends FunSpec with ShouldMatchers {
	class HtmlFunctionWrapper(isXhtml: Boolean = false) extends HtmlFunction(null, isXhtml) {
		override def checkboxTag(obj: Any, param: NativeObject): String = super.checkboxTag(obj, param)
		override def radioTag(obj: Any, param: NativeObject): String = super.radioTag(obj, param)
		override def selectTag(obj: Any, param: NativeObject): String = super.selectTag(obj, param)
	}


	private def createParam(script: String, binds: (String, Any)*): NativeObject = {
		val engine = new ScriptEngineManager().getEngineByName("JavaScript")
		binds.foreach(t => engine.put(t._1, t._2))
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
					val html = new HtmlFunctionWrapper(true)
					val actual = html.checkboxTag("5", createParam("param = { value: \"1\" }"))
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
					val html = new HtmlFunctionWrapper(true)
					val actual = html.checkboxTag("1", createParam("param = { value: \"1\" }"))
					actual should be ("""<input type="checkbox" value="1" checked="checked" />""")
				}
			}

			describe("現在値が数値") {
				it("選択される") {
					val html = new HtmlFunctionWrapper
					val actual = html.checkboxTag(1, createParam("param = { value: \"1\" }"))
					actual should be ("""<input type="checkbox" value="1" checked>""")
				}
			}

			describe("value が数値") {
				it("選択される") {
					val html = new HtmlFunctionWrapper
					val actual = html.checkboxTag("1", createParam("param = { value: 1 }"))
					actual should be ("""<input type="checkbox" value="1" checked>""")
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
					val html = new HtmlFunctionWrapper(true)
					val actual = html.radioTag("5", createParam("param = { value: \"1\" }"))
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
					val html = new HtmlFunctionWrapper(true)
					val actual = html.radioTag("1", createParam("param = { value: \"1\" }"))
					actual should be ("""<input type="radio" value="1" checked="checked" />""")
				}
			}

			describe("現在値が数値") {
				it("選択される") {
					val html = new HtmlFunctionWrapper
					val actual = html.radioTag(1, createParam("param = { value: \"1\" }"))
					actual should be ("""<input type="radio" value="1" checked>""")
				}
			}

			describe("value が数値") {
				it("選択される") {
					val html = new HtmlFunctionWrapper
					val actual = html.radioTag("1", createParam("param = { value: 1 }"))
					actual should be ("""<input type="radio" value="1" checked>""")
				}
			}
		}

		describe("その他パラメータ") {
			describe("HTML") {
				it("HTML タグを返す") {
					val html = new HtmlFunctionWrapper
					val actual = html.radioTag("5",
						createParam("""param = { value: "1", name: "rd", id: "rd1", "class": "xxx" }"""))
					actual should be ("""<input type="radio" value="1" name="rd" id="rd1" class="xxx">""")
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

	describe("select") {
		describe("パラメータ指定に _list が無い") {
			it("IllegalArgumentException") {
				val html = new HtmlFunctionWrapper
				val e = evaluating { html.selectTag("5", createParam("{value: 3 }")) } should produce
					[IllegalArgumentException]
				e.getMessage should be ("\"_list\" is required.")
			}
		}

		describe("値が一致") {
			describe("_list は JavaScript の配列") {
				describe("HTML") {
					it("HTML タグを返す") {
						val html = new HtmlFunctionWrapper
						val actual = html.selectTag("2", createParam("param = { _list: [{1: 'a'}, {2: 'b'}, {3: 'c'}] }"))
						actual should be ("""<select><option value="1">a</option><option value="2" selected>b</option>""" +
							"""<option value="3">c</option></select>""")
					}
				}

				describe("XHTML") {
					it("XHTML タグを返す") {
						val html = new HtmlFunctionWrapper(true)
						val actual = html.selectTag("2",
							createParam("param = { _list: [{1: 'a'}, {2: 'b'}, {3: 'c'}] }"))
						actual should be ("""<select><option value="1">a</option>""" +
							"""<option value="2" selected="selected">b</option><option value="3">c</option></select>""")
					}
				}
			}
		}

		describe("_list は List") {
			describe("HTML") {
				it("HTML タグを返す") {
					val html = new HtmlFunctionWrapper
					val actual = html.selectTag("2", createParam("param = { _list: options }", ("options",
						List("1" -> "a", "2" -> "b", "3" -> "c"))))
					actual should be ("""<select><option value="1">a</option><option value="2" selected>b</option>""" +
						"""<option value="3">c</option></select>""")
				}
			}
		}

		describe("_list は Array") {
			describe("HTML") {
				it("HTML タグを返す") {
					val html = new HtmlFunctionWrapper
					val actual = html.selectTag("2", createParam("param = { _list: options }", ("options",
						Array("1" -> "a", "2" -> "b", "3" -> "c"))))
					actual should be ("""<select><option value="1">a</option><option value="2" selected>b</option>""" +
						"""<option value="3">c</option></select>""")
				}
			}
		}

		describe("その他パラメータ") {
			describe("HTML") {
				it("HTML タグを返す") {
					val html = new HtmlFunctionWrapper
					val actual = html.selectTag("2", createParam("param = { _list: [{1: 'a'}, {2: 'b'}, {3: 'c'}], " +
						"""name: "slt", id: "slt1", "class": "xxx" }"""))
					actual should be ("""<select name="slt" id="slt1" class="xxx"><option value="1">a</option>""" +
						"""<option value="2" selected>b</option><option value="3">c</option></select>""")
				}
			}
		}

		describe("デフォルト値") {
			it("最初に挿入される") {
				val html = new HtmlFunctionWrapper
				val actual = html.selectTag("2", createParam("param = { _list: [{1: 'a'}, {2: 'b'}, {3: 'c'}], " +
					"""_default: { "": "選択してください" } }"""))
				actual should be ("""<select><option value="">選択してください</option><option value="1">a</option>""" +
					"""<option value="2" selected>b</option><option value="3">c</option></select>""")
			}
		}
	}

	describe("br") {
		val html = new HtmlFunctionWrapper
		describe("文字列を与えた場合") {
			it("エスケープ後、改行を <br /> に変換した文字列を返す") {
				val actual = html.br("a\n<b>\rc\r\nd").mkString
				actual should be ("a<br />&lt;b&gt;<br />c<br />d")
			}
		}

		describe("Raw を与えた場合") {
			it("エスケープせず、改行を <br /> に変換した文字列を返す") {
				val actual = html.br(Raw("a\n<b>\rc\r\nd")).mkString
				actual should be ("a<br /><b><br />c<br />d")
			}
		}
	}
}
