package sample;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rendering.Render;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class Controller {
    @FXML
    private Canvas canvasDrawElement;

    @FXML
    private Pane panelParentDraw;

    @FXML
    private ToggleGroup toolsPaint;

    @FXML
    private Slider sliderStrokeWidth;
    @FXML
    private Label sliderStrokeWidthLabel;

    @FXML
    private ColorPicker colorPickerFill;
    @FXML
    private ColorPicker colorPickerStroke;
    @FXML
    private CheckBox isFill;

    private ToggleButton currentToggleButton;
    private double x1 = 0;
    private double y1 = 0;
    private boolean isPress = false;
    private int indexShape = 0;
    private GraphicsContext gc;
    private Image snapshot;
    private boolean isSave = false;


    private Render rend;

    @FXML
    public void initialize() {
        currentToggleButton = (ToggleButton)toolsPaint.getSelectedToggle();
        gc = canvasDrawElement.getGraphicsContext2D();
        snapshot = canvasDrawElement.snapshot(null, null);
        rend = Render.getInstanced();
        rend.Init(1, Color.BLACK, Color.BLUE);

        colorPickerFill.setValue(Color.BLUE);
        colorPickerStroke.setValue(Color.BLACK);
        isFill.setSelected(true);


        panelParentDraw.widthProperty().addListener((obs, oldVal, newVal) -> {
            canvasDrawElement.setWidth(panelParentDraw.getWidth());
            gc.drawImage(snapshot, 0, 0);
        });

        panelParentDraw.heightProperty().addListener((obs, oldVal, newVal) -> {
            canvasDrawElement.setHeight(panelParentDraw.getHeight());
            gc.drawImage(snapshot, 0, 0);
        });

        /*Circle c = new Circle();
        c.setCenterX(50);
        c.setCenterY(50);
        c.setRadius(20);
        panelParentDraw.getChildren().add(c);*/
    }

    public void mouseClickOnDrawPanel(MouseEvent mouseEvent) {
        isSave = true;
        snapshot = canvasDrawElement.snapshot(null, null);
        x1 = mouseEvent.getX();
        y1 = mouseEvent.getY();
        rend.setFillColor(isFill.isSelected() ? colorPickerFill.getValue() : Color.TRANSPARENT);
        rend.setStrokeColor(colorPickerStroke.getValue());
        switch(indexShape) {
            case -3: case -4:
                rend.startPath((int)x1, (int)y1, gc);
                break;

            case -2:
                rend.startPath(Color.WHITE, (int)x1, (int)y1, gc);
                break;

            case -1:
                rend.setFillColor(colorPickerFill.getValue());
                Image img = canvasDrawElement.snapshot(null, null);
                rend.floodFill((int) x1, (int) y1, img, (int)canvasDrawElement.getWidth(), (int)canvasDrawElement.getHeight());
                gc.drawImage(img, 0, 0);
                img = null;
                System.gc();
                break;
        }
        isPress = true;
    }

    public void mouseMoved(MouseEvent mouseEvent) {
        if(isPress) {
            double x2 = mouseEvent.getX();
            double y2 = mouseEvent.getY();
            gc.drawImage(snapshot, 0, 0);
            switch(indexShape) {
                case -2: case -3:
                    rend.addPointPath((int)x2, (int)y2, gc);
                    snapshot = canvasDrawElement.snapshot(null, null);
                    return;

                case -4:
                    rend.addPointPath((int)x2, (int)y2, gc);
                    rend.draw(2, (int)x1, (int)y1, (int)x2, (int)y2, gc);
                    return;

                default:
                    break;
            }
            rend.draw(indexShape, x1, y1, x2, y2, gc);
        }
    }

    public void mouseRelease(MouseEvent mouseEvent) {
        if(indexShape == -2 || indexShape == -3 || indexShape == -4) {
            rend.endPath(gc);
        }
        snapshot = null;
        System.gc();
        snapshot = canvasDrawElement.snapshot(null, null);
        isPress = false;
    }

    public void paintToolClick(MouseEvent mouseEvent) {
        if(toolsPaint.getSelectedToggle() != null)
            currentToggleButton = (ToggleButton)toolsPaint.getSelectedToggle();
        else
            currentToggleButton.setSelected(true);
        indexShape = Integer.parseInt((String)currentToggleButton.getUserData());
    }

    public void sliderDraggin(MouseEvent mouseEvent) {
        setStrokeWidth();
    }

    public void sliderClick(MouseEvent mouseEvent) {
        setStrokeWidth();
    }

    private void setStrokeWidth(){
        rend.setStrokeWidth((int)sliderStrokeWidth.getValue());
        sliderStrokeWidthLabel.setText("Толщина линии: " + String.valueOf((int)sliderStrokeWidth.getValue()));
    }

    public void saveImage(ActionEvent actionEvent) {
        saveFromFile();
    }

    public void saveFromFile(){
        isSave = false;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите директорию для сохранения изображения...");
        fileChooser.setInitialFileName("new_image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Изображение", "*.png"));
        File newImageFile = fileChooser.showSaveDialog(stage.getOwner());
        if(newImageFile == null)
            return;
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", newImageFile);
        } catch (IOException e) {
            System.out.println("Save image failed!");
        }
    }

    private Stage stage;
    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void menuExit() {
        if(isSave){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Предупреждение");
            alert.setHeaderText(null);
            alert.setContentText("Имеются не сохранённые элементы. Сохранить перед выходом?");
            Optional<ButtonType> buttonType = alert.showAndWait();
            if(buttonType.get() == ButtonType.OK){
                saveFromFile();
            }
        }
        Platform.exit();
        System.exit(0);
    }

    public void about(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("О программе");
        alert.setHeaderText(null);
        alert.setContentText("Создатель: Трубников Иван(Range), 2018");
        alert.showAndWait();
    }
}

// TODO: разработать систему буфера  возвращаемся назад, двигаемся вперёд
// TODO: добавить метод загрузки изображения
// TODO: добавление текста на картинку