package ca.jrvs.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class JDBCExecutor {
    public static void main(String... args) {
        final Logger logger = LoggerFactory.getLogger(JDBCExecutor.class);
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("172.28.27.43", "hplussport","postgres", "password");
        try{
            Connection connection = dcm.getConnection();
            OrderDAO orderDAO = new OrderDAO(connection);
            List<Order> orders = orderDAO.getOrdersForCustomer(789);
            orders.forEach(System.out::println);
        }catch(SQLException e){
            logger.error("Connection error");
            }
    }
}