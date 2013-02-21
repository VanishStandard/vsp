package com.v_standard.vsp

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers


/**
 * Template テストスペッククラス。
 */
class TemplateSpec extends FunSpec with ShouldMatchers {
	describe("build") {
		describe("正常なフォーマットファイル") {
			it("展開された文字列を返す") {
				DefaultTemplateManager.init("vsp_success2.xml")
				val template = DefaultTemplateManager.template("vsp_success2.xml")
				template.addVar("n", 1)
				template.addVar("title", "タイトル")

				case class Obj(id: Int, name: String)
				template.build("list.html",
					"list" -> List(Obj(1, "あいうえお"), Obj(2, "かきくけこ"), Obj(3, "さしすせそ"))) should be ("""<html>
	<head>
		<script type="text/javascript" src="..."></script>
<script type="text/javascript" src="n.js"></script>
<title>タイトル</title>

	</head>

	<body>
<ul>
<li id="1">あいうえお</li>

<li id="2">かきくけこ</li>

<li id="3">さしすせそ</li>

		</ul>
	</body>
</html>
""")
			}
		}
	}
}
