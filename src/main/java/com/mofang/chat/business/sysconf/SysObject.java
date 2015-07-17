package com.mofang.chat.business.sysconf;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.http.impl.client.CloseableHttpClient;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.mofang.framework.data.mysql.pool.BoneCPPool;
import com.mofang.framework.data.mysql.pool.MysqlPool;
import com.mofang.framework.data.redis.RedisExecutor;
import com.mofang.framework.data.redis.pool.RedisPoolConfig;
import com.mofang.framework.data.redis.pool.RedisPoolProvider;
import com.mofang.framework.net.http.HttpClientConfig;
import com.mofang.framework.net.http.HttpClientProvider;

/**
 * 
 * @author zhaodx
 *
 */
public class SysObject
{
	/**
	 * Master Redis Executor Instance
	 */
	public final static RedisExecutor REDIS_MASTER_EXECUTOR = new RedisExecutor();
	
	/**
	 * Slave Redis Executor Instance
	 */
	public final static RedisExecutor REDIS_SLAVE_EXECUTOR = new RedisExecutor();
	
	/**
	 * Write Queue Redis Executor Intance
	 */
	public final static RedisExecutor WRITE_QUEUE_EXECUTOR = new RedisExecutor();
	
	/**
	 * Push Queue Redis Executor Instance
	 */
	public final static RedisExecutor PUSH_QUEUE_EXECUTOR = new RedisExecutor();
	
	/**
	 * Guild Slave Redis Executor Instance
	 */
	public final static RedisExecutor GUILD_SLAVE_EXECUTOR = new RedisExecutor();
	
	/**
	 * Mysql Pool Instance
	 */
	public static MysqlPool MYSQL_CONNECTION_POOL = null;
	
	public static String USER_AUTH_URL;
	
	public static String USER_INFO_URL;
	
	public static String ALLOW_SEND_URL;
	
	/**
	 * Global Http Client Instance
	 */
	public static CloseableHttpClient HTTP_CLIENT;
	
	/***************************************初始化系统对象*************************************/
	private final static String JdbcUrlFat = "jdbc:mysql://%s:%s/%s?user=%s&password=%s&useUnicode=true&characterEncoding=%s&autoReconnect=true&failOverReadOnly=false";
	private final static String Driver = "com.mysql.jdbc.Driver";
	
	public static void initWriteQueue(String configPath) throws Exception
	{
		RedisPoolConfig config = getRedisConfig(configPath);
		JedisPool pool = RedisPoolProvider.getRedisPool(config);
		SysObject.WRITE_QUEUE_EXECUTOR.setJedisPool(pool);
	}
	
	public static void initPushQueue(String configPath) throws Exception
	{
		RedisPoolConfig config = getRedisConfig(configPath);
		JedisPool pool = RedisPoolProvider.getRedisPool(config);
		SysObject.PUSH_QUEUE_EXECUTOR.setJedisPool(pool);
	}
	
	public static void initRedisMaster(String configPath) throws Exception
	{
		RedisPoolConfig config = getRedisConfig(configPath);
		JedisPool pool = RedisPoolProvider.getRedisPool(config);
		SysObject.REDIS_MASTER_EXECUTOR.setJedisPool(pool);
	}
	
	public static void initRedisSlave(String configPath) throws Exception
	{
		RedisPoolConfig config = getRedisConfig(configPath);
		JedisPool pool = RedisPoolProvider.getRedisPool(config);
		SysObject.REDIS_SLAVE_EXECUTOR.setJedisPool(pool);
	}
	
	public static void initGuildSlave(String configPath) throws Exception
	{
		RedisPoolConfig config = getRedisConfig(configPath);
		JedisPool pool = RedisPoolProvider.getRedisPool(config);
		SysObject.GUILD_SLAVE_EXECUTOR.setJedisPool(pool);
	}
	
	private static RedisPoolConfig getRedisConfig(String configPath) throws Exception
	{
        try
        {
        	Properties configurations = loadConfig(configPath);
			String host = configurations.getProperty("host");
			int port = Integer.valueOf(configurations.getProperty("port"));
			int timeout = Integer.valueOf(configurations.getProperty("timeout"));
			int maxActive = Integer.valueOf(configurations.getProperty("maxActive"));
			int maxIdle = Integer.valueOf(configurations.getProperty("maxIdle"));
			boolean testOnBorrow = Boolean.valueOf(configurations.getProperty("testOnBorrow"));
			
			RedisPoolConfig config = new RedisPoolConfig();
			JedisPoolConfig poolConf = new JedisPoolConfig();
			poolConf.setMaxActive(maxActive);
			poolConf.setMaxIdle(maxIdle);
			poolConf.setTestOnBorrow(testOnBorrow);
			config.setConfig(poolConf);
			config.setHost(host);
			config.setPort(port);
			config.setTimeout(timeout);
			return config;
        }
        catch(Exception e)
        {
        	throw e;
        }
	}

