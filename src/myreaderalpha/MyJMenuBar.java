package myreaderalpha;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.URL;

/**
 * Created by Luke on 22.03.2018.
 */
public class MyJMenuBar extends JMenuBar {
    String imgPathJMenuBar = "/resources/imgBackground.jpg";
    URL imgPathJMenuBarURL = MyFrame.class.getResource(imgPathJMenuBar);

    public MyJMenuBar() {
        super();
    }

    @Override
    public void paint(Graphics g) { // Переопределяем метод paint
        Image bgImg = Toolkit.getDefaultToolkit().getImage(imgPathJMenuBarURL);

        g.drawImage(bgImg, 0, 0, this);
        g.setColor(Color.BLACK);

        super.paintComponents(g);
    }
}
