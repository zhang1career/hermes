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
    static private final int PARAM_SIZE = 1;

    @Override
    protected String[] getRequiredParams() {
        return new String[]{"comment"};
    }

    @Override
    protected Object doCalc(List<? extends Valuable<Object>> list, ParamContext paramContext) {
        // check
        if (list == null || list.size() < PARAM_SIZE) {
            throw new ServiceException("Exactly one param needed, class: " + this.getClass().getSimpleName());
        }

        // get parameters
        Valuable<Object> valuable = list.get(0);
        if (valuable == null) {
            throw new ServiceException("Valuable param needed, class: " + this.getClass().getSimpleName());
        }

        String dut = (String) valuable.getValue(paramContext);
        return "敏感词".equals(dut);
    }
}
