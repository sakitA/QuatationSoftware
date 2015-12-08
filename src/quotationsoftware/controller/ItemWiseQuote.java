/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quotationsoftware.controller;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import quotationsoftware.dao.QuotationDAO;
import quotationsoftware.model.Item;
import quotationsoftware.model.Quotation;
import quotationsoftware.util.CustomCells;
import quotationsoftware.util.Formats;
import quotationsoftware.dao.ItemDAO;
import quotationsoftware.util.DbHandler;
import quotationsoftware.util.Notifications;
import static quotationsoftware.QuatationSoftware.PROP;

/**
 *
 * @author sakit
 */
public class ItemWiseQuote implements Initializable {

    @FXML
    private Label customerName;
    @FXML
    private Label customerAddress;
    @FXML
    private Label customerContact;
    @FXML
    private Label enquiryDate;
    @FXML
    private Label quotationNumber;
    @FXML
    private Label shopCity;
    @FXML
    private Label shopName;
    @FXML
    private Label shopAddress;
    @FXML
    private Text textMessage;
    @FXML
    private Label lblTotal, lblQuantity, lblMrp, lblNrp, lblDiscount, lblAev, lblVta, lblTa, empty;
    @FXML
    private TableColumn<Item, Integer> itemNumber;
    @FXML
    private TableColumn<Item, String> itemCode;
    @FXML
    private TableColumn<Item, String> itemDescription;
    @FXML
    private TableColumn<Item, Integer> itemQuantity;
    @FXML
    private TableColumn<Item, Double> mrp;
    @FXML
    private TableColumn<Item, Double> discount;
    @FXML
    private TableColumn<Item, Double> nrp;
    @FXML
    private TableColumn<Item, Double> vat;
    @FXML
    private TableColumn<Item, Double> aev;
    @FXML
    private TableColumn<Item, Double> vta;
    @FXML
    private TableColumn<Item, Double> total;
    @FXML
    private TableColumn<Item, Integer> image;
    @FXML
    private TableColumn<Item, Integer> delImage;
    @FXML
    private TableView<Item> tableView;
    @FXML
    private HBox hbox; 

    private ResourceBundle rb;
    private final QuotationDAO qdao = QuotationDAO.getInstance();
    private final ItemDAO idao = ItemDAO.getInstance();
    private final Quotation quota = qdao.getQuotation();
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rb = resources;
        initQuatationInfo();
        initTable();
        bindLabelWidthProperty();
        tableView.getItems().clear();
        tableView.setPlaceholder(new Text(rb.getString("empty")));
        tableView.getSelectionModel().setCellSelectionEnabled(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableView.getItems().addListener((ListChangeListener.Change<? extends Item> c) -> {
            doCalculations();
        });
        
        fillTable();
        switch (quota.getHeadings()) {
            case "MRP":
                hideColumn(nrp, discount, vat, aev, vta);
                showLabel(lblTotal, lblQuantity, lblMrp, lblTa);
                break;
            case "NRP":
                hideColumn(mrp, discount, vat, aev, vta);
                showLabel(lblTotal, lblQuantity, lblNrp, lblTa);
                break;
            case "MRP With NRP & Discounts":
                hideColumn(vat, aev, vta);
                showLabel(lblTotal, lblQuantity, lblMrp, lblDiscount, lblNrp, lblTa);
                break;
            case "MRP With Discounts":
                if (quota.getVat().equals("VAT in total")) {
                    hideColumn(nrp, vat, vta, total);
                    showLabel(lblTotal, lblQuantity, lblMrp, lblDiscount, lblAev);
                    
                } else {
                    hideColumn();
                    showLabel();
                }
        }
        
    }
    
    private void initQuatationInfo() {
        textMessage.setText(String.format(
                rb.getString("cl_msg"),
                quota.getEnquiryReferenceNo(),
                Formats.toDateFormat(quota.getEnquiryDate())));
        customerName.textProperty().bind(quota.customerNameProperty());
        customerAddress.textProperty().bind(quota.customerAddressProperty());
        customerContact.textProperty().bind(Bindings.concat(quota.contactNumberProperty(), "/", quota.emailIdProperty()));
        enquiryDate.setText(Formats.toDateFormat(quota.getEnquiryDate()));
        quotationNumber.setText(Integer.toString(DbHandler.getNextId("quotations")));
        shopName.setText(PROP.getProperty("company_name"));
        shopAddress.setText(PROP.getProperty("company_address"));
        shopCity.setText(PROP.getProperty("company_address"));
    }

    @FXML
    private void back(ActionEvent e) {
        Node node = (Node) e.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        hbox.getChildren().clear();
        stage.getOnCloseRequest().handle(null);
        stage.close();
    }

    private void hideColumn(TableColumn<Item, ?>... columns) {
        image.setVisible(quota.getDisplay().equals("Display with image"));
        final ObservableList<TableColumn<Item, ?>> list = tableView.getColumns();
        if (columns.length != 0) {
            int index = 0;
            for (int i = 0; i < columns.length; i++) {
                index = list.indexOf(columns[i]);
                list.get(index).setVisible(false);                
            }
        } else {
            for (int i = 0, len = list.size(); i < len; i++) {
                list.get(i).setVisible(true);
            }
        }
    }
    
    private void showLabel(Label... labels){
        hbox.getChildren().clear();
        
        if(labels.length==0)
            hbox.getChildren().addAll(lblTotal, lblQuantity, lblMrp, lblDiscount, lblNrp, empty, lblAev, lblVta, lblTa);
        else
            hbox.getChildren().addAll(labels);
    }

