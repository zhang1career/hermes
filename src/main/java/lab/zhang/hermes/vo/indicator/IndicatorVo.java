package lab.zhang.hermes.vo.indicator;

import lab.zhang.hermes.entity.indicator.IndicatorEntity;
import lombok.Data;

/**
 * @author zhangrj
 */
@Data
public class IndicatorVo {

    static private final int DEPTH_MAX = 1;

    private Long id;
    private String name;
    private String operator;
    private String operands;

    static public IndicatorVo of(IndicatorEntity indicatorEntity) {
        if (indicatorEntity == null) {
            return null;
        }

        IndicatorVo indicatorVo = new IndicatorVo();
        indicatorVo.setId(indicatorEntity.getId());
        indicatorVo.setName(indicatorEntity.getName());
        indicatorVo.setOperator(String.valueOf(indicatorEntity.getOperatorId()));
        indicatorVo.setOperands(indicatorEntity.getOperands());

        return indicatorVo;
    }
}
