package me.adbi.eftw.dataaccess;

import me.adbi.eftw.business.enums.DBType;

public final class DBWriter {

    //region CTOR
    public DBWriter(String connString, DBType dbType)
    {
        switch (dbType) {
            case MY_SQL -> {
                System.out.println("MySQL");
            }
            case MONGO -> {
                //mongoClient = new MongoClient(mongoDbConnectionString);
                //mongoDB = mongoClient.GetDatabase("EscapeFromTheWoods");
                System.out.println("Mongo");
            }
        }
        this.connString = connString;
    }
    //endregion

    //region ATTRIB
    private String connString;
    //TODO: FIX db
    //private IMongoClient mongoClient;
    //private IMongoDatabase mongoDB;
    //endregion

    //region PROPS

    //endregion
}
