package com.zlb.ecp.data.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.zlb.ecp.data.idao.IWebAccessDataDao;
import com.zlb.ecp.helper.DateCenterHelper;
import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 页面访问数据
 * @author Jane.Luo
 * @date 2017年8月9日 下午3:32:06
 * @version V1.0
 */
@Repository
public class WebAccessDataDao implements IWebAccessDataDao {

	private static final Logger log = LoggerFactory.getLogger(UserUseStatDao.class);

	@Autowired
	private MongoOperations mongoTemplate;

	/**
	 * <p>
	 * Description: 今日浏览次数
	 * <p>
	 * 
	 * @return Integer
	 * @date 2017年8月9日 下午3:33:18
	 */
	@Override
	public Integer currBrowseNum(Map<String, Object> where) {
		// 获取当月的表
		String currTable = currMongoTable();
		String currDate = DateCenterHelper.currDate("yyyy-MM-dd");
		String matchStr = "{pdate:{$regex:'^.*" + currDate + "_*.*$'}}";
		DBObject match = (DBObject) JSON.parse(matchStr);
		int count = mongoTemplate.getCollection(currTable).find(match).count();
		return count;
	}

	/**
	 * <p>
	 * Description: 今日独立访客
	 * <p>
	 * 
	 * @return Integer
	 * @date 2017年8月9日 下午3:33:18
	 */
	@Override
	public Integer currIndepCustNum(Map<String, Object> where) {
		Object layerLevel = where.get("layerLevel");
		Object gid = where.get("gid");
		// 获取当月的表
		String currTable = currMongoTable();
		String currDate = DateCenterHelper.currDate("yyyy-MM-dd");
		String groupStr = "{$group:{_id:{'userRelName':'$userRelName'}}}";
		String matchStr = "{$match:{pdate:{$regex:'^.*" + currDate + "_*.*$'}}}";
		DBObject match = (DBObject) JSON.parse(matchStr);
		DBObject group = (DBObject) JSON.parse(groupStr);
		AggregationOutput ao = mongoTemplate.getCollection(currTable).aggregate(match, group);
		int allNum = 0;
		for (Iterator<DBObject> it = ao.results().iterator(); it.hasNext(); it.next()) {
			allNum += 1;
		}
		return allNum;
	}

	/**
	 * <p>
	 * Description: 今日访问Ip数
	 * <p>
	 * 
	 * @param where
	 * @return Integer
	 * @date 2017年8月9日 下午3:35:17
	 */
	@Override
	public Integer currIpNum(Map<String, Object> where) {
		// 获取当月的表
		String currTable = currMongoTable();
		String currDate = DateCenterHelper.currDate("yyyy-MM-dd");
		String groupStr = "{$group:{_id:{'ip':'$ip'}}}";
		String matchStr = "{$match:{pdate:{$regex:'^.*" + currDate + "_*.*$'}}}";
		DBObject match = (DBObject) JSON.parse(matchStr);
		DBObject group = (DBObject) JSON.parse(groupStr);
		AggregationOutput ao = mongoTemplate.getCollection(currTable).aggregate(match, group);
		int allNum = 0;
		for (Iterator<DBObject> it = ao.results().iterator(); it.hasNext(); it.next()) {
			allNum += 1;
		}
		return allNum;
	}

	/**
	 * <p>
	 * Description: 今日访问次数
	 * <p>
	 * 
	 * @param where
	 * @return Integer
	 * @date 2017年8月9日 下午3:35:53
	 */
	@Override
	public Integer currAccessNum(Map<String, Object> where) {
		// 获取当月的表
		String currTable = currMongoTable();
		String currDate = DateCenterHelper.currDate("yyyy-MM-dd");
		String groupStr = "{$group:{_id:{'userRelName':'$userRelName'}}}";
		String matchStr = "{$match:{pdate:{$regex:'^.*" + currDate + "_*.*$'},module:'登录模块'}}";
		DBObject match = (DBObject) JSON.parse(matchStr);
		DBObject group = (DBObject) JSON.parse(groupStr);
		AggregationOutput ao = mongoTemplate.getCollection(currTable).aggregate(match, group);
		int allNum = 0;
		for (Iterator<DBObject> it = ao.results().iterator(); it.hasNext(); it.next()) {
			allNum += 1;
		}
		return allNum;
	}

