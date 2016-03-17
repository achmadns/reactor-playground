package com.github.achmadns.lab;

import com.opengamma.elsql.ElSql;
import com.opengamma.elsql.ElSqlConfig;
import org.sfm.sql2o.SfmResultSetHandlerFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.ResultSetHandlerFactory;
import org.sql2o.Sql2o;

import javax.sql.DataSource;

@Repository
public class DBUtil {
    private final ElSql queries = ElSql.of(ElSqlConfig.MYSQL, DBUtil.class);
    private final Sql2o sql;
    private final ResultSetHandlerFactory<User> factory =
            new SfmResultSetHandlerFactoryBuilder().newFactory(User.class);

    @Autowired
    public DBUtil(DataSource dataSource) {
        this.sql = new Sql2o(dataSource);
    }

    public boolean check() {
        try {
            sql.withConnection((connection, argument) -> {
                connection.createQuery(queries.getSql("check")).executeAndFetch(Integer.class);
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public User getFirstUser() {
        return sql.withConnection((connection, argument) -> {
            return connection.createQuery(queries.getSql("getFirstUser")).executeAndFetchFirst(factory);
        });
    }
}
