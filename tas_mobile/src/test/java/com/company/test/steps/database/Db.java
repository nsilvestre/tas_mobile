package com.company.test.steps.database;

import com.company.framework.dtos.ProductDto;
import com.company.framework.utils.Context;
import com.company.framework.utils.ProductFactory;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.io.FileUtils;
import org.dbunit.Assertion;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.database.search.TablesDependencyHelper;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Statement;

import static com.company.framework.base.Framework.process_setup_db_handler;
import static com.company.framework.utils.DbSqlHelper.readSqlFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.dbunit.Assertion.assertEquals;


public class Db {
    private ProductFactory _productFactory;
    private IDatabaseConnection connection;
    private JdbcDatabaseTester databaseTester;
    public static final String TABLE = "product";
    private static Integer idProduct;
    private IDataSet dataSet;
    private ITable table;
    private ITableMetaData metadata;
    private Column[] columns;
    private IDataSet expectedDataSet;
    private QueryDataSet queryDataSet;
    private boolean exists;
    private File inputFile;
    private File outputFile;
    private final Context context;

    public Db(Context context) {
        this.context = context;
    }

    @After
    public void tearDown() throws Exception {
        databaseTester.onTearDown();
    }

    @Given("([^\"]*) Handler for \"([^\"]*)\"$")
    public void setup_handler(String configName, String typeTest) throws Exception {
        context.setValue("typeTest", typeTest);
        databaseTester = process_setup_db_handler(context, configName);

        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        dataSet = builder.build((getClass().getResourceAsStream("/datasets/product-init.xml")));

        databaseTester.setDataSet(dataSet);
        databaseTester.onSetup();
        //_productFactory = ProductFactory.getInstance();
        _productFactory = new ProductFactory(context);
    }

    @When("^I insert a product$")
    public void insert_product() throws Exception {
        //Insert a product in the BD
        ProductDto product = _productFactory.create(
                1,
                "TV Led 32",
                100,
                20,
                100,
                50,
                15);

        _productFactory.insert(product);

        //Recover the conexion with the DB
        connection = databaseTester.getConnection();

        //Configure the conexion as Mysql type
        DatabaseConfig dbconfig = connection.getConfig();
        dbconfig.setProperty("http://www.dbunit.org/properties/datatypeFactory", new MySqlDataTypeFactory());
    }

    @Then("^should validate and compare with the expected dataset$")
    public void validate_expected_dataset() throws Exception {
        //Get real values from the table
        IDataSet databaseDataSet = connection.createDataSet();
        ITable actualTable = databaseDataSet.getTable("product");

        //Set the expected values from product-expected.xml file
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        IDataSet expectedDataSet = builder.build(this.getClass().getResourceAsStream(
                "/datasets/product-expected.xml"));

        ITable expectedTable = expectedDataSet.getTable("product");

        //Compare the expected table with the real table
        assertEquals(expectedTable, actualTable);
    }

    @When("^I delete a product by id (\\d+)$")
    public void delete_product(int id) throws Exception {
        //Insert a product in the BD
        ProductDto product = _productFactory.create(
                1,
                "TV Led 32",
                100,
                20,
                100,
                50,
                15);

        _productFactory.insert(product);
        _productFactory.delete(id);

        connection = databaseTester.getConnection();

        DatabaseConfig dbconfig = connection.getConfig();
        dbconfig.setProperty("http://www.dbunit.org/properties/datatypeFactory", new MySqlDataTypeFactory());
    }

    @Then("^should validate expected result with rowcount (\\d+)$")
    public void validate_expected_result(int count) throws Exception {
        IDataSet databaseDataSet = connection.createDataSet();
        int rowCount = databaseDataSet.getTable("product").getRowCount();

        assertThat(count).isEqualTo(rowCount);
    }

    @When("^I search a product by id (\\d+)$")
    public void search_product(int id) throws Exception {
        //Insert a product in the BD
        ProductDto product = _productFactory.create(
                1,
                "TV Led 32",
                100,
                20,
                100,
                50,
                15);

        _productFactory.insert(product);

        connection = databaseTester.getConnection();

        DatabaseConfig dbconfig = connection.getConfig();
        dbconfig.setProperty("http://www.dbunit.org/properties/datatypeFactory", new MySqlDataTypeFactory());
        idProduct = id;
    }

    @Then("^should find the product$")
    public void find_product() throws Exception {
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        IDataSet expectedDataSet = builder.build(this.getClass().getResourceAsStream(
                "/datasets/product-expected.xml"));

        QueryDataSet queryDataSet = new QueryDataSet(connection);
        queryDataSet.addTable(TABLE, "SELECT * FROM product WHERE id = " + idProduct);
        assertEquals(expectedDataSet, queryDataSet);
    }

    @When("^I insert a products$")
    public void insert_products() throws Exception {
        ProductDto product = _productFactory.create(
                1,
                "TV Led 32",
                100,
                20,
                100,
                50,
                15);

        _productFactory.insert(product);

        product = _productFactory.create(
                2,
                "radio",
                30,
                200,
                10,
                20,
                18);

        _productFactory.insert(product);

        connection = databaseTester.getConnection();

        DatabaseConfig dbconfig = connection.getConfig();
        dbconfig.setProperty("http://www.dbunit.org/properties/datatypeFactory", new MySqlDataTypeFactory());
    }

