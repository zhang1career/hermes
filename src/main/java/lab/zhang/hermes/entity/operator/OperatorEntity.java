package lab.zhang.hermes.entity.operator;

import lab.zhang.hermes.entity.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.text.StringEscapeUtils;

/**
 * @author zhangrj
 */
@Data
public class OperatorEntity extends BaseEntity {

    private String clazz;

    public OperatorEntity(Long id, String clazz) {
        this.id = id;
        this.clazz = clazz;
    }

    public OperatorEntity(String clazz) {
        this(null, clazz);
    }

    @Override
    public String toString() {
        String jsonStr = new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("clazz", clazz)
                .toString();
        return StringEscapeUtils.unescapeJava(jsonStr);

    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }
}
