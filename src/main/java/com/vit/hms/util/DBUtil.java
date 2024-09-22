package com.vit.hms.util;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class DBUtil {
    private static MongoClient mongoClient = null;

    public static MongoDatabase getDBConnection() {
        if (mongoClient == null) {
            // Connect using URI (adjust as needed)
            MongoClientURI uri = new MongoClientURI("mongodb+srv://Raghu:mbdEDjlHTXyp87TI@cluster0.tvkq752.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0");
            mongoClient = new MongoClient(uri);
        }
        MongoDatabase database = mongoClient.getDatabase("HouseManagementSystem");
        System.out.println("Connected to the database successfully");
        return database;
    }
}
