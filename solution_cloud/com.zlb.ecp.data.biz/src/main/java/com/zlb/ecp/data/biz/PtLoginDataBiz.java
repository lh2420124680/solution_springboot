package com.zlb.ecp.data.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlb.ecp.data.ibiz.IPtLoginDataBiz;
import com.zlb.ecp.data.idao.IPtLoginDataDao;
import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 平台登录数据 
 * @author Jane.Luo
 * @date 2017年8月9日 下午2:32:04 
 * @version V1.0
 */
@Service
public class PtLoginDataBiz implements IPtLoginDataBiz {

	@Autowired
	private IPtLoginDataDao ptLoginDataDao;
	
	/**
	 * <p>Description: 获取顶部的总数据<p>
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月9日 下午2:19:46
	 */
	@Override
	public ListResult<Map<String, Object>> queryTopNum(Map<String, Object> where) {
		ListResult<Map<String, Object>> result = new ListResult<>();
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		Integer preLoginNum = ptLoginDataDao.preLoginNum(where);
		Integer weekLoginNum = ptLoginDataDao.weekLoginNum(where);
		Integer monthLoginNum = ptLoginDataDao.monthLoginNum(where);
		Integer totalLoginNum = ptLoginDataDao.totalLoginNum(where);
		map.put("preLoginNum", preLoginNum);
		map.put("weekLoginNum", weekLoginNum);
		map.put("monthLoginNum", monthLoginNum);
		map.put("totalLoginNum", totalLoginNum);
		list.add(map);
		result.setRows(list);
		return result;
	}
	
	/**
	 * <p>Description: 平台登录用户分布<p>
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月9日 下午2:42:25
	 */
	@Override
	public ListResult<Map<String, Object>> queryLoginUserDist(Map<String, Object> where) {
		return ptLoginDataDao.queryLoginUserDist(where);
	}
	
	/**
	 * <p>Description:平台登录趋势定时任务<p>
	 * @date 2017年8月9日 下午5:02:12
	 */
	@Override
	public void insertLoginMongoTimer() {
		ptLoginDataDao.insertLoginMongoTimer();
	}
	
	/**
	 * <p>Description: 平台登录趋势分析<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月10日 上午10:51:32
	 */
	@Override
	public ListResult<Map<String, Object>> queryLoginTrend(Map<String, Object> where) {
		return ptLoginDataDao.queryLoginTrend(where);
	}
}
