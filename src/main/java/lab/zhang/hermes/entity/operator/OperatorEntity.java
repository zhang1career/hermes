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

    private String name;

    private String clazz;

    public OperatorEntity(long id, String name, String clazz) {
        this.id = id;
        this.name = name;
        this.clazz = clazz;
    }

    public OperatorEntity(long id, String name) {
        this(id, name, "");
    }

    public OperatorEntity(String name, String clazz) {
        this(0, name, clazz);
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
