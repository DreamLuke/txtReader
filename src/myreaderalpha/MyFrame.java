/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myreaderalpha;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.event.*;

/**
 * @author Luke
 */
class MyFrame extends JFrame implements KeyListener, ActionListener, MouseListener, Runnable {

    private ImageLabel jlab1, jlab2;
    private JPopupMenu jpu;
    private JFileChooser fileChooser = new JFileChooser();
    private javax.swing.filechooser.FileFilter fileFilter = new SimpleFileFilter(".txt");
    private StringBuffer filePath = new StringBuffer("");
    //Параметры
    private String imgPath1 = "/resources/imgBackground.jpg";
    private String imgPath2 = "/resources/imgBackground2.jpg";
    private String imgPath3 = "/resources/tray.png";
    private URL imgPath1URL = MyFrame.class.getResource(imgPath1);
    private URL imgPath2URL = MyFrame.class.getResource(imgPath2);
    private URL imgPath3URL = MyFrame.class.getResource(imgPath3);

    private int leftIndent = 50, rightIndent = 50;
    private int topIndent = 30, bottomIndent = 30;
    private String text;
    //StringBuffer sbText;
    private LinkedList<LinkedList<String>> pages = new LinkedList();
    private static int pageNumber = 0;
    private String[] words;
    private final LinkedList<String> emptyVector = new LinkedList();

    private JMenuItem jmiOpen = new MyJMenuItem("Open");
    private JMenu jmNavigation = new MyJMenu("Navigation");
    private JMenuItem jmiNextPage = new MyJMenuItem("Next page");
    private JMenuItem jmiPreviousPage = new MyJMenuItem("Previous page");
    private JMenuItem jmiToTheBeginning = new MyJMenuItem("To the beginning");
    private JMenuItem jmiByTheEnd = new MyJMenuItem("By the end");
    //JButton jmiByTheEnd = new MyJButton("By the end");
    private JMenuBar jmbGoToPageNumber = new MyJMenuBar();
    private JButton jButton = new MyJButton("Go to page...");
    //JMenuItem jButton = new MyJMenuItem("Go to page...");
    private JTextField jTextField = new JFormattedTextField(1);

    private JMenu jmTextSize = new MyJMenu("Text size...");
    private JMenuItem jmiMakeBigger = new MyJMenuItem("Make bigger");
    private JMenuItem jmiMakeSmaller = new MyJMenuItem("Make smaller");
    private JMenuItem jmiExit = new MyJMenuItem("Exit");
    private Thread thread;
    private static boolean isOpening = false;

    private int pageNumberBefore;
    private int pagesSizeBefore;
    private double onePersentBefore;
    private double pageNumberPositionInPersentBefore;

    private int pageNumberAfter;
    private int pagesSizeAfter;
    private double onePersenAfter;
    private double pageNumberPositionInPersentAfter;

    public MyFrame() throws AWTException {
        super("Моя читалка");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setFocusable(true);
        this.setLayout(null);
        this.setVisible(true);
        this.setResizable(false);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(imgPath3URL));

        //Описание меток
        //Первая метка
        ImageIcon ico = new ImageIcon(imgPath1URL);
        jlab1 = new ImageLabel(imgPath1URL);
        jlab1.setAlignmentParameters(leftIndent, rightIndent, topIndent, bottomIndent);
        jlab1.setVerticalAlignment(JLabel.TOP);
        jlab1.setSize(ico.getImage().getWidth(jlab1), ico.getImage().getHeight(jlab1));
        //Вторая метка
        ico = new ImageIcon(imgPath1URL);
        jlab2 = new ImageLabel(imgPath2URL);
        jlab2.setAlignmentParameters(leftIndent, rightIndent, topIndent, bottomIndent);
        jlab2.setVerticalAlignment(JLabel.TOP);
        jlab2.setSize(ico.getImage().getWidth(jlab2), ico.getImage().getHeight(jlab2));
        jlab2.setLocation(jlab1.getWidth(), 0);

