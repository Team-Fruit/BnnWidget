package com.kamesuta.mc.bnnwidget.component;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VMotion;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

/**
 * Minecraftデザインのボタンコンポーネントです。
 *
 * @author TeamFruit
 */
public class MButton extends WBase {
	/**
	 * BnnWidget同封のMinecraftデザイン、ボタンです。
	 */
	public static final @Nonnull ResourceLocation button = new ResourceLocation("bnnwidget", "textures/gui/buttons.png");
	/**
	 * BnnWidgetは新デザインを開発中です。
	 */
	public static boolean tryNew;

	/**
	 * ボタンのテキストです
	 */
	public @Nullable String text = null;
	private boolean isEnabled = true;

	public MButton(final @Nonnull R position) {
		super(position);
	}

	/**
	 * ボタンのテキストを設定します
	 * @param s テキスト
	 * @return this
	 */
	public @Nonnull MButton setText(final @Nullable String s) {
		this.text = s;
		return this;
	}

	/**
	 * このボタンが有効な状態であるか
	 * @return 有効な状態の場合true
	 */
	public boolean isEnabled() {
		return this.isEnabled;
	}

	/**
	 * このボタンが有効な状態かどうかを設定します
	 * @param b 有効な状態の場合true
	 * @return this
	 */
	public @Nonnull MButton setEnabled(final boolean b) {
		this.isEnabled = b;
		return this;
	}

	@Override
	public boolean mouseClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
		final Area abs = getGuiPosition(pgp);
		if (abs.pointInside(p)) {
			if (isEnabled())
				if (onClicked(ev, pgp, p, button)) {
					ev.bus.post(makeEvent(button));
					playPressButtonSound();
				}
			return true;
		}
		return false;
	}

	/**
	 * ボタンが押された時の効果音を再生します
	 */
	public static void playPressButtonSound() {
		mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
	}

	/**
	 * ボタンが押されたときに呼び出されます
	 * @param ev イベント
	 * @param pgp 親コンポーネントの絶対座標
	 * @param mouse カーソル絶対座標
	 * @param button クリックされたボタン
	 * @return イベントを受け取った場合はtrue
	 */
	protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point mouse, final int button) {
		return true;
	}

	@Override
	public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity) {
		final Area a = getGuiPosition(pgp);
		final float opacity = getGuiOpacity(popacity);

		if (tryNew) {
			WRenderer.startShape();
			if (isEnabled()) {
				OpenGL.glColor4f(.2f, .2f, .2f, opacity*.2f);
				draw(a);
			}
			OpenGL.glColor4f(1f, 1f, 1f, opacity*this.o.get()*.3f);
			draw(a);
			OpenGL.glLineWidth(1f);
			if (isEnabled())
				OpenGL.glColor4f(0f, 0f, 0f, opacity);
			else
				OpenGL.glColor4f(.5f, .5f, .5f, opacity);
			draw(a, GL_LINE_LOOP);
		} else {
			WRenderer.startTexture();
			OpenGL.glColor4f(1.0F, 1.0F, 1.0F, opacity);
			texture().bindTexture(button);
			final int state = !isEnabled() ? 0 : a.pointInside(p) ? 2 : 1;

			drawTextureModalSize(a.x1(), a.y1(), a.w()/2, a.h()/2, 0, state*80, a.w()/2, a.h()/2);
			drawTextureModalSize(a.x1()+a.w()/2, a.y1(), a.w()/2, a.h()/2, 256-a.w()/2, state*80, a.w()/2, a.h()/2);
			drawTextureModalSize(a.x1(), a.y1()+a.h()/2, a.w()/2, a.h()/2, 0, state*80+80-a.h()/2, a.w()/2, a.h()/2);
			drawTextureModalSize(a.x1()+a.w()/2, a.y1()+a.h()/2, a.w()/2, a.h()/2, 256-a.w()/2, state*80+80-a.h()/2, a.w()/2, a.h()/2);
		}
		drawText(ev, pgp, p, frame, opacity);
	}

	/**
	 * カーソルホバーの透明度変化
	 */
	protected VMotion o = V.pm(0).start();
	/**
	 * カーソルがホバーかどうか
	 */
	protected boolean ob = false;;

	@Override
	public void update(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
		final Area a = getGuiPosition(pgp);
		if (a.pointInside(p)) {
			if (!this.ob)
				this.o.stop().add(Easings.easeLinear.move(.1f, 1f)).start();
			this.ob = true;
		} else {
			if (this.ob)
				this.o.stop().add(Easings.easeLinear.move(.1f, 0f)).start();
			this.ob = false;
		}
	}

	/**
	 * テキストを描画します
	 * @param ev イベント
	 * @param pgp 親コンポーネントの絶対座標
	 * @param mouse カーソル絶対座標
	 * @param frame 描画されるタイミングのpartialTicksです。
	 * @param popacity 親コンポーネントの絶対透明度
	 */
	public void drawText(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point mouse, final float frame, final float popacity) {
		final String text = this.text;
		if (text!=null) {
			final Area a = getGuiPosition(pgp);
			WRenderer.startTexture();
			final Color c = new Color(getTextColor(ev, pgp, mouse, frame));
			OpenGL.glColor4i(c.getRed(), c.getGreen(), c.getBlue(), (int) (c.getAlpha()*popacity));
			drawString(text, a, Align.CENTER, VerticalAlign.MIDDLE, true);
		}
	}

	/**
	 * テキストの色
	 * @param ev イベント
	 * @param pgp 親コンポーネントの絶対座標
	 * @param mouse カーソル絶対座標
	 * @param frame 描画されるタイミングのpartialTicksです。
	 * @return
	 */
	public int getTextColor(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point mouse, final float frame) {
		final Area abs = getGuiPosition(pgp);
		return abs.pointInside(mouse) ? -96 : !isEnabled() ? -6250336 : -2039584;
	}

	/**
	 * イベントを作成します
	 * @param mousebutton 押されたマウスボタン
	 * @return イベント
	 */
	public MButtonEvent makeEvent(final int mousebutton) {
		return new MButtonEvent(this, mousebutton);
	}

	/**
	 * ボタンが押されたときのイベントです
	 *
	 * @author TeamFruit
	 */
	public static class MButtonEvent extends Event {
		/**
		 * 押されたボタン
		 */
		public final MButton button;
		/**
		 * 押されたマウスボタン
		 */
		public final int mousebutton;

		public MButtonEvent(final MButton button, final int mousebutton) {
			this.button = button;
			this.mousebutton = mousebutton;
		}
	}
}
