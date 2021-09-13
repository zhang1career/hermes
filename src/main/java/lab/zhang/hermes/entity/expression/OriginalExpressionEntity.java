package lab.zhang.hermes.entity.expression;

import lab.zhang.hermes.entity.BaseEntity;
import lombok.Data;

/**
 * @author zhangrj
 */
@Data
public class OriginalExpressionEntity extends BaseEntity {

    private long indicatorId;

    private String expression;

    public OriginalExpressionEntity() {
    }

    public OriginalExpressionEntity(long indicatorId, String expression) {
        this.indicatorId = indicatorId;
        this.expression = expression;
    }
}
