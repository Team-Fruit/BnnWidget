package com.kamesuta.mc.bnnwidget.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kamesuta.mc.bnnwidget.font.FontSet.SizedFontSet;
import com.kamesuta.mc.bnnwidget.font.LoadedFontShape.FontAtlas.TextureSlot;
import com.kamesuta.mc.bnnwidget.font.LoadedFontShape.FontAtlas.TextureSlot.FontImage;

import net.minecraft.client.renderer.texture.TextureUtil;

public class LoadedFontShape {
	private final @Nonnull FontShape shape;

	LoadedFontShape(@Nonnull final FontShape shape) {
		this.shape = shape;
	}

	private @Nonnull FontContent getContent() {
		return FontManager.getFont(this.shape);
	}

	public @Nonnull FontShape getFontShape() {
		return this.shape;
	}

	public @Nonnull TextureManager getTexture() {
		return getContent().texture;
	}

	public @Nonnull CharManager getCharacter() {
		return getContent().character;
	}

	public @Nonnull SizedFontSet getSizedFont() {
		return getContent().sizedFont;
	}

	static class FontContent {
		public final @Nonnull TextureManager texture;
		public final @Nonnull CharManager character;
		public final @Nonnull SizedFontSet sizedFont;
		protected long lastaccess = -1;

		public FontContent(@Nonnull final TextureManager texture, @Nonnull final CharManager character, @Nonnull final SizedFontSet sizedFont) {
			this.texture = texture;
			this.character = character;
			this.sizedFont = sizedFont;
		}

		public FontContent(@Nonnull final FontShape shape) {
			this.texture = new TextureManager(shape);
			this.sizedFont = shape.font.getSizedFont(shape.fontSize);
			this.character = new CharManager(shape, this.sizedFont, this.texture);
		}

		public void dispose() {
			this.texture.dispose();
		}
	}

	/**
	 * 描画のためのフォントインスタンスを管理します
	 *
	 * @author TeamFruit
	 */
	static class FontManager {
		private static @Nonnull Map<FontShape, FontContent> fonts = Maps.newHashMap();
		private static final long collectTime = TimeUnit.SECONDS.toMillis(1);
		private static long lastaccess = -1;

		/**
		 * フォント描画のためのインスタンスを返します。
		 * <p>
		 * 以前に内容が同じ設定({@link FontShape})で取得している場合は、同じインスタンスが再利用されます。
		 * @param shape フォント設定
		 * @return フォントインスタンス
		 */
		public static @Nonnull FontContent getFont(@Nonnull final FontShape shape) {
			final long time = System.currentTimeMillis();
			FontContent font = fonts.get(shape);
			if (font!=null)
				return font;
			font = new FontContent(shape);
			font.lastaccess = time;
			fonts.put(shape, font);

			if (lastaccess>=0&&time-lastaccess>collectTime)
				gc(time);
			lastaccess = time;
			return font;
		}

		private static void gc(final long time) {
			for (final Iterator<Entry<FontShape, FontContent>> itr = fonts.entrySet().iterator(); itr.hasNext();) {
				final Entry<FontShape, FontContent> entry = itr.next();
				final FontContent content = entry.getValue();
				if (content.lastaccess>=0&&time-content.lastaccess>collectTime) {
					content.dispose();
					itr.remove();
				}
			}
		}
	}

	static class CharManager {
		private static final @Nonnull Graphics2D g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();

		private final @Nonnull FontShape shape;
		private final @Nonnull SizedFontSet sizedFont;
		private final @Nonnull TextureManager texture;

		public CharManager(@Nonnull final FontShape shape, final @Nonnull SizedFontSet sizedFont, @Nonnull final TextureManager texture) {
			this.shape = shape;
			this.sizedFont = sizedFont;
			this.texture = texture;
		}

		/** Map of user defined font characters (Character <-> IntObject) */
		private @Nonnull Map<Character, FontAtlas> customChars = Maps.newHashMap();

