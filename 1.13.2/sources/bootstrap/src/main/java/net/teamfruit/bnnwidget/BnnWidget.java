package net.teamfruit.bnnwidget;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(value = Reference.MODID)
public class BnnWidget {
	public static @Nullable BnnWidget instance;

	public BnnWidget() {
		instance = this;
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::preInit);
	}

	/*	@NetworkCheckHandler
		public boolean checkModList(final @Nonnull Map<String, String> versions, final @Nonnull Side side) {
			return true;
		}
	*/
	public void preInit(final @Nonnull FMLClientSetupEvent event) {
	}
}
