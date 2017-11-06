package com.petinn.restful.base;
import com.petinn.component.cache.EcommerceGlobalCache;
import javolution.util.FastList;
import javolution.util.FastMap;
import net.sf.json.JSONSerializer;
import org.ofbiz.base.util.*;
import org.ofbiz.base.util.cache.UtilCache;
import com.petinn.util.BasePosObject;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.entity.transaction.GenericTransactionException;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.entity.util.EntityFindOptions;
import org.ofbiz.entity.util.EntityListIterator;
import org.ofbiz.service.GenericServiceException;
import com.petinn.util.ConvertUtil;
import com.petinn.util.JSONUtil;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RESTfulForCommonDatabaseBase
 *
 * @author lj
 * @date 2017/10/29
 */
@Path("commdbhandle")
public class RESTfulForCommonDatabaseBase extends RESTfulForBase {
    public static final String module = RESTfulForCommonDatabaseBase.class.getName();
    public static final String T_TYPE_TABLLE = "T"; //表
    public static final String T_TYPE_VIEW = "V";   //视图
    public static final String T_TYPE_ENTITY = "E"; //实体
    public static final String T_TYPE_SQL = "S";    //SQL
    public static final String O_TYPE_SAVE = "S";   //保存
    public static final String O_TYPE_DELETE = "D"; //删除
    public static final String O_TYPE_UPDATE = "U"; //更新
    public static final String O_TYPE_QUERY = "Q";  //查询

    /**
     * 查询
     * @param jsonParams
     * @param response
     * @return
     */
    @POST
    @Path("query")
    @Produces(MediaType.APPLICATION_JSON)
    public String query(@FormParam(value = "jsonParams") String jsonParams,
                        @Context HttpServletResponse response){
        //设置响应头编码格式
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=UTF-8");
        Map<String, Object> queryResultMap = FastMap.newInstance();
        PrintWriter out = null;
        BasePosObject pObject = new BasePosObject();
        Delegator delegator = getDelegator();
        boolean isSuccess = true;
        try {
            out = response.getWriter();
            Map<String, Object> jsonObj = UtilValidate.isNotEmpty(jsonParams) ? JSONUtil.json2Map(jsonParams): new HashMap<String, Object>();
            String rid =  UtilValidate.isNotEmpty(jsonObj.get("rid")) ? jsonObj.get("rid").toString() : "";
            String olist =  UtilValidate.isNotEmpty(jsonObj.get("olist")) ? jsonObj.get("olist").toString() : "";
            List<Map<String, Object>> operateList = (List<Map<String, Object>>)jsonObj.get("olist");
            // List<Map<String, Object>> operateList = (List<Map<String, Object>>)JSONUtil.json2Bean(olist, List.class);
            for(Map<String, Object> operateMap : operateList){
                if(UtilValidate.isEmpty(operateMap.get("tname")) || UtilValidate.isEmpty(operateMap.get("ttype")) ||
                        UtilValidate.isEmpty(operateMap.get("otype"))){
                    isSuccess = false;
                    break;
                }

                Map<String, Object> resultMap = executeQuery(operateMap);
                List<Integer> totalNumList = FastList.newInstance();
                List<List<Map<String, Object>>> dataList = FastList.newInstance();
                //如果查询结果中存在，则增加
                if(UtilValidate.isNotEmpty(queryResultMap.get("dataList")) && UtilValidate.isNotEmpty(queryResultMap.get("totalNum")))
                {
                    totalNumList.add(Integer.valueOf(""+resultMap.get("totalNum")));
                    dataList.add((List<Map<String, Object>>)resultMap.get("dataList"));
                    queryResultMap.remove("totalNum");
                    queryResultMap.remove("dataList");
                    queryResultMap.put("totalNum", totalNumList);
                    queryResultMap.put("dataList", dataList);
                    //如果查询结果中不存在，则插入
                }else{
                    queryResultMap.put("totalNum", resultMap.get("totalNum"));
                    queryResultMap.put("dataList", resultMap.get("dataList"));
                }
            }
            if(isSuccess) {
                pObject.setFlag("S");
                pObject.setMsg("查询成功");
                pObject.setData(queryResultMap);
            }else{
                pObject.setFlag("F");
                pObject.setMsg("参数不正确");
            }
        }catch (Exception e) {
            if (Debug.errorOn()) {
                Debug.logError(e, module);
            }
            pObject.setFlag("F");
            pObject.setMsg("服务错误:"+e.toString());
            pObject.setData("");
        }
        out.println(JSONSerializer.toJSON(JSONUtil.bean2Json(pObject)).toString());
        out.flush();
        out.close();
        return null;
    }

