package lab.zhang.hermes.dao;

import lab.zhang.hermes.entity.indicator.IndicatorEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zhangrj
 */
@Mapper
public interface IndicatorIndicatorDao {

    List<IndicatorEntity> selectParents(long id);
    List<IndicatorEntity> selectParentsForUpdate(long id);

    List<IndicatorEntity> selectChildren(long id);
    List<IndicatorEntity> selectChildrenForUpdate(long id);

    int insertChild(long id, long childId);
    int insertChildren(long id, Long[] childrenIds);

    int deleteChild(long id, long childId);
    int deleteChildren(long id, Long[] childrenIds);
}
