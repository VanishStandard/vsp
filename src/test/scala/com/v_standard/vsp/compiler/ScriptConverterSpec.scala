package com.v_standard.vsp.compiler

import com.v_standard.vsp.script.ScriptDefine
import java.io.File
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import scala.io.Source


/**
 * ScriptConverter テストスペッククラス。
 */
class ScriptConverterSpec extends FunSpec with ShouldMatchers {
	describe("convert") {
		describe("単純な文字列のみ") {
			it("print 文のみ") {
				val res = ScriptConverter.convert(Source.fromString("""<html>
	<body>
		"AAA"
	</body>
</html>"""), TokenParseConfig(null, '%'))

				res._1 should be (
"""print("<html>\n\t<body>\n\t\t\"AAA\"\n\t</body>\n</html>");
""")

				res._2 should be (true)
			}
		}

		describe("出力モード") {
			it("escape 経由の print 文") {
				val res = ScriptConverter.convert(Source.fromString("""%{var1}<html>
	<body>
		%{abc}%{efg}
	</body>
</html>%{var2}"""), TokenParseConfig(null, '%'))

				res._1 should be (
"""print(""" + ScriptDefine.SCRIPT_OBJ_NAME + """.escape(var1));
print("<html>\n\t<body>\n\t\t");
print(""" + ScriptDefine.SCRIPT_OBJ_NAME + """.escape(abc));
print(""" + ScriptDefine.SCRIPT_OBJ_NAME + """.escape(efg));
print("\n\t</body>\n</html>");
print(""" + ScriptDefine.SCRIPT_OBJ_NAME + """.escape(var2));
""" + ScriptConverter.FUNC_FORSEQ)

				res._2 should be (false)
			}
		}

		describe("構文モード") {
			it("構文部分は展開") {
				val res = ScriptConverter.convert(Source.fromString("""<%if (abc > 0) {%><html>
	<body>
		%{abc}
	</body>
</html><%}%>"""), TokenParseConfig(null, '%'))

				res._1 should be (
"""if (abc > 0) {
print("<html>\n\t<body>\n\t\t");
print(""" + ScriptDefine.SCRIPT_OBJ_NAME + """.escape(abc));
print("\n\t</body>\n</html>");
}
""" + ScriptConverter.FUNC_FORSEQ)

				res._2 should be (false)
			}
		}

		describe("インクルードモード") {
			describe("インクルードファイルもテンプレート") {
				it("インクルードファイル内も解析") {
					val res = ScriptConverter.convert(Source.fromString("""<html>
	<%/common.html%>
	<body>
		%{abc}
	</body>
</html>"""), TokenParseConfig(new File("./src/test/resources/templates"), '%'))

					res._1 should be (
						"""print("<html>\n\t");
print("<script type=\"text/javascript\" src=\"...\"></script>\n");
if (n == 1) {
print("<script type=\"text/javascript\" src=\"n.js\"></script>");
}
print("\n<title>");
print(""" + ScriptDefine.SCRIPT_OBJ_NAME + """.escape(title));
print("</title>\n");
print("\n\t<body>\n\t\t");
print(""" + ScriptDefine.SCRIPT_OBJ_NAME + """.escape(abc));
print("\n\t</body>\n</html>");
""" + ScriptConverter.FUNC_FORSEQ)

					res._2 should be (false)
				}
			}

			describe("インクルードファイルはテキスト") {
				it("textOnly は true") {
					val res = ScriptConverter.convert(Source.fromString("""<html>
	<%/text.html%>
	<body>
	</body>
</html>"""), TokenParseConfig(new File("./src/test/resources/templates"), '%'))

					res._1 should be (
						"""print("<html>\n\t");
print("<title>Text Only</title>\n");
print("\n\t<body>\n\t</body>\n</html>");
""")

					res._2 should be (true)
				}
			}

			describe("ループ時") {
				it("例外") {
					evaluating { ScriptConverter.convert(Source.fromString("""<html>
	<%/loop.html%>
	<body>
		%{abc}
	</body>
</html>"""), TokenParseConfig(new File("./src/test/resources/templates"), '%')) } should
					produce [IllegalStateException]
				}
			}
		}
	}
}
