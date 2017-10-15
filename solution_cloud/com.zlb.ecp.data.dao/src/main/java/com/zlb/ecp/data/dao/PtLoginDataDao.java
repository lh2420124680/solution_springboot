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

import com.alibaba.fastjson.JSONObject;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.zlb.ecp.data.idao.IPtLoginDataDao;
import com.zlb.ecp.helper.DateCenterHelper;
import com.zlb.ecp.helper.StringHelper;
import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 平台登录数据
 * @author Jane.Luo
 * @date 2017年8月9日 下午2:32:04
 * @version V1.0
 */
@Repository
public class PtLoginDataDao implements IPtLoginDataDao {

	private static final Logger log = LoggerFactory.getLogger(UserUseStatDao.class);

	@Autowired
	private MongoOperations mongoTemplate;

	/**
	 * <p>
	 * Description: 昨日登录量
	 * <p>
	 * 
	 * @param where
	 * @return Integer
	 * @date 2017年8月9日 下午2:02:55
	 */
	@Override
	public Integer preLoginNum(Map<String, Object> where) {
		Integer allNum = 0;
		try {
			Object layerLevel = where.get("layerLevel");
			Object gid = where.get("gid");
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoLists();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_login_pre_table").insert(list);
			String preDate = DateCenterHelper.preDate(-1);
			String groupStr = "{$group:{_id:{'userRelName':'$userRelName'}}}";
			String matchStr = "{$match:{pdate:{$regex:'^.*" + preDate + ".*$'},module:'登录模块'}}";
			DBObject group = (DBObject) JSON.parse(groupStr);
			DBObject match = (DBObject) JSON.parse(matchStr);
			AggregationOutput ao = mongoTemplate.getCollection("dataCenter_login_pre_table").aggregate(match, group);
			for (Iterator<DBObject> it = ao.results().iterator(); it.hasNext(); it.next()) {
				allNum += 1;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			// 清空临时总表
			mongoTemplate.dropCollection("dataCenter_login_pre_table");
		}
		return allNum;
	}

	/**
	 * <p>
	 * Description: 本周登录量
	 * <p>
	 * 
	 * @param where
	 * @return Integer
	 * @date 2017年8月9日 下午2:15:27
	 */
	@Override
	public Integer weekLoginNum(Map<String, Object> where) {
		Integer allNum = 0;
		try {
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoLists();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_login_week_table").insert(list);
			// 获取当前日期的前七天
			String weekTable = DateCenterHelper.preDate(-7);
			// 获取当前的天
			String currDataTable = DateCenterHelper.currDate("yyyy-MM-dd");
			String groupStr = "{$group:{_id:{'userRelName':'$userRelName'}}}";
			String matchStr = "{$match:{'pdate':{'$gte':'" + weekTable + "', '$lt':'" + currDataTable
					+ "'},module:'登录模块'}}";
			DBObject group = (DBObject) JSON.parse(groupStr);
			DBObject match = (DBObject) JSON.parse(matchStr);
			AggregationOutput ao = mongoTemplate.getCollection("dataCenter_login_week_table").aggregate(match, group);
			for (Iterator<DBObject> it = ao.results().iterator(); it.hasNext(); it.next()) {
				allNum += 1;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			// 清空临时总表
			mongoTemplate.dropCollection("dataCenter_login_week_table");
		}
		return allNum;
	}

	/**
	 * <p>
	 * Description: 本月登录量
	 * <p>
	 * 
	 * @param where
	 * @return Integer
	 * @date 2017年8月9日 下午2:15:55
	 */
	@Override
	public Integer monthLoginNum(Map<String, Object> where) {
		Integer allNum = 0;
		try {
			Object layerLevel = where.get("layerLevel");
			Object gid = where.get("gid");
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoLists();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_login_month_table").insert(list);
			// 获取当前日期的前七天
			String monthTable = DateCenterHelper.preDate(-30);
			// 获取当前的天
			String currDataTable = DateCenterHelper.currDate("yyyy-MM-dd");
			String groupStr = "{$group:{_id:{'userRelName':'$userRelName'}}}";
			String matchStr = "{$match:{'pdate':{'$gte':'" + monthTable + "', '$lt':'" + currDataTable
					+ "'},module:'登录模块'}}";
			DBObject group = (DBObject) JSON.parse(groupStr);
			DBObject match = (DBObject) JSON.parse(matchStr);
			AggregationOutput ao = mongoTemplate.getCollection("dataCenter_login_month_table").aggregate(match, group);
			for (Iterator<DBObject> it = ao.results().iterator(); it.hasNext(); it.next()) {
				allNum += 1;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			// 清空临时总表
			mongoTemplate.dropCollection("dataCenter_login_month_table");
		}
		return allNum;
	}

	/**
	 * <p>
	 * Description: 累计登录量
	 * <p>
	 * 
	 * @param where
	 * @return Integer
	 * @date 2017年8月9日 下午2:31:44
	 */
	@Override
	public Integer totalLoginNum(Map<String, Object> where) {
		Integer allNum = 0;
		try {
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoLists();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_login_total_table").insert(list);
			String groupStr = "{$group:{_id:{'userRelName':'$userRelName'}}}";
			String matchStr = "{$match:{module:'登录模块'}}";
			DBObject group = (DBObject) JSON.parse(groupStr);
			DBObject match = (DBObject) JSON.parse(matchStr);
			AggregationOutput ao = mongoTemplate.getCollection("dataCenter_login_total_table").aggregate(match, group);
			for (Iterator<DBObject> it = ao.results().iterator(); it.hasNext(); it.next()) {
				allNum += 1;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			// 清空临时总表
			mongoTemplate.dropCollection("dataCenter_login_total_table");
		}
		return allNum;
	}

	/**
	 * <p>
	 * Description: 平台登录用户分布
	 * <p>
	 * 
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月9日 下午2:42:25
	 */
	@Override
	public ListResult<Map<String, Object>> queryLoginUserDist(Map<String, Object> where) {
		ListResult<Map<String, Object>> result = new ListResult<>();
		try {
			List<Map<String, Object>> table = new ArrayList<>();
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoListsByRole();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_use_userDist_table").insert(list);
			// 过滤条件的gid
			Object gid = where.get("gid");
			Object schoolGid = where.get("schoolGid");
			// 层级
			String layerLevel = where.get("layerLevel").toString();
			// 选择区间
			String timeType = where.get("timeType").toString();
			String currDate = DateCenterHelper.currDate("yyyy-MM-dd");
			String monthDate = "";
			if ("1".equals(timeType)) { // 进一个月
				monthDate = DateCenterHelper.preDate(-30); 
			} else if ("2".equals(timeType)) { // 进半年
				monthDate = DateCenterHelper.preDate(-182); 
			} else if ("3".equals(timeType)) { // 进一年
				monthDate = DateCenterHelper.preDate(-365); 
			}
			String groupStr = "";
			String monthMatchStr = "";
			String keyValu = "roleFlag";
			if (StringHelper.IsEmptyOrNull(schoolGid)) {
				if ("1".equals(layerLevel)) { // 海南省
					groupStr = "{$group:{_id:{'cityName':'$cityName','roleFlag':'$roleFlag','userRelName':'$userRelName'},actNum:{$sum:1}}}";
					monthMatchStr = "{$match:{'pdate':{'$gte':'" + monthDate + "', '$lt':'" + currDate + "'},provinceGid:'"
							+ gid + "',module:'登录模块'}}";
				} else if ("2".equals(layerLevel)) { // 市县
					groupStr = "{$group:{_id:{'townName':'$townName','roleFlag':'$roleFlag','userRelName':'$userRelName'},actNum:{$sum:1}}}";
					monthMatchStr = "{$match:{'pdate':{'$gte':'" + monthDate + "', '$lt':'" + currDate + "'},cityGid:'"
							+ gid + "',module:'登录模块'}}";
				} else if ("3".equals(layerLevel)) { // 区
					groupStr = "{$group:{_id:{'schoolName':'$schoolName','roleFlag':'$roleFlag','userRelName':'$userRelName'},actNum:{$sum:1}}}";
					monthMatchStr = "{$match:{'pdate':{'$gte':'" + monthDate + "', '$lt':'" + currDate + "'},townGid:'"
							+ gid + "',module:'登录模块'}}";
				} 
			} else {
				groupStr = "{$group:{_id:{'schoolName':'$schoolName','roleFlag':'$roleFlag','userRelName':'$userRelName'},actNum:{$sum:1}}}";
				monthMatchStr = "{$match:{'pdate':{'$gte':'" + monthDate + "', '$lt':'" + currDate + "'},schoolGid:'"
						+ schoolGid + "',module:'登录模块'}}";
			}
			DBObject group = (DBObject) JSON.parse(groupStr);
			DBObject monthMatch = (DBObject) JSON.parse(monthMatchStr);
			AggregationOutput monthAo = mongoTemplate.getCollection("dataCenter_use_userDist_table")
					.aggregate(monthMatch, group);

			Integer preNum = 0;
			// 登录数
			Map<String, Object> monthMap = new HashMap<>();
			for (Iterator<DBObject> it = monthAo.results().iterator(); it.hasNext();) { // 昨日活跃
				BasicDBObject dbo = (BasicDBObject) it.next();
				BasicDBObject keyValus = (BasicDBObject) dbo.get("_id");
				String roleFlag = keyValus.getString(keyValu);
				// 有效次数
				Integer validCount = Integer.valueOf(dbo.get("actNum").toString());
				if (validCount >= 1) {
					if (monthMap.containsKey(roleFlag)) {
						monthMap.put(roleFlag, Integer.valueOf(monthMap.get(roleFlag).toString()) + 1);
					} else {
						preNum = 0;
						monthMap.put(roleFlag, 1);
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
			mongoTemplate.dropCollection("dataCenter_use_userDist_table");
		}
		return result;
	}
	
	/**
	 * <p>Description:平台登录趋势定时任务测试方法，主方法在下面<p>
	 * @date 2017年8月9日 下午5:02:12
	 */
	@Override
	public void insertLoginMongoTimer() {
		try {
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoListsByRole();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_trendLogin_timer_table").insert(list);
			String currDate = DateCenterHelper.preDate(-1);
			String preDate = DateCenterHelper.preDate(-2);
			String weekDate = DateCenterHelper.preDate(-8);
			String monthDate = DateCenterHelper.preDate(-31);
			String preMatchStr = "{$match:{pdate:{$regex:'^.*" + preDate + ".*$'},module:'登录模块'}}";
			String weekMatchStr = "{$match:{'pdate':{'$gte':'" + weekDate + "', '$lt':'" + currDate
					+ "'},module:'登录模块'}}";
			String monthMatchStr = "{$match:{'pdate':{'$gte':'" + monthDate + "', '$lt':'" + currDate
					+ "'},module:'登录模块'}}";
			String groupStr = "{$group:{_id:{'userRelName':'$userRelName','provinceGid':'$provinceGid',"
					+ "'provinceName':'$provinceName','cityGid':'$cityGid','cityName':'$cityName',"
					+ "'townGid':'$townGid','townName':'$townName','schoolGid':'$schoolGid',"
					+ "'schoolName':'$schoolName'},actNum:{$sum:1}}}";
			DBObject preMatch = (DBObject) JSON.parse(preMatchStr);
			DBObject weekMatch = (DBObject) JSON.parse(weekMatchStr);
			DBObject monthMatch = (DBObject) JSON.parse(monthMatchStr);
			DBObject group = (DBObject) JSON.parse(groupStr);
			Map<String, BasicDBObject> row = new HashMap<>();
			AggregationOutput preAo = mongoTemplate.getCollection("dataCenter_trendLogin_timer_table").aggregate(preMatch,
					group);
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
			AggregationOutput weekAo = mongoTemplate.getCollection("dataCenter_trendLogin_timer_table").aggregate(weekMatch,
					group);
			for (Iterator<DBObject> it = weekAo.results().iterator(); it.hasNext();) { // 昨日活跃
				BasicDBObject dbo = (BasicDBObject) it.next();
				BasicDBObject keyValus = (BasicDBObject) dbo.get("_id");
				String userRelName = keyValus.getString("userRelName");
				// 有效次数
				Integer validCount = Integer.valueOf(dbo.get("actNum").toString());
				if (validCount >= 1) {
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
			AggregationOutput monthAo = mongoTemplate.getCollection("dataCenter_trendLogin_timer_table")
					.aggregate(monthMatch, group);
			for (Iterator<DBObject> it = monthAo.results().iterator(); it.hasNext();) { // 昨日活跃
				BasicDBObject dbo = (BasicDBObject) it.next();
				BasicDBObject keyValus = (BasicDBObject) dbo.get("_id");
				String userRelName = keyValus.getString("userRelName");
				// 有效次数
				Integer validCount = Integer.valueOf(dbo.get("actNum").toString());
				if (validCount >= 1) {
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
			System.out.println(JSONObject.toJSONString(row));

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
			mongoTemplate.getCollection("trendLogin_timer_tmp").insert(dbList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			// 清空临时总表
			mongoTemplate.dropCollection("dataCenter_trendLogin_timer_table");
		}
	}
	
	/**
	 * <p>Description: 平台登录趋势分析<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月10日 上午10:51:32
	 */
	public ListResult<Map<String, Object>> queryLoginTrend(Map<String, Object> where) {
		ListResult<Map<String, Object>> result = new ListResult<>();
		try {
			// 过滤条件的gid
			Object gid = where.get("gid");
			Object timeType = where.get("timeType");
			Object schoolGid = where.get("schoolGid");
			// 层级
			String layerLevel = where.get("layerLevel").toString();
			String currDate = DateCenterHelper.currDate("yyyy-MM-dd");
			String monthDate = "";
			Integer arrSize = 0; // 半年或一年的数组大小
			Map<Object, String> dateAreaMap = null;
			Map<Object, String> dateMonthList = null;
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
			if ("2".equals(timeType) || "3".equals(timeType)) {
				String groupYearStr = "";
				String matchYearStr = "";
				for(Map.Entry<Object, String> entry : dateAreaMap.entrySet()) {
					String beginDate = entry.getValue();
					String endDate = "";
					if (StringHelper.IsEmptyOrNull(dateAreaMap.get(arrSize-1))) {
						endDate = currDate;
					} else {
						endDate = dateAreaMap.get(arrSize-1);
					}
					arrSize -=1;
					if (StringHelper.IsEmptyOrNull(schoolGid)) {
						if ("1".equals(layerLevel)) { // 海南省
							groupYearStr = "{$group:{_id:{'pdate':'$pdate'},actNum:{$sum:1},preNum:{$sum:'$preCount'}"
									+ ",weekNum:{$sum:'$weekCount'},monthNum:{$sum:'$monthCount'}}}";
							matchYearStr = "{$match:{'pdate':{'$gte':'" + beginDate + "', '$lt':'" + endDate + "'},provinceGid:'"
									+ gid + "'}}";
						} else if ("2".equals(layerLevel)) { // 市县
							groupYearStr = "{$group:{_id:{'pdate':'$pdate'},actNum:{$sum:1},preNum:{$sum:'$preCount'}"
									+ ",weekNum:{$sum:'$weekCount'},monthNum:{$sum:'$monthCount'}}}";
							matchYearStr = "{$match:{'pdate':{'$gte':'" + beginDate + "', '$lt':'" + endDate + "'},cityGid:'"
									+ gid + "'}}";
						} else if ("3".equals(layerLevel)) { // 区
							groupYearStr = "{$group:{_id:{'pdate':'$pdate'},actNum:{$sum:1},preNum:{$sum:'$preCount'}"
									+ ",weekNum:{$sum:'$weekCount'},monthNum:{$sum:'$monthCount'}}}";
							matchYearStr = "{$match:{'pdate':{'$gte':'" + beginDate + "', '$lt':'" + endDate + "'},townGid:'"
									+ gid + "'}}";
						}
					} else {
						groupYearStr = "{$group:{_id:{'pdate':'$pdate'},actNum:{$sum:1},preNum:{$sum:'$preCount'}"
								+ ",weekNum:{$sum:'$weekCount'},monthNum:{$sum:'$monthCount'}}}";
						matchYearStr = "{$match:{'pdate':{'$gte':'" + beginDate + "', '$lt':'" + endDate + "'},schoolGid:'"
								+ schoolGid + "'}}";
					}
					DBObject group = (DBObject) JSON.parse(groupYearStr);
					DBObject match = (DBObject) JSON.parse(matchYearStr);
					AggregationOutput monthAo = mongoTemplate.getCollection("trendLogin_timer_tmp").aggregate(match, group);
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
			} else {
				String groupStr = "";
				String monthMatchStr = "";
				for(Map.Entry<Object, String> entry : dateMonthList.entrySet()) {
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
					AggregationOutput monthAo = mongoTemplate.getCollection("trendLogin_timer_tmp").aggregate(monthMatch,
							group);
					if (StringHelper.IsEmptyOrNull(monthAo.results())) {
						Map<String, Object> map = new HashMap<>();
						map.put("name", date);
						map.put("preNum", 0);
						map.put("weekNum", 0);
						map.put("monthNum", 0);
						table.add(map);
					} else {
						for (Iterator<DBObject> it = monthAo.results().iterator(); it.hasNext();) { // 近一个月
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

	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>本类公用方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**
	 * <p>
	 * Description: 获取mongodb所有的数据表
	 * <p>
	 * 
	 * @return List<String>
	 * @date 2017年8月7日 下午5:20:44
	 */
	public List<DBObject> queryMongoLists() {
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
				String pdate = next.get("pdate").toString();
				String dateFormatTran = dateFormatTran(pdate);
				next.put("pdate", dateFormatTran);
				list.add(next);
			}
		}
		return list;
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

	/**
	 * <p>
	 * Description: 获取mongodb所有的数据表(转换角色)
	 * <p>
	 * 
	 * @return List<String>
	 * @date 2017年8月7日 下午5:20:44
	 */
	public List<DBObject> queryMongoListsByRole() {
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
				String pdate = next.get("pdate").toString();
				String dateFormatTran = dateFormatTran(pdate);
				next.put("pdate", dateFormatTran);
				String roleFlag = next.get("roleFlag").toString();
				if (roleFlag.contains("001")) { // 说明是老师身份
					next.put("roleFlag", "教师");
				} else if (roleFlag.contains("002")) {
					next.put("roleFlag", "学生");
				} else if (roleFlag.contains("003")) {
					next.put("roleFlag", "家长");
				} else {
					next.put("roleFlag", "其他");
				}
				list.add(next);
			}
		}
		return list;
	}
	
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>定时任务<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**
	 * <p>Description:平台登录趋势定时任务<p>
	 * @date 2017年8月9日 下午5:02:12
	 */
	@Scheduled(cron="0 0 1 * * ?") 
	public void insertLoginScheduled() {
		try {
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoListsByRole();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_trendLogin_timer_table").insert(list);
			String currDate = DateCenterHelper.preDate(-1);
			String preDate = DateCenterHelper.preDate(-2);
			String weekDate = DateCenterHelper.preDate(-8);
			String monthDate = DateCenterHelper.preDate(-31);
			String preMatchStr = "{$match:{pdate:{$regex:'^.*" + preDate + ".*$'},module:'登录模块'}}";
			String weekMatchStr = "{$match:{'pdate':{'$gte':'" + weekDate + "', '$lt':'" + currDate
					+ "'},module:'登录模块'}}";
			String monthMatchStr = "{$match:{'pdate':{'$gte':'" + monthDate + "', '$lt':'" + currDate
					+ "'},module:'登录模块'}}";
			String groupStr = "{$group:{_id:{'userRelName':'$userRelName','provinceGid':'$provinceGid',"
					+ "'provinceName':'$provinceName','cityGid':'$cityGid','cityName':'$cityName',"
					+ "'townGid':'$townGid','townName':'$townName','schoolGid':'$schoolGid',"
					+ "'schoolName':'$schoolName'},actNum:{$sum:1}}}";
			DBObject preMatch = (DBObject) JSON.parse(preMatchStr);
			DBObject weekMatch = (DBObject) JSON.parse(weekMatchStr);
			DBObject monthMatch = (DBObject) JSON.parse(monthMatchStr);
			DBObject group = (DBObject) JSON.parse(groupStr);
			Map<String, BasicDBObject> row = new HashMap<>();
			AggregationOutput preAo = mongoTemplate.getCollection("dataCenter_trendLogin_timer_table").aggregate(preMatch,
					group);
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
			AggregationOutput weekAo = mongoTemplate.getCollection("dataCenter_trendLogin_timer_table").aggregate(weekMatch,
					group);
			for (Iterator<DBObject> it = weekAo.results().iterator(); it.hasNext();) { // 昨日活跃
				BasicDBObject dbo = (BasicDBObject) it.next();
				BasicDBObject keyValus = (BasicDBObject) dbo.get("_id");
				String userRelName = keyValus.getString("userRelName");
				// 有效次数
				Integer validCount = Integer.valueOf(dbo.get("actNum").toString());
				if (validCount >= 1) {
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
			AggregationOutput monthAo = mongoTemplate.getCollection("dataCenter_trendLogin_timer_table")
					.aggregate(monthMatch, group);
			for (Iterator<DBObject> it = monthAo.results().iterator(); it.hasNext();) { // 昨日活跃
				BasicDBObject dbo = (BasicDBObject) it.next();
				BasicDBObject keyValus = (BasicDBObject) dbo.get("_id");
				String userRelName = keyValus.getString("userRelName");
				// 有效次数
				Integer validCount = Integer.valueOf(dbo.get("actNum").toString());
				if (validCount >= 1) {
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
			System.out.println(JSONObject.toJSONString(row));

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
			mongoTemplate.getCollection("trendLogin_timer_tmp").insert(dbList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			// 清空临时总表
			mongoTemplate.dropCollection("dataCenter_trendLogin_timer_table");
		}
	}
	
}
