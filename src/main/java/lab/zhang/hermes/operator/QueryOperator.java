package lab.zhang.hermes.operator;

import lab.zhang.apollo.bo.Valuable;
import lab.zhang.apollo.pojo.ParamContext;
import lab.zhang.apollo.pojo.operators.externals.ExternalOperator;
import lab.zhang.hermes.exception.ServiceException;
import lab.zhang.hermes.service.sql.SqlConfig;
import lab.zhang.hermes.service.sql.SqlService;
import lab.zhang.hermes.util.CastUtil;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * @author zhangrj
 */
abstract public class QueryOperator extends ExternalOperator {
    @NotNull
    protected SqlConfig getSqlConfig(@NotNull List<? extends Valuable<Object>> list, ParamContext paramContext) {
        Valuable<Map<String, Object>> configMapValuable = CastUtil.from(list.get(0));
        if (configMapValuable == null) {
            throw new ServiceException("sql config needed, class: " + this.getClass().getSimpleName());
        }
        Map<String, Object> configMap = configMapValuable.getValue(paramContext);
        if (configMap == null) {
            throw new ServiceException("sql config getValue failed, class: " + this.getClass().getSimpleName());
        }
        return SqlConfig.of(configMap);
    }

    protected String getSql(@NotNull List<? extends Valuable<Object>> list, ParamContext paramContext) {
        Valuable<String> sqlValuable = CastUtil.from(list.get(1));
        if (sqlValuable == null) {
            throw new ServiceException("sql needed, class: " + this.getClass().getSimpleName());
        }
        return sqlValuable.getValue(paramContext);
    }

    protected Object[] getParams(@NotNull List<? extends Valuable<Object>> list, ParamContext paramContext) {
        Valuable<Object[]> paramsArrValuable = CastUtil.from(list.get(2));
        if (paramsArrValuable == null) {
            throw new ServiceException("params needed, class: " + this.getClass().getSimpleName());
        }
        return paramsArrValuable.getValue(paramContext);
    }

    @NotNull
    protected DataSource getDataSource(SqlConfig config) {
        DataSource ds = SqlService.dataSource(config);
        if (ds == null) {
            throw new ServiceException("get datasource failed, class: " + this.getClass().getSimpleName());
        }
        return ds;
    }
}