	/**
	 * <p>
	 * Description: 页面访问趋势分析
	 * <p>
	 * 
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月10日 下午1:49:21
	 */
	@Override
	public ListResult<Map<String, Object>> queryWebLoginTrend(Map<String, Object> where) {
		ListResult<Map<String, Object>> result = new ListResult<>();
		try {
			String object = where.get("timeType").toString();
			String currDate = DateCenterHelper.currDate("yyyy-MM-dd");
			String currTable = ""; // 当前表
			String broMatchStr = ""; // 浏览量
			String inteMatchStr = ""; // 独立访客
			String ipMatchStr = ""; // ip 
			String accMatchStr = ""; // 访问次数
			String inteGroupStr = "";
			String ipGroupStr = "";
			String accGroupStr = "";
			Map<Object, String> dateList = null;
			if ("1".equals(object)) {
				currTable = currMongoTableHour();
				dateList = DateCenterHelper.dateHourList();
			} else {
				currTable = "dataCenter_loginTrend_table";
				List<DBObject> list = queryMongoLists();
				// 将所有的日志表数据存到一个临时总表中
				mongoTemplate.getCollection(currTable).insert(list);
				if ("2".equals(object)) {
					dateList = DateCenterHelper.dateWeekList();
				} else if ("3".equals(object)) {
					dateList = DateCenterHelper.dateMonthList();
				}
			}
			List<Map<String, Object>> table = new ArrayList<>();
			for(Map.Entry<Object, String> entry : dateList.entrySet()){
				String beginTime = "";
				String endTime = "";
				String date = "";
				if ("1".equals(object)) {
					date = entry.getValue().split(" ")[1];
					String[] split = date.split("-");
					beginTime = currDate + " " + split[0];
					endTime = currDate + " " + split[1];
					broMatchStr = "{'pdate':{'$gte':'" + beginTime + "', '$lt':'" + endTime+ "'}}";
					inteMatchStr = "{$match:{pdate:{'$gte':'" + beginTime + "', '$lt':'" + endTime+ "'}}}";
					ipMatchStr = "{$match:{pdate:{'$gte':'" + beginTime + "', '$lt':'" + endTime+ "'}}}";
					accMatchStr = "{$match:{pdate:{'$gte':'" + beginTime + "', '$lt':'" + endTime+ "'},module:'登录模块'}}";
				} else {
					currDate = entry.getValue();
					date = currDate;
					broMatchStr = "{'pdate':{$regex:'^.*" + currDate + "_*.*$'}}";
					inteMatchStr = "{$match:{pdate:{$regex:'^.*" + currDate + "_*.*$'}}}";
					ipMatchStr = "{$match:{pdate:{$regex:'^.*" + currDate + "_*.*$'}}}";
					accMatchStr = "{$match:{pdate:{$regex:'^.*" + currDate + "_*.*$'},module:'登录模块'}}";
				}
				inteGroupStr = "{$group:{_id:{'userRelName':'$userRelName'}}}";
				ipGroupStr = "{$group:{_id:{'ip':'$ip'}}}";
				accGroupStr = "{$group:{_id:{'userRelName':'$userRelName'}}}";
				DBObject broMatch = (DBObject) JSON.parse(broMatchStr);
				DBObject inteMatch = (DBObject) JSON.parse(inteMatchStr);
				DBObject ipMatch = (DBObject) JSON.parse(ipMatchStr);
				DBObject accMatch = (DBObject) JSON.parse(accMatchStr);
				DBObject inteGroup = (DBObject) JSON.parse(inteGroupStr);
				DBObject ipGroup = (DBObject) JSON.parse(ipGroupStr);
				DBObject accGroup = (DBObject) JSON.parse(accGroupStr);
				// 浏览量
				int broNum = mongoTemplate.getCollection(currTable).find(broMatch).count();
				AggregationOutput inteAo = mongoTemplate.getCollection(currTable).aggregate(inteMatch, inteGroup);
				AggregationOutput ipAo = mongoTemplate.getCollection(currTable).aggregate(ipMatch, ipGroup);
				AggregationOutput accAo = mongoTemplate.getCollection(currTable).aggregate(accMatch, accGroup);
				int inteNum = 0,ipNum=0,accNum=0;
				for (Iterator<DBObject> it = inteAo.results().iterator(); it.hasNext(); it.next()) {
					inteNum += 1;
				}
				for (Iterator<DBObject> it = ipAo.results().iterator(); it.hasNext(); it.next()) {
					ipNum += 1;
				}
				for (Iterator<DBObject> it = accAo.results().iterator(); it.hasNext(); it.next()) {
					accNum += 1;
				}
				Map<String, Object> map = new HashMap<>();
				
				map.put("name", date);
				map.put("broNum", broNum);
				map.put("inteNum", inteNum);
				map.put("ipNum", ipNum);
				map.put("accNum", accNum);
				table.add(map);
			}
			result.setRows(table);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		} finally {
			mongoTemplate.dropCollection("dataCenter_loginTrend_table");
			mongoTemplate.dropCollection("dataCenter_loginTrend_table_temp");
		}
		return result;
	}

	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>本类公用方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**
	 * <p>
	 * Description: 获取当前的表
	 * <p>
	 * 
	 * @return String
	 * @date 2017-6-28 下午5:41:13
	 */
	public String currMongoTable() {
		// 得到当前的时间，用于去寻找表
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		String table = "dplog_" + year + "_" + month;
		return table;
	}
	
	/**
	 * <p>Description: 转换当前表的时间格式<p>
	 * @return String
	 * @date 2017年8月10日 下午4:27:58
	 */
	public String currMongoTableHour() {
		// 得到当前的时间，用于去寻找表
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		String table = "dplog_" + year + "_" + month;
		DBCursor find = mongoTemplate.getCollection(table).find();
		List<DBObject> list = new ArrayList<DBObject>();
		for (Iterator<DBObject> it = find.iterator(); it.hasNext();) {
			DBObject next = it.next();
			String pdate = next.get("pdate").toString();
			String dateFormatTran = dateFormatTranHour(pdate);
			next.put("pdate", dateFormatTran);
			list.add(next);
		}
		String newTable = "dataCenter_loginTrend_table_temp";
		mongoTemplate.getCollection(newTable).insert(list);
		return newTable;
	}

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
	 * Description: 日期格式转换
	 * <p>
	 * 
	 * @param parseTime
	 * @return
	 * @throws ParseException
	 *             String
	 * @date 2017-6-27 下午4:07:35
	 */
	public static String dateFormatTranHour(String parseTime) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS");
		Date parse = null;
		try {
			parse = sf.parse(parseTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long time = parse.getTime();
		Date date = new Date(time);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String dateString = format.format(date);
		return dateString;
	}
	
}
