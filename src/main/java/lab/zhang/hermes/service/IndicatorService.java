package lab.zhang.hermes.service;

import lab.zhang.apollo.pojo.ApolloType;
import lab.zhang.apollo.pojo.Token;
import lab.zhang.apollo.service.LexerService;
import lab.zhang.apollo.service.lexer.BasicLexerService;
import lab.zhang.hermes.entity.indicator.IndicatorEntity;
import lab.zhang.hermes.entity.operator.OperatorEntity;
import lab.zhang.hermes.repo.BaseRepo;
import lab.zhang.hermes.repo.IndicatorRepo;
import lab.zhang.hermes.repo.OperatorRepo;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * @author zhangrj
 */
@Service
public class IndicatorService {

    @Autowired
    private IndicatorRepo indicatorRepo;

    @Autowired
    private OperatorRepo operatorRepo;

    private final LexerService lexerService = new BasicLexerService();


    public Long createIndicator(String name, long operatorId, List<IndicatorEntity> indicatorEntityList) {
        List<Long> indicatorIdList = BaseRepo.columnOf(indicatorEntityList, IndicatorEntity::getId);
        List<Long> operatorIdList = BaseRepo.columnOf(indicatorEntityList, IndicatorEntity::getOperatorId);
        OperatorEntity operatorEntity = operatorRepo.getItem(operatorId);
        Map<Long, OperatorEntity> operatorEntityDic = operatorRepo.getListIndexById(operatorIdList);
        String expression = expressionOf(operatorEntity, indicatorEntityList, operatorEntityDic);
        return indicatorRepo.create(name, operatorId, expression, indicatorIdList);
    }

    private String expressionOf(OperatorEntity operatorEntity, @NotNull List<IndicatorEntity> indicatorEntityList, Map<Long, OperatorEntity> operatorEntityDic) {
        Token[] childrenToken = new Token[indicatorEntityList.size()];
        for (int i = 0; i < indicatorEntityList.size(); i++) {
            OperatorEntity childOperatorEntity = operatorEntityDic.get(indicatorEntityList.get(i).getOperatorId());
            childrenToken[i] = new Token(
                    childOperatorEntity.getName(),
                    ApolloType.EXTERNAL_OPERATOR,
                    childOperatorEntity.getId(),
                    null);
        }
        Token token = new Token(operatorEntity.getName(), ApolloType.EXTERNAL_OPERATOR, operatorEntity.getId(), childrenToken);
        return lexerService.jsonOf(token);
    }
}
