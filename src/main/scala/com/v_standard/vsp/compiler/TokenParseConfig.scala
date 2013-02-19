package com.v_standard.vsp.compiler

import java.io.File


/**
 * トークン解析設定クラス。
 *
 * @param baseDir テンプレートベースディレクトリ
 * @param sign 記号
 */
case class TokenParseConfig(baseDir: File, sign: Char)
