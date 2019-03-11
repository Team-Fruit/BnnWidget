package net.teamfruit.bnnwidget.compat;

import java.io.IOException;
import java.nio.DoubleBuffer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class Compat {
	public static class CompatSound {
		private final ISound sound;

		public CompatSound(final ISound sound) {
			this.sound = sound;
		}

		public static CompatSound createClickSound() {
			return new CompatSound(SimpleSound.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
		}

		public void play() {
			getMinecraft().getSoundHandler().play(this.sound);
		}
	}

	public static class CompatGuiTextField extends GuiTextField {
		public CompatGuiTextField() {
			super(1, CompatMinecraft.getMinecraft().getFontRenderer().getFontRendererObj(), 0, 0, 0, 0);
		}

		public int getNthWordFromPosWSCompat(final int n, final int pos, final boolean skipWs) {
			return getNthWordFromPosWS(n, pos, skipWs);
		}

		public int getX() {
			return this.x;
		}

		public void setX(final int value) {
			this.x = value;
		}

		public int getY() {
			return this.y;
		}

		public void setY(final int value) {
			this.y = value;
		}

		public void tickCompat() {
			tick();
		}

		public void charTypedCompat(final char c, final int keycode) {
			charTyped(c, keycode);
		}

		public void drawTextBox() {
			drawTextBoxCompat();
		}

		public void drawTextBoxCompat() {
		}
	}

	public static abstract class CompatFontRendererBase extends FontRenderer {
		public CompatFontRendererBase(final GameSettings gameSettingsIn, final ResourceLocation location, final TextureManager textureManagerIn, final boolean unicode) {
			super(textureManagerIn, new Font(textureManagerIn, location));
		}

		@Override
		public int drawStringWithShadow(@Nullable final String str, final float x, final float y, final int color) {
			return drawStringWithShadowCompat(str, x, y, color);
		}

		protected abstract int drawStringWithShadowCompat(@Nullable final String str, final float x, final float y, final int color);

		@Override
		public int drawString(@Nullable final String str, final float x, final float y, final int color) {
			return drawStringCompat(str, x, y, color, false);
		}

		protected abstract int drawStringCompat(@Nullable final String str, final float x, final float y, final int color, final boolean shadow);

		@Override
		public int getWordWrappedHeight(final String str, final int maxLength) {
			return getWordWrappedHeightCompat(str, maxLength);
		}

		public abstract int getWordWrappedHeightCompat(final String str, final int maxLength);
	}

	public static @Nonnull Minecraft getMinecraft() {
		return Minecraft.getInstance();
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
			return new CompatFontRenderer(this.minecraft.fontRenderer);
		}

		public int getDisplayWidth() {
			return this.minecraft.mainWindow.getWidth();
		}

		public int getDisplayHeight() {
			return this.minecraft.mainWindow.getHeight();
		}
	}

	public static class CompatFontRenderer {
		private final FontRenderer font;

		public CompatFontRenderer(final FontRenderer font) {
			this.font = font;
		}

		public int drawString(final String msg, final float x, final float y, final int color, final boolean shadow) {
			if (shadow)
				return this.font.drawStringWithShadow(msg, x, y, color);
			else
				return this.font.drawString(msg, x, y, color);
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
			return getStringWidth(TextFormatting.getTextWithoutFormattingCodes(s));
		}

		public FontRenderer getFontRendererObj() {
			return this.font;
		}
	}

	private static class WVertexImpl implements WVertex {
		public static final @Nonnull Tessellator t = Tessellator.getInstance();
		public static final @Nonnull BufferBuilder w = t.getBuffer();

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
			return GLFW.glfwGetTimerFrequency();
		}

		public static long getTime() {
			return GLFW.glfwGetTimerValue();
		}
	}

	public static class CompatCursor {
		public static void setCursorVisible(final boolean b) {
			if (b)
				GLFW.glfwSetInputMode(getMinecraft().mainWindow.getHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
			else
				GLFW.glfwSetInputMode(getMinecraft().mainWindow.getHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
		}
	}

	public static class CompatGuiScreen extends GuiScreen {
		@Override
		public void render(final int mousex, final int mousey, final float f) {
			drawScreenCompat(mousex, mousey, f);
		}

		public void drawScreenCompat(final int mousex, final int mousey, final float f) {
			super.render(mousex, mousey, f);
		}

		public static void drawScreen(final GuiScreen screen, final int mousex, final int mousey, final float f) {
			screen.render(mousex, mousey, f);
		}

		@Override
		public boolean mouseClicked(final double x, final double y, final int button) {
			return mouseClickedCompat(x, y, button);
		}

		public boolean mouseClickedCompat(final double x, final double y, final int button) {
			try {
				final boolean b = super.mouseClicked(x, y, button);
				if (!"".isEmpty())
					throw new IOException();
				return b;
			} catch (final IOException e) {
			}
			return false;
		}

		@Override
		public boolean mouseDragged(final double x, final double y, final int button, final double dx, final double dy) {
			return mouseClickedCompat(x, y, button);
		}

		public boolean mouseClickMoveCompat(final double x, final double y, final int button, final long time, final double dx, final double dy) {
			return super.mouseDragged(x, y, button, dx, dy);
		}

		@Override
		public void tick() {
			updateScreenCompat();
		}

		public void updateScreenCompat() {
			super.tick();
		}

		public static void updateScreen(final GuiScreen screen) {
			screen.tick();
		}

		@Override
		public boolean charTyped(final char c, final int keycode) {
			return keyTypedCompat(c, keycode);
		}

		public boolean keyTypedCompat(final char c, final int keycode) {
			try {
				final boolean b = super.charTyped(c, keycode);
				if (!"".isEmpty())
					throw new IOException();
				return b;
			} catch (final IOException e) {
			}
			return false;
		}

		@Override
		public boolean mouseScrolled(final double scroll) {
			return mouseScrolledCompat(scroll);
		}

		public boolean mouseScrolledCompat(final double scroll) {
			return super.mouseScrolled(scroll);
		}
	}

	public static class CompatMouse {
		public static double getX() {
			final DoubleBuffer pos = BufferUtils.createDoubleBuffer(1);
			GLFW.glfwGetCursorPos(getMinecraft().mainWindow.getHandle(), pos, null);
			return pos.get(0);
		}

		public static double getY() {
			final DoubleBuffer pos = BufferUtils.createDoubleBuffer(1);
			GLFW.glfwGetCursorPos(getMinecraft().mainWindow.getHandle(), null, pos);
			return pos.get(0);
		}

		public static boolean isButtonDown(final int button) {
			return GLFW.glfwGetMouseButton(getMinecraft().mainWindow.getHandle(), button)==GLFW.GLFW_PRESS;
		}
	}
}
