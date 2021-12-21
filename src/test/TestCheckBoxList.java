package test;

import java.awt.*;
import java.awt.event.*;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

public class TestCheckBoxList extends JList {

    DefaultListModel listModel = new DefaultListModel();

    public TestCheckBoxList() {
        setModel(listModel);
        setCellRenderer(new CellRenderer());
        System.out.println("SET CELL RENDERER");
        addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                int index = locationToIndex(e.getPoint());

                if (index != -1) {
                    JCheckBox checkbox = (JCheckBox) getModel().getElementAt(index);
                    checkbox.setSelected(!checkbox.isSelected());
                    repaint();
                }
            }
        });
    }

    protected class CellRenderer implements ListCellRenderer {

        public CellRenderer() {
            System.out.println("CREATED RENDERER");
        }

        public Component getListCellRendererComponent(
                JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            JCheckBox checkbox = (JCheckBox) value;
            checkbox.setBackground(isSelected ? getSelectionBackground() : getBackground());
            checkbox.setForeground(isSelected ? getSelectionForeground() : getForeground());
            checkbox.setEnabled(isEnabled());
            checkbox.setFont(getFont());
            checkbox.setFocusPainted(false);
            checkbox.setBorderPainted(true);

            System.out.println("RETURNING A CHECKBOX");
            return checkbox;
        }
    }

    public static void main(String args[]) {

        JFrame frame = new JFrame("JList CheckBox Example");
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        TestCheckBoxList myList = new TestCheckBoxList();

        for (int i = 0; i < 10; i++) {
            myList.add(new JCheckBox("Box" + Integer.toString(i)));
            System.out.println("added BOX list ");
        }

        System.out.println("MODEL SIZE:" + ((DefaultListModel) myList.getModel()).size());
        panel.add(new JScrollPane(myList));

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
