package quotationsoftware.model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import static quotationsoftware.QuatationSoftware.PROP;
/**
 * Model class for item object with javafx specific properties.
 */
public class Item {

    private IntegerProperty id;
    private IntegerProperty serialNo;
    private StringProperty itemCode;
    private StringProperty itemDescription;
    private IntegerProperty quantity;
    private DoubleProperty mrp;
    private DoubleProperty nrp;
    private DoubleProperty discount;
    private DoubleProperty vatPercentage;
    private DoubleProperty nrpWithoutVat;
    private DoubleProperty rdWithoutVat;
    private DoubleProperty vatAmount;
    private DoubleProperty rdIncludingVat;
    private DoubleProperty marginAmount;
    private DoubleProperty marginPercentage;
    private IntegerProperty image;
    private IntegerProperty quotationId;
    private IntegerProperty bathroomId;
    private IntegerProperty bathroomElement;
    private DoubleProperty totalAmount;
    private String itemCodeL;
    private String itemCodeM;
    private String itemCodeS;    
    private String itemDescriptionS;
    private String itemDescriptionL;
    private boolean label;
    private Double percentage = null;
    
    public Item() {
        this.id = new SimpleIntegerProperty(-1);
        this.image = new SimpleIntegerProperty(-1);
        this.bathroomId = new SimpleIntegerProperty(-1);
        this.label = false;
    }

    /**
     * Constructor which copies the properties of the parameter item.
     * @param original
     */
    public Item(Item original) {
        this.id = new SimpleIntegerProperty(original.getId());
        this.serialNo = new SimpleIntegerProperty(original.getSerialNo());
        this.itemCode = new SimpleStringProperty(original.getItemCode());
        this.itemDescription = new SimpleStringProperty(original.getItemDescription());
        this.quantity = new SimpleIntegerProperty(original.getQuantity());
        this.mrp = new SimpleDoubleProperty(original.getMrp());
        this.nrp = new SimpleDoubleProperty(original.getNrp());
        this.discount = new SimpleDoubleProperty(original.getDiscount());
        this.vatPercentage = new SimpleDoubleProperty(original.getVatPercentage());
        this.nrpWithoutVat = new SimpleDoubleProperty(original.getNrpWithoutVat());
        this.rdWithoutVat = new SimpleDoubleProperty(original.getRdWithoutVat());
        this.vatAmount = new SimpleDoubleProperty(original.getVatAmount());
        this.rdIncludingVat = new SimpleDoubleProperty(original.getRdIncludingVat());
        this.marginAmount = new SimpleDoubleProperty(original.getMarginAmount());
        this.marginPercentage = new SimpleDoubleProperty(original.getMarginPercentage());
        this.image = new SimpleIntegerProperty(original.getImage());
        this.quotationId = new SimpleIntegerProperty(original.getQuotationId());
        this.bathroomElement = new SimpleIntegerProperty(original.getBathroomElement());
        this.totalAmount = new SimpleDoubleProperty(original.getTotalAmount());
    }

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

    public IntegerProperty serialNoProperty() {
        if (serialNo == null) {
            serialNo = new SimpleIntegerProperty(this, "serialNo");            
        }
        
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        serialNoProperty().set(serialNo);
    }

    public Integer getSerialNo() {
        return serialNoProperty().get();
    }

    public StringProperty itemCodeProperty() {
        if (itemCode == null) {
            itemCode = new SimpleStringProperty(this, "itemCode", getItemCode());
        }
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        itemCodeProperty().set(itemCode);
    }

    public String getItemCode() {
        return getCode();
    }
    
    private String getCode(){
        String code;
        switch(PROP.getProperty("product_code")){
            case "Long":
                code = itemCodeL;
                break;
            case "Medium":
                code = itemCodeM;
                break;
            case "Small":
                code = itemCodeS;
                break;
            default:
                code = null;
        }
        return code;
    }

    public void setLongItemCode(String longCode){
        itemCodeL = longCode;
    }
    
