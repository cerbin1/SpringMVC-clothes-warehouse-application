package warehouse;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class WarehouseService {
    private static final int MAX_NUMBER_OF_WAREHOUSES = 10;
    private WarehouseRepository warehouseRepository;
    private WarehousesGenerator warehousesGenerator;

    public WarehouseService(WarehouseRepository warehouseRepository, WarehousesGenerator warehousesGenerator) {
        this.warehouseRepository = warehouseRepository;
        this.warehousesGenerator = warehousesGenerator;
        generateWarehouses();
    }

    private void generateWarehouses() {
        int numberOfWarehouses = new Random().nextInt(MAX_NUMBER_OF_WAREHOUSES) + 1;
        for (int i = 0; i < numberOfWarehouses; i++) {
            warehouseRepository.save(warehousesGenerator.generateWarehouse());
        }
    }

    public List<Warehouse> getAllWarehouses() {
        List<Warehouse> warehouses = new ArrayList<>();
        warehouseRepository.findAll().forEach(warehouses::add);
        return warehouses;
    }

    public Warehouse getWarehouse(String name) {
        return warehouseRepository.findById(name).orElse(null);
    }

    public Warehouse addWarehouse(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }
}
