package com.kamesuta.mc.bnnwidget.compat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class Compat {
	public static PositionedSoundRecord createClickSound() {
		return PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F);
	}

	public static class CompatGuiTextField extends GuiTextField {
		public CompatGuiTextField() {
			super(getFontRenderer().getFontRendererObj(), 0, 0, 0, 0);
		}

		public int getNthWordFromPosWS(final int p_146197_1_, final int p_146197_2_, final boolean p_146197_3_) {
			return func_146197_a(p_146197_1_, p_146197_2_, p_146197_3_);
		}

		public int getX() {
			return this.xPosition;
		}

		public void setX(final int value) {
			this.xPosition = value;
		}

		public int getY() {
			return this.yPosition;
		}

		public void setY(final int value) {
			this.yPosition = value;
		}
	}

	public static abstract class CompatFontRendererBase extends FontRenderer {
		public CompatFontRendererBase(final GameSettings gameSettingsIn, final ResourceLocation location, final TextureManager textureManagerIn, final boolean unicode) {
			super(gameSettingsIn, location, textureManagerIn, unicode);
		}

		@Override
		public int drawStringWithShadow(@Nullable final String str, final int x, final int y, final int color) {
			return drawStringWithShadowCompat(str, x, y, color);
		}

		protected abstract int drawStringWithShadowCompat(@Nullable final String str, final float x, final float y, final int color);

		@Override
		public int drawString(@Nullable final String str, final int x, final int y, final int color, final boolean shadow) {
			return drawStringCompat(str, x, y, color, shadow);
		}

		protected abstract int drawStringCompat(@Nullable final String str, final float x, final float y, final int color, final boolean shadow);

		@Override
		public int splitStringWidth(final String str, final int maxLength) {
			return getWordWrappedHeightCompat(str, maxLength);
		}

		public abstract int getWordWrappedHeightCompat(final String str, final int maxLength);
	}

	public static @Nonnull Minecraft getMinecraft() {
		return Minecraft.getMinecraft();
	}

	public static @Nonnull CompatFontRenderer getFontRenderer() {
		return new CompatFontRenderer(getMinecraft().fontRenderer);
	}

	public static class CompatFontRenderer {
		private final FontRenderer font;

		public CompatFontRenderer(final FontRenderer font) {
			this.font = font;
		}

		public int drawString(final String msg, final float x, final float y, final int color, final boolean shadow) {
			return this.font.drawString(msg, (int) x, (int) y, color, shadow);
		}

		public int drawString(final String msg, final float x, final float y, final int color) {
			return drawString(msg, x, y, color, false);
		}

		public int drawStringWithShadow(final String msg, final float x, final float y, final int color) {
			return drawString(msg, x, y, color, true);
		}

		public int getStringWidth(final @Nullable String s) {
			return this.font.getStringWidth(s);
		}

		public int getStringWidthWithoutFormattingCodes(final @Nullable String s) {
			return getStringWidth(EnumChatFormatting.getTextWithoutFormattingCodes(s));
		}

		public FontRenderer getFontRendererObj() {
			return this.font;
		}
	}

	private static class WVertexImpl implements WVertex {
		public static final @Nonnull Tessellator t = Tessellator.instance;

		public WVertexImpl() {
		}

		@Override
		public void draw() {
			endVertex();
			t.draw();
		}

		@Override
		public WVertex begin(final int mode) {
			t.startDrawing(mode);
			init();
			return this;
		}

		@Override
		public WVertex beginTexture(final int mode) {
			t.startDrawing(mode);
			init();
			return this;
		}

		private void init() {
			this.stack = false;
		}

		private boolean stack;
		private double stack_x;
		private double stack_y;
		private double stack_z;

		@Override
		public WVertex pos(final double x, final double y, final double z) {
			endVertex();
			this.stack_x = x;
			this.stack_y = y;
			this.stack_z = z;
			this.stack = true;
			return this;
		}

		@Override
		public WVertex tex(final double u, final double v) {
			t.setTextureUV(u, v);
			return this;
		}

		@Override
		public WVertex color(final float red, final float green, final float blue, final float alpha) {
			return this.color((int) (red*255.0F), (int) (green*255.0F), (int) (blue*255.0F), (int) (alpha*255.0F));
		}

		@Override
		public WVertex color(final int red, final int green, final int blue, final int alpha) {
			t.setColorRGBA(red, green, blue, alpha);
			return this;
		}

		@Override
		public WVertex normal(final float nx, final float ny, final float nz) {
			t.setNormal(nx, ny, nz);
			return this;
		}

		@Override
		public void setTranslation(final double x, final double y, final double z) {
			t.setTranslation(x, y, z);
		}

		private void endVertex() {
			if (this.stack) {
				this.stack = false;
				t.addVertex(this.stack_x, this.stack_y, this.stack_z);
			}
		}
	}

	public static @Nonnull WVertex getWVertex() {
		return new WVertexImpl();
	}
}
