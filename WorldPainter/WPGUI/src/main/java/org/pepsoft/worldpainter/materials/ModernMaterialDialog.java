package org.pepsoft.worldpainter.materials;

import org.pepsoft.minecraft.Material;
// import org.pepsoft.util.swing.ListSelectionDialog;
import org.pepsoft.worldpainter.ColourScheme;
import org.pepsoft.worldpainter.MixedMaterial;
import org.pepsoft.worldpainter.Platform;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ModernMaterialDialog extends JDialog {
    private final Platform platform;
    private final boolean extendedBlockIds;
    private final ColourScheme colourScheme;
    private MixedMaterial selectedMaterial;
    private JPanel gridPanel;
    private JTextField searchField;
    private JDialog parent;
    private Properties ruNames;

    public ModernMaterialDialog(Window owner, Platform platform, MixedMaterial initialMaterial, boolean extendedBlockIds, ColourScheme colourScheme) {
        super(owner, "Select Material", ModalityType.APPLICATION_MODAL);
        this.platform = platform;
        this.extendedBlockIds = extendedBlockIds;
        this.colourScheme = colourScheme;
        this.selectedMaterial = initialMaterial;

        loadRuNames();

        setLayout(new BorderLayout());
        setSize(800, 600);
        setLocationRelativeTo(owner);

        // Top bar: Search
        JPanel topPanel = new JPanel(new BorderLayout());
        searchField = new JTextField();
        searchField.putClientProperty("JTextField.placeholderText", "Поиск блока...");
        searchField.addActionListener(e -> filterBlocks(searchField.getText()));
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterBlocks(searchField.getText()); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterBlocks(searchField.getText()); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterBlocks(searchField.getText()); }
        });
        topPanel.add(new JLabel("Поиск: "), BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Center: Grid of blocks
        gridPanel = new JPanel(new GridLayout(0, 5, 10, 10)); // 5 columns, auto rows
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(e -> {
            selectedMaterial = null;
            dispose();
        });
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Populate grid
        filterBlocks("");
    }

    private void loadRuNames() {
        ruNames = new Properties();
        try {
            ruNames.load(getClass().getResourceAsStream("/org/pepsoft/worldpainter/materials/minecraft_blocks_ru.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filterBlocks(String query) {
        gridPanel.removeAll();
        String q = query.toLowerCase();

        // Optimize: Use distinct names only to avoid duplicates for every block state
        Set<String> distinctNames = new HashSet<>();
        List<Material> materials = new ArrayList<>();

        for (Material m : Material.getAllMaterials()) {
            if (m.name != null && !m.name.contains("air") && distinctNames.add(m.name)) {
                materials.add(m);
            }
        }

        materials.sort(Comparator.comparing(m -> m.name));

        int limit = 500; // Limit displayed blocks to avoid lag
        int count = 0;

        for (Material m : materials) {
            String ruName = ruNames.getProperty(m.simpleName.toLowerCase(), m.name);
            if (ruName.toLowerCase().contains(q) || m.name.toLowerCase().contains(q)) {
                JButton blockButton = createBlockButton(m, ruName);
                gridPanel.add(blockButton);
                count++;
                if (count >= limit) break;
            }
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JButton createBlockButton(Material m, String name) {
        JButton button = new JButton(name);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);

        // Generate a colored icon based on the colour scheme
        int color = colourScheme.getColour(m);
        // If color is transparent/0 (unmapped), use a default gray
        if (color == 0) color = 0xFF808080;

        BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(new Color(color));
        g2.fillRect(0, 0, 64, 64);

        // Add some noise or shading to look like a block
        g2.setColor(new Color(0, 0, 0, 30));
        g2.drawRect(0, 0, 63, 63);

        g2.dispose();

        button.setIcon(new ImageIcon(img));
        button.setPreferredSize(new Dimension(100, 100));

        button.addActionListener(e -> {
            selectedMaterial = MixedMaterial.create(platform, m);
            dispose();
        });

        return button;
    }

    public MixedMaterial getSelectedMaterial() {
        return selectedMaterial;
    }

    public boolean isCancelled() {
        return selectedMaterial == null;
    }
}
