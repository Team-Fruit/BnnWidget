package com.kamesuta.mc.bnnwidget.position;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 絶対範囲を表現します
 *
 * @author TeamFruit
 */
public class Area {
	/**
	 * 1つ目のX絶対座標
	 */
	protected final float x1;
	/**
	 * 1つ目のY絶対座標
	 */
	protected final float y1;
	/**
	 * 2つ目のX絶対座標
	 */
	protected final float x2;
	/**
	 * 2つ目のY絶対座標
	 */
	protected final float y2;

	/**
	 * (ax1, ay1)⇒(ax2, ay2)の範囲
	 * @param ax1 絶対座標
	 * @param ay1 絶対座標
	 * @param ax2 絶対座標
	 * @param ay2 絶対座標
	 */
	public Area(final float ax1, final float ay1, final float ax2, final float ay2) {
		this.x1 = ax1;
		this.y1 = ay1;
		this.x2 = ax2;
		this.y2 = ay2;
	}

	/**
	 * 絶対幅
	 * @return 絶対幅
	 */
	public float w() {
		return Math.abs(this.x2-this.x1);
	}

	/**
	 * 絶対高さ
	 * @return 絶対高さ
	 */
	public float h() {
		return Math.abs(this.y2-this.y1);
	}

	/**
	 * 1つ目のX絶対座標
	 */
	public float x1() {
		return this.x1;
	}

	/**
	 * 1つ目のY絶対座標
	 */
	public float y1() {
		return this.y1;
	}

	/**
	 * 2つ目のX絶対座標
	 */
	public float x2() {
		return this.x2;
	}

	/**
	 * 2つ目のY絶対座標
	 */
	public float y2() {
		return this.y2;
	}

	/**
	 * 左側のX絶対座標
	 */
	public float minX() {
		return Math.min(x1(), x2());
	}

	/**
	 * 右側のX絶対座標
	 */
	public float maxX() {
		return Math.max(x1(), x2());
	}

	/**
	 * 上側のY絶対座標
	 */
	public float minY() {
		return Math.min(y1(), y2());
	}

	/**
	 * 下側のY絶対座標
	 */
	public float maxY() {
		return Math.max(y1(), y2());
	}

	/**
	 * 絶対範囲と相対範囲を利用し、新たな絶対範囲を生成します
	 * @param p 相対範囲
	 * @return 新たな絶対範囲
	 */
	public @Nonnull Area child(final R p) {
		return p.getAbsolute(this);
	}

	/**
	 * 絶対範囲と各座標の差を利用し、新たな絶対範囲を生成します
	 * @param ax1 1つ目のX座標の差
	 * @param ay1 1つ目のY座標の差
	 * @param ax2 2つ目のX座標の差
	 * @param ay2 2つ目のY座標の差
	 * @return 新たな絶対範囲
	 */
	public @Nonnull Area child(final float ax1, final float ay1, final float ax2, final float ay2) {
		return new Area(this.x1+ax1, this.y1+ay1, this.x2+ax2, this.y2+ay2);
	}

	/**
	 * この絶対範囲が有効な矩形であるかどうか
	 * @return この絶対範囲が有効な矩形である場合true
	 */
	public boolean isVaild() {
		return this.x1!=this.x2&&this.y1!=this.y2;
	}

	/**
	 * この範囲の中に点が含まれるかどうか
	 * @param p 点
	 * @return この範囲の中に点が含まれる場合true
	 */
	public boolean pointInside(final @Nonnull Point p) {
		return p.x>=this.x1&&p.x<this.x2&&p.y>=this.y1&&p.y<this.y2;
	}

	/**
	 * 範囲がもう一つの範囲に重なっているかどうか
	 * @param a もう一つの範囲
	 * @return 範囲がもう一つの範囲に重なっている場合true
	 */
	public boolean areaOverlap(final @Nonnull Area a) {
		return !(a.maxX()<minX()||a.minX()>maxX()||a.minY()>maxY()||a.maxY()<minY());
	}

	/**
	 * 範囲がもう一つの範囲の中に納まるかどうか
	 * @param a もう一つの範囲
	 * @return 範囲がもう一つの範囲の中に納まる場合true
	 */
	public boolean areaInside(final @Nonnull Area a) {
		return a.minX()>=minX()&&a.minY()>=minY()&&a.maxX()<=maxX()&&a.maxY()<=maxY();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime*result+Float.floatToIntBits(this.x1);
		result = prime*result+Float.floatToIntBits(this.x2);
		result = prime*result+Float.floatToIntBits(this.y1);
		result = prime*result+Float.floatToIntBits(this.y2);
		return result;
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (this==obj)
			return true;
		if (obj==null)
			return false;
		if (!(obj instanceof Area))
			return false;
		final Area other = (Area) obj;
		if (Float.floatToIntBits(this.x1)!=Float.floatToIntBits(other.x1))
			return false;
		if (Float.floatToIntBits(this.x2)!=Float.floatToIntBits(other.x2))
			return false;
		if (Float.floatToIntBits(this.y1)!=Float.floatToIntBits(other.y1))
			return false;
		if (Float.floatToIntBits(this.y2)!=Float.floatToIntBits(other.y2))
			return false;
		return true;
	}

	@Override
	public @Nonnull String toString() {
		return String.format("Area [x1=%s, y1=%s, x2=%s, y2=%s]", this.x1, this.y1, this.x2, this.y2);
	}
}
