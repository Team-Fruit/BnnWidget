package com.kamesuta.mc.bnnwidget.component;

import java.awt.Color;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.font.WFontRenderer;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WRenderer;

public class FontLabel extends MLabel {
	private final @Nonnull WFontRenderer wf;

	public FontLabel(final @Nonnull R position, final @Nonnull WFontRenderer wf) {
		super(position);
		this.wf = wf;
	}

	@Override
	protected void drawText(final @Nonnull WEvent ev, final @Nonnull Area a, final @Nonnull Point p, final float frame, final float opacity) {
		OpenGL.glPushMatrix();
		OpenGL.glTranslated(a.x1()+a.w()/2, a.y1()+a.h()/2, 0);
		OpenGL.glScaled(getScaleWidth(a), getScaleHeight(a), 1);
		OpenGL.glTranslated(-(a.x1()+a.w()/2), -(a.y1()+a.h()/2), 0);
		WRenderer.startTexture();
		drawString(getText(), a, getAlign(), getVerticalAlign(), isShadow());

		OpenGL.glPopMatrix();

		final Color c = new Color(getColor());
		OpenGL.glColor4i(c.getRed(), c.getGreen(), c.getBlue(), (int) Math.max(4, opacity*c.getAlpha()));
		this.wf.drawString(getText(), a, ev.owner.guiScale(), getAlign(), isShadow());
		final String watermark = getWatermark();
		if (watermark!=null&&!StringUtils.isEmpty(watermark)&&StringUtils.isEmpty(getText())) {
			final Color w = new Color(getWatermarkColor());
			OpenGL.glColor4i(w.getRed(), w.getGreen(), w.getBlue(), (int) Math.max(4, opacity*c.getAlpha()));
			this.wf.drawString(getText(), a, ev.owner.guiScale(), getAlign(), isShadow());
		}
	}
}
