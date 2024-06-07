//package org.example.stages;
//
//import javafx.animation.KeyFrame;
//import javafx.animation.Timeline;
//import javafx.fxml.FXML;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.Pane;
//import javafx.util.Duration;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//public class AnimationStage {
//
//    @FXML
//    private AnchorPane rootPane;
//
//    @FXML
//    private Pane animationPane;
//
////    @FXML
////    private Button startButton;
////
////    @FXML
////    private Button stopButton;
////
////    @FXML
////    private Button restartButton;
//
//    private Random random = new Random();
//    private List<ImageViewWrapper> imageViews = new ArrayList<>();
//    private static final int NUMBER_OF_IMAGES = 10;  // Количество изображений
//    private Timeline timeline;
//
//    @FXML
//    public void initialize() {
//        // Загрузите изображение
//        Image image = new Image(getClass().getResourceAsStream("/org/example/user2.png"));
//
//        // Создайте много ImageView и добавьте их в Pane
//        for (int i = 0; i < NUMBER_OF_IMAGES; i++) {
//            ImageView imageView = new ImageView(image);
//            imageView.setFitHeight(150);
//            imageView.setFitWidth(75);
//            double dx = random.nextDouble() * 4 - 2; // Скорость по X от -2 до 2
//            double dy = random.nextDouble() * 4 - 2; // Скорость по Y от -2 до 2
//            ImageViewWrapper wrapper = new ImageViewWrapper(imageView, dx, dy);
//            resetImagePosition(wrapper);
//            imageViews.add(wrapper);
//            animationPane.getChildren().add(imageView);
//        }
//
//        // Настройте анимацию
//        timeline = new Timeline(new KeyFrame(Duration.millis(20), event -> {
//            for (ImageViewWrapper wrapper : imageViews) {
//                moveImage(wrapper);
//            }
//        }));
//
//        timeline.setCycleCount(Timeline.INDEFINITE);
//    }
//
//    @FXML
//    private void handleStart() {
//        timeline.play();
//    }
//
//    @FXML
//    private void handleStop() {
//        timeline.stop();
//    }
//
//    @FXML
//    private void handleRestart() {
//        timeline.stop();
//        for (ImageViewWrapper wrapper : imageViews) {
//            resetImagePosition(wrapper);
//            double dx = random.nextDouble() * 4 - 2; // Скорость по X от -2 до 2
//            double dy = random.nextDouble() * 4 - 2; // Скорость по Y от -2 до 2
//            wrapper.setDx(dx);
//            wrapper.setDy(dy);
//        }
////        timeline.play();
//    }
//
//    private void moveImage(ImageViewWrapper wrapper) {
//        ImageView imageView = wrapper.getImageView();
//        double x = imageView.getLayoutX() + wrapper.getDx();
//        double y = imageView.getLayoutY() + wrapper.getDy();
//
//        // Проверьте границы и измените направление при необходимости
//        if (x < 0 || x > animationPane.getWidth() - imageView.getFitWidth()) {
//            wrapper.setDx(-wrapper.getDx());
//            x = imageView.getLayoutX() + wrapper.getDx();
//        }
//        if (y < 0 || y > animationPane.getHeight() - imageView.getFitHeight()) {
//            wrapper.setDy(-wrapper.getDy());
//            y = imageView.getLayoutY() + wrapper.getDy();
//        }
//
//        // Обновите позиции
//        imageView.setLayoutX(x);
//        imageView.setLayoutY(y);
//    }
//
//    private void resetImagePosition(ImageViewWrapper wrapper) {
//        ImageView imageView = wrapper.getImageView();
//        imageView.setLayoutX(250);
//        imageView.setLayoutY(220);
//    }
//}

