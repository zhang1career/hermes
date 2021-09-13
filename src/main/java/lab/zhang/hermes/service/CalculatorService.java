package lab.zhang.hermes.service;

import lab.zhang.apollo.controller.ApolloController;
import lab.zhang.apollo.pojo.ParamContext;
import lab.zhang.hermes.entity.indicator.IndicatorEntity;
import lab.zhang.hermes.entity.operator.OperatorEntity;
import lab.zhang.hermes.repo.IndicatorRepo;
import lab.zhang.hermes.repo.OperatorRepo;
import lab.zhang.hermes.repo.PlannedExpressionRepo;
import lab.zhang.hermes.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author zhangrj
 */
@Service
public class CalculatorService {

    @Autowired
    private OperatorRepo operatorRepo;

    @Autowired
    private PlannedExpressionRepo plannedExpressionRepo;


    public Object calc(long indicatorId, Map<String, Object> params) {
        String plannedExpression = plannedExpressionRepo.getExpression(indicatorId);
        if (StrUtil.isNill(plannedExpression)) {
            return null;
        }

        ParamContext paramContext = new ParamContext();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            paramContext.putValue(entry.getKey(), entry.getValue());
        }

        ApolloController apolloController = new ApolloController(operatorRepo, plannedExpressionRepo);
        return apolloController.eval(plannedExpression, paramContext);
    }
}
