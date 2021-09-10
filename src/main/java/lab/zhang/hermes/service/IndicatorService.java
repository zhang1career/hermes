package lab.zhang.hermes.service;

import lab.zhang.apollo.pojo.ApolloType;
import lab.zhang.apollo.pojo.Token;
import lab.zhang.apollo.service.LexerService;
import lab.zhang.apollo.service.lexer.BasicLexerService;
import lab.zhang.hermes.repo.IndicatorRepo;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author zhangrj
 */
@Service
public class IndicatorService {

    @Autowired
    private IndicatorRepo indicatorRepo;

    private LexerService lexerService = new BasicLexerService();


    public Long createIndicator(String name, long operatorId, List<Long> indicatorIdList) {
        String expression = expressionOf(operatorId, indicatorIdList);
        return indicatorRepo.create(name, operatorId, expression, indicatorIdList);
    }

    private String expressionOf(long operatorId, @NotNull List<Long> childrenIdList) {
        Token[] childrenToken = new Token[childrenIdList.size()];
        for (int i = 0; i < childrenIdList.size(); i++) {
            childrenToken[i] = new Token("", ApolloType.FRESH_OPERATION, childrenIdList.get(i), null);
        }
        Token token = new Token("", ApolloType.FRESH_OPERATION, operatorId, childrenToken);

        return lexerService.jsonOf(token);
    }
}
