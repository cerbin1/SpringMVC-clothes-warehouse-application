package warehouse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import warehouse.config.WebApplicationContextConfig;
import warehouse.domain.Item;
import warehouse.domain.repository.ItemRepository;
import warehouse.service.ItemService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebApplicationContextConfig.class)
@WebAppConfiguration
public class ItemControllerTest {
    private final List<Item> items = new ArrayList<>();
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Mock
    private ItemService itemService;
    @Autowired
    private ItemRepository itemRepository;
    private EmbeddedDatabase db;
    private JdbcTemplate jdbcTemplate;
    private MockMvc mockMvc;

    private static String asJsonString(final Object o) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .generateUniqueName(true)
                .addScript("db/sql/create-table.sql")
                .build();

        jdbcTemplate = new JdbcTemplate(db);

        addItemsToDatabase();
    }

    private void addItemsToDatabase() {
        itemRepository.deleteItems();

        Item item1 = new Item();
        item1.setItemId("1");
        item1.setName("Shirt A");
        item1.setCategory("Shirt");
        item1.setColor("Black");
        item1.setSize("XL");
        item1.setQuantity(15);
        item1.setArchived(false);
        items.add(item1);
        itemRepository.addItem(item1);

        Item item2 = new Item();
        item2.setItemId("2");
        item2.setName("Trousers D");
        item2.setCategory("Trousers");
        item2.setColor("Brown");
        item2.setSize("S");
        item2.setQuantity(12);
        item2.setArchived(true);
        items.add(item2);
        itemRepository.addItem(item2);
    }

    @Test
    public void shouldGetItems() throws Exception {
        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].itemId").value(items.get(0).getItemId()))
                .andExpect(jsonPath("$[0].name").value(items.get(0).getName()))
                .andExpect(jsonPath("$[0].category").value(items.get(0).getCategory()))
                .andExpect(jsonPath("$[0].color").value(items.get(0).getColor()))
                .andExpect(jsonPath("$[0].size").value(items.get(0).getSize()))
                .andExpect(jsonPath("$[0].quantity").value(items.get(0).getQuantity().intValue()))
                .andExpect(jsonPath("$[0].archived").value(items.get(0).isArchived()))
                .andExpect(jsonPath("$[1].itemId").value(items.get(1).getItemId()))
                .andExpect(jsonPath("$[1].name").value(items.get(1).getName()))
                .andExpect(jsonPath("$[1].category").value(items.get(1).getCategory()))
                .andExpect(jsonPath("$[1].color").value(items.get(1).getColor()))
                .andExpect(jsonPath("$[1].size").value(items.get(1).getSize()))
                .andExpect(jsonPath("$[1].quantity").value(items.get(1).getQuantity().intValue()))
                .andExpect(jsonPath("$[1].archived").value(items.get(1).isArchived()));
    }

    @Test
    public void shouldGetItemById() throws Exception {
        mockMvc.perform(get("/items/item/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.itemId").value(items.get(0).getItemId()))
                .andExpect(jsonPath("$.name").value(items.get(0).getName()))
                .andExpect(jsonPath("$.category").value(items.get(0).getCategory()))
                .andExpect(jsonPath("$.color").value(items.get(0).getColor()))
                .andExpect(jsonPath("$.size").value(items.get(0).getSize()))
                .andExpect(jsonPath("$.quantity").value(items.get(0).getQuantity().intValue()))
                .andExpect(jsonPath("$.archived").value(items.get(0).isArchived()));
    }

    @Test
    public void shouldNotGetItemById() throws Exception {
        mockMvc.perform(get("/items/item/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetItemByName() throws Exception {
        mockMvc.perform(get("/items/item/Shirt A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.itemId").value(items.get(0).getItemId()))
                .andExpect(jsonPath("$.name").value(items.get(0).getName()))
                .andExpect(jsonPath("$.category").value(items.get(0).getCategory()))
                .andExpect(jsonPath("$.color").value(items.get(0).getColor()))
                .andExpect(jsonPath("$.size").value(items.get(0).getSize()))
                .andExpect(jsonPath("$.quantity").value(items.get(0).getQuantity().intValue()))
                .andExpect(jsonPath("$.archived").value(items.get(0).isArchived()));
    }

    @Test
    public void shouldNotGetItemByName() throws Exception {
        mockMvc.perform(get("/items/item/foo"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetItemsByCategory() throws Exception {
        mockMvc.perform(get("/items/category/Shirt"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(items.get(0).getName()))
                .andExpect(jsonPath("$[0].category").value(items.get(0).getCategory()))
                .andExpect(jsonPath("$[0].color").value(items.get(0).getColor()))
                .andExpect(jsonPath("$[0].size").value(items.get(0).getSize()))
                .andExpect(jsonPath("$[0].quantity").value(items.get(0).getQuantity().intValue()))
                .andExpect(jsonPath("$[0].archived").value(items.get(0).isArchived()));
    }

    @Test
    public void shouldNotGetItemsByCategory() throws Exception {
        mockMvc.perform(get("/items/category/foo"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetItemsByColor() throws Exception {
        mockMvc.perform(get("/items/color/Black"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(items.get(0).getName()))
                .andExpect(jsonPath("$[0].category").value(items.get(0).getCategory()))
                .andExpect(jsonPath("$[0].color").value(items.get(0).getColor()))
                .andExpect(jsonPath("$[0].size").value(items.get(0).getSize()))
                .andExpect(jsonPath("$[0].quantity").value(items.get(0).getQuantity().intValue()))
                .andExpect(jsonPath("$[0].archived").value(items.get(0).isArchived()));
    }

    @Test
    public void shouldNotGetItemsByColor() throws Exception {
        mockMvc.perform(get("/items/color/foo"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetItemsBySize() throws Exception {
        mockMvc.perform(get("/items/size/XL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(items.get(0).getName()))
                .andExpect(jsonPath("$[0].category").value(items.get(0).getCategory()))
                .andExpect(jsonPath("$[0].color").value(items.get(0).getColor()))
                .andExpect(jsonPath("$[0].size").value(items.get(0).getSize()))
                .andExpect(jsonPath("$[0].quantity").value(items.get(0).getQuantity().intValue()))
                .andExpect(jsonPath("$[0].archived").value(items.get(0).isArchived()));
    }

    @Test
    public void shouldNotGetItemsBySize() throws Exception {
        mockMvc.perform(get("/items/size/foo"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetItemsByArchived() throws Exception {
        mockMvc.perform(get("/items/archived/true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(items.get(1).getName()))
                .andExpect(jsonPath("$[0].category").value(items.get(1).getCategory()))
                .andExpect(jsonPath("$[0].color").value(items.get(1).getColor()))
                .andExpect(jsonPath("$[0].size").value(items.get(1).getSize()))
                .andExpect(jsonPath("$[0].quantity").value(items.get(1).getQuantity().intValue()))
                .andExpect(jsonPath("$[0].archived").value(items.get(1).isArchived()));
    }

    @Test
    public void shouldGetBadRequestStatusWhenPassingWrongArchivedParam() throws Exception {
        mockMvc.perform(get("/items/archived/foo"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldCreateItem() throws Exception {
        // given
        Item item = new Item();
        item.setItemId("15");
        item.setName("Trousers D");
        item.setCategory("Trousers");
        item.setColor("Brown");
        item.setSize("S");
        item.setQuantity(12);
        item.setArchived(true);

        // when
        mockMvc.perform(post("/items")
                .content(asJsonString(item))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated());

        // then
        mockMvc.perform(get("/items/item/" + item.getItemId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.itemId").value(item.getItemId()))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.category").value(item.getCategory()))
                .andExpect(jsonPath("$.color").value(item.getColor()))
                .andExpect(jsonPath("$.size").value(item.getSize()))
                .andExpect(jsonPath("$.quantity").value(item.getQuantity().intValue()))
                .andExpect(jsonPath("$.archived").value(item.isArchived()));
    }

    @Test
    public void shouldReturnBadRequestStatusWhenItemToCreateIsNull() throws Exception {
        mockMvc.perform(post("/items"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnConflictStatusWhenIdOfItemToAddExist() throws Exception {
        mockMvc.perform(post("/items")
                .content(asJsonString(items.get(0)))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldUpdateItem() throws Exception {
        Item item = new Item();
        String updatedItemId = items.get(0).getItemId();
        item.setItemId(updatedItemId);
        String newName = "Nice shoes";
        String newCategory = "Shoes";
        String newColor = "Red";
        String newSize = "XXL";
        Long newQuantity = 150L;

        item.setName(newName);
        item.setCategory(newCategory);
        item.setColor(newColor);
        item.setSize(newSize);
        item.setQuantity(newQuantity);
        item.setArchived(false);

        mockMvc.perform(put("/items/item/" + updatedItemId)
                .content(asJsonString(item))
                .contentType(APPLICATION_JSON)
        ).andExpect(status().isOk());

        mockMvc.perform(get("/items/item/" + updatedItemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.itemId").value(updatedItemId))
                .andExpect(jsonPath("$.name").value(newName))
                .andExpect(jsonPath("$.category").value(newCategory))
                .andExpect(jsonPath("$.color").value(newColor))
                .andExpect(jsonPath("$.size").value(newSize))
                .andExpect(jsonPath("$.quantity").value(newQuantity.intValue()))
                .andExpect(jsonPath("$.archived").value(false));
    }

    @Test
    public void shouldReturnBadRequestStatusWhenItemToUpdateIsNull() throws Exception {
        mockMvc.perform(put("/items/item/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnNotFoundStatusWhenItemToUpdateNotExist() throws Exception {
        Item item = items.get(0);
        mockMvc.perform(put("/items/item/" + 150)
                .content(asJsonString(item))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteItem() throws Exception {
        mockMvc.perform(get("/items"))
                .andExpect(jsonPath("$", hasSize(2)));

        mockMvc.perform(delete("/items/item/" + items.get(0).getItemId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/items"))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void shouldReturnNotFoundStatusWhenItemWithIdToDeleteNotExist() throws Exception {
        mockMvc.perform(delete("/items/item/150"))
                .andExpect(status().isNotFound());
    }

    @After
    public void tearDown() {
        db.shutdown();

    }
}
