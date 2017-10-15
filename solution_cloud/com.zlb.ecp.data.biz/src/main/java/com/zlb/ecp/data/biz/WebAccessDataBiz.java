package com.zlb.ecp.data.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlb.ecp.data.ibiz.IWebAccessDataBiz;
import com.zlb.ecp.data.idao.IWebAccessDataDao;
import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 页面访问数据
 * @author Jane.Luo
 * @date 2017年8月9日 下午3:32:06
 * @version V1.0
 */
@Service
public class WebAccessDataBiz implements IWebAccessDataBiz {

	@Autowired
	private IWebAccessDataDao webAccessDataDao;

	/**
	 * <p>
	 * Description: 页面访问顶部数据
	 * <p>
	 * 
	 * @param where
	 * @return List<Map<String,Object>>
	 * @date 2017年8月10日 上午11:58:39
	 */
	@Override
	public ListResult<Map<String, Object>> queryWebTopNum(Map<String, Object> where) {
		ListResult<Map<String, Object>> result = new ListResult<>();
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		Integer currBrowseNum = webAccessDataDao.currBrowseNum(where);
		Integer currIndepCustNum = webAccessDataDao.currIndepCustNum(where);
		Integer currIpNum = webAccessDataDao.currIpNum(where);
		Integer currAccessNum = webAccessDataDao.currAccessNum(where);
		map.put("currBrowseNum", currBrowseNum);
		map.put("currIndepCustNum", currIndepCustNum);
		map.put("currIpNum", currIpNum);
		map.put("currAccessNum", currAccessNum);
		list.add(map);
		result.setRows(list);
		return result;
	}

	/**
	 * <p>
	 * Description: 页面访问趋势分析
	 * <p>
	 * 
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月10日 下午1:49:21
	 */
	@Override
	public ListResult<Map<String, Object>> queryWebLoginTrend(Map<String, Object> where) {
		return webAccessDataDao.queryWebLoginTrend(where);
	}

}
