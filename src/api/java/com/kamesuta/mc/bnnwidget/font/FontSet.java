package com.kamesuta.mc.bnnwidget.font;

import java.awt.Font;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * フォント集合
 * <p>
 * スタイルを保持し、優先度順に複数のフォントを指定することができます
 *
 * @author TeamFruit
 */
public class FontSet {
	/**
	 * フォントに該当の文字が含まれない際の最終的に使われるフォントです
	 */
	public final @Nonnull String defaultFontName;;
	/**
	 * 0番目から順番にフォントの文字を検索します
	 */
	public final @Nonnull ImmutableList<String> fontNames;
	/**
	 * フォントスタイル
	 */
	public final int style;

	FontSet(@Nonnull final String defaultFontName, @Nonnull final ImmutableList<String> fontNames, final int style) {
		this.defaultFontName = defaultFontName;
		this.fontNames = fontNames;
		this.style = style;
	}

	/**
	 * スタイルを変更して新しいインスタンスを作成します
	 * @param style スタイル
	 * @return スタイルを変更したインスタンス
	 */
	public @Nonnull FontSet deriveFontStyle(final int style) {
		return new FontSet(this.defaultFontName, this.fontNames, style);
	}

	/**
	 * サイズを指定して新しいサイズフォントインスタンスを作成します
	 * @param size サイズ
	 * @return サイズを変更したサイズフォントインスタンス
	 */
	public @Nonnull SizedFontSet getSizedFont(final int size) {
		final Font defaultFont = new Font(this.defaultFontName, this.style, size);
		final ImmutableList.Builder<Font> builder = ImmutableList.builder();
		for (final String fontName : this.fontNames)
			if (FontShape.isSupported(fontName))
				builder.add(new Font(fontName, this.style, size));
		return new SizedFontSet(defaultFont, builder.build(), this.style, size);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime*result+this.defaultFontName.hashCode();
		result = prime*result+this.fontNames.hashCode();
		result = prime*result+this.style;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this==obj)
			return true;
		if (obj==null)
			return false;
		if (!(obj instanceof FontSet))
			return false;
		final FontSet other = (FontSet) obj;
		if (!this.defaultFontName.equals(other.defaultFontName))
			return false;
		if (!this.fontNames.equals(other.fontNames))
			return false;
		if (this.style!=other.style)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("FontSet [defaultFontName=%s, fontNames=%s, style=%s]", this.defaultFontName, this.fontNames, this.style);
	}

	/**
	 * サイズが指定されたフォント集合
	 *
	 * @author TeamFruit
	 */
	public static class SizedFontSet {
		/**
		 * フォントに該当の文字が含まれない際の最終的に使われるフォントです
		 */
		public final @Nonnull Font defaultFont;
		/**
		 * 0番目から順番にフォントの文字を検索します
		 */
		public final @Nonnull ImmutableList<Font> fonts;
		/**
		 * フォントスタイル
		 */
		public final int style;
		/**
		 * フォントサイズ
		 */
		public final int size;

		SizedFontSet(@Nonnull final Font defaultFont, @Nonnull final ImmutableList<Font> fonts, final int style, final int size) {
			this.defaultFont = defaultFont;
			this.fonts = fonts;
			this.style = style;
			this.size = size;
		}

		/**
		 * 指定した文字を含むフォントを検索します
		 * @param ch 文字
		 * @return フォント
		 */
		public @Nonnull Font getCharFont(final char ch) {
			for (final Font font : this.fonts)
				if (font.canDisplay(ch))
					return font;
			return this.defaultFont;
		}

		/**
		 * スタイルを変更して新しいインスタンスを作成します
		 * @param style スタイル
		 * @return スタイルを変更したインスタンス
		 */
		public @Nonnull SizedFontSet deriveFontStyle(final int style) {
			final ImmutableList.Builder<Font> builder = ImmutableList.builder();
			for (final Font font : this.fonts)
				builder.add(font.deriveFont(style));
			return new SizedFontSet(this.defaultFont.deriveFont(style), builder.build(), style, this.size);
		}

		/**
		 * サイズを変更して新しいインスタンスを作成します
		 * @param style サイズ
		 * @return サイズを変更したインスタンス
		 */
		public @Nonnull SizedFontSet deriveFontSize(final int size) {
			final ImmutableList.Builder<Font> builder = ImmutableList.builder();
			for (final Font font : this.fonts)
				builder.add(font.deriveFont((float) size));
			return new SizedFontSet(this.defaultFont.deriveFont((float) size), builder.build(), this.style, size);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime*result+this.defaultFont.hashCode();
			result = prime*result+this.fonts.hashCode();
			result = prime*result+this.size;
			result = prime*result+this.style;
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this==obj)
				return true;
			if (obj==null)
				return false;
			if (!(obj instanceof SizedFontSet))
				return false;
			final SizedFontSet other = (SizedFontSet) obj;
			if (!this.defaultFont.equals(other.defaultFont))
				return false;
			if (!this.fonts.equals(other.fonts))
				return false;
			if (this.size!=other.size)
				return false;
			if (this.style!=other.style)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return String.format("SizedFontSet [defaultFont=%s, fonts=%s, style=%s, size=%s]", this.defaultFont, this.fonts, this.style, this.size);
		}
	}

	/**
	 * フォント集合を作成します
	 *
	 * @author TeamFruit
	 */
	public static class Builder {
		/**
		 * 2番目のデフォルトのフォントです
		 */
		public static final @Nonnull String defaultFontName = "YuGothic";

		/**
		 * 0番目から順番にフォントの文字を検索します
		 */
		private final @Nonnull List<String> fontnames = Lists.newArrayList(defaultFontName);
		/**
		 * フォントスタイル
		 */
		private int style = Font.PLAIN;

		public Builder() {
		}

		public Builder(final @Nonnull FontSet fontSet) {
		}

		/**
		 * フォントの候補を追加します。後から追加したものが優先されます
		 * @param name フォント名
		 * @return this
		 */
		public @Nonnull Builder addName(@Nonnull final String name) {
			this.fontnames.add(name);
			return this;
		}

		/**
		 * フォントスタイルを設定します
		 * @param style フォントスタイル
		 * @return this
		 */
		public @Nonnull Builder setStyle(final int style) {
			this.style = style;
			return this;
		}

		/**
		 * フォント集合を作成します
		 * @return フォント集合
		 */
		public @Nonnull FontSet build() {
			final ImmutableList.Builder<String> builder = ImmutableList.builder();
			for (final ListIterator<String> itr = this.fontnames.listIterator(this.fontnames.size()); itr.hasPrevious();) {
				final String fontname = itr.previous();
				if (FontShape.isSupported(fontname))
					builder.add(fontname);
			}
			return new FontSet(Font.DIALOG, builder.build(), this.style);
		}
	}
}