    /**
     * 更新
     * @param jsonParams
     * @param response
     * @return
     */
    @POST
    @Path("update")
    @Produces(MediaType.APPLICATION_JSON)
    public String update(@FormParam(value = "jsonParams") String jsonParams,
            @Context HttpServletResponse response){
        //设置响应头编码格式
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=UTF-8");
        Map<String, Object> resultMap = FastMap.newInstance();
        PrintWriter out = null;
        BasePosObject pObject = new BasePosObject();
        Delegator delegator = getDelegator();
        boolean isSuccess = true;
        try {
                TransactionUtil.begin(30);
                out = response.getWriter();
                Map<String, Object> jsonObj = UtilValidate.isNotEmpty(jsonParams) ? JSONUtil.json2Map(jsonParams): new HashMap<String, Object>();
                String tid =  UtilValidate.isNotEmpty(jsonObj.get("rid")) ? jsonObj.get("rid").toString() : "";
                String olist =  UtilValidate.isNotEmpty(jsonObj.get("olist")) ? jsonObj.get("olist").toString() : "";
                List<Map<String, Object>> operateList = (List<Map<String, Object>>)JSONUtil.json2Bean(olist, List.class);
                for(Map<String, Object> operateMap : operateList){
                    if(UtilValidate.isEmpty(operateMap.get("tname")) || UtilValidate.isEmpty(operateMap.get("ttype")) ||
                            UtilValidate.isEmpty(operateMap.get("otype")) || UtilValidate.isEmpty(operateMap.get("tdata"))){
                        isSuccess = false;
                        break;
                    }
                    //如果操作类型为创建
                    if(O_TYPE_SAVE.equals(operateMap.get("otype"))){
                        executeSave(operateMap);
                    //如果操作类型为删除
                    }else if(O_TYPE_DELETE.equals(operateMap.get("otype"))){
                        executeDelete(operateMap);
                    //如果操作类型为更新
                    }else if(O_TYPE_UPDATE.equals(operateMap.get("otype"))){
                        executeUpdate(operateMap);
                    }
                }
                if(isSuccess) {
                    pObject.setFlag("S");
                    pObject.setMsg("保存成功");
                    TransactionUtil.commit();
                }else{
                    pObject.setFlag("F");
                    pObject.setMsg("参数不正确");
                    TransactionUtil.rollback();
                }
            }catch (Exception e) {
                if (Debug.errorOn()) {
                    Debug.logError(e, module);
                }
                try {
                    TransactionUtil.rollback();
                } catch (GenericTransactionException e1) {
                    e1.printStackTrace();
                    Debug.logError(e1.toString(), module);
                }
                pObject.setFlag("F");
                pObject.setMsg("服务错误:"+e.toString());
                pObject.setData("");
        }
        out.println(JSONSerializer.toJSON(JSONUtil.bean2Json(pObject)).toString());
        out.flush();
        out.close();
        return null;
    }

