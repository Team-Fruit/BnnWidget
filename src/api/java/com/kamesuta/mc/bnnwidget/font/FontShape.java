package com.kamesuta.mc.bnnwidget.font;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FontShape {
	public final @Nonnull FontSet font;

	/** Boolean flag on whether AntiAliasing is enabled or not */
	public final boolean antiAlias;

	/** Font's size */
	public final int fontSize;

	/** Default font texture size */
	public final int textureSize;

	protected FontShape(final @Nonnull FontSet font, final boolean antiAlias, final int fontSize) {
		this.font = font;
		this.antiAlias = antiAlias;
		this.fontSize = getFontSize(fontSize);
		this.textureSize = getTextureSize(getFontSize(this.fontSize)*8);
	}

	private static int getTextureSize(final int fsize) {
		int size = 512;
		while (size<fsize)
			size *= 2;
		return size;
	}

	public static int getFontSize(final int fontsize) {
		return fontsize+3;
	}

	protected FontShape(final @Nonnull FontShape shape) {
		this(shape.font, shape.antiAlias, shape.fontSize);
	}

	public @Nonnull LoadedFontShape load() {
		return new LoadedFontShape(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime*result+(this.antiAlias ? 1231 : 1237);
		result = prime*result+this.font.hashCode();
		result = prime*result+this.fontSize;
		result = prime*result+this.textureSize;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this==obj)
			return true;
		if (obj==null)
			return false;
		if (!(obj instanceof FontShape))
			return false;
		final FontShape other = (FontShape) obj;
		if (this.antiAlias!=other.antiAlias)
			return false;
		if (!this.font.equals(other.font))
			return false;
		if (this.fontSize!=other.fontSize)
			return false;
		if (this.textureSize!=other.textureSize)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("FontShape [font=%s, antiAlias=%s, fontSize=%s, textureSize=%s]", this.font, this.antiAlias, this.fontSize, this.textureSize);
	}

	public static boolean isSupported(final @Nullable String fontname) {
		final Font font[] = getFonts();
		for (int i = font.length-1; i>=0; i--)
			if (font[i].getName().equalsIgnoreCase(fontname))
				return true;
		return false;
	}

	public static @Nonnull Font[] getFonts() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
	}
}
