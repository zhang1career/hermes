package lab.zhang.hermes.dao;

import lab.zhang.hermes.entity.indicator.IndicatorEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zhangrj
 */
@Mapper
public interface IndicatorRelationDao {

    int insertChild(long parentId, long childId);

    int deleteChild(long parentId, long childId);

    List<IndicatorEntity> findChildren(long id);

    List<IndicatorEntity> findParents(long id);
}
