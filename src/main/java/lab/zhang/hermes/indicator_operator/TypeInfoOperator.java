package lab.zhang.hermes.indicator_operator;

import lab.zhang.apollo.bo.Valuable;
import lab.zhang.apollo.pojo.ParamContext;
import lab.zhang.apollo.pojo.operators.externals.ExternalOperator;

import java.util.List;

/**
 * @author zhangrj
 */
public class TypeInfoOperator extends ExternalOperator {
    @Override
    protected Object doCalc(List<? extends Valuable<Object>> list, ParamContext paramContext) {
        System.out.println("This is a user defined indicator");
        return null;
    }
}