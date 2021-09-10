package lab.zhang.hermes.action.indicator;

import lab.zhang.hermes.action.BaseAction;
import lab.zhang.hermes.entity.operator.OperatorEntity;
import lab.zhang.hermes.exception.InvalidParameterException;
import lab.zhang.hermes.repo.IndicatorRepo;
import lab.zhang.hermes.repo.OperatorRepo;
import lab.zhang.hermes.service.IndicatorService;
import lab.zhang.hermes.util.ListUtil;
import lab.zhang.hermes.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhangrj
 */
@Service
public class CreateAction extends BaseAction {
    @Autowired
    private IndicatorRepo indicatorRepo;

    @Autowired
    private OperatorRepo operatorRepo;

    @Autowired
    private IndicatorService indicatorService;

    public Long act(String name, long operatorId, String indicatorIds) {
        if (StrUtil.isNill(name)) {
            throw new InvalidParameterException("the name should be present and not empty");
        }

        OperatorEntity item = operatorRepo.getItem(operatorId);
        if (item == null) {
            throw new InvalidParameterException("the operator_id has a wrong value");
        }

        if (StrUtil.isNill(indicatorIds)) {
            throw new InvalidParameterException("the indicator_ids should be present and not empty");
        }

        List<Long> requestedIndicatorIdList = StrUtil.explode(indicatorIds);
        List<Long> existedIndicatorIdList = indicatorRepo.getIdList(requestedIndicatorIdList);
        if (!ListUtil.isEqual(requestedIndicatorIdList, existedIndicatorIdList)) {
            throw new InvalidParameterException("the indicator_ids have some wrong value");
        }

        return indicatorService.createIndicator(name, operatorId, existedIndicatorIdList);
    }
}
