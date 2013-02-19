package com.v_standard.vsp.compiler

import com.v_standard.vsp.script.ScriptDefine
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers


/**
 * Token テストスペッククラス。
 */
class TokenSpec extends FunSpec with ShouldMatchers {
	describe("StringToken.toScript") {
		it("追加した文字、文字列が文字列として print の引き数に与えられた文字列として返される") {
			val token = new StringToken
			token += 'a'
			token += "b\nc"
			token += "d\te"
			token += "f\rg"
			token += "\""
			token += 'h'

			token.toScript should be ("print(\"ab\\ncd\\tef\\rg\\\"h\");\n")
		}
	}

	describe("PrintToken.toScript") {
		it("追加した文字、文字列が評価され print の引き数に与えられた文字列として返される") {
			val token = new PrintToken
			token += 'a'
			token += "b\nc"
			token += "d\te"
			token += "f\rg"
			token += "\""
			token += 'h'

			token.toScript should be ("print(" + ScriptDefine.SCRIPT_OBJ_NAME +
        ".escape(ab\ncd\tef\rg\"h));\n")
		}
	}

	describe("SyntaxToken.toScript") {
		it("追加した文字、文字列がそのまま返される") {
			val token = new SyntaxToken
			token += 'a'
			token += "b\nc"
			token += "d\te"
			token += "f\rg"
			token += "\""
			token += 'h'

			token.toScript should be ("ab\ncd\tef\rg\"h\n")
		}
	}

	describe("IncludeToken.toScript") {
		it("") {
			(pending)
		}
	}
}