    /**
     * 查询
     * @param operateMap
     * @return
     */
    public Map<String, Object> executeQuery(Map<String, Object> operateMap){
        Map<String, Object> resultMap = FastMap.newInstance();
        List dataList = FastList.newInstance();
        //如果为查询实体
        if(T_TYPE_ENTITY.equals(operateMap.get("ttype"))){
            resultMap = executeEntityQuery(String.valueOf(operateMap.get("tname")), String.valueOf(operateMap.get("torder")),
                    UtilValidate.isNotEmpty(operateMap.get("tdata")) ? JSONUtil.json2Map(String.valueOf(operateMap.get("tdata"))): null);
        }
        //如果为查询表或直接SQL查询
        else if(T_TYPE_TABLLE.equals(operateMap.get("ttype")) || T_TYPE_SQL.equals(operateMap.get("ttype"))){
            Map<String, Object> paramMap = UtilValidate.isNotEmpty(operateMap.get("tdata")) ? JSONUtil.json2Map(String.valueOf(operateMap.get("tdata"))) : null;
            resultMap = executeSqlQuery(getQuerySql(operateMap), paramMap);
        }
        return  resultMap;
    }

    /**
     * 保存
     * @param operateMap
     * @return
     */
    public void executeSave(Map<String, Object> operateMap) throws GenericEntityException{
        Map<String, Object> resultMap = FastMap.newInstance();
        //如果为操作实体
        if(T_TYPE_ENTITY.equals(operateMap.get("ttype"))){
            entitySave(operateMap);
        }else{
            tableSave(operateMap);
        }
    }

    /**
     * 删除
     * @param operateMap
     * @return
     */
    public void executeDelete(Map<String, Object> operateMap) throws Exception{
        Map<String, Object> resultMap = FastMap.newInstance();
        //如果为操作实体
        if(T_TYPE_ENTITY.equals(operateMap.get("ttype"))){
            entityDelete(operateMap);
        }else{
            tableDelete(operateMap);
        }
    }

    /**
     * 更新
     * @param operateMap
     * @return
     */
    public void executeUpdate(Map<String, Object> operateMap) throws Exception{
        Map<String, Object> resultMap = FastMap.newInstance();
        //如果为操作实体
        if(T_TYPE_ENTITY.equals(operateMap.get("ttype"))){
            entityUpdate(operateMap);
        }else{
            tableUpdate(operateMap);
        }
    }

    /**
     * 实体保存
     * @param operateMap
     * @return
     */
    public void entitySave(Map<String, Object> operateMap) throws GenericEntityException{
        Delegator delegator = EcommerceGlobalCache.getInstance().getDelegator();
        Map<String, Object> resultMap = FastMap.newInstance();
        //如果表编号需要数据库端生成
        if(String.valueOf(operateMap.get("tdata")).contains("#UUID#")){
            operateMap.put("tdata", String.valueOf(operateMap.get("tdata")).replace("#UUID#", delegator.getNextSeqId(String.valueOf(operateMap.get("tname")))));
        }
        GenericValue genericValue = delegator.makeValidValue(String.valueOf(operateMap.get("tname")), JSONUtil.json2Map(String.valueOf(operateMap.get("tdata"))));
        delegator.create(genericValue);
    }

    /**
     * 表保存
     * @param operateMap
     * @return
     */
    public void tableSave(Map<String, Object> operateMap) throws GenericEntityException{
        Delegator delegator = EcommerceGlobalCache.getInstance().getDelegator();
        Map<String, Object> resultMap = FastMap.newInstance();
        //如果表编号需要数据库端生成
        if(String.valueOf(operateMap.get("tdata")).contains("#UUID#")){
            operateMap.put("tdata", String.valueOf(operateMap.get("tdata")).replace("#UUID#", delegator.getNextSeqId(String.valueOf(operateMap.get("tname")))));
        }
        GenericValue genericValue = delegator.makeValidValue(String.valueOf(operateMap.get("tname")), JSONUtil.json2Map(String.valueOf(operateMap.get("tdata"))));
        delegator.create(genericValue);
    }

