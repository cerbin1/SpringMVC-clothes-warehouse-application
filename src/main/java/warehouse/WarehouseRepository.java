package warehouse;

import java.util.List;

public interface WarehouseRepository {
    List<Warehouse> findAll();

    Warehouse findOne(String name);
}
