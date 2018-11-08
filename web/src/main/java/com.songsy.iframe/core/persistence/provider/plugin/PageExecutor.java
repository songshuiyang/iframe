package com.songsy.iframe.core.persistence.provider.plugin;

import com.songsy.iframe.core.persistence.provider.Page;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class PageExecutor implements Executor {
    private static Logger logger = LoggerFactory.getLogger(PageExecutor.class);

    private final Executor executor;

    public PageExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler,
                             CacheKey cacheKey, BoundSql boundSql) throws SQLException {
        final List<E> rows = executor.query(ms, parameter, rowBounds, resultHandler);
        if (parameter != null && parameter instanceof Page<?>) {
            Page<E> page = (Page<E>) parameter;
            doCache(ms, page, parameter, rowBounds);
            page.setRows(rows);
        }
        return rows;
    }

    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException {
        final List<E> rows = executor.query(ms, parameter, rowBounds, resultHandler);
        if (parameter != null && parameter instanceof Page<?>) {
            Page<E> page = (Page<E>) parameter;
            doCache(ms, page, parameter, rowBounds);
            page.setRows(rows);
        }
        return rows;
    }

    private <E> void doCache(MappedStatement ms, Page<E> result, Object parameter, RowBounds rowBounds) {
        final Cache cache = ms.getCache();
        if (executor.getClass().isAssignableFrom(CachingExecutor.class) && cache != null) {
            BoundSql boundSql = ms.getBoundSql(parameter);
            final CacheKey cacheKey = createCacheKey(ms, parameter, rowBounds, boundSql);
            if (logger.isDebugEnabled()) {
                logger.debug("cache executor the cache's kye  is " + cacheKey);
            }
            cache.putObject(cacheKey, result);
        }
    }

    @Override
    public void setExecutorWrapper(Executor executor) {
        executor.setExecutorWrapper(executor);
    }

    @Override
    public int update(MappedStatement ms, Object parameter) throws SQLException {
        return executor.update(ms, parameter);
    }

    @Override
    public List<BatchResult> flushStatements() throws SQLException {
        return executor.flushStatements();
    }

    @Override
    public void commit(boolean required) throws SQLException {
        executor.commit(required);
    }

    @Override
    public void rollback(boolean required) throws SQLException {
        executor.rollback(required);
    }

    @Override
    public CacheKey createCacheKey(MappedStatement ms, Object parameterObject, RowBounds rowBounds, BoundSql boundSql) {
        return executor.createCacheKey(ms, parameterObject, rowBounds, boundSql);
    }

    @Override
    public boolean isCached(MappedStatement ms, CacheKey key) {
        return executor.isCached(ms, key);
    }

    @Override
    public void clearLocalCache() {
        executor.clearLocalCache();
    }

    @Override
    public void deferLoad(MappedStatement mappedStatement, MetaObject metaObject, String s, CacheKey cacheKey, Class<?> aClass) {
        executor.deferLoad(mappedStatement, metaObject, s, cacheKey, aClass);
    }

    @Override
    public Transaction getTransaction() {
        return executor.getTransaction();
    }

    @Override
    public void close(boolean forceRollback) {
        executor.close(forceRollback);
    }

    @Override
    public boolean isClosed() {
        return executor.isClosed();
    }

	@Override
	public <E> Cursor<E> queryCursor(MappedStatement ms, Object parameter, RowBounds rowBounds) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}
