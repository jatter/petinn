package com.petinn.restful.product;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.petinn.restful.base.RESTfulForBase;
import javolution.util.FastList;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.model.DynamicViewEntity;
import org.ofbiz.entity.model.ModelKeyMap;
import org.ofbiz.entity.transaction.GenericTransactionException;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.entity.util.EntityFindOptions;
import org.ofbiz.entity.util.EntityListIterator;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.ModelService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import com.petinn.util.JavaWebToken;

/**
 * RESTfulForProductCard
 *
 * @author zangqisong
 * @date 2016/12/8
 */
@Path("product")
public class RESTfulForProductCard extends RESTfulForBase {
    public static final String module = RESTfulForProductCard.class.getName();

    @POST
    @Path("findAll")
    @Produces(MediaType.APPLICATION_JSON)
    public String findSelfCard(@FormParam(value = "viewIndex") String viewIndex,
                               @FormParam(value = "viewSize") String viewSize,
                               @FormParam(value = "productName") String productName,
                               @FormParam(value = "productStoreId") String productStoreId,
                               @Context HttpServletRequest request) {
        PrintWriter out = null;
        try {//分页查询商品列表
            Map<String, Object> resultMap = getLocalDispatcher().runSync("SOAFindAllProducts",
                    UtilMisc.toMap("viewIndex", viewIndex, "viewSize", viewSize, "productName", productName, "productStoreId", productStoreId));

            if (ModelService.RESPOND_SUCCESS.equals(resultMap.get("responseMessage"))) {    // 成功
                return success("productList", resultMap.get("productList"),"listSize", resultMap.get("listSize"));
            } else {    // 失败
                String errorMessage = (String) resultMap.get("errorMessage");

                Debug.logError(errorMessage, module);
                return failure(errorMessage);
            }
        } catch (GenericServiceException e) {
            e.printStackTrace();
            Debug.logError(e.toString(), module);
            return failure("服务错误");
        }
    }


    @POST
    @Path("findProductStoreGroup")
    @Produces(MediaType.APPLICATION_JSON)
    public String findProductStoreGroup(@Context HttpServletRequest request) {
        try {//分页查询列表
            Map<String, Object> resultMap = getLocalDispatcher().runSync("SOAFindProductStoreGroup", UtilMisc.toMap());
            if (ModelService.RESPOND_SUCCESS.equals(resultMap.get("responseMessage"))) {    // 成功
                return success("productStoreGroups", resultMap.get("productStoreGroups"), "listSize", resultMap.get("listSize"));
            } else {    // 失败
                String errorMessage = (String) resultMap.get("errorMessage");

                Debug.logError(errorMessage, module);
                return failure(errorMessage);
            }
        } catch (GenericServiceException e) {
            e.printStackTrace();
            Debug.logError(e.toString(), module);
            return failure("服务错误");
        }
    }

    @POST
    @Path("test")
    @Produces(MediaType.APPLICATION_JSON)
    public String findSelfCard(@Context HttpServletRequest request) {
        Map<String,Object> m = new HashMap<String,Object>();
        m.put("userid", "234241432412412421341");
        String token = JavaWebToken.createJavaWebToken(m);
        System.out.println(token);
        return failure(token);
    }


    @POST
    @Path("parser")
    @Produces(MediaType.APPLICATION_JSON)
    public String parser(@FormParam(value = "token") String token,
                         @Context HttpServletRequest request) {

        String token1 = request.getParameter("token1");
        System.out.println(token);
        if(JavaWebToken.parserJavaWebToken(token) != null){
            return failure(token);
            //表示token合法
        }else{
            //token不合法或者过期
            return success(token);
        }

    }

}
