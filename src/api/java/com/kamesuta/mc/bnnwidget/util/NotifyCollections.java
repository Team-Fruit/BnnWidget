package com.kamesuta.mc.bnnwidget.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import javax.annotation.Nonnull;

/**
 * 変更を確認することができるリストです
 *
 * @author TeamFruit
 */
public class NotifyCollections {
	/**
	 * 変更を確認することができるインターフェイスです
	 *
	 * @author TeamFruit
	 */
	public static interface IModCount<E> extends Collection<E> {
		int getModCount();
	}

	/**
	 * 変更を確認することができるArrayListです
	 *
	 * @author TeamFruit
	 */
	public static class NotifyArrayList<E> extends ArrayList<E> implements IModCount<E> {
		public NotifyArrayList(final int initialCapacity) {
			super(initialCapacity);
		}

		public NotifyArrayList() {
			super();
		}

		public NotifyArrayList(final @Nonnull Collection<? extends E> c) {
			super(c);
		}

		@Override
		public int getModCount() {
			return this.modCount;
		}
	}

	/**
	 * 変更を確認することができるLinkedListです
	 *
	 * @author TeamFruit
	 */
	public static class NotifyLinkedList<E> extends LinkedList<E> implements IModCount<E> {
		public NotifyLinkedList() {
			super();
		}

		public NotifyLinkedList(final @Nonnull Collection<? extends E> c) {
			super(c);
		}

		@Override
		public int getModCount() {
			return this.modCount;
		}
	}

	/**
	 * 変更を確認することができるArrayDequeです
	 *
	 * @author TeamFruit
	 */
	public static class NotifyArrayDeque<E> extends ArrayDeque<E> implements IModCount<E> {
		public NotifyArrayDeque() {
			super();
		}

		public NotifyArrayDeque(final @Nonnull Collection<? extends E> c) {
			super(c);
		}

		@Override
		public int getModCount() {
			return size();
		}
	}
}