    /**
     * 实体删除
     * @param operateMap
     * @return
     */
    public int entityDelete(Map<String, Object> operateMap) throws Exception{
        Delegator delegator = EcommerceGlobalCache.getInstance().getDelegator();
        Map<String, Object> resultMap = FastMap.newInstance();
        int count = 0;
        //如果为操作实体
        if(T_TYPE_ENTITY.equals(operateMap.get("ttype"))){
            List<GenericValue> genericValueList = delegator.findByAnd(String.valueOf(operateMap.get("tname")), JSONUtil.json2Map(String.valueOf(operateMap.get("tdata"))));
            count = delegator.removeAll(genericValueList);
        }
        return  count;
    }

    /**
     * 表删除
     * @param operateMap
     * @return
     */
    public int tableDelete(Map<String, Object> operateMap) throws Exception{
        Delegator delegator = EcommerceGlobalCache.getInstance().getDelegator();
        Map<String, Object> resultMap = FastMap.newInstance();
        int count = 0;
        //如果为操作实体
        if(T_TYPE_ENTITY.equals(operateMap.get("ttype"))){
            List<GenericValue> genericValueList = delegator.findByAnd(String.valueOf(operateMap.get("tname")), JSONUtil.json2Map(String.valueOf(operateMap.get("tdata"))));
            count = delegator.removeAll(genericValueList);
        }
        return  count;
    }

    /**
     * 实体更新
     * @param operateMap
     * @return
     */
    public void entityUpdate(Map<String, Object> operateMap) throws GenericEntityException{
        Delegator delegator = EcommerceGlobalCache.getInstance().getDelegator();
        //如果为操作表
        if(T_TYPE_TABLLE.equals(operateMap.get("ttype"))){
            GenericValue genericValue = delegator.makeValidValue(String.valueOf(operateMap.get("tname")), JSONUtil.json2Map(String.valueOf(operateMap.get("tdata"))));
            delegator.createOrStore(genericValue);
        }
    }

    /**
     * 表更新
     * @param operateMap
     * @return
     */
    public void tableUpdate(Map<String, Object> operateMap) throws GenericEntityException{
        Delegator delegator = EcommerceGlobalCache.getInstance().getDelegator();
        //如果为操作表
        if(T_TYPE_TABLLE.equals(operateMap.get("ttype"))){
            GenericValue genericValue = delegator.makeValidValue(String.valueOf(operateMap.get("tname")), JSONUtil.json2Map(String.valueOf(operateMap.get("tdata"))));
            delegator.createOrStore(genericValue);
        }
    }

    /**
     * 获取查询sql
     * @param operateMap
     * @return
     */
    private String getQuerySql(Map<String, Object> operateMap){
        List<Map<String, Object>> dataList = FastList.newInstance();
        String sql = "";
        if(T_TYPE_TABLLE.equals(operateMap.get("ttype")))
        {
            sql = "select * from " + operateMap.get("tname");
            //todo 当存在查询条件时
            if(UtilValidate.isNotEmpty(operateMap.get("tdata"))){

            }
        }else if(T_TYPE_SQL.equals(operateMap.get("ttype")))
        {
            GenericValue paramObject = (GenericValue) EcommerceGlobalCache.getInstance().getGlobalCache().get(operateMap.get("tname"));;
            sql = UtilValidate.isNotEmpty(paramObject) ? String.valueOf(paramObject.get("paramValue")) : "" ;
        }
        return  sql;
    }

    /**
     * 执行带参数的实体对象查询,返回列表结果集
     * @param entityName
     * @return
     */
    public Map<String, Object> executeEntityQuery(String entityName, String orderBy){
        return  executeEntityQuery(entityName, orderBy, null);
    }

