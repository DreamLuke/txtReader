/*
 * Класс наследует объект JLabel.
 * В нём переопределён метод пейнт для того,
 * чтобы можно было ставить в качестве фона
 * метки картинку и выводить текст в несколько
 * строк без использования тегов.
 */
package myreaderalpha;

import org.mozilla.universalchardet.UniversalDetector;

import java.awt.*;
import java.io.*;
import java.net.URL;
import javax.swing.ImageIcon;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.LinkedList;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

/**
 * @author Luke
 */
class ImageLabel extends JLabel {

    private String imgagePath;
    private URL imgagePathURL;
    private String s = "";
    private static int numPageWords = 0;
    private static int insideWordIndex = 0;
    private LinkedList<String> pageText;
    final private int numLines = 27;


    private LinkedList<String> vectorPage = new LinkedList();
    private int topIndent = this.getFontMetrics(this.getFont()).getHeight(), bottomIndent, leftIndent = 0, rightIndent = 0;

    private JFileChooser fileChooser = new JFileChooser();
    //filechooser.FileFilter fileFilter = new SimpleFileFilter(".txt");
    private String indentation = "   ";

    private Color textColor = Color.BLACK;
    //Font pageNumberFont = new Font(this.getFont().getName(), Font.PLAIN, 16);
    private Font pageNumberFont = new Font(null, Font.ITALIC, 14);
    Font pageLettersFont = new Font(this.getFont().getName(), Font.PLAIN, 16);

    public ImageLabel(URL imgagePathURL) {
        super();
        this.imgagePathURL = imgagePathURL;
        //this.setFont(new Font(this.getFont().getName(), Font.PLAIN, 16));
        this.setFont(pageLettersFont);
        //numLines = (this.getHeight() - this.topIndent - this.bottomIndent) / this.getPrintTextHeight();
        //System.out.println("mumLines " +numLines);
    }

    void setAlignmentParameters(int leftIndent, int rightIndent, int topIndent, int bottomIndent) {
        this.leftIndent = leftIndent;
        this.rightIndent = rightIndent;
        this.topIndent = topIndent;
        this.bottomIndent = bottomIndent;
    }

    void setTextForPrint(LinkedList<String> vec) {
        this.vectorPage = vec;
    }

    void setTextForPrint() {
        this.vectorPage.clear();
    }

    //Функция возвращает высоту шрифта
    private int getPrintTextHeight() {
        //FontMetrics fm = this.getFontMetrics(this.getFont()); //Для определения размера символов в пикселях
        FontMetrics fm = this.getFontMetrics(pageLettersFont); //Для определения размера символов в пикселях
        System.out.println("Text size " + pageLettersFont.getSize());
        System.out.println("fm.getHeight() " + fm.getHeight());

        return fm.getHeight();
    }

    //Функция возвращает ширину строки
    private int getPrintTextWidth(String measuredString) {
        //FontMetrics fm = this.getFontMetrics(this.getFont()); //Для определения размера символов в пикселях
        FontMetrics fm = this.getFontMetrics(pageLettersFont); //Для определения размера символов в пикселях
        return fm.stringWidth(measuredString);
    }

    //Функция выделяет из текста строку
    String getLineFromArray(String[] wordsArray, int placeForPrintWidth) {
        String returnStr = "";
        int j = numPageWords;

        if (j <= wordsArray.length - 1 && getPrintTextWidth(wordsArray[j]) > placeForPrintWidth) {
            for (int i = insideWordIndex; i < wordsArray[j].length(); i++) {
                if (getPrintTextWidth(returnStr + wordsArray[j].charAt(i)) > placeForPrintWidth) {
                    insideWordIndex = i;
                    return returnStr;
                } else {
                    insideWordIndex = i;
                    returnStr = returnStr + wordsArray[j].charAt(i);

                }
            }
            insideWordIndex = 0;
            j++;
        }

        while (j < wordsArray.length) {
            if (getPrintTextWidth(returnStr + " " + wordsArray[j]) <= placeForPrintWidth) {
                returnStr = returnStr + " " + wordsArray[j];
                j++;
                if (j < wordsArray.length && wordsArray[j].length() == 0) {
                    break;
                }
            } else {
                break;
            }
        }
        numPageWords = j;

        return returnStr;
    }


