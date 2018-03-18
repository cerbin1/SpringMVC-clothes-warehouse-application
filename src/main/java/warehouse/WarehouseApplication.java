package warehouse;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Repository
public class WarehouseApplication implements WarehouseRepository {
    private WarehousesGenerator warehousesGenerator;
    private List<Warehouse> warehouses;

    public WarehouseApplication(WarehousesGenerator warehousesGenerator) {
        this.warehousesGenerator = warehousesGenerator;
        generateWarehouses();
    }

    private void generateWarehouses() {
        warehouses = new ArrayList<>();
        for (int i = 0; i < new Random().nextInt(10); i++) {
            warehouses.add(warehousesGenerator.generateWarehouse());
        }
    }

    @Override
    public List<Warehouse> findAll() {
        return warehouses;
    }

    @Override
    public Warehouse findOne(String name) {
        return warehouses
                .stream()
                .filter(
                        warehouse -> warehouse
                                .getName()
                                .equals(name))
                .findFirst()
                .orElse(null);
    }

}
