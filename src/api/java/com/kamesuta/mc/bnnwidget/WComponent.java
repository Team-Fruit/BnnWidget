package com.kamesuta.mc.bnnwidget;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.render.WGui;

/**
 * 実装を含まない、コンポーネントの基盤です。
 *
 * @author TeamFruit
 */
public class WComponent extends WGui implements WCommon {
	@Override
	@OverridablePoint
	public void onAdded() {
	}

	@Override
	@OverridablePoint
	public void onInit(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
	}

	@Override
	@OverridablePoint
	public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity) {
	}

	@Override
	@OverridablePoint
	public void update(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
	}

	@Override
	@OverridablePoint
	public boolean keyTyped(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final char c, final int keycode) {
		return false;
	}

	@Override
	@OverridablePoint
	public boolean mouseScrolled(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int scroll) {
		return false;
	}

	@Override
	@OverridablePoint
	public boolean mouseMoved(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
		return false;
	}

	@Override
	@OverridablePoint
	public boolean mouseClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
		return false;
	}

	@Override
	@OverridablePoint
	public boolean mouseDragged(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button, final long time) {
		return false;
	}

	@Override
	@OverridablePoint
	public boolean mouseReleased(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
		return false;
	}

	@Override
	@OverridablePoint
	public boolean onCloseRequest() {
		return true;
	}

	@Override
	@OverridablePoint
	public boolean onClosing(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
		return true;
	}

	@Override
	@OverridablePoint
	public @Nullable WCommon top(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
		return null;
	}
}
