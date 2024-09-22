package com.vit.hms.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.vit.hms.bean.RentalPropertyBean;
import com.vit.hms.util.DBUtil;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RentalPropertyDAO {

    // Method to generate property ID
    public String generatePropertyID(String city) {
        Random rand = new Random();
        int number = rand.nextInt(9000) + 1000; // Ensures a 4-digit number
        return city.substring(0, 3).toUpperCase() + number;
    }

    // Method to insert a new rental property
    public String createRentalProperty(RentalPropertyBean bean) {
        MongoDatabase database = DBUtil.getDBConnection();
        MongoCollection<Document> collection = database.getCollection("RENTAL_TBL");

        String propertyId;
        do {
            propertyId = generatePropertyID(bean.getCity());
        } while (collection.find(Filters.eq("propertyId", propertyId)).first() != null); // Ensure uniqueness

        bean.setPropertyId(propertyId);

        Document doc = new Document("propertyId", bean.getPropertyId())
                .append("rentalAmount", bean.getRentalAmount())
                .append("noOfBedRooms", bean.getNoOfBedRooms())
                .append("location", bean.getLocation())
                .append("city", bean.getCity());

        collection.insertOne(doc);
        return propertyId; // Return the generated property ID
    }

    // Method to find rental properties by criteria
    public List<RentalPropertyBean> findPropertyByCriteria(float minRentalAmount, float maxRentalAmount, RentalPropertyBean bean) {
        MongoDatabase database = DBUtil.getDBConnection();
        MongoCollection<Document> collection = database.getCollection("RENTAL_TBL");

        List<RentalPropertyBean> propertyList = new ArrayList<>();
        for (Document document : collection.find(Filters.and(
                Filters.gte("rentalAmount", minRentalAmount),
                Filters.lte("rentalAmount", maxRentalAmount),
                Filters.eq("noOfBedRooms", bean.getNoOfBedRooms()),
                Filters.eq("location", bean.getLocation()),
                Filters.eq("city", bean.getCity())
        ))) {
            RentalPropertyBean resultBean = new RentalPropertyBean();
            resultBean.setRentalAmount(document.getDouble("rentalAmount").floatValue()); // Use getDouble and convert to float
            resultBean.setNoOfBedRooms(document.getInteger("noOfBedRooms"));
            resultBean.setLocation(document.getString("location"));
            resultBean.setCity(document.getString("city"));
            resultBean.setPropertyId(document.getString("propertyId"));
            propertyList.add(resultBean);
        }

        return propertyList;
    }
}
