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
import com.zlb.ecp.data.idao.IAppDetailDao;
import com.zlb.ecp.helper.StringHelper;
import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 应用使用明细
 * @author Jane.Luo
 * @date 2017年8月17日 下午4:04:32
 * @version V1.0
 */
@Repository
public class AppDetailDao implements IAppDetailDao {

	private static final Logger log = LoggerFactory.getLogger(UserUseStatDao.class);

	@Autowired
	private MongoOperations mongoTemplate;
	
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
		Map<String, Object> newMap = new HashMap<>();
		try {
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoListsByAppDetail();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_appUsePeo_detail_table").insert(list);
			// 过滤条件的gid
			Object gid = where.get("gid");
			Object schoolGid = where.get("schoolGid");
			// 层级
			String layerLevel = where.get("layerLevel").toString();
			String groupStr = "{$group:{_id:{'pparam':'$pparam','type':'$type','loginMail':'$loginMail'}}}";
			String matchStr = "";
			if (StringHelper.IsEmptyOrNull(schoolGid)) {
				if ("1".equals(layerLevel)) {
					matchStr = "{$match:{provinceGid:'" + gid + "'}}";
				} else if ("2".equals(layerLevel)) { // 市县
					matchStr = "{$match:{cityGid:'" + gid + "'}}";
				} else if ("3".equals(layerLevel)) { // 区
					matchStr = "{$match:{townGid:'" + gid + "'}}";
				}
			} else { // 学校
				matchStr = "{$match:{schoolGid:'" + schoolGid + "'}}";
			}
			DBObject group = (DBObject) JSON.parse(groupStr);
			DBObject match = (DBObject) JSON.parse(matchStr);
			List<Map<String, Object>> table = new ArrayList<>();
			AggregationOutput ao = mongoTemplate.getCollection("dataCenter_appUsePeo_detail_table").aggregate(match, group);
			for (Iterator<DBObject> it = ao.results().iterator(); it.hasNext();) {
				BasicDBObject next = (BasicDBObject) it.next();
				BasicDBObject object = (BasicDBObject) next.get("_id");
				String name = object.get("pparam").toString();
				if (newMap.containsKey(name)) {
					Integer num = Integer.valueOf(newMap.get(name).toString());
					newMap.put(name, num + 1);
				} else {
					newMap.put(name, 1);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			// 清空临时总表
			mongoTemplate.dropCollection("dataCenter_appUsePeo_detail_table");
		}
		return newMap;
	}

	/**
	 * <p>
	 * Description: 应用使用明细
	 * <p>
	 * 
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 下午4:05:32
	 */
	@Override
	public ListResult<Map<String, Object>> queryAppDetail(Map<String, Object> where) {
		ListResult<Map<String, Object>> result = new ListResult<>();
		try {
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoListsByAppDetail();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_app_detail_table").insert(list);
			// 过滤条件的gid
			Object gid = where.get("gid");
			Object schoolGid = where.get("schoolGid");
			// 层级
			String layerLevel = where.get("layerLevel").toString();
			String groupStr = "{$group:{_id:{'pparam':'$pparam','type':'$type'},actNum:{$sum:1}}}";
			String matchStr = "";
			if (StringHelper.IsEmptyOrNull(schoolGid)) {
				if ("1".equals(layerLevel)) {
					matchStr = "{$match:{provinceGid:'" + gid + "'}}";
				} else if ("2".equals(layerLevel)) { // 市县
					matchStr = "{$match:{cityGid:'" + gid + "'}}";
				} else if ("3".equals(layerLevel)) { // 区
					matchStr = "{$match:{townGid:'" + gid + "'}}";
				}
			} else { // 学校
				matchStr = "{$match:{schoolGid:'" + schoolGid + "'}}";
			}
			DBObject group = (DBObject) JSON.parse(groupStr);
			DBObject match = (DBObject) JSON.parse(matchStr);
			List<Map<String, Object>> table = new ArrayList<>();
			AggregationOutput ao = mongoTemplate.getCollection("dataCenter_app_detail_table").aggregate(match, group);
			for (Iterator<DBObject> it = ao.results().iterator(); it.hasNext();) {
				BasicDBObject dbo = (BasicDBObject) it.next();
				BasicDBObject keyVals = (BasicDBObject) dbo.get("_id");
				Object appTypeName = keyVals.get("type");
				Object appName = keyVals.get("pparam");
				Object actNum = dbo.get("actNum");
				Map<String, Object> map = new HashMap<>();
				map.put("appTypeName", appTypeName);
				map.put("appName", appName);
				map.put("actNum", actNum);
				table.add(map);
			}
			result.setRows(table);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			// 清空临时总表
			mongoTemplate.dropCollection("dataCenter_app_detail_table");
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
	public List<DBObject> queryMongoListsByAppDetail() {
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
					String pparam = next.get("pparam").toString();
					com.alibaba.fastjson.JSONObject parseObject = com.alibaba.fastjson.JSON.parseObject(pparam);
					String appName = parseObject.get("appName").toString();
					String appTypeName = parseObject.get("appTypeName").toString();
					next.put("pparam", appName);
					next.put("type", appTypeName);
					list.add(next);
				}
			}
		}
		return list;
	}
	
