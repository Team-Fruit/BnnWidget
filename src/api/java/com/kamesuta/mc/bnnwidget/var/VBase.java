package com.kamesuta.mc.bnnwidget.var;

import javax.annotation.Nonnull;

/**
 * 絶対的な値と相対的な値を定義た値です
 *
 * @author TeamFruit
 */
public class VBase implements VCommon {
	private final float coord;
	private @Nonnull VType type;

	public VBase(final float coord, final @Nonnull VType type) {
		this.coord = coord;
		this.type = type;
	}

	@Override
	public float get() {
		return this.coord;
	}

	@Override
	public float getAbsCoord(final float a, final float b) {
		return this.type.calc(a, b, get());
	}
}