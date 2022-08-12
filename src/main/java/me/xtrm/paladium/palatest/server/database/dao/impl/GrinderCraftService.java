package me.xtrm.paladium.palatest.server.database.dao.impl;

import me.xtrm.paladium.palatest.server.database.dao.DAO;
import me.xtrm.paladium.palatest.server.database.dao.model.GrinderCraftModel;
import org.sql2o.Connection;
import org.sql2o.Query;

import java.util.List;

public enum GrinderCraftService implements DAO<GrinderCraftModel> {
    INSTANCE;

    private final List<GrinderCraftModel> crafts;

    GrinderCraftService() {
        try (Connection con = SQL.open()) {
            String query = "SELECT * FROM grinderCrafts";
            this.crafts = con.createQuery(query)
                .executeAndFetch(GrinderCraftModel.class);
        }
    }

    @Override
    public List<GrinderCraftModel> getAll() {
        return this.crafts;
    }

    @Override
    public GrinderCraftModel getById(int id) {
        return this.crafts.stream()
            .filter(it -> it.getId() == id)
            .findFirst()
            .orElse(null);
    }

    @Override
    public GrinderCraftModel insert(GrinderCraftModel dataObject) {
        if (getById(dataObject.getId()) != null) {
            update(dataObject);
            return dataObject;
        }

        try (Connection con = SQL.open()) {
            String insertQuery =
                "INSERT INTO grinderCrafts (playerName, craftedItem, craftDate, worldName, x, y, z) " +
                    "VALUES (:playerName, :craftedItem, :craftDate, :worldName, :x, :y, :z)";

            appendParams(con.createQuery(insertQuery), dataObject)
                .executeUpdate();

            this.crafts.add(dataObject);
        }
        return dataObject;
    }

    @Override
    public void update(GrinderCraftModel dataObject) {
        GrinderCraftModel model;
        if ((model = getById(dataObject.getId())) == null) {
            insert(dataObject);
            return;
        }

        try (Connection con = SQL.open()) {
            String updateQuery = "UPDATE `grinderCrafts` SET " +
                "`playerName`=:playerName," +
                "`craftedItem`=:craftedItem," +
                "`craftDate`=:craftDate," +
                "`worldName`=:worldName," +
                "`x`=:x," +
                "`y`=:y," +
                "`z`=:z" +
                " WHERE `id`=:id";

            appendParams(con.createQuery(updateQuery), dataObject)
                .addParameter("id", dataObject.getId())
                .executeUpdate();
        }

        model.setPlayerName(dataObject.getPlayerName());
        model.setCraftedItem(dataObject.getCraftedItem());
        model.setCraftDate(dataObject.getCraftDate());
        model.setWorldName(dataObject.getWorldName());
        model.setX(dataObject.getX());
        model.setY(dataObject.getY());
        model.setZ(dataObject.getZ());
    }

    @Override
    public void delete(GrinderCraftModel dataObject) {
        GrinderCraftModel model;
        if ((model = getById(dataObject.getId())) == null) {
            throw new UnsupportedOperationException(
                "Tried deleting unknown model."
            );
        }

        try (Connection con = SQL.open()) {
            String deleteQuery = "DELETE FROM `grinderCrafts` WHERE `id`=:id";

            con.createQuery(deleteQuery)
                .addParameter("id", model.getId())
                .executeUpdate();

            this.crafts.remove(model);
        }
    }

    private Query appendParams(Query query, GrinderCraftModel dataObject) {
        return query.addParameter("playerName", dataObject.getPlayerName())
            .addParameter("craftedItem", dataObject.getCraftedItem())
            .addParameter("craftDate", dataObject.getCraftDate())
            .addParameter("worldName", dataObject.getWorldName())
            .addParameter("x", dataObject.getX())
            .addParameter("y", dataObject.getY())
            .addParameter("z", dataObject.getZ());
    }
}
