package com.company.framework.utils;

import com.company.framework.dtos.ProductDto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductFactory {
    private static Context context;

    public ProductFactory(Context context) {
        this.context = context;
    }

    public ProductDto create(int id, String name, int price, int stock, int length, int width, int height) {
        return new ProductDto(id, name, price, stock, length, width, height);
    }

    public void insert(ProductDto product) throws SQLException {
        Connection connection = DriverManager.getConnection(
                context.getValue("connectionUrl").toString(),
                context.getValue("username").toString(),
                context.getValue("password").toString());

        String sql = "insert into product (id, name, price, stock, length, width, height) values ("
                + product.get_id() + ", "
                + "'" + product.get_name() + "', "
                + "'" + product.get_price() + "', "
                + "'" + product.get_stock() + "', "
                + "'" + product.get_length() + "', "
                + "'" + product.get_width() + "', "
                + "'" + product.get_height() + "'"
                + ")";

        Statement stmt = connection.createStatement();
        stmt.execute(sql);
        if (stmt.getUpdateCount() != 1) {
            throw new SQLException("Insert failed!");
        }
    }

    public void delete (int id) throws SQLException {
        Connection connection = DriverManager.getConnection(
                context.getValue("connectionUrl").toString(),
                context.getValue("username").toString(),
                context.getValue("password").toString());

        String sql = "delete from product where id= " + id;

        Statement stmt = connection.createStatement();
        stmt.execute(sql);
        if (stmt.getUpdateCount() != 1) {
            throw new SQLException("Delete failed!");
        }
    }

    public void drop_procedure (String procedureName) throws SQLException {
        Connection connection = DriverManager.getConnection(
                context.getValue("connectionUrl").toString(),
                context.getValue("username").toString(),
                context.getValue("password").toString());

        String sql = "DROP PROCEDURE IF EXISTS " + procedureName;

        Statement stmt = connection.createStatement();
        stmt.execute(sql);
        if (stmt.getUpdateCount() != 0) {
            throw new SQLException("Delete failed!");
        }
    }
}
