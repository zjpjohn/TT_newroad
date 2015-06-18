package com.newroad.util.container;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 堆栈类.用LinkedList实现存储,优于用数组存储的{@link java.util.Stack}<br><br>非线程安全
 *
 * @param <E> the element type
 * @author xupeng3
 * @noneThreadSafe
 */
public class Stack<E> {
	
	/** The list. */
	private LinkedList<E> list;
	
	/**
	 * Instantiates a new stack.
	 */
	public Stack() {
		this.list = new LinkedList<E>();
	}
	
	/**
	 * Size.
	 *
	 * @return the int
	 */
	public int size() {
		return list.size();
	}
	
	/**
	 * Pop the top.
	 *
	 * @return the e
	 */
	public E pop() {
		if(isEmpty()) {
			return null;
		}
		return this.list.removeLast();
	}
	
	/**
	 * Push.
	 *
	 * @param e the e
	 */
	public void push(E e) {
		this.list.addLast(e);
	}
	
	/**
	 * Gets the top.
	 *
	 * @return the top
	 */
	public E getTop() {
		if(list.isEmpty()) {
			return null;
		}
		return this.list.getLast();
	}
	
	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		return this.list.size() == 0;
	}
	
	/**
	 * Contains.
	 *
	 * @param e the e
	 * @return true, if contains e
	 */
	public boolean contains(E e) {
		return list.contains(e);
	}
	
	/**
	 * To list.
	 *
	 * @return the new ArrayList contains all elements of itself
	 */
	public List<E> toList() {
		return new ArrayList<E>(list);
	}
}
