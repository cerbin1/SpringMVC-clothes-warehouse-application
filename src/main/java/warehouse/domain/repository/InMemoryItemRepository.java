package warehouse.domain.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import warehouse.domain.Item;
import warehouse.exception.CategoryNotFoundException;
import warehouse.exception.ColorNotFoundException;
import warehouse.exception.ItemNotFoundException;
import warehouse.exception.SizeNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static warehouse.DatabaseNames.TABLE_NAME_ITEMS;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public InMemoryItemRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Item> getAllItems() {
        String sql = "SELECT * FROM " + TABLE_NAME_ITEMS + " ";
        Map<String, Object> params = new HashMap<>();
        return jdbcTemplate.query(sql, params, new ItemMapper());
    }

    @Override
    public Item getItemById(String itemId) {
        String sql = "SELECT * FROM " + TABLE_NAME_ITEMS + "  WHERE ID = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", itemId);
        try {
            return jdbcTemplate.queryForObject(sql, params, new ItemMapper());
        } catch (DataAccessException e) {
            throw new ItemNotFoundException();
        }
    }

    @Override
    public Item getItemByName(String name) {
        String sql = "SELECT * FROM " + TABLE_NAME_ITEMS + "  WHERE NAME = :name";
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        try {
            return jdbcTemplate.queryForObject(sql, params, new ItemMapper());
        } catch (DataAccessException e) {
            throw new ItemNotFoundException();
        }
    }

    @Override
    public List<Item> getItemsByCategory(String category) {
        String sql = "SELECT * FROM " + TABLE_NAME_ITEMS + "  WHERE CATEGORY = :category";
        Map<String, Object> params = new HashMap<>();
        params.put("category", category);
        List<Item> items = jdbcTemplate.query(sql, params, new ItemMapper());
        if (items.isEmpty()) {
            throw new CategoryNotFoundException();
        }
        return items;
    }

    @Override
    public List<Item> getItemsByColor(String color) {
        String sql = "SELECT * FROM " + TABLE_NAME_ITEMS + "  WHERE COLOR = :color";
        Map<String, Object> params = new HashMap<>();
        params.put("color", color);
        List<Item> items = jdbcTemplate.query(sql, params, new ItemMapper());
        if (items.isEmpty()) {
            throw new ColorNotFoundException();
        }
        return items;
    }

    @Override
    public List<Item> getItemsBySize(String size) {
        String sql = "SELECT * FROM " + TABLE_NAME_ITEMS + "  WHERE SIZE = :size";
        Map<String, Object> params = new HashMap<>();
        params.put("size", size);
        List<Item> items = jdbcTemplate.query(sql, params, new ItemMapper());
        if (items.isEmpty()) {
            throw new SizeNotFoundException();
        }
        return items;
    }

    @Override
    public List<Item> getItemsByArchived(boolean archived) {
        String sql = "SELECT * FROM " + TABLE_NAME_ITEMS + "  WHERE ARCHIVED = :archived";
        Map<String, Object> params = new HashMap<>();
        params.put("archived", archived);
        return jdbcTemplate.query(sql, params, new ItemMapper());
    }

    @Override
    public void addItem(Item newItem) {
        String sql = "INSERT INTO " + TABLE_NAME_ITEMS + "  VALUES (" +
                ":id, :name, :category, :color, :size, :quantity, :archived)";
        Map<String, Object> params = new HashMap<>();
        params.put("id", newItem.getItemId());
        params.put("name", newItem.getName());
        params.put("category", newItem.getCategory());
        params.put("color", newItem.getColor());
        params.put("size", newItem.getSize());
        params.put("quantity", newItem.getQuantity());
        params.put("archived", newItem.isArchived());
        jdbcTemplate.update(sql, params);
    }

    @Override
    public void updateItem(Item item, String itemId) {
        String sql = "UPDATE " + TABLE_NAME_ITEMS + "  SET " +
                "NAME=:name, " +
                "CATEGORY=:category, " +
                "COLOR=:color, " +
                "SIZE=:size, " +
                "QUANTITY=:quantity, " +
                "ARCHIVED=:archived WHERE ID=:id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", itemId);
        params.put("name", item.getName());
        params.put("category", item.getCategory());
        params.put("color", item.getColor());
        params.put("size", item.getSize());
        params.put("quantity", item.getQuantity());
        params.put("archived", item.isArchived());
        jdbcTemplate.update(sql, params);
    }

    @Override
    public void delete(String itemId) {
        String sql = "DELETE FROM " + TABLE_NAME_ITEMS + "  WHERE ID=:id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", itemId);
        jdbcTemplate.update(sql, params);
    }

    @Override
    public void deleteItems() {
        String sql = "TRUNCATE TABLE " + TABLE_NAME_ITEMS + " ";
        jdbcTemplate.update(sql, new HashMap<>());
    }

    private class ItemMapper implements RowMapper<Item> {
        @Override
        public Item mapRow(ResultSet resultSet, int i) throws SQLException {
            Item item = new Item();
            item.setItemId(resultSet.getString("ID"));
            item.setName(resultSet.getString("NAME"));
            item.setCategory(resultSet.getString("CATEGORY"));
            item.setColor(resultSet.getString("COLOR"));
            item.setSize(resultSet.getString("SIZE"));
            item.setQuantity(resultSet.getLong("QUANTITY"));
            item.setArchived(resultSet.getBoolean("ARCHIVED"));
            return item;
        }
    }
}
