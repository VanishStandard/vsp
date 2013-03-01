package com.v_standard.vsp.utils

import com.v_standard.vsp.utils.ResourceUtil.using
import java.io.{File, FileNotFoundException}
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import scala.io.Source


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

	describe("getSource") {
		describe("存在するファイル") {
			it("Source を返す") {
				val actual = ResourceUtil.getSource(
					new File("./target/scala-2.10/test-classes/com/v_standard/vsp/utils/ResourceUtilSpec.class"))
				actual.isInstanceOf[Source] should be (true)
				actual.close
			}
		}

		describe("存在しないファイル") {
			it("例外") {
				evaluating {
					ResourceUtil.getSource(new File("./target/Nothing"))
				} should produce [FileNotFoundException]
			}
		}

		describe("JAR 内のファイル") {
			it("Source を返す") {
				val actual = ResourceUtil.getSource(
					new File(".\\templates2\\simple.html"))
				actual.isInstanceOf[Source] should be (true)
				actual.close
			}
		}
	}
}
