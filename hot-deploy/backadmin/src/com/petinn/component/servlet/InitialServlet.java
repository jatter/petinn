package com.petinn.component.servlet;

import com.petinn.component.cache.EcommerceGlobalCache;

import org.ofbiz.base.util.Debug;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.service.LocalDispatcher;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class InitialServlet extends HttpServlet {

    public static final String module = InitialServlet.class.getName();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        Delegator delegator = (Delegator) getServletContext().getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) getServletContext().getAttribute("dispatcher");

        EcommerceGlobalCache instance = EcommerceGlobalCache.getInstance();
        instance.putGlobalCache("delegator", delegator);
        instance.putGlobalCache("dispatcher", dispatcher);

        // 初始化系统参数
        initialCacheSystemParam(instance);
        System.out.println("......................................... cached");
    }

    private void initialCacheSystemParam(EcommerceGlobalCache instance) {
        Delegator delegator = instance.getDelegator();
        try {
            List<GenericValue> systemParamList = delegator.findByAnd("SystemParam", (Object[]) null);
            for (GenericValue systemParam : systemParamList) {
                instance.putGlobalCache(systemParam.getString("paramName"), systemParam);
            }
        } catch (GenericEntityException e) {
            Debug.logError(e.toString(), module);

            throw new ExceptionInInitializerError(e);
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}

