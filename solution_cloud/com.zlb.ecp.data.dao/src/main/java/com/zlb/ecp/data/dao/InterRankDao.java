package com.zlb.ecp.data.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.zlb.ecp.data.idao.IInterRankDao;
import com.zlb.ecp.helper.StringHelper;
import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 积分排行榜
 * @author Jane.Luo
 * @date 2017年8月18日 下午2:29:19
 * @version V1.0
 */
@Repository
public class InterRankDao implements IInterRankDao {

	private static final Logger log = LoggerFactory.getLogger(UserUseStatDao.class);

	@Autowired
	private MongoOperations mongoTemplate;

	/**
	 * <p>
	 * Description: 积分排行榜
	 * <p>
	 * 
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月18日 下午2:30:16
	 */
	@Override
	public ListResult<Map<String, Object>> queryInterRank(Map<String, Object> where) {
		int currentPage = StringHelper.IsEmptyOrNull(where.get("page")) ? 1
				: Integer.parseInt(where.get("page").toString().trim());
		int pageSize = StringHelper.IsEmptyOrNull(where.get("rows")) ? Integer.MAX_VALUE
				: Integer.parseInt(where.get("rows").toString().trim());
		int count = 0;
		// 过滤条件
		Object gid = where.get("gid");
		// 层级
		String layerLevel = where.get("layerLevel").toString();
		// 排序字段
		Object sort = where.get("sort");
		// 1是升序，-1降序
		Object object = where.get("sortBy");
		Object schoolGid = where.get("schoolGid");
		String matchStr = "";
		if (StringHelper.IsEmptyOrNull(schoolGid)) {
			if ("1".equals(layerLevel)) {
				matchStr = "{provinceGid:'" + gid + "'}";
			} else if ("2".equals(layerLevel)) { // 市县
				matchStr = "{cityGid:'" + gid + "'}";
			} else if ("3".equals(layerLevel)) { // 区
				matchStr = "{townGid:'" + gid + "'}";
			} 
		} else {
			matchStr = "{schoolGid:'" + schoolGid + "'}";
		}
		DBObject match = (DBObject) JSON.parse(matchStr);
		DBCursor find = mongoTemplate.getCollection("all_integral").find(match)
				.sort(new BasicDBObject(sort.toString(), Integer.valueOf(object.toString())))
				.skip((currentPage - 1) * pageSize).limit(pageSize);
		count = mongoTemplate.getCollection("all_integral").find(match).count();
		List<Map<String, Object>> table = new ArrayList<Map<String, Object>>();
		for (Iterator<DBObject> it = find.iterator(); it.hasNext();) {
			BasicDBObject next = (BasicDBObject) it.next();
			Map<String, Object> map = new HashMap<>();
			map.put("userRelName", next.get("userRelName"));
			map.put("scNum", next.get("scNum"));
			map.put("exNum", next.get("exNum"));
			map.put("teNum", next.get("teNum"));
			table.add(map);
		}
		ListResult<Map<String, Object>> result = new ListResult<>();
		result.setRows(table);
		result.setPageindex(currentPage);
		result.setPages(pageSize);
		result.setRecords(count);
		return result;
	}
	
	/**
	 * <p>Description: 积分排行榜导出 <p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月18日 下午2:30:16
	 */
	@Override
	public ListResult<Map<String, Object>> queryInterRankExport(Map<String, Object> where) {
		// 过滤条件
		Object gid = where.get("gid");
		Object schoolGid = where.get("schoolGid");
		// 层级
		String layerLevel = where.get("layerLevel").toString();
		// 排序字段
		Object sort = "scNum";
		// 1是升序，-1降序
		Object object = "-1";
		String matchStr = "";
		if (StringHelper.IsEmptyOrNull(schoolGid)) {
			if ("1".equals(layerLevel)) {
				matchStr = "{provinceGid:'" + gid + "'}";
			} else if ("2".equals(layerLevel)) { // 市县
				matchStr = "{cityGid:'" + gid + "'}";
			} else if ("3".equals(layerLevel)) { // 区
				matchStr = "{townGid:'" + gid + "'}";
			}
		} else {
			matchStr = "{schoolGid:'" + schoolGid + "'}";
		}
		DBObject match = (DBObject) JSON.parse(matchStr);
		DBCursor find = mongoTemplate.getCollection("all_integral").find(match)
				.sort(new BasicDBObject(sort.toString(), Integer.valueOf(object.toString())));
		List<Map<String, Object>> table = new ArrayList<Map<String, Object>>();
		for (Iterator<DBObject> it = find.iterator(); it.hasNext();) {
			BasicDBObject next = (BasicDBObject) it.next();
			Map<String, Object> map = new HashMap<>();
			map.put("userRelName", next.get("userRelName"));
			map.put("scNum", next.get("scNum"));
			map.put("exNum", next.get("exNum"));
			map.put("teNum", next.get("teNum"));
			table.add(map);
		}
		ListResult<Map<String, Object>> result = new ListResult<>();
		result.setRows(table);
		return result;
	}

}
