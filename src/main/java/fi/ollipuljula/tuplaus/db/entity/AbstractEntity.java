package fi.ollipuljula.tuplaus.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
abstract public class AbstractEntity implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Version
    private Long version;

    private LocalDateTime changed;

    @PreUpdate
    @PrePersist
    public void setChanged() {
        this.changed = LocalDateTime.now();
    }
}
