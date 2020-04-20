package com.company.test.steps.databasenonsql;


import com.company.framework.utils.Context;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.main.JsonValidator;
import com.mongodb.*;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import com.mongodb.util.JSON;
import java.io.*;
import static com.company.framework.base.Framework.process_setup_dbnonsql_handler;
import static com.company.framework.utils.DbNonSqlHelper.readFile;
import static org.assertj.core.api.Assertions.assertThat;


public class Dbnonsql {
    private static MongoClient client;
    private static DBCollection collection;
    private static DBCursor cursor = null;
    private static BasicDBObject query = null;
    private static int idProduct;
    private static DB productDB = null;
    private static DBObject dbObject = null;
    private static DBObject doc = null;
    private static boolean collectionExists;
    private final Context context;

    public Dbnonsql(Context context) {
        this.context = context;
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    @Given("([^\"]*) Handler for \"([^\"]*)\"$")
    public void setup_handler(String configName, String typeTest) throws Exception {
        context.setValue("typeTest", typeTest);
        client = process_setup_dbnonsql_handler(context, configName);

        String dbname = context.getValue("dbname").toString();
        String col = context.getValue("collection").toString();

        productDB = client.getDB(dbname);
        collection   = productDB.getCollection(col);
    }

    @When("^I insert a document in the collection$")
    public void insert_document() throws IOException {
        String jsonData = readFile(System.getProperty("user.dir") +
                                    "/src/test/resources/datasets/product.json");

        dbObject = (DBObject)JSON.parse(jsonData);

        collection.insert(dbObject);
        idProduct = 1;
    }

    @Then("^I must validate existence$")
    public void validate_existence() throws Exception {
        BasicDBObject query = new BasicDBObject("id", idProduct);
        cursor = collection.find(query);
        while(cursor.hasNext()) {
            DBObject document = cursor.next();
            assertThat(idProduct).isEqualTo(document.get("id"));
        }
    }

    @When("^I find first matched document$")
    public void get_first_matched_document() throws Exception {
        String jsonData = readFile(System.getProperty("user.dir") +
                                    "/src/test/resources/datasets/product.json");

        query = new BasicDBObject("id", "1");

        dbObject = (DBObject)JSON.parse(jsonData);
    }

    @Then("^I must validate fields of the document$")
    public void validate_document() throws Exception {
        doc = collection.findOne();

        assertThat(doc.get("id")).isEqualTo(dbObject.get("id"));
        assertThat(doc.get("name")).isEqualTo(dbObject.get("name"));
        assertThat(doc.get("price")).isEqualTo(dbObject.get("price"));
        assertThat(doc.get("stock")).isEqualTo(dbObject.get("stock"));
        assertThat(doc.get("lenght")).isEqualTo(dbObject.get("lenght"));
        assertThat(doc.get("width")).isEqualTo(dbObject.get("width"));
        assertThat(doc.get("height")).isEqualTo(dbObject.get("height"));
    }

    @When("^I delete a document by id equal to (\\d+)$")
    public void delete_document(int id) throws IOException {
        BasicDBObject query = new BasicDBObject("id", id);
        collection.remove(query);
        cursor = collection.find(query);
    }

    @Then("^I must validate schema json file$")
    public void validate_schema_json() throws Exception {
        cursor = collection.find(query);
        String json = "" + cursor.next().toString();
        JsonValidator VALIDATOR  = JsonSchemaFactory.byDefault().getValidator();

        JsonNode schemaNode= JsonLoader.fromResource("/schemas/product_schema.json");
        JsonNode data = JsonLoader.fromString(json);

        ProcessingReport report = VALIDATOR.validateUnchecked(schemaNode, data);

        assertThat(report.isSuccess()).isTrue();
    }

    @When("^I search all documents by name equal to \"([^\"]*)\"$")
    public void search_some_products(String nameField) throws Exception {
        query = new BasicDBObject("name", nameField);
        cursor = collection.find(query);
    }

    @When("^I search a document by id equal to (\\d+)$")
    public void search_some_products(int id) throws Exception {
        query = new BasicDBObject("id", id);
        cursor = collection.find(query);
    }

    @Then("^I must validate that number of results is (\\d+)$")
    public void validate_existence(int numberResults) throws Exception {
        assertThat(numberResults).isEqualTo(cursor.length());
    }

    @When("^I search the collection \"([^\"]*)\"$")
    public void search_collection(String collname) throws Exception {
        //collectionExists = productDB.getCollectionNames().contains(collname); // Works with Windows only
        collectionExists = productDB.getCollectionFromString(collname).toString().contains(collname);
    }

    @Then("^I must validate if exists$")
    public void validate_if_exists() throws Exception {
        assertThat(collectionExists).isTrue();
    }

    @Then("^I must validate if it is empty$")
    public void validate_if_it_is_empty() throws Exception {
        assertThat(collectionExists).isTrue();
        collection   = productDB.getCollection("products");
        assertThat(0L).isEqualTo(collection.count());
    }

}

