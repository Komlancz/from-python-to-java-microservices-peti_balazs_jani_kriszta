package com.codecool.reviewservice.controller;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is for showing the registration page
 */
public class RegistrationPageController {
    /**
     * This method has called from server (/).
     * It responsible for render the registration page.
     * @param request Spark request object.
     * @param response Spark response object.
     * @return A ModalAndView object for render registration page
     */
    public static ModelAndView renderRegistrationPage(Request request, Response response) {
        Map params = new HashMap<>();
        return new ModelAndView(params, "registration");
    }
}


