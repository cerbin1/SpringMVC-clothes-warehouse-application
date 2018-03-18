package warehouse;


import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @PostMapping("{warehouseName}/items")
    public Item addItem(@PathVariable String warehouseName,
                        @RequestParam String itemName,
                        @RequestParam int itemQuantity) {
        Item newItem = new Item(itemName, itemQuantity);
        Warehouse warehouse = warehouseRepository.findOne(warehouseName);
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
        List<Item> items = warehouseRepository.findOne(warehouseName).getItems();
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
        Warehouse warehouse = warehouseRepository.findOne(warehouseName);
        Item deletedItem = warehouse.getItemWithId(itemId);
        List<Item> items = warehouse.getItems().stream().filter(item -> item.getId() != itemId).collect(Collectors.toList());
        warehouse.setItems(items);
        return deletedItem;
    }
}
