package com.drzk.pay.core;

import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * @Description:
 * @Date: 2018/5/3 16:27
 * @Auther: nothing
 */
@Configuration
@ConfigurationProperties(prefix = "spring.data.mongodb")
public class MongoConfig {

	private String uri;
	private String database;
	private String host;
	private String port;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	@Autowired
	private ApplicationContext appContext;

	// ***********************mongoClient 配置*****************************
	/*
	 * @Bean public MongoClient mongoClient() throws Exception { return new
	 * MongoClient(new MongoClientURI(uri)); }
	 */

	// ***********************MongoTemplate 配置*****************************
	@Bean
	public MongoDbFactory mongoDbFactory() throws Exception {
		return new SimpleMongoDbFactory(new MongoClientURI(uri));
		// return new SimpleMongoDbFactory(new MongoClient(),database);
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		MongoDbFactory factory = mongoDbFactory();
		MongoMappingContext mongoMappingContext = new MongoMappingContext();
		mongoMappingContext.setApplicationContext(appContext);
		MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(factory),
				mongoMappingContext);
		// 去掉 _class 属性
		converter.setTypeMapper(new DefaultMongoTypeMapper(null));
		return new MongoTemplate(factory, converter);

	}

}