    public String getLongItemCode(){
        return itemCodeL;
    }
    
    public void setMediumItemCode(String mediumCode){
        itemCodeM = mediumCode;
    }
    
    public String getMediumItemCode(){
        return itemCodeM;
    }
    
    public void setShortItemCode(String smallCode){
        itemCodeS = smallCode;
    }

    public String getShortItemCode(){
        return itemCodeS;
}
    
    public StringProperty itemDescriptionProperty() {
        if (itemDescription == null) {
            itemDescription = new SimpleStringProperty(this, "itemDescription", getDescription());
        }
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        itemDescriptionProperty().set(itemDescription);
    }

    public String getItemDescription() {
        return getDescription();
    }
    
    private String getDescription(){
        String description;
        switch(PROP.getProperty("item_description")){
            case "Detailed":
                description = itemDescriptionL;
                break;
            case "Short":
                description = itemDescriptionS;
                break;
            default:
                description = null;
        }
        return description;
    }

    public void setDetailedItemDescription(String detailedDescription){
        itemDescriptionL = detailedDescription;
    }
    
    public String getDetailedItemDescription(){
        return itemDescriptionL;
    }
    
    public void setShortItemDescription(String shortDescription){
        itemDescriptionS = shortDescription;
    }
    
    public String getShortItemDescription(){
        return itemDescriptionS;
    }
    
    public IntegerProperty quantityProperty() {
        if (quantity == null) {
            quantity = new SimpleIntegerProperty(this, "quantity");
        }
        return quantity;
    }

    public void setQuantity(int quantity) {
        quantityProperty().set(quantity);
    }

    public Integer getQuantity() {
        return quantityProperty().get();
    }

    public DoubleProperty mrpProperty() {
        if (mrp == null) {
            mrp = new SimpleDoubleProperty(this, "mrp");
        }
        return mrp;
    }

    public void setMrp(Double mrp) {
        mrpProperty().set(mrp);
    }

    public Double getMrp() {
        return mrpProperty().get();
    }

    public DoubleProperty nrpProperty() {
        if (nrp == null) {
            nrp = new SimpleDoubleProperty(this, "nrp");
        }
        return nrp;
    }

    public void setNrp(Double nrp) {
        nrpProperty().set(nrp);
    }

    public Double getNrp() {
        return nrpProperty().get();
    }

    public DoubleProperty discountProperty() {
        if (discount == null) {
            discount = new SimpleDoubleProperty(this, "discount");
        }
        return discount;
    }

    public void setDiscount(Double discount) {
        discountProperty().set(discount);
    }

    public Double getDiscount() {
        return discountProperty().get();
    }

    public DoubleProperty vatPercentageProperty() {
        if (vatPercentage == null) {
            vatPercentage = new SimpleDoubleProperty(this, "vatPercentage");
            System.out.println(vatPercentage.getValue());
        }
        return vatPercentage;
    }

    public void setVatPercentage(Double vatPercentage) {
        vatPercentageProperty().set(vatPercentage);
    }

    public Double getVatPercentage() {
        return vatPercentageProperty().get();
    }

    public DoubleProperty nrpWithoutVatProperty() {
        if (nrpWithoutVat == null) {
            nrpWithoutVat = new SimpleDoubleProperty(this, "nrpWithoutVat");
        }
        if(totalAmount.getValue()!=null)
            nrpWithoutVat.bind(Bindings.subtract(totalAmount, vatAmountProperty()));
        return nrpWithoutVat;
    }

    public void setNrpWithoutVat(Double nrpWithoutVat) {
        nrpWithoutVatProperty().set(nrpWithoutVat);
    }

    public Double getNrpWithoutVat() {
        return nrpWithoutVatProperty().get();
    }

    public DoubleProperty rdWithoutVatProperty() {
        if (rdWithoutVat == null) {
            rdWithoutVat = new SimpleDoubleProperty(this, "rdWithoutVat");
        }
        return rdWithoutVat;
    }

    public void setRdWithoutVat(Double rdWithoutVat) {
        rdWithoutVatProperty().set(rdWithoutVat);
    }

