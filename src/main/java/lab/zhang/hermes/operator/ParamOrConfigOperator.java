package lab.zhang.hermes.operator;

import lab.zhang.apollo.bo.Valuable;
import lab.zhang.apollo.pojo.ParamContext;
import lab.zhang.apollo.pojo.operators.externals.ExternalOperator;

import java.util.List;

/**
 * @author zhangrj
 */
public class ParamOrConfigOperator extends ExternalOperator {

    @Override
    protected Object doCalc(List<? extends Valuable<Object>> list, ParamContext paramContext) {
        return list.get(0).getValue(paramContext);
    }
}
