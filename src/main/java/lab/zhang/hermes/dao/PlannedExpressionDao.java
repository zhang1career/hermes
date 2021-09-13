package lab.zhang.hermes.dao;

import lab.zhang.hermes.entity.expression.PlannedExpressionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author zhangrj
 */
@Mapper
public interface PlannedExpressionDao {

    PlannedExpressionEntity findByIndicatorId(@Param("indicatorId") long indicatorId);

    int insert(PlannedExpressionEntity entity);
}
