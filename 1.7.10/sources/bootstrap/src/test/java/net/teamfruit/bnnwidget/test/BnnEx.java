package net.teamfruit.bnnwidget.test;

import java.util.Map;

import javax.annotation.Nonnull;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = "bnnex", name = "BnnExample", version = "1.0.0")
public class BnnEx {
	@NetworkCheckHandler
	public boolean checkModList(final @Nonnull Map<String, String> versions, final @Nonnull Side side) {
		return true;
	}

	@EventHandler
	public void preInit(final @Nonnull FMLPreInitializationEvent event) {
	}

	@EventHandler
	public void init(final @Nonnull FMLInitializationEvent event) {
		final BnnExKeyHandler handler = new BnnExKeyHandler();
		BnnExKeyHandler.init();
		FMLCommonHandler.instance().bus().register(handler);
		MinecraftForge.EVENT_BUS.register(handler);
	}

	@EventHandler
	public void postInit(final @Nonnull FMLPostInitializationEvent event) {
	}
}
