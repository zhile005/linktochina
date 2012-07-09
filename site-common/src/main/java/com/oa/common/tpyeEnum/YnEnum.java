package com.oa.common.tpyeEnum;

/**
 * 结算单类型
 */
public enum YnEnum {
    
	Y(1,"是"),
	N(0,"否");
	
	private final int value;
	private final String text;
	private YnEnum(int value,String text){
		this.value = value;
		this.text = text;
	}
	public int getValue() {
        return value;
    }
	public String getText() {
        return text;
    }
	public static String toText(Integer value) {
		String returnText = "未知";
		if(value != null){
			for(YnEnum ot:values()){
				if(ot.getValue() == value){
					returnText = ot.getText();
					break;
				}
			}
		}
        return returnText;
    }
}
