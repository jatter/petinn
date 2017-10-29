package com.petinn.component.cache;
import com.petinn.util.Constants;
import net.sf.json.JSONObject;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.base.util.cache.UtilCache;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.service.LocalDispatcher;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;

public final class EcommerceGlobalCache {

	private static final String module = EcommerceGlobalCache.class.getName();

    private static EcommerceGlobalCache instance = null;

    private EcommerceGlobalCache() {  }

    public static EcommerceGlobalCache getInstance() {
        if (instance == null) {
            synchronized (EcommerceGlobalCache.class) {
                if (instance == null) {
                    instance = new EcommerceGlobalCache();
                }
            }
        }
        return instance;
    }

    public synchronized Delegator getDelegator() {
        return (Delegator) getGlobalCache().get("delegator");
    }

    public synchronized LocalDispatcher getLocalDispatcher() {
        return (LocalDispatcher) getGlobalCache().get("dispatcher");
    }

    public void putGlobalCache(String key, Object value) {
        UtilCache<String, Object> cache = getGlobalCache();
        cache.put(key, value);
    }

    public void putGlobalCache(String key, Object value, long expireTime) {
        UtilCache<String, Object> cache = getGlobalCache();
        cache.put(key, value, expireTime);
    }

    public UtilCache<String, Object> getGlobalCache() {
        UtilCache<String, Object> cache = UtilCache.findCache("ECOMMERCE_GLOBAL_CACHE");
        if (cache == null) {
            cache = UtilCache.createUtilCache("ECOMMERCE_GLOBAL_CACHE");
        }
        return cache;
    }
    
    public String getCurrentDomain() {
        UtilCache<String, Object> cache = getGlobalCache();
        String domain = (String) cache.get(Constants.DOMAIN_KEY);
        if (domain == null) {
            domain = UtilProperties.getPropertyValue(Constants.DEFAULT_RESOURCE_FILE, "domain");
            putGlobalCache(Constants.DOMAIN_KEY, domain);
        }
        return domain;
    }

}
