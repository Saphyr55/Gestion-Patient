package windows.technician;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import hopital.Consultation;
import hopital.patient.Patient;

class CheckBoxList<E extends CheckBoxNode> extends JList<E> {

    private CheckBoxCellRenderer<E> renderer;
    private CheckBoxNode node;
    private static Patient patient;
    private static Consultation consultation;
    private static File appareillageFile;
    private static JSONObject jsonObjectAppareillage;
    private static ArrayList<Boolean> listCheck = new ArrayList<>();
    private static Map<String, Boolean> mapCheck = FrameTechnician.getAppareillageMap();

    protected CheckBoxList(ListModel<E> model) {
        super(model);
        patient = FrameTechnician.getCurrentPatient();
        consultation = FrameTechnician.getCurrentConsultation();
        appareillageFile = FrameTechnician.getAppareillageFile();
        for (Entry<String, Boolean> element : mapCheck.entrySet()) {
            listCheck.add(element.getValue());
        }
        updateAppareillage();
    }

    @Override
    public void updateUI() {
        setForeground(null);
        setBackground(null);
        setSelectionForeground(null);
        setSelectionBackground(null);
        removeMouseListener(renderer);
        removeMouseMotionListener(renderer);
        super.updateUI();
        renderer = new CheckBoxCellRenderer<>();
        setCellRenderer(renderer);
        addMouseListener(renderer);
        addMouseMotionListener(renderer);
        putClientProperty("List.isFileList", Boolean.TRUE);
    }
    // @see SwingUtilities2.pointOutsidePrefSize(...)

    private boolean pointOutsidePrefSize(Point p) {
        int i = locationToIndex(p);
        E cbn = getModel().getElementAt(i);
        Component c = getCellRenderer().getListCellRendererComponent(this, cbn, i, false, false);
        Rectangle rect = getCellBounds(i, i);
        rect.width = c.getPreferredSize().width;
        return i < 0 || !rect.contains(p);
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        if (!pointOutsidePrefSize(e.getPoint())) {
            super.processMouseEvent(e);
        }
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent e) {
        if (pointOutsidePrefSize(e.getPoint())) {
            MouseEvent ev = new MouseEvent(
                    e.getComponent(), MouseEvent.MOUSE_EXITED, e.getWhen(),
                    e.getModifiersEx(), e.getX(), e.getY(), e.getXOnScreen(), e.getYOnScreen(),
                    e.getClickCount(), e.isPopupTrigger(), MouseEvent.NOBUTTON);
            super.processMouseEvent(ev);
        } else {
            super.processMouseMotionEvent(e);
        }
    }

    class CheckBoxCellRenderer<E extends CheckBoxNode> extends MouseAdapter implements ListCellRenderer<E> {
        private final JCheckBox checkBox = new JCheckBox();
        private int rollOverRowIndex = -1;

        @Override
        public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected,
                boolean cellHasFocus) {
            checkBox.setOpaque(true);
            if (isSelected) {
                checkBox.setBackground(list.getSelectionBackground());
                checkBox.setForeground(list.getSelectionForeground());
            } else {
                checkBox.setBackground(list.getBackground());
                checkBox.setForeground(list.getForeground());
            }
            checkBox.setSelected(value.selected);
            checkBox.getModel().setRollover(index == rollOverRowIndex);
            checkBox.setText(value.text);
            return checkBox;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (rollOverRowIndex >= 0) {
                JList<?> l = (JList<?>) e.getComponent();
                l.repaint(l.getCellBounds(rollOverRowIndex, rollOverRowIndex));
                rollOverRowIndex = -1;
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                JList<?> l = (JList<?>) e.getComponent();
                Point p = e.getPoint();
                int index = l.locationToIndex(p);
                if (index >= 0) {
                    @SuppressWarnings("unchecked")
                    DefaultListModel<CheckBoxNode> model = (DefaultListModel<CheckBoxNode>) l.getModel();
                    node = model.get(index);
                    mapCheck.replace(node.text, node.selected, !node.selected);
                    model.set(index, new CheckBoxNode(node.text, !node.selected));
                    l.repaint(l.getCellBounds(index, index));
                    updateAppareillage();
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            JList<?> l = (JList<?>) e.getComponent();
            int index = l.locationToIndex(e.getPoint());
            if (index != rollOverRowIndex) {
                rollOverRowIndex = index;
                l.repaint();
            }
        }

    }

    private void updateAppareillage() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(
                        appareillageFile.getAbsolutePath()), "UTF-8"))) {
            JSONParser parser = new JSONParser();
            jsonObjectAppareillage = (JSONObject) parser.parse(reader);
            for (Entry<String, Boolean> element : mapCheck.entrySet()) {
                jsonObjectAppareillage.put(element.getKey(), element.getValue());
            }
            reader.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public CheckBoxCellRenderer<E> getRenderer() {
        return renderer;
    }

    public void setRenderer(CheckBoxCellRenderer<E> renderer) {
        this.renderer = renderer;
    }

    public CheckBoxNode getNode() {
        return node;
    }

    public void setNode(CheckBoxNode node) {
        this.node = node;
    }

    public static JSONObject getJsonObjectAppareillage() {
        return jsonObjectAppareillage;
    }

    public static void setJsonObjectAppareillage(JSONObject jsonObjectAppareillage) {
        CheckBoxList.jsonObjectAppareillage = jsonObjectAppareillage;
    }

}