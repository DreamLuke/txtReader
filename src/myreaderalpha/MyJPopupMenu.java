/*
 * Наследую элемент JPopupMenu и
 * переопределяю его метод пейнт.
 */
package myreaderalpha;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.URL;
import javax.swing.JPopupMenu;

/**
 * @author Luke
 */
public class MyJPopupMenu extends JPopupMenu {

    //String imgPathJPopupMenu = "imgBackground.jpg";
    String imgPathJPopupMenu = "/resources/imgBackground.jpg";
    URL imgPathJPopupMenuURL = MyFrame.class.getResource(imgPathJPopupMenu);

    public MyJPopupMenu() {
        super();
    }

    @Override
    public void paint(Graphics g) { // Переопределяем метод paint
        //Image bgImg = Toolkit.getDefaultToolkit().getImage(this.imgPathJPopupMenu);
        Image bgImg = Toolkit.getDefaultToolkit().getImage(imgPathJPopupMenuURL);
        g.drawImage(bgImg, 0, 0, this);
        g.setColor(Color.BLACK);

        g.drawLine(0, 0, this.getWidth(), 0);
        g.drawLine(0, 0, 0, this.getHeight());
        g.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1, this.getHeight());
        g.drawLine(0, this.getHeight() - 1, this.getWidth(), this.getHeight() - 1);
        super.paintComponents(g);
    }
}
