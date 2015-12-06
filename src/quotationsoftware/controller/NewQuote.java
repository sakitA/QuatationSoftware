package quotationsoftware.controller;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import quotationsoftware.model.Quotation;
import quotationsoftware.dao.QuotationDAO;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import quotationsoftware.dao.ExcelDAO;
import quotationsoftware.util.Keys;
import quotationsoftware.util.UndecoratedWindow;

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
    @FXML
    private BorderPane borderPane;
    @FXML
    private ProgressIndicator prgInd;
    @FXML
    private Label prgLbl;
    @FXML
    private Region region;

    private final QuotationDAO quotationDAO = QuotationDAO.getInstance();
    private Service service;
    private ResourceBundle rb;
    private static boolean oncestart = false;
    private String view;
    private Stage primaryStage;
    private Stage childStage;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        enterSampleData();
        rb = arg1;
        setFormData();

        vat.disableProperty().bind(mrd.selectedProperty().not());
        btnNext.disableProperty().bind(Bindings.or(Bindings.or(
                customerName.textProperty().isEmpty(),
                customerAddress.textProperty().isEmpty()),
                quoteFormat.getSelectionModel().selectedIndexProperty().isEqualTo(-1)));

        quoteFormat.getItems().addAll(Keys.QUOTE_FORMAT);

        service = new Service() {

            @Override
            protected Task createTask() {
                return myTask();
            }
        };
        prgInd.visibleProperty().bind(service.runningProperty());
        prgLbl.visibleProperty().bind(service.runningProperty());
        region.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");
        region.visibleProperty().bind(service.runningProperty());

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
            } catch (Exception e) {
                new Alert(AlertType.ERROR, e.getMessage()).showAndWait();
            }
        } else {
            int index = quoteFormat.getSelectionModel().getSelectedIndex();

            switch (index) {
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
        }
        
        try {
            primaryStage = (Stage) btnNext.getScene().getWindow();
            childStage = new Stage();
            if (oncestart) {
                service.restart();
            } else {
                oncestart = true;
                service.start();
            }
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
        Date date = ld == null ? Date.valueOf(LocalDate.now()) : Date.valueOf(ld);
        quotation.setEnquiryDate(date);
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
        if (date != null) {
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

    private void enterSampleData() {
        customerName.setText("John F. Smith");
        customerAddress.setText("Washington Street 20b");
        customerContactNumber.setText("0500/111-222");
        customerEmailID.setText("john.smith@gmail.com");
        enquiryReference.setText("#JHOOIJ10001");
        reference.setText("Nothing to remark.");
        enquiryDate.setValue(LocalDate.now());
    }

    private Task myTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                ExcelDAO.getInstance();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(view), rb);
                Scene scene = new Scene(loader.load());
                Platform.runLater(() -> {
                    childStage.setScene(scene);
                    childStage.setTitle(view.substring(view.lastIndexOf("/") + 1, view.lastIndexOf(".")));
                    childStage.initOwner(primaryStage);
                    childStage.initModality(Modality.WINDOW_MODAL);
                    childStage.initStyle(StageStyle.UNDECORATED);
                    childStage.setMaximized(true);
                    new UndecoratedWindow(childStage, scene);
                });
                return true;
            }

            @Override
            protected void succeeded() {
                super.succeeded(); //To change body of generated methods, choose Tools | Templates.
                primaryStage.hide();
                childStage.setOnCloseRequest(e -> primaryStage.show());
                childStage.show();
            }
        };
    }
}
