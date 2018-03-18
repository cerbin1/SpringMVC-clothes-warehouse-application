package warehouse;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("warehouses")
@RestController
public class WarehouseController {
    private WarehouseRepository warehouseRepository;

    public WarehouseController(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @GetMapping
    public List<Warehouse> findAll() {
        return warehouseRepository.findAll();
    }

    @GetMapping("{warehouseName}")
    public Warehouse findOne(@PathVariable String warehouseName) {
        return warehouseRepository.findOne(warehouseName);
    }

    @GetMapping("{warehouseName}/items")
    public List<Item> findItems(@PathVariable String warehouseName) {
        return findOne(warehouseName).getItems();
    }

    @GetMapping("{warehouseName}/items/{itemId:\\d+}")
    public Item findItemWithId(@PathVariable String warehouseName,
                               @PathVariable int itemId) {
        return warehouseRepository
                .findOne(warehouseName)
                .getItems()
                .stream()
                .filter(
                        item -> item.getId() == itemId)
                .findFirst()
                .orElse(null);
    }

    @GetMapping("{warehouseName}/items/{itemName:[A-Z]-?\\d+}")
    public Item findItemWithName(@PathVariable String warehouseName,
                                 @PathVariable String itemName) {
        return warehouseRepository
                .findOne(warehouseName)
                .getItems()
                .stream()
                .filter(
                        item -> item.getName().equals(itemName))
                .findFirst()
                .orElse(null);
    }
}
