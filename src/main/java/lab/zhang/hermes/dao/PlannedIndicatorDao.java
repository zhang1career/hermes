package lab.zhang.hermes.dao;

import lab.zhang.hermes.entity.indicator.PlannedIndicatorEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zhangrj
 */
@Mapper
public interface PlannedIndicatorDao {

    List<PlannedIndicatorEntity> findAll();

    List<PlannedIndicatorEntity> findAllByIdList(List<Long> idList);

    PlannedIndicatorEntity findOne(Long id);

    int insert(PlannedIndicatorEntity entity);

    void update(PlannedIndicatorEntity entity);

    void delete(long id);
}