    private void initTable() {
        itemNumber.setCellValueFactory(new PropertyValueFactory<>("serialNo"));
        itemNumber.setCellFactory(param -> {
            return CustomCells.numberCellId();
        });

        itemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        itemCode.setCellFactory(param -> {
            return CustomCells.codeListCell();
        });

        itemDescription.setCellValueFactory(new PropertyValueFactory<>("itemDescription"));

        itemQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        itemQuantity.setCellFactory(param -> {
            return CustomCells.quantityCell();
        });
        itemQuantity.setOnEditCommit(t->{
            int index = t.getTablePosition().getRow();
            Item item = t.getTableView().getItems().get(index);
            item.setQuantity(t.getNewValue());
            doCalculations();
        });
        

        mrp.setCellValueFactory(new PropertyValueFactory<>("mrp"));
        mrp.setCellFactory(param -> {
            return CustomCells.amountCell();
        });

        nrp.setCellValueFactory(new PropertyValueFactory<>("nrp"));
        nrp.setCellFactory(param -> {
            return CustomCells.amountCell();
        });

        discount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        discount.setCellFactory(param -> {
            return CustomCells.percentageCell();
        });

        vat.setCellValueFactory(new PropertyValueFactory<>("vatPercentage"));
        vat.setCellFactory(param -> {
            return CustomCells.percentageCell();
        });

        aev.setCellValueFactory(new PropertyValueFactory<>("nrpWithoutVat"));
        aev.setCellFactory(param -> {
            return CustomCells.amountCell();
        });

        vta.setCellValueFactory(new PropertyValueFactory<>("vatAmount"));
        vta.setCellFactory(param -> {
            return CustomCells.amountCell();
        });

        total.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        total.setCellFactory(param -> {
            return CustomCells.amountCell();
        });

        image.setCellValueFactory(new PropertyValueFactory<>("image"));
        image.setCellFactory(param -> {
            return CustomCells.productImageCell();
        });

        //add delete image to each row if new row not empty
        delImage.setCellValueFactory(new PropertyValueFactory<>("serialNo"));

        //handle button action for deleteing the row
        delImage.setCellFactory(param -> {
            return CustomCells.imageCell();
        });
    }

    @FXML
    private void save() {
        if (tableView.getItems().isEmpty()) {
            new Alert(AlertType.WARNING, "Quotation cannot be empty. Please add items to it.").showAndWait();
            return;
        }
        try {
            int quotationId = qdao.getQuotation().getId();
            if (quotationId == 0) {
                quotationId = qdao.save();
            } else {
                idao.delete(quotationId, -1);
            }

            List<Item> listSave = new ArrayList<>();
            tableView.getItems().stream().filter((item) -> (item.getSerialNo() != 0)).forEach((item) -> {
                listSave.add(item);
            });

            idao.save(listSave, quotationId, -1);
            qdao.getQuotation().setId(quotationId);

            Notifications.showNotification(tableView.getScene().getWindow(), "Quotation saved successfully");
        } catch (Exception e) {
            new Alert(AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void fillTable() {
        if (quota.getId() != 0) {
            try {
                tableView.getItems().clear();
                tableView.setItems(FXCollections.observableArrayList(idao.getListItem(quota.getId(), -1)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            tableView.getItems().add(new Item());
        }
    }

    private void bindLabelWidthProperty() {
        lblTotal.prefWidthProperty().bind(Bindings.
                        add(itemNumber.widthProperty(), itemCode.widthProperty()).
                        add(itemDescription.widthProperty()));
        lblQuantity.prefWidthProperty().bind(itemQuantity.widthProperty());
        lblMrp.prefWidthProperty().bind(mrp.widthProperty());
        lblDiscount.prefWidthProperty().bind(discount.widthProperty());
        lblNrp.prefWidthProperty().bind(nrp.widthProperty());
        lblAev.prefWidthProperty().bind(aev.widthProperty());
        lblVta.prefWidthProperty().bind(vta.widthProperty());
        lblTa.prefWidthProperty().bind(total.widthProperty());
        empty.prefWidthProperty().bind(vat.widthProperty());
    }
    
    private void doCalculations(){
        System.out.println("in calc");
        List<Item> items = tableView.getItems();
        NumberFormat cf = NumberFormat.getCurrencyInstance(Locale.US);
        int tQuantity = 0;
        double tmrp=0, tnrp=0, tdiscount=0, taev=0, tta=0, tvta=0;
        for(Item it:items){
            tQuantity +=it.getQuantity();
            tmrp += it.getMrp();
            tnrp += it.getNrp();
            tdiscount += it.getDiscount();
            taev += it.getNrpWithoutVat();
            tta += it.getTotalAmount();
            tvta += it.getVatAmount();
        }
        
        lblQuantity.setText(cf.format(tQuantity).replaceAll("$", ""));
        lblMrp.setText(cf.format(tmrp).replaceAll("$", ""));
        lblNrp.setText(cf.format(tnrp).replaceAll("$", ""));
        lblDiscount.setText(cf.format(tdiscount).replaceAll("$", ""));
        lblAev.setText(cf.format(taev).replaceAll("$", ""));
        lblTa.setText(cf.format(tta).replaceAll("$", ""));
        lblVta.setText(cf.format(tvta).replaceAll("$", ""));       
        if(quota.getVat().equals("VAT in total")){
            lblTotal.setText(rb.getString("iwq_total")+"\n"
                    +String.format(rb.getString("iwq_outvat"), PROP.get("vat_percentage")+"%="
                    +"\n"+rb.getString("iwq_taiv")));
            lblAev.setText(cf.format(taev).replaceAll("$", "")
                    +"\n"+cf.format(tvta).replaceAll("$", "")
                    +"\n"+cf.format(tta).replaceAll("$", ""));
        }
    }
}
