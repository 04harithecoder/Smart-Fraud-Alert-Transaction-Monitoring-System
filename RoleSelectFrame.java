import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class RoleSelectFrame extends JFrame {

    // ── Colour Palette ───────────────────────────────────────────────────
    private static final Color MINT     = new Color(0xBB, 0xD2, 0xC5);
    private static final Color SLATE    = new Color(0x53, 0x69, 0x76);
    private static final Color SLATE_DK = new Color(0x3A, 0x4E, 0x58);
    private static final Color WHITE    = Color.WHITE;
    private static final Color BG       = new Color(0xF2, 0xF7, 0xF5);
    private static final Color TEXT_DARK= new Color(0x1C, 0x2B, 0x33);
    private static final Color TEXT_GRAY= new Color(0x7A, 0x90, 0x9C);
    private static final Color BORDER   = new Color(0xDD, 0xE8, 0xE4);

    public RoleSelectFrame() {
        setTitle("SreeHari Transaction Monitering System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 480);
        setLocationRelativeTo(null);
        setResizable(true);

        add(buildRolePanel());
        setVisible(true);
    }

    // ════════════════════════════════════════════════════════════════════
    //  ROOT PANEL
    // ════════════════════════════════════════════════════════════════════
    private JPanel buildRolePanel() {
        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background gradient
                GradientPaint gp = new GradientPaint(0, 0, BG, getWidth(), getHeight(), MINT);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Decorative circles — fill the blank space
                g2.setColor(new Color(0x53, 0x69, 0x76, 22));
                g2.fillOval(-80, -80, 300, 300);
                g2.fillOval(getWidth() - 160, getHeight() - 160, 280, 280);
                g2.setColor(new Color(0x53, 0x69, 0x76, 14));
                g2.fillOval(getWidth() - 80, 10, 180, 180);
                g2.fillOval(-40, getHeight() - 180, 200, 200);
                g2.fillOval(getWidth()/2 - 60, getHeight() - 80, 140, 140);
                g2.setColor(new Color(0xFF, 0xFF, 0xFF, 30));
                g2.fillOval(getWidth()/2 - 200, -40, 160, 160);

                g2.dispose();
            }
        };

        // ── TOP BAR ──────────────────────────────────────────────────────
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory.createEmptyBorder(18, 28, 0, 28));

        JLabel appTitle = new JLabel("SreeHari Transaction Monitoring System");
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        appTitle.setForeground(SLATE_DK);

        JLabel dateLabel = new JLabel(java.time.LocalDate.now().toString());
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(TEXT_GRAY);

        topBar.add(appTitle, BorderLayout.WEST);
        topBar.add(dateLabel, BorderLayout.EAST);

        // ── CENTER CONTENT ────────────────────────────────────────────────
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel("Welcome!");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(TEXT_DARK);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Please select your role to continue");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sub.setForeground(TEXT_GRAY);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Role Cards
        JPanel cardsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 28, 0));
        cardsRow.setOpaque(false);
        cardsRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel userCard  = buildRoleCard("👤", "User",  "View transactions\nand account details");
        JPanel adminCard = buildRoleCard("🛡", "Admin", "Manage system\nand monitor alerts");

        userCard.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { dispose(); new AuthFrame("USER");  }
            public void mouseEntered(MouseEvent e) { hoverCard(userCard,  true);  }
            public void mouseExited (MouseEvent e) { hoverCard(userCard,  false); }
        });
        adminCard.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { dispose(); new AuthFrame("ADMIN"); }
            public void mouseEntered(MouseEvent e) { hoverCard(adminCard, true);  }
            public void mouseExited (MouseEvent e) { hoverCard(adminCard, false); }
        });

        cardsRow.add(userCard);
        cardsRow.add(adminCard);

        // Stats strip
        JPanel statsStrip = buildStatsStrip();
        statsStrip.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Feature highlights
        JPanel features = buildFeatureHighlights();
        features.setAlignmentX(Component.CENTER_ALIGNMENT);

        center.add(Box.createVerticalStrut(30));
        center.add(heading);
        center.add(Box.createVerticalStrut(6));
        center.add(sub);
        center.add(Box.createVerticalStrut(36));
        center.add(cardsRow);
        center.add(Box.createVerticalStrut(36));
        center.add(statsStrip);
        center.add(Box.createVerticalStrut(28));
        center.add(features);

        // ── FOOTER ────────────────────────────────────────────────────────
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 28, 14, 28));

        JLabel footerLeft = new JLabel("© 2024 SreeHari Systems");
        footerLeft.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLeft.setForeground(new Color(0x90, 0xA8, 0xB0));

        JLabel footerRight = new JLabel("v1.0.0  |  Secure & Monitored");
        footerRight.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerRight.setForeground(new Color(0x90, 0xA8, 0xB0));

        footer.add(footerLeft,  BorderLayout.WEST);
        footer.add(footerRight, BorderLayout.EAST);

        root.add(topBar,  BorderLayout.NORTH);
        root.add(center,  BorderLayout.CENTER);
        root.add(footer,  BorderLayout.SOUTH);

        return root;
    }

    // ════════════════════════════════════════════════════════════════════
    //  STATS STRIP
    // ════════════════════════════════════════════════════════════════════
    private JPanel buildStatsStrip() {
        JPanel strip = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        strip.setOpaque(false);

        String[][] stats = {
            {"12,483", "Total Transactions"},
            {"27",     "Fraud Alerts Today"},
            {"₹4.8Cr", "Amount Monitored"},
        };

        for (String[] s : stats) {
            JPanel tile = new JPanel() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(0xFF, 0xFF, 0xFF, 160));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.setColor(BORDER);
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                    g2.dispose();
                }
            };
            tile.setOpaque(false);
            tile.setLayout(new BoxLayout(tile, BoxLayout.Y_AXIS));
            tile.setPreferredSize(new Dimension(150, 62));
            tile.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

            JLabel val = new JLabel(s[0], SwingConstants.CENTER);
            val.setFont(new Font("Segoe UI", Font.BOLD, 20));
            val.setForeground(SLATE_DK);
            val.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel lbl = new JLabel(s[1], SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lbl.setForeground(TEXT_GRAY);
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

            tile.add(val);
            tile.add(Box.createVerticalStrut(3));
            tile.add(lbl);
            strip.add(tile);
        }

        return strip;
    }

    // ════════════════════════════════════════════════════════════════════
    //  FEATURE HIGHLIGHTS
    // ════════════════════════════════════════════════════════════════════
    private JPanel buildFeatureHighlights() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 36, 0));
        row.setOpaque(false);

        String[][] features = {
            {"🔒", "Secure Login"},
            {"⚡", "Real-Time Alerts"},
            {"📊", "Smart Analytics"},
        };

        for (String[] f : features) {
            JPanel item = new JPanel();
            item.setOpaque(false);
            item.setLayout(new BoxLayout(item, BoxLayout.Y_AXIS));

            JLabel icon = new JLabel(f[0], SwingConstants.CENTER);
            icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
            icon.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel lbl = new JLabel(f[1], SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lbl.setForeground(SLATE);
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

            item.add(icon);
            item.add(Box.createVerticalStrut(4));
            item.add(lbl);
            row.add(item);
        }

        return row;
    }

    // ════════════════════════════════════════════════════════════════════
    //  ROLE CARD
    // ════════════════════════════════════════════════════════════════════
    private JPanel buildRoleCard(String emoji, String role, String desc) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 18, 18);
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(WHITE);
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(170, 200));
        card.setBorder(BorderFactory.createEmptyBorder(28, 20, 28, 20));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel emojiCircle = new JLabel(emoji, SwingConstants.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MINT);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        emojiCircle.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        emojiCircle.setPreferredSize(new Dimension(64, 64));
        emojiCircle.setMaximumSize(new Dimension(64, 64));
        emojiCircle.setOpaque(false);
        emojiCircle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel roleLbl = new JLabel(role, SwingConstants.CENTER);
        roleLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        roleLbl.setForeground(TEXT_DARK);
        roleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(emojiCircle);
        card.add(Box.createVerticalStrut(14));
        card.add(roleLbl);
        card.add(Box.createVerticalStrut(8));

        for (String l : desc.split("\n")) {
            JLabel d = new JLabel(l, SwingConstants.CENTER);
            d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            d.setForeground(TEXT_GRAY);
            d.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(d);
        }

        return card;
    }

    private void hoverCard(JPanel card, boolean hover) {
        card.setBackground(hover ? new Color(0xF0, 0xF8, 0xF5) : WHITE);
        card.repaint();
    }

    // ════════════════════════════════════════════════════════════════════
    //  MAIN
    // ════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(RoleSelectFrame::new);
    }
}