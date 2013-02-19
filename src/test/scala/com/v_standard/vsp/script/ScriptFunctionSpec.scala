package com.v_standard.vsp.script

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers


/**
 * ScriptFunction テストスペッククラス。
 */
class ScriptFunctionSpec extends FunSpec with ShouldMatchers {
	describe("row") {
    val sf = new ScriptFunction
    describe("文字を与えた場合") {
		  it("Row を返す") {

        sf.raw("abcd") should be (Raw("abcd"))
		  }
	  }

	  describe("escape") {
      val sf = new ScriptFunction
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
}
