package lab.zhang.hermes.dao;

import lab.zhang.hermes.entity.operator.OperatorEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author zhangrj
 */
@Mapper
public interface OperatorDao {

    List<OperatorEntity> findAll();

    List<OperatorEntity> findByCondition(@Param("condition") Map<String, Object> condition);

    OperatorEntity findOne(long id);

    int insert(OperatorEntity entity);

    void update(OperatorEntity entity);

    void delete(long id);
}
