package com.kamesuta.mc.bnnwidget.render;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.bnnwidget.position.Area;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;

/**
 * GUI関連の描画を担当します
 * <p>
 * 各ウィジェットはこのクラスを継承します
 *
 * @author TeamFruit
 */
public class WGui extends Gui {
	/**
	 * Minecraftインスタンス
	 */
	public static final @Nonnull Minecraft mc = FMLClientHandler.instance().getClient();
	/**
	 * Tessellatorインスタンス
	 * <p>
	 * 描画に使用します
	 */
	public static final @Nonnull Tessellator t = WRenderer.t;
	/**
	 * VertexBufferインスタンス
	 * <p>
	 * 描画に使用します
	 */
	public static final @Nonnull BufferBuilder w = WRenderer.w;
	// public static final StencilClip clip = StencilClip.instance;

	/**
	 * テクスチャ倍率
	 * <p>
	 * GUIを描画する際に使用します。
	 */
	public static final float textureScale = 0.00390625F;

	/**
	 * 4つの絶対座標からテクスチャを描画します
	 * <p>
	 * テクスチャ座標(倍率)は(0, 0)⇒(1, 1)にすることでテクスチャを1枚表示できます
	 * @param vx1 1つ目のX絶対座標
	 * @param vy1 1つ目のY絶対座標
	 * @param vx2 2つ目のX絶対座標
	 * @param vy2 2つ目のY絶対座標
	 * @param tx1 1つ目のXテクスチャ座標(倍率)
	 * @param ty1 1つ目のYテクスチャ座標(倍率)
	 * @param tx2 2つ目のXテクスチャ座標(倍率)
	 * @param ty2 2つ目のYテクスチャ座標(倍率)
	 */
	public static void drawTextureAbs(final float vx1, final float vy1, final float vx2, final float vy2, final float tx1, final float ty1, final float tx2, final float ty2) {
		w.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		w.pos(vx1, vy2, 0).tex(tx1, ty2).endVertex();
		w.pos(vx2, vy2, 0).tex(tx2, ty2).endVertex();
		w.pos(vx2, vy1, 0).tex(tx2, ty1).endVertex();
		w.pos(vx1, vy1, 0).tex(tx1, ty1).endVertex();
		t.draw();
	}

	/**
	 * 4つの絶対座標からテクスチャを描画します
	 * <p>
	 * テクスチャ座標(倍率)は(0, 0)⇒(1, 1)が使用されます
	 * @param vx1 1つ目のX絶対座標
	 * @param vy1 1つ目のY絶対座標
	 * @param vx2 2つ目のX絶対座標
	 * @param vy2 2つ目のY絶対座標
	 */
	public static void drawTextureAbs(final float vx1, final float vy1, final float vx2, final float vy2) {
		drawTextureAbs(vx1, vy1, vx2, vy2, 0, 0, 1, 1);
	}

	/**
	 * 2つの絶対座標と2つの絶対サイズからテクスチャを描画します
	 * <p>
	 * テクスチャ座標(倍率)は(0, 0)：(1×1)にすることでテクスチャを1枚表示できます
	 * @param vx X絶対座標
	 * @param vy Y絶対座標
	 * @param vw 絶対幅
	 * @param vh 絶対高さ
	 * @param tx Xテクスチャ座標(倍率)
	 * @param ty Yテクスチャ座標(倍率)
	 * @param tw テクスチャ幅(倍率)
	 * @param th テクスチャ高さ(倍率)
	 */
	public static void drawTextureSize(final float vx, final float vy, final float vw, final float vh, final float tx, final float ty, final float tw, final float th) {
		drawTextureAbs(vx, vy, vx+vw, vy+vh, tx, ty, tx+tw, ty+th);
	}

	/**
	 * 2つの絶対座標と2つの絶対サイズからテクスチャを描画します
	 * <p>
	 * テクスチャ座標(倍率)は(0, 0)：(1×1)が使用されます
	 * @param vx X絶対座標
	 * @param vy Y絶対座標
	 * @param vw 絶対幅
	 * @param vh 絶対高さ
	 */
	public static void drawTextureSize(final float vx, final float vy, final float vw, final float vh) {
		drawTextureAbs(vx, vy, vx+vw, vy+vh, 0, 0, 1, 1);
	}