	/**
	 * <p>
	 * Description: 获取mongodb与空间相关的数据
	 * <p>
	 * 
	 * @return List<String>
	 * @date 2017年8月7日 下午5:20:44
	 */
	public void queryMongoListsByApp() {
		// 获取所有相关的数据表
		Set<String> collectionNames = mongoTemplate.getCollectionNames();
		ArrayList<String> tables = new ArrayList<>();
		for (String name : collectionNames) {
			if ("dplog".equals(name.substring(0, 5))) {
				tables.add(name);
			}
		}
		// List<DBObject> list = new ArrayList<DBObject>();
		for (int i = 0; i < tables.size(); i++) {
			DBCursor find = mongoTemplate.getCollection(tables.get(i)).find();
			for (Iterator<DBObject> it = find.iterator(); it.hasNext();) {
				DBObject next = it.next();
				String module = next.get("module").toString();
				if ("应用模块".equals(module)) {
					String pdate = next.get("pdate").toString();
					String dateFormatTran = dateFormatTran(pdate);
					next.put("pdate", dateFormatTran);
					String pparam = next.get("pparam").toString();
					com.alibaba.fastjson.JSONObject parseObject = com.alibaba.fastjson.JSON.parseObject(pparam);
					String appName = parseObject.get("appName").toString();
					String appTypeName = parseObject.get("appTypeName").toString();
					String[] appTypeNameArr = appTypeName.split(",");
					for (int j = 0; j < appTypeNameArr.length; j++) {
						BasicDBObject dbo = new BasicDBObject();
						if (j == 0) {
							dbo.put("typeCount", 1);
						} else {
							dbo.put("typeCount", 0);
						}
						dbo.put("loginMail", next.get("loginMail"));
						dbo.put("userRelName", next.get("userRelName"));
						dbo.put("pdate", next.get("pdate"));
						dbo.put("pmethod", next.get("pmethod"));
						dbo.put("psrc", next.get("psrc"));
						dbo.put("pparam", appName);
						dbo.put("ip", next.get("ip"));
						dbo.put("module", next.get("module"));
						dbo.put("provinceGid", next.get("provinceGid"));
						dbo.put("provinceName", next.get("provinceName"));
						dbo.put("cityGid", next.get("cityGid"));
						dbo.put("cityName", next.get("cityName"));
						dbo.put("townGid", next.get("townGid"));
						dbo.put("townName", next.get("townName"));
						dbo.put("roleFlag", next.get("roleFlag"));
						dbo.put("type", appTypeNameArr[j]);
						dbo.put("remark", next.get("remark"));
						dbo.put("schoolGid", next.get("schoolGid"));
						dbo.put("schoolName", next.get("schoolName"));
						mongoTemplate.getCollection("dataCenter_app_detail_table").insert(dbo);
						// list.add(next);
					}
				}
			}
		}
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
