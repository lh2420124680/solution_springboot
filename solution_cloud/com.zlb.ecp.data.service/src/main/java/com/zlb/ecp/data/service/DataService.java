package com.zlb.ecp.data.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.entity.bo.DictionaryBo;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;
import com.zlb.api.IDictinaryService;
import com.zlb.ecp.helper.DataConvertHelper;

@RestController
@RequestMapping(value="zlbapp/datacenter/DataService")
public class DataService {/*

	@Autowired
	private MongoOperations mongoTemplate;
	
	@Autowired
	private IUserBiz userBiz;
	
	@Autowired
	private IDictinaryService dictinaryService;
 	
	@RequestMapping(value = "queryData.do", method = RequestMethod.GET)
	public String queryData(){
		System.out.println(">>>>>>>>>>>>>>>>>>>>开始远程调用");
		HashMap<String, Object> where = new HashMap<>();
		where.put("key", "9527");
		String remoteTest = userBiz.remoteTest(where);
		System.out.println(remoteTest);
		System.out.println(">>>>>>>>>>>>>>>>>>>>结束远程调用");
		return remoteTest;
		return null;
	}
	
	@RequestMapping(value = "mongoTest.do", method = RequestMethod.GET)
	public String mongoTest(){
		String where = "{'loginMail':'ys001','userRelName':'123456'}";
    	DBObject query = (DBObject) JSON.parse(where);
		WriteResult insert = mongoTemplate.getCollection("test").insert(query);
		int n = insert.getN();
		return String.valueOf(n);
	}
	
	@RequestMapping(value = "remoTest.do", method = RequestMethod.GET)
	public String remoTest(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> requestParams = DataConvertHelper.getRequestParams(request);
		List<DictionaryBo> listTownTree = dictinaryService.listTownTree();
		return listTownTree.get(0).getAppCode();
	}
*/}
