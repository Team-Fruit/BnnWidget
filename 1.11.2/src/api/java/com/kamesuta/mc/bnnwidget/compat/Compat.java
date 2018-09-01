package com.kamesuta.mc.bnnwidget.compat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class Compat {
	public static PositionedSoundRecord createClickSound() {
		return PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F);
	}

	public static class CompatGuiTextField extends GuiTextField {
		public CompatGuiTextField() {
			super(1, Minecraft_font(), 0, 0, 0, 0);
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

	public static abstract class CompatFontRenderer extends FontRenderer {
		public CompatFontRenderer(final GameSettings gameSettingsIn, final ResourceLocation location, final TextureManager textureManagerIn, final boolean unicode) {
			super(gameSettingsIn, location, textureManagerIn, unicode);
		}

		@Override
		public int drawStringWithShadow(@Nullable final String str, final float x, final float y, final int color) {
			return drawStringWithShadowCompat(str, (int) x, (int) y, color);
		}

		protected abstract int drawStringWithShadowCompat(@Nullable final String str, final float x, final float y, final int color);

		@Override
		public int drawString(@Nullable final String str, final float x, final float y, final int color, final boolean shadow) {
			return drawStringCompat(str, (int) x, (int) y, color, shadow);
		}

		protected abstract int drawStringCompat(@Nullable final String str, final float x, final float y, final int color, final boolean shadow);

		@Override
		public int getWordWrappedHeight(final String str, final int maxLength) {
			return getWordWrappedHeightCompat(str, maxLength);
		}

		public abstract int getWordWrappedHeightCompat(final String str, final int maxLength);
	}

	public static @Nonnull Minecraft getMinecraft() {
		return Minecraft.getMinecraft();
	}

	public static @Nonnull FontRenderer Minecraft_font() {
		return getMinecraft().fontRendererObj;
	}

	private static class WVertexImpl implements WVertex {
		/**
		 * Tessellatorインスタンス
		 * <p>
		 * 描画に使用します
		 */
		public static final @Nonnull Tessellator t = Tessellator.getInstance();
		/**
		 * WorldRendererインスタンス
		 * <p>
		 * 描画に使用します
		 */
		public static final @Nonnull VertexBuffer w = t.getBuffer();

		public WVertexImpl() {
		}

		/**
		 * 実際に描画します
		 */
		@Override
		public void draw() {
			endVertex();
			t.draw();
		}

		/**
		 * 描画を開始します。
		 * @param mode GL描画モード
		 * @return this
		 */
		@Override
		public @Nonnull WVertex begin(final int mode) {
			w.begin(mode, DefaultVertexFormats.POSITION);
			init();
			return this;
		}

		/**
		 * テクスチャ描画を開始します。
		 * @param mode GL描画モード
		 * @return this
		 */
		@Override
		public @Nonnull WVertex beginTexture(final int mode) {
			w.begin(mode, DefaultVertexFormats.POSITION_TEX);
			init();
			return this;
		}

		private void init() {
			this.stack = false;
		}

		private boolean stack;
		//		private double stack_x;
		//		private double stack_y;
		//		private double stack_z;

		/**
		 * 頂点の設定を開始します。
		 * <p>
		 * この後続けて色やテクスチャなどの設定を行うことができます。
		 * @param x X座標
		 * @param y Y座標
		 * @param z Z座標
		 * @return this
		 */
		@Override
		public @Nonnull WVertex pos(final double x, final double y, final double z) {
			endVertex();
			w.pos(x, y, z);
			//			this.stack_x = x;
			//			this.stack_y = y;
			//			this.stack_z = z;
			this.stack = true;
			return this;
		}

		/**
		 * テクスチャのマッピング
		 * @param u U座標
		 * @param v V座標
		 * @return this
		 */
		@Override
		public @Nonnull WVertex tex(final double u, final double v) {
			w.tex(u, v);
			return this;
		}

		/**
		 * 頂点の色
		 * @param red 赤(0～1)
		 * @param green 緑(0～1)
		 * @param blue 青(0～1)
		 * @param alpha アルファ(0～1)
		 * @return this
		 */
		@Override
		public @Nonnull WVertex color(final float red, final float green, final float blue, final float alpha) {
			return this.color((int) (red*255.0F), (int) (green*255.0F), (int) (blue*255.0F), (int) (alpha*255.0F));
		}

		/**
		 * 頂点の色
		 * @param red 赤(0～255)
		 * @param green 緑(0～255)
		 * @param blue 青(0～255)
		 * @param alpha アルファ(0～255)
		 * @return this
		 */
		@Override
		public @Nonnull WVertex color(final int red, final int green, final int blue, final int alpha) {
			w.putColorRGBA(0, red, green, blue, alpha);
			return this;
		}

		/**
		 * 法線
		 * @param nx X法線
		 * @param ny Y法線
		 * @param nz Z法線
		 * @return
		 */
		@Override
		public @Nonnull WVertex normal(final float nx, final float ny, final float nz) {
			w.normal(nx, ny, nz);
			return this;
		}

		/**
		 * オフセット
		 * @param x Xオフセット
		 * @param y Yオフセット
		 * @param z Zオフセット
		 */
		@Override
		public void setTranslation(final double x, final double y, final double z) {
			w.setTranslation(x, y, z);
		}

		/**
		 * 頂点の描画を終了します。
		 * <p>
		 * なお、{@link #pos(double, double, double) pos()}や{@link #draw() draw()}時に自動的に実行されるため、手動で呼ぶ必要はありません。
		 */
		private void endVertex() {
			if (this.stack) {
				this.stack = false;
				w.endVertex();
			}
		}
	}

	public static @Nonnull WVertex getWVertex() {
		return new WVertexImpl();
	}

	public static int getStringWidthWithoutFormattingCodes(final @Nonnull String s) {
		return Minecraft_font().getStringWidth(TextFormatting.getTextWithoutFormattingCodes(s));
	}
}
