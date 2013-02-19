package com.v_standard.vsp.utils

import java.net.URL
import java.net.URLClassLoader
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers


/**
 * ClassUtil テストスペッククラス。
 */
class ClassUtilSpec extends FunSpec with ShouldMatchers {
	class CLThread extends Thread {
		var cl: ClassLoader = _

		override def run() {
			cl = ClassUtil.classLoader
		}
	}


	describe("classLoader") {
		describe("スレッド無し") {
			it("自身のクラスのクラスローダを返す") {
				val cl = new URLClassLoader(new Array[URL](0))
				ClassUtil.classLoader should not be theSameInstanceAs (cl)
				ClassUtil.classLoader should be theSameInstanceAs (this.getClass.getClassLoader)
			}
		}

		describe("スレッド有り、クラスローダ未定義") {
			it("自身のクラスのクラスローダを返す") {
				val cl = new URLClassLoader(new Array[URL](0))
				val t = new CLThread()
				t.start()
				t.join()
				t.cl should not be theSameInstanceAs (cl)
				t.cl should be theSameInstanceAs (this.getClass.getClassLoader)
			}
		}

		describe("スレッド有り、クラスローダ設定") {
			it("自身のクラスのクラスローダを返す") {
				val cl = new URLClassLoader(new Array[URL](0))
				val t = new CLThread()
				t.setContextClassLoader(cl)
				t.start()
				t.join()
				t.cl should be theSameInstanceAs (cl)
				t.cl should not be theSameInstanceAs (this.getClass.getClassLoader)
			}
		}
	}
}
