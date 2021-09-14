package lab.zhang.hermes.operator;

import lab.zhang.apollo.bo.Valuable;
import lab.zhang.apollo.pojo.ParamContext;
import lab.zhang.apollo.pojo.operators.externals.ExternalOperator;
import lab.zhang.hermes.exception.ServiceException;

import java.util.List;

/**
 * @author zhangrj
 */
public class SensitiveWordOperator extends ExternalOperator {

    @Override
    protected Object doCalc(List<? extends Valuable<Object>> list, ParamContext paramContext) {
        if (list == null || list.size() != 1) {
            throw new ServiceException("Exactly one param needed, class=" + this.getClass().getSimpleName());
        }

        Valuable<Object> valuable = list.get(0);
        if (valuable == null) {
            throw new ServiceException("Valuable param needed, class=" + this.getClass().getSimpleName());
        }

        String dut = (String) valuable.getValue(paramContext);
        return "敏感词".equals(dut);
    }
}
