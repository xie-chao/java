package com.calix.tools.widget;

import com.calix.tools.param.GlobalConfig;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by calix on 16-11-11
 * 笔记
 */
class NotePanel extends JPanel {

    private static NotePanel tipPanel;
    private JTextArea noteContent;
    private JComboBox<File> notes;
    private Component mainComponent;
    private boolean lastSelectModify = false;
    private File lastSelectFile = null;
    private final Map<String, File> modifyFiles = new HashMap<>();

    private NotePanel(Component mainComponent) {
        this.mainComponent = mainComponent;
        this.setLayout(new BorderLayout());
        this.add(getNoteToolbar(), BorderLayout.NORTH);
        this.add(getNoteArea(), BorderLayout.CENTER);
        loadNoteFile();
    }

    static NotePanel getNotePanel(Component mainComponent) {
        if (tipPanel == null) {
            tipPanel = new NotePanel(mainComponent);
        }
        return tipPanel;
    }

    private void loadNoteFile() {
        notes.addItem(new File("/home/calix/share/temp.sql"));
        notes.setSelectedIndex(-1);
        notes.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    loadFile((File) e.getItem());
                }
            }
        });
    }

    private JToolBar getNoteToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        notes = new JComboBox<>();
        notes.setIgnoreRepaint(true);
        toolBar.add(notes);
        toolBar.addSeparator();
        JButton addNote = new JButton("打开");
        toolBar.add(addNote);
        addNote.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SqlFileChooser chooser = new SqlFileChooser();
                int result = chooser.showOpenDialog(mainComponent);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File[] files = chooser.getSelectedFiles();
                    if (files.length > 0) {
                        File[] existFile = new File[notes.getItemCount()];
                        for (int i = 0; i < existFile.length; i++) {
                            existFile[i] = notes.getItemAt(i);
                        }
                        for (File f : files) {
                            boolean exist = false;
                            for (File ef : existFile) {
                                if (f.getAbsolutePath().equals(ef.getAbsolutePath())) {
                                    exist = true;
                                    break;
                                }
                            }
                            if (exist) continue;
                            notes.addItem(f);
                        }
                    }
                    notes.setSelectedIndex(notes.getItemCount() - 1);
                }
            }
        });

        return toolBar;
    }

    private JScrollPane getNoteArea() {
        noteContent = new JTextArea();
        if (GlobalConfig.GLOBAL_FONT != null) {
            noteContent.setFont(GlobalConfig.GLOBAL_FONT);
        }
        noteContent.setTabSize(4);
        noteContent.setLineWrap(false);
        noteContent.setMargin(new Insets(2, 2, 2, 2));
        noteContent.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                lastSelectModify = true;
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                lastSelectModify = true;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        return new JScrollPane(noteContent);
    }

    private class SqlFileChooser extends JFileChooser {
        SqlFileChooser() {
            setFileSelectionMode(FILES_ONLY);
//            setMultiSelectionEnabled(true);
            this.setDialogTitle("选择");
            this.setFileFilter(new SqlFileFilter());
        }
    }

    /**
     * 加载文件
     */
    private void loadFile(File file) {
        saveTempEdit();
        noteContent.setText(null);
        BufferedReader reader = null;
        try {
            if (modifyFiles.get(file.getAbsolutePath()) != null) {
                reader = new BufferedReader(new FileReader(modifyFiles.get(file.getAbsolutePath())));
            } else {
                reader = new BufferedReader(new FileReader(file));
            }
            String line;
            while ((line = reader.readLine()) != null) {
                noteContent.append(line + GlobalConfig.LINE_SEPARATOR);
            }
            lastSelectFile = file;
            lastSelectModify = false;
        } catch (IOException e) {
            LogPanel.append(e.getStackTrace());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LogPanel.append(e.getStackTrace());
                }
            }
        }
    }

    /**
     * 保存临时修改
     */
    private void saveTempEdit() {
        if (!lastSelectModify) {
            return;
        }
        File temp;
        if ((temp = modifyFiles.get(lastSelectFile.getAbsolutePath())) == null) {
            String projectDir = GlobalConfig.getGlobalConfig().PROJECT_DIR;
            temp = new File(projectDir + "~temp_" + System.currentTimeMillis());
        }
        changeToFile(temp);
        modifyFiles.put(lastSelectFile.getAbsolutePath(), temp);
    }

    /**
     * 文件选择过滤器
     */
    private class SqlFileFilter extends FileFilter {
        private final String suffix[] = {"sql", "txt"};

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            for (String s : suffix) {
                if (f.getName().endsWith(s)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getDescription() {
            return "*.sql/*.txt";
        }
    }

    /**
     * 打开的文件是否被修改过
     */
    private boolean fileIsModifyed() {
        return modifyFiles.size() > 0 || lastSelectModify;
    }

    void saveChanges() {
        if (!fileIsModifyed()) {
            return;
        }
        int reuslt = JOptionPane.showConfirmDialog(mainComponent, "请选择", "打开的文件已经修改，是否保存修改？", JOptionPane.OK_CANCEL_OPTION);
        if (reuslt == JOptionPane.OK_OPTION) {
            if (modifyFiles.size() == 0) {
                changeToFile((File)notes.getSelectedItem());
                return;
            }
            for (Map.Entry<String, File> entry : modifyFiles.entrySet()) {
                File temp = entry.getValue();
                copyFile(new File(entry.getKey()), temp);
            }
        }
        for (File f : modifyFiles.values()) {
            if (f.delete()) {
                LogPanel.append("缓存文件删除失败！");
            }
        }
    }

    private void changeToFile(File file) {
        int length = noteContent.getDocument().getLength();
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            int start = 0, size = 1024;
            while (start < length) {
                String s = noteContent.getDocument().getText(start, Math.min(size, length - start));
                start += size;
                writer.write(s);
            }
            writer.flush();
        } catch (BadLocationException e) {
            LogPanel.append(e.getStackTrace());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    LogPanel.append(e.getStackTrace());
                }
            }
        }
    }

    private void copyFile(File from, File to) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(from);
            out = new FileOutputStream(to);
            int i;
            byte[] cache = new byte[1024];
            while ((i = in.read(cache)) > 0) {
                out.write(cache, 0, i);
            }
            out.flush();
        } catch (IOException e) {
            LogPanel.append(e.getStackTrace());
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