    /**
     * 执行不带参数的实体对象查询,返回列表结果集
     * @param entityName
     * @param paramMap
     * @return
     */
    public Map<String, Object> executeEntityQuery(String entityName, String orderBy, Map<String, Object> paramMap){
        Delegator delegator = EcommerceGlobalCache.getInstance().getDelegator();
        Map<String, Object> resultMap = FastMap.newInstance();
        List dataList = FastList.newInstance();
        int viewIndex =  1;//页数
        int viewSize = 10; //条数
        int listSize = 0;
        EntityListIterator iterator = null;
        EntityCondition entityCondition = null;
        EntityFindOptions options = null;
        List<String> orderByList = (UtilValidate.isNotEmpty(orderBy) && !"null".equals(orderBy)) ?
                java.util.Arrays.asList(orderBy.split(";")) : new ArrayList<String>();//排序

        //分页查询
        if(UtilValidate.isNotEmpty(paramMap)  && UtilValidate.isNotEmpty(paramMap.get("viewIndex")) && UtilValidate.isNotEmpty(paramMap.get("viewSize"))){
            viewIndex = Integer.parseInt(String.valueOf(paramMap.get("viewIndex")));
            viewSize = Integer.parseInt(String.valueOf(paramMap.get("viewSize")));
            paramMap.remove("viewIndex");
            paramMap.remove("viewSize");
            options = new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, false);
            options.setMaxRows(viewSize * viewIndex);
        }

