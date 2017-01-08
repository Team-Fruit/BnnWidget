package com.kamesuta.mc.bnnwidget.font;

import java.awt.Font;
import java.util.Iterator;
import java.util.Random;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Iterators;
import com.kamesuta.mc.bnnwidget.font.LoadedFontShape.FontAtlas;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WGui;
import com.kamesuta.mc.bnnwidget.render.WGui.Align;
import com.kamesuta.mc.bnnwidget.render.WRenderer;

/**
 * {@link WFont}のTrueTypeフォント実装です
 *
 * @author TeamFruit
 */
public class TrueTypeFont implements WFont {
	private static @Nonnull Random fontRandom = new Random();

	private final @Nonnull FontStyle style;
	private final @Nonnull FontStyle styleitalic;

	private static final @Nonnull int[] colorCode = new int[32];

	static {
		for (int i = 0; i<32; ++i) {
			final int j = (i>>3&1)*85;
			int k = (i>>2&1)*170+j;
			int l = (i>>1&1)*170+j;
			int i1 = (i>>0&1)*170+j;

			if (i==6)
				k += 85;

			if (i>=16) {
				k /= 4;
				l /= 4;
				i1 /= 4;
			}

			colorCode[i] = (k&255)<<16|(l&255)<<8|i1&255;
		}
	}

	public TrueTypeFont(final @Nonnull FontStyle style) {
		this.style = style;

		final FontSet src = style.font;
		if ((src.style&Font.ITALIC)!=0)
			this.styleitalic = style;
		else
			this.styleitalic = new FontStyle.Builder(style).setFont(src.deriveFontStyle(src.style|Font.ITALIC)).build();
	}

	@Override
	public float drawString(final FontPosition p) {
		final int color = OpenGL.glGetColorRGBA();

		final int startIndex = p.getStartIndex();
		final int endIndex = p.getEndIndex();
		final String whatchars = p.getText();
		final boolean colorcode = p.isUseCode();
		final float x = p.getX();
		final float y = p.getY();
		final float scaleX = p.getScaleX();
		final float scaleY = p.getScaleY();
		final int fontsize = p.getFontSize();
		final Align align = p.getAlign();

		char charCurrent;

		float totalwidth = 0;
		final String[] line = StringUtils.split(whatchars, '\n');
		final Iterator<String> lineitr = Iterators.forArray(line);
		float guesswidth = lineitr.hasNext() ? getWidth(new FontPosition(p).setText(lineitr.next())) : 0;
		int i = startIndex;
		float startY = 0;

		boolean randomStyle = false;
		boolean boldStyle = false;
		boolean strikethroughStyle = false;
		boolean underlineStyle = false;
		boolean italicStyle = false;
		while (i>=startIndex&&i<=endIndex) {
			charCurrent = whatchars.charAt(i);

			if (charCurrent=='\n') {
				startY += this.style.getFontShape(fontsize).getFontShape().fontSize;
				totalwidth = 0;
				guesswidth = lineitr.hasNext() ? getWidth(new FontPosition(p).setText(lineitr.next())) : 0;
			} else {
				boolean draw = true;
				if (colorcode&&charCurrent==167&&i+1<whatchars.length()) {
					int j = "0123456789abcdefklmnor".indexOf(whatchars.toLowerCase().charAt(i+1));

					draw = false;
					if (0<=j&&j<16) {
						randomStyle = false;
						boldStyle = false;
						strikethroughStyle = false;
						underlineStyle = false;
						italicStyle = false;

						if (j<0||j>15)
							j = 15;

						final int k = TrueTypeFont.colorCode[j];
						OpenGL.glColorRGB(k);
						;
					} else if (j==16)
						randomStyle = true;
					else if (j==17)
						boldStyle = true;
					else if (j==18)
						strikethroughStyle = true;
					else if (j==19)
						underlineStyle = true;
					else if (j==20)
						italicStyle = true;
					else if (j==21) {
						randomStyle = false;
						boldStyle = false;
						strikethroughStyle = false;
						underlineStyle = false;
						italicStyle = false;
						OpenGL.glColorRGB(color);
					} else
						draw = true;

					if (!draw)
						i++;
				}

				if (draw) {
					final int srcfontwidth = getCharWidth(charCurrent, fontsize);

					final String str = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000";
					int j = str.indexOf(charCurrent);

					final String random = " !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
					if (randomStyle&&j!=-1) {
						j = fontRandom.nextInt(random.length());
						charCurrent = random.charAt(j);
					}

					float correctX;
					switch (align) {
						default:
						case LEFT:
							correctX = 0;
							break;
						case CENTER:
							correctX = guesswidth/2f;
							break;
						case RIGHT:
							correctX = guesswidth;
							break;
					}

					drawChar(charCurrent, totalwidth, startY, scaleX, scaleY, x-correctX, y, fontsize, italicStyle);

					if (boldStyle) {
						drawChar(charCurrent, totalwidth, startY, scaleX, scaleY, x-correctX+1f*scaleX, y, fontsize, italicStyle);
						totalwidth += 1f*scaleX;
					}

					doDraw(charCurrent, totalwidth/2f, startY, scaleX, scaleY, x-correctX, y, srcfontwidth-1f, fontsize, strikethroughStyle, underlineStyle);

					totalwidth += srcfontwidth;
				}
			}

			i++;
		}
		//		WRenderer.startShape();
		//		WGui.drawSize(x, y, totalwidth*scaleX, this.style.getFontShape(fontsize).getFontShape().fontSize*scaleY, GL11.GL_LINE_LOOP);
		//		WRenderer.startTexture();
		OpenGL.glColorRGBA(color);
		return totalwidth*scaleX;
	}

