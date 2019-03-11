package net.teamfruit.bnnwidget.compat;

import java.io.IOException;
import java.nio.IntBuffer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class Compat {
	public static class CompatSound {
		private final ISound sound;

		public CompatSound(final ISound sound) {
			this.sound = sound;
		}

		public static CompatSound createClickSound() {
			return new CompatSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
		}

		public void play() {
			getMinecraft().getSoundHandler().playSound(this.sound);
		}
	}

	public static class CompatGuiTextField extends GuiTextField {
		public CompatGuiTextField() {
			super(1, CompatMinecraft.getMinecraft().getFontRenderer().getFontRendererObj(), 0, 0, 0, 0);
		}

		public int getNthWordFromPosWSCompat(final int n, final int pos, final boolean skipWs) {
			return func_146197_a(n, pos, skipWs);
		}

		public int getX() {
			return this.xPosition;
		}

		public void setX(final int value) {
			this.xPosition = value;
		}

		public int getY() {
			return this.yPosition;
		}

		public void setY(final int value) {
			this.yPosition = value;
		}

		public void tickCompat() {
			updateCursorCounter();
		}

		public void charTypedCompat(final char c, final int keycode) {
			textboxKeyTyped(c, keycode);
		}

		@Override
		public void drawTextBox() {
			drawTextBoxCompat();
		}

		public void drawTextBoxCompat() {
		}
	}

	public static abstract class CompatFontRendererBase extends FontRenderer {
		public CompatFontRendererBase(final GameSettings gameSettingsIn, final ResourceLocation location, final TextureManager textureManagerIn, final boolean unicode) {
			super(gameSettingsIn, location, textureManagerIn, unicode);
		}

		@Override
		public int drawStringWithShadow(@Nullable final String str, final float x, final float y, final int color) {
			return drawStringWithShadowCompat(str, x, y, color);
		}

		protected abstract int drawStringWithShadowCompat(@Nullable final String str, final float x, final float y, final int color);

		@Override
		public int drawString(@Nullable final String str, final float x, final float y, final int color, final boolean shadow) {
			return drawStringCompat(str, x, y, color, shadow);
		}

		protected abstract int drawStringCompat(@Nullable final String str, final float x, final float y, final int color, final boolean shadow);

		@Override
		public int splitStringWidth(final String str, final int maxLength) {
			return getWordWrappedHeightCompat(str, maxLength);
		}

		public abstract int getWordWrappedHeightCompat(final String str, final int maxLength);
	}

	public static @Nonnull Minecraft getMinecraft() {
		return Minecraft.getMinecraft();
	}

	public static class CompatMinecraft {
		private final Minecraft minecraft;

		public CompatMinecraft(final Minecraft minecraft) {
			this.minecraft = minecraft;
		}

		public static @Nonnull CompatMinecraft getMinecraft() {
			return new CompatMinecraft(Compat.getMinecraft());
		}

		public @Nonnull TextureManager getTextureManager() {
			return this.minecraft.getTextureManager();
		}

		public @Nonnull CompatFontRenderer getFontRenderer() {
			return new CompatFontRenderer(this.minecraft.fontRendererObj);
		}

		public int getDisplayWidth() {
			return this.minecraft.displayWidth;
		}

		public int getDisplayHeight() {
			return this.minecraft.displayHeight;
		}
	}

	public static class CompatFontRenderer {
		private final FontRenderer font;

		public CompatFontRenderer(final FontRenderer font) {
			this.font = font;
		}

		public int drawString(final String msg, final float x, final float y, final int color, final boolean shadow) {
			return this.font.drawString(msg, x, y, color, shadow);
		}

		public int drawString(final String msg, final float x, final float y, final int color) {
			return drawString(msg, x, y, color, false);
		}

		public int drawStringWithShadow(final String msg, final float x, final float y, final int color) {
			return drawString(msg, x, y, color, true);
		}

		public int getStringWidth(final @Nullable String s) {
			return this.font.getStringWidth(s);
		}

		public int getStringWidthWithoutFormattingCodes(final @Nullable String s) {
			return getStringWidth(EnumChatFormatting.getTextWithoutFormattingCodes(s));
		}

		public FontRenderer getFontRendererObj() {
			return this.font;
		}
	}

	private static class WVertexImpl implements WVertex {
		public static final @Nonnull Tessellator t = Tessellator.getInstance();
		public static final @Nonnull WorldRenderer w = t.getWorldRenderer();

		public WVertexImpl() {
		}

		@Override
		public void draw() {
			endVertex();
			t.draw();
		}

		@Override
		public @Nonnull WVertex begin(final int mode) {
			w.begin(mode, DefaultVertexFormats.POSITION);
			init();
			return this;
		}

		@Override
		public @Nonnull WVertex beginTexture(final int mode) {
			w.begin(mode, DefaultVertexFormats.POSITION_TEX);
			init();
			return this;
		}

		private void init() {
			this.stack = false;
		}

		private boolean stack;

		@Override
		public @Nonnull WVertex pos(final double x, final double y, final double z) {
			endVertex();
			w.pos(x, y, z);
			this.stack = true;
			return this;
		}

		@Override
		public @Nonnull WVertex tex(final double u, final double v) {
			w.tex(u, v);
			return this;
		}

		@Override
		public @Nonnull WVertex color(final float red, final float green, final float blue, final float alpha) {
			return this.color((int) (red*255.0F), (int) (green*255.0F), (int) (blue*255.0F), (int) (alpha*255.0F));
		}

		@Override
		public @Nonnull WVertex color(final int red, final int green, final int blue, final int alpha) {
			w.putColorRGBA(0, red, green, blue, alpha);
			return this;
		}

		@Override
		public @Nonnull WVertex normal(final float nx, final float ny, final float nz) {
			w.normal(nx, ny, nz);
			return this;
		}

		@Override
		public void setTranslation(final double x, final double y, final double z) {
			w.setTranslation(x, y, z);
		}

		private void endVertex() {
			if (this.stack) {
				this.stack = false;
				w.endVertex();
			}
		}
	}

	public static @Nonnull WVertex getWVertex() {
		return new WVertexImpl();
	}

	public static class CompatTime {
		public static long getTimerResolution() {
			return Sys.getTimerResolution();
		}

		public static long getTime() {
			return Sys.getTime();
		}
	}

	public static class CompatCursor {
		private static final @Nullable org.lwjgl.input.Cursor cur;

		static {
			org.lwjgl.input.Cursor cursor = null;
			try {
				final IntBuffer buf = GLAllocation.createDirectIntBuffer(1);
				buf.put(0);
				buf.flip();
				cursor = new org.lwjgl.input.Cursor(1, 1, 0, 0, 1, buf, null);
			} catch (final LWJGLException e) {
			}
			cur = cursor;
		}

		public static void setCursorVisible(final boolean b) {
			if (cur!=null)
				try {
					Mouse.setNativeCursor(b ? null : cur);
				} catch (final LWJGLException e) {
				}
		}
	}

	public static class CompatGuiScreen extends GuiScreen {
		@Override
		public void drawScreen(final int mousex, final int mousey, final float f) {
			drawScreenCompat(mousex, mousey, f);
		}

		public void drawScreenCompat(final int mousex, final int mousey, final float f) {
			super.drawScreen(mousex, mousey, f);
		}

		public static void drawScreen(final GuiScreen screen, final int mousex, final int mousey, final float f) {
			screen.drawScreen(mousex, mousey, f);
		}

		@Override
		public void mouseClicked(final int x, final int y, final int button) {
			mouseClickedCompat(x, y, button);
		}

		public boolean mouseClickedCompat(final double x, final double y, final int button) {
			try {
				super.mouseClicked((int) x, (int) y, button);
				if (!"".isEmpty())
					throw new IOException();
			} catch (final IOException e) {
			}
			return true;
		}

		@Override
		public void mouseClickMove(final int x, final int y, final int button, final long time) {
			mouseClickMoveCompat(x, y, button, time, 0, 0);
		}

		public boolean mouseClickMoveCompat(final double x, final double y, final int button, final long time, final double dx, final double dy) {
			super.mouseClickMove((int) x, (int) y, button, time);
			return true;
		}

		@Override
		public void updateScreen() {
			updateScreenCompat();
		}

		public void updateScreenCompat() {
			super.updateScreen();
		}

		public static void updateScreen(final GuiScreen screen) {
			screen.updateScreen();
		}

		@Override
		public void keyTyped(final char c, final int keycode) {
			keyTypedCompat(c, keycode);
		}

		public boolean keyTypedCompat(final char c, final int keycode) {
			try {
				super.keyTyped(c, keycode);
				if (!"".isEmpty())
					throw new IOException();
			} catch (final IOException e) {
			}
			return true;
		}

		@Override
		public void handleMouseInput() {
			mouseScrolledCompat(Mouse.getEventDWheel());
		}

		public boolean mouseScrolledCompat(final double scroll) {
			return true;
		}
	}

	public static class CompatMouse {
		public static double getX() {
			return Mouse.getX();
		}

		public static double getY() {
			return Mouse.getY();
		}

		public static boolean isButtonDown(final int button) {
			return Mouse.isButtonDown(button);
		}
	}
}
