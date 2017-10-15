package com.zlb.ecp.data.idao;

import java.util.List;
import java.util.Map;

import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 空间使用分析 
 * @author Jane.Luo
 * @date 2017年8月16日 下午4:48:06 
 * @version V1.0
 */
public interface IZoneUseAnalyDao {

	/**
	 * <p>Description: 教师空间使用数<p>
	 * @param where
	 * @return Integer
	 * @date 2017年8月16日 下午4:49:15
	 */
	public Integer queryTeacherZoneUseNum(Map<String, Object> where);
	
	/**
	 * <p>Description: 学生空间使用数<p>
	 * @param where
	 * @return Integer
	 * @date 2017年8月16日 下午4:49:15
	 */
	public Integer queryStuZoneUseNum(Map<String, Object> where);
	
	/**
	 * <p>Description: 班级空间使用数<p>
	 * @param where
	 * @return Integer
	 * @date 2017年8月16日 下午4:49:15
	 */
	public Integer queryClassZoneUseNum(Map<String, Object> where);
	
	/**
	 * <p>Description: 学校空间使用数<p>
	 * @param where
	 * @return Integer
	 * @date 2017年8月16日 下午4:49:15
	 */
	public Integer querySchoolZoneUseNum(Map<String, Object> where);
	
	/**
	 * <p>Description: 机构空间使用数<p>
	 * @param where
	 * @return Integer
	 * @date 2017年8月16日 下午4:49:15
	 */
	public Integer queryMechZoneUseNum(Map<String, Object> where);
	
	/**
	 * <p>Description: 查询各空间的使用数量<p>
	 * @param where
	 * @return Map<String,List<Map<String,Object>>>
	 * @date 2017年8月18日 下午3:45:51
	 */
	public Map<String, Object> queryAnyZone(Map<String, Object> where);
	
	/**
	 * <p>Description: 各区域空间使用统计<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月16日 下午5:26:35
	 */
	public ListResult<Map<String, Object>> queryAreaZoneUseStat(Map<String, Object> where);
	
	/**
	 * <p>Description: 各区域活跃空间分布<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 上午9:16:01
	 */
	public ListResult<Map<String, Object>> queryAreaZoneDistUser(Map<String, Object> where);
	
	/**
	 * <p>Description: 各类型活跃空间分布<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 上午9:40:10
	 */
	public ListResult<Map<String, Object>> queryTypeZoneUser(Map<String, Object> where);
	
	/**
	 * <p>Description: 空间使用趋势分析<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 上午10:01:12
	 */
	public ListResult<Map<String, Object>> queryZoneUseTrend(Map<String, Object> where);
	
	/**
	 * <p>Description: 定时去存每一天的活跃数据(测试方法)<p>
	 * void
	 * @date 2017年8月17日 上午10:08:43
	 */
	public void insertMongoTimer();
	
}
