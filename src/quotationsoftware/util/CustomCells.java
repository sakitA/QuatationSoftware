/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quotationsoftware.util;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import quotationsoftware.dao.ExcelDAO;
import quotationsoftware.model.Item;
import static quotationsoftware.QuatationSoftware.PROP;

/**
 *
 * @author sakit
 */
public class CustomCells {

    private final static ResourceBundle RB = ResourceBundle.getBundle("resources.bundles.Bundle", Locale.getDefault());

    public static TableCell<Item, Integer> numberCellId() {
        return new TableCell<Item, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText("");
                if (empty) {
                    setGraphic(null);
                } else if (item != 0) {
                    int id = getIndex() + 1;
                    setText(String.valueOf(id));
                    ((Item) getTableView().getItems().get(getIndex())).setSerialNo(id);
                }
            }
        };
    }

    public static TableCell<Item, String> codeListCell() {
        return new TableCell<Item, String>() {
            private ComboBox<String> codes;
            private String current;
            private boolean cancel = false, focus = true;

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(current);
                cancel = false;
                setGraphic(null);
            }

            @Override
            public void startEdit() {
                current = getText();
                if (!isEmpty()) {
                    super.startEdit(); //To change body of generated methods, choose Tools | Templates.
                    createComboBox();
                    setText(null);
                    setGraphic(codes);
                }
            }

            @Override
            public void commitEdit(String newValue) {
                System.out.println("newv:" + newValue + ", curv:" + current);
                super.commitEdit(newValue);
                focus = true;
                if (newValue != null && !newValue.isEmpty() && !newValue.equals(current)) {
                    System.err.println("in commit");
                    Platform.runLater(() -> {
                        Item item = ExcelDAO.getInstance().getItem(newValue);
                        item.setQuantity(1);
                        item.setPercentage();
                        item.setItemCode(newValue);
                        if (getIndex() < getTableView().getItems().size()) {
                            getTableView().getItems().set(getIndex(), item);
                            getTableView().getItems().add(new Item());
                        }
                    });
                }
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else if (isEditing()) {
                    if (codes != null) {
                        codes.setValue(getString());
                    }
                    setText(null);
                    setGraphic(codes);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }

            private void createComboBox() {
                codes = new ComboBox<>(FXCollections.observableArrayList(ExcelDAO.getInstance().getListCodes()));
                codes.setEditable(true);
                codes.setPromptText(RB.getString(Keys.CMB_TXT));
                codes.setValue(current);
                codes.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
                FxUtil.autoCompleteComboBox(codes, FxUtil.AutoCompleteMode.STARTS_WITH);
                codes.focusedProperty().addListener((obs, oldv, newv) -> {
                    if (!newv && focus && !cancel) {
                        commitEdit(getSelectedCode());
                    }
                });
                codes.setOnKeyPressed(e -> {
                    switch (e.getCode()) {
                        case TAB:
                        case ENTER:
                            focus = false;
                            commitEdit(getSelectedCode());
                            break;
                        case ESCAPE:
                            cancel = true;
                            cancelEdit();
                    }
                });
            }

            private String getSelectedCode() {
                int index = codes.getSelectionModel().getSelectedIndex();
                if (index < 0) {
                    return current;
                } else {
                    return codes.getSelectionModel().getSelectedItem();
                }
            }

            private String getString() {
                return getItem() == null ? "" : getItem();
            }
        };
    }

    public static TableCell<Item, Integer> quantityCell() {
        return new TableCell<Item, Integer>() {
            private TextField quantityField;
            private String current;
            private boolean cancel = false;
            private int quantity = 1;

            @Override
            public void startEdit() {
                current = getText();
                if (!isEmpty()) {
                    super.startEdit();
                    createTextField();
                    setGraphic(quantityField);
                    setText(null);
                    quantityField.selectAll();
                }
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(current);
                cancel = false;
                setGraphic(null);
            }

            @Override
            public void commitEdit(Integer newValue) {
                super.commitEdit(newValue);
                ((Item) getTableView().getItems().get(getIndex())).setQuantity(newValue);
            }

            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else if (isEditing()) {
                    if (quantityField != null) {
                        quantityField.setText(getString());
                    }
                    setText(null);
                    setGraphic(quantityField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }

            private void createTextField() {
                quantityField = new TextField("1");
                quantityField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
                quantityField.setMinHeight(this.getHeight());
                if (current == null || current.equals("")) {
                    quantityField.setText(Integer.toString(1));
                } else {
                    quantityField.setText(current);
                }

                quantityField.focusedProperty().addListener((obs, oldValue, newValue) -> {
                    if (!newValue && !cancel) {
                        commitEdit(getInteger());
                    }
                });

                quantityField.setOnKeyPressed(e -> {
                    switch (e.getCode()) {
                        case UP:
                            quantityField.setText(String.valueOf(getInteger() + 1));
                            break;
                        case DOWN:
                            int q = getInteger();
                            quantityField.setText(String.valueOf(q == 1 ? q : q - 1));
                            break;
                        case TAB:
                        case ENTER:
                            commitEdit(getInteger());
                            break;
                        case ESCAPE:
                            cancel = true;
                            cancelEdit();
                    }
                });
                quantityField.setOnKeyTyped(e -> {
                    if (!e.getCode().isLetterKey() && e.getText().matches("[0-9]")) {
                        System.out.println("it is number");
                        quantityField.setText(quantityField.getText() + e.getText());
                    }
                });
            }

            private Integer getInteger() {
                if (quantityField != null) {
                    quantity = Integer.valueOf(quantityField.getText());
                }
                return quantity == 1 ? 1 : quantity;
            }

            private String getString() {
                if (isEmpty() || getItem() == null || getItem() == 0) {
                    return "";
                }
                return getItem().toString();
            }
        };
    }

    public static TableCell imageCell() {
        return new TableCell() {
            final Button delete = new Button();
            final ImageView delImage = new ImageView(
                    new Image(getClass().getResourceAsStream("/resources/images/delete-small.png")));

            {
                delete.setGraphic(delImage);
                delete.setId("delete");
                delete.setOnAction(e -> {
                    Item currentItem = (Item) this.
                            getTableView().getItems().get(this.getIndex());
                    //remove selected item from the table list
                    getTableView().getItems().remove(currentItem);
                });
            }

            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else if (item != null && (int) item > 0) {
                    setGraphic(delete);
                } else {
                    setGraphic(null);
                }
            }
        };
    }

    public static TableCell productImageCell() {
        return new TableCell() {
            ImageView productImage;

            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                setText("");
                if (item != null && (int) item >= 0) {
                    productImage = new ImageView(
                            ExcelDAO.getInstance().getImage((int) item));
                    setImageSize();
                    setGraphic(productImage);
                } else {
                    setGraphic(null);
                }
            }

            private void setImageSize() {
                switch (PROP.getProperty("product_image")) {
                    case "Large":
                        productImage.setFitHeight(256);
                        productImage.setFitWidth(256);
                        break;
                    case "Medium":
                        productImage.setFitHeight(128);
                        productImage.setFitWidth(128);
                        break;
                    default:
                        productImage.setFitHeight(64);
                        productImage.setFitWidth(64);
                        break;
                }
            }
        };
    }

    public static TableCell amountCell() {
        return new TableCell() {
            @Override
            public void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                setText("");
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
                if (item != null) {
                    if (getDouble() != 0) {
                        setText(currencyFormat.format(getDouble()).replace("$", ""));
                    } else {
                        setGraphic(null);
                    }
                }
            }

            private Double getDouble() {
                return getItem() == null ? 0d : (double) getItem();
            }

        };
    }

    public static TableCell<Item, Double> percentageCell() {
        return new TableCell<Item, Double>() {
            @Override
            public void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText("");
                if (item != null) {
                    if (getDouble() != 0) {
                        setText(getDouble()+" %");
                    } else {
                        setGraphic(null);
                    }
                }
            }

            private Double getDouble() {
                return getItem() == null ? 0d : (double) getItem();
            }
        };
    }
}