	protected void doDraw(final char ch, final float x, final float y, final float scaleX, final float scaleY, final float offsetX, final float offsetY, final float width, final int fontsize, final boolean strikethroughStyle, final boolean underlineStyle) {
		final FontAtlas intObject = this.style.getFontShape(fontsize).getCharacter().getFont(ch);
		final int height = intObject.size.height;

		if (strikethroughStyle) {
			OpenGL.glDisable(GL11.GL_TEXTURE_2D);
			WGui.drawSize(x*scaleX+offsetX, (y+height/2)*scaleY+offsetY, width*scaleX, 1f*scaleY);
			OpenGL.glEnable(GL11.GL_TEXTURE_2D);
		}

		if (underlineStyle) {
			OpenGL.glDisable(GL11.GL_TEXTURE_2D);
			WGui.drawSize((x-1)*scaleX+offsetX, (y+height-3f-1f)*scaleY+offsetY, (width+1)*scaleX, 1f*scaleY);
			OpenGL.glEnable(GL11.GL_TEXTURE_2D);
		}
	}

	public void drawChar(final char ch, final float x, final float y, final float scaleX, final float scaleY, final float offsetX, final float offsetY, final int fontsize, final boolean italicStyle) {
		final FontAtlas intObject = (italicStyle ? this.styleitalic : this.style).getFontShape(fontsize).getCharacter().getFont(ch);
		WRenderer.startTexture();
		OpenGL.glBindTexture(GL11.GL_TEXTURE_2D, intObject.location.image.textureID);
		final float size = this.style.getFontShape(fontsize).getFontShape().textureSize;
		WGui.drawTextureSize(x*scaleX+offsetX, y*scaleY+offsetY, intObject.size.width*scaleX, intObject.size.height*scaleY,
				intObject.location.storedX/size, intObject.location.storedY/size, intObject.size.width/size, intObject.size.height/size);
	}

	@Override
	public float getWidth(final @Nonnull FontPosition p) {
		final int startIndex = p.getStartIndex();
		final int endIndex = p.getEndIndex();
		final String whatchars = p.getText();
		final boolean colorcode = p.isUseCode();
		final float scaleX = p.getScaleX();
		final int fontsize = p.getFontSize();

		char charCurrent;

		float totalwidth = 0;
		int i = startIndex;
		boolean boldStyle = false;
		while (i>=startIndex&&i<=endIndex) {
			charCurrent = whatchars.charAt(i);

			if (charCurrent=='\n')
				totalwidth = 0;
			else
				check: {
					if (colorcode&&charCurrent==167&&i+1<whatchars.length()) {
						int j;
						if ("0123456789abcdefklmnor".indexOf(j = whatchars.toLowerCase().charAt(i+1))!=-1) {
							if (0<=j&&j<16)
								boldStyle = false;
							else if (j==17)
								boldStyle = true;
							else if (j==21)
								boldStyle = false;
							i += 2;
							break check;
						}
					}
				}

			final int srcfontwidth = getCharWidth(charCurrent, fontsize);

			if (boldStyle)
				totalwidth += 1f;

			totalwidth += srcfontwidth;

			i++;
		}
		return totalwidth*scaleX;
	}

	@Override
	public float getCharWidth(final char ch, final int fontsize, final float scaleX) {
		return getCharWidth(ch, fontsize)*scaleX;
	}

	public int getCharWidth(final char ch, final int fontsize) {
		return this.style.getFontShape(fontsize).getCharacter().getFont(ch).size.width;
	}

	@Override
	public @Nonnull FontStyle getStyle() {
		return this.style;
	}
}