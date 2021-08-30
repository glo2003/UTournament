package com.github.glo2003.utournament;

import static spark.Spark.exception;
import static spark.Spark.*;

public class UTournament {
    public static void main(String[] args) {
        exception(Exception.class, (e, req, res) -> e.printStackTrace()); // print all exceptions
        port(8080);


    }
}
