package lab.zhang.hermes.operator.sql.user;

import org.apache.commons.dbutils.ResultSetHandler;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangrj
 */
public class UserListResult implements ResultSetHandler<List<UserDao>> {

    @Override
    public List<UserDao> handle(@NotNull ResultSet rs) throws SQLException {
        List<UserDao> userDaoList = new ArrayList<>();
        while (rs.next()) {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            userDaoList.add(new UserDao(id,name));
        }
        return userDaoList;
    }
}
