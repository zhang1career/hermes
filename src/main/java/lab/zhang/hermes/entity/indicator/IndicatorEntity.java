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

    private String operands;

    private String nestedChildrenIds;

    private List<IndicatorEntity> parents;

    private List<IndicatorEntity> children;


    public IndicatorEntity() {
    }

    public IndicatorEntity(long id, String name, long operatorId, String operands) {
        this.id = id;
        this.name = name;
        this.operatorId = operatorId;
        this.operands = operands;
    }

    public IndicatorEntity(String name, long operatorId, String operands) {
        this(0, name, operatorId, operands);
    }

    @Override
    public String toString() {
        String jsonStr = new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("name", name)
                .append("operatorId", operatorId)
                .append("operands", operands)
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

    public String getOperands() {
        return this.operands;
    }

    public void setOperands(String operands) {
        this.operands = operands;
    }
}
