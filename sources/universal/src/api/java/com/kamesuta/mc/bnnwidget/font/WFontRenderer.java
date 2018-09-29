package com.kamesuta.mc.bnnwidget.font;

import java.awt.Font;

import javax.annotation.Nonnull;

import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.render.WGui.Align;
import com.kamesuta.mc.bnnwidget.render.WGui.VerticalAlign;

/**
 * 範囲に沿って適切なサイズのフォントを描画します。
 *
 * @author TeamFruit
 */
public class WFontRenderer implements WFont {
	public static final @Nonnull WFont defaultFont;

	static {
		final FontSet fontSet = new FontSet.Builder().addName("HGP創英角ﾎﾟｯﾌﾟ体").setStyle(Font.PLAIN).build();
		final FontStyle style = new FontStyle.Builder().setFont(fontSet).build();
		defaultFont = new TrueTypeFont(style);
	}

	private final @Nonnull WFont font;

	private @Nonnull FontPosition setting = new FontPosition();
	private @Nonnull FontPosition p = new FontPosition();

	public WFontRenderer(@Nonnull final WFont font) {
		this.font = font;
	}

	public WFontRenderer() {
		this(defaultFont);
	}

	@Override
	public float drawString(@Nonnull final FontPosition p) {
		return this.font.drawString(p);
	}

	@Override
	public float getWidth(@Nonnull final FontPosition p) {
		return this.font.getWidth(p);
	}

	@Override
	public float getCharWidth(final char ch, final int fontsize, final float scaleX) {
		return this.font.getCharWidth(ch, fontsize, scaleX);
	}

	@Override
	public @Nonnull FontStyle getStyle() {
		return this.font.getStyle();
	}

	/**
	 * 設定を設定
	 * @param setting 設定
	 * @return this
	 */
	public @Nonnull WFontRenderer setSetting(@Nonnull final FontPosition setting) {
		this.setting = setting;
		return this;
	}

	/**
	 * 設定を取得します
	 * @return
	 */
	public @Nonnull FontPosition getSetting() {
		return this.setting;
	}

	/**
	 * use {@link #drawString(String, Area, float, Align, VerticalAlign, boolean)}
	 */
	@Deprecated
	public void drawString(final String str, final float x, final float y, final float w, final float h, final float scale, final @Nonnull Align align, final @Nonnull VerticalAlign valign, final boolean shadow) {
		final float abswidth = w/scale;
		final float absheight = h/scale;
		float correctscale = 1f;
		float dx;
		switch (align) {
			default:
			case LEFT:
				dx = 0;
				break;
			case CENTER:
				dx = w/2;
				break;
			case RIGHT:
				dx = w;
				break;
		}
		this.p.set(this.p).setScale(1f).setText(str).setAlign(align).setPosition(x+dx, y).setFontSize(Math.round(absheight)).setShadow(shadow);
		final float ratiowh = this.font.getWidth(this.p)/Math.round(absheight);
		if (absheight*ratiowh>abswidth) {
			final float newwidth = absheight*ratiowh;
			correctscale *= abswidth/newwidth;
		} else
			correctscale *= 1f;
		final float correctheight = absheight*correctscale;
		final float correctedscale = correctheight/(float) Math.ceil(correctheight);
		this.font.drawString(this.p.setScale(scale*correctedscale).setFontSize((int) Math.floor(correctheight)));
	}

	/**
	 * 文字を範囲内に描画します
	 * @param str 文字列
	 * @param a 絶対範囲
	 * @param scale 解像度
	 * @param align 横寄せ
	 * @param valign 縦寄せ
	 * @param shadow 影
	 */
	public void drawString(final String str, final Area a, final float scale, final @Nonnull Align align, final @Nonnull VerticalAlign valign, final boolean shadow) {
		drawString(str, a.x1(), a.y1(), a.w(), a.h(), scale, align, valign, shadow);
	}

	/**
	 * 文字を範囲内に描画します
	 * @param str 文字列
	 * @param a 絶対範囲
	 * @param scale 解像度
	 * @param align 横寄せ
	 * @param shadow 影
	 */
	public void drawString(final String str, final Area a, final float scale, final @Nonnull Align align, final boolean shadow) {
		drawString(str, a, scale, align, VerticalAlign.TOP, shadow);
	}
}
