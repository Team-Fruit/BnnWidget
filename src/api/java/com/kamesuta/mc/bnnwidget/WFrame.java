package com.kamesuta.mc.bnnwidget;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

/**
 * MinecraftのGUIとウィジェットをつなぐ重量コンポーネントです。
 * <p>
 * 全てのウィジェットはこの重量コンポーネントの上で動作し、描画されます。
 * <p>
 * {@link net.minecraft.client.Minecraft#displayGuiScreen(GuiScreen) displayGuiScreen(GuiScreen)}メソッドなどでGUIを開く際はこのクラスのインスタンスを渡す必要があります。
 *
 * @author TeamFruit
 */
public class WFrame extends GuiScreen implements WContainer<WCommon> {
	/**
	 * GUIの背後に表示するGUIです
	 * <p>
	 * ウィジェットを他のGUIの上で動作させる場合に役立ちます
	 */
	protected @Nullable GuiScreen parent;
	/**
	 * ルート・パネルです。すべてのウィジェットはこのパネルの中に配置されます。
	 */
	private @Nonnull WPanel contentPane = new WPanel(new R());
	/**
	 * これはGUIで管理されるイベントです。
	 */
	protected final @Nonnull WEvent event = new WEvent(this);
	/**
	 * ウィジェットが初期化されたかどうかを保持します。
	 * <p>
	 * これは{@link #initWidget()}を一度だけ呼び出すのに役立ちます。
	 */
	protected boolean initialized;
	/**
	 * 画面サイズによるサイズ変更の影響を受けなくする場合はtrue
	 */
	protected boolean fixGuiScale;
	/**
	 * シングルプレイ時にゲームを一時停止させる場合はtrue
	 * @see GuiScreen#doesGuiPauseGame()
	 */
	protected boolean doesPauseGui = true;
	/**
	 * このフラグをtrueにするとこのGUIの一切の処理が行われなくなります。
	 */
	protected boolean closed = false;
	private float width;
	private float height;

	/**
	 * 入力されたマウスボタンを保持します
	 */
	protected int mousebutton = -1;
	/**
	 * 最後に入力されたカーソル位置を保持します
	 */
	protected @Nullable Point mouselast;
	/**
	 * 最後に入力されたマウスボタンを保持します
	 */
	protected int lastbutton = -1;
	/**
	 * ウィジェットを終了する際のフラグです。
	 */
	protected boolean closeRequest;

	/**
	 * float精度で幅を設定します
	 * @param width 幅
	 * @return this
	 */
	public @Nonnull WFrame setWidth(final float width) {
		this.width = width;
		super.width = (int) width;
		return this;
	}

	/**
	 * float精度で高さを設定します
	 * @param height 高さ
	 * @return this
	 */
	public @Nonnull WFrame setHeight(final float height) {
		this.height = height;
		super.height = (int) height;
		return this;
	}

	/**
	 * float精度の幅
	 * @return float精度の幅
	 */
	public float guiWidth() {
		if (super.width!=(int) this.width)
			this.width = super.width;
		return this.width;
	}

	/**
	 * float精度の高さ
	 * @return float精度の高さ
	 */
	public float guiHeight() {
		if (super.height!=(int) this.height)
			this.height = super.height;
		return this.height;
	}

	/**
	 * float精度の幅 (GUIスケールが固定されている場合はMinecraftの画面の幅)
	 * @return float精度の幅
	 */
	public float width() {
		if (this.fixGuiScale)
			return getDisplayWidth();
		return guiWidth();
	}

	/**
	 * float精度の高さ (GUIスケールが固定されている場合はMinecraftの画面の高さ)
	 * @return float精度の高さ
	 */
	public float height() {
		if (this.fixGuiScale)
			return getDisplayHeight();
		return guiHeight();
	}

	/**
	 * Minecraftの画面の幅
	 * @return Minecraftの画面の幅
	 */
	protected static float getDisplayWidth() {
		return WRenderer.mc.displayWidth;
	}

	/**
	 * Minecraftの画面の高さ
	 * @return Minecraftの画面の高さ
	 */
	protected static float getDisplayHeight() {
		return WRenderer.mc.displayHeight;
	}

	/**
	 * このGUIの幅がMinecraftの画面の幅よりも何倍であるか
	 * <p>
	 * この時、GUIは1/n倍されています。
	 * @return このGUIの幅がMinecraftの画面の幅よりも何倍であるか
	 */
	public float guiScaleX() {
		return guiWidth()/getDisplayWidth();
	}

