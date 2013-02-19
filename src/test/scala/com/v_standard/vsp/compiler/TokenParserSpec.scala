package com.v_standard.vsp.compiler

import com.v_standard.vsp.script.ScriptDefine
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import scala.io.Source


/**
 * TokenParser テストスペッククラス。
 */
class TokenParserSpec extends FunSpec with ShouldMatchers {
	describe("StateStart") {
		describe("記号の場合") {
			val context = ScriptConverterContext(TokenParseConfig(null, '%'))
			val res = TokenParser.StateStart.doEvent('%', context)

			it("バッファに詰める") {
				context.buffer.toString should be ("%")
			}
			it("StateAfterSign を返す") {
				res should be (TokenParser.StateAfterSign)
			}
		}

		describe("< の場合") {
			val context = ScriptConverterContext(TokenParseConfig(null, '%'))
			val res = TokenParser.StateStart.doEvent('<', context)

			it("バッファに詰める") {
				context.buffer.toString should be ("<")
			}
			it("StateAfterSign を返す") {
				res should be (TokenParser.StateAfterChevronOpen)
			}
		}

		describe("上記以外場合") {
			val context = ScriptConverterContext(TokenParseConfig(null, '%'))
			val res = TokenParser.StateStart.doEvent('A', context)

			it("バッファクリア") {
				context.buffer.toString should be ("")
			}
			it("StateStart を返す") {
				res should be (TokenParser.StateStart)
			}
		}
	}

	describe("StateAfterSign") {
		describe("{ の場合") {
			val context = ScriptConverterContext(TokenParseConfig(null, '%'))
			val res = TokenParser.StateAfterSign.doEvent('{', context)

			it("バッファクリア") {
				context.buffer.toString should be ("")
			}
			it("StatePrintMode を返す") {
				res should be (TokenParser.StatePrintMode)
			}
		}

		describe("{ 以外場合") {
			val context = ScriptConverterContext(TokenParseConfig(null, '%'))
			val res = TokenParser.StateAfterSign.doEvent('A', context)

			it("バッファクリア") {
				context.buffer.toString should be ("")
			}
			it("StateStart を返す") {
				res should be (TokenParser.StateStart)
			}
		}
	}

	describe("StatePrintMode") {
		describe("} の場合") {
			val context = ScriptConverterContext(TokenParseConfig(null, '%'))
			val res = TokenParser.StatePrintMode.doEvent('}', context)

			it("バッファクリア") {
				context.buffer.toString should be ("")
			}
			it("StateStart を返す") {
				res should be (TokenParser.StateStart)
			}
		}

		describe("{ 以外場合") {
			val context = ScriptConverterContext(TokenParseConfig(null, '%'))
			val res = TokenParser.StatePrintMode.doEvent('A', context)

			it("バッファクリア") {
				context.buffer.toString should be ("")
			}
			it("StatePrintMode を返す") {
				res should be (TokenParser.StatePrintMode)
			}
		}
	}

	describe("StateAfterChevronOpen") {
		describe("記号の場合") {
			val context = ScriptConverterContext(TokenParseConfig(null, '%'))
			val res = TokenParser.StateAfterChevronOpen.doEvent('%', context)

			it("バッファクリア") {
				context.buffer.toString should be ("")
			}
			it("StateSyntaxMode を返す") {
				res should be (TokenParser.StateStartSyntaxMode)
			}
		}

		describe("記号以外の場合") {
			val context = ScriptConverterContext(TokenParseConfig(null, '%'))
			val res = TokenParser.StateAfterChevronOpen.doEvent('A', context)

			it("バッファクリア") {
				context.buffer.toString should be ("")
			}
			it("StateStart を返す") {
				res should be (TokenParser.StateStart)
			}
		}
	}

	describe("StateStartSyntaxMode") {
		describe("記号の場合") {
			val context = ScriptConverterContext(TokenParseConfig(null, '%'))
			val res = TokenParser.StateStartSyntaxMode.doEvent('%', context)

			it("バッファに詰める") {
				context.buffer.toString should be ("%")
			}
			it("StateSyntaxModeAfterSign を返す") {
				res should be (TokenParser.StateSyntaxModeAfterSign)
			}
		}

		describe("/ の場合") {
			val context = ScriptConverterContext(TokenParseConfig(null, '%'))
			val res = TokenParser.StateStartSyntaxMode.doEvent('/', context)

			it("バッファクリア") {
				context.buffer.toString should be ("")
			}
			it("StateIncludeMode を返す") {
				res should be (TokenParser.StateIncludeMode)
			}
		}

		describe("記号以外の場合") {
			val context = ScriptConverterContext(TokenParseConfig(null, '%'))
			val res = TokenParser.StateSyntaxMode.doEvent('A', context)

			it("バッファクリア") {
				context.buffer.toString should be ("")
			}
			it("StateSyntaxMode を返す") {
				res should be (TokenParser.StateSyntaxMode)
			}
		}
	}

