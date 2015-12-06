package quotationsoftware.dao;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import quotationsoftware.model.Item;

/**
 * Singleton data access class which handles Excel operations.
 */
public class ExcelDAO {

    private static final ExcelDAO instance = new ExcelDAO();
    private Item item;
    private final List<Item> listItem = new ArrayList();
    private final List<Image> listImage = new ArrayList<>();

    /**
     * Constructor which reads all the items in Database excel file into a list.
     * This is done only once when application is started.
     */
    private ExcelDAO() {
        Workbook workbook = getWorkbook();
        Sheet sheet = workbook.getSheet("Database");
        for (int i = 1; i < sheet.getRows(); i++) {
            Item it = new Item();
            it.setSerialNo(Integer.valueOf(sheet.getCell(0, i).getContents()));
            it.setLongItemCode(sheet.getCell(1, i).getContents());
            it.setMediumItemCode(sheet.getCell(2, i).getContents());
            it.setShortItemCode(sheet.getCell(3, i).getContents());
            it.setShortItemDescription(sheet.getCell(4, i).getContents());
            it.setDetailedItemDescription(sheet.getCell(5, i).getContents());
            it.setMrp(getDouble(sheet.getCell(6, i)));
            it.setNrp(getDouble(sheet.getCell(7, i)));
            it.setDiscount(getDouble(sheet.getCell(8, i)));
            //it.setNrpWithoutVat(getDouble(sheet.getCell(9, i)));
            it.setRdWithoutVat(getDouble(sheet.getCell(10, i)));
            it.setRdIncludingVat(getDouble(sheet.getCell(11, i)));
            it.setMarginAmount(getDouble(sheet.getCell(12, i)));
            it.setMarginPercentage(getDouble(sheet.getCell(13, i)));
            it.setImage(i - 1); //saving only the index of the image on sheet
            listItem.add(it);
            
            listImage.add(new Image(new ByteArrayInputStream(sheet.getDrawing(i - 1).getImageData())));
        }

        workbook.close();
    }

    public static ExcelDAO getInstance() {
        return instance;
    }

    private Workbook getWorkbook() {
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(getClass().getResourceAsStream("/resources/excel/database.xls"));
        } catch (IOException | BiffException e) {
            new Alert(AlertType.ERROR, "Error reading Excel file: " + e.getMessage()).showAndWait();
        }
        return workbook;
    }

    private Double getDouble(Cell cell) {
        return new Double(cell.getContents().replace(",", "."));
    }

    /**
     * Returns an image object of the item with the specified index.
     * @param itemIndex
     * @return 
     */
    public Image getImage(int itemIndex) {
//        Workbook workbook = getWorkbook();
//        Sheet sheet = workbook.getSheet("Database");
//        InputStream inputStream = new ByteArrayInputStream(sheet.getDrawing(itemIndex).getImageData());
//        Image image = new Image(inputStream);
//        workbook.close();
        return listImage.get(itemIndex);
    }

    public List<Item> getListItem() {
        return listItem;
    }

    public Item getItem(String itemCode){
        Item itm = null;
        for(Item it:listItem)
            if(it.getItemCode().equals(itemCode)){
                itm = it;
                break;
            }
        return itm;
    }
    /**
     * Returns list of item codes for autocomplete-comboboxes which appear during
     * item editing inside table views.
     * Returned code are small, medium or large depending on the settings.
     * @return 
     */
    public List<String> getListCodes() {
        List<String> listCode = new ArrayList<>();
        listItem.stream().forEach((it) -> {
            listCode.add(it.getItemCode());
        });
        return listCode;
    }
    
    /**
     * Return the list of images.
     * @return 
     */
    public List<Image> getListImage() {
        return listImage;
    }
}