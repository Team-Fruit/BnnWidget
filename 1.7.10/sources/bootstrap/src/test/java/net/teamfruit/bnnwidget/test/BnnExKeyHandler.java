package net.teamfruit.bnnwidget.test;

import javax.annotation.Nonnull;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.settings.KeyBinding;
import net.teamfruit.bnnwidget.WFrame;

public class BnnExKeyHandler {
	public static enum Keys {
		KEY_BINDING_GUI(new KeyBinding("bnnex.key.gui", Keyboard.KEY_APOSTROPHE, "bnnex.key.category")) {
			@Override
			public void onKeyInput(final @Nonnull InputEvent event, final @Nonnull KeyBinding binding) {
				if (binding.isPressed())
					WFrame.displayFrame(new BnnExGui());
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
