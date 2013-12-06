# VSP

Scala で書かれたテンプレートエンジン。

## 主な特徴

* Java の ScriptEngine を使用して実装
* テンプレートに JavaScript を使用
* テンプレートをコンパイルした結果をキャッシュ
* キャッシュの更新確認間隔を設定可

下記のような方にお勧め
* 頻繁にテンプレートの更新があり、都度デプロイするのが面倒な方
* テンプレート毎の記述の習得が面倒な方
* それなりのスピードは欲しい方
* Web で使用した場合に、JavaScript の記述がサーバ側なのかクライアント側なのかで混乱しない方
* テンプレート内で直接 DB に接続しない方

## 環境

Scala 2.10.X

## セットアップ

sbt
```scala
sbtresolvers += "VanishStandard Maven Repository" at "http://vanishstandard.github.com/mvn-repo"

libraryDependencies += "com.v_standard.vsp" %% "vsp" % "0.6.5"
```

## 使い方

### 設定ファイル

vsp.xml(指定しなかった場合のデフォルト設定ファイル名)

```xml
<template>
  <!-- テンプレートファイルを置くディレクトリ -->
  <!-- 未指定時 ./ -->
  <templateDir>templates</templateDir>
  <!-- テンプレート変数出力、構文に使う記号 -->
  <sign>%</sign>
  <!-- テンプレート更新チェック間隔 -->
  <!-- 0 なら都度チェック -->
  <checkPeriod>60</checkPeriod>
  <!-- 初期化時コンパイル対象ファイルの正規表現 -->
  <!-- 未指定の場合初期化時コンパイルなし -->
  <initCompileFilter>.*\.tmpl$</initCompileFilter>
</template>
```

### テンプレート呼び出し

```scala
import com.v_standard.vsp.{DefaultTemplateManager, Template}

...

// 初期化
// アプリケーション内で一度呼ぶ
// vsp.xml とは別の設定ファイルの場合指定する
DefaultTemplateManager.init()  // DefaultTemplateManager.init("vsp-custom.xml")

// テンプレート枠作成
// vsp.xml とは別の設定ファイルの場合指定する
val tmpl = DefaultTemplateManager.template()  // DefaultTemplateManager.template("vsp-custom.xml)

// 変数名と値追加
tmpl.addVar("id", 10)
tmpl.addVar("name", "VSP")
val dt = Calendar.getInstance
dt.set(2000, 0, 2, 3, 4, 5)
tmpl.addVar("dt", dt)
tmpl.addVar("list", Seq(2, 4, 6, 8))

// 設定ファイル templateDir からテンプレートファイルへの相対パスを指定してビルド
// 形成された文字れが返る
val result = tmpl.build("template1/template.tmpl")
```

### テンプレート

* 指定した記号(%) と {} で値出力
* 指定した記号(%) と <> で構文

template1/template.tmpl

```text
# %{name}

作成日: %{vsp.format("yyyy/MM/dd HH:mm:ss", dt)}

<% for (var i = 0; i < list.length(); ++i) { %>
%{i}: %{list.apply(i)}
<% } %>
```


### 結果

```text
# VSP

作成日: 2000/01/02 03:04:05


1: 2

2: 4

3: 6

4: 8

```

## 組み込み関数・オブジェクト
### JavaScript 関数
**forseq(list, action)**
> Scala の Seq ループ用ヘルパー関数
> * list: length(), appli() 関数を持つ Scala オブジェクト
> * action: 各要素, インデックスを受け処理を行うアクション

```text
<% forseq(list, function(num, i) { %>
%{i}: %{num}
<% }); %>
```
結果
```text
1: 2

2: 4

3: 6

4: 8
```

**br(str)**
> 改行を &lt;br /&gt; タグに変換
> * str: 変換対象文字列

```text
%{br("aaa\nbbb")}
```
結果
```text
aaa<br />bbb
```

### vsp オブジェクト
**raw(str)**
> HTML エスケープしない文字を出力
> * str: HTML 文字列

```text
%{"<br />"}
%{vsp.raw("<br />")}
```
結果
```text
&lt;br /&gt;
<br />
```

**format(pattern, obj)**
> 指定したパターンで値の型によってフォーマット
> * pattern: パターン
> * obj: フォーマット対象オブジェクト

```text
日付(java.util.Date, java.util.Calendar)
%{vsp.format("yyyy-MM-dd hh:mm", dt)}

整数(int, long)
%{vsp.format("#,###,##0", 1000)}

小数(float, double)
%{vsp.format("0.00", 1.1)}
```
結果
```text
日付(java.util.Date, java.util.Calendar)
2013-12-04 10:30

整数(int, long)
1,000

小数(float, double)
1.10
```

### html オブジェクト
**checkbox(currentValue, param)**
> &lt;input type="checkbox"&gt; を生成
> * currentValue: 現在の値 この値と同じ value のチェックボックスに checked が付く
> * param: パラメータ用 JSON オブジェクト キー名が _ で始まるもの以外は input タグの属性として展開される

```text
<% var currentVal = 1 %>
<% html.checkbox(currentVal, { value: "1", name: "chk1", id: "chk1", "class": "xxx" }); %>
```
結果
```text

<input type="checkbox" value="1" name="chk1" id="chk1" class="xxx" checked>
```

**radio(currentValue, param)**
> &lt;input type="radio"&gt; を生成
> * currentValue: 現在の値 この値と同じ value のチェックボックスに checked が付く
> * param: パラメータ用 JSON オブジェクト キー名が _ で始まるもの以外は input タグの属性として展開される

```text
<% var currentVal = 2 %>
<% html.radio(currentVal, { value: "1", name: "rd1", id: "rd1", "class": "xxx" }); %>
```
結果
```text

<input type="radio" value="1" name="rd1" id="rd1" class="xxx">
```

**select(currentValue, param)**
> &lt;select&gt; を生成
> * currentValue: 現在の値 この値と同じ value のチェックボックスに selected が付く。multiple には未対応。
> * param: パラメータ用 JSON オブジェクト キー名が _ で始まるもの以外は input タグの属性として展開される。<br />\_list: option 用のリスト。JSON の value をキー、ラベルを値としたリストかScala の (value, ラベル) タプルの Seq または配列。<br />\_default: デフォルトに選択される option。value をキー、ラベルとしたJSON。

```text
<% var currentVal = 2 %>
<% html.select(currentVal, { _list: [{1: 'a'}, {2: 'b'}, {3: 'c'}], _default: { "": "選択してください" }, name: "slt", id: "slt1", "class": "xxx" }); %>
```
結果
```text

<select name="slt" id="slt1" class="xxx>
  <option value="">選択してください</option>
  <option value="1">a</option>
  <option value="2" selected>b</option>
  <option value="3">c</option>
</select>
```

## ライセンス

3-clause BSD license
