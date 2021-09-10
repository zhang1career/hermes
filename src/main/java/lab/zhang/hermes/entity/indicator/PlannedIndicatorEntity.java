package lab.zhang.hermes.entity.indicator;

import lab.zhang.hermes.entity.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.text.StringEscapeUtils;

/**
 * @author zhangrj
 */
@Data
public class PlannedIndicatorEntity extends BaseEntity {

    private Long indicatorId;

    private String expression;

    public PlannedIndicatorEntity(long id, Long indicatorId, String expression) {
        this.id = id;
        this.indicatorId = indicatorId;
        this.expression = expression;
    }

    public PlannedIndicatorEntity(long indicatorId, String expression) {
        this(0, indicatorId, expression);
    }

    public PlannedIndicatorEntity(String expression) {
        this(0, null, expression);
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
