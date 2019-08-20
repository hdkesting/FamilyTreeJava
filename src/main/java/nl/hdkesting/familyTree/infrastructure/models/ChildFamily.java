package nl.hdkesting.familyTree.infrastructure.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "children")
public class ChildFamily implements Serializable {
    @Id
    @Column(name = "childid")
    public Long childId;

    @Id
    @Column(name = "familyid")
    public Long familyId;

    public ChildFamily() {}

    public ChildFamily(long familyId, long childId) {
        this.familyId = familyId;
        this.childId = childId;
    }

}