	describe("StateSyntaxMode") {
		describe("記号の場合") {
			val context = ScriptConverterContext(TokenParseConfig(null, '%'))
			val res = TokenParser.StateSyntaxMode.doEvent('%', context)

			it("バッファに詰める") {
				context.buffer.toString should be ("%")
			}
			it("StateSyntaxModeAfterSign を返す") {
				res should be (TokenParser.StateSyntaxModeAfterSign)
			}
		}

		describe("記号以外の場合") {
			val context = ScriptConverterContext(TokenParseConfig(null, '%'))
			val res = TokenParser.StateSyntaxMode.doEvent('A', context)

			it("バッファクリア") {
				context.buffer.toString should be ("")
			}
			it("StateSyntaxMode を返す") {
				res should be (TokenParser.StateSyntaxMode)
			}
		}
	}

	describe("StateSyntaxModeAfterSign") {
		describe("> の場合") {
			val context = ScriptConverterContext(TokenParseConfig(null, '%'))
			val res = TokenParser.StateSyntaxModeAfterSign.doEvent('>', context)

			it("バッファクリア") {
				context.buffer.toString should be ("")
			}
			it("StateStart を返す") {
				res should be (TokenParser.StateStart)
			}
		}

		describe("記号以外の場合") {
			val context = ScriptConverterContext(TokenParseConfig(null, '%'))
			val res = TokenParser.StateSyntaxModeAfterSign.doEvent('%', context)

			it("バッファクリア") {
				context.buffer.toString should be ("")
			}
			it("StateSyntaxMode を返す") {
				res should be (TokenParser.StateSyntaxMode)
			}
		}
	}

	describe("StateIncludeMode") {
		describe("記号の場合") {
			val context = ScriptConverterContext(TokenParseConfig(null, '%'))
			val res = TokenParser.StateIncludeMode.doEvent('%', context)

			it("バッファに詰める") {
				context.buffer.toString should be ("%")
			}
			it("StateIncludeModeAfterSign を返す") {
				res should be (TokenParser.StateIncludeModeAfterSign)
			}
		}

		describe("記号以外の場合") {
			val context = ScriptConverterContext(TokenParseConfig(null, '%'))
			val res = TokenParser.StateIncludeMode.doEvent('/', context)

			it("バッファクリア") {
				context.buffer.toString should be ("")
			}
			it("StateIncludeMode を返す") {
				res should be (TokenParser.StateIncludeMode)
			}
		}
	}

	describe("StateIncludeModeAfterSign") {
		describe("> の場合") {
			val context = ScriptConverterContext(TokenParseConfig(null, '%'))
			val res = TokenParser.StateIncludeModeAfterSign.doEvent('>', context)

			it("バッファクリア") {
				context.buffer.toString should be ("")
			}
			it("StateStart を返す") {
				res should be (TokenParser.StateStart)
			}
		}

		describe("記号以外の場合") {
			val context = ScriptConverterContext(TokenParseConfig(null, '%'))
			val res = TokenParser.StateIncludeModeAfterSign.doEvent('%', context)

			it("バッファクリア") {
				context.buffer.toString should be ("")
			}
			it("StateIncludeMode を返す") {
				res should be (TokenParser.StateIncludeMode)
			}
		}
	}


	describe("exec") {
		describe("単純な文字列のみ") {
			val context = ScriptConverterContext(TokenParseConfig(null, '%'))
			TokenParser.exec(Source.fromString("""<html>
	<body>
		"AAA"
	</body>
</html>"""), context)

			it("textOnly は true") {
				context.textOnly should be (true)
			}
			it("tokens は 1") {
				context.tokens should have size(1)
			}
		}

		describe("出力モード") {
			val context = ScriptConverterContext(TokenParseConfig(null, '%'))
			TokenParser.exec(Source.fromString("""<html>
	<body>
		%{abc}
	</body>
</html>"""), context)

			it("textOnly は false") {
				context.textOnly should be (false)
			}
			it("tokens は 3") {
				context.tokens should have size(3)
			}
		}

		describe("構文モード") {
			val context = ScriptConverterContext(TokenParseConfig(null, '%'))
			TokenParser.exec(Source.fromString("""<html>
	<body>
		<% if (abc > 0) { %>
			abc
		<% } %>
	</body>
</html>"""), context)

			it("textOnly は false") {
				context.textOnly should be (false)
			}
			it("tokens は 5") {
				context.tokens should have size(5)
			}
		}

		describe("インクルードモード") {
			val context = ScriptConverterContext(TokenParseConfig(null, '%'))
			TokenParser.exec(Source.fromString("""<html>
	<body>
		<%/common/footer.html %>
	</body>
</html>"""), context)

			it("textOnly は false") {
				context.textOnly should be (true)
			}
			it("tokens は 3") {
				context.tokens should have size(3)
			}
		}
	}
}
