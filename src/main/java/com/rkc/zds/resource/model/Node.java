package com.rkc.zds.resource.model;

public class Node<T> {
	
	public T data;
	
	public Node(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return data.toString();
	}

}
