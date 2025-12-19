package com.example.quiz_1141013.constants;

public enum Type {
	SINGLE("Single"),//
	MULTIPLE("Multiple"),//
	TEXT("Text");
	
	private String type;
	
	private Type(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	// values():指得是上面列舉的所有項目
	public static boolean checkType(String input) {
		for(Type type:values()) {
			if(input.equalsIgnoreCase(type.getType())) {
				return true;
			}
		}
		return false;
	}
	public static boolean isChosenType(String input) {
		if(input.equalsIgnoreCase(Type.SINGLE.getType()) 
				|| input.equalsIgnoreCase(Type.MULTIPLE.getType())) {
			return true;
		}
		return false;
	}
}
