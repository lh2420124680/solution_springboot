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
import com.zlb.ecp.data.idao.IAppUseStatDao;
import com.zlb.ecp.helper.StringHelper;
import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 应用使用统计
 * @author Jane.Luo
 * @date 2017年8月17日 上午11:39:28
 * @version V1.0
 */
@Repository
public class AppUseStatDao implements IAppUseStatDao {

	private static final Logger log = LoggerFactory.getLogger(UserUseStatDao.class);

	@Autowired
	private MongoOperations mongoTemplate;
	
	/**
	 * <p>Description: 应用使用人数<p>
	 * @param where
	 * @return Integer
	 * @date 2017年8月17日 上午11:40:46
	 */
	@Override
	public Integer queryAppUsePeoNum(Map<String, Object> where) {
		Integer allNum = 0;
		try {
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoListsByApp();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_app_peoNum_table").insert(list);
			String groupStr = "{$group:{_id:{'userRelName':'$userRelName'}}}";
			DBObject group = (DBObject) JSON.parse(groupStr);
			AggregationOutput ao = mongoTemplate.getCollection("dataCenter_app_peoNum_table").aggregate(group);
			for (Iterator<DBObject> it = ao.results().iterator(); it.hasNext(); it.next()) {
				allNum += 1;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			// 清空临时总表
			mongoTemplate.dropCollection("dataCenter_app_peoNum_table");
		}
		return allNum;
	}
	
	/**
	 * <p>
	 * Description: 应用使用次数
	 * <p>
	 * 
	 * @param where
	 * @return Integer
	 * @date 2017年8月17日 上午11:40:36
	 */
	@Override
	public Integer queryAppUseNum(Map<String, Object> where) {
		Integer allNum = 0;
		try {
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoListsByApp();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_app_use_table").insert(list);
			allNum = (int) mongoTemplate.getCollection("dataCenter_app_use_table").count();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			// 清空临时总表
			mongoTemplate.dropCollection("dataCenter_app_use_table");
		}
		return allNum;
	}

	/**
	 * <p>
	 * Description: 应用浏览次数
	 * <p>
	 * 
	 * @param where
	 * @return Integer
	 * @date 2017年8月17日 上午11:40:46
	 */
	@Override
	public Integer queryAppBrowNum(Map<String, Object> where) {
		Integer allNum = 0;
		try {
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoListsByApp();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_app_brow_table").insert(list);
			String groupStr = "{$group:{_id:{'userRelName':'$userRelName'},actNum:{$sum:1}}}";
			DBObject group = (DBObject) JSON.parse(groupStr);
			AggregationOutput ao = mongoTemplate.getCollection("dataCenter_app_brow_table").aggregate(group);
			for (Iterator<DBObject> it = ao.results().iterator(); it.hasNext();) {
				BasicDBObject next = (BasicDBObject) it.next();
				Integer num = Integer.valueOf(next.get("actNum").toString());
				allNum += num;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			// 清空临时总表
			mongoTemplate.dropCollection("dataCenter_app_brow_table");
		}
		return allNum;
	}
	
	/**
	 * <p>Description: 各区域应用使用人数<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 下午12:02:03
	 */
	@Override
	public ListResult<Map<String, Object>> queryAreaAppUseNum(Map<String, Object> where) {
		ListResult<Map<String, Object>> result = new ListResult<>();
		try {
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoListsByApp();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_app_areUseNum_table").insert(list);
			String layerLevel = where.get("layerLevel").toString();
			String gid = where.get("gid").toString();
			Object schoolGid = where.get("schoolGid");
			String groupStr = "";
			String matchStr = "";
			String keyValus = "";
			if (StringHelper.IsEmptyOrNull(schoolGid)) {
				if ("1".equals(layerLevel)) { // 海南省
					groupStr = "{$group:{_id:{'cityName':'$cityName','loginMail':'$loginMail'}}}";
					matchStr = "{$match:{provinceGid:'" + gid + "'}}";
					keyValus = "cityName";
				} else if ("2".equals(layerLevel)) { // 市县
					groupStr = "{$group:{_id:{'townName':'$townName','loginMail':'$loginMail'}}}";
					matchStr = "{$match:{cityGid:'" + gid + "'}}";
					keyValus = "townName";
				} else if ("3".equals(layerLevel)) { // 区
					groupStr = "{$group:{_id:{'schoolName':'$schoolName','loginMail':'$loginMail'}}}";
					matchStr = "{$match:{townGid:'" + gid + "'}}";
					keyValus = "schoolName";
				}
			} else {
				groupStr = "{$group:{_id:{'schoolName':'$schoolName','loginMail':'$loginMail'}}}";
				matchStr = "{$match:{schoolGid:'" + schoolGid + "'}}";
				keyValus = "schoolName";
			}
			DBObject group = (DBObject) JSON.parse(groupStr);
			DBObject match = (DBObject) JSON.parse(matchStr);
			List<Map<String, Object>> table = new ArrayList<>();
			AggregationOutput ao = mongoTemplate.getCollection("dataCenter_app_areUseNum_table").aggregate(match,group);
			Map<String, Object> newMap = new HashMap<>();
			for (Iterator<DBObject> it = ao.results().iterator(); it.hasNext();) {
				BasicDBObject next = (BasicDBObject) it.next();
				BasicDBObject object = (BasicDBObject) next.get("_id");
				String name = object.get(keyValus).toString();
				if (newMap.containsKey(name)) {
					Integer num = Integer.valueOf(newMap.get(name).toString());
					newMap.put(name, num + 1);
				} else {
					newMap.put(name, 1);
				}
			}
			for (Map.Entry<String, Object> entry : newMap.entrySet()) {
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
			mongoTemplate.dropCollection("dataCenter_app_areUseNum_table");
		}
		return result;
	}
	
	/**
	 * <p>Description: 各区域应用使用次数<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 下午12:02:03
	 */
	@Override
	public ListResult<Map<String, Object>> queryAreaAppUse(Map<String, Object> where) {
		ListResult<Map<String, Object>> result = new ListResult<>();
		try {
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoListsByApp();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_app_areUse_table").insert(list);
			String layerLevel = where.get("layerLevel").toString();
			String gid = where.get("gid").toString();
			Object schoolGid = where.get("schoolGid");
			String groupStr = "";
			String matchStr = "";
			String keyValus = "";
			if (StringHelper.IsEmptyOrNull(schoolGid)) {
				if ("1".equals(layerLevel)) { // 海南省
					groupStr = "{$group:{_id:{'cityName':'$cityName'},actNum:{$sum:1}}}";
					matchStr = "{$match:{provinceGid:'" + gid + "'}}";
					keyValus = "cityName";
				} else if ("2".equals(layerLevel)) { // 市县
					groupStr = "{$group:{_id:{'townName':'$townName'},actNum:{$sum:1}}}";
					matchStr = "{$match:{cityGid:'" + gid + "'}}";
					keyValus = "townName";
				} else if ("3".equals(layerLevel)) { // 区
					groupStr = "{$group:{_id:{'schoolName':'$schoolName'},actNum:{$sum:1}}}";
					matchStr = "{$match:{townGid:'" + gid + "'}}";
					keyValus = "schoolName";
				}
			} else {
				groupStr = "{$group:{_id:{'schoolName':'$schoolName'},actNum:{$sum:1}}}";
				matchStr = "{$match:{schoolGid:'" + schoolGid + "'}}";
				keyValus = "schoolName";
			}
			DBObject group = (DBObject) JSON.parse(groupStr);
			DBObject match = (DBObject) JSON.parse(matchStr);
			List<Map<String, Object>> table = new ArrayList<>();
			AggregationOutput ao = mongoTemplate.getCollection("dataCenter_app_areUse_table").aggregate(match,group);
			for (Iterator<DBObject> it = ao.results().iterator(); it.hasNext();) {
				BasicDBObject next = (BasicDBObject) it.next();
				Map<String, Object> map = new HashMap<>();
				BasicDBObject object = (BasicDBObject) next.get("_id");
				String name = object.get(keyValus).toString();
				map.put("name", name);
				map.put("value", next.get("actNum"));
				table.add(map);
			}
			result.setRows(table);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			// 清空临时总表
			mongoTemplate.dropCollection("dataCenter_app_areUse_table");
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
