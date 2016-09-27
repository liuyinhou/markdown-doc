package com.get.markdown.doc.dao;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.get.markdown.doc.utils.OrmUtil;

/**
 * @author inigo.liu
 * 
 */
public abstract class BaseDao<T> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 获得数据库连接
	 * 
	 * @return
	 */
	public Connection getConnection() {
		return ConnectionFactory.getConnection();
	}

	public T findById(Integer id) {
		T obj = null;
		String preparedSql = "select * from " + OrmUtil.getTableName(getTClass()) + " where id=?";
		PreparedStatement pstmt = null;
		ResultSet reSet = null;
		try {
			pstmt = getConnection().prepareStatement(preparedSql);
			pstmt.setInt(1, id);
			reSet = pstmt.executeQuery();
			Map<String, Field> fieldMap = OrmUtil.metchClassFieldForDb(getTClass());
			while (reSet.next()) {
				obj = getTClass().newInstance();
				ResultSetMetaData metaData = reSet.getMetaData();
				int columns = metaData.getColumnCount();
				// 显示列,表格的表头
				for (int i = 1; i <= columns; i++) {
					Field field = fieldMap.get(metaData.getColumnName(i).toUpperCase());
					if (field != null) {
						field.set(obj, reSet.getObject(metaData.getColumnName(i)));
					}
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return obj;
	}

	/**
	 * 条件查询
	 * @param page
	 * @param query
	 * @param sort
	 */
	public List<T> find(Map<String, Object> query, String sort) {
		return findPage(null, null, query, sort);
	}
	
	/**
	 * 分页查询
	 * @param page
	 * @param query
	 * @param sort
	 */
	public List<T> findPage(Integer pageNum, Integer pageSize, Map<String, Object> query, String sort) {
		List<T> result = new ArrayList<T>();
		StringBuffer preparedSql = new StringBuffer();
		preparedSql.append("select * from ").append(OrmUtil.getTableName(getTClass())).append(" where 1=1 ");
		PreparedStatement pstmt = null;
		ResultSet reSet = null;
		try {
			//查询条件
			List<Object> params = new ArrayList<Object>();
			if (query != null && !query.isEmpty()) {
				for (Entry<String, Object> one : query.entrySet()) {
					String key = one.getKey();
					if (!OrmUtil.isEndWithOperator(key)) {
						key += "=";
					}
					preparedSql.append("and ").append(key).append("? ");
					params.add(one.getValue());
				}
			}
			//排序
			if (!StringUtils.isEmpty(sort)) {
				preparedSql.append("order by ").append(sort).append(" ");
			}
			//分页
			boolean isPage = false;
			if (pageNum != null
					&& pageNum > 0
					&& pageSize !=null
					&& pageSize > 0) {
				isPage = true;
				preparedSql.append("limit ?,?");
			}
			//填充参数
			pstmt = getConnection().prepareStatement(preparedSql.toString());
			int index = 1;
			for (Object obj : params) {
				pstmt.setObject(index, obj);
				index++;
			}
			if (isPage) {
				int start = (pageNum-1)*pageSize;
				pstmt.setObject(index, start);
				index++;
				pstmt.setObject(index, pageSize);
			}
			
			reSet = pstmt.executeQuery();
			
			Map<String, Field> fieldMap = OrmUtil.metchClassFieldForDb(getTClass());
			while (reSet.next()) {
				T obj = getTClass().newInstance();
				ResultSetMetaData metaData = reSet.getMetaData();
				int columns = metaData.getColumnCount();
				// 显示列,表格的表头
				for (int i = 1; i <= columns; i++) {
					Field field = fieldMap.get(metaData.getColumnName(i).toUpperCase());
					if (field != null) {
						field.set(obj, reSet.getObject(metaData.getColumnName(i)));
					}
				}
				result.add(obj);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return result;
	}
	
	/**
	 * 统计数量
	 * @param page
	 * @param query
	 * @param sort
	 */
	public Integer count(Map<String, Object> query) {
		Integer result = 0;
		StringBuffer preparedSql = new StringBuffer();
		preparedSql.append("select count(id) from ").append(OrmUtil.getTableName(getTClass())).append(" where 1=1 ");
		PreparedStatement pstmt = null;
		ResultSet reSet = null;
		try {
			//查询条件
			List<Object> params = new ArrayList<Object>();
			if (query != null && !query.isEmpty()) {
				for (Entry<String, Object> one : query.entrySet()) {
					String key = one.getKey();
					if (!OrmUtil.isEndWithOperator(key)) {
						key += "=";
					}
					preparedSql.append("and ").append(key).append("? ");
					params.add(one.getValue());
				}
			}
			
			//填充参数
			pstmt = getConnection().prepareStatement(preparedSql.toString());
			int index = 1;
			for (Object obj : params) {
				pstmt.setObject(index, obj);
				index++;
			}
			reSet = pstmt.executeQuery();
			while (reSet.next()) {
				result = reSet.getInt(1);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return result;
	}

	public Integer save(T t) {
		Map<String, Field> fieldMap = OrmUtil.metchClassFieldForDb(getTClass());
		List<Field> fields = new ArrayList<Field>();
		//拼接INSERT语句
		int size = 0;
		StringBuffer preparedSql = new StringBuffer();
		preparedSql.append("INSERT INTO ").append(OrmUtil.getTableName(getTClass())).append("(");
		for (Entry<String, Field> one : fieldMap.entrySet()) {
			if ("id".equalsIgnoreCase(one.getKey())) {
				//id不赋值，使用数据库的自增
				continue;
			}
			size ++;
			preparedSql.append(one.getKey()).append(",");
			fields.add(one.getValue());
		}
		preparedSql.deleteCharAt(preparedSql.length() - 1);
		preparedSql.append(") values(");
		for (int i=0;i<size;i++) {
			preparedSql.append("?,");
		}
		preparedSql.deleteCharAt(preparedSql.length() - 1);
		preparedSql.append(");");
		int result = 0;
		try {
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(preparedSql.toString(), Statement.RETURN_GENERATED_KEYS);
			//填充参数
			int index = 1;
			for (Field field : fields) {
				pstmt.setObject(index, field.get(t));
				index++;
			}
			result = pstmt.executeUpdate();
			//立即查出最新的ID，赋值给对象id字段
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				int key = rs.getInt(1);
				Field idField = getTClass().getDeclaredField("id");
				idField.setAccessible(true);
				idField.set(t, key);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return result;
	}
	
	public Integer update(Integer id, Map<String, Object> params) {
		//拼接UPDATE语句
		StringBuffer preparedSql = new StringBuffer();
		List<Object> values = new ArrayList<Object>();
		preparedSql.append("UPDATE ").append(OrmUtil.getTableName(getTClass())).append(" SET ");
		try {
			for (Entry<String, Object> one : params.entrySet()) {
				preparedSql.append(" ").append(one.getKey()).append("=?,");
				values.add(one.getValue());
			}
		} catch (Exception e) {
			logger.error("", e);
			return 0;
		}
		preparedSql.deleteCharAt(preparedSql.length() - 1);
		preparedSql.append(" where id=?");
		preparedSql.append(";");
		int result = 0;
		try {
			logger.debug(preparedSql.toString());
			PreparedStatement pstmt = getConnection().prepareStatement(preparedSql.toString());
			//填充参数
			int index = 1;
			for (Object value : values) {
				pstmt.setObject(index, value);
				index++;
			}
			pstmt.setObject(index, id);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("", e);
		}
		return result;
	}

	private Class<T> getTClass() {
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		return (Class) params[0];
	}

}
