package com.zlb.ecp.data.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.zlb.ecp.data.idao.IZoneUseAnalyDao;
import com.zlb.ecp.helper.DateCenterHelper;
import com.zlb.ecp.helper.StringHelper;
import com.zlb.ecp.pojo.DataCommDto;
import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 空间使用分析
 * @author Jane.Luo
 * @date 2017年8月16日 下午4:48:06
 * @version V1.0
 */
@Repository
public class ZoneUseAnalyDao implements IZoneUseAnalyDao {

	private static final Logger log = LoggerFactory.getLogger(ZoneUseAnalyDao.class);

	@Autowired
	private MongoOperations mongoTemplate;

	/**
	 * <p>
	 * Description: 教师空间使用数
	 * <p>
	 * 
	 * @param where
	 * @return Integer
	 * @date 2017年8月16日 下午4:49:15
	 */
	@Override
	public Integer queryTeacherZoneUseNum(Map<String, Object> where) {
		Integer allNum = 0;
		try {
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoListsByZone();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_zone_teacher_table").insert(list);
			String matchStr = "{$match:{module:'教师空间'}}";
			DBObject match = (DBObject) JSON.parse(matchStr);
			AggregationOutput ao = mongoTemplate.getCollection("dataCenter_zone_teacher_table").aggregate(match);
			for (Iterator<DBObject> it = ao.results().iterator(); it.hasNext(); it.next()) {
				allNum += 1;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			// 清空临时总表
			mongoTemplate.dropCollection("dataCenter_zone_teacher_table");
		}
		return allNum;
	}

	/**
	 * <p>
	 * Description: 学生空间使用数
	 * <p>
	 * 
	 * @param where
	 * @return Integer
	 * @date 2017年8月16日 下午4:49:15
	 */
	@Override
	public Integer queryStuZoneUseNum(Map<String, Object> where) {
		Integer allNum = 0;
		try {
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoListsByZone();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_zone_stu_table").insert(list);
			String matchStr = "{$match:{module:'学生空间'}}";
			DBObject match = (DBObject) JSON.parse(matchStr);
			AggregationOutput ao = mongoTemplate.getCollection("dataCenter_zone_stu_table").aggregate(match);
			for (Iterator<DBObject> it = ao.results().iterator(); it.hasNext(); it.next()) {
				allNum += 1;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			// 清空临时总表
			mongoTemplate.dropCollection("dataCenter_zone_stu_table");
		}
		return allNum;
	}

	/**
	 * <p>
	 * Description: 班级空间使用数
	 * <p>
	 * 
	 * @param where
	 * @return Integer
	 * @date 2017年8月16日 下午4:49:15
	 */
	@Override
	public Integer queryClassZoneUseNum(Map<String, Object> where) {
		Integer allNum = 0;
		try {
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoListsByZone();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_zone_class_table").insert(list);
			String matchStr = "{$match:{module:'班级空间'}}";
			DBObject match = (DBObject) JSON.parse(matchStr);
			AggregationOutput ao = mongoTemplate.getCollection("dataCenter_zone_class_table").aggregate(match);
			for (Iterator<DBObject> it = ao.results().iterator(); it.hasNext(); it.next()) {
				allNum += 1;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			// 清空临时总表
			mongoTemplate.dropCollection("dataCenter_zone_class_table");
		}
		return allNum;
	}

	/**
	 * <p>
	 * Description: 学校空间使用数
	 * <p>
	 * 
	 * @param where
	 * @return Integer
	 * @date 2017年8月16日 下午4:49:15
	 */
	@Override
	public Integer querySchoolZoneUseNum(Map<String, Object> where) {
		Integer allNum = 0;
		try {
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoListsByZone();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_zone_school_table").insert(list);
			String matchStr = "{$match:{module:'学校空间'}}";
			DBObject match = (DBObject) JSON.parse(matchStr);
			AggregationOutput ao = mongoTemplate.getCollection("dataCenter_zone_school_table").aggregate(match);
			for (Iterator<DBObject> it = ao.results().iterator(); it.hasNext(); it.next()) {
				allNum += 1;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			// 清空临时总表
			mongoTemplate.dropCollection("dataCenter_zone_school_table");
		}
		return allNum;
	}

	/**
	 * <p>
	 * Description: 机构空间使用数
	 * <p>
	 * 
	 * @param where
	 * @return Integer
	 * @date 2017年8月16日 下午4:49:15
	 */
	@Override
	public Integer queryMechZoneUseNum(Map<String, Object> where) {
		Integer allNum = 0;
		try {
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoListsByZone();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_zone_mech_table").insert(list);
			String matchStr = "{$match:{module:'机构空间'}}";
			DBObject match = (DBObject) JSON.parse(matchStr);
			AggregationOutput ao = mongoTemplate.getCollection("dataCenter_zone_mech_table").aggregate(match);
			for (Iterator<DBObject> it = ao.results().iterator(); it.hasNext(); it.next()) {
				allNum += 1;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			// 清空临时总表
			mongoTemplate.dropCollection("dataCenter_zone_mech_table");
		}
		return allNum;
	}

	/**
	 * <p>Description: 查询各空间的使用数量<p>
	 * @param where
	 * @return Map<String,List<Map<String,Object>>>
	 * @date 2017年8月18日 下午3:45:51
	 */
	@Override
	public Map<String, Object> queryAnyZone(Map<String, Object> where) {
		Map<String, Object> dataMap = new HashMap<>();
		// 过滤条件的gid
		Object gid = where.get("gid");
		Object schoolGid = where.get("schoolGid");
		// 层级
		String layerLevel = where.get("layerLevel").toString();
		try {
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoListsByZone();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_zone_AnyUse_table").insert(list);
			String matchStrTea = "";
			String matchStrStu = "";
			String matchStrCls = "";
			String matchStrSch = "";
			String matchStrMech = "";
			String groupStr = "";
			String keyVals = "";
			if (StringHelper.IsEmptyOrNull(schoolGid)) {
				if ("1".equals(layerLevel)) {
					groupStr = "{$group:{_id:{'cityName':'$cityName','module':'$module'},actNum:{$sum:1}}}";
					matchStrTea = "{$match:{provinceGid:'"+gid+"'}}";
					keyVals = "cityName";
				} else if ("2".equals(layerLevel)) {
					groupStr = "{$group:{_id:{'townName':'$townName','module':'$module'},actNum:{$sum:1}}}";
					matchStrTea = "{$match:{cityGid:'"+gid+"'}}";
					keyVals = "townName";
				} else if ("3".equals(layerLevel)) {
					groupStr = "{$group:{_id:{'schoolName':'$schoolName','module':'$module'},actNum:{$sum:1}}}";
					matchStrTea = "{$match:{townGid:'"+gid+"'}}";
					keyVals = "schoolName";
				}
			} else {
				groupStr = "{$group:{_id:{'schoolName':'$schoolName','module':'$module'},actNum:{$sum:1}}}";
				matchStrTea = "{$match:{schoolGid:'"+schoolGid+"'}}";
				keyVals = "schoolName";
			}
			DBObject group = (DBObject) JSON.parse(groupStr);
			DBObject matchTea = (DBObject) JSON.parse(matchStrTea);
			AggregationOutput tea = mongoTemplate.getCollection("dataCenter_zone_AnyUse_table").aggregate(matchTea,group);
			List<Map<String, Object>> result = new ArrayList<>();
			for (Iterator<DBObject> it = tea.results().iterator(); it.hasNext();) { 
				Map<String, Object> map = new HashMap<>();
				BasicDBObject dbo = (BasicDBObject) it.next();
				BasicDBObject keyValus = (BasicDBObject) dbo.get("_id");
				String name = keyValus.get(keyVals).toString();
				String zone = keyValus.get("module").toString();
				String actNum = dbo.get("actNum").toString();
				if (dataMap.containsKey(name)) {
					DataCommDto object = (DataCommDto) dataMap.get(name);
					if ("教师空间".equals(zone)) {
						object.setValueOne(actNum);
					} else if ("学生空间".equals(zone)) {
						object.setValueTwo(actNum);
					} else if ("班级空间".equals(zone)) {
						object.setValueThr(actNum);
					} else if ("学校空间".equals(zone)) {
						object.setValueFou(actNum);
					} else if ("机构空间".equals(zone)) {
						object.setValueFiv(actNum);
					}
					dataMap.put(name, object);
				} else {
					DataCommDto dcd = new DataCommDto();
					if ("教师空间".equals(zone)) {
						dcd.setValueOne(actNum);
					} else if ("学生空间".equals(zone)) {
						dcd.setValueTwo(actNum);
					} else if ("班级空间".equals(zone)) {
						dcd.setValueThr(actNum);
					} else if ("学校空间".equals(zone)) {
						dcd.setValueFou(actNum);
					} else if ("机构空间".equals(zone)) {
						dcd.setValueFiv(actNum);
					}
					dataMap.put(name, dcd);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			// 清空临时总表
			mongoTemplate.dropCollection("dataCenter_zone_AnyUse_table");
		}
		return dataMap;
	}

	/**
	 * <p>
	 * Description: 各区域空间使用统计
	 * <p>
	 * 
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月16日 下午5:26:35
	 */
	@Override
	public ListResult<Map<String, Object>> queryAreaZoneUseStat(Map<String, Object> where) {
		ListResult<Map<String, Object>> result = new ListResult<>();
		try {
			List<Map<String, Object>> table = new ArrayList<>();
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoListsByZone();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_zone_areaUse_table").insert(list);
			// 过滤条件的gid
			Object gid = where.get("gid");
			Object schoolGid = where.get("schoolGid");
			// 层级
			String layerLevel = where.get("layerLevel").toString();
			String currDate = DateCenterHelper.currDate("yyyy-MM-dd");
			String preDate = DateCenterHelper.preDate(-1);
			String weekDate = DateCenterHelper.preDate(-7);
			String monthDate = DateCenterHelper.preDate(-30);
			String groupStr = "";
			String preMatchStr = "";
			String weekMatchStr = "";
			String monthMatchStr = "";
			String keyValu = "";
			if (StringHelper.IsEmptyOrNull(schoolGid)) {
				if ("1".equals(layerLevel)) { // 海南省
					groupStr = "{$group:{_id:{'cityName':'$cityName'},actNum:{$sum:1}}}";
					preMatchStr = "{$match:{pdate:{$regex:'^.*" + preDate + ".*$'},provinceGid:'" + gid + "'}}";
					weekMatchStr = "{$match:{'pdate':{'$gte':'" + weekDate + "', '$lt':'" + currDate + "'},provinceGid:'"
							+ gid + "'}}";
					monthMatchStr = "{$match:{'pdate':{'$gte':'" + monthDate + "', '$lt':'" + currDate + "'},provinceGid:'"
							+ gid + "'}}";
					keyValu = "cityName";
				} else if ("2".equals(layerLevel)) { // 市县
					groupStr = "{$group:{_id:{'townName':'$townName'},actNum:{$sum:1}}}";
					preMatchStr = "{$match:{pdate:{$regex:'^.*" + preDate + ".*$'},cityGid:'" + gid + "'}}";
					weekMatchStr = "{$match:{'pdate':{'$gte':'" + weekDate + "', '$lt':'" + currDate + "'},cityGid:'" + gid
							+ "'}}";
					monthMatchStr = "{$match:{'pdate':{'$gte':'" + monthDate + "', '$lt':'" + currDate + "'},cityGid:'"
							+ gid + "'}}";
					keyValu = "townName";
				} else if ("3".equals(layerLevel)) { // 区
					groupStr = "{$group:{_id:{'schoolName':'$schoolName'},actNum:{$sum:1}}}";
					preMatchStr = "{$match:{pdate:{$regex:'^.*" + preDate + ".*$'},townGid:'" + gid + "'}}";
					weekMatchStr = "{$match:{'pdate':{'$gte':'" + weekDate + "', '$lt':'" + currDate + "'},townGid:'" + gid
							+ "'}}";
					monthMatchStr = "{$match:{'pdate':{'$gte':'" + monthDate + "', '$lt':'" + currDate + "'},townGid:'"
							+ gid + "'}}";
					keyValu = "schoolName";
				}
			} else {
				groupStr = "{$group:{_id:{'schoolName':'$schoolName'},actNum:{$sum:1}}}";
				preMatchStr = "{$match:{pdate:{$regex:'^.*" + preDate + ".*$'},schoolGid:'" + schoolGid + "'}}";
				weekMatchStr = "{$match:{'pdate':{'$gte':'" + weekDate + "', '$lt':'" + currDate + "'},schoolGid:'"
						+ schoolGid + "'}}";
				monthMatchStr = "{$match:{'pdate':{'$gte':'" + monthDate + "', '$lt':'" + currDate + "'},schoolGid:'"
						+ schoolGid + "'}}";
				keyValu = "schoolName";
			}
			DBObject group = (DBObject) JSON.parse(groupStr);
			DBObject preMatch = (DBObject) JSON.parse(preMatchStr);
			DBObject weekMatch = (DBObject) JSON.parse(weekMatchStr);
			DBObject monthMatch = (DBObject) JSON.parse(monthMatchStr);
			AggregationOutput preAo = mongoTemplate.getCollection("dataCenter_zone_areaUse_table").aggregate(preMatch,
					group);
			AggregationOutput weekAo = mongoTemplate.getCollection("dataCenter_zone_areaUse_table").aggregate(weekMatch,
					group);
			AggregationOutput monthAo = mongoTemplate.getCollection("dataCenter_zone_areaUse_table")
					.aggregate(monthMatch, group);

			Integer preNum = 0;
			// 昨日活跃数
			Map<String, Object> preMap = new HashMap<>();
			for (Iterator<DBObject> it = preAo.results().iterator(); it.hasNext();) { // 昨日活跃
				BasicDBObject dbo = (BasicDBObject) it.next();
				BasicDBObject keyValus = (BasicDBObject) dbo.get("_id");
				String cityName = keyValus.getString(keyValu);
				DataCommDto dcd = new DataCommDto();
				// 有效次数
				Integer validCount = Integer.valueOf(dbo.get("actNum").toString());
				if (validCount >= 1) {
					if (preMap.containsKey(cityName)) {
						DataCommDto object = (DataCommDto) preMap.get(cityName);
						String valueOne = object.getValueOne();
						object.setValueOne(String.valueOf(Integer.valueOf(valueOne) + 1));
						preMap.put(cityName, object);
					} else {
						preNum = 0;
						dcd.setValueOne("1");
						preMap.put(cityName, dcd);
					}
				}
			}
			// 周活跃数
			Map<String, Object> weekMap = new HashMap<>();
			for (Iterator<DBObject> it = weekAo.results().iterator(); it.hasNext();) { // 昨日活跃
				BasicDBObject dbo = (BasicDBObject) it.next();
				BasicDBObject keyValus = (BasicDBObject) dbo.get("_id");
				String cityName = keyValus.getString(keyValu);
				DataCommDto dcd = new DataCommDto();
				// 有效次数
				Integer validCount = Integer.valueOf(dbo.get("actNum").toString());
				if (validCount >= 5) {
					if (preMap.containsKey(cityName)) {
						DataCommDto object = (DataCommDto) preMap.get(cityName);
						String valueTwo = object.getValueTwo();
						object.setValueTwo(String.valueOf(Integer.valueOf(valueTwo) + 1));
						preMap.put(cityName, object);
					} else {
						dcd.setValueTwo("1");
						preMap.put(cityName, dcd);
					}
				}
			}
			// 月活跃数
			Map<String, Object> monthMap = new HashMap<>();
			for (Iterator<DBObject> it = monthAo.results().iterator(); it.hasNext();) { // 昨日活跃
				BasicDBObject dbo = (BasicDBObject) it.next();
				BasicDBObject keyValus = (BasicDBObject) dbo.get("_id");
				String cityName = keyValus.getString(keyValu);
				DataCommDto dcd = new DataCommDto();
				// 有效次数
				Integer validCount = Integer.valueOf(dbo.get("actNum").toString());
				if (validCount >= 10) {
					if (preMap.containsKey(cityName)) {
						DataCommDto object = (DataCommDto) preMap.get(cityName);
						String valueThr = object.getValueThr();
						object.setValueThr(String.valueOf(Integer.valueOf(valueThr) + 1));
						preMap.put(cityName, object);
					} else {
						dcd.setValueThr("1");
						preMap.put(cityName, dcd);
					}
				}
			}
			for(Map.Entry<String, Object> entry : preMap.entrySet()) {
				Map<String, Object> map = new HashMap<>();
				String name = entry.getKey();
				DataCommDto value = (DataCommDto) entry.getValue();
				map.put("name", name);
				map.put("preNum", value.getValueOne());
				map.put("weekNum", value.getValueTwo());
				map.put("monthNum", value.getValueThr());
				table.add(map);
			}
			result.setRows(table);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			// 清空临时总表
			mongoTemplate.dropCollection("dataCenter_zone_areaUse_table");
		}
		return result;
	}

	/**
	 * <p>
	 * Description: 各区域活跃空间分布
	 * <p>
	 * 
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 上午9:16:01
	 */
	@Override
	public ListResult<Map<String, Object>> queryAreaZoneDistUser(Map<String, Object> where) {
		ListResult<Map<String, Object>> result = new ListResult<>();
		try {
			List<Map<String, Object>> table = new ArrayList<>();
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoListsByZone();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_zone_areaDist_table").insert(list);
			// 过滤条件的gid
			Object gid = where.get("gid");
			// 层级
			String layerLevel = where.get("layerLevel").toString();
			String currDate = DateCenterHelper.currDate("yyyy-MM-dd");
			String monthDate = DateCenterHelper.preDate(-30);
			String groupStr = "";
			String monthMatchStr = "";
			String keyValu = "";
			if ("1".equals(layerLevel)) { // 海南省
				groupStr = "{$group:{_id:{'cityName':'$cityName'},actNum:{$sum:1}}}";
				monthMatchStr = "{$match:{'pdate':{'$gte':'" + monthDate + "', '$lt':'" + currDate + "'},provinceGid:'"
						+ gid + "'}}";
				keyValu = "cityName";
			} else if ("2".equals(layerLevel)) { // 市县
				groupStr = "{$group:{_id:{'townName':'$townName'},actNum:{$sum:1}}}";
				monthMatchStr = "{$match:{'pdate':{'$gte':'" + monthDate + "', '$lt':'" + currDate + "'},cityGid:'"
						+ gid + "'}}";
				keyValu = "townName";
			} else if ("3".equals(layerLevel)) { // 区
				groupStr = "{$group:{_id:{'schoolName':'$schoolName'},actNum:{$sum:1}}}";
				monthMatchStr = "{$match:{'pdate':{'$gte':'" + monthDate + "', '$lt':'" + currDate + "'},townGid:'"
						+ gid + "'}}";
				keyValu = "schoolName";
			}
			DBObject group = (DBObject) JSON.parse(groupStr);
			DBObject monthMatch = (DBObject) JSON.parse(monthMatchStr);
			AggregationOutput monthAo = mongoTemplate.getCollection("dataCenter_zone_areaDist_table")
					.aggregate(monthMatch, group);

			Integer preNum = 0;
			// 月活跃数
			Map<String, Object> monthMap = new HashMap<>();
			for (Iterator<DBObject> it = monthAo.results().iterator(); it.hasNext();) { // 昨日活跃
				BasicDBObject dbo = (BasicDBObject) it.next();
				BasicDBObject keyValus = (BasicDBObject) dbo.get("_id");
				String name = keyValus.getString(keyValu);
				// 有效次数
				Integer validCount = Integer.valueOf(dbo.get("actNum").toString());
				if (validCount >= 10) {
					if (monthMap.containsKey(name)) {
						monthMap.put(name, Integer.valueOf(monthMap.get(name).toString()) + 1);
					} else {
						preNum = 0;
						monthMap.put(name, 1);
					}
				}
			}
			for (Map.Entry<String, Object> entry : monthMap.entrySet()) {
				Map<String, Object> map = new HashMap<>();
				map.put("name", entry.getKey());
				map.put("value", entry.getValue());
				table.add(map);
			}
			result.setRows(table);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			// 清空临时总表
			mongoTemplate.dropCollection("dataCenter_zone_areaDist_table");
		}
		return result;
	}

	/**
	 * <p>
	 * Description: 各类型活跃空间分布
	 * <p>
	 * 
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 上午9:40:10
	 */
	@Override
	public ListResult<Map<String, Object>> queryTypeZoneUser(Map<String, Object> where) {
		ListResult<Map<String, Object>> result = new ListResult<>();
		try {
			List<Map<String, Object>> table = new ArrayList<>();
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoListsByTypeZone();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_zone_typeUser_table").insert(list);
			// 过滤条件的gid
			Object gid = where.get("gid");
			// 层级
			String layerLevel = where.get("layerLevel").toString();
			String currDate = DateCenterHelper.currDate("yyyy-MM-dd");
			String monthDate = DateCenterHelper.preDate(-30);
			String groupStr = "";
			String monthMatchStr = "";
			String keyValu = "roleFlag";
			if ("1".equals(layerLevel)) { // 海南省
				groupStr = "{$group:{_id:{'cityName':'$cityName','roleFlag':'$roleFlag','userRelName':'$userRelName'},actNum:{$sum:1}}}";
				monthMatchStr = "{$match:{'pdate':{'$gte':'" + monthDate + "', '$lt':'" + currDate + "'},provinceGid:'"
						+ gid + "'}}";
			} else if ("2".equals(layerLevel)) { // 市县
				groupStr = "{$group:{_id:{'townName':'$townName','roleFlag':'$roleFlag','userRelName':'$userRelName'},actNum:{$sum:1}}}";
				monthMatchStr = "{$match:{'pdate':{'$gte':'" + monthDate + "', '$lt':'" + currDate + "'},cityGid:'"
						+ gid + "'}}";
			} else if ("3".equals(layerLevel)) { // 区
				groupStr = "{$group:{_id:{'schoolName':'$schoolName','roleFlag':'$roleFlag','userRelName':'$userRelName'},actNum:{$sum:1}}}";
				monthMatchStr = "{$match:{'pdate':{'$gte':'" + monthDate + "', '$lt':'" + currDate + "'},townGid:'"
						+ gid + "'}}";
			}
			DBObject group = (DBObject) JSON.parse(groupStr);
			DBObject monthMatch = (DBObject) JSON.parse(monthMatchStr);
			AggregationOutput monthAo = mongoTemplate.getCollection("dataCenter_zone_typeUser_table")
					.aggregate(monthMatch, group);

			Integer preNum = 0;
			// 月活跃数
			Map<String, Object> monthMap = new HashMap<>();
			for (Iterator<DBObject> it = monthAo.results().iterator(); it.hasNext();) { // 昨日活跃
				BasicDBObject dbo = (BasicDBObject) it.next();
				BasicDBObject keyValus = (BasicDBObject) dbo.get("_id");
				String cityName = keyValus.getString(keyValu);
				// 有效次数
				Integer validCount = Integer.valueOf(dbo.get("actNum").toString());
				if (validCount >= 3) {
					if (monthMap.containsKey(cityName)) {
						monthMap.put(cityName, Integer.valueOf(monthMap.get(cityName).toString()) + 1);
					} else {
						preNum = 0;
						monthMap.put(cityName, 1);
					}
				}
			}
			System.out.println(monthMap);
			for (Map.Entry<String, Object> entry : monthMap.entrySet()) {
				Map<String, Object> map = new HashMap<>();
				map.put("name", entry.getKey());
				map.put("value", entry.getValue());
				table.add(map);
			}
			result.setRows(table);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			// 清空临时总表
			mongoTemplate.dropCollection("dataCenter_zone_typeUser_table");
		}
		return result;
	}

	/**
	 * <p>
	 * Description: 定时去存每一天的活跃数据(测试方法)
	 * <p>
	 * 
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月9日 上午11:05:38
	 */
	@Override
	public void insertMongoTimer() {
		try {
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoListsByTypeZone();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_zoneUseTrend_timer_table").insert(list);
			String currDate = DateCenterHelper.preDate(-1);
			String preDate = DateCenterHelper.preDate(-2);
			String weekDate = DateCenterHelper.preDate(-8);
			String monthDate = DateCenterHelper.preDate(-31);
			String preMatchStr = "{$match:{'pdate':{$regex:'^.*" + preDate + ".*$'}}}";
			String weekMatchStr = "{$match:{'pdate':{'$gte':'" + weekDate + "', '$lt':'" + currDate + "'}}}";
			String monthMatchStr = "{$match:{'pdate':{'$gte':'" + monthDate + "', '$lt':'" + currDate + "'}}}";
			String groupStr = "{$group:{_id:{'userRelName':'$userRelName','provinceGid':'$provinceGid',"
					+ "'provinceName':'$provinceName','cityGid':'$cityGid','cityName':'$cityName',"
					+ "'townGid':'$townGid','townName':'$townName','schoolGid':'$schoolGid',"
					+ "'schoolName':'$schoolName'},actNum:{$sum:1}}}";
			DBObject preMatch = (DBObject) JSON.parse(preMatchStr);
			DBObject weekMatch = (DBObject) JSON.parse(weekMatchStr);
			DBObject monthMatch = (DBObject) JSON.parse(monthMatchStr);
			DBObject group = (DBObject) JSON.parse(groupStr);
			Map<String, BasicDBObject> row = new HashMap<>();
			AggregationOutput preAo = mongoTemplate.getCollection("dataCenter_zoneUseTrend_timer_table")
					.aggregate(preMatch, group);
			int preNum = 0;
			for (Iterator<DBObject> it = preAo.results().iterator(); it.hasNext();) { // 昨日活跃
				BasicDBObject dbo = (BasicDBObject) it.next();
				BasicDBObject keyValus = (BasicDBObject) dbo.get("_id");
				String userRelName = keyValus.getString("userRelName");
				// 有效次数
				Integer validCount = Integer.valueOf(dbo.get("actNum").toString());
				if (validCount >= 1) {
					if (row.containsKey(userRelName)) {
						Integer num = Integer.valueOf(row.get(userRelName).get("preCount").toString()) + 1;
						row.get(userRelName).put("preCount", num);
						row.put(userRelName, row.get(userRelName));
					} else {
						preNum = 0;
						dbo.put("preCount", 1);
						dbo.put("weekCount", 0);
						dbo.put("monthCount", 0);
						row.put(userRelName, dbo);
					}
				}
			}
			AggregationOutput weekAo = mongoTemplate.getCollection("dataCenter_zoneUseTrend_timer_table")
					.aggregate(weekMatch, group);
			for (Iterator<DBObject> it = weekAo.results().iterator(); it.hasNext();) { // 昨日活跃
				BasicDBObject dbo = (BasicDBObject) it.next();
				BasicDBObject keyValus = (BasicDBObject) dbo.get("_id");
				String userRelName = keyValus.getString("userRelName");
				// 有效次数
				Integer validCount = Integer.valueOf(dbo.get("actNum").toString());
				if (validCount >= 5) {
					if (row.containsKey(userRelName)) {
						Integer num = Integer.valueOf(row.get(userRelName).get("weekCount").toString()) + 1;
						row.get(userRelName).put("weekCount", num);
						row.put(userRelName, row.get(userRelName));
					} else {
						preNum = 0;
						dbo.put("preCount", 0);
						dbo.put("weekCount", 1);
						dbo.put("monthCount", 0);
						row.put(userRelName, dbo);
					}
				}
			}
			AggregationOutput monthAo = mongoTemplate.getCollection("dataCenter_zoneUseTrend_timer_table")
					.aggregate(monthMatch, group);
			for (Iterator<DBObject> it = monthAo.results().iterator(); it.hasNext();) { // 昨日活跃
				BasicDBObject dbo = (BasicDBObject) it.next();
				BasicDBObject keyValus = (BasicDBObject) dbo.get("_id");
				String userRelName = keyValus.getString("userRelName");
				// 有效次数
				Integer validCount = Integer.valueOf(dbo.get("actNum").toString());
				if (validCount >= 10) {
					if (row.containsKey(userRelName)) {
						Integer num = Integer.valueOf(row.get(userRelName).get("monthCount").toString()) + 1;
						row.get(userRelName).put("monthCount", num);
						row.put(userRelName, row.get(userRelName));
					} else {
						preNum = 0;
						dbo.put("preCount", 0);
						dbo.put("weekCount", 0);
						dbo.put("monthCount", 1);
						row.put(userRelName, dbo);
					}
				}
			}
			List<BasicDBObject> dbList = new ArrayList<>();
			for (Map.Entry<String, BasicDBObject> entry : row.entrySet()) {
				BasicDBObject value = entry.getValue();
				BasicDBObject keyValus = (BasicDBObject) value.get("_id");
				keyValus.put("preCount", value.get("preCount"));
				keyValus.put("weekCount", value.get("weekCount"));
				keyValus.put("monthCount", value.get("monthCount"));
				keyValus.put("pdate", currDate);
				dbList.add(keyValus);
			}
			mongoTemplate.getCollection("zoneUseTrend_timer_tmp").insert(dbList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			// 清空临时总表
			mongoTemplate.dropCollection("dataCenter_zoneUseTrend_timer_table");
		}
	}

	/**
	 * <p>
	 * Description: 空间使用趋势分析
	 * <p>
	 * 
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 上午10:01:12
	 */
	@Override
	public ListResult<Map<String, Object>> queryZoneUseTrend(Map<String, Object> where) {
		ListResult<Map<String, Object>> result = new ListResult<>();
		try {
			// 过滤条件的gid
			Object gid = where.get("gid");
			Object timeType = where.get("timeType");
			Object schoolGid = where.get("schoolGid");
			// 层级
			String layerLevel = where.get("layerLevel").toString();
			String currDate = DateCenterHelper.currDate("yyyy-MM-dd");
			Map<Object, String> dateAreaMap = null;
			Map<Object, String> dateMonthList = null;
			String monthDate = "";
			Integer arrSize = 0; // 半年或一年的数组大小
			if ("1".equals(timeType)) {
				monthDate = DateCenterHelper.preDate(-30);
				dateMonthList = DateCenterHelper.dateMonthList();
			} else if ("2".equals(timeType)) {
				dateAreaMap = DateCenterHelper.dateHalfYearList();
				arrSize = 26;
			} else if ("3".equals(timeType)) {
				dateAreaMap = DateCenterHelper.dateYearList();
				arrSize = 52;
			}
			List<Map<String, Object>> table = new ArrayList<>();
			// 半年或一年
			if ("2".equals(timeType) || "3".equals(timeType)) {
				String groupYearStr = "";
				String matchYearStr = "";
				for (Map.Entry<Object, String> entry : dateAreaMap.entrySet()) {
					String beginDate = entry.getValue();
					String endDate = "";
					if (StringHelper.IsEmptyOrNull(dateAreaMap.get(arrSize - 1))) {
						endDate = currDate;
					} else {
						endDate = dateAreaMap.get(arrSize - 1);
					}
					arrSize -= 1;
					if (StringHelper.IsEmptyOrNull(schoolGid)) {
						if ("1".equals(layerLevel)) { // 海南省
							groupYearStr = "{$group:{_id:{'pdate':'$pdate'},actNum:{$sum:1},preNum:{$sum:'$preCount'}"
									+ ",weekNum:{$sum:'$weekCount'},monthNum:{$sum:'$monthCount'}}}";
							matchYearStr = "{$match:{'pdate':{'$gte':'" + beginDate + "', '$lt':'" + endDate
									+ "'},provinceGid:'" + gid + "'}}";
						} else if ("2".equals(layerLevel)) { // 市县
							groupYearStr = "{$group:{_id:{'pdate':'$pdate'},actNum:{$sum:1},preNum:{$sum:'$preCount'}"
									+ ",weekNum:{$sum:'$weekCount'},monthNum:{$sum:'$monthCount'}}}";
							matchYearStr = "{$match:{'pdate':{'$gte':'" + beginDate + "', '$lt':'" + endDate
									+ "'},cityGid:'" + gid + "'}}";
						} else if ("3".equals(layerLevel)) { // 区
							groupYearStr = "{$group:{_id:{'pdate':'$pdate'},actNum:{$sum:1},preNum:{$sum:'$preCount'}"
									+ ",weekNum:{$sum:'$weekCount'},monthNum:{$sum:'$monthCount'}}}";
							matchYearStr = "{$match:{'pdate':{'$gte':'" + beginDate + "', '$lt':'" + endDate
									+ "'},townGid:'" + gid + "'}}";
						} 
					} else {
						groupYearStr = "{$group:{_id:{'pdate':'$pdate'},actNum:{$sum:1},preNum:{$sum:'$preCount'}"
								+ ",weekNum:{$sum:'$weekCount'},monthNum:{$sum:'$monthCount'}}}";
						matchYearStr = "{$match:{'pdate':{'$gte':'" + beginDate + "', '$lt':'" + endDate
								+ "'},schoolGid:'" + schoolGid + "'}}";
					}
					DBObject group = (DBObject) JSON.parse(groupYearStr);
					DBObject match = (DBObject) JSON.parse(matchYearStr);
					AggregationOutput monthAo = mongoTemplate.getCollection("zoneUseTrend_timer_tmp").aggregate(match,
							group);
					int preNumSum = 0;
					int weekNumSum = 0;
					int monthNumSum = 0;
					for (Iterator<DBObject> it = monthAo.results().iterator(); it.hasNext();) {
						BasicDBObject dbo = (BasicDBObject) it.next();
						Integer preNum = Integer.valueOf(dbo.get("preNum").toString());
						Integer weekNum = Integer.valueOf(dbo.get("weekNum").toString());
						Integer monthNum = Integer.valueOf(dbo.get("monthNum").toString());
						preNumSum += preNum;
						weekNumSum += weekNum;
						monthNumSum += monthNum;
					}
					Map<String, Object> map = new HashMap<>();
					String areaDate = beginDate + "至" + endDate;
					map.put("name", areaDate);
					map.put("preNum", preNumSum);
					map.put("weekNum", weekNumSum);
					map.put("monthNum", monthNumSum);
					table.add(map);
				}
				result.setRows(table);
			} else { // 一个月
				String groupStr = "";
				String monthMatchStr = "";
				for (Map.Entry<Object, String> entry : dateMonthList.entrySet()) {
					String date = entry.getValue();
					if (StringHelper.IsEmptyOrNull(schoolGid)) {
						if ("1".equals(layerLevel)) { // 海南省
							groupStr = "{$group:{_id:{'pdate':'$pdate'},actNum:{$sum:1},preNum:{$sum:'$preCount'}"
									+ ",weekNum:{$sum:'$weekCount'},monthNum:{$sum:'$monthCount'}}}";
							monthMatchStr = "{$match:{'pdate':{'$gte':'" + date + "', '$lte':'" + date + "'},provinceGid:'"
									+ gid + "'}}";
						} else if ("2".equals(layerLevel)) { // 市县
							groupStr = "{$group:{_id:{'pdate':'$pdate'},actNum:{$sum:1},preNum:{$sum:'$preCount'}"
									+ ",weekNum:{$sum:'$weekCount'},monthNum:{$sum:'$monthCount'}}}";
							monthMatchStr = "{$match:{'pdate':{'$gte':'" + date + "', '$lte':'" + date + "'},cityGid:'"
									+ gid + "'}}";
						} else if ("3".equals(layerLevel)) { // 区
							groupStr = "{$group:{_id:{'pdate':'$pdate'},actNum:{$sum:1},preNum:{$sum:'$preCount'}"
									+ ",weekNum:{$sum:'$weekCount'},monthNum:{$sum:'$monthCount'}}}";
							monthMatchStr = "{$match:{'pdate':{'$gte':'" + date + "', '$lte':'" + date + "'},townGid:'"
									+ gid + "'}}";
						} 
					} else {
						groupStr = "{$group:{_id:{'pdate':'$pdate'},actNum:{$sum:1},preNum:{$sum:'$preCount'}"
								+ ",weekNum:{$sum:'$weekCount'},monthNum:{$sum:'$monthCount'}}}";
						monthMatchStr = "{$match:{'pdate':{'$gte':'" + date + "', '$lte':'" + date + "'},schoolGid:'"
								+ schoolGid + "'}}";
					}
					DBObject group = (DBObject) JSON.parse(groupStr);
					DBObject monthMatch = (DBObject) JSON.parse(monthMatchStr);
					AggregationOutput monthAo = mongoTemplate.getCollection("zoneUseTrend_timer_tmp")
							.aggregate(monthMatch, group);
					if (StringHelper.IsEmptyOrNull(monthAo.results())) {
						Map<String, Object> map = new HashMap<>();
						map.put("name", date);
						map.put("preNum", 0);
						map.put("weekNum", 0);
						map.put("monthNum", 0);
						table.add(map);
					} else {
						for (Iterator<DBObject> it = monthAo.results().iterator(); it.hasNext();) {
							BasicDBObject dbo = (BasicDBObject) it.next();
							BasicDBObject keyValus = (BasicDBObject) dbo.get("_id");
							Map<String, Object> map = new HashMap<>();
							map.put("name", keyValus.get("pdate"));
							map.put("preNum", dbo.get("preNum"));
							map.put("weekNum", dbo.get("weekNum"));
							map.put("monthNum", dbo.get("monthNum"));
							table.add(map);
						}
					}
				}
				result.setRows(table);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>定时任务方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**
	 * <p>
	 * Description: 定时去存每一天的活跃数据(测试方法)
	 * <p>
	 * 
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月9日 上午11:05:38
	 */
	@Scheduled(cron = "0 0 3 * * ?")
	public void insertZoneUseMongoTimer() {
		try {
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoListsByTypeZone();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_zoneUseTrend_timer_table").insert(list);
			String currDate = DateCenterHelper.preDate(-1);
			String preDate = DateCenterHelper.preDate(-2);
			String weekDate = DateCenterHelper.preDate(-8);
			String monthDate = DateCenterHelper.preDate(-31);
			String preMatchStr = "{$match:{'pdate':{$regex:'^.*" + preDate + ".*$'}}}";
			String weekMatchStr = "{$match:{'pdate':{'$gte':'" + weekDate + "', '$lt':'" + currDate + "'}}}";
			String monthMatchStr = "{$match:{'pdate':{'$gte':'" + monthDate + "', '$lt':'" + currDate + "'}}}";
			String groupStr = "{$group:{_id:{'userRelName':'$userRelName','provinceGid':'$provinceGid',"
					+ "'provinceName':'$provinceName','cityGid':'$cityGid','cityName':'$cityName',"
					+ "'townGid':'$townGid','townName':'$townName','schoolGid':'$schoolGid',"
					+ "'schoolName':'$schoolName'},actNum:{$sum:1}}}";
			DBObject preMatch = (DBObject) JSON.parse(preMatchStr);
			DBObject weekMatch = (DBObject) JSON.parse(weekMatchStr);
			DBObject monthMatch = (DBObject) JSON.parse(monthMatchStr);
			DBObject group = (DBObject) JSON.parse(groupStr);
			Map<String, BasicDBObject> row = new HashMap<>();
			AggregationOutput preAo = mongoTemplate.getCollection("dataCenter_zoneUseTrend_timer_table")
					.aggregate(preMatch, group);
			int preNum = 0;
			for (Iterator<DBObject> it = preAo.results().iterator(); it.hasNext();) { // 昨日活跃
				BasicDBObject dbo = (BasicDBObject) it.next();
				BasicDBObject keyValus = (BasicDBObject) dbo.get("_id");
				String userRelName = keyValus.getString("userRelName");
				// 有效次数
				Integer validCount = Integer.valueOf(dbo.get("actNum").toString());
				if (validCount >= 1) {
					if (row.containsKey(userRelName)) {
						Integer num = Integer.valueOf(row.get(userRelName).get("preCount").toString()) + 1;
						row.get(userRelName).put("preCount", num);
						row.put(userRelName, row.get(userRelName));
					} else {
						preNum = 0;
						dbo.put("preCount", 1);
						dbo.put("weekCount", 0);
						dbo.put("monthCount", 0);
						row.put(userRelName, dbo);
					}
				}
			}
			AggregationOutput weekAo = mongoTemplate.getCollection("dataCenter_zoneUseTrend_timer_table")
					.aggregate(weekMatch, group);
			for (Iterator<DBObject> it = weekAo.results().iterator(); it.hasNext();) { // 昨日活跃
				BasicDBObject dbo = (BasicDBObject) it.next();
				BasicDBObject keyValus = (BasicDBObject) dbo.get("_id");
				String userRelName = keyValus.getString("userRelName");
				// 有效次数
				Integer validCount = Integer.valueOf(dbo.get("actNum").toString());
				if (validCount >= 5) {
					if (row.containsKey(userRelName)) {
						Integer num = Integer.valueOf(row.get(userRelName).get("weekCount").toString()) + 1;
						row.get(userRelName).put("weekCount", num);
						row.put(userRelName, row.get(userRelName));
					} else {
						preNum = 0;
						dbo.put("preCount", 0);
						dbo.put("weekCount", 1);
						dbo.put("monthCount", 0);
						row.put(userRelName, dbo);
					}
				}
			}
			AggregationOutput monthAo = mongoTemplate.getCollection("dataCenter_zoneUseTrend_timer_table")
					.aggregate(monthMatch, group);
			for (Iterator<DBObject> it = monthAo.results().iterator(); it.hasNext();) { // 昨日活跃
				BasicDBObject dbo = (BasicDBObject) it.next();
				BasicDBObject keyValus = (BasicDBObject) dbo.get("_id");
				String userRelName = keyValus.getString("userRelName");
				// 有效次数
				Integer validCount = Integer.valueOf(dbo.get("actNum").toString());
				if (validCount >= 10) {
					if (row.containsKey(userRelName)) {
						Integer num = Integer.valueOf(row.get(userRelName).get("monthCount").toString()) + 1;
						row.get(userRelName).put("monthCount", num);
						row.put(userRelName, row.get(userRelName));
					} else {
						preNum = 0;
						dbo.put("preCount", 0);
						dbo.put("weekCount", 0);
						dbo.put("monthCount", 1);
						row.put(userRelName, dbo);
					}
				}
			}
			List<BasicDBObject> dbList = new ArrayList<>();
			for (Map.Entry<String, BasicDBObject> entry : row.entrySet()) {
				BasicDBObject value = entry.getValue();
				BasicDBObject keyValus = (BasicDBObject) value.get("_id");
				keyValus.put("preCount", value.get("preCount"));
				keyValus.put("weekCount", value.get("weekCount"));
				keyValus.put("monthCount", value.get("monthCount"));
				keyValus.put("pdate", currDate);
				dbList.add(keyValus);
			}
			mongoTemplate.getCollection("zoneUseTrend_timer_tmp").insert(dbList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			// 清空临时总表
			mongoTemplate.dropCollection("dataCenter_zoneUseTrend_timer_table");
		}
	}

	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>本类公用方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**
	 * <p>
	 * Description: 获取mongodb与空间相关的数据
	 * <p>
	 * 
	 * @return List<String>
	 * @date 2017年8月7日 下午5:20:44
	 */
	public List<DBObject> queryMongoListsByZone() {
		// 获取所有相关的数据表
		Set<String> collectionNames = mongoTemplate.getCollectionNames();
		ArrayList<String> tables = new ArrayList<>();
		for (String name : collectionNames) {
			if ("dplog".equals(name.substring(0, 5))) {
				tables.add(name);
			}
		}
		List<DBObject> list = new ArrayList<DBObject>();
		for (int i = 0; i < tables.size(); i++) {
			DBCursor find = mongoTemplate.getCollection(tables.get(i)).find();
			for (Iterator<DBObject> it = find.iterator(); it.hasNext();) {
				DBObject next = it.next();
				String module = next.get("module").toString();
				if ("教师/学生空间".equals(module) || "班级空间".equals(module) || "学校空间".equals(module)
						|| "机构空间".equals(module)) {
					String pdate = next.get("pdate").toString();
					String dateFormatTran = dateFormatTran(pdate);
					next.put("pdate", dateFormatTran);
					if ("教师/学生空间".equals(module)) {
						String roleFlag = next.get("roleFlag").toString();
						if (roleFlag.contains("001")) { // 说明是老师身份
							next.put("module", "教师空间");
						} else if (roleFlag.contains("002")) {
							next.put("module", "学生空间");
						}
					}
					list.add(next);
				}
			}
		}
		return list;
	}

	/**
	 * <p>
	 * Description: 获取mongodb与空间省份类型的数据
	 * <p>
	 * 
	 * @return List<DBObject>
	 * @date 2017年8月17日 上午9:34:18
	 */
	public List<DBObject> queryMongoListsByTypeZone() {
		List<DBObject> list = queryMongoListsByZone();
		List<DBObject> newList = new ArrayList<DBObject>();
		String roleFlag = "";
		for (int i = 0; i < list.size(); i++) {
			DBObject next = list.get(i);
			roleFlag = next.get("roleFlag").toString();
			if (roleFlag.contains("001")) {
				next.put("roleFlag", "教师");
			} else if (roleFlag.contains("002")) {
				next.put("roleFlag", "学生");
			} else if (roleFlag.contains("003")) {
				next.put("roleFlag", "家长");
			} else {
				next.put("roleFlag", "其他");
			}
			newList.add(next);
		}
		return newList;
	}

	/**
	 * <p>
	 * Description: 日期格式转换
	 * <p>
	 * 
	 * @param parseTime
	 * @return
	 * @throws ParseException
	 *             String
	 * @date 2017-6-27 下午4:07:35
	 */
	public static String dateFormatTran(String parseTime) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS");
		Date parse = null;
		try {
			parse = sf.parse(parseTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long time = parse.getTime();
		Date date = new Date(time);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = format.format(date);
		return dateString;
	}

}
