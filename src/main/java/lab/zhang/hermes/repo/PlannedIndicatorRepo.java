package lab.zhang.hermes.repo;

import lab.zhang.apollo.pojo.Token;
import lab.zhang.apollo.repo.StorableExpression;
import lab.zhang.apollo.service.LexerService;
import lab.zhang.apollo.service.lexer.BasicLexerService;
import lab.zhang.hermes.dao.PlannedIndicatorDao;
import lab.zhang.hermes.entity.indicator.PlannedIndicatorEntity;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangrj
 */
@Repository
public class PlannedIndicatorRepo extends BaseRepo implements StorableExpression {

    @Autowired
    private PlannedIndicatorDao plannedIndicatorDao;

    private final LexerService lexerService;

    public PlannedIndicatorRepo() {
        lexerService = new BasicLexerService();
    }

    @Nullable
    public List<PlannedIndicatorEntity> getList(List<Long> idList) {
        return plannedIndicatorDao.findAllByIdList(idList);
    }

    public PlannedIndicatorEntity getItem(long id) {
        return plannedIndicatorDao.findOne(id);
    }

    @Override
    public String getExpression(long id) {
        PlannedIndicatorEntity plannedIndicatorEntity = getItem(id);
        if (plannedIndicatorEntity == null) {
            return null;
        }
        return plannedIndicatorEntity.getExpression();
    }

//    public Map<Long, Token> getTokenListIndexById(List<Long> idList) {
//        List<PlannedIndicatorEntity> plannedIndicatorEntityList = getList(idList);
//        if (plannedIndicatorEntityList == null) {
//            return null;
//        }
//
//        Map<Long, PlannedIndicatorEntity> plannedIndicatorEntityMap = indexById(plannedIndicatorEntityList);
//        return columnOf(plannedIndicatorEntityMap, p -> lexerService.tokenOf(p.getExpression()));
//    }

    public Long create(long operationId, Token node) {
        PlannedIndicatorEntity plannedIndicatorEntity = new PlannedIndicatorEntity(operationId, lexerService.jsonOf(node));
        int count = plannedIndicatorDao.insert(plannedIndicatorEntity);
        if (count < 1) {
            return null;
        }
        return plannedIndicatorEntity.getId();
    }
}