    @Then("^should validate using xml files$")
    public void validate_using_xmls() throws Exception {
        IDataSet databaseDataSet = connection.createDataSet(new String[] {TABLE} );

        URL url = IDatabaseTester.class.getResource("/datasets/product-input-data.xml");
        assertThat(url).isNotNull();

        File inputFile = new File(url.getPath());
        File outputFile = new File("product-output-data.xml");
        FlatXmlDataSet.write(databaseDataSet, new FileOutputStream(outputFile));

        assertThat(FileUtils.readLines(inputFile, "UTF8")).
                isEqualTo(FileUtils.readLines(outputFile, "UTF8"));
    }

    @When("^I execute the store procedure \"([^\"]*)\"$")
    public void execute_store_procedure(String storeProcedureName) throws Exception {

        _productFactory.drop_procedure("getProducts");
        connection = databaseTester.getConnection();

        String localSqlFile = System.getProperty("user.dir") + "/src/test/resources/datasets/getProducts.sql";
        String sqlString= readSqlFile(localSqlFile);

        Statement stmt = connection.getConnection().createStatement() ;

        stmt.execute(sqlString);
        connection.getConnection().createStatement().execute(
                "{call `test`.`getProducts`()}");

        DatabaseConfig dbconfig = connection.getConfig();
        dbconfig.setProperty("http://www.dbunit.org/properties/datatypeFactory", new MySqlDataTypeFactory());
    }

    @When("^I get structure of the table \"([^\"]*)\"$")
    public void get_colums_from_table(String tableName) throws Exception {
        table = dataSet.getTable(tableName);
        metadata = table.getTableMetaData();
        columns = metadata.getColumns();

        connection = databaseTester.getConnection();

        queryDataSet = new QueryDataSet(connection);

        queryDataSet.addTable(tableName,
                             "SELECT column_name, column_type, column_key\n" +
                              "FROM information_schema.COLUMNS\n" +
                              "WHERE TABLE_NAME = '" + tableName + "'");
    }

    @Then("^should validate that number of fields is (\\d+)$")
    public void validate_number_of_fields(int numberFields) throws Exception {
        assertThat(numberFields).isEqualTo(metadata.getColumns().length);
    }

    @Then("^should validate name, type and primary key of the fields$")
    public void validate_fields() throws Exception {
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        IDataSet expectedDataSet = builder.build(this.getClass().getResourceAsStream("/datasets/product-structure-expected.xml"));
        ITable expectedTable = expectedDataSet.getTable("product");

        SortedTable sortedTable1 = new SortedTable(expectedTable, new String[]{"column_name"});
        SortedTable sortedTable2 = new SortedTable(queryDataSet.getTables()[0], new String[]{"column_name"});

        Assertion.assertEquals(sortedTable1, sortedTable2);
    }

    @When("^I count the rows in the search$")
    public void get_count_rows() throws Exception {
        connection = databaseTester.getConnection();

        DatabaseConfig dbconfig = connection.getConfig();
        dbconfig.setProperty("http://www.dbunit.org/properties/datatypeFactory", new MySqlDataTypeFactory());
    }

    @When("^I search the column \"([^\"]*)\" in the table \"([^\"]*)\"$")
    public void search_column_in_table(String colName, String tableName) throws Exception {
        table = dataSet.getTable(tableName);
        metadata = table.getTableMetaData();
        columns = metadata.getColumns();

        connection = databaseTester.getConnection();
        columns = metadata.getColumns();
        exists = false;
        for (Column column : columns) {
            if (column.getColumnName().equals(colName)) {
                exists = true;
            }
        }
    }

    @Then("^should validate if exists")
    public void validate_if_exists_column() throws Exception {
        assertThat(exists).isTrue();
    }

    @When("^I search the table \"([^\"]*)\" in the database$")
    public void search_column_in_table(String tableName) throws Exception {
        connection = databaseTester.getConnection();
        String[] tables = dataSet.getTableNames();
        exists = false;
        for (String tbl : tables) {
            if (tbl.equals(tableName)) {
                exists = true;
            }
        }
    }

    @When("^I search a Foreign Key in \"([^\"]*)\" and others tables$")
    public void search_foreign_key(String anotherTable) throws Exception {
        connection = databaseTester.getConnection();

        URL url = IDatabaseTester.class.getResource("/datasets/dependents-expected-data.xml");
        assertThat(url).isNotNull();
        inputFile = new File(url.getPath());

        String[] depTableNames = TablesDependencyHelper.getAllDependentTables( connection, anotherTable );
        IDataSet depDataset = connection.createDataSet( depTableNames );

        outputFile = new File("dependents.xml");
        FlatXmlDataSet.write(depDataset, new FileOutputStream(outputFile));
    }

    @Then("^should validate dependences")
    public void validate_dependences() throws Exception {
        assertThat(FileUtils.readLines(inputFile, "UTF8")).
                isEqualTo(FileUtils.readLines(outputFile, "UTF8"));
    }

}

