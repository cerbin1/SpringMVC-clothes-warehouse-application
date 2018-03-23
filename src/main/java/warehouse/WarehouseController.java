package warehouse;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("warehouses")
@RestController
public class WarehouseController {
    private WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping
    public List<Warehouse> findAll() {
        return warehouseService.getAllWarehouses();
    }

    @GetMapping("{warehouseName}")
    public Warehouse findOne(@PathVariable String warehouseName) {
        return warehouseService.getWarehouse(warehouseName);
    }

    @PostMapping
    public Warehouse addWarehouse(@RequestBody Warehouse warehouse) {
        return warehouseService.addWarehouse(warehouse);
    }

/*
    @GetMapping("{warehouseName}/items")
    public List<Item> findItems(@PathVariable String warehouseName) {
        return findOne(warehouseName).getItems();
    }

    @GetMapping("{warehouseName}/items/{itemId:\\d+}")
    public Item findItemWithId(@PathVariable String warehouseName,
                               @PathVariable int itemId) {
        return warehouseService
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
        return warehouseService
                .findOne(warehouseName)
                .getItems()
                .stream()
                .filter(
                        item -> item.getName().equals(itemName))
                .findFirst()
                .orElse(null);
    }

    @PostMapping("{warehouseName}/items")
    public Item addItem(@PathVariable String warehouseName,
                        @RequestParam String itemName,
                        @RequestParam int itemQuantity) {
        Item newItem = new Item(itemName, itemQuantity);
        Warehouse warehouse = warehouseService.findOne(warehouseName);
        List<Item> items = warehouse.getItems();
        items.add(newItem);
        warehouse.setItems(items);
        return newItem;
    }

    @PutMapping("{warehouseName}/items/{itemId:\\d+}")
    public Item updateItemWithId(@PathVariable String warehouseName,
                                 @PathVariable int itemId,
                                 @RequestParam String itemName,
                                 @RequestParam int itemQuantity) {
        Item newItem = new Item(itemId, itemName, itemQuantity);
        List<Item> items = warehouseService.findOne(warehouseName).getItems();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == itemId) {
                items.set(i, newItem);
                break;
            }
        }
        return newItem;
    }

    @DeleteMapping("{warehouseName}/items/{itemId:\\d+}")
    public Item deleteItemWithId(@PathVariable String warehouseName,
                                 @PathVariable int itemId
    ) {
        Warehouse warehouse = warehouseService.findOne(warehouseName);
        Item deletedItem = warehouse.getItemWithId(itemId);
        List<Item> items = warehouse
                .getItems()
                .stream()
                .filter(
                        item -> item.getId() != itemId)
                .collect(Collectors.toList());
        warehouse.setItems(items);
        return deletedItem;
    }*/
}
