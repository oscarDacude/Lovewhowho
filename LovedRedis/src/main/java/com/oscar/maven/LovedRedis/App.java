package com.oscar.maven.LovedRedis;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Hello World!");
		//new App().redisTester();
		
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
		Student stu = context.getBean("Student", Student.class);
		System.out.println(stu.toString());



	}

	@Test
	public void redisTester() {
		Jedis jedis = new Jedis("localhost", 6379, 100000);
		try {
			jedis.set("test", "AAA");		
		} finally {// 关闭连接
			jedis.close();
		}
	}

	@Test
	public void test() {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
		RedisTemplate<String, Student> redisTemplate = context.getBean(RedisTemplate.class);
		Student student = new Student();
		student.setName("Oscar.niu");
		student.setAge(21);
		redisTemplate.opsForValue().set("student1", student);
		Student student1 = redisTemplate.opsForValue().get("student1");
		student1.service();
	}
	
	@Test
	public void tesstX() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		// 最大空闲数
		poolConfig.setMaxIdle(50);
		// 最大连接数
		poolConfig.setMaxTotal(100);
		// 最大等待毫秒数
		poolConfig.setMaxWaitMillis(20000);
		// 使用配置创建连接池
		JedisPool pool = new JedisPool(poolConfig, "localhost");
		// 从连接池中获取单个连接
		Jedis jedis = pool.getResource();
		System.out.println(jedis.get("aaa"));
	}
	
	// list数据类型适合于消息队列的场景:比如12306并发量太高，而同一时间段内只能处理指定数量的数据！必须满足先进先出的原则，其余数据处于等待
	@Test
	public void listPushResitTest() {

		ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
		RedisTemplate<String, String> redisTemplate = context.getBean(RedisTemplate.class);
	    // leftPush依次由右边添加
		redisTemplate.opsForList().rightPush("myList", "1");
		redisTemplate.opsForList().rightPush("myList", "2");
		redisTemplate.opsForList().rightPush("myList", "A");
		redisTemplate.opsForList().rightPush("myList", "B");
	    // leftPush依次由左边添加
		redisTemplate.opsForList().leftPush("myList", "0");
	}

	@Test
	public void listGetListResitTest() {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
		RedisTemplate<String, String> redisTemplate = context.getBean(RedisTemplate.class);
	    // 查询类别所有元素
	    List<String> listAll = redisTemplate.opsForList().range("myList", 0, -1);
	    // 查询前3个元素
	    List<String> list = redisTemplate.opsForList().range("myList", 0, 3);
	    for(String s : listAll) {
	    	System.out.println(s);
	    }
	    System.out.println("~~~~~~~~~~~~~~~");
	    for(String s : list) {
	    	System.out.println(s);
	    }
	}

	@Test
	public void listRemoveOneResitTest() {

		ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
		RedisTemplate<String, String> redisTemplate = context.getBean(RedisTemplate.class);
	    // 删除先进入的B元素
		redisTemplate.opsForList().remove("myList", 1, "B");
	}

	@Test
	public void listRemoveAllResitTest() {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
		RedisTemplate<String, String> redisTemplate = context.getBean(RedisTemplate.class);
	    // 删除所有A元素
		redisTemplate.opsForList().remove("myList", 0, "A");
	}

}
