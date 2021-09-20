package lab.zhang.hermes.action.indicator;

import lab.zhang.hermes.action.BaseAction;
import lab.zhang.hermes.entity.expression.PlannedExpressionEntity;
import lab.zhang.hermes.entity.indicator.IndicatorEntity;
import lab.zhang.hermes.entity.operator.OperatorEntity;
import lab.zhang.hermes.exception.InvalidParameterException;
import lab.zhang.hermes.exception.ServiceException;
import lab.zhang.hermes.repo.OperatorRepo;
import lab.zhang.hermes.service.IndicatorService;
import lab.zhang.hermes.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhangrj
 */
@Service
public class UpdateAction extends BaseAction {
    @Autowired
    private OperatorRepo operatorRepo;

    @Autowired
    private IndicatorService indicatorService;


    public long act(long id, String name, long operatorId, String operands) {
        if (id <= 0) {
            throw new InvalidParameterException("the id should be a positive integer");
        }
        if (StrUtil.isNill(name)) {
            throw new InvalidParameterException("the name should be present and not empty");
        }

        OperatorEntity item = operatorRepo.getItem(operatorId);
        if (item == null) {
            throw new InvalidParameterException("the operator_id has a wrong value");
        }

        if (StrUtil.isNill(operands)) {
            throw new InvalidParameterException("the operands should be present and not empty");
        }

        IndicatorEntity indicatorEntity = indicatorService.updateOriginalIndicator(id, name, operatorId, operands);
        if (indicatorEntity == null) {
            throw new ServiceException("create indicator failed");
        }
//        PlannedExpressionEntity plannedIndicator = indicatorService.updatePlannedIndicator(indicatorEntity);
//        if (plannedIndicator == null) {
//            throw new ServiceException("plan failed");
//        }

        return indicatorEntity.getId();
    }
}
