package com.oa.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author liushenbao
 * 页面展示rowspan计算工具类
 * 使用方法
 * 前提条件
	List<Map> inList ：是排序排好的
 	String[] paraList = {"DEPID","WHID"};
 	proSuffix：自定义后缀【这里是字符串"_ROWSPAN"】
	list = RowsspanUtil.currentList(list,paraList,proSuffix);
	list中的每个map即会添加排序字段+_ROWSPAN，的key值
	若排序字段+_ROWSPAN 的值为-1，则不打印本行
	else <td rowspan='$!reportmap.WHID_ROWSPAN'>$!reportmap.WHNAME</td>
	vm展示如下
	#foreach($reportmap in $list)
		<tr>
			#if($reportmap.DEPID_ROWSPAN != -1)
			   <td rowspan='$!reportmap.DEPID_ROWSPAN'>$!reportmap.DEPNAME</td>
			#end
			#if($reportmap.WHID_ROWSPAN != -1)
			   <td rowspan='$!reportmap.WHID_ROWSPAN'>$!reportmap.WHNAME</td>
			#end
			<td>$!reportmap.PACKTYPENAME</td>
			<td>$!reportmap.VALUE</td>
        </tr>
    #end
    可以参照类【ReportAction】里面的实现
 */
public class RowsspanUtil {
	
	/**
	 * 页面展示rowspan计算工具类
	 */
	public static List<Map<String, Object>> currentList(List<Map<String,Object>> inList,String[] orderByStr,String proSuffix){
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		returnList.addAll(inList);
		Map<String,Integer> rowspanNumMap = new HashMap<String,Integer>();
		for (int i = 0;i<returnList.size();i++){
			rowspanNumMap.clear();
			for(String proorderBy : orderByStr){
				rowspanNumMap.put(proorderBy, 1);
			}
			Map<String, Object> proMap = returnList.get(i);
			for (int j = i+1;j<returnList.size();j++) {
				Map<String, Object> nextProMap = returnList.get(j);
				for (String proorderBy : orderByStr){
					String rowspanproorderBy = proorderBy+proSuffix;
					boolean eq = !"".equals(getMapValue(proMap,proorderBy)) && getMapValue(proMap,proorderBy).equals(getMapValue(nextProMap,proorderBy));
					if(eq){
						if("".equals(getMapValue(nextProMap,rowspanproorderBy))){
							nextProMap.put(rowspanproorderBy, -1);
							rowspanNumMap.put(proorderBy, rowspanNumMap.get(proorderBy)+1);
						}
					}else{
						break;
					}
				}
			}
			for (String proorderBy : orderByStr){
				String rowspanproorderBy = proorderBy+proSuffix;
				if("".equals(getMapValue(proMap,rowspanproorderBy))){
					proMap.put(rowspanproorderBy, rowspanNumMap.get(proorderBy));
				}
			}
		}
		return returnList;
	}
	private static String getMapValue(Map<String, Object> inmap,String key){
		return inmap.get(key)==null?"":inmap.get(key).toString();
	}
}
