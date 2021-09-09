package lab.zhang.hermes.dao;

import lab.zhang.hermes.entity.operator.OperatorEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zhangrj
 */
@Mapper
public interface OperatorDao {

    List<OperatorEntity> findAll();

    OperatorEntity findOne(long id);

    int insert(OperatorEntity entity);

    void update(OperatorEntity entity);

    void delete(long id);
}
