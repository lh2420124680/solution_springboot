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
import org.springframework.stereotype.Repository;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.zlb.ecp.data.idao.IAppUseAnalyDao;
import com.zlb.ecp.helper.DateCenterHelper;
import com.zlb.ecp.helper.StringHelper;
import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 应用使用分析
 * @author Jane.Luo
 * @date 2017年8月17日 下午2:16:06
 * @version V1.0
 */
@Repository
public class AppUseAnalyDao implements IAppUseAnalyDao {

	private static final Logger log = LoggerFactory.getLogger(UserUseStatDao.class);

	@Autowired
	private MongoOperations mongoTemplate;
	
	/**
	 * <p>
	 * Description: 各应用使用人数趋势分析
	 * <p>
	 * 
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 下午2:18:12
	 */
	@Override
	public ListResult<Map<String, Object>> queryAppUsePeoNumTrend(Map<String, Object> where) {
		ListResult<Map<String, Object>> result = new ListResult<>();
		try {
			// 过滤条件的gid
			Object gid = where.get("gid");
			Object schoolGid = where.get("schoolGid");
			// 层级
			String layerLevel = where.get("layerLevel").toString();
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoListsByApp();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_app_usePeoNumTrend_table").insert(list);
			// 应用编码
			Object appCode = where.get("appCode");
			// 时间类型
			String timeType = where.get("timeType").toString();
			String currDate = DateCenterHelper.currDate("yyyy-MM-dd");
			Map<Object, String> dateAreaMap = null;
			Integer arrSize = 0; // 半年或一年的数组大小
			if ("1".equals(timeType)) {
				dateAreaMap = DateCenterHelper.dateMonthList();
			} else if ("2".equals(timeType)) {
				dateAreaMap = DateCenterHelper.dateHalfYearList();
				arrSize = 26;
			} else if ("3".equals(timeType)) {
				dateAreaMap = DateCenterHelper.dateYearList();
				arrSize = 52;
			}
			List<Map<String, Object>> table = new ArrayList<>();
			if ("2".equals(timeType) || "3".equals(timeType)) {
				String groupStr = "";
				String matchStr = "";
				for (Map.Entry<Object, String> entry : dateAreaMap.entrySet()) {
					String beginDate = entry.getValue();
					String endDate = "";
					if (StringHelper.IsEmptyOrNull(dateAreaMap.get(arrSize - 1))) {
						endDate = currDate;
					} else {
						endDate = dateAreaMap.get(arrSize - 1);
					}
					arrSize -= 1;
					groupStr = "{$group:{_id:{'pdate':'$pdate','cityName':'$cityName','loginMail':'$loginMail'}}}";
					if (StringHelper.IsEmptyOrNull(appCode)) {
						if (StringHelper.IsEmptyOrNull(schoolGid)) {
							if ("1".equals(layerLevel)) {
								matchStr = "{$match:{pdate:{'$gte':'" + beginDate + "', '$lt':'" + endDate
										+ "'},provinceGid:'" + gid + "'}}";
							} else if ("2".equals(layerLevel)) {
								matchStr = "{$match:{pdate:{'$gte':'" + beginDate + "', '$lt':'" + endDate
										+ "'},cityGid:'" + gid + "'}}";
							} else if ("3".equals(layerLevel)) {
								matchStr = "{$match:{pdate:{'$gte':'" + beginDate + "', '$lt':'" + endDate
										+ "'},townGid:'" + gid + "'}}";
							}
						} else {
							matchStr = "{$match:{pdate:{'$gte':'" + beginDate + "', '$lt':'" + endDate
									+ "'},schoolGid:'" + schoolGid + "'}}";
						}
					} else {
						if (StringHelper.IsEmptyOrNull(schoolGid)) {
							if ("1".equals(layerLevel)) {
								matchStr = "{$match:{pparam:{$regex:'^.*" + appCode + ".*$'},pdate:{'$gte':'" + beginDate
										+ "', '$lt':'" + endDate + "'},provinceGid:'" + gid + "'}}";
							} else if ("2".equals(layerLevel)) {
								matchStr = "{$match:{pparam:{$regex:'^.*" + appCode + ".*$'},pdate:{'$gte':'" + beginDate
										+ "', '$lt':'" + endDate + "'},cityGid:'" + gid + "'}}";
							} else if ("3".equals(layerLevel)) {
								matchStr = "{$match:{pparam:{$regex:'^.*" + appCode + ".*$'},pdate:{'$gte':'" + beginDate
										+ "', '$lt':'" + endDate + "'},townGid:'" + gid + "'}}";
							}
						} else {
							matchStr = "{$match:{pparam:{$regex:'^.*" + appCode + ".*$'},pdate:{'$gte':'" + beginDate
									+ "', '$lt':'" + endDate + "'},schoolGid:'" + schoolGid + "'}}";
						}
					}
					DBObject group = (DBObject) JSON.parse(groupStr);
					DBObject match = (DBObject) JSON.parse(matchStr);
					AggregationOutput ao = mongoTemplate.getCollection("dataCenter_app_usePeoNumTrend_table")
							.aggregate(match, group);
					Integer allNum = 0;
					for (Iterator<DBObject> it = ao.results().iterator(); it.hasNext();it.next()) {
						allNum += 1;
					}
					Map<String, Object> map = new HashMap<>();
					String areaDate = beginDate + "至" + endDate;
					map.put("name", areaDate);
					map.put("value", allNum);
					table.add(map);
				}
			} else { // 一个月
				String groupStr = "";
				String matchStr = "";
				for (Map.Entry<Object, String> entry : dateAreaMap.entrySet()) {
					String date = entry.getValue();
					groupStr = "{$group:{_id:{'pdate':'$pdate','cityName':'$cityName','loginMail':'$loginMail'}}}";
					if (StringHelper.IsEmptyOrNull(appCode)) {
						if (StringHelper.IsEmptyOrNull(schoolGid)) {
							if ("1".equals(layerLevel)) {
								matchStr = "{$match:{pdate:{'$gte':'" + date + "', '$lte':'" + date + "'},provinceGid:'" + gid + "'}}";
							} else if ("2".equals(layerLevel)) {
								matchStr = "{$match:{pdate:{'$gte':'" + date + "', '$lte':'" + date + "'},cityGid:'" + gid + "'}}";
							} else if ("3".equals(layerLevel)) {
								matchStr = "{$match:{pdate:{'$gte':'" + date + "', '$lte':'" + date + "'},townGid:'" + gid + "'}}";
							} 
						} else {
							matchStr = "{$match:{pdate:{'$gte':'" + date + "', '$lte':'" + date + "'},schoolGid:'" + schoolGid + "'}}";
						}
					} else {
						if (StringHelper.IsEmptyOrNull(schoolGid)) {
							if ("1".equals(layerLevel)) {
								matchStr = "{$match:{pparam:{$regex:'^.*" + appCode + ".*$'},pdate:{'$gte':'" + date
										+ "', '$lte':'" + date + "'},provinceGid:'" + gid + "'}}";
							} else if ("2".equals(layerLevel)) {
								matchStr = "{$match:{pparam:{$regex:'^.*" + appCode + ".*$'},pdate:{'$gte':'" + date
										+ "', '$lte':'" + date + "'},cityGid:'" + gid + "'}}";
							} else if ("3".equals(layerLevel)) {
								matchStr = "{$match:{pparam:{$regex:'^.*" + appCode + ".*$'},pdate:{'$gte':'" + date
										+ "', '$lte':'" + date + "'},townGid:'" + gid + "'}}";
							} 
						} else {
							matchStr = "{$match:{pparam:{$regex:'^.*" + appCode + ".*$'},pdate:{'$gte':'" + date
									+ "', '$lte':'" + date + "'},schoolGid:'" + schoolGid + "'}}";
						}
					}
					DBObject group = (DBObject) JSON.parse(groupStr);
					DBObject match = (DBObject) JSON.parse(matchStr);
					AggregationOutput ao = mongoTemplate.getCollection("dataCenter_app_usePeoNumTrend_table")
							.aggregate(match, group);
					Integer allNum = 0;
					for (Iterator<DBObject> it = ao.results().iterator(); it.hasNext();it.next()) {
						allNum += 1;
					}
					Map<String, Object> map = new HashMap<>();
					map.put("name", date);
					map.put("value", allNum);
					table.add(map);
				}
			}
			result.setRows(table);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			// 清空临时总表
			mongoTemplate.dropCollection("dataCenter_app_usePeoNumTrend_table");
		}
		return result;
	}

