package warehouse;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

@Service
public class WarehousesGenerator {
    private static final int MAX_NUMBER_OF_ITEMS = 10;
    private static final char[] CHARACTERS = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'I', 'J', 'K'};
    private static final String PATH_TO_FILE_NAMES = "src/main/resources/names";
    private static final Random random = new Random();
    private final List<String> names;
    private static int generatedWarehouses = 0;

    public WarehousesGenerator() {
        names = initializeNames();
    }

    public Warehouse generateWarehouse() {
        String randomWarehouseName = names.get(generatedWarehouses++);
        List<Item> randomItems = generateItems();
        return new Warehouse(randomWarehouseName, randomItems);
    }

    private List<Item> generateItems() {
        int itemsCount = random.nextInt(MAX_NUMBER_OF_ITEMS);
        List<Item> items = new ArrayList<>(itemsCount);
        for (int i = 0; i < itemsCount; i++) {
            String randomName = "" + CHARACTERS[random.nextInt(CHARACTERS.length)] + random.nextInt();
            int randomQuantity = random.nextInt(1000);
            items.add(new Item(randomName, randomQuantity));
        }
        return items;
    }

    private List<String> initializeNames() {
        List<String> names = new ArrayList<>(10);
        try {
            File fileWithNames = new File(PATH_TO_FILE_NAMES);
            Scanner scanner = new Scanner(fileWithNames);
            while (scanner.hasNextLine()) {
                names.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.err.println("File: names not found");
        }
        return names;
    }
}
