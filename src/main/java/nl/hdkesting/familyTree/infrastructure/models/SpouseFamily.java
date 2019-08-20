package nl.hdkesting.familyTree.infrastructure.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "spouses")
public class SpouseFamily implements Serializable {
    @Id
    @Column(name = "spouseid")
    public Long spouseId;

    @Id
    @Column(name = "familyid")
    public Long familyId;

    public SpouseFamily() {}

    public SpouseFamily(long familyId, long spouseId) {
        this.familyId = familyId;
        this.spouseId = spouseId;
    }
}
