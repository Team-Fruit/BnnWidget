package com.kamesuta.mc.bnnwidget.component;

import javax.annotation.Nonnull;

import com.kamesuta.mc.bnnwidget.compat.Compat;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.R;

/**
 * コンポーネントの大きさによって可変なテキストラベルです
 *
 * @author TeamFruit
 */
public class MScaledLabel extends MLabel {
	public MScaledLabel(final @Nonnull R position) {
		super(position);
	}

	@Override
	public float getScaleWidth(final @Nonnull Area a) {
		final float f1 = a.w()/Compat.getFontRenderer().getStringWidth(getText());
		final float f2 = a.h()/Compat.getFontRenderer().getFontRendererObj().FONT_HEIGHT;
		return Math.min(f1, f2);
	}

	@Override
	public float getScaleHeight(final @Nonnull Area a) {
		final float f1 = a.w()/Compat.getFontRenderer().getStringWidth(getText());
		final float f2 = a.h()/Compat.getFontRenderer().getFontRendererObj().FONT_HEIGHT;
		return Math.min(f1, f2);
	}
}
