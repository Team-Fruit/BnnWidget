package com.kamesuta.mc.bnnwidget.render;

import static org.lwjgl.opengl.GL11.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;

/**
 * 基本描画準備を担当します
 *
 * @author TeamFruit
 */
public class WRenderer {
	/**
	 * Tessellatorインスタンス
	 * <p>
	 * 描画に使用します
	 */
	public static final @Nonnull Tessellator t = Tessellator.getInstance();
	/**
	 * VertexBufferインスタンス
	 * <p>
	 * 描画に使用します
	 */
	public static final @Nonnull VertexBuffer w = t.getBuffer();

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
