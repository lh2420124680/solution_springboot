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
import com.zlb.ecp.data.idao.IAppUseRankDao;
import com.zlb.ecp.helper.StringHelper;
import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 应用使用排行
 * @author Jane.Luo
 * @date 2017年8月17日 下午3:16:08
 * @version V1.0
 */
@Repository
public class AppUseRankDao implements IAppUseRankDao {

	private static final Logger log = LoggerFactory.getLogger(UserUseStatDao.class);

	@Autowired
	private MongoOperations mongoTemplate;

	/**
	 * <p>
	 * Description: 应用使用排行
	 * <p>
	 * 
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 下午3:17:26
	 */
	@Override
	public ListResult<Map<String, Object>> queryAppUseRank(Map<String, Object> where) {
		ListResult<Map<String, Object>> result = new ListResult<>();
		try {
			Object gid = where.get("gid");
			Object schoolGid = where.get("schoolGid");
			// 层级
			String layerLevel = where.get("layerLevel").toString();
			// 获取所有相关的数据表
			List<DBObject> list = queryMongoListsByApp();
			// 将所有的日志表数据存到一个临时总表中
			mongoTemplate.getCollection("dataCenter_app_useRank_table").insert(list);
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
			AggregationOutput ao = mongoTemplate.getCollection("dataCenter_app_useRank_table").aggregate(match, group);
			for (Iterator<DBObject> it = ao.results().iterator(); it.hasNext();) {
				BasicDBObject dbo = (BasicDBObject) it.next();
				BasicDBObject keyVals = (BasicDBObject) dbo.get("_id");
				Object pparam = keyVals.get("pparam");
				Object actNum = dbo.get("actNum");
				Map<String, Object> map = new HashMap<>();
				map.put("name", pparam);
				map.put("value", actNum);
				table.add(map);
			}
			result.setRows(table);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			// 清空临时总表
			mongoTemplate.dropCollection("dataCenter_app_useRank_table");
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
