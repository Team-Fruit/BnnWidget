package com.kamesuta.mc.bnnwidget.example;

import javax.annotation.Nonnull;

import com.kamesuta.mc.bnnwidget.font.FontSet;
import com.kamesuta.mc.bnnwidget.font.FontStyle;
import com.kamesuta.mc.bnnwidget.font.MinecraftFontRenderer;
import com.kamesuta.mc.bnnwidget.font.TrueTypeFont;
import com.kamesuta.mc.bnnwidget.render.WGui;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

/**
 * クライアント処理
 *
 * @author TeamFruit
 */
public class BnnExClient extends BnnExCommon {
	@Override
	public void preInit(final @Nonnull FMLPreInitializationEvent event) {
	}

	@Override
	public void init(final @Nonnull FMLInitializationEvent event) {
		final BnnExKeyHandler handler = new BnnExKeyHandler();
		FMLCommonHandler.instance().bus().register(handler);
		MinecraftForge.EVENT_BUS.register(handler);
	}

	@Override
	public void postInit(final @Nonnull FMLPostInitializationEvent event) {
		final FontSet fontSet = new FontSet.Builder().addName("HGP創英角ﾎﾟｯﾌﾟ体").build();
		final FontStyle style = new FontStyle.Builder().setFont(fontSet).build();
		WGui.mc.fontRenderer = new MinecraftFontRenderer(new TrueTypeFont(style), WGui.mc.fontRenderer.FONT_HEIGHT*2);
	}
}
