package com.kamesuta.mc.bnnwidget.render;

import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;

/**
 * 基本描画準備を担当します
 *
 * @author TeamFruit
 */
public class WRenderer extends Gui {
	/**
	 * Minecraftインスタンス
	 */
	public static final @Nonnull Minecraft mc = FMLClientHandler.instance().getClient();

	/**
	 * Tessellatorインスタンス
	 * <p>
	 * 描画に使用します
	 */
	public static final @Nonnull Tessellator t = Tessellator.instance;

	/**
	 * テクスチャマネージャ
	 * <p>
	 * テクスチャをバインドする際に使用します
	 * <br>
	 * {@link Minecraft#renderEngine}と等価です
	 * @return {@link Minecraft#renderEngine}
	 */
	public static @Nonnull TextureManager texture() {
		return mc.renderEngine;
	}

	/**
	 * フォントレンダラー
	 * <p>
	 * フォントを描画する際に使用します
	 * <br>
	 * {@link Minecraft#fontRenderer}と等価です
	 * @return {@link Minecraft#fontRenderer}
	 */
	public static @Nonnull FontRenderer font() {
		return mc.fontRenderer;
	}

	public static @Nonnull WVertex begin(final int mode) {
		return vertex.begin(mode);
	}

	public static @Nonnull WVertex beginQuads() {
		return vertex.begin(GL_QUADS);
	}

	public static @Nonnull WVertex beginTexture(final int mode) {
		return vertex.beginTexture(mode);
	}

	public static @Nonnull WVertex beginTextureQuads() {
		return vertex.beginTexture(GL_QUADS);
	}

	public static final @Nonnull WVertex vertex = new WVertex();

	public static class WVertex {
		private WVertex() {
		}

		public void draw() {
			endVertex();
			t.draw();
		}

		public @Nonnull WVertex begin(final int mode) {
			t.startDrawing(mode);
			init(mode);
			return this;
		}

		public @Nonnull WVertex beginTexture(final int mode) {
			t.startDrawing(mode);
			init(mode);
			return this;
		}

		public void init(final int mode) {
			this.stack = false;
		}

		private boolean stack;
		private double stack_x;
		private double stack_y;
		private double stack_z;

		public @Nonnull WVertex pos(final double d, final double e, final double z) {
			endVertex();
			this.stack_x = d;
			this.stack_y = e;
			this.stack_z = z;
			this.stack = true;
			return this;
		}

		public @Nonnull WVertex tex(final double u, final double v) {
			t.setTextureUV(u, v);
			return this;
		}

		public @Nonnull WVertex color(final float red, final float green, final float blue, final float alpha) {
			return this.color((int) (red*255.0F), (int) (green*255.0F), (int) (blue*255.0F), (int) (alpha*255.0F));
		}

		public @Nonnull WVertex color(final int red, final int green, final int blue, final int alpha) {
			t.setColorRGBA(red, green, blue, alpha);
			return this;
		}

		public @Nonnull WVertex normal(final float nx, final float ny, final float nz) {
			t.setNormal(nx, ny, nz);
			return this;
		}

		public void setTranslation(final double x, final double y, final double z) {
			t.setTranslation(x, y, z);
		}

		public void endVertex() {
			if (this.stack) {
				this.stack = false;
				t.addVertex(this.stack_x, this.stack_y, this.stack_z);
			}
		}
	}

	/**
	 * {@link net.minecraft.client.gui.FontRenderer FontRenderer}で使用可能なカラーコードへ変換します。
	 * @param color カラーコード
	 * @return {@link net.minecraft.client.gui.FontRenderer FontRenderer}で使用可能なカラーコード
	 */
	public int toFontColor(final int color) {
		final int alpha = Math.max(color>>24&255, 0x4)<<24;
		return color&0xffffff|alpha;
	}

	/**
	 * int型RGBAカラーをカラーコードに変換します
	 * <p>
	 * フォントカラーの範囲は0～255です
	 * @param r カラー(赤)
	 * @param g カラー(緑)
	 * @param b カラー(青)
	 * @param a カラー(不透明度)
	 * @return カラーコード
	 */
	public static int toColorCode(final int r, final int g, final int b, final int a) {
		return (a&0xff)<<24|(r&0xff)<<16|(g&0xff)<<8|(b&0xff)<<0;
	}

	/**
	 * float型RGBAカラーをカラーコードに変換します
	 * <p>
	 * フォントカラーの範囲は0～1です
	 * @param r カラー(赤)
	 * @param g カラー(緑)
	 * @param b カラー(青)
	 * @param a カラー(不透明度)
	 * @return カラーコード
	 */
	public static int toColorCode(final float r, final float g, final float b, final float a) {
		return toColorCode((int) (r*255+.5f), (int) (g*255+.5f), (int) (b*255+.5f), (int) (a*255+.5f));
	}

	public static FloatBuffer buf = GLAllocation.createDirectFloatBuffer(16);

