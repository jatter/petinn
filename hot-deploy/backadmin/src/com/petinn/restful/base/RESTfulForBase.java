package com.petinn.restful.base;

import com.alibaba.fastjson.JSON;
import com.petinn.component.cache.EcommerceGlobalCache;
import net.sf.json.JSONObject;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.service.LocalDispatcher;

/**
 * Created by lijie on 10/29/17.
 */
public abstract class RESTfulForBase {
    protected Delegator getDelegator() { return EcommerceGlobalCache.getInstance().getDelegator(); }
    protected LocalDispatcher getLocalDispatcher() { return EcommerceGlobalCache.getInstance().getLocalDispatcher(); }
    protected String getLocalDomain() { return EcommerceGlobalCache.getInstance().getCurrentDomain(); }

    protected String success() {
        return success("");
    }

    protected String success(String message) {
        return success("message", message);
    }

    protected String success(Object... params) {
         JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", "0");
        int length = params.length;
        if (length % 1 != 0) { throw new IllegalArgumentException("invalid argument params isn't match"); }
        if ("".equals(params[1]))   return jsonObject.toString();

        for (int i = 0; i < length; i++) {
            if ("java.lang.String".equals(params[i+1].getClass().getName())) {
                jsonObject.put(params[i], params[i+1]);
            } else {
                jsonObject.put(params[i], JSON.toJSONString(params[i+1]));
            }
            i+=1;
        }
        return jsonObject.toString();
        /*for (int i = 0; i < length; i++) {

            jsonObject.put(params[i].toString(), JSON.toJSONString(params[i + 1]));
            i += 1;
        }
        return jsonObject.toString();*/
    }

    protected String successForOrder(Object... params) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", "0");
        int length = params.length;
        if (length % 1 != 0) { throw new IllegalArgumentException("invalid argument params isn't match"); }
        for (int i = 0; i < length; i++) {
            jsonObject.put(params[i].toString(), params[i + 1]);
            i += 1;
        }
        return jsonObject.toString();
    }

    protected String failure() {
        return failure("");
    }

    protected String failure(String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", "1");
        if (!(message == null || "".equals(message)))
            jsonObject.put("message", message);
        return jsonObject.toString();
    }
}
