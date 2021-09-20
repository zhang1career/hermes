package lab.zhang.hermes.service.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lab.zhang.hermes.exception.SqlException;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangrj
 */
public class SqlService {
    static private final Map<Integer, DataSource> DS_MAP = new HashMap<>();
    static private final QueryRunner qr = new QueryRunner();

    static public DataSource dataSource(SqlConfig sqlConfig) {
        if (sqlConfig == null) {
            return null;
        }

        int key = sqlConfig.hash();
        if (DS_MAP.containsKey(key)) {
            return DS_MAP.get(key);
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + sqlConfig.getHost() + ":" + sqlConfig.getPort() + "/" + sqlConfig.getDb());
        config.setUsername(sqlConfig.getUser());
        config.setPassword(sqlConfig.getPass());
        config.setConnectionTimeout(1000);
        config.setIdleTimeout(60000);
        config.setMaximumPoolSize(10);
        config.setAutoCommit(false);
        config.addDataSourceProperty("useSSL", false);

        DataSource ds = new HikariDataSource(config);
        // @todo when delete the key?
        DS_MAP.put(key, ds);
        return ds;
    }

    static public <T> T query(DataSource ds, String sql, Object[] params, ResultSetHandler<T> resultSetHandler) {
        try (Connection conn = ds.getConnection()) {
            return qr.query(conn, sql, resultSetHandler, params);
        } catch (SQLException e) {
            throw new SqlException(e);
        }
    }
}