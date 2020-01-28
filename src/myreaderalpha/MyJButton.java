package myreaderalpha;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import java.net.URL;

/**
 * Created by Luke on 22.03.2018.
 */
public class MyJButton extends JButton implements MouseListener {

    String imgPath1 = "/resources/imgBackground.jpg";
    URL imgPathJButtonURL = MyFrame.class.getResource(imgPath1);
    String s;
    static boolean isMouseEntered = false;

    public MyJButton(String s) {
        super(s);
        this.s = s;
        this.addMouseListener(this);
    }

    @Override
    public void paint(Graphics g) { // Переопределяем метод paint
        FontMetrics fm = this.getFontMetrics(this.getFont()); //Для определения размера символов в пикселях
        Image bgImg = Toolkit.getDefaultToolkit().getImage(imgPathJButtonURL);
        g.drawImage(bgImg, 0, 0, this);

        //Кусочки фона вложенного меню (вверху и внизу)
        this.getParent().getGraphics().drawImage(bgImg, 1, this.getParent().getHeight() - 2, this.getParent().getWidth() - 1, this.getParent().getHeight() - 1, 0, 0, this.getParent().getWidth(), this.getParent().getHeight(), this.getParent());

        if (isMouseEntered) {
        //if (this.getModel().isRollover()) {
            g.setColor(Color.red);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(Color.black);
            Font f = new Font(g.getFont().getFontName(), Font.BOLD, g.getFont().getSize());
            g.setFont(f);
            g.drawString(this.s, (this.getWidth() / 2 - (this.getWidth() / 2) / 2 - (this.getWidth() / 2) / 2 / 2) + 2, (this.getHeight() / 2 + fm.getHeight() / 2 / 2) + 2);
        } else {
            g.drawImage(bgImg, 0, 0, this);
            Font f = new Font(g.getFont().getFontName(), Font.BOLD, g.getFont().getSize());
            g.setFont(f);
            g.drawString(this.s, this.getWidth() / 2 - (this.getWidth() / 2) / 2 - (this.getWidth() / 2) / 2 / 2, this.getHeight() / 2 + fm.getHeight() / 2 / 2);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //isMouseEntered = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //isMouseEntered = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //isMouseEntered = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //isMouseEntered = false;
    }
}
