package com.kamesuta.mc.bnnwidget.font;

import javax.annotation.Nonnull;

import com.kamesuta.mc.bnnwidget.font.LoadedFontShape.FontManager;

/**
 * フォント描画をします。
 * @see FontManager#getFont(FontShape)
 * @author TeamFruit
 */
public interface WFont {
	/**
	 * フォントを描画します。
	 * @param p 文字描画設定
	 * @return 幅
	 */
	float drawString(@Nonnull FontPosition p);

	/**
	 * 文字列の幅を取得します。
	 * @param p 文字描画設定
	 * @return 幅
	 */
	float getWidth(final @Nonnull FontPosition p);

	/**
	 * 文字の幅を取得します。
	 * @param whatchars 描画文字
	 * @return 幅
	 */
	float getCharWidth(final char ch, int fontsize, final float scaleX);

	/**
	 * フォントのスタイルを取得します。
	 * @return フォントのスタイル
	 */
	@Nonnull
	FontStyle getStyle();
}
