package com.kamesuta.mc.bnnwidget.example;

import javax.annotation.Nonnull;

import org.lwjgl.input.Keyboard;

import com.kamesuta.mc.bnnwidget.example.gui.GuiMain;
import com.kamesuta.mc.bnnwidget.render.WGui;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.settings.KeyBinding;

public class BnnExKeyHandler {
	public static enum Keys {
		KEY_BINDING_GUI(new KeyBinding("bnnex.key.gui", Keyboard.KEY_APOSTROPHE, "bnnex.key.category")) {
			@Override
			public void onKeyInput(final @Nonnull InputEvent event, final @Nonnull KeyBinding binding) {
				if (WGui.mc.currentScreen==null&&binding.isPressed())
					WGui.mc.displayGuiScreen(new GuiMain());
			}
		},
		;

		public final @Nonnull KeyBinding binding;

		private Keys(final @Nonnull KeyBinding binding) {
			this.binding = binding;
		}

		public abstract void onKeyInput(@Nonnull InputEvent event, @Nonnull KeyBinding binding);
	}

	public static void init() {
		for (final Keys key : Keys.values())
			ClientRegistry.registerKeyBinding(key.binding);
	}

	@SubscribeEvent
	public void onKeyInput(final @Nonnull InputEvent event) {
		for (final Keys key : Keys.values())
			key.onKeyInput(event, key.binding);
	}
}
