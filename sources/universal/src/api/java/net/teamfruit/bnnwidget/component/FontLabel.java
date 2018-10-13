package net.teamfruit.bnnwidget.component;

import javax.annotation.Nonnull;

import net.teamfruit.bnnwidget.WEvent;
import net.teamfruit.bnnwidget.compat.Compat;
import net.teamfruit.bnnwidget.font.WFontRenderer;
import net.teamfruit.bnnwidget.motion.Motion;
import net.teamfruit.bnnwidget.position.Area;
import net.teamfruit.bnnwidget.position.Coord;
import net.teamfruit.bnnwidget.position.Point;
import net.teamfruit.bnnwidget.position.R;
import net.teamfruit.bnnwidget.var.V;
import net.teamfruit.bnnwidget.var.VMotion;
import net.teamfruit.bnnwidget.var.VPercent;

public class FontLabel extends FontScaledLabel {
	protected final @Nonnull VMotion height = V.pm(1f);
	private final @Nonnull VPercent absheight = V.per(V.a(0f), V.a(Compat.getFontRenderer().getFontRendererObj().FONT_HEIGHT), this.height);
	private final @Nonnull R limit = new R(Coord.height(this.absheight));

	public FontLabel(final @Nonnull R position, final @Nonnull WFontRenderer wf) {
		super(position, wf);
	}

	@Override
	protected void drawText(final @Nonnull WEvent ev, final @Nonnull Area a, final @Nonnull Point p, final float frame, final float opacity) {
		super.drawText(ev, a.child(this.limit), p, frame, opacity);
	}

	public float getScale() {
		return this.height.get();
	}

	public @Nonnull FontLabel setScale(final float scale) {
		this.height.add(Motion.move(scale));
		return this;
	}
}
