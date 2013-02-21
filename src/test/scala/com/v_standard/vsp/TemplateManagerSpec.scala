package com.v_standard.vsp

import java.io.FileNotFoundException
import javax.script.CompiledScript
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers


/**
 * TemplateManager テストスペッククラス。
 */
class TemplateManagerSpec extends FunSpec with ShouldMatchers {
	describe("template") {
		describe("デフォルトファイルの読み込み") {
			it("Template オブジェクトを返す") {
				DefaultTemplateManager.init()
				DefaultTemplateManager.template() should not be (null)
			}
		}

		describe("正常なフォーマットファイル") {
			it("Template オブジェクトを返す") {
				DefaultTemplateManager.init("vsp_success.xml")
				DefaultTemplateManager.template("vsp_success.xml") should not be (null)
			}
		}

		describe("2度 同じ設定ファイルで呼び出し") {
			it("TemplateManager は同じオブジェクトを返す") {
				DefaultTemplateManager.init("vsp_success.xml")
				DefaultTemplateManager.template("vsp_success.xml").manager should be theSameInstanceAs(
					DefaultTemplateManager.template("vsp_success.xml").manager)
			}
		}

		describe("2度 同じ設定ファイルで呼び出し") {
			it("Template は異なるオブジェクトを返す") {
				DefaultTemplateManager.init("vsp_success.xml")
				DefaultTemplateManager.template("vsp_success.xml") should not be theSameInstanceAs(
					DefaultTemplateManager.template("vsp_success.xml"))
			}
		}

		describe("初期化されていない") {
			it("IllegalStateException") {
				evaluating {
					DefaultTemplateManager.template("vsp_not_init.xml")
				} should produce [IllegalStateException]
			}
		}
	}

	describe("getScriptData") {
		describe("正常なテンプレート") {
			it("ScriptData が返される") {
				DefaultTemplateManager.init("vsp_success2.xml")
				val template = DefaultTemplateManager.template("vsp_success2.xml")
				template.manager.getScriptData("common.html").cscript.get.isInstanceOf[CompiledScript]  should be (true)
			}
		}

		describe("存在しないファイル名") {
			it("FileNotFoundException") {
				DefaultTemplateManager.init("vsp_success2.xml")
				val template = DefaultTemplateManager.template("vsp_success2.xml")
				evaluating {
					template.manager.getScriptData("common2.html")
				} should produce [FileNotFoundException]
			}
		}

		describe("コンパイルエラーになるテンプレート") {
			it("Text にエラー") {
				DefaultTemplateManager.init("vsp_success2.xml")
				val template = DefaultTemplateManager.template("vsp_success2.xml")
				template.manager.getScriptData("compile_error.html").text should be ('defined)
			}
		}
	}
}
