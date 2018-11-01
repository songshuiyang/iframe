package com.songsy.iframe.core.persistence.provider.utils;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.songsy.iframe.core.persistence.provider.Page;
import com.songsy.iframe.core.persistence.provider.constant.CommonConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.songsy.iframe.core.persistence.provider.constant.CommonConstant.DATE_PATTERNS;

public class PageUtils {

    public static String getColumns(Page<?> page, String asTable) {
        if (page.getColumns().isEmpty()) {
            return asTable + ".*";
        } else {
            List<String> colums = page.getColumns().stream()
                    .map(n -> asTable + "." + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, n)).collect(Collectors.toList());
            return StringUtils.join(colums, ",");
        }
    }

    public static String getWhere(Map<String, Object> params, String asTable, Map<String, Map<String, Object>> joinTables, String tableName) throws ParseException {
        List<String> expresses = Lists.newArrayList();
        Set<String> cloneSet = params.keySet().stream().collect(Collectors.toSet());
        for (String key : cloneSet) {
            String value = "";
            List<String> values = Lists.newArrayList();
            switch (getOpt(key).toLowerCase()) {
                case "in":
                    // 将其转换成 list
                    if (params.get(key) instanceof List) {
                        values = (List<String>) params.get(key);
                    } else {
                        values = getFilterList(params.get(key).toString());
                    }
                    if (values.isEmpty()) {
                        break;
                    } else {
                        params.put(key, values);
                        // 分割 IN 的里面具体数据
                        List<String> inExpresses = Lists.newArrayList();

                        for (int i = 0; i < values.size(); i++) {
                            inExpresses.add("#{ params." + key + "[" + i + "]}");
                        }

                        expresses.add(asTable + "." + getExpressColumn(key) + " IN " + "(" + StringUtils.join(inExpresses, ",") + ")");
                    }
                    break;
                case "notin":
                    // 将其转换成 list
                    if (params.get(key) instanceof List) {
                        values = (List<String>) params.get(key);
                    } else {
                        values = getFilterList(params.get(key).toString());
                    }
                    if (values.isEmpty()) {
                        break;
                    } else {
                        params.put(key, values);
                        // 分割 IN 的里面具体数据
                        List<String> inExpresses = Lists.newArrayList();

                        for (int i = 0; i < values.size(); i++) {
                            inExpresses.add("#{ params." + key + "[" + i + "]}");
                        }

                        expresses.add(asTable + "." + getExpressColumn(key) + " NOT IN " + "(" + StringUtils.join(inExpresses, ",") + ")");
                    }
                    break;
                case "gt":
                    value = params.get(key).toString();
                    if (!StringUtils.isBlank(value)) {
                        expresses.add(asTable + "." + getExpressColumn(key) + " > #{params." + key + "}");
                    }
                    break;
                case "lt":
                    value = params.get(key).toString();
                    if (!StringUtils.isBlank(value)) {
                        expresses.add(asTable + "." + getExpressColumn(key) + " < #{params." + key + "}");
                    }
                    break;
                case "eq":
                    value = params.get(key).toString();
                    if (!StringUtils.isBlank(value)) {
                        expresses.add(asTable + "." + getExpressColumn(key) + " = #{params." + key + "}");
                    }
                    break;
                case "like":
                    value = params.get(key).toString();
                    if (!StringUtils.isBlank(value)) {
                        expresses.add(asTable + "." + getExpressColumn(key) + " like CONCAT(CONCAT('%', #{params." + key + "}), '%')");
                    }
                    break;
                case "btw":
                    value = params.get(key).toString();
                    if (value.indexOf("~") == -1) {
                        break;
                    }
                    String startStr = value.split("~")[0];
                    String endStr = value.split("~")[1];
                    Date start = DateUtils.parseDate(startStr, DATE_PATTERNS);
                    Date end = DateUtils.parseDate(endStr, DATE_PATTERNS);
                    params.put(key + "_start", start);
                    params.put(key + "_end", end);
                    if (!StringUtils.isBlank(startStr) && !StringUtils.isBlank(endStr)) {
                        expresses.add(asTable + "." + getExpressColumn(key) + " between #{params." + key + "_start"
                                + "} and #{params." + key + "_end" + "}");
                    }
                    break;
                case "null":
                    value = params.get(key).toString();
                    if (value.equals("true")) {
                        expresses.add(asTable + "." + getExpressColumn(key) + " is null");
                    } else {
                        expresses.add(asTable + "." + getExpressColumn(key) + " is not null");
                    }
                    break;
                case "not":
                    value = params.get(key).toString();
                    if (!StringUtils.isBlank(value)) {
                        expresses.add(asTable + "." + getExpressColumn(key) + " <> #{params." + key + "}");
                    }
                    break;
                case "match":
                    value = params.get(key).toString();
                    if (!StringUtils.isBlank(value)) {
                        expresses.add("match (" + asTable + "." + getExpressColumn(key) + ") against  (#{params." + key + "})");
                    }
                    break;
                case "join":
                    //join_表名对应的实体类_操作_字段
                    Assert.isTrue(key.split("_").length == 4, "连表格式操作非法");
                    // table
                    // on
                    // params
                    value = params.get(key).toString();
                    if (!StringUtils.isBlank(value)) {
                        String joinTableName = getJoinTable(key);//替换前缀
                        Map<String, Object> joinTable = Maps.newHashMap();
                        //初始化对象
                        if (joinTables.containsKey(joinTableName)) {
                            joinTable = joinTables.get(joinTableName);
                        } else {
                            joinTables.put(joinTableName, joinTable);
                        }

                        Map<String, Object> joinTableParams = Maps.newHashMap();
                        if (joinTable.containsKey("params")) {
                            joinTableParams = (Map<String, Object>) joinTable.get("params");
                        } else {
                            joinTable.put("params", joinTableParams);
                        }
                        joinTable.put("on", joinTableName);

                        joinTableParams.put(key.split("_")[2] + "_" + key.split("_")[3], value);
                        params.remove(key);
                    }
                default:
                    String[] optAndExpressColumn = key.split("_");
                    if (optAndExpressColumn.length == 1) {
                        expresses.add(asTable + "." + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, optAndExpressColumn[0])
                                + "= #{params." + key + "}");
                    }
                    break;
            }
        }
        return StringUtils.join(expresses, " and ");
    }


    public static String getOpt(String temp) {
        return temp.split("_")[0];
    }

    public static String getExpressColumn(String temp) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, temp.split("_")[1]);
    }

    public static String getJoinTable(String temp) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, temp.split("_")[1]);
    }

    public static String getJoinColumn(String tableName) {
        return tableName.replaceAll("ge", "fd") + "_id";
    }

    /**
     * 字符集过滤转换
     *
     * @param filterValue
     * @return
     */
    public static List<String> getFilterList(String filterValue) {
        List<String> filterList = Lists.newArrayList();
        if (StringUtils.isNotBlank(filterValue)) {
            filterValue = filterValue.trim().replace(CommonConstant.CHAR_COMMA_FULL, CommonConstant.CHAR_COMMA)
                    .replace(CommonConstant.CHAR_SPACE, CommonConstant.CHAR_COMMA)
                    .replace(CommonConstant.CHAR_SPACE_FULL, CommonConstant.CHAR_COMMA)
                    .replace(CommonConstant.CHAR_ENTER, CommonConstant.CHAR_COMMA);
            String[] filterArr = filterValue.split(CommonConstant.CHAR_COMMA);
            for (String filter : filterArr) {
                if (StringUtils.isNotBlank(filter.trim())) {
                    filterList.add(filter.trim());
                }
            }
        }
        return filterList;
    }
}
