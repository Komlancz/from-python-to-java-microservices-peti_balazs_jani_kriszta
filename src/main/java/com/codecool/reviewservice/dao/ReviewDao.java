package com.codecool.reviewservice.dao;

import com.codecool.reviewservice.model.Review;

import java.util.ArrayList;

public interface ReviewDao {
    /**
     * This method gets a review object, creates an sql INSERT query and uploads that to the database.
     * @param reviewModel not null.
     */
    void add(Review reviewModel);
    /**
     * This method gets an review key and deletes the review from the database where review key is same.
     * @param reviewKey not null.
     */
    void remove(int reviewKey);
    /**
     * This method get a client id and search review(s) whose match
     * @param clientID not null.
     * @return List of review object(s).
     */
    ArrayList<Review> getByClientID(int clientID);
    /**
     * This method gets a product name and searches review(s) whose match
     * @param productName not null.
     * @return Lis of review object(s).
     */
    ArrayList<Review> getByProductName(String productName);
    /**
     * This method gets a client id and searches review(s) whose match and the status is APPROVED
     * @param clientID not null.
     * @return Lis of review object(s).
     */
    ArrayList<Review> getApprovedByClientId(int clientID);
    /**
     * This method gets a product name and searches review(s) whose match and the status is APPROVED
     * @param productName not null.
     * @return Lis of review object(s).
     */
    ArrayList<Review> getApprovedByProductName(String productName);
    /**
     * This method searches review by review key and updates status
     * @param review_key not null.
     * @param newStatus not null.
     */
    void updateStatus(String review_key, String newStatus);
}
