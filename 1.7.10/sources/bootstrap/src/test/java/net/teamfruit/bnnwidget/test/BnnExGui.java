package net.teamfruit.bnnwidget.test;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiScreen;
import net.teamfruit.bnnwidget.WBase;
import net.teamfruit.bnnwidget.WEvent;
import net.teamfruit.bnnwidget.WFrame;
import net.teamfruit.bnnwidget.WPanel;
import net.teamfruit.bnnwidget.compat.Compat;
import net.teamfruit.bnnwidget.compat.OpenGL;
import net.teamfruit.bnnwidget.component.MScaledLabel;
import net.teamfruit.bnnwidget.font.MFont;
import net.teamfruit.bnnwidget.font.WFontRenderer;
import net.teamfruit.bnnwidget.position.Area;
import net.teamfruit.bnnwidget.position.Coord;
import net.teamfruit.bnnwidget.position.Point;
import net.teamfruit.bnnwidget.position.R;
import net.teamfruit.bnnwidget.render.RenderOption;
import net.teamfruit.bnnwidget.render.WGui;
import net.teamfruit.bnnwidget.render.WRenderer;
import net.teamfruit.bnnwidget.var.VCommon;
import net.teamfruit.bnnwidget.var.VType;

public class BnnExGui extends WFrame {

	{
		setFixGuiScale(true);
	}

	public BnnExGui() {
		super();
	}

	public BnnExGui(final GuiScreen parent) {
		super(parent);
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
	}

	// init widgets in "initWidget"
	@Override
	protected void initWidget() {
		setGuiPauseGame(false);

		// add your panel extends "WPanel"
		add(new WPanel(new R()) {
			@Override
			protected void initWidget() {
				final VCommon vwidth = new VCommon() {
					@Override
					public float getAbsCoord(final float a, final float b) {
						return VType.Absolute.calc(a, b, get());
					}

					@Override
					public float get() {
						return point.x();
					}
				};
				final VCommon vheight = new VCommon() {
					@Override
					public float getAbsCoord(final float a, final float b) {
						return VType.Absolute.calc(a, b, get());
					}

					@Override
					public float get() {
						return point.y();
					}
				};

				final WFontRenderer font = new WFontRenderer(new MFont(Compat.getFontRenderer().getFontRendererObj()));
				add(new WBase(new R(Coord.left(10), Coord.top(10), Coord.width(vwidth), Coord.height(vheight))) {
					@Override
					public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity, final RenderOption opt) {
						final Area a = getGuiPosition(pgp);
						OpenGL.glColor(Color.LIGHT_GRAY);
						WRenderer.startShape();
						WGui.draw(a);
						OpenGL.glColor(Color.BLACK);
						WRenderer.startTexture();
						font.drawString("kjhdazrgckajgc", a, ev.owner.guiScale(), Align.CENTER, VerticalAlign.MIDDLE, false);
					}
				});
				add(new MScaledLabel(new R(Coord.left(10), Coord.top(100), Coord.width(vwidth), Coord.height(vheight))).setText("kjhdazrgckajgc"));
				add(new WBase(new R(Coord.left(10), Coord.top(100), Coord.width(vwidth), Coord.height(vheight))) {
					@Override
					public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity, final RenderOption opt) {
						final Area a = getGuiPosition(pgp);
						OpenGL.glColor(Color.GRAY);
						WRenderer.startShape();
						WGui.draw(a, GL11.GL_LINE_LOOP);
					}
				});
			}

			private Point mousepoint = new Point();
			private Point point = new Point(50, 20);

			@Override
			public boolean mouseClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
				this.mousepoint = p;
				return super.mouseClicked(ev, pgp, p, button);
			}

			@Override
			public boolean mouseDragged(final WEvent ev, final Area pgp, final Point p, final int button, final long time) {
				final Point p2 = new Point(p.x()-this.mousepoint.x(), p.y()-this.mousepoint.y());
				this.mousepoint = p;
				this.point = new Point(this.point.x()+p2.x(), this.point.y()+p2.y());
				return super.mouseDragged(ev, pgp, p, button, time);
			}
		});
	}
}