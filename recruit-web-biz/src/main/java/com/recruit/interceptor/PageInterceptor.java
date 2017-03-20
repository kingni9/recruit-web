package com.recruit.interceptor;

import com.recruit.base.BaseQuery;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class PageInterceptor extends SqlSessionDaoSupport implements Interceptor {
	private static final Logger logger = LoggerFactory.getLogger(PageInterceptor.class);
	private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
	private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
	private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();
	private static String defaultDialect = "mysql";
	private static String defaultPageSqlId = ".*AutoPage$";
	private static String dialect = "";
	private static String pageSqlId = "";
	
	@Resource
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory){
		super.setSqlSessionFactory(sqlSessionFactory);
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY,
				DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);

		while (metaStatementHandler.hasGetter("h")) {
			Object object = metaStatementHandler.getValue("h");
			metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY,
					DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
		}

		while (metaStatementHandler.hasGetter("target")) {
			Object object = metaStatementHandler.getValue("target");
			metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY,
					DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
		}

		Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
		dialect = configuration.getVariables().getProperty("dialect");
		if (null == dialect || "".equals(dialect)) {
			logger.warn("Property dialect is not setted,use default 'mysql' ");
			dialect = defaultDialect;
		}

		pageSqlId = configuration.getVariables().getProperty("pageSqlId");
		if (null == pageSqlId || "".equals(pageSqlId)) {
			logger.warn("Property pageSqlId is not setted,use default '.*Page$' ");
			pageSqlId = defaultPageSqlId;
		}

		MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");

		if (mappedStatement.getId().matches(pageSqlId)) {
			BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
			Object parameterObject = boundSql.getParameterObject();
			if (parameterObject == null) {
				throw new NullPointerException("parameterObject is null!");
			} else {
				Object paramObject = metaStatementHandler.getValue("delegate.boundSql.parameterObject");
				BaseQuery page = getBaseQuery(paramObject);
				String sql = boundSql.getSql();
				Connection connection = (Connection) invocation.getArgs()[0];
				setPageParameter(sql, connection, mappedStatement, boundSql, page, paramObject); // 重设分页参数里的总页数等
				String pageSql = buildPageSql(sql, page); // 重写sql
				metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);
				metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET); // 采用物理分页后，就不需要mybatis的内存分页了，所以重置下面的两个参数
				metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
			}
		}

		// 将执行权交给下一个拦截器
		return invocation.proceed();
	}
	
	@SuppressWarnings("unchecked")
	public static BaseQuery getBaseQuery(Object paramObject){
		BaseQuery page = null;
		if (paramObject instanceof BaseQuery) {
			page = (BaseQuery)paramObject;
		} else if (paramObject instanceof Map) {
			Map<String, Object> parameterMap = ((Map<String, Object>) paramObject);
			for (Map.Entry<String, Object> entry : parameterMap.entrySet()) {
				Object valueObject = entry.getValue();
				if (valueObject instanceof BaseQuery) {
					page = (BaseQuery)valueObject;
					break;
				}
			}
		}
		if (page == null) {
			throw new NullPointerException("page is null!");
		}
		return page;
	}

	/**
	 * 从数据库里查询总的记录数并计算总页数，回写进分页参数<code>BaseQuery</code>,这样调用者就可用通过 分页参数
	 * <code>BaseQuery</code>获得相关信息。
	 * 
	 * @param sql
	 * @param connection
	 * @param mappedStatement
	 * @param boundSql
	 * @param page
	 */
	private void setPageParameter(String sql, Connection connection, MappedStatement mappedStatement,
								  BoundSql boundSql, BaseQuery page, Object paramObject) {
		// 记录总记录数
		String countSqlPrefix = "select count(0) as total ";
		int index = sql.indexOf("distinct");
		if (index > -1) {
			String alias = sql.substring(index + "distinct".length()).trim();
			alias = alias.substring(0, alias.indexOf(" "));
			countSqlPrefix = "select count(distinct " + alias + ") as total ";
		}else{
			index = sql.indexOf("DISTINCT");
			if (index > -1) {
				String alias = sql.substring(index + "DISTINCT".length()).trim();
				alias = alias.substring(0, alias.indexOf(" "));
				countSqlPrefix = "select count(distinct " + alias + ") as total ";
			}
		}
		
		index = sql.indexOf("order by");
		if (index > -1) {
			sql = sql.substring(0, index);
		}else {
			index = sql.indexOf("ORDER BY");
			if (index > -1) {
				sql = sql.substring(0, index);
			}
		}
		
		index = sql.indexOf("from");
		if (index > -1) {
			sql = sql.substring(index);
		}else{
			index = sql.indexOf("FROM");
			if (index > -1) {
				sql = sql.substring(index);
			}
		}

		String countSql = countSqlPrefix + sql;
		PreparedStatement countStmt = null;
		ResultSet rs = null;
		try {
			countStmt = connection.prepareStatement(countSql);
			BoundSql countBS = mappedStatement.getBoundSql(paramObject);
			setParameters(countStmt, mappedStatement, countBS, boundSql.getParameterObject());
			rs = countStmt.executeQuery();
			int totalCount = 0;
			if (rs.next()) {
				totalCount = rs.getInt(1);
			}
			page.setTotalRecord(totalCount);

		} catch (SQLException e) {
			logger.error("Ignore this exception", e);
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.error("Ignore this exception", e);
			}
			try {
				countStmt.close();
			} catch (SQLException e) {
				logger.error("Ignore this exception", e);
			}
		}
	}

	/**
	 * 对SQL参数(?)设值
	 * 
	 * @param ps
	 * @param mappedStatement
	 * @param boundSql
	 * @param parameterObject
	 * @throws SQLException
	 */
	private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject) throws SQLException {
		ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject,
				boundSql);
		parameterHandler.setParameters(ps);
	}

	/**
	 * 根据数据库类型，生成特定的分页sql
	 * 
	 * @param sql
	 * @param page
	 * @return
	 */
	private String buildPageSql(String sql, BaseQuery page) {
		if (page != null) {
			StringBuilder pageSql = new StringBuilder();
			if ("mysql".equals(dialect)) {
				pageSql = buildPageSqlForMysql(sql, page);
			} else {
				return sql;
			}
			return pageSql.toString();
		} else {
			return sql;
		}
	}

	/**
	 * mysql的分页语句
	 * 
	 * @param sql
	 * @param page
	 * @return String
	 */
	public StringBuilder buildPageSqlForMysql(String sql, BaseQuery page) {
		StringBuilder pageSql = new StringBuilder(100);
		int beginrow = page.getStartPosForMysql();
		pageSql.append(sql);
		pageSql.append(" limit " + beginrow + "," + page.getPageSize());
		return pageSql;
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {

	}
}
