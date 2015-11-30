package quotationsoftware.controller;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import quotationsoftware.model.Quotation;
import quotationsoftware.dao.QuotationDAO;
import javafx.scene.control.TitledPane;
import quotationsoftware.util.Keys;

/**
 * Controller class for new quotation screen.
 */
public class NewQuote implements Initializable {

    @FXML
    private ToggleGroup headingGroup;
    @FXML
    private ToggleGroup vatGroup;
    @FXML
    private ToggleGroup displayGroup;
    @FXML
    private TextField customerName;
    @FXML
    private TextField customerAddress;
    @FXML
    private TextField customerEmailID;
    @FXML
    private TextField customerContactNumber;
    @FXML
    private TextField enquiryReference;
    @FXML
    private TextField reference;
    @FXML
    private DatePicker enquiryDate;
    @FXML
    private TitledPane vat;
    @FXML
    private ChoiceBox<String> quoteFormat;
    @FXML
    private RadioButton mrd;
    @FXML
    private Button btnNext;

    private final QuotationDAO quotationDAO = QuotationDAO.getInstance();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        //enterSampleData();
        setFormData();

        vat.disableProperty().bind(mrd.selectedProperty().not());
        btnNext.disableProperty().bind(Bindings.or(
                customerName.textProperty().isEmpty(),
                customerAddress.textProperty().isEmpty()));
        
        quoteFormat.getItems().addAll(Keys.QUOTE_FORMAT);
    }

    /**
     * Handles action on next button. Opens either item or bathroom wise quote
     * depending on what's selected.
     */
    @FXML
    private void next() {
        quotationDAO.setQuotation(getFormData());
        if (quotationDAO.getQuotation().getId() != 0) {
            try {
                quotationDAO.copy();
                btnNext.getScene().getWindow().hide();
                return;
            } catch (Exception e) {
                new Alert(AlertType.ERROR, e.getMessage()).showAndWait();
            }
        }
        int index = quoteFormat.getSelectionModel().getSelectedIndex();
        String view;
        switch(index){
            case 0:
                view = Keys.ITEM_WISE_QUOTE;
                break;
            case 1:
                view = Keys.BATHROM_WISE_SUMMARY;
                break;
            case 2:
                view = Keys.COMPARATIVE_BATHROOM_QUOTE;
                break;
        }

        try {
//            Pane nextScreen = FXMLLoader.load(getClass().getClassLoader().getResource(view));
//            ((BorderPane) rootPane.getParent()).setCenter(nextScreen);
            System.out.println("new window will be open");
        } catch (Exception e) {
            new Alert(AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    private Quotation getFormData() {
        Quotation quotation = new Quotation();
        if (quotationDAO.getQuotation() != null) {
            quotation = quotationDAO.getQuotation();
        }
        quotation.setCustomerName(customerName.getText());
        quotation.setCustomerAddress(customerAddress.getText());
        quotation.setContactNumber(customerContactNumber.getText());
        quotation.setEmailId(customerEmailID.getText());
        quotation.setEnquiryReferenceNo(enquiryReference.getText());
        LocalDate ld = enquiryDate.getValue();
        Date date = ld==null? Date.valueOf(LocalDate.now()):Date.valueOf(ld);
        quotation.setDate(date);
        quotation.setReferenceRemark(reference.getText());
        quotation.setQuoteFormat(quoteFormat.getSelectionModel().getSelectedItem());
        quotation.setHeadings(((RadioButton) headingGroup.getSelectedToggle()).getText());
        quotation.setVat(((RadioButton) vatGroup.getSelectedToggle()).getText());
        quotation.setDisplay(((RadioButton) displayGroup.getSelectedToggle()).getText());
        return quotation;
    }

    private void setFormData() {
        if (quotationDAO.getQuotation() == null) {
            return;
        }

        Quotation quotation = quotationDAO.getQuotation();
        customerName.setText(quotation.getCustomerName());
        customerAddress.setText(quotation.getCustomerAddress());
        customerContactNumber.setText(quotation.getContactNumber());
        customerEmailID.setText(quotation.getEmailId());
        enquiryReference.setText(quotation.getEnquiryReferenceNo());
        final Date date = quotation.getEnquiryDate();
        if ( date != null) {
            enquiryDate.setValue(date.toLocalDate());
        }
        reference.setText(quotation.getReferenceRemark());
        quoteFormat.getSelectionModel().select(quotation.getQuoteFormat());
        for (Toggle toggle : headingGroup.getToggles()) {
            if (((RadioButton) toggle).getText().equals(quotation.getHeadings())) {
                ((RadioButton) toggle).setSelected(true);
            }
        }
        for (Toggle toggle : vatGroup.getToggles()) {
            if (((RadioButton) toggle).getText().equals(quotation.getVat())) {
                ((RadioButton) toggle).setSelected(true);
            }
        }
        for (Toggle toggle : displayGroup.getToggles()) {
            if (((RadioButton) toggle).getText().equals(quotation.getDisplay())) {
                ((RadioButton) toggle).setSelected(true);
            }
        }
    }
}
