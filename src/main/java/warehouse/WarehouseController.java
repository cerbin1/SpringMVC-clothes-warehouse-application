package warehouse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WarehouseController {
    private WarehouseRepository warehouseRepository;

    public WarehouseController(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @RequestMapping("warehouses")
    public List<Warehouse> findAll() {
        return warehouseRepository.findAll();
    }

    @RequestMapping("warehouses/{warehouseName}")
    public Warehouse findOne(@PathVariable String warehouseName) {
        return warehouseRepository.findOne(warehouseName);
    }
}
