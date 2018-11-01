package com.songsy.iframe.core.persistence.provider;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Page<T> {
    private int end;                                        // 当前页尾条记录位置
    private int limit;                                      // 每页记录数
    private int page;                                       // 当前页
    private long total;                                     // 总记录数
    private String sortName;                                // 排序列
    private String sortOrder;                               // 排序方式
    private Map<String, Object> params = new HashMap<>();   // 请求参数
    private List<String> columns = Lists.newArrayList();    // 查询出来的参数
    private List<T> rows = new ArrayList<>();               // 记录

    public Page(int limit) {
        this.limit = limit;
    }

    public Page(HttpServletRequest request) {
        String param = null;
        String value = null;

        param = "pageIndex";
        value = request.getParameter(param);
        if (value != null && value.length() > 0) {
            this.page = Integer.parseInt(value);
        } else {
            this.page = 1;
        }

        param = "limit";
        value = request.getParameter(param);
        if (value != null && value.length() > 0) {
            this.limit = Integer.parseInt(value);
            if (this.limit > 15) {
                this.limit = 15;
            }
        } else {
            this.limit = 15;
        }

        param = "sortName";
        value = request.getParameter(param);
        if (value != null && value.length() > 0) {
            this.sortName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, value);
        }

        param = "sortOrder";
        value = request.getParameter(param);
        if (value != null && value.length() > 0) {
            this.sortOrder = value;
        } else {
            this.sortOrder = "asc";
        }

        Map<String, String[]> paramMap = request.getParameterMap();
        for (String key : paramMap.keySet()) {
            if (key.startsWith("s_")) {
                String vkey = key.substring(2);
                String[] _params = paramMap.get(key);
                if (_params.length > 0 && StringUtils.isNotEmpty(_params[0])) {
                    getParams().put(vkey, StringUtils.join(_params, ">").trim());
                }
            }
        }
    }

    /**
     * 当前页首条记录位置
     *
     * @return
     */
    public int getStart() {
        if (page == 0) {
            return 0;
        }
        return (page - 1) * limit;
    }

    /**
     * 总页数
     *
     * @return
     */
    public int getTotalPage() {
        if (limit == 0) {
            return 0;
        }
        return (int) Math.ceil(total / Double.valueOf(limit));
    }

    /**
     * 必须和数据库字段一致
     *
     * @param sortName
     */
    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    /**
     * 设置默认排序方式
     *
     * @param sortName  需要排序的表字段，数据库字段
     * @param sortOrder
     */
    public void sortDefault(String sortName, String sortOrder) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(getSortName()) || org.apache.commons.lang3.StringUtils.isEmpty(getSortOrder())) {
            setSortName(sortName);
            setSortOrder(sortOrder);
        }
    }

    public String getSortName() {
        return sortName;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public void setParams(String key, Object value) {
        Assert.notNull(key, "key must be not null");
        Assert.notNull(value, "value must be not null ");
        this.params.put(key, value);
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
