package quotationsoftware.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

/**
 * Utility class to implement custom behaviour on fx ui elements.
 */
public class FxUtil {

    public enum AutoCompleteMode {
        STARTS_WITH, 
        CONTAINING;
    }

    /**
     * Specifies the auto complete feature on a specified combobox.
     * @param <T>
     * @param comboBox
     * @param mode
     */
    public static <T> void autoCompleteComboBox(ComboBox<T> comboBox, AutoCompleteMode mode) {
        ObservableList<T> data = comboBox.getItems();

        comboBox.setEditable(true);
        comboBox.addEventHandler(KeyEvent.KEY_PRESSED, t -> comboBox.hide());
        comboBox.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {

            private boolean moveCaretToPos = false;
            private int caretPos;

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.UP) {
                    caretPos = -1;
                    moveCaret(comboBox.getEditor().getText().length());
                    return;
                } else if (event.getCode() == KeyCode.DOWN) {
                    if (!comboBox.isShowing()) {
                        comboBox.show();
                    }
                    caretPos = -1;
                    moveCaret(comboBox.getEditor().getText().length());
                    return;
                } else if (event.getCode() == KeyCode.BACK_SPACE) {
                    moveCaretToPos = true;
                    caretPos = comboBox.getEditor().getCaretPosition();
                } else if (event.getCode() == KeyCode.DELETE) {
                    moveCaretToPos = true;
                    caretPos = comboBox.getEditor().getCaretPosition();
                }

                if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT
                        || event.isControlDown() || event.getCode() == KeyCode.HOME
                        || event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB) {
                    return;
                }

                ObservableList<T> list = FXCollections.observableArrayList();
                data.stream().forEach((aData) -> {
                    if (mode.equals(AutoCompleteMode.STARTS_WITH) && aData.toString().toLowerCase().startsWith(comboBox.getEditor().getText().toLowerCase())) {
                        list.add(aData);
                    } else if (mode.equals(AutoCompleteMode.CONTAINING) && aData.toString().toLowerCase().contains(comboBox.getEditor().getText().toLowerCase())) {
                        list.add(aData);
                    }
                });
                String t = comboBox.getEditor().getText();

                comboBox.setItems(list);
                comboBox.getEditor().setText(t);
                if (!moveCaretToPos) {
                    caretPos = -1;
                }
                moveCaret(t.length());
                if (!list.isEmpty()) {
                    comboBox.show();
                }
            }

            private void moveCaret(int textLength) {
                if (caretPos == -1) {
                    comboBox.getEditor().positionCaret(textLength);
                } else {
                    comboBox.getEditor().positionCaret(caretPos);
                }
                moveCaretToPos = false;
            }
        });
    }

    /**
     * Convinient class which return null-safe combobox value.
     * @param <T>
     * @param comboBox
     * @return 
     */
    public static <T> T getComboBoxValue(ComboBox<T> comboBox) {
        if (comboBox.getSelectionModel().getSelectedIndex() < 0) {
            return null;
        } else {
            return comboBox.getItems().get(comboBox.getSelectionModel().getSelectedIndex());
        }
    }
    
    /**
     * Sets format on a date picker.
     * A format is defined in the Formats class.
     * @param datePicker
     */
    public static void setDatePickerFormat(DatePicker datePicker) {
        datePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Formats.pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
    }
    
    /**
     * Convinient method which forces table to start editing on keyboard key press.
     * @param tableView
     */
    public static void setEditTableOnKeyPress(TableView tableView) {
        tableView.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                return;
            } else if (event.getCode() == KeyCode.TAB && tableView.getEditingCell() == null) {
                event.consume();
                tableView.getSelectionModel().selectNext();
            }
            
            if (tableView.getEditingCell() == null) {
                if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
                    TablePosition focusedCellPosition = tableView.getFocusModel().getFocusedCell();
                    tableView.edit(focusedCellPosition.getRow(), focusedCellPosition.getTableColumn());
                }
            }
        });
    }
}