package org.example.stages;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnimationStage {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Pane animationPane;

    private Random random = new Random();
    private List<StickmanWrapper> stickmen = new ArrayList<>();
    private final int numberOfStickmen = 10;  // Количество стикменов
    private Timeline timeline;
    private Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.PURPLE}; // Цвета для изменения

    @FXML
    public void backToCommands() throws IOException {
        Client.setRoot("commands");
    }

    @FXML
    public void initialize() {
        // Создайте много Canvas и добавьте их в Pane
        for (int i = 0; i < numberOfStickmen; i++) {
            Canvas canvas = new Canvas(100, 200);  // Canvas для каждого стикмена
            double dx = random.nextDouble() * 4 - 2; // Скорость по X от -2 до 2
            double dy = random.nextDouble() * 4 - 2; // Скорость по Y от -2 до 2
            StickmanWrapper wrapper = new StickmanWrapper(canvas, dx, dy);
            resetStickmanPosition(wrapper);
            stickmen.add(wrapper);
            animationPane.getChildren().add(canvas);
            drawStickman(canvas);

            // Добавляем обработчик события нажатия
            canvas.setOnMouseClicked(event -> handleStickmanClick(wrapper));
        }

        // Настройте анимацию
        timeline = new Timeline(new KeyFrame(Duration.millis(20), event -> {
            for (StickmanWrapper wrapper : stickmen) {
                moveStickman(wrapper);
            }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    @FXML
    private void handleStart() {
        timeline.play();
    }

    @FXML
    private void handleStop() {
        timeline.stop();
    }

    @FXML
    private void handleRestart() {
        timeline.stop();
        for (StickmanWrapper wrapper : stickmen) {
            resetStickmanPosition(wrapper);
            double dx = random.nextDouble() * 4 - 2; // Скорость по X от -2 до 2
            double dy = random.nextDouble() * 4 - 2; // Скорость по Y от -2 до 2
            wrapper.setDx(dx);
            wrapper.setDy(dy);
        }
//        timeline.play();
    }

    private void moveStickman(StickmanWrapper wrapper) {
        Canvas canvas = wrapper.getCanvas();
        double x = canvas.getLayoutX() + wrapper.getDx();
        double y = canvas.getLayoutY() + wrapper.getDy();

        // Проверьте границы и измените направление при необходимости
        if (x < 0 || x > animationPane.getWidth() - canvas.getWidth()) {
            wrapper.setDx(-wrapper.getDx());
            x = canvas.getLayoutX() + wrapper.getDx();
        }
        if (y < 0 || y > animationPane.getHeight() - canvas.getHeight()) {
            wrapper.setDy(-wrapper.getDy());
            y = canvas.getLayoutY() + wrapper.getDy();
        }

        // Обновите позиции
        canvas.setLayoutX(x);
        canvas.setLayoutY(y);
    }

    private void resetStickmanPosition(StickmanWrapper wrapper) {
        Canvas canvas = wrapper.getCanvas();
        canvas.setLayoutX(250);
        canvas.setLayoutY(220);
    }

    private void drawStickman(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2.0);

        // Рисуем голову
        double headCenterX = canvas.getWidth() / 2;
        double headCenterY = 50;
        double headRadius = 20;
        gc.strokeOval(headCenterX - headRadius, headCenterY - headRadius, headRadius * 2, headRadius * 2);

        // Рисуем тело
        double bodyStartX = headCenterX;
        double bodyStartY = headCenterY + headRadius;
        double bodyEndY = bodyStartY + 60;
        gc.strokeLine(bodyStartX, bodyStartY, bodyStartX, bodyEndY);

        // Рисуем руки
        double armLength = 40;
        double armY = bodyStartY + 20;
        gc.strokeLine(bodyStartX, armY, bodyStartX - armLength, armY);
        gc.strokeLine(bodyStartX, armY, bodyStartX + armLength, armY);

        // Рисуем ноги
        double legLength = 50;
        double legY = bodyEndY;
        gc.strokeLine(bodyStartX, legY, bodyStartX - legLength, legY + legLength);
        gc.strokeLine(bodyStartX, legY, bodyStartX + legLength, legY + legLength);

        // Рисуем глаза
        double eyeRadius = 3;
        double eyeOffsetX = 8;
        double eyeOffsetY = 10;
        gc.setFill(Color.WHITE);
        gc.strokeOval(headCenterX - eyeOffsetX, headCenterY - eyeOffsetY, eyeRadius * 2, eyeRadius * 2);
        gc.strokeOval(headCenterX + eyeOffsetX - eyeRadius * 2, headCenterY - eyeOffsetY, eyeRadius * 2, eyeRadius * 2);
        gc.fillOval(headCenterX - eyeOffsetX, headCenterY - eyeOffsetY, eyeRadius * 2, eyeRadius * 2);
        gc.fillOval(headCenterX + eyeOffsetX - eyeRadius * 2, headCenterY - eyeOffsetY, eyeRadius * 2, eyeRadius * 2);

        // Рисуем волосы (заливка цветом)
        gc.setFill(Color.WHITE); // Цвет волос
        double hairStartY = headCenterY - headRadius;

        // Центральный треугольник
        double[] centerHairX = {headCenterX, headCenterX - 10, headCenterX + 10};
        double[] centerHairY = {hairStartY, hairStartY - 15, hairStartY - 15};
        gc.strokePolygon(centerHairX, centerHairY, 3);
        gc.fillPolygon(centerHairX, centerHairY, 3);

        // Левый треугольник
        double[] leftHairX = {headCenterX - 15, headCenterX - 20, headCenterX - 10};
        double[] leftHairY = {hairStartY + 5, hairStartY - 10, hairStartY - 10};
        gc.strokePolygon(leftHairX, leftHairY, 3);
        gc.fillPolygon(leftHairX, leftHairY, 3);

        // Правый треугольник
        double[] rightHairX = {headCenterX + 15, headCenterX + 20, headCenterX + 10};
        double[] rightHairY = {hairStartY + 5, hairStartY - 10, hairStartY - 10};
        gc.strokePolygon(rightHairX, rightHairY, 3);
        gc.fillPolygon(rightHairX, rightHairY, 3);
    }

    private void handleStickmanClick(StickmanWrapper wrapper) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/fillCollectionObject.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setScene(new Scene(loader.load()));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(rootPane.getScene().getWindow());

            FillCollectionObjectDialogStage controller = loader.getController();
            controller.setAnimationStage(this);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Color newColor = colors[random.nextInt(colors.length)];
//        drawStickman(wrapper.getCanvas(), newColor);
    }
}
