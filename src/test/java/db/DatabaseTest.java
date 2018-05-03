package db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.deleteFromTables;
import static warehouse.DatabaseNames.TABLE_ITEMS_NAME;

public class DatabaseTest {
    private EmbeddedDatabase db;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .generateUniqueName(true)
                .addScript("db/sql/create-table.sql")
                .addScript("db/sql/insert-data.sql")
                .build();

        jdbcTemplate = new JdbcTemplate(db);
    }

    @Test
    public void testDataAccess() {
        int itemsCount = countRowsInTable(jdbcTemplate, TABLE_ITEMS_NAME);

        assertEquals(15, itemsCount);
    }

    @Test
    public void shouldDeleteRows() {
        int deletedRows = deleteFromTables(jdbcTemplate, TABLE_ITEMS_NAME);
        int itemsCount = countRowsInTable(jdbcTemplate, TABLE_ITEMS_NAME);

        assertEquals(15, deletedRows);
        assertEquals(0, itemsCount);
    }

    @After
    public void tearDown() {
        db.shutdown();
    }
}
