package nl.hdkesting.familyTree.infrastructure.models;

import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@NamedNativeQueries({
        @NamedNativeQuery(
                name = NameCountModel.LASTNAME_QUERY,
                query = "SELECT lastname, count(*) as count " +
                        "FROM individual GROUP BY lastname ORDER BY lastname",
//                query = "SELECT new nl.hdkesting.familyTree.core.dto.NameCount(lastname, count(*)) " +
//                        "FROM individual GROUP BY lastname ORDER BY lastname",
                resultClass = NameCountModel.class)
})

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