	/**
	 * このGUIの高さがMinecraftの画面の高さよりも何倍であるか
	 * <p>
	 * この時、GUIは1/n倍されています。
	 * @return このGUIの高さがMinecraftの画面の高さよりも何倍であるか
	 */
	public float guiScaleY() {
		return guiHeight()/getDisplayHeight();
	}

	/**
	 * このGUIの大きさがMinecraftの画面の大きさよりも何倍であるか
	 * <p>
	 * この時、GUIは1/n倍されています。
	 * <br>
	 * 縦と横の比率が違うことは考え難いですが、違う場合は小さい方の値が返されます。
	 * @return GUIのコンテンツが、Minecraftの画面の何分の1で描画されるか
	 */
	public float guiScale() {
		return Math.min(guiScaleX(), guiScaleY());
	}

	/**
	 * GUIのコンテンツが、Minecraftの画面の何分の1で描画されるか
	 * @return GUIのコンテンツが、Minecraftの画面の何分の1で描画されるか
	 */
	public float scaleX() {
		return 1f/guiScaleX();
	}

	/**
	 * GUIのコンテンツが、Minecraftの画面の何分の1で描画されるか
	 * @return GUIのコンテンツが、Minecraftの画面の何分の1で描画されるか
	 */
	public float scaleY() {
		return 1f/guiScaleY();
	}

	/**
	 * GUIのコンテンツが、Minecraftの画面の何分の1で描画されるか
	 * <p>
	 * 縦と横の比率が違うことは考え難いですが、違う場合は大きい方の値が返されます。
	 * @return GUIのコンテンツが、Minecraftの画面の何分の1で描画されるか
	 */
	public float scale() {
		return 1f/guiScale();
	}

	/**
	 * 親GUIを指定してGUIを作成します
	 * @param parent 親GUI
	 */
	public WFrame(final @Nullable GuiScreen parent) {
		this.parent = parent;
		this.mc = WRenderer.mc;
	}

	public WFrame() {
	}

	/**
	 * GUIの絶対座標
	 * @return GUIの絶対座標
	 */
	public @Nonnull Area getAbsolute() {
		return new Area(0, 0, width(), height());
	}

	/**
	 * カーソルの絶対座標
	 * @return カーソルの絶対座標
	 */
	public @Nonnull Point getMouseAbsolute() {
		return new Point(Mouse.getX()*width()/getDisplayWidth(),
				height()-Mouse.getY()*height()/getDisplayHeight()-1);
	}

	@Override
	public @Nonnull List<WCommon> getContainer() {
		return getContentPane().getContainer();
	}

	@Override
	public boolean add(final @Nonnull WCommon widget) {
		return getContentPane().add(widget);
	}

	@Override
	public boolean remove(final @Nonnull WCommon widget) {
		return getContentPane().remove(widget);
	}

	/**
	 * ルート・パネルです。すべてのウィジェットはこのパネルの中に配置されます。
	 * @return ルート・パネル
	 */
	public @Nonnull WPanel getContentPane() {
		return this.contentPane;
	}

	/**
	 * ルート・パネルを設定します。すべてのウィジェットはこのパネルの中に配置されます。
	 * @return ルート・パネル
	 */
	public void setContentPane(final @Nonnull WPanel panel) {
		this.contentPane = panel;
	}

	@Override
	public void initGui() {
		sInitGui();
		if (!this.initialized) {
			initPane();
			initWidget();
			this.initialized = true;
		}
	}

	protected void sInitGui() {
		checkParentAndClose();
		if (this.parent!=null)
			this.parent.initGui();
		super.initGui();
	}

	protected void initPane() {
		final Area gp = getAbsolute();
		final Point p = getMouseAbsolute();
		getContentPane().onInit(this.event, gp, p);
	}

	/**
	 * ウィジェットを初期化します。
	 * <p>
	 * このメソッドはGUIの初期化時に一度だけ呼び出されます。
	 * <p>
	 * オーバーライドしてGUIの構築を行いましょう。
	 */
	protected void initWidget() {
	}

	@Override
	public void setWorldAndResolution(final @Nullable Minecraft mc, final int i, final int j) {
		sSetWorldAndResolution(WRenderer.mc, i, j);
	}