	/**
	 * 非テクスチャ要素を描画する前に呼び出します
	 * @param src アルファブレンドsrc、null指定={@link org.lwjgl.opengl.GL11#GL_SRC_ALPHA GL_SRC_ALPHA}
	 * @param dest アルファブレンドdest、null指定={@link org.lwjgl.opengl.GL11#GL_ONE_MINUS_SRC_ALPHA GL_ONE_MINUS_SRC_ALPHA}
	 */
	public static void startShape(final @Nullable BlendType src, final @Nullable BlendType dest) {
		OpenGL.glBlendFunc(src!=null ? src.glEnum : GL_SRC_ALPHA, dest!=null ? dest.glEnum : GL_ONE_MINUS_SRC_ALPHA);
		OpenGL.glDisable(GL_LIGHTING);
		OpenGL.glEnable(GL_BLEND);
		OpenGL.glDisable(GL_TEXTURE_2D);
	}

	/**
	 * 非テクスチャ要素を描画する前に呼び出します
	 * <p>
	 * デフォルトアルファブレンド({@link org.lwjgl.opengl.GL11#GL_SRC_ALPHA GL_SRC_ALPHA}, {@link org.lwjgl.opengl.GL11#GL_ONE_MINUS_SRC_ALPHA GL_ONE_MINUS_SRC_ALPHA})が使用されます
	 */
	public static void startShape() {
		startShape(null, null);
	}

	/**
	 * テクスチャ要素を描画する前に呼び出します
	 * @param src アルファブレンドsrc、null指定={@link org.lwjgl.opengl.GL11#GL_SRC_ALPHA GL_SRC_ALPHA}
	 * @param dest アルファブレンドdest、null指定={@link org.lwjgl.opengl.GL11#GL_ONE_MINUS_SRC_ALPHA GL_ONE_MINUS_SRC_ALPHA}
	 */
	public static void startTexture(final @Nullable BlendType src, final @Nullable BlendType dest) {
		OpenGL.glBlendFunc(src!=null ? src.glEnum : GL_SRC_ALPHA, dest!=null ? dest.glEnum : GL_ONE_MINUS_SRC_ALPHA);
		OpenGL.glDisable(GL_LIGHTING);
		OpenGL.glEnable(GL_BLEND);
		OpenGL.glEnable(GL_TEXTURE_2D);
	}

	/**
	 * テクスチャ要素を描画する前に呼び出します
	 * <p>
	 * デフォルトアルファブレンド({@link org.lwjgl.opengl.GL11#GL_SRC_ALPHA GL_SRC_ALPHA}, {@link org.lwjgl.opengl.GL11#GL_ONE_MINUS_SRC_ALPHA GL_ONE_MINUS_SRC_ALPHA})が使用されます
	 */
	public static void startTexture() {
		startTexture(null, null);
	}

	/**
	 * アルファブレンドを表現します
	 *
	 * @author TeamFruit
	 */
	public static enum BlendType {
		// @formatter:off
		ZERO(0, GL_ZERO),
		ONE(1, GL_ONE),
		SRC_COLOR(2, GL_SRC_COLOR),
		ONE_MINUS_SRC_COLOR(3, GL_ONE_MINUS_SRC_COLOR),
		DST_COLOR(4, GL_DST_COLOR),
		ONE_MINUS_DST_COLOR(5, GL_ONE_MINUS_DST_COLOR),
		SRC_ALPHA(6, GL_SRC_ALPHA),
		ONE_MINUS_SRC_ALPHA(7, GL_ONE_MINUS_SRC_ALPHA),
		DST_ALPHA(8, GL_DST_ALPHA),
		ONE_MINUS_DST_ALPHA(9, GL_ONE_MINUS_DST_ALPHA),
		SRC_ALPHA_SATURATE(10, GL_SRC_ALPHA_SATURATE),
		CONSTANT_COLOR(11, GL_CONSTANT_COLOR),
		ONE_MINUS_CONSTANT_COLOR(12, GL_ONE_MINUS_CONSTANT_COLOR),
		CONSTANT_ALPHA(13, GL_CONSTANT_ALPHA),
		ONE_MINUS_CONSTANT_ALPHA(14, GL_ONE_MINUS_CONSTANT_ALPHA),
		// @formatter:on

		;
		/**
		 * 内部ID
		 */
		public final int id;
		/**
		 * GLEnum
		 */
		public final int glEnum;

		private BlendType(final int id, final int glEnum) {
			this.id = id;
			this.glEnum = glEnum;
		}

		private static final @Nonnull ImmutableMap<Integer, BlendType> blendIds;

		/**
		 * 内部IDからアルファブレンドを取得します
		 * @param id 内部ID
		 * @return アルファブレンド
		 */
		public static @Nullable BlendType fromId(final int id) {
			return blendIds.get(id);
		}

		static {
			final Builder<Integer, BlendType> builder = ImmutableMap.builder();
			for (final BlendType blend : BlendType.values())
				builder.put(blend.id, blend);
			blendIds = builder.build();
		}
	}
}
