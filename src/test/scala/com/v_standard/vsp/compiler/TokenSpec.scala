package com.v_standard.vsp.compiler

import com.v_standard.vsp.script.ScriptDefine
import java.io.{File, FileNotFoundException}
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
        ".escape((ab\ncd\tef\rg\"h) == null ? \"\" : (ab\ncd\tef\rg\"h)));\n")
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
    describe("ScriptDefine.MAX_INCLUDE 以上の深さを指定") {
      it("IllegalStateException") {
        val context = ScriptConverterContext(null, ScriptDefine.MAX_INCLUDE)
        val token = new IncludeToken(context)

        evaluating {
          token.toScript
        } should produce [IllegalStateException]
      }
    }

    describe("存在しないファイルを指定") {
      it("FileNotFoundException") {
        val context = ScriptConverterContext(TokenParseConfig(new File("./"), '%'), 0)
        val token = new IncludeToken(context)
        token += "notfound.txt"

        evaluating {
          token.toScript
        } should produce [FileNotFoundException]
      }
    }

    describe("ネストしてインクルードしたファイルを指定") {
      it("FileNotFoundException") {
        val context = ScriptConverterContext(TokenParseConfig(new File("./templates"), '%'), 0)
        val token = new IncludeToken(context)
        token += "nest_include.html"

        val res = token.toScript
        context.includeFiles.size should be (3)
        context.includeFiles(new File("./templates/nest_include.html").getCanonicalFile) should be (true)
        context.includeFiles(new File("./templates/include_twice.html").getCanonicalFile) should be (true)
        context.includeFiles(new File("./templates/common.html").getCanonicalFile) should be (true)
      }
    }
  }
}
