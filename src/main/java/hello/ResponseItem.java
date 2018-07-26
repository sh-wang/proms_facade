package hello;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.io.Serializable;
import java.util.Objects;

public class ResponseItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Integer value;

    private String question;

    private String localId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResponseItem responseItem = (ResponseItem) o;
        if (responseItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), responseItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ResponseItem{" +
                "id=" + getId() +
                ", question='" + getQuestion() + "'" +
                ", localId='" + getLocalId() + "'" +
                ", value=" + getValue() +
                "}";
    }
}
