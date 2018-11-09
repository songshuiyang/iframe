package com.songsy.iframe.core.persistence.provider.plugin;

import com.google.common.base.CaseFormat;
import com.songsy.iframe.core.persistence.provider.Page;
import com.songsy.iframe.core.persistence.provider.utils.JdbcUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * mybatis拦截器，实现接口分页，拦截Executor接口的query方法
 */
@Component
@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class }) })
public class PageInterceptor implements Interceptor {
    private static Logger logger = LoggerFactory.getLogger(PageInterceptor.class);

    static int MAPPED_STATEMENT_INDEX = 0;
    static int PARAMETER_INDEX = 1;
    static int ROWBOUNDS_INDEX = 2;
    static int RESULT_HANDLER_INDEX = 3;

    // 需要拦截的ID(正则匹配)
    private static final String DEFAULT_PAGE_SQL_ID = ".*Page$";

    @Override
    public void setProperties(Properties properties) {

    }

    @Override
    public Object plugin(Object o) {
        if (Executor.class.isAssignableFrom(o.getClass())) {
            return Plugin.wrap(new PageExecutor((Executor) o), this);
        }
        return Plugin.wrap(o, this);
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final Object[] queryArgs = invocation.getArgs();
        final MappedStatement mappedStatement = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
        final Object parameterObject = queryArgs[PARAMETER_INDEX];
        BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);

        if (mappedStatement.getId().matches(DEFAULT_PAGE_SQL_ID)) {
            if (parameterObject == null) {
                throw new NullPointerException("parameterObject is null!");
            } else {
                if (parameterObject instanceof Page<?>) {
                    Page<?> page = (Page<?>) parameterObject;
                    // 获取数据库连接
                    Connection con = mappedStatement.getConfiguration().getEnvironment().getDataSource()
                            .getConnection();
                    // 执行总记录数查询
                    setTotalRecord(page, mappedStatement, boundSql, con);

                    String orderSql = getOrderSql(boundSql.getSql(), page);
                    String pageSql = getPageSql(orderSql, page);

                    if (logger.isDebugEnabled()) {
                        logger.debug("page sql : {} ", pageSql);
                    }

                    BoundSql newBoundSql = copyFromBoundSql(mappedStatement, boundSql, pageSql);
                    MappedStatement newMappedStatement = copyFromMappedStatement(mappedStatement,
                            new BoundSqlSqlSource(newBoundSql));
                    queryArgs[ROWBOUNDS_INDEX] = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
                    queryArgs[MAPPED_STATEMENT_INDEX] = newMappedStatement;
                }
            }
        }
        return invocation.proceed();
    }

    public static BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql) {
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(),
                boundSql.getParameterObject());
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        return newBoundSql;
    }

    private static MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource,
                ms.getSqlCommandType());

        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        String[] keyProperties = ms.getKeyProperties();
        builder.keyProperty(keyProperties == null ? null : keyProperties[0]);

        // setStatementTimeout()
        builder.timeout(ms.getTimeout());

        // setStatementResultMap()
        builder.parameterMap(ms.getParameterMap());

        // setStatementResultMap()
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());

        // setStatementCache()
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    public static class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

    private void setTotalRecord(Page<?> page, MappedStatement mappedStatement, BoundSql boundSql, Connection con) {
        String sql = getCountSql(boundSql.getSql());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), sql, parameterMappings, page);
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, page, countBoundSql);

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            int total = 0;
            stmt = con.prepareStatement(sql);
            parameterHandler.setParameters(stmt);
            rs = stmt.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
            page.setTotal(total);
            logger.debug("page count sql : {}", sql);
            logger.debug("page count total : {}", total);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(rs, stmt);
            JdbcUtils.close(con);
        }
    }

    private String getCountSql(String sql) {
        int index = sql.indexOf("from") == -1 ? sql.indexOf("FROM") : sql.indexOf("from");
        return "select count(*) " + sql.substring(index);
    }

    private String getPageSql(String sql, Page<?> page) {
        if (page != null && page.getLimit() > 0) {
            StringBuilder pageSql = getMySQLPageSql(sql, page);
            return pageSql.toString();
        } else {
            return sql;
        }
    }

    /**
     * 得到排序sql
     * @param sql
     * @param page
     * @return
     */
    private String getOrderSql(String sql, Page<?> page) {
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(page.getSortName())) {
            StringBuilder pageSql = new StringBuilder(100);
            pageSql.append(sql);
            if ((page.getSortName().indexOf("_") == -1)) {
                page.setSortName(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, page.getSortName()));
            }
            if (("asc".equalsIgnoreCase(page.getSortOrder()) || "desc".equalsIgnoreCase(page.getSortOrder()))) {
                pageSql.append(" order by " + page.getSortName() + " " + page.getSortOrder());
            }
            return pageSql.toString();
        } else {
            return sql;
        }
    }

    /**
     * 得到mysql 分页语句
     * @param sql
     * @param page
     * @return
     */
    public StringBuilder getMySQLPageSql(String sql, Page page) {
        StringBuilder pageSql = new StringBuilder(100);
        pageSql.append(sql);
        pageSql.append(" limit " + page.getStart() + "," + page.getLimit());
        return pageSql;
    }
}
