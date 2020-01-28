/*
 * Решение тестового задания.
 * Создаётся экранная форма. При нажатии на иконку в трее происходит
 * анимированное скрытие формы, при повторном нажатии - анимированое
 * отображение. Внутри формы заданы элементы JCheckBox для расчёта суммы
 * чисел(сумма выбраных чисел отображается в метке).
 * Также в форме задан элемент JTree, отображающий иерархию книг.
 */
package myreaderalpha;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Luke
 */
public class TestTaskFrame extends JFrame {
    //Определяю положение рабочей области экрана

    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    boolean isFrameShown = true;
    //FontsComponent fc = new FontsComponent();


    public TestTaskFrame() {
        //
        //fc.getFont("festus.ttf", 14);
        //Font myFont = new FontsComponent().getFont("festus.ttf", 14.0f);
        Font myFont = new Font(null);


        //
        final JFrame jfrm;
        TrayIcon trayIcon;
        Image image;
        final int jfrmWidth = 400;

        JFrame.setDefaultLookAndFeelDecorated(true);
        jfrm = new JFrame("Test Task");
        //jfrm.getContentPane().setLayout(new FlowLayout());
        jfrm.getContentPane().setLayout(null);
        jfrm.setBounds(ge.getMaximumWindowBounds().getBounds().width - jfrmWidth, ge.getMaximumWindowBounds().getBounds().y, jfrmWidth, ge.getMaximumWindowBounds().getBounds().height);
        jfrm.setResizable(false);
        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Задаю слушателя для формы
        jfrm.addComponentListener(new ComponentListener() {
            //Переопределяю метод движения формы

            public void componentMoved(ComponentEvent e) {
                if (isFrameShown) {
                    jfrm.setLocation(ge.getMaximumWindowBounds().getBounds().width - jfrmWidth, ge.getMaximumWindowBounds().getBounds().y);
                }
                if (!isFrameShown) {
                    jfrm.setLocation(ge.getMaximumWindowBounds().getBounds().width, ge.getMaximumWindowBounds().getBounds().y);
                }
            }

            public void componentHidden(ComponentEvent e) {
                //System.out.println("Hidden");
            }

            public void componentShown(ComponentEvent e) {
                //System.out.println("Shown");
            }

            public void componentResized(ComponentEvent e) {
                //System.out.println("Resized");
            }
        });

        //Проверяю подерживается ли работа с системным треем
        if (SystemTray.isSupported()) {
            //Создаю объект для работы с треем
            SystemTray tray = SystemTray.getSystemTray();
            //Задаю иконку и описываю объект системного трея
            image = Toolkit.getDefaultToolkit().getImage("tray.png");
            trayIcon = new TrayIcon(image, "Click here");
            trayIcon.setImageAutoSize(true);
            //Создаю слушателя событий мыши для иконки системного трея
            trayIcon.addMouseListener(new MouseAdapter() {

                //При клике на объекте системного трея форма заезжает в право или выезжает
                @Override
                public void mouseClicked(MouseEvent arg0) {
                    boolean isAlredyPressed = false;
                    if (jfrm.getLocation().x == ge.getMaximumWindowBounds().getBounds().width - jfrmWidth && !isAlredyPressed && jfrm.getState() != jfrm.ICONIFIED) {
                        System.out.println("hide");
                        for (int i = 0; i <= jfrmWidth * 100; i += 1) {
                            jfrm.setLocation(ge.getMaximumWindowBounds().getBounds().width - jfrmWidth + i / (jfrmWidth / 4), ge.getMaximumWindowBounds().getBounds().y);
                        }
                        isAlredyPressed = true;
                        isFrameShown = false;
                    }
                    if (jfrm.getLocation().x == ge.getMaximumWindowBounds().getBounds().width && !isAlredyPressed && jfrm.getState() != jfrm.ICONIFIED) {
                        //System.out.println("show");
                        for (int i = 0; i <= jfrmWidth * 100; i += 1) {
                            jfrm.setLocation(ge.getMaximumWindowBounds().getBounds().width - i / (jfrmWidth / 4), ge.getMaximumWindowBounds().getBounds().y);
                        }
                        isAlredyPressed = true;
                        isFrameShown = true;
                    }
                    isAlredyPressed = false;
                }

                @Override
                public void mouseReleased(MouseEvent arg0) {
                    System.out.println("Welcome to Java Programming!");
                }
            });
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }
        }

        //Работа с чек-боксами
        final JCheckBox jchb1 = new JCheckBox("12");
        jchb1.setBounds(0, 10, jfrmWidth, 20);
        jfrm.add(jchb1);

        final JCheckBox jchb2 = new JCheckBox("42");
        jchb2.setBounds(0, 30, jfrmWidth, 20);
        jfrm.add(jchb2);

        final JCheckBox jchb3 = new JCheckBox("201");
        jchb3.setBounds(0, 50, jfrmWidth, 20);
        jfrm.add(jchb3);

        //Задаю константы для расчёта суммы
        final int a1, a2, a3;
        a1 = 12;
        a2 = 42;
        a3 = 201;

        final JLabel jLabSum = new JLabel("Сумма чисел равна: 0");
        jLabSum.setBounds(0, 70, jfrmWidth, 20);
        jfrm.add(jLabSum);

