package com.kamesuta.mc.bnnwidget;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class BnnWidget {
	@Instance(Reference.MODID)
	public static @Nullable BnnWidget instance;

	@NetworkCheckHandler
	public boolean checkModList(final @Nonnull Map<String, String> versions, final @Nonnull Side side) {
		return true;
	}

	@EventHandler
	public void preInit(final @Nonnull FMLPreInitializationEvent event) {
	}

	@EventHandler
	public void init(final @Nonnull FMLInitializationEvent event) {
	}

	@EventHandler
	public void postInit(final @Nonnull FMLPostInitializationEvent event) {
	}
}
