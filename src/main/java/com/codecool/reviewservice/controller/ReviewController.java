package com.codecool.reviewservice.controller;

import com.codecool.reviewservice.dao.ClientDao;
import com.codecool.reviewservice.dao.ReviewDao;
import com.codecool.reviewservice.dao.implementation.ClientDaoJdbc;
import com.codecool.reviewservice.dao.implementation.ReviewDaoJdbc;
import com.codecool.reviewservice.email.Email;
import com.codecool.reviewservice.errorHandling.InvalidClient;
import com.codecool.reviewservice.model.Client;
import com.codecool.reviewservice.model.Review;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;


public class ReviewController {
    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    private static ReviewDao reviews = ReviewDaoJdbc.getInstance();
    private static ClientDao clients = ClientDaoJdbc.getInstance();

    /**
     * This method has called from server (/review/:APIKey/:productName/:comment/:ratings).
     * It runs a validation the API key (that gets from request.params("APIKey") is valid by calling the validateClient() method,
     * if it is valid it creates a new Review object and adds it to the database with the review.add(newReview) method and also
     * sends email by calls ReviewForModerationEmail(), which sends an email to the client.
     * If the API key is invalid the method throws an InvalidClient exception.
     * @param request Spark request.
     * @param response Spark response.
     * @return null. (it needs to returns null)
     * @throws IOException
     * @throws URISyntaxException
     * @throws InvalidClient
     */
    public static String newReview(Request request, Response response) throws IOException, URISyntaxException, InvalidClient {
        String APIKey = request.params("APIKey");

        if (!validateClient(APIKey)) {
            throw new InvalidClient("Client is not found in database.");
        } else {
            Review newReview = new Review(getClientID(APIKey),
                                          request.params("productName"),
                                          request.params("comment"),
                                          Integer.parseInt(request.params("ratings")));
            reviews.add(newReview);
            Email.ReviewForModerationEmail(newReview);
            return null;
        }
    }

    /**
     * This method has called from server (/changeStatus/:APIKey/:reviewKey/:status).
     * It runs a validation the API key (that gets from request.params("APIKey") is valid by calling the validateClient() method,
     * if it is valid gets the reviewKey by request and modify the review's status by calls reviews.updateStatus(reviewKey, status) method.
     * If it is not valid throws an InvalidClient() exception.
     * @param request Spark request
     * @param response Spark response
     * @return null. (it needs to returns null)
     * @throws IOException
     * @throws URISyntaxException
     * @throws InvalidClient
     */
    public static String changeStatus(Request request, Response response) throws IOException, URISyntaxException, InvalidClient {
        String APIKey = request.params("APIKey");

        if (!validateClient(APIKey)) {
            throw new InvalidClient("Client is not found in database.");
        } else {
            String reviewKey = request.params("reviewKey");
            String status = request.params("status").toUpperCase();
            reviews.updateStatus(reviewKey, status);
            response.redirect("/newStatus");
            return null;
        }
    }

    /**
     * This method has called from server (/reviewFromClient/:APIKey).
     * It runs a validation the API key (that gets from request.params("APIKey") is valid by calling the validateClient() method,
     * if it is valid get all reviews by calls reviews.gettApprovedByClientId(clientId) and upload the 'reviewsOfClient' list with them.
     * If it is not valid throws InvalidClient exception.
     * @param request Spark request
     * @param response Spark response
     * @return a Review objects as JSON string.
     * @throws IOException
     * @throws URISyntaxException
     * @throws InvalidClient
     */
    public static String getAllReviewFromClient(Request request, Response response) throws IOException, URISyntaxException, InvalidClient {
        ArrayList<String> reviewsOfClient = new ArrayList<>();

        String APIKey = request.params("APIKey");

        if (!validateClient(APIKey)) {
            throw new InvalidClient("Client is not found in database.");
        } else {
            ArrayList<Review> returnReviews = reviews.getApprovedByClientId(getClientID(APIKey));
            for (Review review : returnReviews) {
                reviewsOfClient.add(review.toString());
            }

            return jsonify(reviewsOfClient);
        }
    }
    /**
     * This method has called from server (/allReviewOfProduct/:APIKey/:ProductName).
     * It runs a validation the API key (that gets from request.params("APIKey") is valid by calling the validateClient() method,
     * if it is valid get all reviews by calls reviews.getApprovedByProductName(productName) -it is IMPORTANT the productName has to be UPPERCASE
     * and without any whitespace- and upload the 'approvedReviews' list with them.
     * If it is not valid throws InvalidClient exception.
     * @param request Spark request
     * @param response Spark response
     * @return a Review objects as JSON string.
     * @throws IOException
     * @throws URISyntaxException
     * @throws InvalidClient
     */
    public static String getAllReviewOfProduct(Request request, Response response) throws IOException, URISyntaxException, InvalidClient {
        String APIKey = request.params("APIKey");
        String productName = request.params("productName");

        ArrayList<String> approvedReviews = new ArrayList<>();

        if (!validateClient(APIKey)) {
            throw new InvalidClient("Client is not found in database.");
        } else {
            ArrayList<Review> returnReviews = reviews.getApprovedByProductName(productName.replace(" ", "").toUpperCase());
            for (Review review : returnReviews) {
                approvedReviews.add(review.toString());
            }
            return jsonify(approvedReviews);
        }
    }
    /**
     * This method is used for validating the clients by their API Key. If the API key is not in the database the method returns false,
     * if it is in the database, the method returns true.
     * @param APIKey an unique hash belongs to every Client record in the database, this is the APIKey
     * @return Boolean
     */
    private static boolean validateClient(String APIKey) {
        Client client = clients.getByAPIKey(APIKey);
        if (client == null) {
            return false;
        }
        return true;
    }
    /**
     * This method is used for getting a specific client's ID by their API Key.
     * @param APIKey an unique hash belongs to every Client record in the database, this is the APIKey
     * @return  Returns the ID of a client as an Integer.
     */
    private static int getClientID(String APIKey){
        return clients.getByAPIKey(APIKey).getId();
    }
    /**
     * This method is used for converting an ArrayList (which contains Review objects as strings) into JSON.
     * @param reviews Review objects as strings in an ArrayList
     * @return Review objects as JSON string.
     */
    private static String jsonify(ArrayList<String> reviews) {
        return new Gson().toJson(reviews);
    }

}