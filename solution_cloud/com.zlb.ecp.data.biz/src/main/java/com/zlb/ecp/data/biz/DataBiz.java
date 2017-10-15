package com.zlb.ecp.data.biz;

import org.springframework.stereotype.Service;

import com.zlb.ecp.data.ibiz.IDataBiz;

@Service
public class DataBiz implements IDataBiz {

	@Override
	public void queryData() {
		System.out.println("sss");
	}

}
