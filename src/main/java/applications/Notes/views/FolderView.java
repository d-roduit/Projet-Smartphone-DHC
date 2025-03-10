package applications.Notes.views;

import applications.Notes.models.FolderModel;
import applications.Notes.models.NoteModel;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>FolderPanel is the class that represents the notes list of a folder.</b>
 *
 * @see JPanel
 *
 * @author Cathy Gay
 */
public class FolderView extends JPanel {

    private final FolderModel folderModel;
    private Color mainTextColor = Color.WHITE;
    private Color secondaryTextColor = new Color(217, 169, 25);
    private JButton addNoteButton;
    private JButton deleteFolderButton;
    private JButton returnToFolderListButton;
    private JLabel folderTitleLabel;
    private Map<JPanel, NoteModel> panelNoteModelMap = new HashMap<>();

    /**
     * FolderPanel constructor.
     */
    public FolderView(FolderModel folderModel) {
        this.folderModel = folderModel;

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JPanel topLanePanel = createTopLanePanel();
        JScrollPane notesListScrollPane = createNotesListScrollPane();
        JPanel botLanePanel = createBotLanePanel();

        add(topLanePanel, BorderLayout.NORTH);
        add(notesListScrollPane, BorderLayout.CENTER);
        add(botLanePanel, BorderLayout.SOUTH);
    }

    private JPanel createBotLanePanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        int nbNotes = createNotesListPanel().getComponentCount() / 2;
        JLabel nbNotesLabel = new JLabel(nbNotes +" notes");
        nbNotesLabel.setForeground(mainTextColor);

        panel.add(nbNotesLabel);

        return panel;
    }

    private JPanel createTopLanePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);

        Icon goBackIcon = IconFontSwing.buildIcon(FontAwesome.ANGLE_LEFT, 22, secondaryTextColor);

        returnToFolderListButton = new JButton("Folders");
        returnToFolderListButton.setIcon(goBackIcon);
        returnToFolderListButton.setContentAreaFilled(false);
        returnToFolderListButton.setFocusPainted(false);
        returnToFolderListButton.setBorderPainted(false);
        returnToFolderListButton.setForeground(secondaryTextColor);
        returnToFolderListButton.setFont(new Font("Arial", Font.BOLD, 17));
        returnToFolderListButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        returnToFolderListButton.setToolTipText("Go back to folders");

        folderTitleLabel = new JLabel(folderModel.getFolderTitle());
        folderTitleLabel.setForeground(mainTextColor);
        folderTitleLabel.setHorizontalAlignment(JLabel.CENTER);
        folderTitleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        folderTitleLabel.setCursor(new Cursor(Cursor.TEXT_CURSOR));

        Icon addNoteIcon = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 28, secondaryTextColor);

        Icon deleteFolderIcon = IconFontSwing.buildIcon(FontAwesome.TRASH_O, 24, secondaryTextColor);

        addNoteButton = new JButton(addNoteIcon);
        addNoteButton.setFocusPainted(false);
        addNoteButton.setBorderPainted(false);
        addNoteButton.setContentAreaFilled(false);
        addNoteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addNoteButton.setToolTipText("Add a note");

        deleteFolderButton = new JButton(deleteFolderIcon);
        deleteFolderButton.setFocusPainted(false);
        deleteFolderButton.setBorderPainted(false);
        deleteFolderButton.setContentAreaFilled(false);
        deleteFolderButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteFolderButton.setToolTipText("Delete this folder");

        JPanel eastPanel = new JPanel();
        eastPanel.setOpaque(false);

        eastPanel.add(addNoteButton);
        eastPanel.add(deleteFolderButton);

        JLabel folderCreationDateLabel = new JLabel("       créé le "+folderModel.getFolderCreationDate());
        folderCreationDateLabel.setFont(new Font("Arial", Font.ITALIC, 12));

        panel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.NORTH);
        panel.add(returnToFolderListButton, BorderLayout.WEST);
        panel.add(folderTitleLabel, BorderLayout.CENTER);
        panel.add(eastPanel, BorderLayout.EAST);
        panel.add(folderCreationDateLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JScrollPane createNotesListScrollPane() {
        JScrollPane scrollPane = new JScrollPane(createNotesListPanel(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(10); //change the scrolling speed

        return scrollPane;
    }

    private JPanel createNotesListPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        try {

            List<NoteModel> noteModels = folderModel.getNotes();

            for (NoteModel noteModel : noteModels) {
                JPanel notePanel = createNotePanel(noteModel);
                panel.add(notePanel);
                panelNoteModelMap.put(notePanel, noteModel);
                JSeparator separator = new JSeparator();
                separator.setPreferredSize(new Dimension(342, 2));
                separator.setMaximumSize(new Dimension(separator.getPreferredSize()));
                separator.setBackground(new Color(49, 49, 49));
                separator.setForeground(new Color(49, 49, 49));
                panel.add(separator);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return panel;
    }

    private JPanel createNotePanel(NoteModel noteModel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(320, 40));
        panel.setMaximumSize(new Dimension(panel.getPreferredSize()));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel noteTitle = new JLabel(noteModel.getNoteTitle());
        noteTitle.setForeground(mainTextColor);

        Icon goToNoteIcon = IconFontSwing.buildIcon(FontAwesome.ANGLE_RIGHT, 20, mainTextColor);

        JLabel goToNoteLabel = new JLabel(goToNoteIcon);

        panel.add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.WEST);
        panel.add(noteTitle, BorderLayout.CENTER);
        panel.add(goToNoteLabel, BorderLayout.EAST);

        return panel;
    }

    public JButton getAddNoteButton() {
        return addNoteButton;
    }

    public JButton getDeleteFolderButton() {
        return deleteFolderButton;
    }

    public JButton getReturnToFolderListButton() {
        return returnToFolderListButton;
    }

    public JLabel getFolderTitleLabel() {
        return folderTitleLabel;
    }

    public Map<JPanel, NoteModel> getPanelNoteModelMap() {
        return panelNoteModelMap;
    }
}
