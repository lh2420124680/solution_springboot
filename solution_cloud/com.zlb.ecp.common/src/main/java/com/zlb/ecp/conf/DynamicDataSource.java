package com.zlb.ecp.conf;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;

/**
 * @Description: 动态数据源 
 * @author Jane.Luo
 * @date 2017年10月12日 下午2:57:35 
 * @version V1.0
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

	private static final Logger log = LoggerFactory.getLogger(DynamicDataSource.class);
	public static final String DATASOURCE_BEAN_CLASS = "com.alibaba.druid.pool.DruidDataSource";
	@Value("${ecp.read.db.size}")
	int readDBSize;

	protected Object determineCurrentLookupKey() {
		String dsKey = ReadWriteDataSourceDecision.getDsKey();
		log.info("当前数据源key:{}", dsKey);
		if ((ReadWriteDataSourceDecision.isChoiceRead()) && (this.readDBSize > 0)) {
			dsKey = dsKey + RandomUtils.nextInt(this.readDBSize);
		}
		return dsKey;
	}

	public void setDataSourceLookup(DataSourceLookup dataSourceLookup) {
		super.setDataSourceLookup(dataSourceLookup);
	}

	public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
		super.setDefaultTargetDataSource(defaultTargetDataSource);
	}

	public void setTargetDataSources(Map<Object, Object> targetDataSources) {
		super.setTargetDataSources(targetDataSources);
	}

	public DataSource getTargetDataSource() {
		return determineTargetDataSource();
	}

	public void setReadDBSize(int readDBSize) {
		this.readDBSize = readDBSize;
	}

	public int getReadDBSize() {
		return this.readDBSize;
	}
}
