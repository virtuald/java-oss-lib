/**
 * 
 */
package com.trendrr.oss.tests;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.junit.Test;

import com.trendrr.oss.DynMap;
import com.trendrr.oss.DynMapFactory;



/**
 * @author Dustin Norlander
 * @created Dec 30, 2010
 * 
 */
public class DynMapTests {

	protected Logger log = Logger.getLogger(DynMapTests.class.getCanonicalName());
	
	@Test
	public void test1() {
		DynMap mp = DynMapFactory.instance("{ \"key\" : 90 }");
		log.info(mp.toJSONString());
		Assert.assertTrue(mp.get(Integer.class, "key") == 90);
		
		Assert.assertTrue(mp.get(String.class, "key").equals("90"));
		
		mp = DynMapFactory.instance("{ \"key\" : 90," +
				"\"map1\" : {" +
					"\"key2\" : \"today\"" +
				"}," +
				"\"list\" : [ 1,2,3]" +
			" }");
		
		
		
		
		
		Assert.assertEquals(mp.get(String.class, "map1.key2"), "today");
		mp.remove("map1");
		Assert.assertEquals("key=90&list=1&list=2&list=3", mp.toURLString());
	}
	
	@Test
	public void putDotTest() {
		DynMap mp = new DynMap();
		mp.putWithDot("map1.map2.map3.val", 0);
		
		mp.putWithDot("map1.map2.val", 10);
		
		Assert.assertNotNull(mp.getMap("map1"));
	}
	
	@Test
	public void outputTest() {
		DynMap mp = new DynMap();
		
		HashSet<String> test = new HashSet<String>();
		List<String> test2 = new ArrayList<String>();
		
		for (int i=0; i < 3; i++){
			test.add("str" + i);
			test2.add("2str" + i);
		}
		
		mp.put("list", test2);
		mp.put("set", test);
		Assert.assertEquals("{\"set\":[\"str2\",\"str1\",\"str0\"],\"list\":[\"2str0\",\"2str1\",\"2str2\"]}", mp.toJSONString());
	}
	
	@Test
	public void extendTest() {
		DynMap mp1 = new DynMap();
		
		mp1.putWithDot("map1.map2.map3.val", 0);
		mp1.putWithDot("map1.map2.map3.keep", 66);
		mp1.putWithDot("map1.mapz.sub.devil", 666);
		
		
		DynMap mp2 = new DynMap();
		mp2.putWithDot("map1.map2.map3.val", 10);
		mp1.putWithDot("map1.mapz", 777);
		
		mp1.extend(mp2);
//		System.out.println(mp1.toJSONString());
		
		//no exception!
		mp1.extend(null);
		
		Assert.assertEquals((int)mp1.getInteger("map1.map2.map3.val"), 10);
		Assert.assertEquals((int)mp1.getInteger("map1.mapz"), 777);
		Assert.assertEquals((int)mp1.getInteger("map1.map2.map3.keep"), 66);
		Assert.assertNull(mp1.get("map1.mapz.sub.devil"));
	}
	
	@Test
	public void nullTest() {
		DynMap mp = new DynMap();
		mp.put("null", null);
		Assert.assertEquals("{\"null\":null}", mp.toJSONString());
	}
	
}
