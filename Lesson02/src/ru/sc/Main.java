package ru.sc;

import java.sql.*;
import java.util.Scanner;

public class Main {
    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement preparedStatement;
    private static Scanner scanner;

    public static void main(String[] args) {
        try {
            connect();
            initDb();
            scanner = new Scanner(System.in);
            System.out.println("Для выхода из программы  наберите  команду:   конец");
            loop:
            while (true) {
                String[] newCommand = scanner.nextLine().split(" ");
                switch (newCommand[0]) {
                    case "конец":
                        System.out.println("Программа завершена");
                        break loop;
                    case "цена":
                        getCost(newCommand[1]);
                        break;
                    case "сменитьцену":
                        changeCost(newCommand[1], Integer.parseInt(newCommand[2]));
                        break;
                    case "товарыпоцене":
                        getGoodsForCost(Integer.parseInt(newCommand[1]), Integer.parseInt(newCommand[2]));
                        break;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:goods.db");
        statement = connection.createStatement();
    }

    public static void initDb() throws SQLException {
        connection.setAutoCommit(false);
        statement.executeUpdate("delete from goods;");
        preparedStatement = connection.prepareStatement("INSERT INTO goods (prodid, title, cost) VALUES (?, ?, ?);");
        for (int i = 1; i < 10000; i++) {
            preparedStatement.setInt(1, i);
            preparedStatement.setString(2, "товар" + i);
            preparedStatement.setInt(3, i * 10);
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        connection.setAutoCommit(true);
    }

    public static void getCost(String goods) {
        try {
            preparedStatement = connection.prepareStatement("select cost from goods where title = ?");
            preparedStatement.setString(1, goods);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                System.out.println("Цена товара " + goods + ": " + rs.getDouble(1));
            } else {
                System.out.println("Извините, такого товара не найдено");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void changeCost(String goods, Integer newCost) {
        try {
            Statement statement = connection.createStatement();
            statement.execute("update goods set cost = " + newCost + " where title = '" + goods + "'");
            System.out.println("Новая цена товара " + goods + ": " + newCost);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getGoodsForCost(Integer minCost, Integer maxCost) {
        try {
            preparedStatement = connection.prepareStatement("select title, cost from goods where cost > ? and cost < ?");
            preparedStatement.setInt(1, minCost);
            preparedStatement.setInt(2, maxCost);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                System.out.println("Товара " + rs.getString(1) + ", его цена: " + rs.getInt(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
