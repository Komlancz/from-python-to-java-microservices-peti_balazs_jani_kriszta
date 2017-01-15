package com.codecool.reviewservice.dao;

import com.codecool.reviewservice.model.Client;

public interface ClientDao {
    /**
     * This method gets a client object, creates an sql INSERT query and uploads that to the database.
     * @param clientModel not null.
     */
    void add(Client clientModel);
    /**
     * This method gets an API key and deletes the client from the database where api key is same.
     * @param APIKey not null.
     */
    void remove(String APIKey);
    /**
     * This method gets an id search it in database and create a client object with details.
     * @param id not null.
     * @return Client object which has same id
     */
    Client getById(int id);
    /**
     * This method gets an APIKey search it in database and create a client object with details.
     * @param APIKey not null.
     * @return Client object which has same APIKey
     */
    Client getByAPIKey(String APIKey);
}
