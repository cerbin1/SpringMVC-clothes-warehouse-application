package warehouse.domain.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import warehouse.domain.Item;

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

    private class ItemMapper implements RowMapper<Item> {

        @Override
        public Item mapRow(ResultSet resultSet, int i) throws SQLException {
            Item item = new Item();
            item.setId(resultSet.getString("ID"));
            item.setName(resultSet.getString("NAME"));
            item.setQuantity(resultSet.getLong("QUANTITY"));
            item.setUnit(resultSet.getString("UNIT"));
            return item;
        }
    }
}