        //查询条件
        if(UtilValidate.isNotEmpty(paramMap)) {
            List<EntityCondition> condList = FastList.newInstance();
            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                condList.add(EntityCondition.makeCondition(entry.getKey(), EntityOperator.EQUALS, entry.getValue()));
            }
            entityCondition = EntityCondition.makeCondition(condList, EntityOperator.AND);
        }

        try{
            iterator = delegator.find(entityName, entityCondition, null, null, orderByList, options);
            listSize = iterator.getResultsSizeAfterPartialList();
            dataList = iterator.getPartialList(viewSize * (viewIndex - 1) + 1, viewSize);
            iterator.close();

            resultMap.put("totalNum", listSize);
            resultMap.put("dataList", dataList);
        }catch (Exception e1){
            e1.printStackTrace();
            Debug.logError(e1.toString(), module);
        }
        return  resultMap;
    }

    /**
     * 执行不带参数的查询,返回列表结果集
     * @param sql
     * @return
     */
    public Map<String, Object> executeSqlQuery(String sql){
        return executeSqlQuery(sql, null);
    }

    /**
     * 执行带参数的查询,返回列表结果集
     * @param querySql
     * @param paramMap
     * @return
     */
    public Map<String, Object> executeSqlQuery(String querySql, Map<String, Object> paramMap){
        Delegator delegator = EcommerceGlobalCache.getInstance().getDelegator();
        SQLProcessor sqlProc = new SQLProcessor(delegator.getGroupHelperInfo("org.ofbiz"));
        Map<String, Object> resultMap = FastMap.newInstance();
        List<Map<String, Object>> dataList = FastList.newInstance();
        ResultSet rsCount = null;
        ResultSet rsRecord = null;
        int viewIndex =  1;//页数
        int viewSize = 10; //条数
        int listSize = 0;
        StringBuilder sqlCount = new StringBuilder("SELECT count(1) ct FROM ( ");
        sqlCount.append(querySql);
        sqlCount.append("))");

        StringBuilder sqlRecord = new StringBuilder();
        //分页查询
        if(UtilValidate.isNotEmpty(paramMap)  && UtilValidate.isNotEmpty(paramMap.get("viewIndex")) && UtilValidate.isNotEmpty(paramMap.get("viewSize"))){
            viewIndex = Integer.parseInt(String.valueOf(paramMap.get("viewIndex")));
            viewSize = Integer.parseInt(String.valueOf(paramMap.get("viewSize")));
            sqlRecord.append("SELECT * FROM ( SELECT a.*, ROWNUM rn FROM ( ");
            sqlRecord.append(querySql);
            sqlRecord.append(") a WHERE ROWNUM <= ").append(viewSize * viewIndex);
            sqlRecord.append(" ) WHERE rn > ").append(viewSize * (viewIndex - 1));
            paramMap.remove("viewIndex");
            paramMap.remove("viewSize");
        //全量查询
        }else{
            sqlRecord.append(querySql);
        }

        //查询总记录数
        try {
            sqlProc.prepareStatement(sqlCount.toString());
            if(UtilValidate.isNotEmpty(paramMap)){
                for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                    sqlProc.setValue(entry.getValue());
                }
            }
            rsCount = sqlProc.executeQuery(sqlCount.toString());
            while (rsCount.next()) {
                listSize = rsCount.getInt("CT");
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        resultMap.put("totalNum", listSize);

        //查询记录数据
        try {
            sqlProc.prepareStatement(sqlRecord.toString());
            if(UtilValidate.isNotEmpty(paramMap)){
                for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                    sqlProc.setValue(entry.getValue());
                }
            }
            rsRecord = sqlProc.executeQuery(sqlRecord.toString());
            ResultSetMetaData md = rsRecord.getMetaData(); //得到结果集(rs)的结构信息，比如字段数、字段名等
            int columnCount = md.getColumnCount(); //返回此 ResultSet 对象中的列数
            Map rowData = new HashMap();
            while (rsRecord.next()) {
                rowData = new HashMap(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                    //System.out.println(md.getColumnType(i) +" md.getColumnClassName():" + md.getColumnClassName(i));
                    //if(Types.TIMESTAMP == md.getColumnType(i)){
                    //    rowData.put(md.getColumnName(i), UtilValidate.isNotEmpty(rsRecord.getObject(i)) ?
                    //            ConvertUtil.convertTimestampToString(((mysql.sql.TIMESTAMP) rsRecord.getObject(i)).timestampValue()) : "");
                    //}else{
                        rowData.put(tableFieldToModelField(md.getColumnName(i)), UtilValidate.isNotEmpty(rsRecord.getObject(i)) ? rsRecord.getObject(i) : "");
                    //}
                }
                dataList.add(rowData);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

        resultMap.put("dataList", dataList);
        return resultMap;
    }

    /**
     * 实体字段转表字段
     * @param field
     * @return
     */
    public static String modelFieldToTableField(String field){
        Pattern pattern = Pattern.compile("[^_][A-Z]");
        Matcher matcher = pattern.matcher(field);
        while(matcher.find()){
            field = field.replaceFirst(matcher.group().substring(1), "_"+matcher.group().substring(1));
        }

        return field.toUpperCase();
    }

    /**
     * 表字段转实体字段
     * @param field
     * @return
     */
    public static String tableFieldToModelField(String field){
        Pattern pattern = Pattern.compile("[_][a-z]");
        Matcher matcher = pattern.matcher(field.toLowerCase());
        while(matcher.find()){
            field = field.replaceFirst(matcher.group(), matcher.group().substring(1).toUpperCase());
        }

        return field;
    }

    /**
     * 字符串大写字母前加下划线
     * @param param
     * @return
     */
    public static String upperCharAddUnderLine(String param){
        Pattern pattern = Pattern.compile("[^_][A-Z]");
        Matcher matcher = pattern.matcher(param);
        while(matcher.find()){
            param = param.replaceFirst(matcher.group().substring(1),"_"+matcher.group().substring(1));
        }
        return param;
    }

    /**
     * 字符串大写字母前减去划线
     * @param param
     * @return
     */
    public static String upperCharSubtractUnderLine(String param){
        Pattern pattern = Pattern.compile("[_][A-Z]");
        Matcher matcher = pattern.matcher(param);
        while(matcher.find()){
            param = param.replaceFirst(matcher.group(), matcher.group().substring(1));
        }
        return param;
    }

    /**
     * 字符串小写字母前减去划线
     * @param param
     * @return
     */
    public static String lowerCharSubtractUnderLine(String param){
        Pattern pattern = Pattern.compile("[_][a-z]");
        Matcher matcher = pattern.matcher(param);
        while(matcher.find()){
            param = param.replaceFirst(matcher.group(), matcher.group().substring(1));
        }
        return param;
    }

    public static void main(String[] args){

        System.out.println("======");
    }

}