        this.add(jlab1);
        this.add(jlab2);
        this.setSize(this.getInsets().left + jlab1.getWidth() + jlab2.getWidth() + this.getInsets().right, jlab1.getHeight() + this.getInsets().top + this.getInsets().bottom);

        //Обработка событий
        //Создание слушателей
        this.addKeyListener(this);

        //Слушатели для событий мыши
        jlab1.addMouseListener(this);
        jlab2.addMouseListener(this);

        //Описание контекстного меню и его элементов
        FontMetrics fm = this.getFontMetrics(this.getFont()); //Для определения размера символов в пикселях
        jpu = new MyJPopupMenu();
        jpu.add(jmiOpen);
        jpu.add(jmNavigation);
        jmNavigation.setMenuLocation(fm.stringWidth(jmNavigation.getText()) + 20 + 1, 0);

        jmiNextPage.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (jmiNextPage.isArmed()) {
                    //System.out.println("NEXT PAGE!!!!!!!!!!!!!!!!");
                    jmNavigation.requestFocusInWindow();
                    MyJButton.isMouseEntered = false;
                    jButton.repaint();
                }
            }
        });
        jmiPreviousPage.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (jmiPreviousPage.isArmed()) {
                    //System.out.println("PREVIOUS PAGE!!!!!!!!!!!!!!!!");
                    jmNavigation.requestFocusInWindow();
                    MyJButton.isMouseEntered = false;
                    jButton.repaint();
                }
            }
        });
        jmiToTheBeginning.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (jmiToTheBeginning.isArmed()) {
                    //System.out.println("BEGINING!!!!!!!!!!!!!!!");
                    jmNavigation.requestFocusInWindow();
                    MyJButton.isMouseEntered = false;
                    jButton.repaint();
                }
            }
        });
        jmiByTheEnd.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (jmiByTheEnd.isArmed()) {
                    //System.out.println("END!!!!!!!!!!!!!!!");
                    jmNavigation.requestFocusInWindow();
                    MyJButton.isMouseEntered = false;
                    jButton.repaint();
                    jButton.repaint();
                }
            }
        });

        jTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                //System.out.println();
                //System.out.println("jTextField got focus");

                jmiNextPage.setArmed(false);
                jmiPreviousPage.setArmed(false);
                jmiToTheBeginning.setArmed(false);
                jmiByTheEnd.setArmed(false);

                MyJButton.isMouseEntered = true;
                jButton.repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                //System.out.println();
                //System.out.println("jTextField lost focus");

                jmNavigation.requestFocusInWindow();
                MyJButton.isMouseEntered = false;
                jButton.repaint();
            }
        });


        jButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //System.out.println("BUTTON");

                jmiNextPage.setArmed(false);
                jmiPreviousPage.setArmed(false);
                jmiToTheBeginning.setArmed(false);
                jmiByTheEnd.setArmed(false);

                jmbGoToPageNumber.setSelected(jTextField);
                jTextField.requestFocusInWindow();
            }
        });

        //Откулчаю смену фокуса по нажатию TAB
        jmNavigation.setFocusTraversalKeysEnabled(false);
        jTextField.setFocusTraversalKeysEnabled(false);

        //Описание элементов вложенных в JMenuBar
        jTextField.setColumns(5);
        jButton.setFocusable(false);

        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                //System.out.println("поймал форточку");
                if (jpu.isShowing()) {
                    jmNavigation.requestFocusInWindow();
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });

        jTextField.addKeyListener(new KeyAdapter() {
                                      @Override
                                      public void keyPressed(KeyEvent e) {
                                          if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                                              //System.out.println("ENTER");
                                              jButton.doClick();
                                          }

                                          /*if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                                              //System.out.println();
                                              //System.out.println("вниз");
                                              jmNavigation.requestFocusInWindow();
                                          }*/
                                          if (e.getKeyCode() == KeyEvent.VK_UP) {
                                              //System.out.println();
                                              //System.out.println("вверх");//
                                              //jmNavigation.requestFocusInWindow();

                                              /*System.out.println(jmNavigation.getSelectedObjects().length);
                                              System.out.println(jmNavigation.getSelectedObjects()[0]);
                                              System.out.println(jmNavigation.getSubElements().length);
                                              System.out.println(jmNavigation.getSubElements()[0]);
                                              System.out.println(jmNavigation.getSubElements()[0].getSubElements());
                                              System.out.println(jmNavigation.getSubElements()[0].getSubElements().length);

                                              System.out.println(jmNavigation.getSubElements()[0].getSubElements()[0]);
                                              System.out.println(jmNavigation.getSubElements()[0].getSubElements()[1]);
                                              System.out.println(jmNavigation.getSubElements()[0].getSubElements()[2]);
                                              System.out.println(jmNavigation.getSubElements()[0].getSubElements()[3]);
                                              System.out.println(jmNavigation.getSubElements()[0].getSubElements()[4]);*/

                                          }
                                      }
                                  }
        );

        //Событие по которому текстовое поле ввода получает фокус
        jmiNextPage.addMenuKeyListener(new MenuKeyListener() {
            @Override
            public void menuKeyTyped(MenuKeyEvent e) {
            }

            @Override
            public void menuKeyPressed(MenuKeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP && jmiNextPage.isArmed()) {
                    //System.out.println("UP");
                    jmbGoToPageNumber.setSelected(jTextField);
                    jTextField.requestFocusInWindow();
                }
            }

            @Override
            public void menuKeyReleased(MenuKeyEvent e) {
            }
        });
        //Событие по которому текстовое поле ввода получает фокус
        jmiByTheEnd.addMenuKeyListener(new MenuKeyListener() {
            @Override
            public void menuKeyTyped(MenuKeyEvent e) {
            }

            @Override
            public void menuKeyPressed(MenuKeyEvent e) {
                //System.out.println("22222");
                if (e.getKeyCode() == KeyEvent.VK_DOWN && jmiByTheEnd.isArmed()) {
                    //if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    jmbGoToPageNumber.setSelected(jTextField);
                    //jTextField.setText("12345");
                    jTextField.requestFocusInWindow();
                }
            }

            @Override
            public void menuKeyReleased(MenuKeyEvent e) {
            }
        });

        //Переход на страницу с заданным номером
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pages.size() == 0) { //Если нет текста, то перемещаться некуда - выходим из функции
                    jTextField.setText("" + 1);
                    return;
                }

                //Получаем из строки ввода только нужные символы
                String s1 = "";
                String s2 = jTextField.getText();
                for (int i = 0; i < s2.length(); i++) {
                    if (s2.charAt(i) == '1' || s2.charAt(i) == '2' || s2.charAt(i) == '3' || s2.charAt(i) == '4' ||
                            s2.charAt(i) == '5' || s2.charAt(i) == '6' || s2.charAt(i) == '7' || s2.charAt(i) == '8' ||
                            s2.charAt(i) == '9' || (s2.charAt(i) == '0' && s1.length() != 0)) {
                        s1 = s1 + s2.charAt(i);
                    }
                }
                //После устранения лишних символолв отображаем номер страницы в окне ввода
                jTextField.setText(s1);

                try {
                    pageNumber = Integer.parseInt(s1) - 1;
                } catch (NumberFormatException nfe) {
                    pageNumber = pages.size();
                    jTextField.setText("" + pageNumber);
                    byTheEnd();
                    return;
                }

                if (pageNumber < 0) {
                    pageNumber = 1;
                    jTextField.setText("" + 1);
                    return;
                }

                //С форматом ввода номера всё номально - проверяем дальше
                if (pageNumber >= pages.size() - 1) { // Если номер страницы больше числа страниц в книги - перемещаемся в конец книги
                    pageNumber = pages.size();
                    jTextField.setText("" + pageNumber);
                    byTheEnd();
                } else if (pageNumber % 2 == 0) { //Т.к. используем нумерацию для элементов коллекции
                    jlab1.setTextForPrint(pages.get(pageNumber));
                    jlab2.setTextForPrint(pages.get(pageNumber + 1));
                    jlab1.repaint();
                    jlab2.repaint();
                } else if (pageNumber % 2 == 1) {
                    jlab1.setTextForPrint(pages.get(pageNumber - 1));
                    jlab2.setTextForPrint(pages.get(pageNumber));

                    pageNumber--;
                    jlab1.repaint();
                    jlab2.repaint();
                }
            }
        });

        jmNavigation.add(jmiNextPage);
        jmNavigation.add(jmiPreviousPage);
        jmNavigation.add(jmiToTheBeginning);
        jmNavigation.add(jmiByTheEnd);

        jmbGoToPageNumber.add(jButton);
        jmbGoToPageNumber.add(jTextField);
        jmNavigation.add(jmbGoToPageNumber);

        jmTextSize.add(jmiMakeBigger);
        jmTextSize.add(jmiMakeSmaller);
        jpu.add(jmTextSize);
        jpu.add(jmiExit);

        jmiOpen.addActionListener(this);
        jmiNextPage.addActionListener(this);
        jmiPreviousPage.addActionListener(this);
        jmiToTheBeginning.addActionListener(this);
        jmiByTheEnd.addActionListener(this);
        jmiMakeBigger.addActionListener(this);
        jmiMakeSmaller.addActionListener(this);
        jmiExit.addActionListener(this);

        ///////////////////////////////////////
        // Прописываю события клавитуры для пунктов контесктного меню,
        // чтобы они были активны после потери фокуса
        jmiOpen.addMenuKeyListener(new MenuKeyListener() {
            @Override
            public void menuKeyTyped(MenuKeyEvent e) {
            }

            @Override
            public void menuKeyPressed(MenuKeyEvent e) {
                //if (e.getKeyCode() == KeyEvent.VK_DOWN && jmiByTheEnd.isArmed()) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (jmNavigation.isFocusOwner() && jmiOpen.isArmed()) {
                        jmiOpen.doClick();
                    }
                }
            }

            @Override
            public void menuKeyReleased(MenuKeyEvent e) {
            }
        });

        jmiNextPage.addMenuKeyListener(new MenuKeyListener() {
            @Override
            public void menuKeyTyped(MenuKeyEvent e) {
            }

            @Override
            public void menuKeyPressed(MenuKeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (jmNavigation.isFocusOwner() && jmiNextPage.isArmed()) {
                        nextPage();
                    }
                }
            }

            @Override
            public void menuKeyReleased(MenuKeyEvent e) {
            }
        });

        jmiPreviousPage.addMenuKeyListener(new MenuKeyListener() {
            @Override
            public void menuKeyTyped(MenuKeyEvent e) {
            }

            @Override
            public void menuKeyPressed(MenuKeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (jmNavigation.isFocusOwner() && jmiPreviousPage.isArmed()) {
                        previousPage();
                    }
                }
            }

            @Override
            public void menuKeyReleased(MenuKeyEvent e) {
            }
        });

        jmiToTheBeginning.addMenuKeyListener(new MenuKeyListener() {
            @Override
            public void menuKeyTyped(MenuKeyEvent e) {
            }

            @Override
            public void menuKeyPressed(MenuKeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (jmNavigation.isFocusOwner() && jmiToTheBeginning.isArmed()) {
                        toTheBeginning();
                    }
                }
            }

            @Override
            public void menuKeyReleased(MenuKeyEvent e) {
            }
        });

        jmiByTheEnd.addMenuKeyListener(new MenuKeyListener() {
            @Override
            public void menuKeyTyped(MenuKeyEvent e) {
            }

            @Override
            public void menuKeyPressed(MenuKeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (jmNavigation.isFocusOwner() && jmiByTheEnd.isArmed()) {
                        byTheEnd();
                    }
                }
            }

            @Override
            public void menuKeyReleased(MenuKeyEvent e) {
            }
        });


        jmiMakeBigger.addMenuKeyListener(new MenuKeyListener() {
            @Override
            public void menuKeyTyped(MenuKeyEvent e) {
            }

            @Override
            public void menuKeyPressed(MenuKeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (jmNavigation.isFocusOwner() && jmiMakeBigger.isArmed()) {
                        makeBigger();
                    }
                }
            }

            @Override
            public void menuKeyReleased(MenuKeyEvent e) {
            }
        });

        jmiMakeSmaller.addMenuKeyListener(new MenuKeyListener() {
            @Override
            public void menuKeyTyped(MenuKeyEvent e) {
            }

            @Override
            public void menuKeyPressed(MenuKeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (jmNavigation.isFocusOwner() && jmiMakeSmaller.isArmed()) {
                        makeSmaller();
                    }
                }
            }

            @Override
            public void menuKeyReleased(MenuKeyEvent e) {
            }
        });


        jmiExit.addMenuKeyListener(new MenuKeyListener() {
            @Override
            public void menuKeyTyped(MenuKeyEvent e) {
            }

            @Override
            public void menuKeyPressed(MenuKeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (jmNavigation.isFocusOwner() && jmiExit.isArmed()) {
                        System.exit(0);
                    }
                }
            }

            @Override
            public void menuKeyReleased(MenuKeyEvent e) {
            }
        });
    }

    void makeOpen() {
        isOpening = true;
        fileChooser.setFileFilter(fileFilter);
        fileChooser.setAcceptAllFileFilterUsed(false);

        if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
            System.out.println("Отказались от выбора");
            isOpening = false;
            return;
        }

        filePath.delete(0, filePath.length());
        filePath.append(fileChooser.getSelectedFile().getParent() + fileChooser.getSelectedFile().separator + fileChooser.getSelectedFile().getName());

        try {
            text = ImageLabel.readText(filePath.toString());
            //Заменяю знаки табудяции пробелом
            text = text.replaceAll("\t", " ");
        } catch (Exception e) {
            System.out.println("Исключение при попытке чтения файла (MyFrame)");
        }


        if (text.length() != 0) {
            System.out.println("begin");
            double d1 = System.currentTimeMillis();
            pageNumber = 0;
            //ImageLabel.numPageWords = 0;
            ImageLabel.setNumPageWords(0);

            pages.clear();


            words = text.split("\\s");

            System.out.println("words size(): " + words.length);

            while (ImageLabel.getNumPageWords() < words.length) {
                pages.add(jlab1.makePage(words));
            }


            //Передаю меткам выводимый текст
            if (pageNumber < pages.size()) {
                jlab1.setTextForPrint(pages.get(pageNumber));
            }
            if (pageNumber + 1 < pages.size()) {
                jlab2.setTextForPrint(pages.get(pageNumber + 1));
            } else {
                jlab2.setTextForPrint();
            }
            jlab1.repaint();
            jlab2.repaint();
            double d2 = System.currentTimeMillis();
            System.out.println("time: " + (d2 - d1) / 1000);
            System.out.println("end");
            isOpening = false;
        } else {
            System.out.println("Попытка открыть пустой файл");
            isOpening = false;
            return;
        }


    }

    //ОПИСАНИЕ действий происходящих при выборе пунктов всплывающего меня
    public void actionPerformed(ActionEvent ae) {

        //
        JMenuItem menuitem = (JMenuItem) ae.getSource();
        JPopupMenu popupMenu = (JPopupMenu) menuitem.getParent();
        String comStr = ae.getActionCommand();

        if (comStr.equals("Open")) {
            thread = new Thread(this);
            thread.start();
            Thread.yield();
        }

        //События для перемещения по старницам
        switch (comStr) {
            case "Next page":
                if (pages.size() != 0) {
                    nextPage();
                } else {
                    System.out.println("Нечего листать!");
                }
                break;
            case "Previous page":
                if (pages.size() != 0) {
                    previousPage();
                } else {
                    System.out.println("Нечего листать!");
                }
                break;
            case "To the beginning":
                toTheBeginning();
                break;
            case "By the end":
                byTheEnd();
                break;
        }

        //События для изменения размера шрифта
        if (comStr.equals("Make bigger")) {
            makeBigger();
        }

        if (comStr.equals("Make smaller")) {
            makeSmaller();
        }

        //Выход
        if (comStr.equals("Exit"))

        {
            System.exit(0);
            thread = new Thread(this);
            thread.start();
            Thread.yield();
        }
    }

    //Описание действий происходящих при нажатии клавиш клавиатуры
    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
        if (!isOpening && ke.getKeyCode() == KeyEvent.VK_RIGHT) {
            nextPage();
        }
        if (!isOpening && ke.getKeyCode() == KeyEvent.VK_LEFT) {
            previousPage();

        }


    }

    public void keyReleased(KeyEvent ke) {
    }

    public void keyTyped(KeyEvent ke) {
    }

    //Функции перемещения по страницам
    //Функция листает страницы вперёд
    void nextPage() {
        if (!isOpening) {
            if (pageNumber < pages.size() - 2) {
                pageNumber = pageNumber + 2;
                //System.out.println("pageNumber (next): " + pageNumber);
                jlab1.setTextForPrint(pages.get(pageNumber));
                jlab1.repaint();
            }
            if (pageNumber < pages.size() - 1) {
                jlab2.setTextForPrint(pages.get(pageNumber + 1));
                jlab2.repaint();
            } else {
                jlab2.setTextForPrint(emptyVector);
                jlab2.repaint();
            }
        }
    }

    //Функция листает страницы назад
    void previousPage() {
        if (!isOpening) {
            if (pageNumber >= 2) {
                pageNumber = pageNumber - 2;
                //System.out.println("pageNumber (previous): " + pageNumber);
                jlab1.setTextForPrint(pages.get(pageNumber));
                jlab2.setTextForPrint(pages.get(pageNumber + 1));
                jlab1.repaint();
                jlab2.repaint();

            }
        }
    }

    //В конец книги
    void byTheEnd() {
        if (pages.size() > 2) {
            //System.out.println(" byTheEnd ");
            if (pages.size() % 2 == 0) {
                //System.out.println(" чётное ");
                //4 6
                pageNumber = pages.size() - 2;
                jlab1.setTextForPrint(pages.get(pageNumber));
                jlab2.setTextForPrint(pages.get(pageNumber + 1));
                jlab1.repaint();
                jlab2.repaint();
            } else {
                //System.out.println(" не чётное ");
                //3 5
                pageNumber = pages.size() - 1;
                jlab1.setTextForPrint(pages.get(pageNumber));
                jlab2.setTextForPrint(emptyVector);
                jlab1.repaint();
                jlab2.repaint();
            }
        }
    }

    //В начало книги
    void toTheBeginning() {
        //System.out.println(" toTheBeginning ");
        if (pageNumber >= 2) {
            pageNumber = 0;
            //System.out.println("pageNumber (beginning): " + pageNumber);
            jlab1.setTextForPrint(pages.get(pageNumber));
            jlab2.setTextForPrint(pages.get(pageNumber + 1));
            jlab1.repaint();
            jlab2.repaint();

        }

    }

    static int getPageNumber() {
        //System.out.println("static " +pageNumber);
        return pageNumber + 1;
    }

    //Прописыаю события мыши
    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        if (!isOpening && e.getButton() == MouseEvent.BUTTON1) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (e.getSource().equals(jlab1)) {
                    previousPage();
                }
                if (e.getSource().equals(jlab2)) {
                    nextPage();
                }
            }
        }
        if (!isOpening && e.getButton() == MouseEvent.BUTTON3) {
            //jpu.show(jlab1, e.getX(), e.getY());
            if (e.getSource().equals(jlab1)) {
                jpu.show(jlab1, e.getX(), e.getY());
                //System.out.println(text.length());
            }
            if (e.getSource().equals(jlab2)) {
                jpu.show(jlab2, e.getX(), e.getY());
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void run() {
        makeOpen();
    }

    public void makeReopen() {
        if (words.length != 0) {
            double d1 = System.currentTimeMillis();
            isOpening = true;
            ImageLabel.setNumPageWords(0);
            pages.clear();
            /*jlab1.setTextForPrint();
            jlab2.setTextForPrint();*/
            jlab1.repaint();
            jlab2.repaint();

            while (ImageLabel.getNumPageWords() < words.length) {
                pages.add(jlab1.makePage(words));
            }

            onePersenAfter = ((double)pages.size())/100;
            pageNumberAfter = pageNumber + 1;
            pagesSizeAfter = pages.size();
            pageNumberPositionInPersentAfter = onePersenAfter*pageNumberPositionInPersentBefore;

            //System.out.println(" one persent after is " + onePersentAfter +" pages");
            //((double)pages.size())/100
            System.out.println(" one persent after is " + ((double)pages.size())/100 +" pages");
            System.out.println(" after it's " + pageNumberPositionInPersentAfter +" persents page position");
            System.out.println("pages " +pages.size());

            pageNumber = (int)pageNumberPositionInPersentAfter;
            System.out.println("pageNumber " +pageNumber);
            if(pageNumber > 0) {
                pageNumber--;
            }
            if(pageNumber % 2 == 1) {
                pageNumber--;
            }

            //Передаю меткам выводимый текст
            if (pageNumber < pages.size()) {
                jlab1.setTextForPrint(pages.get(pageNumber));
            }
            if (pageNumber + 1 < pages.size()) {
                jlab2.setTextForPrint(pages.get(pageNumber + 1));
            } else {
                jlab2.setTextForPrint();
            }
            double d2 = System.currentTimeMillis();
            System.out.println("time: " + (d2 - d1) / 1000);
            System.out.println("end");
            isOpening = false;
            jlab1.repaint();
            jlab2.repaint();
        }
    }

    public void makeBigger() {
        System.out.println("bigger");

        if (jlab1.pageLettersFont.getSize() == 24) {
            System.out.println("24!");
            return;
        }

        pageNumberBefore = pageNumber + 1;
        pagesSizeBefore = pages.size();
        onePersentBefore = ((double)pages.size())/100;
        pageNumberPositionInPersentBefore = ((pageNumber + 1)/(((double)pages.size())/100));

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                jlab1.makeTextBigger();
                jlab2.makeTextBigger();
                makeReopen();
            }
        });
        thread2.start();
    }

    public void makeSmaller() {
        System.out.println("smaller");

        if (jlab1.pageLettersFont.getSize() == 14) {
            System.out.println("14!");
            return;
        }

        pageNumberBefore = pageNumber + 1;
        pagesSizeBefore = pages.size();
        onePersentBefore = ((double)pages.size())/100;
        pageNumberPositionInPersentBefore = ((pageNumber + 1)/(((double)pages.size())/100));
        System.out.println(" pageNumber before " + pageNumberBefore);
        System.out.println(" pages.size() before " + pagesSizeBefore);
        System.out.println(" one persent before is " + onePersentBefore +" pages");
        System.out.println(" before it's " + pageNumberPositionInPersentBefore +" persents page position");

        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                jlab1.makeTextSmaller();
                jlab2.makeTextSmaller();
                makeReopen();
            }
        });
        thread3.start();
    }


}
