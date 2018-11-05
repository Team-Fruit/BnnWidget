package net.teamfruit.bnnwidget.font;

import javax.annotation.Nonnull;

import net.teamfruit.bnnwidget.position.Area;
import net.teamfruit.bnnwidget.render.WGui.Align;
import net.teamfruit.bnnwidget.render.WGui.VerticalAlign;

/**
 * 範囲に沿って適切なサイズのフォントを描画します。
 *
 * @author TeamFruit
 */
public class WFontRenderer implements WFont {
	private final @Nonnull WFont font;

	private @Nonnull FontPosition setting = new FontPosition();
	private @Nonnull FontPosition p = new FontPosition();

	public WFontRenderer(@Nonnull final WFont font) {
		this.font = font;
	}

	@Override
	public void drawString(@Nonnull final FontPosition p) {
		this.font.drawString(p);
	}

	@Override
	public float getWidth(@Nonnull final FontPosition p) {
		return this.font.getWidth(p);
	}

	@Override
	public float getHeight(@Nonnull final FontPosition p) {
		return this.font.getHeight(p);
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
		final float abswidth = w;
		final float absheight = h;
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
		this.p.set(this.p).setScale(1).setText(str).setAlign(align).setPosition(x+dx, y).setShadow(shadow);
		final float fontwidth = this.font.getWidth(this.p);
		final float fontheight = this.font.getHeight(this.p);
		if (fontwidth/fontheight>abswidth/absheight)
			correctscale *= abswidth/fontwidth;
		else
			correctscale *= absheight/fontheight;
		final float correctheight = absheight*correctscale;
		this.font.drawString(this.p.setScale(correctscale));
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
