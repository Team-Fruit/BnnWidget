package com.kamesuta.mc.bnnwidget.font;

import java.awt.Font;

import javax.annotation.Nonnull;

import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.render.WGui.Align;

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

	public @Nonnull WFontRenderer setSetting(@Nonnull final FontPosition setting) {
		this.setting = setting;
		return this;
	}

	public @Nonnull FontPosition getSetting() {
		return this.setting;
	}

	public void drawString(final String str, final float x, final float y, final float w, final float h, final float scale, final @Nonnull Align align) {
		final float abswidth = w/scale;
		final float absheight = h/scale;
		float correctscale = 1f;
		this.p.set(this.p).setScale(1f).setText(str).setAlign(align).setPosition(x, y).setFontSize(Math.round(absheight));
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

	public void drawString(final String str, final Area a, final float scale, final Align align) {
		drawString(str, a.x1(), a.y1(), a.w(), a.h(), scale, align);
	}
}