	/**
	 * <p>
	 * Description: 各应用使用次数趋势分析
	 * <p>
	 * 
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 下午2:18:12
	 */
	@Override
	public ListResult<Map<String, Object>> queryAppUseNumTrend(Map<String, Object> where) {
		ListResult<Map<String, Object>> result = new ListResult<>();
		try {
			// 过滤条件的gid
			Object gid = where.get("gid");
			Object schoolGid = where.get("schoolGid");
			// 层级
			String layerLevel = where.get("layerLevel").toString();
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoListsByApp();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_app_useNumTrend_table").insert(list);
			// 应用编码
			Object appCode = where.get("appCode");
			// 时间类型
			String timeType = where.get("timeType").toString();
			String currDate = DateCenterHelper.currDate("yyyy-MM-dd");
			Map<Object, String> dateAreaMap = null;
			Integer arrSize = 0; // 半年或一年的数组大小
			if ("1".equals(timeType)) {
				dateAreaMap = DateCenterHelper.dateMonthList();
			} else if ("2".equals(timeType)) {
				dateAreaMap = DateCenterHelper.dateHalfYearList();
				arrSize = 26;
			} else if ("3".equals(timeType)) {
				dateAreaMap = DateCenterHelper.dateYearList();
				arrSize = 52;
			}
			List<Map<String, Object>> table = new ArrayList<>();
			if ("2".equals(timeType) || "3".equals(timeType)) {
				String groupStr = "";
				String matchStr = "";
				for (Map.Entry<Object, String> entry : dateAreaMap.entrySet()) {
					String beginDate = entry.getValue();
					String endDate = "";
					if (StringHelper.IsEmptyOrNull(dateAreaMap.get(arrSize - 1))) {
						endDate = currDate;
					} else {
						endDate = dateAreaMap.get(arrSize - 1);
					}
					arrSize -= 1;
					groupStr = "{$group:{_id:{'pdate':'$pdate'},actNum:{$sum:1}}}";
					if (StringHelper.IsEmptyOrNull(appCode)) {
						if (StringHelper.IsEmptyOrNull(schoolGid)) {
							if ("1".equals(layerLevel)) {
								matchStr = "{$match:{pdate:{'$gte':'" + beginDate + "', '$lt':'" + endDate
										+ "'},provinceGid:'" + gid + "'}}";
							} else if ("2".equals(layerLevel)) {
								matchStr = "{$match:{pdate:{'$gte':'" + beginDate + "', '$lt':'" + endDate
										+ "'},cityGid:'" + gid + "'}}";
							} else if ("3".equals(layerLevel)) {
								matchStr = "{$match:{pdate:{'$gte':'" + beginDate + "', '$lt':'" + endDate
										+ "'},townGid:'" + gid + "'}}";
							}
						} else {
							matchStr = "{$match:{pdate:{'$gte':'" + beginDate + "', '$lt':'" + endDate
									+ "'},schoolGid:'" + schoolGid + "'}}";
						}
					} else {
						if (StringHelper.IsEmptyOrNull(schoolGid)) {
							if ("1".equals(layerLevel)) {
								matchStr = "{$match:{pparam:{$regex:'^.*" + appCode + ".*$'},pdate:{'$gte':'" + beginDate
										+ "', '$lt':'" + endDate + "'},provinceGid:'" + gid + "'}}";
							} else if ("2".equals(layerLevel)) {
								matchStr = "{$match:{pparam:{$regex:'^.*" + appCode + ".*$'},pdate:{'$gte':'" + beginDate
										+ "', '$lt':'" + endDate + "'},cityGid:'" + gid + "'}}";
							} else if ("3".equals(layerLevel)) {
								matchStr = "{$match:{pparam:{$regex:'^.*" + appCode + ".*$'},pdate:{'$gte':'" + beginDate
										+ "', '$lt':'" + endDate + "'},townGid:'" + gid + "'}}";
							}
						} else {
							matchStr = "{$match:{pparam:{$regex:'^.*" + appCode + ".*$'},pdate:{'$gte':'" + beginDate
									+ "', '$lt':'" + endDate + "'},schoolGid:'" + schoolGid + "'}}";
						}
					}
					DBObject group = (DBObject) JSON.parse(groupStr);
					DBObject match = (DBObject) JSON.parse(matchStr);
					AggregationOutput ao = mongoTemplate.getCollection("dataCenter_app_useNumTrend_table")
							.aggregate(match, group);
					Integer allNum = 0;
					for (Iterator<DBObject> it = ao.results().iterator(); it.hasNext();) {
						BasicDBObject dbo = (BasicDBObject) it.next();
						Integer actNum = Integer.valueOf(dbo.get("actNum").toString());
						allNum += actNum;
					}
					Map<String, Object> map = new HashMap<>();
					String areaDate = beginDate + "至" + endDate;
					map.put("name", areaDate);
					map.put("value", allNum);
					table.add(map);
				}
			} else { // 一个月
				String groupStr = "";
				String matchStr = "";
				for (Map.Entry<Object, String> entry : dateAreaMap.entrySet()) {
					String date = entry.getValue();
					groupStr = "{$group:{_id:{'pdate':'$pdate'},actNum:{$sum:1}}}";
					if (StringHelper.IsEmptyOrNull(appCode)) {
						if (StringHelper.IsEmptyOrNull(schoolGid)) {
							if ("1".equals(layerLevel)) {
								matchStr = "{$match:{pdate:{'$gte':'" + date + "', '$lte':'" + date + "'},provinceGid:'" + gid + "'}}";
							} else if ("2".equals(layerLevel)) {
								matchStr = "{$match:{pdate:{'$gte':'" + date + "', '$lte':'" + date + "'},cityGid:'" + gid + "'}}";
							} else if ("3".equals(layerLevel)) {
								matchStr = "{$match:{pdate:{'$gte':'" + date + "', '$lte':'" + date + "'},townGid:'" + gid + "'}}";
							} 
						} else {
							matchStr = "{$match:{pdate:{'$gte':'" + date + "', '$lte':'" + date + "'},schoolGid:'" + schoolGid + "'}}";
						}
					} else {
						if (StringHelper.IsEmptyOrNull(schoolGid)) {
							if ("1".equals(layerLevel)) {
								matchStr = "{$match:{pparam:{$regex:'^.*" + appCode + ".*$'},pdate:{'$gte':'" + date
										+ "', '$lte':'" + date + "'},provinceGid:'" + gid + "'}}";
							} else if ("2".equals(layerLevel)) {
								matchStr = "{$match:{pparam:{$regex:'^.*" + appCode + ".*$'},pdate:{'$gte':'" + date
										+ "', '$lte':'" + date + "'},cityGid:'" + gid + "'}}";
							} else if ("3".equals(layerLevel)) {
								matchStr = "{$match:{pparam:{$regex:'^.*" + appCode + ".*$'},pdate:{'$gte':'" + date
										+ "', '$lte':'" + date + "'},townGid:'" + gid + "'}}";
							} 
						} else {
							matchStr = "{$match:{pparam:{$regex:'^.*" + appCode + ".*$'},pdate:{'$gte':'" + date
									+ "', '$lte':'" + date + "'},schoolGid:'" + schoolGid + "'}}";
						}
					}
					DBObject group = (DBObject) JSON.parse(groupStr);
					DBObject match = (DBObject) JSON.parse(matchStr);
					AggregationOutput ao = mongoTemplate.getCollection("dataCenter_app_useNumTrend_table")
							.aggregate(match, group);
					Integer allNum = 0;
					for (Iterator<DBObject> it = ao.results().iterator(); it.hasNext();) {
						BasicDBObject dbo = (BasicDBObject) it.next();
						Integer actNum = Integer.valueOf(dbo.get("actNum").toString());
						allNum += actNum;
					}
					Map<String, Object> map = new HashMap<>();
					map.put("name", date);
					map.put("value", allNum);
					table.add(map);
				}
			}
			result.setRows(table);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			// 清空临时总表
			mongoTemplate.dropCollection("dataCenter_app_useNumTrend_table");
		}
		return result;
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
	public List<DBObject> queryMongoListsByApp() {
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
				if ("应用模块".equals(module)) {
					String pdate = next.get("pdate").toString();
					String dateFormatTran = dateFormatTran(pdate);
					next.put("pdate", dateFormatTran);
					list.add(next);
				}
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
}
