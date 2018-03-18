package warehouse;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class WarehousesGeneratorTest {

    @Test
    public void shouldGenerateWarehouse() {
        // given
        WarehousesGenerator warehousesGenerator = new WarehousesGenerator();

        // when
        Warehouse warehouse = warehousesGenerator.generateWarehouse();

        // then
        assertFalse(warehouse == null);
    }
}