	/**
	 * 絶対範囲からテクスチャを描画します
	 * <p>
	 * テクスチャ座標(倍率)は(0, 0)⇒(1, 1)にすることでテクスチャを1枚表示できます
	 * @param vertex 絶対範囲
	 * @param textrue テクスチャ範囲
	 */
	public static void drawTexture(final @Nonnull Area vertex, final @Nonnull Area textrue) {
		drawTextureAbs(vertex.x1(), vertex.y1(), vertex.x2(), vertex.y2(), textrue.x1(), textrue.y1(), textrue.x2(), textrue.y2());
	}

	/**
	 * 絶対範囲からテクスチャを描画します
	 * <p>
	 * テクスチャ座標(倍率)は(0, 0)⇒(1, 1)が使用されます
	 * @param vertex 絶対範囲
	 */
	public static void drawTexture(final @Nonnull Area vertex) {
		drawTextureAbs(vertex.x1(), vertex.y1(), vertex.x2(), vertex.y2(), 0, 0, 1, 1);
	}

	/**
	 * テクスチャ倍率(0.00390625)をかけ、{@link #drawTextureAbs(float, float, float, float, float, float, float, float)}と同様に描画します。
	 * <p>
	 * GUIを描画する場合主にこちらを使用します。
	 * @param vx1 1つ目のX絶対座標
	 * @param vy1 1つ目のY絶対座標
	 * @param vx2 2つ目のX絶対座標
	 * @param vy2 2つ目のY絶対座標
	 * @param tx1 1つ目のXテクスチャ座標(倍率)
	 * @param ty1 1つ目のYテクスチャ座標(倍率)
	 * @param tx2 2つ目のXテクスチャ座標(倍率)
	 * @param ty2 2つ目のYテクスチャ座標(倍率)
	 */
	public static void drawTextureModalAbs(final float vx1, final float vy1, final float vx2, final float vy2, final float tx1, final float ty1, final float tx2, final float ty2) {
		final float f = textureScale;
		drawTextureAbs(vx1, vy1, vx2, vy2, f*tx1, f*ty1, f*tx2, f*ty2);
	}

	/**
	 * テクスチャ倍率(0.00390625)をかけ、{@link #drawTextureAbs(float, float, float, float)}と同様に描画します。
	 * <p>
	 * GUIを描画する場合主にこちらを使用します。
	 * @param vx1 1つ目のX絶対座標
	 * @param vy1 1つ目のY絶対座標
	 * @param vx2 2つ目のX絶対座標
	 * @param vy2 2つ目のY絶対座標
	 */
	public static void drawTextureModalAbs(final float vx1, final float vy1, final float vx2, final float vy2) {
		drawTextureModalAbs(vx1, vy1, vx2, vy2, 0, 0, 1, 1);
	}

	/**
	 * テクスチャ倍率(0.00390625)をかけ、{@link #drawTextureSize(float, float, float, float, float, float, float, float)}と同様に描画します。
	 * <p>
	 * GUIを描画する場合主にこちらを使用します。
	 * @param vx X絶対座標
	 * @param vy Y絶対座標
	 * @param vw 絶対幅
	 * @param vh 絶対高さ
	 * @param tx Xテクスチャ座標(倍率)
	 * @param ty Yテクスチャ座標(倍率)
	 * @param tw テクスチャ幅(倍率)
	 * @param th テクスチャ高さ(倍率)
	 */
	public static void drawTextureModalSize(final float vx, final float vy, final float vw, final float vh, final float tx, final float ty, final float tw, final float th) {
		drawTextureModalAbs(vx, vy, vx+vw, vy+vh, tx, ty, tx+tw, ty+th);
	}

	/**
	 * テクスチャ倍率(0.00390625)をかけ、{@link #drawTextureModalAbs(float, float, float, float)}と同様に描画します。
	 * <p>
	 * GUIを描画する場合主にこちらを使用します。
	 * @param vx X絶対座標
	 * @param vy Y絶対座標
	 * @param vw 絶対幅
	 * @param vh 絶対高さ
	 */
	public static void drawTextureModalSize(final float vx, final float vy, final float vw, final float vh) {
		drawTextureModalAbs(vx, vy, vx+vw, vy+vh, 0, 0, 1, 1);
	}

	/**
	 * テクスチャ倍率(0.00390625)をかけ、{@link #drawTextureModal(Area, Area)}と同様に描画します。
	 * <p>
	 * GUIを描画する場合主にこちらを使用します。
	 * @param vertex 絶対範囲
	 * @param textrue テクスチャ範囲
	 */
	public static void drawTextureModal(final @Nonnull Area vertex, final @Nonnull Area textrue) {
		drawTextureModalAbs(vertex.x1(), vertex.y1(), vertex.x2(), vertex.y2(), textrue.x1(), textrue.y1(), textrue.x2(), textrue.y2());
	}

