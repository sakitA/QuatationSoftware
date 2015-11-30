package quotationsoftware.model;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class for bathroom object with javafx specific properties.
 */
public class Bathroom {

    private SimpleIntegerProperty id;
    private SimpleStringProperty description;
    private SimpleStringProperty series;
    private SimpleDoubleProperty amount;

    private List<Item> listItems = new ArrayList<>();

    public IntegerProperty idProperty() {
        if (id == null) {
            id = new SimpleIntegerProperty(this, "id");
        }
        return id;
    }

    public void setId(Integer id) {
        idProperty().set(id);
    }

    public Integer getId() {
        return idProperty().get();
    }

    public StringProperty descriptionProperty() {
        if (description == null) {
            description = new SimpleStringProperty(this, "description");
        }
        return description;
    }

    public void setDescription(String description) {
        descriptionProperty().set(description);
    }

    public String getDescription() {
        return descriptionProperty().get();
    }

    public StringProperty seriesProperty() {
        if (series == null) {
            series = new SimpleStringProperty(this, "series");
        }
        return series;
    }

    public void setSeries(String series) {
        seriesProperty().set(series);
    }

    public String getSeries() {
        return seriesProperty().get();
    }

    public DoubleProperty amountProperty() {
        if (amount == null) {
            amount = new SimpleDoubleProperty(this, "amount");
        }
        return amount;
    }

    public void setAmount(Double amount) {
        amountProperty().set(amount);
    }

    public Double getAmount() {
        return amountProperty().get();
    }
    
    public List<Item> getListItems() {
        return listItems;
    }

    public void setListItems(List<Item> listItems) {
        this.listItems = listItems;
    }
}