package com.codecool.reviewservice.dao.implementation;

import com.codecool.reviewservice.dao.ClientDao;
import com.codecool.reviewservice.dao.connection.DBConnection;
import com.codecool.reviewservice.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ClientDaoJdbc implements ClientDao {
    private static final Logger logger = LoggerFactory.getLogger(ClientDaoJdbc.class);
    private DBConnection connection = new DBConnection();
    private static ClientDaoJdbc instance = null;
    private String sql;
    /**
     * This method performs the singleton
     * @return ClientDaoJdbc's instance
     */
    public static ClientDaoJdbc getInstance(){
        if(instance == null){
            instance = new ClientDaoJdbc();
            logger.info("New ClientDaoJdbc instance created: {}", instance);
        }
        return instance;
    }
    /**
     * This method gets a client object, creates an sql INSERT query and uploads that to the database.
     * @param clientModel not null.
     */
    public void add(Client clientModel) {
        String APIKey = clientModel.getAPIKey();
        String name = clientModel.getName();
        String email = clientModel.getEmail();

        sql = "INSERT INTO client (api_key, name, email)" +
                "VALUES('" + APIKey + "', '" + name + "', '" + email + "');";
        logger.debug("Saving to database: {}", clientModel);
        executeQuery(sql);
    }
    /**
     * This method gets an API key and deletes the client from the database where api key is same.
     * @param APIKey not null.
     */
    public void remove(String APIKey) {
        sql = "DELETE FROM client WHERE api_key='" + APIKey + "';";
        logger.debug("Deleting client with API key {}", APIKey);
        executeQuery(sql);
    }

    /**
     * This method gets an id search it in database and create a client object with details.
     * @param id not null.
     * @return Client object which has same id
     */
    public Client getById(int id) {
        sql = "SELECT * FROM client WHERE id='" + id + "';";
        logger.debug("Selecting client with id {}", id);
        return createClientModel(sql);
    }
    /**
     * This method gets an APIKey search it in database and create a client object with details.
     * @param APIKey not null.
     * @return Client object which has same APIKey
     */
    public Client getByAPIKey(String APIKey) {
        sql = "SELECT * FROM client WHERE api_key='" + APIKey + "';";
        logger.debug("Selecting client with API key {}", APIKey);
        return createClientModel(sql);
    }

    private Client createClientModel(String sql){
        try (Connection conn = connection.connect();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(sql)){
            if (rs.next()){
                Client client = new Client(rs.getString("name"), rs.getString("email"), rs.getString("api_key"));
                client.setId(rs.getInt("id"));
                return client;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void executeQuery(String sql){
        connection.executeQuery(sql);
    }

}
