package com.kamesuta.mc.bnnwidget.font;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Maps;

/**
 * フォントスタイル
 *
 * @author TeamFruit
 */
public class FontStyle {
	/** A reference to Java's AWT Font that we create our font texture from */
	public final @Nonnull FontSet font;

	/** Boolean flag on whether AntiAliasing is enabled or not */
	public final boolean antiAlias;

	/**
	 * ロード済みフォント
	 */
	private final @Nonnull Map<Integer, LoadedFontShape> cache = Maps.newHashMap();

	private FontStyle(@Nonnull final FontSet font, final boolean antiAlias) {
		this.font = font;
		this.antiAlias = antiAlias;
	}

	/**
	 * 指定したサイズのフォントを取得します。
	 * @param size サイズ
	 * @return ロード済みフォント
	 */
	public @Nonnull LoadedFontShape getFontShape(final int size) {
		final LoadedFontShape cacheshape = this.cache.get(size);
		if (cacheshape!=null)
			return cacheshape;
		final LoadedFontShape shape = new FontShape(this.font, this.antiAlias, size).load();
		this.cache.put(size, shape);
		return shape;
	}

	/**
	 * フォントスタイルを作成します
	 *
	 * @author TeamFruit
	 */
	public static class Builder {
		/** A reference to Java's AWT Font that we create our font texture from */
		private @Nullable FontSet font;;

		/** Boolean flag on whether AntiAliasing is enabled or not */
		private boolean antiAlias = true;

		public Builder() {
		}

		public Builder(final @Nonnull FontStyle style) {
			this.font = style.font;
			this.antiAlias = style.antiAlias;
		}

		/**
		 * フォント集合を設定
		 * @param font フォント集合
		 * @return this
		 */
		public @Nonnull Builder setFont(final @Nonnull FontSet font) {
			this.font = font;
			return this;
		}

		/**
		 * アンチエイリアスを設定
		 * @param antiAlias アンチエイリアスを使用する場合true
		 * @return this
		 */
		public @Nonnull Builder setAntiAlias(final boolean antiAlias) {
			this.antiAlias = antiAlias;
			return this;
		}

		/**
		 * フォントスタイルを作成します
		 * @return フォントスタイル
		 */
		public @Nonnull FontStyle build() {
			FontSet font = this.font;
			if (font==null)
				font = new FontSet.Builder().build();
			return new FontStyle(font, this.antiAlias);
		}
	}
}
