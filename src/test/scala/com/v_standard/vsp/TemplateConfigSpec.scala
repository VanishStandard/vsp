package com.v_standard.vsp

import java.io.File
import java.io.FileNotFoundException
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers


/**
 * TemplateConfig テストスペッククラス。
 */
class TemplateConfigSpec extends FunSpec with ShouldMatchers {
	describe("apply") {
		describe("正常なフォーマットファイル") {
			it("正常に読み込む") {
				val config = TemplateConfig("vsp_success.xml")
				config.templateDir.getAbsolutePath should be (new File("./").getAbsolutePath)
				config.sign should be ('%')
				config.checkPeriod should be (60)
				config.initCompileFilter should be (".*\\.html$")
			}
		}

		describe("initCompileFilter を省略") {
			it("initCompileFilter は None") {
				val config = TemplateConfig("vsp_success_empty_init_compile_filter.xml")
				config.templateDir.getAbsolutePath should be (new File("./src/test/resources/templates").getAbsolutePath)
				config.sign should be ('%')
				config.checkPeriod should be (60)
				config.initCompileFilter should be ("")
			}
		}

		describe("存在しないファイル") {
			it("例外") {
				evaluating {
					val config = TemplateConfig("vsp_not_found.xml")
				} should produce [FileNotFoundException]
			}
		}

		describe("テンプレートディレクトリ省略") {
			it("空文字としてカレントディレクトリを返す") {
				val config = TemplateConfig("vsp_read_classpath.xml")
				config.templateDir.getAbsolutePath should be (new File("./").getAbsolutePath)
			}
		}
	}
}
