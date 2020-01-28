/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myreaderalpha;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import javax.swing.JMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.URL;

/**
 * @author Luke
 */
public class MyJMenu extends JMenu {

    //String imgPathJMenu = "imgBackground.jpg";
    String imgPathJMenu = "/resources/imgBackground.jpg";
    URL imgPathJMenuURL = MyFrame.class.getResource(imgPathJMenu);
    String s;

    public MyJMenu(String s) {
        super(s);
        this.s = s;
    }

    @Override
    public void paint(Graphics g) { // Переопределяем метод paint

        FontMetrics fm = this.getFontMetrics(this.getFont()); //Для определения размера символов в пикселя
        Image bgImg = Toolkit.getDefaultToolkit().getImage(imgPathJMenuURL);

        if (this.getParent().getClass().toString().equals("class javax.swing.JPopupMenu")) {
            //Кусочки фона вложенного меню (вверху и внизу)
            this.getParent().getGraphics().drawImage(bgImg, 1, 1, this.getParent().getWidth() - 1, 3, 0, 0, this.getParent().getWidth(), this.getParent().getHeight(), this.getParent());
            this.getParent().getGraphics().drawImage(bgImg, 1, this.getParent().getHeight() - 2, this.getParent().getWidth() - 1, this.getParent().getHeight() - 1, 0, 0, this.getParent().getWidth(), this.getParent().getHeight(), this.getParent());

            //Рамка вложенного меню
            this.getParent().getGraphics().drawLine(0, 0, this.getParent().getWidth(), 0);
            this.getParent().getGraphics().drawLine(0, 0, 0, this.getParent().getHeight());
            this.getParent().getGraphics().drawLine(this.getParent().getWidth() - 1, 0, this.getParent().getWidth() - 1, this.getParent().getHeight());
            this.getParent().getGraphics().drawLine(0, this.getParent().getHeight() - 1, this.getParent().getWidth(), this.getParent().getHeight() - 1);

            //this.getParent().getComponents()[0].repaint();
        }

        if (this.isSelected()) {
            g.setColor(Color.red);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(Color.black);
            Font f = new Font(g.getFont().getFontName(), Font.BOLD, g.getFont().getSize());
            g.setFont(f);
            g.drawString(this.s + " >", (this.getWidth() / 2 - (this.getWidth() / 2) / 2 - (this.getWidth() / 2) / 2 / 2) + 2, this.getHeight() / 2 + fm.getHeight() / 2 / 2);
        } else {
            //Image bgImg = Toolkit.getDefaultToolkit().getImage(this.imgPathJMenu);
            //Image bgImg = Toolkit.getDefaultToolkit().getImage(imgPathJMenuURL);
            g.drawImage(bgImg, 0, 0, this);
            Font f = new Font(g.getFont().getFontName(), Font.BOLD, g.getFont().getSize());
            g.setFont(f);
            g.drawString(this.s + " >", this.getWidth() / 2 - (this.getWidth() / 2) / 2 - (this.getWidth() / 2) / 2 / 2, this.getHeight() / 2 + fm.getHeight() / 2 / 2);
        }
    }
}