	/**
	 * テクスチャ倍率(0.00390625)をかけ、{@link #drawTextureModal(Area)}と同様に描画します。
	 * <p>
	 * GUIを描画する場合主にこちらを使用します。
	 * @param vertex 絶対範囲
	 */
	public static void drawTextureModal(final @Nonnull Area vertex) {
		drawTextureModalAbs(vertex.x1(), vertex.y1(), vertex.x2(), vertex.y2(), 0, 0, 1, 1);
	}

	/**
	 * 4つの絶対座標とGL描画モードを使用して描画します。
	 * @param x1 1つ目のX絶対座標
	 * @param y1 1つ目のY絶対座標
	 * @param x2 2つ目のX絶対座標
	 * @param y2 2つ目のY絶対座標
	 * @param mode GL描画モード
	 */
	public static void drawAbs(final float x1, final float y1, final float x2, final float y2, final int mode) {
		w.begin(mode, DefaultVertexFormats.POSITION);
		w.pos(x1, y2, 0).endVertex();
		w.pos(x2, y2, 0).endVertex();
		w.pos(x2, y1, 0).endVertex();
		w.pos(x1, y1, 0).endVertex();
		t.draw();
	}

	/**
	 * 2つの絶対座標と2つの絶対サイズとGL描画モードを使用して描画します。
	 * @param x X絶対座標
	 * @param y Y絶対座標
	 * @param w 絶対幅
	 * @param h 絶対高さ
	 * @param mode GL描画モード
	 */
	public static void drawSize(final float x, final float y, final float w, final float h, final int mode) {
		drawAbs(x, y, x+w, y+h, mode);
	}

	/**
	 * 絶対範囲とGL描画モードを使用して描画します。
	 * @param p 絶対範囲
	 * @param mode GL描画モード
	 */
	public static void draw(final @Nonnull Area p, final int mode) {
		drawAbs(p.x1(), p.y1(), p.x2(), p.y2(), mode);
	}

	/**
	 * 4つの絶対座標を使用して描画します。
	 * <p>
	 * GL描画モードは{@link org.lwjgl.opengl.GL11#GL_QUADS GL_QUADS}が使用されます
	 * @param x1 1つ目のX絶対座標
	 * @param y1 1つ目のY絶対座標
	 * @param x2 2つ目のX絶対座標
	 * @param y2 2つ目のY絶対座標
	 */
	public static void drawAbs(final float x1, final float y1, final float x2, final float y2) {
		drawAbs(x1, y1, x2, y2, GL_QUADS);
	}

	/**
	 * 2つの絶対座標と2つの絶対サイズを使用して描画します。
	 * <p>
	 * GL描画モードは{@link org.lwjgl.opengl.GL11#GL_QUADS GL_QUADS}が使用されます
	 * @param x X絶対座標
	 * @param y Y絶対座標
	 * @param w 絶対幅
	 * @param h 絶対高さ
	 */
	public static void drawSize(final float x, final float y, final float w, final float h) {
		drawAbs(x, y, x+w, y+h, GL_QUADS);
	}

	/**
	 * 絶対範囲を使用して描画します。
	 * <p>
	 * GL描画モードは{@link org.lwjgl.opengl.GL11#GL_QUADS GL_QUADS}が使用されます
	 * @param p 絶対範囲
	 */
	public static void draw(final @Nonnull Area p) {
		draw(p, GL_QUADS);
	}

	private static int fontcolor;

	/**
	 * 次の描画で使用されるフォントカラーを設定します
	 * <p>
	 * ※一度使用されると元のフォントカラーに戻ります
	 * @param color フォントカラー
	 */
	public static void fontColor(final int color) {
		WGui.fontcolor = color;
	}

	/**
	 * 次の描画で使用されるフォントカラーを設定します
	 * <p>
	 * ※一度使用されると元のフォントカラーに戻ります
	 * <br>
	 * フォントカラーの範囲は0～255です
	 * @param r フォントカラー(赤)
	 * @param g フォントカラー(緑)
	 * @param b フォントカラー(青)
	 * @param a フォントカラー(不透明度)
	 */
	public static void fontColor(final int r, final int g, final int b, final int a) {
		fontColor(Math.max(a&0xff, 0x4)<<24|(r&0xFF)<<16|(g&0xFF)<<8|(b&0xFF)<<0);
	}

