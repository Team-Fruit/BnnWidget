package com.kamesuta.mc.bnnwidget.font;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WGui;
import com.kamesuta.mc.bnnwidget.render.WGui.Align;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

/**
 * {@link WFont}のMinecraftFontRendererラッパーです
 * <p>
 * 不完全な部分があるので、加筆、修正をお待ちしております
 *
 * @author TeamFruit
 */
@SideOnly(Side.CLIENT)
public class MinecraftFontRenderer extends FontRenderer {
	private static final float scale = .5f;

	private final WFont font;

	public MinecraftFontRenderer(final WFont font, final int fontheight) {
		super(WGui.mc.gameSettings, null, null, true);
		this.font = font;
		this.FONT_HEIGHT = fontheight;
	}

	@Override
	protected void bindTexture(final @Nullable ResourceLocation location) {
	}

	@Override
	public void onResourceManagerReload(@Nullable final IResourceManager resourceManager) {
	}

	@Override
	public int drawStringWithShadow(@Nullable final String str, final int x, final int y, final int color) {
		return this.drawString(str, x, y, color, true);
	}

	@Override
	public int drawString(@Nullable final String str, final int x, final int y, final int color) {
		return this.drawString(str, x, y, color, false);
	}

	private FontPosition pdraw = new FontPosition().setFontSize(FontShape.getFontSize(this.FONT_HEIGHT));

	@Override
	public int drawString(@Nullable final String str, final int x, final int y, final int color, final boolean shadow) {
		if (str!=null) {
			OpenGL.glColorRGB(color);
			this.font.drawString(this.pdraw.setPosition(x, y).setText(str).setScale(scale*10f/10f, scale*10f/10f));
		}
		return getStringWidth(str);
	}

	@Override
	public int getStringWidth(@Nullable final String str) {
		if (str==null)
			return 0;
		return (int) this.font.getWidth(this.pdraw.setText(str).setAlign(Align.LEFT).setScale(scale, scale));
	}

	@Override
	public int getCharWidth(final char c) {
		return (int) this.font.getCharWidth(c, -1, scale);
	}

	@Override
	public String trimStringToWidth(final String str, final int width) {
		return this.trimStringToWidth(str, width, false);
	}

	@Override
	public String trimStringToWidth(final String str, final int width, final boolean trimRight) {
		final StringBuilder stringbuilder = new StringBuilder();
		float j = 0;
		final int k = trimRight ? str.length()-1 : 0;
		final int l = trimRight ? -1 : 1;
		boolean flag1 = false;
		boolean flag2 = false;

		for (int i1 = k; i1>=0&&i1<str.length()&&j<width; i1 += l) {
			final char c0 = str.charAt(i1);
			final float j1 = this.font.getCharWidth(c0, -1, scale/2f);

			if (flag1) {
				flag1 = false;

				if (c0!=108&&c0!=76) {
					if (c0==114||c0==82)
						flag2 = false;
				} else
					flag2 = true;
			} else if (j1<0)
				flag1 = true;
			else {
				j += j1;

				if (flag2)
					++j;
			}

			if (j>width)
				break;

			if (trimRight)
				stringbuilder.insert(0, c0);
			else
				stringbuilder.append(c0);
		}

		return stringbuilder.toString();
	}

	@Override
	public void drawSplitString(String str, final int x, final int y, final int color, final int p_78279_5_) {
		str = trimStringNewline(str);
		renderSplitString(str, x, y, color, false);
	}

	private String trimStringNewline(String str) {
		while (str!=null&&str.endsWith("\n"))
			str = str.substring(0, str.length()-1);

		return str;
	}

	private void renderSplitString(final String str, final int x, int y, final int color, final boolean shadow) {
		final List<String> list = listFormattedStringToWidth(str, color);

		for (final Iterator<String> iterator = list.iterator(); iterator.hasNext(); y += this.FONT_HEIGHT) {
			final String s1 = iterator.next();
			this.drawString(s1, x, y, color, shadow);
		}
	}

	@Override
	public int splitStringWidth(final String p_78267_1_, final int p_78267_2_) {
		return this.FONT_HEIGHT*listFormattedStringToWidth(p_78267_1_, p_78267_2_).size();
	}

	private String wrapFormattedStringToWidth0(final String p_78280_1_, final int p_78280_2_) {
		final int j = sizeStringToWidth(p_78280_1_, p_78280_2_);

		if (p_78280_1_.length()<=j)
			return p_78280_1_;
		else {
			final String s1 = p_78280_1_.substring(0, j);
			final char c0 = p_78280_1_.charAt(j);
			final boolean flag = c0==32||c0==10;
			final String s2 = getFormatFromString(s1)+p_78280_1_.substring(j+(flag ? 1 : 0));
			return s1+"\n"+wrapFormattedStringToWidth0(s2, p_78280_2_);
		}
	}

	private static boolean isFormatColor(final char p_78272_0_) {
		return p_78272_0_>=48&&p_78272_0_<=57||p_78272_0_>=97&&p_78272_0_<=102||p_78272_0_>=65&&p_78272_0_<=70;
	}

	private static boolean isFormatSpecial(final char p_78270_0_) {
		return p_78270_0_>=107&&p_78270_0_<=111||p_78270_0_>=75&&p_78270_0_<=79||p_78270_0_==114||p_78270_0_==82;
	}

	private int sizeStringToWidth(final String p_78259_1_, final int p_78259_2_) {
		final int j = p_78259_1_.length();
		int k = 0;
		int l = 0;
		int i1 = -1;

		for (boolean flag = false; l<j; ++l) {
			final char c0 = p_78259_1_.charAt(l);

			switch (c0) {
				case 10:
					--l;
					break;
				case 167:
					if (l<j-1) {
						++l;
						final char c1 = p_78259_1_.charAt(l);

						if (c1!=108&&c1!=76) {
							if (c1==114||c1==82||isFormatColor(c1))
								flag = false;
						} else
							flag = true;
					}

					break;
				case 32:
					i1 = l;
				default:
					k += getCharWidth(c0);

					if (flag)
						++k;
			}

			if (c0==10) {
				++l;
				i1 = l;
				break;
			}

			if (k>p_78259_2_)
				break;
		}

		return l!=j&&i1!=-1&&i1<l ? i1 : l;
	}

	@Override
	public void setUnicodeFlag(final boolean isUnicode) {
	}

	@Override
	public boolean getUnicodeFlag() {
		return true;
	}

	@Override
	public void setBidiFlag(final boolean isBidi) {
	}

	@Override
	public List<String> listFormattedStringToWidth(final String p_78271_1_, final int p_78271_2_) {
		return Arrays.asList(wrapFormattedStringToWidth0(p_78271_1_, p_78271_2_).split("\n"));
	}

	private static String getFormatFromString(final String p_78282_0_) {
		String s1 = "";
		int i = -1;
		final int j = p_78282_0_.length();

		while ((i = p_78282_0_.indexOf(167, i+1))!=-1)
			if (i<j-1) {
				final char c0 = p_78282_0_.charAt(i+1);

				if (isFormatColor(c0))
					s1 = "\u00a7"+c0;
				else if (isFormatSpecial(c0))
					s1 = s1+"\u00a7"+c0;
			}

		return s1;
	}

	@Override
	public boolean getBidiFlag() {
		return false;
	}
}
