package GUI;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Created by Jacks on 11/23/18.
 * M for menu.
 */

public class MRectangle extends StackPane {
    private final Rectangle rectangle;

    public MRectangle(String text) {
        this(text, 1);
    }

    public MRectangle(String text, double scaleFactor) {
        rectangle = new Rectangle(160 * scaleFactor, 60 * scaleFactor, Color.ORANGE);
        rectangle.setArcHeight(7 * scaleFactor);
        rectangle.setArcWidth(7 * scaleFactor);
        rectangle.setStroke(Color.WHITE);
        rectangle.setStrokeWidth(0);
        rectangle.setStrokeType(StrokeType.INSIDE);
        Text label = new Text(text);
        label.setFont(Font.font("Corbel", FontWeight.EXTRA_BOLD, 16 * scaleFactor));
        label.setTextAlignment(TextAlignment.CENTER);
        /* addition label formatting if desired */
        getChildren().addAll(rectangle, label);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}