		private @Nonnull BufferedImage getFontImage(final char ch) {
			final Font font = this.sizedFont.getCharFont(ch);
			// Create a temporary image to extract the character's size
			g.setFont(font);
			final FontMetrics fontMetrics = g.getFontMetrics();
			int charwidth = fontMetrics.charWidth(ch);

			if (charwidth<=0)
				charwidth = 7;
			int charheight = fontMetrics.getHeight();
			if (charheight<=0)
				charheight = this.shape.fontSize;

			// Create another image holding the character we are creating
			final BufferedImage fontImage = new BufferedImage(charwidth, charheight,
					BufferedImage.TYPE_INT_ARGB);
			final Graphics2D gt = (Graphics2D) fontImage.getGraphics();
			if (this.shape.antiAlias)
				gt.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			gt.setFont(font);

			gt.setColor(Color.WHITE);
			gt.drawString(String.valueOf(ch), 0, fontMetrics.getAscent());
			gt.dispose();

			return fontImage;
		}

		public @Nonnull FontAtlas getFont(final char ch) {
			final FontAtlas stored = this.customChars.get(ch);
			if (stored!=null)
				return stored;

			final BufferedImage fontImage = getFontImage(ch);
			final FontAtlas storage = this.texture.uploadFont(new TextureSize(fontImage.getWidth(), fontImage.getHeight()), fontImage);
			this.customChars.put(ch, storage);
			return storage;
		}
	}

	static class FontAtlas {
		public final @Nonnull TextureSize size;

		public final @Nonnull TextureSlot location;

		public FontAtlas(final @Nonnull TextureSize size, final @Nonnull TextureSlot location) {
			this.size = size;
			this.location = location;
		}

		public static class TextureSlot {
			public final @Nonnull FontImage image;

			/** Character's stored x position */
			public final int storedX;

			/** Character's stored y position */
			public final int storedY;

			public TextureSlot(final @Nonnull FontImage image, final int storedX, final int storedY) {
				this.image = image;
				this.storedX = storedX;
				this.storedY = storedY;
			}

			public static class FontImage {
				/** Character's texture id */
				public final int textureID;

				protected FontImage(final @Nonnull FontShape base, final int textureID) {
					this.textureID = textureID;
					TextureUtil.allocateTexture(textureID, base.textureSize, base.textureSize);
				}

				public FontImage(final @Nonnull FontShape base) {
					this(base, GL11.glGenTextures());
				}

				public void write(final @Nonnull BufferedImage image, final int storedX, final int storedY) {
					TextureUtil.uploadTextureImageSub(this.textureID, image, storedX, storedY, false, false);
				}

				public void dispose() {
					TextureUtil.deleteTexture(this.textureID);
				}
			}
		}
	}

	static class TextureSize {
		/** Character's width */
		public final int width;

		/** Character's height */
		public final int height;

		public TextureSize(final int width, final int height) {
			this.width = width;
			this.height = height;
		}
	}

	static class TextureManager {
		private final @Nonnull FontShape base;
		private @Nonnull Set<FontImage> images;
		private @Nonnull FontImage image;
		private int rowHeight = 0;
		private int positionX = 0;
		private int positionY = 0;

		public TextureManager(final @Nonnull FontShape base) {
			this.base = base;
			this.images = Sets.newHashSet();
			this.image = newPage();
		}

		private @Nonnull FontImage newPage() {
			final FontImage img = new FontImage(this.base);
			this.images.add(img);
			return img;
		}

		private @Nonnull TextureSlot alloc(final @Nonnull TextureSize size) {
			final int width = size.width;
			final int height = size.height;

			if (this.positionX+width>=this.base.textureSize) {
				this.positionX = 0;
				this.positionY += this.rowHeight;
				this.rowHeight = 0;
			}

			if (height>this.rowHeight)
				this.rowHeight = height;

			if (this.positionY+this.rowHeight>this.base.textureSize) {
				this.image = newPage();
				this.positionX = 0;
				this.positionY = 0;
			}

			final TextureSlot slot = new TextureSlot(this.image, this.positionX, this.positionY);

			this.positionX += width;

			return slot;
		}

		public @Nonnull FontAtlas uploadFont(final @Nonnull TextureSize size, final @Nonnull BufferedImage fontImage) {
			final TextureSlot loc = alloc(size);

			// Draw it here
			loc.image.write(fontImage, loc.storedX, loc.storedY);

			return new FontAtlas(size, loc);
		}

		public void dispose() {
			for (final FontImage image : this.images)
				if (image!=null)
					image.dispose();
			this.image.dispose();
		}
	}
}
