package com.kamesuta.mc.bnnwidget.example.gui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Timer;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VCommon;
import com.kamesuta.mc.bnnwidget.var.VMotion;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiMain extends WFrame {

	{
		setFixGuiScale(true);
	}

	public GuiMain() {
		super();
	}

	public GuiMain(final GuiScreen parent) {
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
			private float x, y;
			private float w = 100, h = 15;

			@Override
			protected void initWidget() {
				// "R" is a class that relatively expresses an area.
				add(new WBase(new R()) {
					// "VMotion" is a constantly changing value depending on the specified animation.
					VMotion m = V.pm(0).add(Easings.easeLinear.move(.5f, .25f)).start();

					@Override
					public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float opacity) {
						Timer.tick();
						// Let's prepare before rendering with "WRenderer.startShape()"
						WRenderer.startShape();
						// BnnWidget provides a GL wrapper that absorbs differences in Minecraft versions.
						// The value of the "V" class is retrieved by get () every time.
						OpenGL.glColor4f(0f, 0f, 0f, this.m.get());
						// "WGui" has drawing methods that can be specified in detail with a float value.
						draw(getGuiPosition(pgp));
					}

					//private boolean mouseinside;

					@Override
					public void update(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
						super.update(ev, pgp, p);
					}

					// It is called when the GUI is closed. If you want to add closing animation, return false and let's wait.
					@Override
					public boolean onCloseRequest() {
						this.m.stop().add(Easings.easeLinear.move(.5f, 0f));
						return false;
					}

					// The GUI will not exit until it returns true. Let's see if the animation has ended with isFinished.
					@Override
					public boolean onClosing(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point mouse) {
						return this.m.isFinished();
					}
				});

				final R box = new R(
						Coord.left(new VCommon() {
							@Override
							public float getAbsCoord(final float a, final float b) {
								return get();
							}

							@Override
							public float get() {
								return x;
							}
						}),
						Coord.top(new VCommon() {
							@Override
							public float getAbsCoord(final float a, final float b) {
								return get();
							}

							@Override
							public float get() {
								return y;
							}
						}),
						Coord.width(new VCommon() {
							@Override
							public float getAbsCoord(final float a, final float b) {
								return get();
							}

							@Override
							public float get() {
								return w;
							}
						}),
						Coord.height(new VCommon() {
							@Override
							public float getAbsCoord(final float a, final float b) {
								return get();
							}

							@Override
							public float get() {
								return h;
							}
						}));
				//new R(Coord.width(200), Coord.height(200), Coord.pleft(.5f), Coord.ptop(.5f)).child(Coord.pleft(-.5f), Coord.ptop(-.5f))
				add(new WPanel(box) {
					@Override
					protected void initWidget() {
						super.initWidget();
					}

					private @Nullable Point point;

					private float wid;
					private float hei;

					@Override
					public boolean mouseClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
						this.point = p;
						if (button==0) {
							this.wid = w;
							this.hei = h;
						} else {
							this.wid = x;
							this.hei = y;
						}
						;
						return super.mouseClicked(ev, pgp, p, button);
					}

					@Override
					public boolean mouseDragged(final WEvent ev, final Area pgp, final Point p, final int button, final long time) {
						final Point point = this.point;
						if (point!=null)
							if (button==0) {
								w = this.wid-point.x()+p.x();
								h = this.hei-point.y()+p.y();
							} else {
								x = this.wid-point.x()+p.x();
								y = this.hei-point.y()+p.y();
							}
						return super.mouseDragged(ev, pgp, p, button, time);
					}

					private final @Nonnull ResourceLocation expimg = new ResourceLocation("bnnwidget", "textures/expimg.png");

					@Override
					public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
						final Area a = getGuiPosition(pgp);
						WRenderer.startTexture();
						OpenGL.glColor4f(1f, 1f, 1f, 1f);
						texture().bindTexture(this.expimg);
						OpenGL.glColor4f(1f, 1f, 1f, .5f);
						final Area b = new Area(.5f, 0f, 3.5f, 2f);
						drawTexture(pgp, b);
						OpenGL.glColor4f(1f, 1f, 1f, 1f);
						drawTextureTrim(pgp, a, b);
						WRenderer.startShape();
						OpenGL.glLineWidth(.5f);
						OpenGL.glColor4f(1f, 1f, 1f, 1f);
						draw(a, GL11.GL_LINE_LOOP);
						super.draw(ev, pgp, p, frame, popacity);
					}
				});
			}
		});
	}
}