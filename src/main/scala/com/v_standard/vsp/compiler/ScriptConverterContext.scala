package com.v_standard.vsp.compiler

import java.io.File
import scala.collection.mutable.ArrayBuffer


/**
 * スクリプト変換コンテキストクラス。
 *
 * @param config トークン解析設定
 * @param deep 深さ
 */
case class ScriptConverterContext(config: TokenParseConfig, deep: Int = 0) {
	val tokens = new ArrayBuffer[Token]
	val buffer = new StringBuilder

	var currentToken: Option[Token] = None
	var textOnly: Boolean = true


	/**
	 * 文字列トークンへ追加。
	 */
	def addStringToken(ch: Char) {
		currentToken match {
			case None =>
				currentToken = Option(new StringToken)
				currentToken.get += buffer.toString
				currentToken.get += ch
			case Some(t) => t match {
				case st: StringToken =>
					st += buffer.toString
					st += ch
				case other =>
					tokens += currentToken.get
					currentToken = Option(new StringToken)
					currentToken.get += buffer.toString
					currentToken.get += ch
			}
		}
		buffer.clear
	}

	/**
	 * 出力トークンへ追加。
	 */
	def addPrintToken(ch: Char) {
		currentToken match {
			case None =>
				currentToken = Option(new PrintToken)
				currentToken.get += buffer.toString
				currentToken.get += ch
			case Some(t) => t match {
				case st: PrintToken =>
					st += buffer.toString
					st += ch
				case other =>
					tokens += currentToken.get
					currentToken = Option(new PrintToken)
					currentToken.get += buffer.toString
					currentToken.get += ch
			}
		}
		buffer.clear
		textOnly = false
	}

	/**
	 * 構文トークンへ追加。
	 */
	def addSyntaxToken(ch: Char) {
		currentToken match {
			case None =>
				currentToken = Option(new SyntaxToken)
				currentToken.get += buffer.toString
				currentToken.get += ch
			case Some(t) => t match {
				case st: SyntaxToken =>
					st += buffer.toString
					st += ch
				case other =>
					tokens += currentToken.get
					currentToken = Option(new SyntaxToken)
					currentToken.get += buffer.toString
					currentToken.get += ch
			}
		}
		buffer.clear
		textOnly = false
	}

	/**
	 * インクルードトークンへ追加。
	 */
	def addIncludeToken(ch: Char) {
		currentToken match {
			case None =>
				currentToken = Option(new IncludeToken(this))
				currentToken.get += buffer.toString
				currentToken.get += ch
			case Some(t) => t match {
				case st: IncludeToken =>
					st += buffer.toString
					st += ch
				case other =>
					tokens += currentToken.get
					currentToken = Option(new IncludeToken(this))
					currentToken.get += buffer.toString
					currentToken.get += ch
			}
		}
		buffer.clear
	}
}
