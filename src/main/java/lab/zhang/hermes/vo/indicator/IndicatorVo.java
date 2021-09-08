package lab.zhang.hermes.vo.indicator;

import com.alibaba.fastjson.JSON;
import lab.zhang.hermes.entity.indicator.IndicatorEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangrj
 */
@Data
public class IndicatorVo {

    static private final int DEPTH_MAX = 1;

    private Long id;
    private String name;
    private List<IndicatorVoLite> children;
    private String expression;

    static public IndicatorVo of(IndicatorEntity indicatorEntity) {
        if (indicatorEntity == null) {
            return null;
        }

        IndicatorVo indicatorVo = new IndicatorVo();
        indicatorVo.setId(indicatorEntity.getId());
        indicatorVo.setName(indicatorEntity.getName());
        indicatorVo.setExpression(indicatorEntity.getExpression());

        List<IndicatorVoLite> indicatorVoLiteList = new ArrayList<>();
        List<IndicatorEntity> indicatorEntityList = indicatorEntity.getChildren();
        for (IndicatorEntity entity : indicatorEntityList) {
            indicatorVoLiteList.add(IndicatorVoLite.of(entity));
        }
        indicatorVo.setChildren(indicatorVoLiteList);

        return indicatorVo;
    }
}
