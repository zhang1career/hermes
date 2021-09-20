package lab.zhang.hermes.dao;

import lab.zhang.hermes.entity.indicator.IndicatorEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author zhangrj
 */
@Mapper
public interface IndicatorDao {

    List<IndicatorEntity> findAll();

    List<IndicatorEntity> findByCondition(@Param("condition") Map<String, Object> condition);

    IndicatorEntity findOne(long id);

    IndicatorEntity selectOneForUpdate(long id);

    int insert(IndicatorEntity entity);

    void update(IndicatorEntity entity);

    void delete(long id);
}
