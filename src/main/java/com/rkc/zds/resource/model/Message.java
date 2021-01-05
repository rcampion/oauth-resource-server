package com.rkc.zds.resource.model;

public class Message<T> {

	private String message;
	
	private Node<T> data;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Node<T> getData() {
		return data;
	}

	public void setData(Node<T> data) {
		this.data = data;
	}

}
