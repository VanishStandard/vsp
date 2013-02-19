package com.v_standard.vsp.compiler

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers


/**
 * ScriptConverterContext テストスペッククラス。
 */
class ScriptConverterContextSpec extends FunSpec with ShouldMatchers {
	describe("addStringToken") {
		describe("currentToken が None の場合") {
			val context = ScriptConverterContext(null, '%')
			context.addStringToken('A')

			it("バッファクリア") {
				context.buffer.toString should be ("")
			}
			it("tokens は空") {
				context.tokens should be ('empty)
			}
			it("currentToken は StringToken") {
				context.currentToken.get.isInstanceOf[StringToken] should be (true)
			}
		}

		describe("currentToken が Some(StringToken) の場合") {
			val context = ScriptConverterContext(null, '%')
			context.currentToken = Option(new StringToken)
			context.addStringToken('A')

			it("バッファクリア") {
				context.buffer.toString should be ("")
			}
			it("tokens は空") {
				context.tokens should be ('empty)
			}
			it("currentToken は StringToken") {
				context.currentToken.get.isInstanceOf[StringToken] should be (true)
			}
		}

		describe("currentToken が Some(SyntaxToken) の場合") {
			val context = ScriptConverterContext(null, '%')
			context.currentToken = Option(new SyntaxToken)
			context.addStringToken('A')

			it("バッファクリア") {
				context.buffer.toString should be ("")
			}
			it("tokens に SyntaxToken が追加") {
				context.tokens should have size(1)
				context.tokens(0).isInstanceOf[SyntaxToken] should be (true)
			}
			it("currentToken は StringToken") {
				context.currentToken.get.isInstanceOf[StringToken] should be (true)
			}
		}
	}

	describe("addPrintToken") {
		describe("currentToken が None の場合") {
			val context = ScriptConverterContext(null, '%')
			context.addPrintToken('A')

			it("バッファクリア") {
				context.buffer.toString should be ("")
			}
			it("tokens は空") {
				context.tokens should be ('empty)
			}
			it("currentToken は PrintToken") {
				context.currentToken.get.isInstanceOf[PrintToken] should be (true)
			}
		}

		describe("currentToken が Some(PrintToken) の場合") {
			val context = ScriptConverterContext(null, '%')
			context.currentToken = Option(new PrintToken)
			context.addPrintToken('A')

			it("バッファクリア") {
				context.buffer.toString should be ("")
			}
			it("tokens は空") {
				context.tokens should be ('empty)
			}
			it("currentToken は PrintToken") {
				context.currentToken.get.isInstanceOf[PrintToken] should be (true)
			}
		}

		describe("currentToken が Some(SyntaxToken) の場合") {
			val context = ScriptConverterContext(null, '%')
			context.currentToken = Option(new SyntaxToken)
			context.addPrintToken('A')

			it("バッファクリア") {
				context.buffer.toString should be ("")
			}
			it("tokens に SyntaxToken が追加") {
				context.tokens should have size(1)
				context.tokens(0).isInstanceOf[SyntaxToken] should be (true)
			}
			it("currentToken は PrintToken") {
				context.currentToken.get.isInstanceOf[PrintToken] should be (true)
			}
		}
	}

	describe("addSyntaxToken") {
		describe("currentToken が None の場合") {
			val context = ScriptConverterContext(null, '%')
			context.addSyntaxToken('A')

			it("バッファクリア") {
				context.buffer.toString should be ("")
			}
			it("tokens は空") {
				context.tokens should be ('empty)
			}
			it("currentToken は SyntaxToken") {
				context.currentToken.get.isInstanceOf[SyntaxToken] should be (true)
			}
		}

		describe("currentToken が Some(SyntaxToken) の場合") {
			val context = ScriptConverterContext(null, '%')
			context.currentToken = Option(new SyntaxToken)
			context.addSyntaxToken('A')

			it("バッファクリア") {
				context.buffer.toString should be ("")
			}
			it("tokens は空") {
				context.tokens should be ('empty)
			}
			it("currentToken は SyntaxToken") {
				context.currentToken.get.isInstanceOf[SyntaxToken] should be (true)
			}
		}

		describe("currentToken が Some(StringToken) の場合") {
			val context = ScriptConverterContext(null, '%')
			context.currentToken = Option(new StringToken)
			context.addSyntaxToken('A')

			it("バッファクリア") {
				context.buffer.toString should be ("")
			}
			it("tokens に SyntaxToken が追加") {
				context.tokens should have size(1)
				context.tokens(0).isInstanceOf[StringToken] should be (true)
			}
			it("currentToken は SyntaxToken") {
				context.currentToken.get.isInstanceOf[SyntaxToken] should be (true)
			}
		}
	}
}
