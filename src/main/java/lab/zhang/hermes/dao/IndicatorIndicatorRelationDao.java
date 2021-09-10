package lab.zhang.hermes.dao;

import lab.zhang.hermes.entity.indicator.IndicatorEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zhangrj
 */
@Mapper
public interface IndicatorIndicatorRelationDao {

    int insert(long parentId, long childId);

    int insertChildren(long parentId, List<Long> childrenIdList);

    int delete(long parentId, long childId);

    List<IndicatorEntity> findChildren(long id);

    List<IndicatorEntity> findParents(long id);
}
