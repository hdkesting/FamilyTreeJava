package nl.hdkesting.familyTree.infrastructure.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class NameCountModel implements Serializable {
    public static final String LASTNAME_QUERY = "lastname-counts";

    @Id
    @Column(name = "lastname")
    private String lastname;

    @Column(name = "count")
    private long count;

    public NameCountModel() {
    }

    public NameCountModel(String lastname, long count) {
        this.lastname = lastname;
        this.count = count;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return String.format("%s * %d", this.lastname, this.count);
    }
}
