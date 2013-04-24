package com.v_standard.vsp.compiler

import com.v_standard.vsp.utils.State
import java.io.File
import scala.collection.mutable.ArrayBuffer
import scala.io.Source


/**
 * トークン解析オブジェクト。
 */
object TokenParser {
	final val CR = '\r'
	final val LF = '\n'
	final val CHEVRON_OPEN = '<'
	final val CHEVRON_CLOSE = '>'
	final val CURLY_OPEN = '{'
	final val CURLY_CLOSE = '}'
	final val SLASH = '/'


	/**
	 * 開始状態。
	 */
	object StateStart extends State[Char, ScriptConverterContext] {
		override def doEvent(event: Char, context: ScriptConverterContext) = {
			event match {
				case context.config.sign =>
					context.buffer.append(event)
					StateAfterSign
				case CHEVRON_OPEN =>
					context.buffer.append(event)
					StateAfterChevronOpen
				case _ =>
					context.addStringToken(event)
					StateStart
			}
		}
	}

	/**
	 * 記号後状態。
	 */
	object StateAfterSign extends State[Char, ScriptConverterContext] {
		override def doEvent(event: Char, context: ScriptConverterContext) = {
			event match {
				case CURLY_OPEN =>
					context.buffer.clear
					StatePrintMode
				case _ =>
					context.addStringToken(event)
					StateStart
			}
		}
	}

	/**
	 * 出力モード状態。
	 */
	object StatePrintMode extends State[Char, ScriptConverterContext] {
		override def doEvent(event: Char, context: ScriptConverterContext) = {
			event match {
				case CURLY_CLOSE =>
					context.endToken()
					StateStart
				case _ =>
					context.addPrintToken(event)
					StatePrintMode
			}
		}
	}

	/**
	 * < 後状態。
	 */
	object StateAfterChevronOpen extends State[Char, ScriptConverterContext] {
		override def doEvent(event: Char, context: ScriptConverterContext) = {
			event match {
				case context.config.sign =>
					context.buffer.clear
					StateStartSyntaxMode
				case _ =>
					context.addStringToken(event)
					StateStart
			}
		}
	}

	/**
	 * 構文モード開始状態。
	 */
	object StateStartSyntaxMode extends State[Char, ScriptConverterContext] {
		override def doEvent(event: Char, context: ScriptConverterContext) = {
			event match {
				case SLASH =>
					context.buffer.clear
					StateIncludeMode
				case context.config.sign =>
					context.buffer.append(event)
					StateSyntaxModeAfterSign
				case _ =>
					context.addSyntaxToken(event)
					StateSyntaxMode
			}
		}
	}

	/**
	 * 構文モード状態。
	 */
	object StateSyntaxMode extends State[Char, ScriptConverterContext] {
		override def doEvent(event: Char, context: ScriptConverterContext) = {
			event match {
				case context.config.sign =>
					context.buffer.append(event)
					StateSyntaxModeAfterSign
				case _ =>
					context.addSyntaxToken(event)
					StateSyntaxMode
			}
		}
	}

	/**
	 * 構文モードで記号後状態。
	 */
	object StateSyntaxModeAfterSign extends State[Char, ScriptConverterContext] {
		override def doEvent(event: Char, context: ScriptConverterContext) = {
			event match {
				case CHEVRON_CLOSE =>
					context.endToken()
					StateStart
				case _ =>
					context.addSyntaxToken(event)
					StateSyntaxMode
			}
		}
	}

	/**
	 * インクルードモード状態。
	 */
	object StateIncludeMode extends State[Char, ScriptConverterContext] {
		override def doEvent(event: Char, context: ScriptConverterContext) = {
			event match {
				case context.config.sign =>
					context.buffer.append(event)
					StateIncludeModeAfterSign
				case _ =>
					context.addIncludeToken(event)
					StateIncludeMode
			}
		}
	}

	/**
	 * インクルードモードで記号後状態。
	 */
	object StateIncludeModeAfterSign extends State[Char, ScriptConverterContext] {
		override def doEvent(event: Char, context: ScriptConverterContext) = {
			event match {
				case CHEVRON_CLOSE =>
					context.endToken()
					StateStart
				case _ =>
					context.addIncludeToken(event)
					StateIncludeMode
			}
		}
	}


	/**
	 * 実行。
	 *
	 * @param source テンプレートソース
	 * @param context スクリプト変換コンテキスト
	 */
	def exec(source: Source, context: ScriptConverterContext) = {
		var state: State[Char, ScriptConverterContext] = StateStart
		source.foreach(ch => state = state.doEvent(ch, context))

		if (context.buffer.size > 0) {
			if (context.currentToken.isEmpty) context.buffer.foreach(context.addStringToken(_))
			else context.buffer.foreach(context.currentToken.get += _)
		}
		context.buffer.clear

		if (!context.currentToken.isEmpty) context.tokens += context.currentToken.get
		context.currentToken = None
	}
}
