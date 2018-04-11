package warehouse.domain.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import warehouse.domain.Item;
import warehouse.exception.CategoryNotFoundException;
import warehouse.exception.ItemNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Item> getAllItems() {
        String sql = "SELECT * FROM ITEMS";
        Map<String, Object> params = new HashMap<>();
        return jdbcTemplate.query(sql, params, new ItemMapper());
    }

    @Override
    public Item getItemById(String itemId) {
        String sql = "SELECT * FROM ITEMS WHERE ID = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", itemId);
        try {
            return jdbcTemplate.queryForObject(sql, params, new ItemMapper());
        } catch (DataAccessException e) {
            throw new ItemNotFoundException();
        }
    }

    @Override
    public List<Item> getItemsByCategory(String category) {
        String sql = "SELECT * FROM ITEMS WHERE CATEGORY = :category";
        Map<String, Object> params = new HashMap<>();
        params.put("category", category);
        try {
            return jdbcTemplate.query(sql, params, new ItemMapper());
        } catch (DataAccessException e) {
            throw new CategoryNotFoundException();
        }
    }

    @Override
    public List<Item> getItemsByName(String name) {
        String sql = "SELECT * FROM ITEMS WHERE NAME = :name";
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        try {
            return jdbcTemplate.query(sql, params, new ItemMapper());
        } catch (DataAccessException e) {
            throw new ItemNotFoundException();
        }
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
