package com.songsy.iframe.core.persistence.provider.utils;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.songsy.iframe.core.persistence.provider.Page;
import com.songsy.iframe.core.persistence.provider.constant.CommonConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

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

    public static String getWhere(Map<String, Object> params, String asTable) throws ParseException {
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
