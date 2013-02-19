package com.v_standard.vsp.utils


/**
 * 状態トレイト。
 */
trait State[Event, Context] {
	/**
	 * イベント発生。
	 *
	 * @param event イベント
	 * @param context コンテキスト
	 * @return 次の状態
	 */
	def doEvent(event: Event, context: Context): State[Event, Context]
}
