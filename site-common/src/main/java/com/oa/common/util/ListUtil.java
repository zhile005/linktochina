package com.oa.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：列表筛选工具类/包括筛选和排序
 * 
 * @author 刘慎宝
 * @since 2010-11-24
 * @version 1.0v
 */
public class ListUtil<T> {

	public interface IFilter<T> {
		boolean exec(T t, Object... paras);
	}

	/**
	 * 筛选过滤器(参照linq语言)
	 * 
	 * @param source
	 * @param filter
	 * @return
	 */
	public List<T> filter(List<T> source, IFilter<T> filter, Object... paras) {
		List<T> returnList = new ArrayList<T>();
		if(source != null){
			for (T t : source) {
				if (filter.exec(t, paras)) {
					returnList.add(t);
				}
			}
		}
		return returnList;
	}
}
