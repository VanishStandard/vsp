package com.v_standard.vsp.compiler

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import scala.io.Source


/**
 * ScriptCompiler テストスペッククラス。
 */
class ScriptCompilerSpec extends FunSpec with ShouldMatchers {
	describe("compile") {
		describe("動的の場合") {
			it("コンパイル済みスクリプトが生成される") {
				val res = ScriptCompiler.compile(Source.fromString("""<html>
	<body>
		%{abc}
	</body>
</html>"""), TokenParseConfig(null, '%'))

				res.cscript should not be (None)
				res.text should be (None)
			}
		}

		describe("静的の場合") {
			it("テキストが生成される") {
				val res = ScriptCompiler.compile(Source.fromString("""<html>
	<body>
	</body>
</html>"""), TokenParseConfig(null, '%'))

				res.cscript should be (None)
				res.text.get should be ("""<html>
	<body>
	</body>
</html>""")
			}
		}

		describe("コンパイルエラーの場合") {
			it("テキストにエラーが出力される") {
				val res = ScriptCompiler.compile(Source.fromString("""<html>
	<body>
<% **abc %>
	</body>
</html>"""), TokenParseConfig(null, '%'))

				res.cscript should be (None)
				res.text should not be (None)
			}
		}
	}
}
