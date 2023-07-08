package me.adbi.eftw.dataaccess;

public final class DBWriter {

    //region CTOR
    public DBWriter(String msSqlConnectionString, String mongoDbConnectionString)
    {
        this.msSqlConnectionString = msSqlConnectionString;
        this.mongoDbConnectionString = mongoDbConnectionString;
        mongoClient = new MongoClient(mongoDbConnectionString);
        mongoDB = mongoClient.GetDatabase("EscapeFromTheWoods");
    }
    //endregion

    //region ATTRIB
    private String msSqlConnectionString, mongoDbConnectionString;
    private IMongoClient mongoClient;
    private IMongoDatabase mongoDB;
    //endregion

    //region PROPS
    
    //endregion
}
