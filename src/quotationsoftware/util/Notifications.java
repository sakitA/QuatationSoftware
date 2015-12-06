package quotationsoftware.util;

import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;

/**
 * Utility class which handles creation of notifivation popups.
 */
public class Notifications {

    /**
     * Creates a notificiation popup with the specified window as parent container.
     * Popup is animated and self destructs after some time.
     * @param window
     * @param message
     */
    public static void showNotification(Window window, String message) {
        final Popup popup = new Popup();
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        ImageView thumb = new ImageView(new Image(Notifications.class.getResourceAsStream("/resources/images/thumb-up.png")));
        Label lbl = new Label(message);
        hbox.getChildren().addAll(thumb, lbl);

        lbl.setStyle(Keys.NOT_LBL);
        hbox.setStyle(Keys.NOT_HBOX);
        popup.getContent().addAll(hbox);

        hbox.setOpacity(0);
        popup.show(window);
        popup.setX(window.getWidth() / 2 - popup.getWidth() / 2);
        popup.setY(window.getHeight() / 2 - popup.getHeight() / 2);

        FadeTransition fadeIn = FadeTransitionBuilder.create()
                .duration(Duration.seconds(0.5))
                .node(hbox)
                .fromValue(0)
                .toValue(1)
                .build();
        fadeIn.play();

        final FadeTransition fadeOut = FadeTransitionBuilder.create()
                .duration(Duration.seconds(1))
                .node(hbox)
                .delay(new Duration(2000))
                .fromValue(1)
                .toValue(0)
                .build();

        fadeIn.setOnFinished((ActionEvent event) -> {
            fadeOut.play();
        });
    }
}
