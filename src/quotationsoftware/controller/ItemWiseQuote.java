/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quotationsoftware.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
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

    private ResourceBundle rb;
    private final QuotationDAO qdao = QuotationDAO.getInstance();
    private final ItemDAO idao = ItemDAO.getInstance();
    private final Quotation quota = qdao.getQuotation();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rb = resources;
        initQuatationInfo();
        initTable();
        tableView.setPlaceholder(new Text(rb.getString("empty")));
        tableView.getSelectionModel().setCellSelectionEnabled(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        fillTable();
        switch (quota.getHeadings()) {
            case "MRP":
                hideColumn(nrp, discount, vat, aev, vta);
                break;
            case "NRP":
                hideColumn(mrp, discount, vat, aev, vta);
                break;
            case "MRP With NRP & Discounts":
                hideColumn(vat, aev, vta);
                break;
            case "MRP With Discounts":
                if (quota.getVat().equals("VAT in total")) {
                    hideColumn(nrp, vta, total);
                } else {
                    hideColumn();
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
}
