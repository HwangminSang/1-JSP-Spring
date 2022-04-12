package kr.co.yooooon.common.sl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class ServiceLocator {
	private Map<String,DataSource> cache;  
	private Context envCtx; // =new InitialContext()   //JNDI Naming 
	private static ServiceLocator instance;

	static {
		try {
			instance = new ServiceLocator();
		} catch (ServiceLocatorException e) {
			e.printStackTrace();
		}
	}

	private ServiceLocator() {
		try {
			envCtx = new InitialContext(); //JNDI �꽌鍮꾩뒪�뿉�꽌  Datasource 媛앹껜瑜� 李얠븘�삤�뒗 ���꽍.  < jdni�뿉 �젒洹쇳빐�꽌 猷⑹뾽�븯�뒗 媛앹껜 >
			cache = Collections.synchronizedMap(new HashMap<String,DataSource>());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceLocatorException(e.getMessage());
		}
	}

	public static ServiceLocator getInstance() {
		return instance;
	}

	public DataSource getDataSource(String jndiName) throws ServiceLocatorException {
		DataSource dataSource;
		try {
			if (cache.containsKey(jndiName)) {
				dataSource=cache.get(jndiName);
			} else {
				dataSource = (DataSource)envCtx.lookup("java:comp/env/"+jndiName); //InitialContext() 
				cache.put(jndiName, dataSource); // 利� jdbc pool留곴린踰�
			}
		} catch (NamingException e) { 
			throw new ServiceLocatorException(e.getMessage());
		}
		return dataSource; 
	}
}




