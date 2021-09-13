package lab.zhang.hermes.dao;

import lab.zhang.hermes.entity.expression.OriginalExpressionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangrj
 */
@Mapper
public interface OriginalExpressionDao {

    OriginalExpressionEntity findByIndicatorId(@Param("indicatorId") long indicatorId);

    int insert(OriginalExpressionEntity entity);
}
