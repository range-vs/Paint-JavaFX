package sample;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rendering.Render;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
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
    private ColorPicker colorPickerFill;
    @FXML
    private ColorPicker colorPickerStroke;
    @FXML
    private CheckBox isFill;
    @FXML
    private TextField valueSliderStrokeWidth;

    private ToggleButton currentToggleButton;
    private double x1 = 0;
    private double y1 = 0;
    private boolean isPress = false;
    private int indexShape = 0;
    private GraphicsContext gc;
    private Image snapshot;
    private boolean isSave = false;
    private BufferedImage iconClear;
    private boolean isSliderWidthActive;


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
            snapshot = canvasDrawElement.snapshot(null, null);
        });

        panelParentDraw.heightProperty().addListener((obs, oldVal, newVal) -> {
            canvasDrawElement.setHeight(panelParentDraw.getHeight());
            gc.drawImage(snapshot, 0, 0);
            snapshot = canvasDrawElement.snapshot(null, null);
        });


        valueSliderStrokeWidth.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                if(isSliderWidthActive){
                    return;
                }

                int num = 1;
                String text = newValue;
                if (!text.equals("")) {
                    try {
                        num = Integer.parseInt(text);
                        if (num == 0) {
                            valueSliderStrokeWidth.setText("1");
                        } else if (num > 500) {
                            valueSliderStrokeWidth.setText("500");
                        } else {
                            final int n = num;
                            Platform.runLater(() -> {
                                valueSliderStrokeWidth.setText(String.valueOf(n));
                                valueSliderStrokeWidth.positionCaret(String.valueOf(n).length());
                            });
                        }
                    } catch (Exception ex) {
                        valueSliderStrokeWidth.setText(oldValue);
                    }
                }
                sliderStrokeWidth.setValue(num);
                actionSlider(false);
            }
        });


        valueSliderStrokeWidth.focusedProperty().addListener( (obs, oldValue, newValue) -> {
            if (!newValue && valueSliderStrokeWidth.getText().equals("")) {
                valueSliderStrokeWidth.setText("1");
            }
        });

        sliderStrokeWidth.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    isSliderWidthActive = true;
                } else {
                    isSliderWidthActive = false;
                }
            }
        });

        /*Circle c = new Circle();
        c.setCenterX(50);
        c.setCenterY(50);
        c.setRadius(20);
        panelParentDraw.getChildren().add(c);*/
    }

    public void mouseClickOnDrawPanel(MouseEvent mouseEvent) {
        isSave = true;
        //snapshot = canvasDrawElement.snapshot(null, null);
        x1 = mouseEvent.getX();
        y1 = mouseEvent.getY();
        rend.setFillColor(isFill.isSelected() ? colorPickerFill.getValue() : Color.TRANSPARENT);
        rend.setStrokeColor(colorPickerStroke.getValue());
        switch(indexShape) {
            case -4:
                snapshot = canvasDrawElement.snapshot(null, null);
                rend.startPath((int)x1, (int)y1, gc);
                break;

            case -3: case -2:
                rend.reflash(snapshot, gc);
                if(indexShape == -2) {
                    rend.startPath(Color.WHITE, (int) x1, (int) y1, gc);
                }else{
                    rend.startPath((int)x1, (int)y1, gc);
                }
                snapshot = canvasDrawElement.snapshot(null, null);
                drawPencilCursor((int) x1, (int) y1);
                break;

            case -1:
                snapshot = canvasDrawElement.snapshot(null, null);
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

    public void drawPencilCursor(int x, int y){
        gc.drawImage(SwingFXUtils.toFXImage(iconClear, null),  x-rend.getStrokeWidth()/2,
                y-rend.getStrokeWidth()/2);
    }

    public void mouseMoved(MouseEvent mouseEvent) {
        if(indexShape == -2 || indexShape == -3){
            rend.reflash(snapshot, gc);
            drawPencilCursor((int)mouseEvent.getX(), (int)mouseEvent.getY());
        }
    }

    public void mouseDraggered(MouseEvent mouseEvent) {
        if(isPress) {
            double x2 = mouseEvent.getX();
            double y2 = mouseEvent.getY();
            rend.reflash(snapshot, gc);
            switch (indexShape) {
                case -2: case -3:
                    rend.reflash(snapshot, gc);
                    rend.addPointPath((int) x2, (int) y2, gc);
                    snapshot = canvasDrawElement.snapshot(null, null);
                    drawPencilCursor((int) x2, (int) y2);
                    return;


                case -4:
                    rend.addPointPath((int) x2, (int) y2, gc);
                    rend.draw(2, (int) x1, (int) y1, (int) x2, (int) y2, gc);
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
            if(indexShape == -2 || indexShape == -3) {
                rend.reflash(snapshot, gc);
            }
        }
        snapshot = null;
        System.gc();
        snapshot = canvasDrawElement.snapshot(null, null);
        isPress = false;
        if(indexShape == -2 || indexShape == -3){
            drawPencilCursor((int)mouseEvent.getX(), (int)mouseEvent.getY());
        }
    }

    public void createPencilCursor(){
        int w = (int)sliderStrokeWidth.getValue();
        iconClear = null;
        System.gc();
        iconClear = new BufferedImage(w, w, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gr = iconClear.createGraphics();
        gr.setColor(java.awt.Color.BLACK);
        gr.drawOval(0,0, w-1, w-1);
        snapshot = canvasDrawElement.snapshot(null, null);
    }

    public void paintToolClick(MouseEvent mouseEvent) {
        if (toolsPaint.getSelectedToggle() != null)
            currentToggleButton = (ToggleButton) toolsPaint.getSelectedToggle();
        else
            currentToggleButton.setSelected(true);
        indexShape = Integer.parseInt((String) currentToggleButton.getUserData());

        if (indexShape == -2 || indexShape == -3) {
            createPencilCursor();
        } else {
            iconClear = null;
            System.gc();
        }
    }

    private void actionSlider(boolean f){
        rend.setStrokeWidth((int)sliderStrokeWidth.getValue());
        createPencilCursor();
        if(f) {
            valueSliderStrokeWidth.setText(String.valueOf(rend.getStrokeWidth()));
        }
    }

    public void sliderDraggin(MouseEvent mouseEvent) {
        actionSlider(true);
    }

    public void sliderClick(MouseEvent mouseEvent) {
        actionSlider(true);
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
        alert.setContentText("Создатель: Трубников Иван(Range), 2019. v 0.3.");
        alert.showAndWait();
    }

    public void openImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите изображение");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Изображение", "*.png"));
        File img = fileChooser.showOpenDialog(stage.getOwner());
        if(img == null){
            System.out.println("no find image!");
            return;
        }
        Image image = new Image("file:/" + img.getAbsolutePath());
        rend.reflash(image, gc);
        snapshot = image;
        image = null;
        System.gc();
    }

    public void mouseExited(MouseEvent mouseEvent) {
        if(indexShape == -2 || indexShape == -3){
            rend.reflash(snapshot, gc);
        }
    }

}

// TODO: сделать в рендере массив операций(заливка, фигура) -- разработать новую модель
// TODO: добавление текста на картинку