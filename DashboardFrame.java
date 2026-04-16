import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class DashboardFrame extends JFrame {

    // ── Colour Palette ───────────────────────────────────────────────────
    private static final Color MINT       = new Color(0xBB, 0xD2, 0xC5);
    private static final Color SLATE      = new Color(0x53, 0x69, 0x76);
    private static final Color SLATE_DK   = new Color(0x3A, 0x4E, 0x58);
    private static final Color SLATE_LT   = new Color(0x6A, 0x88, 0x96);
    private static final Color WHITE      = Color.WHITE;
    private static final Color BG         = new Color(0xF2, 0xF7, 0xF5);
    private static final Color TEXT_DARK  = new Color(0x1C, 0x2B, 0x33);
    private static final Color TEXT_GRAY  = new Color(0x7A, 0x90, 0x9C);
    private static final Color BORDER_CLR = new Color(0xDD, 0xE8, 0xE4);
    private static final Color ALERT_RED  = new Color(0xD9, 0x53, 0x4F);
    private static final Color WARN_AMBER = new Color(0xE8, 0xA8, 0x30);
    private static final Color SUCCESS    = new Color(0x4C, 0xAF, 0x7A);

    private String activeNav = "Dashboard";
    private JPanel contentArea;
    private JLabel topBarTitle;
    private JPanel sidebar;

    // ── Constructor ──────────────────────────────────────────────────────
    public DashboardFrame() {
        setTitle("Smart Fraud Alert & Transaction Monitoring System – Admin");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1150, 700);
        setMinimumSize(new Dimension(900, 580));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        sidebar = buildSidebar();
        root.add(sidebar,  BorderLayout.WEST);
        root.add(buildMainArea(), BorderLayout.CENTER);

        add(root);
        setVisible(true);
    }

    // ════════════════════════════════════════════════════════════════════
    //  SIDEBAR
    // ════════════════════════════════════════════════════════════════════
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, SLATE_DK, 0, getHeight(), SLATE));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(0xFF, 0xFF, 0xFF, 12));
                g2.fillOval(-40, getHeight() - 200, 200, 200);
                g2.dispose();
            }
        };
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // ── Brand / Logo ─────────────────────────────────────────────────
        JPanel brand = new JPanel();
        brand.setOpaque(false);
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        brand.setBorder(BorderFactory.createEmptyBorder(26, 20, 18, 20));
        brand.setMaximumSize(new Dimension(220, 90));

        JLabel appName = new JLabel("FraudGuard");
        appName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        appName.setForeground(WHITE);
        appName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel adminBadge = new JLabel("ADMIN PANEL");
        adminBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        adminBadge.setForeground(MINT);
        adminBadge.setAlignmentX(Component.LEFT_ALIGNMENT);

        brand.add(appName);
        brand.add(Box.createVerticalStrut(4));
        brand.add(adminBadge);
        sidebar.add(brand);
        sidebar.add(sidebarDivider());
        sidebar.add(Box.createVerticalStrut(10));

        // ── Nav Items (Admin menus only) ─────────────────────────────────
        String[][] navItems = {
            {"Dashboard",            "📊"},
            {"Transaction Details",  "💳"},
            {"Fraud Alerts",         "🚨"},
            {"User Details",         "👥"},
        };

        for (String[] item : navItems) {
            sidebar.add(navItem(item[1], item[0]));
        }

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(sidebarDivider());
        sidebar.add(buildSidebarProfile());

        return sidebar;
    }

    private JPanel navItem(String emoji, String label) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0)) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (activeNav.equals(label)) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(0xFF, 0xFF, 0xFF, 22));
                    g2.fillRoundRect(8, 4, getWidth() - 16, getHeight() - 8, 10, 10);
                    g2.setColor(MINT);
                    g2.fillRoundRect(4, 8, 4, getHeight() - 16, 4, 4);
                    g2.dispose();
                }
            }
        };
        item.setOpaque(false);
        item.setPreferredSize(new Dimension(220, 44));
        item.setMaximumSize(new Dimension(220, 44));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel emojiLbl = new JLabel(emoji);
        emojiLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));

        JLabel textLbl = new JLabel(label) {
            @Override protected void paintComponent(Graphics g) {
                setFont(new Font("Segoe UI", activeNav.equals(label) ? Font.BOLD : Font.PLAIN, 13));
                setForeground(activeNav.equals(label) ? WHITE : new Color(0xCC, 0xDB, 0xD6));
                super.paintComponent(g);
            }
        };
        textLbl.setFont(new Font("Segoe UI", activeNav.equals(label) ? Font.BOLD : Font.PLAIN, 13));
        textLbl.setForeground(activeNav.equals(label) ? WHITE : new Color(0xCC, 0xDB, 0xD6));

        item.add(emojiLbl);
        item.add(textLbl);

        item.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                activeNav = label;
                refreshContent();
            }
            public void mouseEntered(MouseEvent e) {
                if (!activeNav.equals(label)) item.setOpaque(true);
                item.setBackground(new Color(0xFF, 0xFF, 0xFF, 10));
                item.repaint();
            }
            public void mouseExited(MouseEvent e) {
                item.setOpaque(false);
                item.repaint();
            }
        });

        return item;
    }

    private JPanel buildSidebarProfile() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 10));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(220, 60));

        JLabel avatar = new JLabel("AD") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MINT);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(SLATE_DK);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(34, 34));
        avatar.setOpaque(false);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel name = new JLabel("Admin");
        name.setFont(new Font("Segoe UI", Font.BOLD, 12));
        name.setForeground(WHITE);

        JLabel role = new JLabel("System Admin");
        role.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        role.setForeground(MINT);

        info.add(name);
        info.add(role);

        p.add(avatar);
        p.add(info);
        return p;
    }

    private JSeparator sidebarDivider() {
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(220, 1));
        sep.setForeground(new Color(0xFF, 0xFF, 0xFF, 30));
        return sep;
    }

    // ════════════════════════════════════════════════════════════════════
    //  MAIN AREA
    // ════════════════════════════════════════════════════════════════════
    private JPanel buildMainArea() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);
        main.add(buildTopBar(), BorderLayout.NORTH);

        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(BG);
        contentArea.add(buildDashboardContent(), BorderLayout.CENTER);

        main.add(contentArea, BorderLayout.CENTER);
        return main;
    }

    private void refreshContent() {
        contentArea.removeAll();
        String pageTitle;
        switch (activeNav) {
            case "Dashboard":
                contentArea.add(buildDashboardContent(), BorderLayout.CENTER);
                pageTitle = "Dashboard Overview";
                break;
            case "Transaction Details":
                contentArea.add(buildTransactionDetailsContent(), BorderLayout.CENTER);
                pageTitle = "Transaction Details";
                break;
            case "Fraud Alerts":
                contentArea.add(buildAlertsContent(), BorderLayout.CENTER);
                pageTitle = "Fraud Alerts";
                break;
            case "User Details":
                contentArea.add(buildUserDetailsContent(), BorderLayout.CENTER);
                pageTitle = "User Details";
                break;
            default:
                contentArea.add(buildPlaceholder(activeNav), BorderLayout.CENTER);
                pageTitle = activeNav;
                break;
        }
        topBarTitle.setText(pageTitle);
        contentArea.revalidate();
        contentArea.repaint();
        sidebar.repaint();
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    // ════════════════════════════════════════════════════════════════════
    //  TOP BAR
    // ════════════════════════════════════════════════════════════════════
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(WHITE);
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_CLR),
            BorderFactory.createEmptyBorder(14, 24, 14, 24)
        ));

        topBarTitle = new JLabel("Dashboard Overview");
        topBarTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        topBarTitle.setForeground(TEXT_DARK);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);

        JLabel bell = new JLabel("🔔 3");
        bell.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        bell.setForeground(ALERT_RED);
        bell.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bell.setBorder(BorderFactory.createCompoundBorder(
            new RoundBorder(ALERT_RED, 1, 16),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));

        JLabel date = new JLabel("Today: " + java.time.LocalDate.now());
        date.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        date.setForeground(TEXT_GRAY);

        JButton logout = smallButton("Logout");
        logout.addActionListener(e -> {
            dispose();
            // new AuthFrame(); // hook up when ready
        });

        right.add(date);
        right.add(bell);
        right.add(logout);

        bar.add(topBarTitle, BorderLayout.WEST);
        bar.add(right,       BorderLayout.EAST);
        return bar;
    }

    // ════════════════════════════════════════════════════════════════════
    //  DASHBOARD CONTENT  (Total / Active / Blocked Users + activity)
    // ════════════════════════════════════════════════════════════════════
    private JPanel buildDashboardContent() {
        JPanel panel = new JPanel();
        panel.setBackground(BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        // ── User Stat Cards ──────────────────────────────────────────────
        JPanel cards = new JPanel(new GridLayout(1, 3, 16, 0));
        cards.setOpaque(false);
        cards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        cards.add(statCard("Total Users",   "1,248", "All registered accounts",  SLATE,      "👥"));
        cards.add(statCard("Active Users",  "1,091", "↑ 24 joined this week",    SUCCESS,    "✅"));
        cards.add(statCard("Blocked Users", "157",   "↑ 3 blocked today",        ALERT_RED,  "🚫"));
        panel.add(cards);
        panel.add(Box.createVerticalStrut(24));

        // ── Middle Row: Activity + Recent Alerts ─────────────────────────
        JPanel midRow = new JPanel(new GridLayout(1, 2, 16, 0));
        midRow.setOpaque(false);
        midRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        midRow.add(buildActivityPanel());
        midRow.add(buildRecentAlertsPanel());
        panel.add(midRow);
        panel.add(Box.createVerticalStrut(24));

        // ── Recent Transactions snapshot ─────────────────────────────────
        panel.add(buildTransactionTablePanel(5));

        return panel;
    }

    // ════════════════════════════════════════════════════════════════════
    //  TRANSACTION DETAILS CONTENT  (Admin view of all transactions)
    // ════════════════════════════════════════════════════════════════════
    private JPanel buildTransactionDetailsContent() {
        JPanel panel = new JPanel();
        panel.setBackground(BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        // ── Header ───────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel lbl = new JLabel("All User Transactions");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl.setForeground(TEXT_DARK);

        JTextField search = new JTextField("🔍  Search by Txn ID, user, amount...");
        search.setForeground(TEXT_GRAY);
        search.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        search.setPreferredSize(new Dimension(280, 36));
        search.setBorder(BorderFactory.createCompoundBorder(
            new RoundBorder(BORDER_CLR, 1, 10),
            BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        search.addFocusListener(new FocusAdapter() {
            final String ph = search.getText();
            public void focusGained(FocusEvent e) {
                if (search.getText().equals(ph)) { search.setText(""); search.setForeground(TEXT_DARK); }
            }
            public void focusLost(FocusEvent e) {
                if (search.getText().isEmpty()) { search.setText(ph); search.setForeground(TEXT_GRAY); }
            }
        });

        header.add(lbl,    BorderLayout.WEST);
        header.add(search, BorderLayout.EAST);
        panel.add(header);
        panel.add(Box.createVerticalStrut(20));
        panel.add(buildTransactionTablePanel(20));

        return panel;
    }

    // ════════════════════════════════════════════════════════════════════
    //  FRAUD ALERTS CONTENT
    // ════════════════════════════════════════════════════════════════════
    private JPanel buildAlertsContent() {
        JPanel panel = new JPanel();
        panel.setBackground(BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel lbl = new JLabel("Fraud Alerts");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl.setForeground(TEXT_DARK);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(20));

        // Summary cards
        JPanel row = new JPanel(new GridLayout(1, 3, 16, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        row.add(statCard("Critical Alerts",  "8",  "Immediate action required", ALERT_RED,  "🔴"));
        row.add(statCard("High Risk",        "12", "Review within 24 hours",    WARN_AMBER, "🟡"));
        row.add(statCard("Resolved Today",   "19", "Cleared by analysts",       SUCCESS,    "✅"));
        panel.add(row);
        panel.add(Box.createVerticalStrut(24));

        // Alerts table
        JPanel card = card("Active Fraud Alerts");
        String[] cols = {"Alert ID", "Transaction", "Amount (₹)", "Risk", "Flagged Reason", "Time", "Action"};
        Object[][] data = {
            {"ALT#001", "TXN#8821", "52,000",   "Critical", "Multiple rapid transactions",  "09:42 AM", "Review"},
            {"ALT#002", "TXN#8819", "8,400",    "High",     "Unusual location",             "10:15 AM", "Review"},
            {"ALT#003", "TXN#8815", "1,20,000", "Critical", "Amount exceeds daily limit",   "11:03 AM", "Block"},
            {"ALT#004", "TXN#8810", "3,200",    "Medium",   "New device login",             "11:30 AM", "Review"},
            {"ALT#005", "TXN#8802", "74,500",   "High",     "International transfer",       "12:00 PM", "Review"},
            {"ALT#006", "TXN#8798", "15,000",   "Medium",   "Repeated failed attempts",     "12:45 PM", "Monitor"},
        };

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        styleTable(table);

        // Risk column renderer
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                l.setFont(new Font("Segoe UI", Font.BOLD, 11));
                l.setHorizontalAlignment(SwingConstants.CENTER);
                if (!sel) switch (v.toString()) {
                    case "Critical": l.setForeground(ALERT_RED);  break;
                    case "High":     l.setForeground(WARN_AMBER); break;
                    default:         l.setForeground(SLATE);      break;
                }
                return l;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(WHITE);
        card.add(scroll);
        panel.add(card);

        return panel;
    }

    // ════════════════════════════════════════════════════════════════════
    //  USER DETAILS CONTENT
    // ════════════════════════════════════════════════════════════════════
    private JPanel buildUserDetailsContent() {
        JPanel panel = new JPanel();
        panel.setBackground(BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        // ── Header ───────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel lbl = new JLabel("User Details");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl.setForeground(TEXT_DARK);

        JTextField search = new JTextField("🔍  Search by name, account, email...");
        search.setForeground(TEXT_GRAY);
        search.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        search.setPreferredSize(new Dimension(280, 36));
        search.setBorder(BorderFactory.createCompoundBorder(
            new RoundBorder(BORDER_CLR, 1, 10),
            BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        search.addFocusListener(new FocusAdapter() {
            final String ph = search.getText();
            public void focusGained(FocusEvent e) {
                if (search.getText().equals(ph)) { search.setText(""); search.setForeground(TEXT_DARK); }
            }
            public void focusLost(FocusEvent e) {
                if (search.getText().isEmpty()) { search.setText(ph); search.setForeground(TEXT_GRAY); }
            }
        });

        header.add(lbl,    BorderLayout.WEST);
        header.add(search, BorderLayout.EAST);
        panel.add(header);
        panel.add(Box.createVerticalStrut(20));

        // ── User Stats Row ───────────────────────────────────────────────
        JPanel statsRow = new JPanel(new GridLayout(1, 3, 16, 0));
        statsRow.setOpaque(false);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        statsRow.add(statCard("Total Users",   "1,248", "All registered accounts", SLATE,     "👥"));
        statsRow.add(statCard("Active Users",  "1,091", "Currently active",        SUCCESS,   "✅"));
        statsRow.add(statCard("Blocked Users", "157",   "Access restricted",       ALERT_RED, "🚫"));
        panel.add(statsRow);
        panel.add(Box.createVerticalStrut(24));

        // ── Users Table ──────────────────────────────────────────────────
        JPanel card = card("Registered Users");

        String[] cols = {"User ID", "Name", "Email", "Account No.", "Joined",   "Status",  "Risk"};
        Object[][] data = {
            {"USR#001", "Arun Kumar",    "arun@email.com",    "ACC#441201", "2023-03-12", "Active",  "Low"},
            {"USR#002", "Priya Sharma",  "priya@email.com",   "ACC#441202", "2023-04-05", "Active",  "Low"},
            {"USR#003", "Vikram Nair",   "vikram@email.com",  "ACC#441203", "2023-05-18", "Blocked", "High"},
            {"USR#004", "Meena Iyer",    "meena@email.com",   "ACC#441204", "2023-06-22", "Active",  "Medium"},
            {"USR#005", "Ravi Patel",    "ravi@email.com",    "ACC#441205", "2023-07-01", "Active",  "Low"},
            {"USR#006", "Sunita Reddy",  "sunita@email.com",  "ACC#441206", "2023-08-14", "Blocked", "Critical"},
            {"USR#007", "Karthik Das",   "karthik@email.com", "ACC#441207", "2023-09-03", "Active",  "Low"},
            {"USR#008", "Divya Menon",   "divya@email.com",   "ACC#441208", "2023-10-11", "Active",  "Medium"},
            {"USR#009", "Anand Singh",   "anand@email.com",   "ACC#441209", "2023-11-20", "Blocked", "High"},
            {"USR#010", "Lakshmi Rao",   "lakshmi@email.com", "ACC#441210", "2024-01-08", "Active",  "Low"},
        };

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        styleTable(table);

        // Status column renderer
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? WHITE : new Color(0xF7, 0xFB, 0xF9));
                    ((JLabel) c).setFont(new Font("Segoe UI", Font.BOLD, 12));
                    switch (val.toString()) {
                        case "Active":  ((JLabel) c).setForeground(SUCCESS);   break;
                        case "Blocked": ((JLabel) c).setForeground(ALERT_RED); break;
                        default:        ((JLabel) c).setForeground(TEXT_GRAY); break;
                    }
                }
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                return c;
            }
        });

        // Risk column renderer
        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                ((JLabel) c).setFont(new Font("Segoe UI", Font.BOLD, 11));
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? WHITE : new Color(0xF7, 0xFB, 0xF9));
                    switch (val.toString()) {
                        case "Critical": ((JLabel) c).setForeground(ALERT_RED);  break;
                        case "High":     ((JLabel) c).setForeground(WARN_AMBER); break;
                        case "Medium":   ((JLabel) c).setForeground(SLATE);      break;
                        default:         ((JLabel) c).setForeground(SUCCESS);    break;
                    }
                }
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(WHITE);
        card.add(scroll);
        panel.add(card);

        return panel;
    }

    // ════════════════════════════════════════════════════════════════════
    //  STAT CARD
    // ════════════════════════════════════════════════════════════════════
    private JPanel statCard(String label, String value, String trend, Color accent, String emoji) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(accent);
                g2.fillRoundRect(0, 0, 5, getHeight(), 14, 14);
                g2.fillRect(0, 0, 5, getHeight());
                g2.setColor(new Color(0, 0, 0, 10));
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 16));

        JLabel emojiLbl = new JLabel(emoji + "  " + label);
        emojiLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        emojiLbl.setForeground(TEXT_GRAY);
        emojiLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valueLbl = new JLabel(value);
        valueLbl.setFont(new Font("Segoe UI", Font.BOLD, 26));
        valueLbl.setForeground(TEXT_DARK);
        valueLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel trendLbl = new JLabel(trend);
        trendLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        trendLbl.setForeground(accent);
        trendLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(emojiLbl);
        card.add(Box.createVerticalStrut(8));
        card.add(valueLbl);
        card.add(Box.createVerticalStrut(4));
        card.add(trendLbl);
        return card;
    }

    // ════════════════════════════════════════════════════════════════════
    //  ACTIVITY BAR CHART PANEL
    // ════════════════════════════════════════════════════════════════════
    private JPanel buildActivityPanel() {
        JPanel card = card("Transaction Activity (Last 7 Days)");
        JPanel chart = new JPanel() {
            final int[] data = {42, 67, 53, 88, 74, 95, 61};
            final String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                int barW = (w - 60) / data.length;
                for (int i = 0; i < data.length; i++) {
                    int bh = (int) ((data[i] / 100.0) * (h - 40));
                    int x  = 30 + i * barW + barW / 6;
                    g2.setPaint(new GradientPaint(x, h - bh - 10, SLATE, x, h - 10, MINT));
                    g2.fillRoundRect(x, h - bh - 10, barW * 2 / 3, bh, 6, 6);
                    g2.setColor(TEXT_GRAY);
                    g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(days[i], x + barW / 3 - fm.stringWidth(days[i]) / 2, h - 2);
                }
                g2.dispose();
            }
        };
        chart.setOpaque(false);
        chart.setPreferredSize(new Dimension(0, 160));
        card.add(chart);
        return card;
    }

    // ════════════════════════════════════════════════════════════════════
    //  RECENT FRAUD ALERTS PANEL  (sidebar widget on Dashboard)
    // ════════════════════════════════════════════════════════════════════
    private JPanel buildRecentAlertsPanel() {
        JPanel card = card("Recent Fraud Alerts");
        String[][] alerts = {
            {"🔴", "TXN#8821", "₹52,000",   "High Risk"},
            {"🟡", "TXN#8819", "₹8,400",    "Medium"},
            {"🔴", "TXN#8815", "₹1,20,000", "High Risk"},
            {"🟢", "TXN#8810", "₹3,200",    "Low Risk"},
        };
        for (String[] a : alerts) {
            JPanel row = new JPanel(new BorderLayout());
            row.setOpaque(false);
            row.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));

            JLabel left = new JLabel(a[0] + "  " + a[1]);
            left.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
            left.setForeground(TEXT_DARK);

            JPanel rightP = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
            rightP.setOpaque(false);
            JLabel amt = new JLabel(a[2]);
            amt.setFont(new Font("Segoe UI", Font.BOLD, 12));
            amt.setForeground(TEXT_DARK);
            rightP.add(amt);
            rightP.add(badge(a[3]));

            row.add(left,   BorderLayout.WEST);
            row.add(rightP, BorderLayout.EAST);

            JSeparator sep = new JSeparator();
            sep.setForeground(BORDER_CLR);
            card.add(row);
            card.add(sep);
        }
        return card;
    }

    // ════════════════════════════════════════════════════════════════════
    //  TRANSACTION TABLE PANEL  (reused on Dashboard + Transaction Details)
    // ════════════════════════════════════════════════════════════════════
    private JPanel buildTransactionTablePanel(int rows) {
        JPanel card = card("Recent Transactions");

        String[] cols     = {"Txn ID", "Date & Time", "Amount (₹)", "Type", "Status", "Risk Level"};
        String[] types    = {"Credit", "Debit", "Transfer", "Payment"};
        String[] statuses = {"Completed", "Pending", "Failed", "Blocked"};
        String[] risks    = {"Low", "Medium", "High", "Critical"};
        java.util.Random rng = new java.util.Random(42);

        Object[][] data = new Object[rows][6];
        for (int i = 0; i < rows; i++) {
            data[i][0] = "TXN#" + (8800 + rows - i);
            data[i][1] = "2024-01-" + String.format("%02d", rng.nextInt(28) + 1)
                       + "  " + String.format("%02d:%02d", rng.nextInt(24), rng.nextInt(60));
            data[i][2] = String.format("%,d", (rng.nextInt(100) + 1) * 1000 + rng.nextInt(999));
            data[i][3] = types[rng.nextInt(types.length)];
            data[i][4] = statuses[rng.nextInt(statuses.length)];
            data[i][5] = risks[rng.nextInt(risks.length)];
        }

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        styleTable(table);

        // Alternating row background (base renderer)
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel) c.setBackground(row % 2 == 0 ? WHITE : new Color(0xF7, 0xFB, 0xF9));
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                return c;
            }
        });

        // Status column
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                ((JLabel) c).setFont(new Font("Segoe UI", Font.PLAIN, 12));
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? WHITE : new Color(0xF7, 0xFB, 0xF9));
                    switch (val.toString()) {
                        case "Completed": ((JLabel) c).setForeground(SUCCESS);    break;
                        case "Blocked":   ((JLabel) c).setForeground(ALERT_RED);  break;
                        case "Pending":   ((JLabel) c).setForeground(WARN_AMBER); break;
                        default:          ((JLabel) c).setForeground(TEXT_GRAY);  break;
                    }
                }
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                return c;
            }
        });

        // Risk Level column
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                ((JLabel) c).setFont(new Font("Segoe UI", Font.BOLD, 11));
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? WHITE : new Color(0xF7, 0xFB, 0xF9));
                    switch (val.toString()) {
                        case "Critical": ((JLabel) c).setForeground(ALERT_RED);  break;
                        case "High":     ((JLabel) c).setForeground(WARN_AMBER); break;
                        case "Medium":   ((JLabel) c).setForeground(SLATE);      break;
                        default:         ((JLabel) c).setForeground(SUCCESS);    break;
                    }
                }
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(WHITE);
        card.add(scroll);
        return card;
    }

    // ════════════════════════════════════════════════════════════════════
    //  PLACEHOLDER  (fallback for any unimplemented page)
    // ════════════════════════════════════════════════════════════════════
    private JPanel buildPlaceholder(String name) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(BG);
        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        JLabel ico = new JLabel("🚧", SwingConstants.CENTER);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        ico.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lbl = new JLabel(name + " — Coming Soon");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbl.setForeground(SLATE);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("This module is under construction.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sub.setForeground(TEXT_GRAY);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        inner.add(ico);
        inner.add(Box.createVerticalStrut(12));
        inner.add(lbl);
        inner.add(Box.createVerticalStrut(6));
        inner.add(sub);
        p.add(inner);
        return p;
    }

    // ════════════════════════════════════════════════════════════════════
    //  SHARED HELPERS
    // ════════════════════════════════════════════════════════════════════

    /** White rounded card with a bold section title and separator. */
    private JPanel card(String title) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(new Color(0, 0, 0, 10));
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(TEXT_DARK);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lbl);
        card.add(Box.createVerticalStrut(14));

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(BORDER_CLR);
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(sep);
        card.add(Box.createVerticalStrut(10));
        return card;
    }

    /** Apply standard table look-and-feel settings. */
    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(38);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(WHITE);
        table.setForeground(TEXT_DARK);
        table.setSelectionBackground(MINT);
        table.setSelectionForeground(SLATE_DK);

        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 12));
        th.setBackground(BG);
        th.setForeground(TEXT_GRAY);
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_CLR));
    }

    private JLabel badge(String text) {
        JLabel l = new JLabel(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = text.contains("High") ? new Color(0xFD, 0xED, 0xEC)
                         : text.contains("Medium") ? new Color(0xFE, 0xF6, 0xE1)
                         : new Color(0xE8, 0xF5, 0xEE);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(text.contains("High") ? ALERT_RED
                      : text.contains("Medium") ? WARN_AMBER : SUCCESS);
        l.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        l.setOpaque(false);
        return l;
    }

    private JButton smallButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = getModel().isPressed()  ? SLATE_DK
                           : getModel().isRollover() ? SLATE
                           : new Color(0xEE, 0xF4, 0xF1);
                g2.setColor(base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(getModel().isRollover() ? WHITE : SLATE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth()  - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        btn.setPreferredSize(new Dimension(80, 34));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ════════════════════════════════════════════════════════════════════
    //  ROUND BORDER
    // ════════════════════════════════════════════════════════════════════
    private static class RoundBorder extends AbstractBorder {
        private final Color color;
        private final int thickness, radius;

        RoundBorder(Color c, int t, int r) { color = c; thickness = t; radius = r; }

        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x, y, w - 1, h - 1, radius, radius);
            g2.dispose();
        }

        @Override public Insets getBorderInsets(Component c) {
            return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
        }
    }

    // ════════════════════════════════════════════════════════════════════
    //  MAIN
    // ════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(DashboardFrame::new);
    }
}