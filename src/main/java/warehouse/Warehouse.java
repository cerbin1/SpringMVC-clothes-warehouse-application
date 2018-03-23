package warehouse;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Warehouse {
    @Id
    private String name;

    public Warehouse() {

    }

    public Warehouse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
