package ru.sukhoa.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "dwd_fedirector")
public class FrontendDirector implements Serializable {

    private long id;

    private String name;

    public FrontendDirector() {
    }

    public FrontendDirector(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Id
    @Column(name = "fedirectorkey")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "fedirectorid")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FrontendDirector that = (FrontendDirector) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
