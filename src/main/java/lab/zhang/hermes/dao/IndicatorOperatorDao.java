package lab.zhang.hermes.dao;

import lab.zhang.hermes.entity.operator.IndicatorOperatorEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zhangrj
 */
@Mapper
public interface IndicatorOperatorDao {

    List<IndicatorOperatorEntity> findAll();

    IndicatorOperatorEntity findOne(long id);

    Long insert(IndicatorOperatorEntity entity);

    void update(IndicatorOperatorEntity entity);

    void delete(long id);
}