	protected void sSetWorldAndResolution(final @Nonnull Minecraft mc, final int i, final int j) {
		checkParentAndClose();
		if (this.parent!=null)
			this.parent.setWorldAndResolution(mc, i, j);
		super.setWorldAndResolution(mc, i, j);
	}

	public void drawScreen(final int mousex, final int mousey, final float f, final float opacity) {
		sDrawScreen(mousex, mousey, f);
		OpenGL.glPushMatrix();
		if (this.fixGuiScale)
			OpenGL.glScalef(guiScaleX(), guiScaleY(), 1f);
		final Area gp = getAbsolute();
		final Point p = getMouseAbsolute();
		getContentPane().draw(this.event, gp, p, f, opacity);
		OpenGL.glPopMatrix();
	}

	@Override
	public void drawScreen(final int mousex, final int mousey, final float f) {
		drawScreen(mousex, mousey, f, getOpacity());
	}

	/**
	 * GUIの絶対透明度
	 * @return 絶対透明度
	 */
	protected float getOpacity() {
		return 1f;
	}

	protected void sDrawScreen(final int mousex, final int mousey, final float f) {
		checkParentAndClose();
		final GuiScreen parent = this.parent;
		if (parent!=null) {
			OpenGL.glPushMatrix();
			OpenGL.glTranslatef(0, 0, -200f);
			parent.drawScreen(mousex, mousey, f);
			OpenGL.glPopMatrix();
		}
		super.drawScreen(mousex, mousey, f);
	}

	@Override
	protected void mouseClicked(final int x, final int y, final int button) {
		this.mousebutton = button;
		final Area gp = getAbsolute();
		final Point p = getMouseAbsolute();
		getContentPane().mouseClicked(this.event, gp, p, button);
		sMouseClicked(x, y, button);
	}

	protected void sMouseClicked(final int x, final int y, final int button) {
		super.mouseClicked(x, y, button);
	}

	@Override
	protected void mouseMovedOrUp(final int x, final int y, final int button) {
		sMouseMovedOrUp(x, y, button);
	}

	protected void sMouseMovedOrUp(final int x, final int y, final int button) {
		super.mouseMovedOrUp(x, y, button);
	}

	@Override
	protected void mouseClickMove(final int x, final int y, final int button, final long time) {
		final Area gp = getAbsolute();
		final Point p = getMouseAbsolute();
		getContentPane().mouseDragged(this.event, gp, p, button, time);
		sMouseClickMove(x, y, button, time);
	}

	protected void sMouseClickMove(final int x, final int y, final int button, final long time) {
		super.mouseClickMove(x, y, button, time);
	}

	@Override
	public void updateScreen() {
		sUpdateScreen();
		final Point p = getMouseAbsolute();
		final Area gp = getAbsolute();
		getContentPane().update(this.event, gp, p);
		final int m = Mouse.getEventButton();
		if (this.lastbutton==-1&&m!=this.lastbutton&&!Mouse.isButtonDown(this.mousebutton))
			getContentPane().mouseReleased(this.event, gp, p, this.mousebutton);
		this.lastbutton = m;
		if (this.mousebutton!=m&&m!=-1)
			this.mousebutton = m;
		if (!p.equals(this.mouselast)) {
			this.mouselast = p;
			getContentPane().mouseMoved(this.event, gp, p, this.mousebutton);
		}
		if (this.closeRequest)
			onCloseRequest();
	}

	protected void sUpdateScreen() {
		checkParentAndClose();
		if (this.parent!=null)
			this.parent.updateScreen();
		super.updateScreen();
	}

	@Override
	protected void keyTyped(final char c, final int keycode) {
		final Area gp = getAbsolute();
		final Point p = getMouseAbsolute();
		getContentPane().keyTyped(this.event, gp, p, c, keycode);
		sKeyTyped(c, keycode);
	}

	protected void sKeyTyped(final char c, final int keycode) {
		if (keycode==Keyboard.KEY_ESCAPE)
			requestClose();
	}

	protected void onCloseRequest() {
		final Area gp = getAbsolute();
		final Point p = getMouseAbsolute();
		if (getContentPane().onClosing(this.event, gp, p))
			close();
	}

	/**
	 * GUIが終了される最終フェーズで呼び出されます。
	 */
	protected void close() {
		if (WRenderer.mc.currentScreen==this)
			WRenderer.mc.displayGuiScreen(this.parent);
		else
			this.closed = true;
	}

