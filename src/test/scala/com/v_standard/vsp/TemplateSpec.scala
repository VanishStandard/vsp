package com.v_standard.vsp

import java.util.Calendar
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers


/**
 * Template テストスペッククラス。
 */
class TemplateSpec extends FunSpec with ShouldMatchers {
	describe("build") {
/*
		describe("正常なフォーマットファイル") {
			it("展開された文字列を返す") {
				DefaultTemplateManager.init("vsp_success2.xml")
				val template = DefaultTemplateManager.template("vsp_success2.xml")
				template.addVar("n", 1)
				template.addVar("title", "タイトル")
				val cal = Calendar.getInstance
				cal.set(2000, 2, 9, 2, 3, 1)
				template.addVar("dt", cal)
				template.addVar("num", 1234)
				template.addVar("crlf", """abc
efg""");


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
2000-03-09 &lt;02:03:01&gt;
1,234
abc<br />efg
	</body>
</html>
""")
			}
		}

		describe("HTML フォームフォーマットファイル") {
			it("展開された文字列を返す") {
				DefaultTemplateManager.init("vsp_success2.xml")
				val template = DefaultTemplateManager.template("vsp_success2.xml")
				template.addVar("chk1", "3")
				template.addVar("chk2", "2")
				template.addVar("rd1", None)
				template.addVar("slt1", None)
				template.addVar("options", Array(("北海道" -> "1"), ("青森県" -> "2")))

				case class Obj(id: Int, name: String)
				template.build("form.html") should be ("""<html>
<body>
	<form action="/profile/register" method="POST">
		<input type="checkbox" name="chk1" value="1">
		<input type="checkbox" name="chk2" value="2" checked>

		<input type="radio" name="rd1" value="3" checked>
		<input type="radio" name="rd1" value="4">

		<select name="slt1"><option value="">選択してください</option><option value="1">北海道</option><option value="2">青森県</option></select>
	</form>
</body>
</html>
""")
			}
		}

		describe("JAR に含まれるファイル") {
			it("展開された文字列を返す") {
				DefaultTemplateManager.init("vsp_read_classpath.xml")
				val template = DefaultTemplateManager.template("vsp_read_classpath.xml")
				template.addVar("fileName", "simple.html")

				template.build("templates2/simple.html") should be ("""<html>
	<head>
		<title>Template in jar</title>

	</head>

	<body>
		Template in jar.<br />
		simple.html
	</body>
</html>
""")
			}
		}
	}
}
