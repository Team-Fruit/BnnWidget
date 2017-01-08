package com.kamesuta.mc.bnnwidget.font;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.render.WGui.Align;

/**
 * 文字描画時の設定
 *
 * @author TeamFruit
 */
public class FontPosition {
	private float x, y, scaleX, scaleY;
	private int fontsize = 22;
	private @Nullable String text;
	private @Nonnull Align align = Align.LEFT;
	private boolean usecode;

	public FontPosition() {
		reset();
	}

	public FontPosition(final @Nonnull FontPosition p) {
		set(p);
	}

	public @Nonnull FontPosition reset() {
		this.x = this.y = 0;
		this.text = null;
		this.scaleX = this.scaleY = .5f;
		this.fontsize = -1;
		this.align = Align.LEFT;
		this.usecode = true;
		return this;
	}

	public @Nonnull FontPosition set(@Nonnull final FontPosition p) {
		this.x = p.getX();
		this.y = p.getY();
		this.text = p.getText();
		this.scaleX = p.getScaleX();
		this.scaleY = p.getScaleY();
		this.fontsize = p.getFontSize();
		this.align = p.getAlign();
		this.usecode = p.isUseCode();
		return this;
	}

	/**
	 * @param x 絶対X座標
	 * @return this
	 */
	public @Nonnull FontPosition setX(final float x) {
		this.x = x;
		return this;
	}

	/**
	 * @return 絶対X座標
	 */
	public float getX() {
		return this.x;
	}

	/**
	 * @param y 絶対Y座標
	 * @return this
	 */
	public @Nonnull FontPosition setY(final float y) {
		this.y = y;
		return this;
	}

	/**
	 * @return 絶対Y座標
	 */
	public float getY() {
		return this.y;
	}

	/**
	 * @param x 絶対X座標
	 * @param y 絶対Y座標
	 * @return this
	 */
	public @Nonnull FontPosition setPosition(final float x, final float y) {
		setX(x);
		setY(y);
		return this;
	}

	//	/**
	//	 * 文字列左側の切り詰め幅
	//	 * @param correctL 文字列左側の切り詰め幅
	//	 * @return this
	//	 */
	//	public @Nonnull FontPosition setCorrectL(final float correctL) {
	//		this.correctL = correctL;
	//		return this;
	//	}
	//
	//	/**
	//	 * 文字列左側の切り詰め幅
	//	 * @return 文字列左側の切り詰め幅
	//	 */
	//	public float getCorrectL() {
	//		return this.correctL;
	//	}
	//
	//	/**
	//	 * 文字列右側の切り詰め幅
	//	 * @param correctR 文字列右側の切り詰め幅
	//	 * @return this
	//	 */
	//	public @Nonnull FontPosition setCorrectR(final float correctR) {
	//		this.correctR = correctR;
	//		return this;
	//	}
	//
	//	/**
	//	 * 文字列右側の切り詰め幅
	//	 * @return 文字列右側の切り詰め幅
	//	 */
	//	public float getCorrectR() {
	//		return this.correctR;
	//	}
	//
	//	/**
	//	 * 文字列の切り詰め幅
	//	 * @param correctL 文字列左側の切り詰め幅
	//	 * @param correctR 文字列右側の切り詰め幅
	//	 * @return this
	//	 */
	//	public @Nonnull FontPosition setCorrect(final float correctL, final float correctR) {
	//		setCorrectL(correctL);
	//		setCorrectR(correctR);
	//		return this;
	//	}
	//
	//	/**
	//	 * 文字列の切り詰め幅
	//	 * @param correct 文字列の切り詰め幅
	//	 * @return this
	//	 */
	//	public @Nonnull FontPosition setCorrect(final float correct) {
	//		setCorrect(correct, correct-1f);
	//		return this;
	//	}

	/**
	 * @param str 描画文字
	 * @return this
	 */
	public @Nonnull FontPosition setText(final String str) {
		this.text = str;
		return this;
	}

	/**
	 * @return 描画文字
	 */
	public @Nonnull String getText() {
		if (this.text!=null)
			return this.text;
		return this.text = "";
	}

	/**
	 * 文字開始位置
	 * @return 0
	 */
	public int getStartIndex() {
		return 0;
	}

	/**
	 * 文字終了位置
	 * @return {@link #getText()}.{@link String#length() length()}-1
	 */
	public int getEndIndex() {
		return getText().length()-1;
	}

	/**
	 * @param scaleX 横倍率
	 * @return this
	 */
	public @Nonnull FontPosition setScaleX(final float scaleX) {
		this.scaleX = scaleX;
		return this;
	}

	/**
	 * @return 横倍率
	 */
	public float getScaleX() {
		return this.scaleX;
	}

	/**
	 * @param scaleY 縦倍率
	 * @return this
	 */
	public @Nonnull FontPosition setScaleY(final float scaleY) {
		this.scaleY = scaleY;
		return this;
	}

	/**
	 * @return 縦倍率
	 */
	public float getScaleY() {
		return this.scaleY;
	}

	/**
	 * フォントサイズを設定します
	 * @param fontsize フォントサイズ
	 * @return this
	 */
	public @Nonnull FontPosition setFontSize(final int fontsize) {
		this.fontsize = fontsize;
		return this;
	}

	/**
	 * @return フォントサイズ
	 */
	public int getFontSize() {
		return this.fontsize;
	}

	/**
	 * @param scaleX 横倍率
	 * @param scaleY 縦倍率
	 * @return this
	 */
	public @Nonnull FontPosition setScale(final float scaleX, final float scaleY) {
		setScaleX(scaleX);
		setScaleY(scaleY);
		return this;
	}

	/**
	 * @param scale 倍率
	 * @return this
	 */
	public @Nonnull FontPosition setScale(final float scale) {
		setScale(scale, scale);
		return this;
	}

	/**
	 * @param align 文字揃え
	 * @return this
	 */
	public @Nonnull FontPosition setAlign(final @Nonnull Align align) {
		this.align = align;
		return this;
	}

	/**
	 * @return 文字揃え
	 */
	public @Nonnull Align getAlign() {
		return this.align;
	}

	/**
	 * @param usecode カラーコードを使う場合true
	 * @return this
	 */
	public @Nonnull FontPosition setUseCode(final boolean usecode) {
		this.usecode = usecode;
		return this;
	}

	/**
	 * @return カラーコードを使う場合true
	 */
	public boolean isUseCode() {
		return this.usecode;
	}
}
