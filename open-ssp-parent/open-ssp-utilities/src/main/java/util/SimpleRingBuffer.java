package util;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author André Schmer
 *
 * @param <T>
 */
public class SimpleRingBuffer<T> implements Serializable {

	private static final long serialVersionUID = -725298200872218297L;
	private final T[] buffer;
	private int pointer = 0;
	private final int capacity;

	@SuppressWarnings("unchecked")
	public SimpleRingBuffer(final int capacity, final Class<T> clazz) {
		if (capacity < 0) {
			throw new IllegalArgumentException("capacity may not be negative");
		}
		if (clazz == null) {
			throw new IllegalArgumentException("null injection is not allowed");
		}
		this.capacity = capacity;
		this.buffer = (T[]) Array.newInstance(clazz, capacity);
	}

	public void add(final T l) {
		pointer = pointer % this.capacity;
		buffer[pointer++] = l;
	}

	public T[] getAll() {
		return buffer;
	}

	public List<T> asList() {
		final List<T> l = new ArrayList<>();
		for (final T t : buffer) {
			if (t != null) {
				l.add(t);
			}
		}
		return l;
	}

	public int capacity() {
		return capacity;
	}

	public int size() {
		return this.buffer.length;
	}

	@Override
	public String toString() {
		return String.format("SimpleRingBuffer [buffer=%s, pointer=%s, capacity=%s]", Arrays.toString(buffer), pointer, capacity);
	}

}
