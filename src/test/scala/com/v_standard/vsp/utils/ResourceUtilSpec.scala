package com.v_standard.vsp.utils

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import com.v_standard.vsp.utils.ResourceUtil.using


/**
 * ResourceUtil テストスペッククラス。
 */
class ResourceUtilSpec extends FunSpec with ShouldMatchers {
	class Closeable {
		var closed = false

		def close(): Unit = { closed = true }
		def isClosed = closed
	}


	describe("using") {
		describe("close メソッド有り") {
			it("クローズされる") {
				val closeable = new Closeable
				closeable.isClosed should be (false)

				using(closeable) { r => }
				closeable.isClosed should be (true)
			}
		}
	}
}
