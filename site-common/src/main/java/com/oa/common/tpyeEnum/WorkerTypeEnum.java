package com.oa.common.tpyeEnum;

/**
 * 
 */
public enum WorkerTypeEnum {
	
	SYN_EVENT(1,"同步事件"),
	ASY_WORKER(2,"worker");
	
	private final int value;
	private final String text;
	
	private WorkerTypeEnum(int value,String text){
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
			for(WorkerTypeEnum ot:values()){
				if(ot.getValue() == value){
					returnText = ot.getText();
					break;
				}
			}
		}
        return returnText;
    }
}
