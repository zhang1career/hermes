package lab.zhang.hermes.entity.expression;

import lab.zhang.hermes.entity.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.text.StringEscapeUtils;

/**
 * @author zhangrj
 */
@Data
public class PlannedExpressionEntity extends BaseEntity {

    private long indicatorId;

    private String expression;

    private Integer isDeleted;

    public PlannedExpressionEntity(long id, long indicatorId, String expression, int isDeleted) {
        this.id = id;
        this.indicatorId = indicatorId;
        this.expression = expression;
        this.isDeleted = isDeleted;
    }

    public PlannedExpressionEntity(long indicatorId, String expression) {
        this(0, indicatorId, expression, 0);
    }

    public PlannedExpressionEntity(String expression) {
        this(0, 0, expression, 0);
    }

    @Override
    public String toString() {
        String jsonStr = new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("indicator_id", indicatorId)
                .append("expression", expression)
                .toString();
        return StringEscapeUtils.unescapeJava(jsonStr);
    }

    // @todo clear to lomok
    public String getExpression() {
        return this.expression;
    }
}
