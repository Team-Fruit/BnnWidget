package com.kamesuta.mc.bnnwidget;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import cpw.mods.fml.common.eventhandler.EventBus;

/**
 * いくつかのデータをGUI全体で共有するのに役立ちます。
 * <p>
 * イベントバスを持ち、独自のイベントを発生させることができます。
 * <p>
 * これはGUIで管理される単一のインスタンスです。イベントはGUI単位で管理されます。
 * @see EventBus
 * @author TeamFruit
 */
public class WEvent {
	/**
	 * このイベントインスタンスを管理しているGUIです。
	 */
	public final @Nonnull WFrame owner;
	/**
	 * データを保持するのに役立ちます。
	 */
	public final @Nonnull Map<String, Object> data;
	/**
	 * GUIで管理されるイベントバスです。
	 */
	public final @Nonnull EventBus bus;

	public WEvent(final @Nonnull WFrame owner) {
		this.owner = owner;
		this.data = new HashMap<String, Object>();
		this.bus = new EventBus();
	}
}
