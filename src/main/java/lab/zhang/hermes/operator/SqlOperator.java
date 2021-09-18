package lab.zhang.hermes.operator;

import lab.zhang.apollo.bo.Valuable;
import lab.zhang.apollo.pojo.ParamContext;
import lab.zhang.apollo.pojo.operators.externals.ExternalOperator;
import lab.zhang.hermes.exception.ServiceException;
import lab.zhang.hermes.operator.sql.user.UserListResult;
import lab.zhang.hermes.service.sql.SqlConfig;
import lab.zhang.hermes.service.sql.SqlService;
import lab.zhang.hermes.util.CastUtil;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * @author zhangrj
 */
public class SqlOperator extends ExternalOperator {
    static private final int PARAM_SIZE = 3;

    @Override
    protected Object doCalc(List<? extends Valuable<Object>> list, ParamContext paramContext) {
        // check
        if (list == null || list.size() < PARAM_SIZE) {
            throw new ServiceException("Exactly one param needed, class: " + this.getClass().getSimpleName());
        }

        // get parameters
        Valuable<Map<String, Object>> configMapValuable = CastUtil.from(list.get(0));
        if (configMapValuable == null) {
            throw new ServiceException("sql config needed, class: " + this.getClass().getSimpleName());
        }
        Map<String, Object> configMap = configMapValuable.getValue(paramContext);
        if (configMap == null) {
            throw new ServiceException("sql config getValue failed, class: " + this.getClass().getSimpleName());
        }
        SqlConfig config = SqlConfig.of(configMap);

        Valuable<String> sqlValuable = CastUtil.from(list.get(1));
        if (sqlValuable == null) {
            throw new ServiceException("sql needed, class: " + this.getClass().getSimpleName());
        }
        String sql = sqlValuable.getValue(paramContext);

        Valuable<Object[]> paramsArrValuable = CastUtil.from(list.get(2));
        if (paramsArrValuable == null) {
            throw new ServiceException("params needed, class: " + this.getClass().getSimpleName());
        }
        Object[] paramsArr = paramsArrValuable.getValue(paramContext);

        // query
        DataSource ds = SqlService.dataSource(config);
        if (ds == null) {
            throw new ServiceException("get datasource failed, class: " + this.getClass().getSimpleName());
        }

        UserListResult userListResult = new UserListResult();
        return SqlService.query(ds, sql, paramsArr, userListResult);
    }
}