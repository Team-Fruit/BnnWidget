package com.kamesuta.mc.bnnwidget.render;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Maps;

public class RenderOption {

	private final @Nonnull Map<String, Object> data = Maps.newHashMap();

	public @Nullable <T> T get(final @Nonnull String key, final @Nonnull Class<T> clazz) {
		final Object obj = this.data.get(key);
		if (clazz.isInstance(obj))
			return (T) obj;
		return null;
	}

	public void set(final @Nonnull String key, final @Nonnull Object obj) {
		this.data.put(key, obj);
	}
}
