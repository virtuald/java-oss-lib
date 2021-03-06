/**
 * 
 */
package com.trendrr.oss.casting;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Logger;

import com.trendrr.json.simple.JSONValue;

import com.trendrr.oss.DynMap;
import com.trendrr.oss.DynMapConvertable;


/**
 * @author Dustin Norlander
 * @created Dec 29, 2010
 * 
 */
public class DynMapCaster extends TypeCaster<DynMap> {
	private static Logger log = Logger.getLogger(DynMapCaster.class.getCanonicalName());
	/* (non-Javadoc)
	 * @see com.trendrr.oss.casting.TypeCaster#doCast(java.lang.Class, java.lang.Object)
	 */
	@Override
	protected DynMap doCast(Class clss, Object object) {
		
		if (object instanceof Map) {
			return toDynMap((Map)object);
		}
		
		if (object instanceof DynMapConvertable) {
			return ((DynMapConvertable)object).toDynMap();
		}
		
		if (object instanceof byte[]) {
			try {
				object = new String((byte[])object, "utf8");
			} catch (Exception x) {
				
			}
		}
		
		if (object instanceof String) {
			//try the json simple here. 
			try {
				Object obj = JSONValue.parseWithException((String)object);
				if (obj instanceof Map){
					return toDynMap((Map)obj);
				}
			} catch (Exception x) {
//				log.info("Unable to parse string: " + object + " into a DynMap (" + x.getMessage() + ")");
			}
			return null;
		}
		
		
		Class cls = object.getClass();
		try {
			Method toMap = cls.getMethod("toMap");
			Map mp = (Map)toMap.invoke(object);
			return toDynMap((Map)mp);
		} catch (NoSuchMethodException x) { 
			//do nothing.
		} catch (Exception x) {
			log.info("toMap method didn't work for " + object + " into a DynMap (" + x.getMessage() + ")");
		}
		return null;
	}
	
	private DynMap toDynMap(Map map) {
		if (map instanceof DynMap) {
			return (DynMap)map;
		}
		
		DynMap mp = new DynMap();
		for (Object key : map.keySet()) {
			mp.put(key.toString(), map.get(key));
		}
		return mp;
	}
}