	/**
	 * 次の描画で使用されるフォントカラーを設定します
	 * <p>
	 * ※一度使用されると元のフォントカラーに戻ります
	 * <br>
	 * フォントカラーの範囲は0～1です
	 * @param r フォントカラー(赤)
	 * @param g フォントカラー(緑)
	 * @param b フォントカラー(青)
	 * @param a フォントカラー(不透明度)
	 */
	public static void fontColor(final float r, final float g, final float b, final float a) {
		fontColor((int) (r*255+0.5), (int) (g*255+0.5), (int) (b*255+0.5), (int) (a*255+0.5));
	}

	/**
	 * 次の描画で使用されるフォントカラーを設定します
	 * <p>
	 * ※一度使用されると元のフォントカラーに戻ります
	 * @param color フォントカラー
	 */
	public static void fontColor(final @Nonnull Color color) {
		fontColor(color.getRGB());
	}

	private static void resetFontColor() {
		WGui.fontcolor = 0xff000000;
	}

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
		return mc.fontRendererObj;
	}

	/**
	 * 文字列を描画します
	 * @param text 文字列
	 * @param x X絶対座標
	 * @param y Y絶対座標
	 * @param w 絶対幅
	 * @param h 絶対高さ
	 * @param align 水平寄せ
	 * @param valign 垂直寄せ
	 * @param shadow 影を付ける場合true
	 */
	public static void drawString(final @Nonnull String text, final float x, final float y, final float w, final float h, final @Nonnull Align align, final @Nonnull VerticalAlign valign, final boolean shadow) {
		OpenGL.glPushMatrix();
		align.translate(text, x, w);
		valign.translate(text, y, h);
		font().drawString(text, 0, 0, fontcolor, shadow);
		OpenGL.glPopMatrix();
		resetFontColor();
	}

	/**
	 * 文字列を描画します
	 * @param text 文字列
	 * @param a 絶対範囲
	 * @param align 水平寄せ
	 * @param valign 垂直寄せ
	 * @param shadow 影を付ける場合true
	 */
	public static void drawString(final @Nonnull String text, final @Nonnull Area a, final @Nonnull Align align, final @Nonnull VerticalAlign valign, final boolean shadow) {
		drawString(text, a.x1(), a.y1(), a.w(), a.h(), align, valign, shadow);
	}

	/**
	 * 水平寄せ
	 *
	 * @author TeamFruit
	 */
	public static enum Align {
		/**
		 * 左寄せ
		 */
		LEFT {
			@Override
			protected void translate(final @Nonnull String text, final float x, final float w) {
				OpenGL.glTranslatef(x, 0, 0);
			}
		},
		/**
		 * 中央寄せ
		 */
		CENTER {
			@Override
			protected void translate(final @Nonnull String text, final float x, final float w) {
				OpenGL.glTranslatef(x+(w-getStringWidth(text))/2, 0, 0);
			}
		},
		/**
		 * 右寄せ
		 */
		RIGHT {
			@Override
			protected void translate(final @Nonnull String text, final float x, final float w) {
				OpenGL.glTranslatef(x-getStringWidth(text), 0, 0);
			}
		},
		;
		protected abstract void translate(@Nonnull String text, float x, float w);
	}

	/**
	 * 垂直寄せ
	 *
	 * @author TeamFruit
	 */
	public static enum VerticalAlign {
		/**
		 * 上寄せ
		 */
		TOP {
			@Override
			protected void translate(final @Nonnull String text, final float y, final float h) {
				OpenGL.glTranslatef(0, y, 0);
			}
		},
		/**
		 * 中央寄せ
		 */
		MIDDLE {
			@Override
			protected void translate(final @Nonnull String text, final float y, final float h) {
				OpenGL.glTranslatef(0, y+(h-font().FONT_HEIGHT)/2, 0);
			}
		},
		/**
		 * 下寄せ
		 */
		BOTTOM {
			@Override
			protected void translate(final @Nonnull String text, final float y, final float h) {
				OpenGL.glTranslatef(0, y+h-font().FONT_HEIGHT, 0);
			}
		},
		;
		protected abstract void translate(@Nonnull String text, float y, float h);
	}

	/**
	 * 文字列の幅
	 * @param s 文字列
	 * @return 幅
	 */
	public static int getStringWidth(final @Nonnull String s) {
		if (StringUtils.isEmpty(s))
			return 0;
		return font().getStringWidth(TextFormatting.getTextWithoutFormattingCodes(s));
	}
}
