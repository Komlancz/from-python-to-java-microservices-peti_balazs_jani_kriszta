package com.codecool.reviewservice;

import com.codecool.reviewservice.controller.ClientController;
import com.codecool.reviewservice.controller.RegistrationPageController;
import com.codecool.reviewservice.controller.ReviewController;
import com.codecool.reviewservice.dao.connection.DBConnection;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.sql.SQLException;

import static spark.Spark.*;
/**
 * <h1>Horseshoe Review Service</h1>
 * Horseshoe Review Service is a cross-store review management system.
 * The service provides a powerful solution to create, moderate and display product reviews in
 * your online store with an integrated possibility to display approved reviews from other online
 * stores as well.
 * <p>
 * Clients need to register with Horseshoe Review Service by providing their company name and email address.
 * After that, an email will be sent to them with their registration data and API Key, which is validated every time
 * they wish to use the Review Service.
 *
 * Features:
 *  1. Register new Company
 *  2. Users of the web shops are able the submit reviews
 *  3. Clients are able to update the status of the reviews (APPROVED or DENIED) submitted on their site with links
 *  provided for them via email.
 *  4. Clients are able to request all the reviews approved by them
 *  5. Clients are able to request all approved reviews of a specific product from the entire database.
 *
 * @author  Jani Peti Balazs Kriszta
 * @version 1.0
 * @since   2017-01-06
 *//**
 * <h1>Horseshoe Review Service</h1>
 * Horseshoe Review Service is a cross-store review management system.
 * The service provides a powerful solution to create, moderate and display product reviews in
 * your online store with an integrated possibility to display approved reviews from other online
 * stores as well.
 * <p>
 * Clients need to register with Horseshoe Review Service by providing their company name and email address.
 * After that, an email will be sent to them with their registration data and API Key, which is validated every time
 * they wish to use the Review Service.
 *
 * Features:
 *  1. Register new Company
 *  2. Users of the web shops are able the submit reviews
 *  3. Clients are able to update the status of the reviews (APPROVED or DENIED) submitted on their site with links
 *  provided for them via email.
 *  4. Clients are able to request all the reviews approved by them
 *  5. Clients are able to request all approved reviews of a specific product from the entire database.
 *
 * @author  Jani Peti Balazs Kriszta
 * @version 1.0
 * @since   2017-01-06
 */
public class Server {


    public static void main(String[] args) throws SQLException {
        // connection to PostgreSQL Database
        DBConnection dbConnection = new DBConnection();
        dbConnection.connect();

        // Instantiate template engine
        ThymeleafTemplateEngine tmp = new ThymeleafTemplateEngine();

        // Default server settings
        exception(Exception.class, (e, req, res) -> e.printStackTrace());
        staticFileLocation("/public");
        port(61000);

        // Routes
        get("/newClient", (request, response) -> ClientController.newClient(request, response));
        get("/review/:APIKey/:productName/:comment/:ratings", (request, response) -> ReviewController.newReview(request, response));
        get("/changeStatus/:APIKey/:reviewKey/:status", (request, response) -> ReviewController.changeStatus(request, response));
        get("/reviewFromClient/:APIKey", (request, response) -> ReviewController.getAllReviewFromClient(request, response));
        get("/allReviewOfProduct/:APIKey/:ProductName", (request, response) -> ReviewController.getAllReviewOfProduct(request, response));
        get("/", RegistrationPageController::renderRegistrationPage, tmp);

        get("/success",  (req, res) -> "Successful registration");
        get("/newStatus", (req, res) -> "New status of review has been set");
    }
}
