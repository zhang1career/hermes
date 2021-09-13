package lab.zhang.hermes.repo;

import lab.zhang.hermes.dao.OperatorDao;
import lab.zhang.hermes.entity.operator.OperatorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangrj
 */
@Repository
public class OperatorRepo extends BaseRepo {

    @Autowired
    private OperatorDao operatorDao;


    public List<OperatorEntity> getList() {
        return operatorDao.findAll();
    }


    public List<OperatorEntity> getList(List<Long> idList) {
        Map<String, Object> condition = new HashMap<>(0);
        condition.put("ids", idList);
        return operatorDao.findByCondition(condition);
    }


    public Map<Long, OperatorEntity> getListIndexById(List<Long> idList) {
        List<OperatorEntity> operatorEntityList = getList(idList);
        if (operatorEntityList == null) {
            return null;
        }
        return indexById(operatorEntityList);
    }


    public OperatorEntity getItem(long id) {
        return operatorDao.findOne(id);
    }


    public Long create(OperatorEntity operatorEntity) {
        int count = operatorDao.insert(operatorEntity);
        if (count < 1) {
            return null;
        }
        return operatorEntity.getId();
    }
}
