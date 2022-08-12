package me.xtrm.paladium.palatest.server.database.dao;

import me.xtrm.paladium.palatest.PalaTest;
import me.xtrm.paladium.palatest.server.ServerProxy;
import org.sql2o.Sql2o;

import java.util.List;

@SuppressWarnings("UnusedReturnValue")
public interface DAO<T> {
    Sql2o SQL = ((ServerProxy) PalaTest.INSTANCE.getSidedProxy())
        .getDatabaseConnector()
        .getSql2o();

    List<T> getAll();
    T getById(int id);
    T insert(T dataObject);
    void update(T dataObject);
    void delete(T dataObject);
}
