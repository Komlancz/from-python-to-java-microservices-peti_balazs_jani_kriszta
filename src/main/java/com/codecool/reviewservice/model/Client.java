package com.codecool.reviewservice.model;

import java.util.UUID;

public class Client {
    String name;
    int id;
    String APIKey;
    String email;

    /**
     * This method has to generate an (UUID) APIKey if it has not got one yet.
     * @param name not null.
     * @param email not null.
     */
    public Client(String name, String email){
        this.name = name;
        this.email = email;
        APIKey = UUID.randomUUID().toString();
    }

    /**
     *
     * @param name not null.
     * @param email not null.
     * @param APIKey not null.
     */
    public Client(String name, String email, String APIKey){
        this.name = name;
        this.email = email;
        this.APIKey = APIKey;
    }

    /**
     * @param id not null.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Name of the client
     */
    public String getName() {
        return name;
    }

    /**
     * @return Client's id
     */
    public int getId() {
        return id;
    }

    /**
     * @return Client's API key
     */
    public String getAPIKey(){
        return APIKey;
    }

    /**
     * @return Client's e-mail address
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return Company name: name, Email address: email, API Key: APIKey
     */
    @Override
    public String toString(){
        return String.format("" +
                "Company name: %s\n" +
                "Email address: %s\n" +
                "API Key: %s\n", getName(), getEmail(), getAPIKey());
    }
}