    //Функция для формирования страницы
    LinkedList makePage(String[] array) {
        pageText = new LinkedList();


        /*if(pageLettersFont.getSize() == 14) {
            numLines = 27;
        } else if(pageLettersFont.getSize() == 15) {
            numLines = 27;
        } else if(pageLettersFont.getSize() == 16) {
            numLines = 27;
        } else if(pageLettersFont.getSize() == 17) {
            numLines = 27;
        } else if(pageLettersFont.getSize() == 18) {
            numLines = 27;
        } else if(pageLettersFont.getSize() == 19) {
            numLines = 27;
        } else if(pageLettersFont.getSize() == 20) {
            numLines = 27;
        } else if(pageLettersFont.getSize() == 21) {
            numLines = 27;
        } else if(pageLettersFont.getSize() == 22) {
            numLines = 27;
        } else if(pageLettersFont.getSize() == 23) {
            numLines = 27;
        } else if(pageLettersFont.getSize() == 24) {
            numLines = 27;
        }*/

        //numLines = (this.getHeight() - this.topIndent - this.bottomIndent) / this.getPrintTextHeight();

        String strLine;

        for (int i = 0; i < numLines; i++) {

            strLine = getLineFromArray(array, this.getWidth() - leftIndent - rightIndent);
            pageText.add(strLine);

        }

        return pageText;
    }

    //Функция определяет кодировку текстового
    // файла и читает содержащийся в нём текст
    public static String readText(String sPath) throws IOException {
        //Определение кодировки файла
        FileInputStream fis = new FileInputStream(sPath);
        byte[] buf = new byte[4096];
        //1
        UniversalDetector detector = new UniversalDetector(null);
        //2
        int nread;
        while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
            detector.handleData(buf, 0, nread);
        }
        //3
        detector.dataEnd();
        //4
        String encoding = detector.getDetectedCharset();
        if (encoding != null) {
            System.out.println("Detected encoding = " + encoding);
        } else {
            System.out.println("No encoding detected.");
            encoding = "UTF-8";
            System.out.println("So encoding = " + encoding);
        }
        //5
        detector.reset();
        //Получение текста из файла
        FileInputStream fileInputStream = new FileInputStream(sPath);
        FileChannel filechannel = fileInputStream.getChannel();

        int size;
        byte[] bytes;//байтовый массив, с которым будет ассоциирован буфер
        ByteBuffer buffer;

        try {
            size = (int) filechannel.size();//число байт в файле
            bytes = new byte[size];//создаем байтовый массив, с которым будет ассоциирован буфер
            buffer = ByteBuffer.wrap(bytes);//ассоциирует буфер с байтовым массивом bytes.Текущая позиция (указатель буфера)=0.
            filechannel.read(buffer);//заполняет буфер данными из файла.Текущая позиция (указатель буфера)=size.
            buffer.flip();//устанавливает текущую позицию (указатель буфера) в 0. После чего буфер готов к считыванию из него данных
        } finally {
            fileInputStream.close(); //автоматически вызывает filechannel.close();
        }
        CharBuffer ch = Charset.forName(encoding).decode(buffer);//декодируем текст в формат Java (UTF-8).

        return ch.toString();
    }

    //Функция установки цвета шрифта
    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }


    static int getNumPageWords() {
        return numPageWords;
    }

    static void setNumPageWords(int npw) {
        ImageLabel.numPageWords = npw;
    }

    @Override
    public void paint(Graphics g) { // Переопределяем метод paint

        Image bgImg = new ImageIcon(this.imgagePathURL).getImage();
        g.drawImage(bgImg, 0, 0, this);

        g.setColor(textColor);
        //System.out.println("font size: " + g.getFont().getSize());
        //g.getFont().getSize()

        for (int i = 0; i < vectorPage.size(); i++) {
            //Вывод текста на страницу
            g.setFont(pageLettersFont);
            g.drawString(vectorPage.get(i).toString(), leftIndent, topIndent + this.getFontMetrics(this.getFont()).getHeight() * (i + 1));
            //Нумерация страниц
            g.setFont(pageNumberFont);
            if (this.getX() == 0) {
                g.drawString("" + MyFrame.getPageNumber(), this.getWidth() / 2, this.getHeight() - bottomIndent + bottomIndent / 2);
            } else {
                g.drawString("" + (MyFrame.getPageNumber() + 1), this.getWidth() / 2, this.getHeight() - bottomIndent + bottomIndent / 2);
            }
            g.setFont(pageLettersFont);
        }
    }

    public void makeTextBigger() {
        try {
            int oldTextSize = pageLettersFont.getSize();
            if (oldTextSize == 24) {
                return;
            }
            pageLettersFont = new Font(this.getFont().getName(), Font.PLAIN, ++oldTextSize);
        } catch (Exception e) {
        }
    }

    public void makeTextSmaller() {
        try {
            int oldTextSize = pageLettersFont.getSize();
            if (oldTextSize == 14) {
                return;
            }
            pageLettersFont = new Font(this.getFont().getName(), Font.PLAIN, --oldTextSize);
        } catch (Exception e) {
        }
    }


}