	public static void initMysql(String configPath) throws Exception
	{
		BoneCPConfig config = getMysqlConfig(configPath);
		Class.forName(Driver);
		BoneCP pool = new BoneCP(config);
		SysObject.MYSQL_CONNECTION_POOL = new BoneCPPool(pool);
	}
	
	private static BoneCPConfig getMysqlConfig(String configPath) throws Exception
	{
        try
        {
        	Properties configurations = loadConfig(configPath);
			String host = configurations.getProperty("host");
			String port = configurations.getProperty("port");
			String user = configurations.getProperty("user");
			String password = configurations.getProperty("password");
			String charset = configurations.getProperty("charset");
			String dbname = configurations.getProperty("dbname");
			int partitionCount = Integer.valueOf(configurations.getProperty("partitionCount"));
			int maxConnectionsPerPartition = Integer.valueOf(configurations.getProperty("maxConnectionsPerPartition"));
			int minConnectionsPerPartition = Integer.valueOf(configurations.getProperty("minConnectionsPerPartition"));
			int acquireIncrement = Integer.valueOf(configurations.getProperty("acquireIncrement"));
			int releaseHelperThreads = Integer.valueOf(configurations.getProperty("releaseHelperThreads"));
			
			String jdbcUrl = String.format(JdbcUrlFat, host, port, dbname, user, password, charset);
			BoneCPConfig config = new BoneCPConfig();
			config.setJdbcUrl(jdbcUrl);
			config.setPartitionCount(partitionCount);
			config.setMaxConnectionsPerPartition(maxConnectionsPerPartition);
			config.setMinConnectionsPerPartition(minConnectionsPerPartition);
			config.setAcquireIncrement(acquireIncrement);
			config.setReleaseHelperThreads(releaseHelperThreads);
			config.setIdleConnectionTestPeriod(60, TimeUnit.SECONDS);
			config.setIdleMaxAgeInSeconds(240);
			return config;
        }
        catch(Exception e)
        {
        	throw e;
        }
	}
	
	public static void initHttpClient(String configPath) throws Exception
	{
        try
        {
        	Properties configurations = loadConfig(configPath);
			String host = configurations.getProperty("host");
			int port = Integer.valueOf(configurations.getProperty("port"));
			int maxTotal = Integer.valueOf(configurations.getProperty("maxTotal"));
			String charset = configurations.getProperty("charset");
			int connTimeout = Integer.valueOf(configurations.getProperty("connTimeout"));
			int socketTimeout = Integer.valueOf(configurations.getProperty("socketTimeout"));
			int keepAliveTimeout = Integer.valueOf(configurations.getProperty("keepAliveTimeout"));
			int checkIdleInitialDelay = Integer.valueOf(configurations.getProperty("checkIdleInitialDelay"));
			int checkIdlePeriod = Integer.valueOf(configurations.getProperty("checkIdlePeriod"));
			int closeIdleTimeout = Integer.valueOf(configurations.getProperty("closeIdleTimeout"));
			
			HttpClientConfig config = new HttpClientConfig();
			config.setHost(host);
			config.setPort(port);
			config.setMaxTotal(maxTotal);
			config.setCharset(charset);
			config.setConnTimeout(connTimeout);
			config.setSocketTimeout(socketTimeout);
			config.setDefaultKeepAliveTimeout(keepAliveTimeout);
			config.setCheckIdleInitialDelay(checkIdleInitialDelay);
			config.setCheckIdlePeriod(checkIdlePeriod);
			config.setCloseIdleTimeout(closeIdleTimeout);
			
			HttpClientProvider provider = new HttpClientProvider(config);
			SysObject.HTTP_CLIENT = provider.getHttpClient();
        }
        catch(Exception e)
        {
        	throw e;
        }
	}
	
	private static Properties loadConfig(String configPath) throws Exception
	{
		Properties configurations = new Properties();
        File file = new File(configPath);
        try
        {
        	configurations.load(new FileInputStream(file));
        	return configurations;
        }
        catch(Exception e)
        {
        	throw e;
        }
	}
}