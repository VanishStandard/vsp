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
			}
		}

		describe("存在しないファイル") {
			it("例外") {
				evaluating {
					val config = TemplateConfig("vsp_not_found.xml")
				} should produce [FileNotFoundException]
			}
		}
	}
}
