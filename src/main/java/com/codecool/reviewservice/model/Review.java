package com.codecool.reviewservice.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;


public class Review {
    private static final Logger logger = LoggerFactory.getLogger(Review.class);

    private String id;
    private int clientID;
    private String productName;
    private String comment;
    private int rating;
    private String status;
    private String reviewKey;

    /**
     * This method has to generate an (UUID) reviewKey if it has not got one yet.
     * @param clientID not null.
     * @param productName not null and it has to be uppercase without any whitespace.
     * @param comment not null.
     * @param rating not null.
     */


//    Constructor without reviewKey, it generates new
    public Review(int clientID, String productName, String comment, int rating) {
        this.clientID = clientID;
        this.productName = handleProductName(productName);
        this.comment = comment;
        this.rating = rating;
        this.status = Status.PENDING.toString();
        this.reviewKey = UUID.randomUUID().toString();

        logger.info("Create new Review model with new reviewKey: " + this.reviewKey + " Status: " + this.status);
    }

    /**
     *
     * @param clientID not null.
     * @param productName not null and it has to be uppercase without any whitespace.
     * @param comment not null.
     * @param rating not null.
     * @param reviewKey not null.
     * @param status not null.
     */
//    Constructor with reviewKey
    public Review(int clientID, String productName, String comment, int rating, String reviewKey, String status) {
        this.clientID = clientID;
        this.productName = productName;
        this.comment = comment;
        this.rating = rating;
        this.reviewKey = reviewKey;
        this.status = status;

        logger.info("Create Review model with the reviewKey: " + this.reviewKey + " Status: " + this.status);

    }
    /**
     *
     * @return the current status of the review
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method set the current review's status to Approved.
     * @return Nothing.
     */
    public void setStatusApproved() {
        logger.info("Set status to: " + Status.APPROVED);
        this.status = Status.APPROVED.toString();
    }

    /**
     * This method set the current review's status to Denied.
     * @return Nothing.
     */
    public void setStatusDenied() {
        logger.info("Set status to: " + Status.DENIED);
        this.status = Status.DENIED.toString();
    }

    /**
     * This method converts the product name to uppercase and deletes the spaces
     * @param prodName not null.
     * @return Uppercase version of prodName without space.
     */
    private String handleProductName(String prodName){
        return prodName.replace(" ", "").toUpperCase();
    }

    /**
     *
     * @return name of the review's product.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * This method set the review model's id what gets from database.
     * @param id not null.
     */
    public void setId(String id){
        this.id = id;
    }

    /**
     * @return current review's id.
     */
    public String getId() {
        return id;
    }

    /**
     * @return Id of the client who sent the review.
     */
    public int getClientID() {
        return clientID;
    }

    /**
     * @return text of the comment.
     */
    public String getComment() {
        return comment;
    }

    /**
     * @return value of the rating.
     */
    public int getRating() {
        return rating;
    }

    /**
     * @return generated review key.
     */
    public String getReviewKey() {
        return reviewKey;
    }

    /**
     * @return Client_id: id, Product name: prodName, Review: comment's text, Ratings: value of rating,
     * Review Key: review key, Status: status of review - in a string
     */
    @Override
    public String toString(){
        return String.format(
                        "Client_id: %s \n" +
                        "Product name: %s \n" +
                        "Review: %s \n" +
                        "Ratings: %d \n" +
                        "Review Key: %s \n" +
                        "Status: %s", getClientID(),getProductName(),getComment(),getRating(),getReviewKey(),getStatus());
    }
}
