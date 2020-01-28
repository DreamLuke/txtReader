/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myreaderalpha;

/**
 * @author Vadim Monakhov
 */

import javax.swing.filechooser.FileView;
import java.io.*;

public class SimpleFileFilter extends javax.swing.filechooser.FileFilter {

    String ext;

    SimpleFileFilter(String ext) {
        this.ext = ext;
    }

    public boolean accept(File f) {
        if (f == null) {
            return false;
        }
        if (f.isDirectory()) {
            return true;
        } else {
            return (f.getName().endsWith(ext));
        }
    }

    /**
     * Описание фильтра, возникающее в строке фильтра
     * @see FileView#getName
     */
    public String getDescription() {
        return "Text files (.txt)";
    }
}