        //Задаю слушателей для чек-боксов и обрабатываю событие изменение состояния элементов
        jchb1.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (jchb1.isSelected() && jchb2.isSelected() && jchb3.isSelected()) {
                    jLabSum.setText("Сумма чисел равна: " + (a1 + a2 + a3));
                }
                if (jchb1.isSelected() && jchb2.isSelected() && !jchb3.isSelected()) {
                    jLabSum.setText("Сумма чисел равна: " + (a1 + a2));
                }
                if (jchb1.isSelected() && !jchb2.isSelected() && jchb3.isSelected()) {
                    jLabSum.setText("Сумма чисел равна: " + (a1 + a3));
                }
                if (jchb1.isSelected() && !jchb2.isSelected() && !jchb3.isSelected()) {
                    jLabSum.setText("Сумма чисел равна: " + (a1));
                }
                if (!jchb1.isSelected() && jchb2.isSelected() && jchb3.isSelected()) {
                    jLabSum.setText("Сумма чисел равна: " + (a2 + a3));
                }
                if (!jchb1.isSelected() && jchb2.isSelected() && !jchb3.isSelected()) {
                    jLabSum.setText("Сумма чисел равна: " + (a2));
                }
                if (!jchb1.isSelected() && !jchb2.isSelected() && jchb3.isSelected()) {
                    jLabSum.setText("Сумма чисел равна: " + (a3));
                }
                if (!jchb1.isSelected() && !jchb2.isSelected() && !jchb3.isSelected()) {
                    jLabSum.setText("Сумма чисел равна: " + (0));
                }
            }
        });

        jchb2.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (jchb1.isSelected() && jchb2.isSelected() && jchb3.isSelected()) {
                    jLabSum.setText("Сумма чисел равна: " + (a1 + a2 + a3));
                }
                if (jchb1.isSelected() && jchb2.isSelected() && !jchb3.isSelected()) {
                    jLabSum.setText("Сумма чисел равна: " + (a1 + a2));
                }
                if (jchb1.isSelected() && !jchb2.isSelected() && jchb3.isSelected()) {
                    jLabSum.setText("Сумма чисел равна: " + (a1 + a3));
                }
                if (jchb1.isSelected() && !jchb2.isSelected() && !jchb3.isSelected()) {
                    jLabSum.setText("Сумма чисел равна: " + (a1));
                }
                if (!jchb1.isSelected() && jchb2.isSelected() && jchb3.isSelected()) {
                    jLabSum.setText("Сумма чисел равна: " + (a2 + a3));
                }
                if (!jchb1.isSelected() && jchb2.isSelected() && !jchb3.isSelected()) {
                    jLabSum.setText("Сумма чисел равна: " + (a2));
                }
                if (!jchb1.isSelected() && !jchb2.isSelected() && jchb3.isSelected()) {
                    jLabSum.setText("Сумма чисел равна: " + (a3));
                }
                if (!jchb1.isSelected() && !jchb2.isSelected() && !jchb3.isSelected()) {
                    jLabSum.setText("Сумма чисел равна: " + (0));
                }
            }
        });

        jchb3.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (jchb1.isSelected() && jchb2.isSelected() && jchb3.isSelected()) {
                    jLabSum.setText("Сумма чисел равна: " + (a1 + a2 + a3));
                }
                if (jchb1.isSelected() && jchb2.isSelected() && !jchb3.isSelected()) {
                    jLabSum.setText("Сумма чисел равна: " + (a1 + a2));
                }
                if (jchb1.isSelected() && !jchb2.isSelected() && jchb3.isSelected()) {
                    jLabSum.setText("Сумма чисел равна: " + (a1 + a3));
                }
                if (jchb1.isSelected() && !jchb2.isSelected() && !jchb3.isSelected()) {
                    jLabSum.setText("Сумма чисел равна: " + (a1));
                }
                if (!jchb1.isSelected() && jchb2.isSelected() && jchb3.isSelected()) {
                    jLabSum.setText("Сумма чисел равна: " + (a2 + a3));
                }
                if (!jchb1.isSelected() && jchb2.isSelected() && !jchb3.isSelected()) {
                    jLabSum.setText("Сумма чисел равна: " + (a2));
                }
                if (!jchb1.isSelected() && !jchb2.isSelected() && jchb3.isSelected()) {
                    jLabSum.setText("Сумма чисел равна: " + (a3));
                }
                if (!jchb1.isSelected() && !jchb2.isSelected() && !jchb3.isSelected()) {
                    jLabSum.setText("Сумма чисел равна: " + (0));
                }
            }
        });

        //Работа с деревом
        Font font = new Font("Festus", Font.BOLD | Font.ITALIC, 12); //Задаю шрифт для работы с деревом

        DefaultMutableTreeNode books = new DefaultMutableTreeNode("Books");
        DefaultMutableTreeNode prog = new DefaultMutableTreeNode("Programming");
        DefaultMutableTreeNode java = new DefaultMutableTreeNode("Java");
        books.add(prog);
        prog.add(java);
        java.add(new DefaultMutableTreeNode("Swing"));
        java.add(new DefaultMutableTreeNode("Самаучитель по Java"));

        DefaultMutableTreeNode job = new DefaultMutableTreeNode("CNC");
        books.add(job);
        job.add(new DefaultMutableTreeNode("Материаловедение"));
        job.add(new DefaultMutableTreeNode("Термообработка"));

        JTree jtree = new JTree(books);
        jtree.setBounds(0, 100, jfrmWidth, 150);
        //jtree.setFont(font);
        jtree.setFont(myFont);
        //System.out.println(jtree.getFont());
        jtree.setRootVisible(true);
        jfrm.add(jtree);

        //Отображаю форму
        jfrm.setVisible(true);
    }

    public static void main(String[] args) {
        //Проверка работы приложения
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                new TestTaskFrame();
            }
        });
    }
}

