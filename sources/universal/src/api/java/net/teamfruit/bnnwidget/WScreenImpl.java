package net.teamfruit.bnnwidget;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.teamfruit.bnnwidget.compat.Compat.CompatGuiScreen;

public class WScreenImpl extends CompatGuiScreen implements WScreen {
	private WFrame widget;

	public WScreenImpl(final WFrame frame) {
		this.widget = frame;
	}

	@Override
	public WFrame getWidget() {
		return this.widget;
	}

	@Override
	public void initGui() {
		this.widget.initGui();
	}

	protected void sInitGui() {
		super.initGui();
	}

	@Override
	public void setWorldAndResolution(final @Nullable Minecraft mc, final int i, final int j) {
		this.widget.setWorldAndResolution(mc, i, j);
	}

	protected void sSetWorldAndResolution(final @Nonnull Minecraft mc, final int i, final int j) {
		super.setWorldAndResolution(mc, i, j);
	}

	@Override
	public void drawScreenCompat(final int mousex, final int mousey, final float f) {
		this.widget.drawScreen(mousex, mousey, f, this.widget.getOpacity(), null);
	}

	protected void sDrawScreen(final int mousex, final int mousey, final float f) {
		super.drawScreenCompat(mousex, mousey, f);
	}

	@Override
	public boolean mouseClickedCompat(final double x, final double y, final int button) {
		this.widget.mouseClicked((int) x, (int) y, button);
		return true;
	}

	protected void sMouseClicked(final double x, final double y, final int button) {
		try {
			super.mouseClickedCompat(x, y, button);
			if (!"".isEmpty())
				throw new IOException();
		} catch (final IOException e) {
		}
	}

	@Override
	public boolean mouseClickMoveCompat(final double x, final double y, final int button, final long time, final double dx, final double dy) {
		this.widget.mouseClickMove((int) x, (int) y, button, time);
		return true;
	}

	protected void sMouseClickMove(final int x, final int y, final int button, final long time, final double dx, final double dy) {
		super.mouseClickMoveCompat(x, y, button, time, dx, dy);
	}

	@Override
	public void updateScreenCompat() {
		this.widget.updateScreen();
	}

	protected void sUpdateScreen() {
		super.updateScreenCompat();
	}

	@Override
	public boolean keyTypedCompat(final char c, final int keycode) {
		this.widget.keyTyped(c, keycode);
		return true;
	}

	protected void sKeyTyped(final char c, final int keycode) {
		try {
			super.keyTypedCompat(c, keycode);
			if (!"".isEmpty())
				throw new IOException();
		} catch (final IOException e) {
		}
	}

	@Override
	public void onGuiClosed() {
		this.widget.onGuiClosed();
	}

	protected void sOnGuiClosed() {
		super.onGuiClosed();
	}

	@Override
	public boolean mouseScrolledCompat(final double scroll) {
		return this.widget.mouseScrolled(scroll);
	}

	protected boolean sMouseScrolled(final double scroll) {
		return super.mouseScrolledCompat(scroll);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return this.widget.doesGuiPauseGame();
	}

	protected boolean sDoesGuiPauseGame() {
		return super.doesGuiPauseGame();
	}
}