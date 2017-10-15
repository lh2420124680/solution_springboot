package com.zlb.ecp.data.biz;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlb.ecp.data.ibiz.IAppDetailBiz;
import com.zlb.ecp.data.idao.IAppDetailDao;
import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 应用使用明细 
 * @author Jane.Luo
 * @date 2017年8月17日 下午4:04:32 
 * @version V1.0
 */
@Service
public class AppDetailBiz implements IAppDetailBiz {

	@Autowired
	private IAppDetailDao appDetailDao;
	
	/**
	 * <p>
	 * Description: 应用使用人数
	 * <p>
	 * 
	 * @param where
	 * @return Map<String, Object>
	 * @date 2017年8月17日 下午4:05:32
	 */
	@Override
	public Map<String, Object> queryAppUsePeoDetail(Map<String, Object> where) {
		return appDetailDao.queryAppUsePeoDetail(where);
	}

	/**
	 * <p>Description: 应用使用明细<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 下午4:05:32
	 */
	@Override
	public ListResult<Map<String, Object>> queryAppDetail(Map<String, Object> where) {
		return appDetailDao.queryAppDetail(where);
	}
	
}