	/**
	 * GUIを終了します。終了処理がある場合は終了処理を行った後、終了されます。
	 */
	public void requestClose() {
		getContentPane().onCloseRequest();
		this.closeRequest = true;
	}

	/**
	 * 終了処理をキャンセルします。
	 * <p>
	 * この機能は正しく機能しません。
	 */
	@Deprecated
	protected void cancelCloseRequest() {
		this.closeRequest = false;
	}

	@Override
	public void onGuiClosed() {
		sOnGuiClosed();
	}

	public void sOnGuiClosed() {
		super.onGuiClosed();
	}

	@Override
	public void handleMouseInput() {
		final int i = Mouse.getEventDWheel();
		if (i!=0) {
			final Area gp = getAbsolute();
			final Point p = getMouseAbsolute();
			getContentPane().mouseScrolled(this.event, gp, p, i);
		}
		sHandleMouseInput();
	}

	protected void sHandleMouseInput() {
		super.handleMouseInput();
	}

	@Override
	public void handleKeyboardInput() {
		sHandleKeyboardInput();
	}

	protected void sHandleKeyboardInput() {
		super.handleKeyboardInput();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return sDoesGuiPauseGame();
	}

	protected boolean sDoesGuiPauseGame() {
		return this.doesPauseGui||parentDoesGuiPauseGame();
	}

	protected boolean parentDoesGuiPauseGame() {
		checkParentAndClose();
		return this.parent!=null&&this.parent.doesGuiPauseGame();
	}

	/**
	 * 画面サイズによるサイズ変更の影響を受けなくします
	 * @param doesFixScale 画面サイズによるサイズ変更の影響を受けなくする場合はtrue
	 * @return this
	 */
	public @Nonnull WFrame setFixGuiScale(final boolean doesFixScale) {
		this.fixGuiScale = doesFixScale;
		return this;
	}

	/**
	 * シングルプレイ時にゲームを一時停止させるかどうかを設定します。
	 * @param doesPause シングルプレイ時にゲームを一時停止させる場合はtrue
	 * @return this
	 */
	public @Nonnull WFrame setGuiPauseGame(final boolean doesPause) {
		this.doesPauseGui = doesPause;
		return this;
	}

	/**
	 * 親GUIが閉じられているのを確認します
	 */
	public boolean isParentClosed() {
		if (this.parent instanceof WFrame)
			return ((WFrame) this.parent).closed;
		return false;
	}

	/**
	 * 親GUIを閉じます
	 */
	public void closeParent() {
		this.parent = getParentOrNull(this.parent);
	}

	/**
	 * 親GUIが閉じられているのを確認し、親GUIを閉じます
	 */
	public void checkParentAndClose() {
		if (isParentClosed())
			closeParent();
	}

	/**
	 * 親GUIを返します
	 * @return 親
	 */
	public @Nullable GuiScreen getParent() {
		return this.parent;
	}

	/**
	 * 現在のGUIを返します
	 * @param screen GUI
	 * @return 親
	 */
	public static @Nullable GuiScreen getCurrent() {
		return WRenderer.mc.currentScreen;
	}

	/**
	 * 親GUIを返します
	 * <p>
	 * screenがWFrameでない場合はnullを返します
	 * @param screen GUI
	 * @return 親
	 */
	public static @Nullable GuiScreen getParentOrNull(@Nullable final GuiScreen screen) {
		if (screen instanceof WFrame)
			return ((WFrame) screen).parent;
		return null;
	}

	/**
	 * 親GUIを返します
	 * <p>
	 * screenがWFrameでない場合はscreenを返します
	 * @param screen GUI
	 * @return 親
	 */
	public static @Nullable GuiScreen getParentOrThis(@Nullable final GuiScreen screen) {
		if (screen instanceof WFrame)
			return ((WFrame) screen).parent;
		return screen;
	}

	/**
	 * 現在のGUIの親GUIを返します
	 * <p>
	 * 現在のGUIがWFrameでない場合はnullを返します
	 * @return 親
	 */
	public static @Nullable GuiScreen getParentOrNull() {
		return getParentOrNull(getCurrent());
	}

	/**
	 * 現在のGUIの親GUIを返します
	 * <p>
	 * 現在のGUIがWFrameでない場合は現在のGUIを返します
	 * @return 親
	 */
	public static @Nullable GuiScreen getParentOrThis() {
		return getParentOrThis(getCurrent());
	}
}