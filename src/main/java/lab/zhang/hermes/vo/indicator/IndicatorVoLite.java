package lab.zhang.hermes.vo.indicator;

import lab.zhang.hermes.entity.indicator.IndicatorEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangrj
 */
@Data
public class IndicatorVoLite {

    static private final int DEPTH_MAX = 1;

    private Long id;
    private String name;

    static public List<IndicatorVoLite> listOf(List<IndicatorEntity> indicatorEntityList) {
        if (indicatorEntityList == null || indicatorEntityList.size() <= 0) {
            return null;
        }

        List<IndicatorVoLite> list = new ArrayList<>();
        for (IndicatorEntity indicatorEntity : indicatorEntityList) {
            IndicatorVoLite indicatorVoLite = of(indicatorEntity);
            if (indicatorVoLite == null) {
                continue;
            }
            list.add(indicatorVoLite);
        }
        return list;
    }

    static public IndicatorVoLite of(IndicatorEntity indicatorEntity) {
        if (indicatorEntity == null) {
            return null;
        }
        IndicatorVoLite indicatorVoLite = new IndicatorVoLite();
        indicatorVoLite.setId(indicatorEntity.getId());
        indicatorVoLite.setName(indicatorEntity.getName());

        return indicatorVoLite;
    }
}
