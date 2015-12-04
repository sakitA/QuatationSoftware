package quotationsoftware.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class for quotation object with javafx specific properties.
 */
public class Quotation {

    private IntegerProperty id;
    private StringProperty customerName;
    private StringProperty customerAddress;
    private StringProperty emailId;
    private StringProperty contactNumber;
    private String enquiryReferenceNo;
    private Date enquiryDate;
    private String referenceRemark;
    private String quoteFormat;
    private String headings;
    private String vat;
    private String display;
    private Date quotationDate;
    private ObjectProperty<Date> date;
    private Integer bathroomId;
    private Integer bathroomElement; //active record
    private List<Item> listItem = new ArrayList<>();

    public Quotation() {

    }

    public Quotation(Quotation original) {
        this.id = new SimpleIntegerProperty(original.getId());
        this.customerName = new SimpleStringProperty(original.getCustomerName());
        this.customerAddress = new SimpleStringProperty(original.getCustomerAddress());
        this.date = new SimpleObjectProperty<>(original.getDate());
        this.emailId = new SimpleStringProperty(original.getEmailId());
        this.contactNumber = new SimpleStringProperty(original.getContactNumber());
        this.enquiryReferenceNo = original.getEnquiryReferenceNo();
        this.enquiryDate = original.getEnquiryDate();
        this.referenceRemark = original.getReferenceRemark();
        this.quoteFormat = original.getQuoteFormat();
        this.headings = original.getHeadings();
        this.vat = original.getVat();
        this.display = original.getDisplay();
        this.quotationDate = original.getQuotationDate();
        this.bathroomId = original.getBathroomId();
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

    public StringProperty customerNameProperty() {
        if (customerName == null) {
            customerName = new SimpleStringProperty(this, "customerName");
        }
        return customerName;
    }

    public void setCustomerName(String customerName) {
        customerNameProperty().set(customerName);
    }

    public String getCustomerName() {
        return customerNameProperty().get();
    }

    public ObjectProperty<Date> dateProperty() {
        if (date == null) {
            date = new SimpleObjectProperty<>(this, "date");
        }
        return date;
    }

    public void setDate(Date date) {
        dateProperty().set(date);
    }

    public Date getDate() {
        return dateProperty().get();
    }

    public StringProperty customerAddressProperty() {
        if (customerAddress == null) {
            customerAddress = new SimpleStringProperty(this, "customerAddress");
        }
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        customerAddressProperty().set(customerAddress);
    }

    public String getCustomerAddress() {
        return customerAddressProperty().get();
    }

    public StringProperty emailIdProperty() {
        if (emailId == null) {
            emailId = new SimpleStringProperty(this, "emailId");
        }
        return emailId;
    }

    public void setEmailId(String emailId) {
        emailIdProperty().set(emailId);
    }

    public String getEmailId() {
        return emailIdProperty().get();
    }

    public StringProperty contactNumberProperty(){
        if (contactNumber == null) {
            contactNumber = new SimpleStringProperty(this, "contactNumber");
        }
        return contactNumber;        
    }
    
    public void setContactNumber(String contactNumber){
        contactNumberProperty().set(contactNumber);
    }
    
    public String getContactNumber() {
        return contactNumberProperty().get();
    }

    public String getEnquiryReferenceNo() {
        return enquiryReferenceNo;
    }

    public void setEnquiryReferenceNo(String enquiryReferenceNo) {
        this.enquiryReferenceNo = enquiryReferenceNo;
    }

    public Date getEnquiryDate() {
        return enquiryDate;
    }

    public void setEnquiryDate(Date enquiryDate) {
        this.enquiryDate = enquiryDate;
    }

    public String getReferenceRemark() {
        return referenceRemark;
    }

    public void setReferenceRemark(String referenceRemark) {
        this.referenceRemark = referenceRemark;
    }

    public String getQuoteFormat() {
        return quoteFormat;
    }

    public void setQuoteFormat(String quoteFormat) {
        this.quoteFormat = quoteFormat;
    }

    public String getHeadings() {
        return headings;
    }

    public void setHeadings(String headings) {
        this.headings = headings;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public Date getQuotationDate() {
        return quotationDate;
    }

    public void setQuotationDate(Date quotationDate) {
        this.quotationDate = quotationDate;
    }

    public Integer getBathroomElement() {
        return bathroomElement;
    }

    public void setBathroomElement(Integer bathroomElement) {
        this.bathroomElement = bathroomElement;
    }

    public Integer getBathroomId() {
        return bathroomId;
    }

    public void setBathroomId(Integer bathroomId) {
        this.bathroomId = bathroomId;
    }

    @Override
    public String toString() {
        return "Quotation [customerName=" + customerName + ", customerAddress="
                + customerAddress + ", contactNumber=" + contactNumber
                + ", emailId=" + emailId + ", enquiryReferenceNo="
                + enquiryReferenceNo + ", enquiryDate=" + enquiryDate
                + ", referenceRemark=" + referenceRemark + ", quoteFormat="
                + quoteFormat + ", headings=" + headings + ", vat=" + vat
                + ", display=" + display + "]";
    }

    public List<Item> getListItem() {
        return listItem;
    }

    public void setListItem(List<Item> listItem) {
        this.listItem = listItem;
    }
}