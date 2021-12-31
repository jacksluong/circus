package GUI;

import common.Employee;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Created by Jacks on 11/22/18.
 * E for employee.
 */

public class ECircle extends StackPane {
    private final Circle circle = new Circle(35);
    private final Text label;
    @SuppressWarnings("unused")
    private final Employee e;

    public ECircle(String text, EmployeeV e) {
        label = new Text(text);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(Font.font(9));
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.DARKGRAY);
        getChildren().addAll(circle, label);
        this.e = e;
    }
    
    @SuppressWarnings("unused")
    public Employee getE() {
        return e;
    }
    
    public Text getLabel() {
        return label;
    }

    public Circle getCircle() {
        return circle;
    }
}
