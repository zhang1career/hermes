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
public class IndicatorEntity extends BaseEntity {

    private String name;
    private String expression;

    public IndicatorEntity(Long id, String name, String expression) {
        this.id = id;
        this.name = name;
        this.expression = expression;
    }

    public IndicatorEntity(String name, String expression) {
        this(null, name, expression);
    }

    public IndicatorEntity(String expression) {
        this(null, null, expression);
    }

    @Override
    public String toString() {
        String jsonStr = new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("name", name)
                .append("expression", expression)
                .toString();
        return StringEscapeUtils.unescapeJava(jsonStr);
    }

    // @todo clear to lomok
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpression() {
        return this.expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
