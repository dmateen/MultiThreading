package com.i2c.mcp.batch.datafetchservice.dao;

import com.i2c.mcp.batch.datafetchservice.beans.TransactionInfo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DataFetcherDao {

    private final String URL_KEY="URL";
    private final String USERNAME_KEY="USERNAME";
    private final String PASSWORD_KEY="PASSWORD";
    private String jdbcUrl;
    private String jdbcUsername;
    private String jdbcPassword;

    public List<TransactionInfo> getData(int min, int max) {
        setDataFromFile();

        List<TransactionInfo> dataList = new ArrayList<>();

        try  {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
            String sql = "SELECT company_id, company_name FROM production_company where company_id>=? AND company_id<=?";
            System.out.println(sql);

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1,min);
            preparedStatement.setInt(2,max);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("company_id");
                String name = resultSet.getString("company_name");

                TransactionInfo data=new TransactionInfo(id,name);
                System.out.println(id+" "+name);
                dataList.add(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return dataList;
    }

    void setDataFromFile(){
        Properties properties=new Properties();
        String fileName="src/resources/db.config";

        try {
            InputStream inputStream = Files.newInputStream(Paths.get(fileName));
            properties.load(inputStream);

            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        jdbcUrl= properties.getProperty(URL_KEY);
        jdbcUsername= properties.getProperty(USERNAME_KEY);
        jdbcPassword=properties.getProperty(PASSWORD_KEY) ;


    }
}
