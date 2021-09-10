package lab.zhang.hermes.entity.indicator;

import lab.zhang.hermes.entity.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.text.StringEscapeUtils;

import java.util.List;

/**
 * @author zhangrj
 */
@Data
public class IndicatorEntity extends BaseEntity {

    private String name;

    private long operatorId;

    private String expression;
    /**
     * many to many relationship
     */
    private List<IndicatorEntity> parents;
    /**
     * many to many relationship
     */
    private List<IndicatorEntity> children;


    public IndicatorEntity() {
    }

    public IndicatorEntity(long id, String name, long operatorId, String expression) {
        this.id = id;
        this.name = name;
        this.operatorId = operatorId;
        this.expression = expression;

        this.parents = null;
        this.children = null;
    }

    public IndicatorEntity(String name, long operatorId, String expression) {
        this(0, name, operatorId, expression);
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
