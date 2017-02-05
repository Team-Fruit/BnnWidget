package com.kamesuta.mc.bnnwidget.component;

import javax.annotation.Nonnull;

import com.kamesuta.mc.bnnwidget.OverridablePoint;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WRenderer;

/**
 * テキストラベル選択肢コンポーネントです
 * @author TeamFruit
 */
public class MSelectLabel extends MSelect<String> {
	/**
	 * テキストラベル
	 */
	protected final @Nonnull MLabel field;
	private @Nonnull StringSelector selector = new StringSelector();

	/**
	 * 文字列セレクターを設定
	 * @param selector ボタンセレクター
	 */
	public void setSelector(final @Nonnull StringSelector selector) {
		this.selector = selector;
	}

	/**
	 * 文字列セレクター
	 */
	@Override
	protected @Nonnull Selector<String> getSelector() {
		return this.selector;
	}

	public MSelectLabel(final @Nonnull R position, final float buttonwidth) {
		super(position, buttonwidth);
		add(this.field = new MLabel(new R(Coord.left(this.buttonwidth), Coord.right(this.buttonwidth), Coord.top(0), Coord.bottom(0))) {
			@Override
			public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity) {
				final Area a = getGuiPosition(pgp);
				WRenderer.startShape();
				OpenGL.glColor4f(0f, 0f, 0f, .4f);
				draw(a);
				super.draw(ev, pgp, p, frame, popacity);
			}

			@Override
			protected void onTextChanged(final @Nonnull String oldText) {
				onChanged(oldText, getText());
			}
		});
	}

	/**
	 * 選択が変更された際に呼ばれます
	 * @param widget ボタン
	 */
	@OverridablePoint
	protected void onChanged(final @Nonnull String oldText, final @Nonnull String newText) {
	}

	@Override
	protected void initWidget() {
		setText(getSelector().get());
	}

	@Override
	public @Nonnull MSelectLabel setText(final @Nonnull String text) {
		this.field.setText(text);
		return this;
	}
}
