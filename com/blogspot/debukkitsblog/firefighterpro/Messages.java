package com.debukkitsblog.blogspot.firefighterpro;

public enum Messages {
	
	ALARM_MESSAGE_DEFAULT;
	
	private String message;
	
	public void initMessages() {
		//TODO Implementieren
	}
	
	public String getMessage() {
		return message;
	}
	
	@Override
	public String toString() {
		return this.message;
	}
	
}