    public Double getRdWithoutVat() {
        return rdWithoutVatProperty().get();
    }

    public DoubleProperty vatAmountProperty() {
        if (vatAmount == null) {
            vatAmount = new SimpleDoubleProperty(this, "vatAmount");
        }
        vatAmount.bind(Bindings.multiply(vatPercentageProperty(), totalAmountProperty()).divide(100.0));
        return vatAmount;
    }

    public void setVatAmount(Double vatAmount) {
        vatAmountProperty().set(vatAmount);
    }

    public Double getVatAmount() {
        return vatAmountProperty().get();
    }

    public DoubleProperty rdIncludingVatProperty() {
        if (rdIncludingVat == null) {
            rdIncludingVat = new SimpleDoubleProperty(this, "rdIncludingVat");
        }
        return rdIncludingVat;
    }

    public void setRdIncludingVat(Double rdIncludingVat) {
        rdIncludingVatProperty().set(rdIncludingVat);
    }

    public Double getRdIncludingVat() {
        return rdIncludingVatProperty().get();
    }

    public DoubleProperty marginAmountProperty() {
        if (marginAmount == null) {
            marginAmount = new SimpleDoubleProperty(this, "marginAmount");
        }
        return marginAmount;
    }

    public void setMarginAmount(Double marginAmount) {
        marginAmountProperty().set(marginAmount);
    }

    public Double getMarginAmount() {
        return marginAmountProperty().get();
    }

    public DoubleProperty marginPercentageProperty() {
        if (marginPercentage == null) {
            marginPercentage = new SimpleDoubleProperty(this, "marginPercentage");
        }
        return marginPercentage;
    }

    public void setMarginPercentage(Double marginPercentage) {
        marginPercentageProperty().set(marginPercentage);
    }

    public Double getMarginPercentage() {
        return marginPercentageProperty().get();
    }

    public IntegerProperty imageProperty() {
        if (image == null) {
            image = new SimpleIntegerProperty(this, "image");
        }
        return image;
    }

    public void setImage(Integer image) {
        imageProperty().set(image);
    }

    public Integer getImage() {
        return imageProperty().get();
    }

    public IntegerProperty quotationIdProperty() {
        if (quotationId == null) {
            quotationId = new SimpleIntegerProperty(this, "quotationId");
        }
        return quotationId;
    }

    public void setQuotationId(Integer quotationId) {
        quotationIdProperty().set(quotationId);
    }

    public Integer getQuotationId() {
        return quotationIdProperty().get();
    }

    public IntegerProperty bathroomElementProperty() {
        if (bathroomElement == null) {
            bathroomElement = new SimpleIntegerProperty(this, "bathroomElement");
        }
        return bathroomElement;
    }

    public void setBathroomId(Integer bathroomId) {
        bathroomIdProperty().set(bathroomId);
    }

    public Integer getBathroomId() {
        return bathroomIdProperty().get();
    }

    public IntegerProperty bathroomIdProperty() {
        if (bathroomId == null) {
            bathroomId = new SimpleIntegerProperty(this, "bathroomId");
        }
        return bathroomId;
    }

    public void setBathroomElement(Integer bathroomElement) {
        bathroomElementProperty().set(bathroomElement);
    }

    public Integer getBathroomElement() {
        return bathroomElementProperty().get();
    }

    public DoubleProperty totalAmountProperty() {
        if (totalAmount == null) {
            totalAmount = new SimpleDoubleProperty(this, "totalAmount");
        }
        totalAmount.bind(Bindings.multiply(quantityProperty(), nrpProperty()));
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        totalAmountProperty().set(totalAmount);
    }

    public Double getTotalAmount() {
        return totalAmountProperty().get();
    }

    public boolean isLabel() {
        return label;
    }

    public void setLabel(boolean label) {
        this.label = label;
    }

    public void setPercentage() {
        setVatPercentage(Double.valueOf(PROP.getProperty("vat_percentage")));
    }
}