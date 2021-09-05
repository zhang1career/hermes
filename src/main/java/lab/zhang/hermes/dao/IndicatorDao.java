package lab.zhang.hermes.dao;

import lab.zhang.hermes.entity.indicator.IndicatorEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zhangrj
 */
@Mapper
public interface IndicatorDao {

    List<IndicatorEntity> findAll();

    IndicatorEntity findOne(long id);

    Long insert(IndicatorEntity entity);

    void update(IndicatorEntity entity);

    void delete(long id);
}
