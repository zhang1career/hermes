package lab.zhang.hermes.vo;

import lab.zhang.hermes.entity.indicator.IndicatorEntity;
import lombok.Data;

import java.util.List;

/**
 * @author zhangrj
 */
@Data
public class IndicatorVo {
    private long id;
    private String name;
    private String expression;

    public static List<IndicatorVo> listOf(List<IndicatorEntity> indicatorEntityList) {
        return null;
    }

    public static IndicatorVo of(IndicatorEntity indicatorEntity) {
        return null;
    }
}
