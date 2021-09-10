package lab.zhang.hermes.entity.indicator;

import lab.zhang.hermes.entity.BaseEntity;

/**
 * @author zhangrj
 */
public class IndicatorIndicatorRelationEntity extends BaseEntity {
    private long parentId;

    private long childId;


    public IndicatorIndicatorRelationEntity() {
    }

    public IndicatorIndicatorRelationEntity(long id, long parentId, long childId) {
        this.id = id;
        this.parentId = parentId;
        this.childId = childId;
    }
}
