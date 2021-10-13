package lab.zhang.hermes.operator.query;

import lab.zhang.apollo.bo.Valuable;
import lab.zhang.apollo.pojo.ParamContext;
import lab.zhang.hermes.exception.ServiceException;
import lab.zhang.hermes.operator.QueryOperator;
import lab.zhang.hermes.service.sql.SqlConfig;
import lab.zhang.hermes.service.sql.SqlService;
import org.apache.commons.dbutils.handlers.MapHandler;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author zhangrj
 */
public class QueryItemOperator extends QueryOperator {
    static private final int PARAM_SIZE = 3;

    @Override
    protected String[] getRequiredParams() {
        return new String[]{"config", "sql", "sql_params"};
    }

    @Override
    protected Object doCalc(List<? extends Valuable<Object>> list, ParamContext paramContext) {
        // check
        if (list == null || list.size() < PARAM_SIZE) {
            throw new ServiceException("Exactly one param needed, class: " + this.getClass().getSimpleName());
        }

        // get parameters
        SqlConfig config = getSqlConfig(list, paramContext);
        String sql = getSql(list, paramContext);
        Object[] paramsArr = getParams(list, paramContext);

        // data source
        DataSource ds = getDataSource(config);

        // query
        return SqlService.query(ds, sql, paramsArr, new MapHandler());
    }
}