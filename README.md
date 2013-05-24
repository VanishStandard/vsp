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

## 環境

Scala 2.1x

## セットアップ

sbt
```scala
sbtresolvers += "VanishStandard Maven Repository" at "http://vanishstandard.github.com/mvn-repo"

libraryDependencies += "com.v_standard.vsp" %% "vsp" % "x.x.x"
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

<% forseq(list, function(num, i) { %>
%{i}: %{num}
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

## ライセンス
3-clause BSD license
