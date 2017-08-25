package com.kamesuta.mc.bnnwidget.example;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;

/**
 * Modベース
 *
 * @author TeamFruit
 */
@Mod(modid = "bnnex", name = "BnnExample", version = "1.0.0")
public class BnnExMod {
	@SidedProxy(serverSide = "com.kamesuta.mc.bnnwidget.example.BnnExCommon", clientSide = "com.kamesuta.mc.bnnwidget.example.BnnExClient")
	public static @Nullable BnnExCommon proxy;

	@NetworkCheckHandler
	public boolean checkModList(final @Nonnull Map<String, String> versions, final @Nonnull Side side) {
		return true;
	}

	@EventHandler
	public void preInit(final @Nonnull FMLPreInitializationEvent event) {
		if (proxy!=null)
			proxy.preInit(event);
	}

	@EventHandler
	public void init(final @Nonnull FMLInitializationEvent event) {
		if (proxy!=null)
			proxy.init(event);
	}

	@EventHandler
	public void postInit(final @Nonnull FMLPostInitializationEvent event) {
		if (proxy!=null)
			proxy.postInit(event);
	}
}
