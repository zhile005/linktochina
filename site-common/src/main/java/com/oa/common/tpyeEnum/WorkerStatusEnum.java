package com.oa.common.tpyeEnum;

/**
 * 
 */
public enum WorkerStatusEnum {
    
	READY(1,"预备"),
	LOCKED(2,"锁定"),
	SUCCESS(3,"执行成功"),
	FAILED(4,"执行失败");
	
	private final int value;
	private final String text;
	
	private WorkerStatusEnum(int value,String text){
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
		for(WorkerStatusEnum ot:values()){
			if(ot.getValue() == value){
				returnText = ot.getText();
				break;
			}
		}
        return returnText;
    }
}
