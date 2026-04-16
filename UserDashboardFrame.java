import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class UserDashboardFrame extends JFrame {

    // ── Colour Palette ───────────────────────────────────────────────────
    private static final Color MINT      = new Color(0xBB, 0xD2, 0xC5);
    private static final Color SLATE     = new Color(0x53, 0x69, 0x76);
    private static final Color SLATE_DK  = new Color(0x3A, 0x4E, 0x58);
    private static final Color SLATE_LT  = new Color(0x6A, 0x88, 0x96);
    private static final Color WHITE     = Color.WHITE;
    private static final Color BG        = new Color(0xF2, 0xF7, 0xF5);
    private static final Color TEXT_DARK = new Color(0x1C, 0x2B, 0x33);
    private static final Color TEXT_GRAY = new Color(0x7A, 0x90, 0x9C);
    private static final Color BORDER    = new Color(0xDD, 0xE8, 0xE4);
    private static final Color SUCCESS   = new Color(0x4C, 0xAF, 0x7A);
    private static final Color WARN      = new Color(0xE8, 0xA8, 0x30);
    private static final Color DANGER    = new Color(0xD9, 0x53, 0x4F);

    // Logged-in user info — replace with actual DB fetch
    private final String userName    = "Hariharan V";
    private final String userEmail   = "hariharan@gmail.com";
    private final String userAccNo   = "ACC#10021";
    private final String userBalance = "₹45,230.00";

    private String activeNav = "Home";
    private JPanel contentArea;
    private JLabel topBarTitle;

    public UserDashboardFrame() {
        setTitle("SreeHari Transaction Monitoring System – User Portal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 680);
        setMinimumSize(new Dimension(900, 560));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        root.add(buildSidebar(),  BorderLayout.WEST);
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
                GradientPaint gp = new GradientPaint(0, 0, SLATE_DK, 0, getHeight(), SLATE);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(0xFF, 0xFF, 0xFF, 12));
                g2.fillOval(-40, getHeight() - 200, 200, 200);
                g2.dispose();
            }
        };
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.add(Box.createVerticalStrut(20));

        // Nav items
        String[][] navItems = {
            {"🏠", "Home"},
            {"💳", "My Transactions"},
            {"📥", "Deposit"},
            {"📤", "Withdrawal"},
            {"🔁", "Money Transfer"},
            {"🚨", "My Alerts"},
        };
        for (String[] item : navItems) {
            sidebar.add(navItem(item[0], item[1]));
        }

        sidebar.add(Box.createVerticalGlue());

        // Divider
        JSeparator div = new JSeparator();
        div.setMaximumSize(new Dimension(220, 1));
        div.setForeground(new Color(0xFF, 0xFF, 0xFF, 30));
        sidebar.add(div);

        // User profile at bottom
        sidebar.add(buildUserProfile());
        return sidebar;
    }

    private JPanel navItem(String emoji, String label) {
        boolean[] hovered = {false};

        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (activeNav.equals(label)) {
                    g2.setColor(new Color(0xFF, 0xFF, 0xFF, 28));
                    g2.fillRoundRect(8, 4, getWidth() - 16, getHeight() - 8, 10, 10);
                    g2.setColor(MINT);
                    g2.fillRoundRect(4, 6, 4, getHeight() - 12, 4, 4);
                } else if (hovered[0]) {
                    g2.setColor(new Color(0xFF, 0xFF, 0xFF, 15));
                    g2.fillRoundRect(8, 4, getWidth() - 16, getHeight() - 8, 10, 10);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        item.setOpaque(false);
        item.setPreferredSize(new Dimension(220, 44));
        item.setMaximumSize(new Dimension(220, 44));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel emojiLbl = new JLabel(emoji);
        emojiLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        emojiLbl.setOpaque(false);

        JLabel textLbl = new JLabel(label);
        textLbl.setFont(new Font("Segoe UI", activeNav.equals(label) ? Font.BOLD : Font.PLAIN, 13));
        textLbl.setForeground(activeNav.equals(label) ? WHITE : new Color(0xCC, 0xDB, 0xD6));
        textLbl.setOpaque(false);

        item.add(emojiLbl);
        item.add(textLbl);

        item.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                activeNav = label;
                refreshContent();
            }
            public void mouseEntered(MouseEvent e) { hovered[0] = true;  item.repaint(); }
            public void mouseExited (MouseEvent e) { hovered[0] = false; item.repaint(); }
        });
        return item;
    }

    private JPanel buildUserProfile() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 10));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(220, 65));

        // Avatar
        String initials = "HV";
        JLabel avatar = new JLabel(initials, SwingConstants.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MINT);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(SLATE_DK);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                        (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(34, 34));
        avatar.setOpaque(false);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel name = new JLabel(userName.length() > 14 ? userName.substring(0, 13) + "…" : userName);
        name.setFont(new Font("Segoe UI", Font.BOLD, 12));
        name.setForeground(WHITE);
        JLabel accLbl = new JLabel(userAccNo);
        accLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        accLbl.setForeground(MINT);
        info.add(name);
        info.add(accLbl);

        p.add(avatar);
        p.add(info);
        return p;
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
        contentArea.add(buildHomeContent(), BorderLayout.CENTER);
        main.add(contentArea, BorderLayout.CENTER);
        return main;
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(WHITE);
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
            BorderFactory.createEmptyBorder(14, 24, 14, 24)
        ));

        topBarTitle = new JLabel("My Dashboard");
        topBarTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        topBarTitle.setForeground(TEXT_DARK);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);

        JLabel date = new JLabel("Today: " + java.time.LocalDate.now());
        date.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        date.setForeground(TEXT_GRAY);

        JButton logoutBtn = new JButton("Logout") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? SLATE : new Color(0xEE, 0xF4, 0xF1));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(getModel().isRollover() ? WHITE : SLATE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                        (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setPreferredSize(new Dimension(80, 34));
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> { dispose(); new AuthFrame("USER"); });

        right.add(date);
        right.add(logoutBtn);

        bar.add(topBarTitle, BorderLayout.WEST);
        bar.add(right,       BorderLayout.EAST);
        return bar;
    }

    private void refreshContent() {
        contentArea.removeAll();
        topBarTitle.setText(activeNav.equals("Home") ? "My Dashboard" : activeNav);
        switch (activeNav) {
            case "Home":             contentArea.add(buildHomeContent(),        BorderLayout.CENTER); break;
            case "My Transactions":  contentArea.add(buildMyTransactions(),     BorderLayout.CENTER); break;
            case "Deposit":          contentArea.add(buildOperationForm("Deposit"),       BorderLayout.CENTER); break;
            case "Withdrawal":       contentArea.add(buildOperationForm("Withdrawal"),    BorderLayout.CENTER); break;
            case "Money Transfer":   contentArea.add(buildOperationForm("Money Transfer"),BorderLayout.CENTER); break;
            case "My Alerts":        contentArea.add(buildMyAlerts(),           BorderLayout.CENTER); break;
        }
        contentArea.revalidate();
        contentArea.repaint();
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    // ════════════════════════════════════════════════════════════════════
    //  HOME PAGE
    // ════════════════════════════════════════════════════════════════════
    private JPanel buildHomeContent() {
        JPanel panel = new JPanel();
        panel.setBackground(BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        // Welcome banner
        JPanel banner = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, SLATE_DK, getWidth(), getHeight(), SLATE);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(new Color(0xFF, 0xFF, 0xFF, 15));
                g2.fillOval(getWidth() - 120, -40, 180, 180);
                g2.fillOval(getWidth() - 60, getHeight() - 60, 120, 120);
                g2.dispose();
            }
        };
        banner.setOpaque(false);
        banner.setLayout(new BoxLayout(banner, BoxLayout.Y_AXIS));
        banner.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        banner.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel greet = new JLabel("Good Day, " + userName + "! 👋");
        greet.setFont(new Font("Segoe UI", Font.BOLD, 22));
        greet.setForeground(WHITE);
        greet.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel accInfo = new JLabel("Account: " + userAccNo + "   |   Available Balance: " + userBalance);
        accInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        accInfo.setForeground(MINT);
        accInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        banner.add(greet);
        banner.add(Box.createVerticalStrut(8));
        banner.add(accInfo);

        panel.add(banner);
        panel.add(Box.createVerticalStrut(24));

        // Quick action cards
        JPanel quickRow = new JPanel(new GridLayout(1, 3, 16, 0));
        quickRow.setOpaque(false);
        quickRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        quickRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        quickRow.add(quickCard("📥", "Deposit",       "Add money",       "Deposit"));
        quickRow.add(quickCard("📤", "Withdrawal",    "Withdraw money",  "Withdrawal"));
        quickRow.add(quickCard("🔁", "Money Transfer","Send to another", "Money Transfer"));
        panel.add(quickRow);
        panel.add(Box.createVerticalStrut(24));

        // Summary stats
        JPanel stats = new JPanel(new GridLayout(1, 3, 16, 0));
        stats.setOpaque(false);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        stats.setAlignmentX(Component.LEFT_ALIGNMENT);
        stats.add(infoCard("💰", "Available Balance", userBalance,    SLATE));
        stats.add(infoCard("📈", "Total Income",      "₹28,670.00",  SUCCESS));
        stats.add(infoCard("📉", "Total Expenses",    "₹ 3,440.00",  DANGER));
        panel.add(stats);
        panel.add(Box.createVerticalStrut(24));

        // Recent transactions (mini)
        panel.add(buildMiniTransactionTable());

        return panel;
    }

    // ── Quick action card ─────────────────────────────────────────────────
    private JPanel quickCard(String emoji, String label, String sub, String navTarget) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 14, 14);
                g2.dispose();
            }
        };
        card.setBackground(WHITE);
        card.setOpaque(false);
        card.setLayout(new FlowLayout(FlowLayout.LEFT, 16, 0));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel emojiLbl = new JLabel(emoji, SwingConstants.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MINT);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        emojiLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        emojiLbl.setPreferredSize(new Dimension(46, 46));
        emojiLbl.setOpaque(false);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(TEXT_DARK);
        JLabel subLbl = new JLabel(sub);
        subLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subLbl.setForeground(TEXT_GRAY);
        info.add(lbl);
        info.add(subLbl);

        card.add(emojiLbl);
        card.add(info);

        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                activeNav = navTarget;
                refreshContent();
            }
            public void mouseEntered(MouseEvent e) { card.setBackground(new Color(0xF0, 0xF8, 0xF5)); card.repaint(); }
            public void mouseExited (MouseEvent e) { card.setBackground(WHITE); card.repaint(); }
        });
        return card;
    }

    // ── Info stat card ────────────────────────────────────────────────────
    private JPanel infoCard(String emoji, String label, String value, Color accent) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(accent);
                g2.fillRoundRect(0, 0, 5, getHeight(), 14, 14);
                g2.fillRect(0, 0, 5, getHeight());
                g2.setColor(new Color(0, 0, 0, 8));
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 14, 14);
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

        JLabel valLbl = new JLabel(value);
        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valLbl.setForeground(TEXT_DARK);
        valLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(emojiLbl);
        card.add(Box.createVerticalStrut(6));
        card.add(valLbl);
        return card;
    }

    // ── Mini recent transactions ──────────────────────────────────────────
    private JPanel buildMiniTransactionTable() {
        JPanel card = sectionCard("Recent Transactions");

        String[] cols = {"Txn ID", "Date", "Type", "Amount (₹)", "Status"};
        Object[][] data = {
            {"TXN#9901", "Apr 01, 2024", "Deposit",  "+5,000",  "Completed"},
            {"TXN#9895", "Mar 31, 2024", "Withdrawal","-2,000", "Completed"},
            {"TXN#9890", "Mar 31, 2024", "Transfer", "-3,500",  "Completed"},
            {"TXN#9885", "Mar 30, 2024", "Deposit",  "+10,000", "Completed"},
            {"TXN#9880", "Mar 29, 2024", "Withdrawal","-1,200", "Pending"},
        };
        card.add(buildSimpleTable(cols, data));
        return card;
    }

    // ════════════════════════════════════════════════════════════════════
    //  MY TRANSACTIONS PAGE
    // ════════════════════════════════════════════════════════════════════
    private JPanel buildMyTransactions() {
        JPanel panel = new JPanel();
        panel.setBackground(BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        JLabel title = new JLabel("💳  My Transactions");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(TEXT_DARK);
        header.add(title, BorderLayout.WEST);
        panel.add(header);
        panel.add(Box.createVerticalStrut(20));

        // Filter row
        JPanel filterRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        filterRow.setOpaque(false);
        filterRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        filterRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        String[] filters = {"All", "Deposit", "Withdrawal", "Transfer"};
        for (String f : filters) {
            JLabel chip = new JLabel(f) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getText().equals("All") ? SLATE : new Color(0xE8, 0xF0, 0xED));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            chip.setFont(new Font("Segoe UI", Font.BOLD, 12));
            chip.setForeground(f.equals("All") ? WHITE : SLATE);
            chip.setBorder(BorderFactory.createEmptyBorder(5, 14, 5, 14));
            chip.setOpaque(false);
            chip.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            filterRow.add(chip);
        }
        panel.add(filterRow);
        panel.add(Box.createVerticalStrut(16));

        // Full transaction table
        JPanel card = sectionCard("Transaction History");
        String[] cols = {"Txn ID", "Date & Time", "Type", "Amount (₹)", "Status", "Reference"};
        Object[][] data = {
            {"TXN#9901", "Apr 01  09:42 AM", "Deposit",    "+5,000",  "Completed", "DEP-001"},
            {"TXN#9895", "Mar 31  11:03 AM", "Withdrawal", "-2,000",  "Completed", "WDR-002"},
            {"TXN#9890", "Mar 31  11:30 AM", "Transfer",   "-3,500",  "Completed", "TRF-003"},
            {"TXN#9885", "Mar 30  12:00 PM", "Deposit",    "+10,000", "Completed", "DEP-004"},
            {"TXN#9880", "Mar 30  12:45 PM", "Withdrawal", "-1,200",  "Pending",   "WDR-005"},
            {"TXN#9875", "Mar 29  01:10 PM", "Transfer",   "-5,000",  "Completed", "TRF-006"},
            {"TXN#9870", "Mar 29  02:30 PM", "Deposit",    "+8,000",  "Completed", "DEP-007"},
            {"TXN#9865", "Mar 28  03:00 PM", "Withdrawal", "-900",    "Failed",    "WDR-008"},
            {"TXN#9860", "Mar 28  04:15 PM", "Deposit",    "+3,200",  "Completed", "DEP-009"},
            {"TXN#9855", "Mar 27  09:00 AM", "Transfer",   "-2,100",  "Completed", "TRF-010"},
        };
        card.add(buildSimpleTable(cols, data));
        panel.add(card);
        return panel;
    }

    // ════════════════════════════════════════════════════════════════════
    //  OPERATION FORMS — Deposit / Withdrawal / Money Transfer
    // ════════════════════════════════════════════════════════════════════
    private JPanel buildOperationForm(String operation) {
        JPanel panel = new JPanel();
        panel.setBackground(BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        // Form card
        JPanel formCard = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                g2.dispose();
            }
        };
        formCard.setOpaque(false);
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(BorderFactory.createEmptyBorder(32, 36, 32, 36));
        formCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.setMaximumSize(new Dimension(520, 9999));

        JLabel msgLabel = new JLabel(" ");
        msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        msgLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        switch (operation) {

            case "Deposit": {
                formCard.add(formTitle("📥  Deposit Funds"));
                formCard.add(formSub("Add money to your account " + userAccNo));
                formCard.add(Box.createVerticalStrut(28));
                formCard.add(formLabel("Deposit Amount (₹)"));
                JTextField amtField = formField("e.g. 5000");
                formCard.add(amtField);
                formCard.add(Box.createVerticalStrut(18));
                formCard.add(formLabel("Payment Mode"));
                JComboBox<String> modeBox = formCombo(new String[]{"Select mode","Cash","UPI","Net Banking","Cheque"});
                formCard.add(modeBox);
                formCard.add(Box.createVerticalStrut(24));
                formCard.add(msgLabel);
                formCard.add(Box.createVerticalStrut(8));
                JButton btn = formBtn("Deposit Now");
                btn.addActionListener(e -> {
                    String amt  = amtField.getText().trim();
                    String mode = (String) modeBox.getSelectedItem();
                    if (amt.isEmpty() || mode.equals("Select mode")) {
                        msgLabel.setForeground(DANGER); msgLabel.setText("⚠  Please fill in all fields.");
                    } else { try {
                        double a = Double.parseDouble(amt); if (a <= 0) throw new NumberFormatException();
                        msgLabel.setForeground(SUCCESS);
                        msgLabel.setText("✔  ₹" + amt + " deposited successfully via " + mode + "!");
                        // TODO: dao.deposit(userAccNo, a, mode);
                    } catch (NumberFormatException ex) {
                        msgLabel.setForeground(DANGER); msgLabel.setText("⚠  Enter a valid amount."); }}
                });
                formCard.add(btn);
                break;
            }

            case "Withdrawal": {
                formCard.add(formTitle("📤  Withdraw Funds"));
                formCard.add(formSub("Withdraw from your account " + userAccNo));
                formCard.add(Box.createVerticalStrut(28));
                formCard.add(formLabel("Withdrawal Amount (₹)"));
                JTextField amtField = formField("e.g. 2000");
                formCard.add(amtField);
                formCard.add(Box.createVerticalStrut(18));
                formCard.add(formLabel("Withdrawal Mode"));
                JComboBox<String> modeBox = formCombo(new String[]{"Select mode","ATM","Counter Withdrawal","UPI","Net Banking"});
                formCard.add(modeBox);
                formCard.add(Box.createVerticalStrut(24));
                formCard.add(msgLabel);
                formCard.add(Box.createVerticalStrut(8));
                JButton btn = formBtn("Withdraw Now");
                btn.addActionListener(e -> {
                    String amt  = amtField.getText().trim();
                    String mode = (String) modeBox.getSelectedItem();
                    if (amt.isEmpty() || mode.equals("Select mode")) {
                        msgLabel.setForeground(DANGER); msgLabel.setText("⚠  Please fill in all fields.");
                    } else { try {
                        double a = Double.parseDouble(amt); if (a <= 0) throw new NumberFormatException();
                        msgLabel.setForeground(SUCCESS);
                        msgLabel.setText("✔  ₹" + amt + " withdrawn successfully via " + mode + "!");
                        // TODO: dao.withdraw(userAccNo, a, mode);
                    } catch (NumberFormatException ex) {
                        msgLabel.setForeground(DANGER); msgLabel.setText("⚠  Enter a valid amount."); }}
                });
                formCard.add(btn);
                break;
            }

            case "Money Transfer": {
                formCard.add(formTitle("🔁  Money Transfer"));
                formCard.add(formSub("Transfer funds from " + userAccNo + " to another account"));
                formCard.add(Box.createVerticalStrut(28));
                formCard.add(formLabel("Recipient Account Number"));
                JTextField toField = formField("Enter recipient account number");
                formCard.add(toField);
                formCard.add(Box.createVerticalStrut(18));
                formCard.add(formLabel("Transfer Amount (₹)"));
                JTextField amtField = formField("e.g. 10000");
                formCard.add(amtField);
                formCard.add(Box.createVerticalStrut(18));
                formCard.add(formLabel("Notes  (optional)"));
                JTextField notesField = formField("e.g. Rent, Family transfer...");
                formCard.add(notesField);
                formCard.add(Box.createVerticalStrut(24));
                formCard.add(msgLabel);
                formCard.add(Box.createVerticalStrut(8));
                JButton btn = formBtn("Transfer Now");
                btn.addActionListener(e -> {
                    String to  = toField.getText().trim();
                    String amt = amtField.getText().trim();
                    if (to.isEmpty() || amt.isEmpty()) {
                        msgLabel.setForeground(DANGER); msgLabel.setText("⚠  Please fill in all required fields.");
                    } else if (to.equals(userAccNo)) {
                        msgLabel.setForeground(DANGER); msgLabel.setText("⚠  Cannot transfer to your own account.");
                    } else { try {
                        double a = Double.parseDouble(amt); if (a <= 0) throw new NumberFormatException();
                        msgLabel.setForeground(SUCCESS);
                        msgLabel.setText("✔  ₹" + amt + " transferred to " + to + " successfully!");
                        // TODO: dao.transfer(userAccNo, to, a, notes);
                    } catch (NumberFormatException ex) {
                        msgLabel.setForeground(DANGER); msgLabel.setText("⚠  Enter a valid amount."); }}
                });
                formCard.add(btn);
                break;
            }
        }

        panel.add(formCard);
        return panel;
    }

    // ════════════════════════════════════════════════════════════════════
    //  MY ALERTS PAGE
    // ════════════════════════════════════════════════════════════════════
    private JPanel buildMyAlerts() {
        JPanel panel = new JPanel();
        panel.setBackground(BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JLabel title = new JLabel("🚨  My Fraud Alerts");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));

        JPanel card = sectionCard("Alerts on Your Account");
        String[] cols = {"Alert ID", "Transaction", "Amount (₹)", "Risk", "Reason", "Time"};
        Object[][] data = {
            {"ALT#021", "TXN#9880", "1,200",  "Medium", "Repeated failed attempts", "Mar 30  12:45 PM"},
            {"ALT#018", "TXN#9855", "2,100",  "Low",    "Unusual time of access",   "Mar 27  09:00 AM"},
        };
        card.add(buildSimpleTable(cols, data));
        panel.add(card);
        return panel;
    }

    // ════════════════════════════════════════════════════════════════════
    //  SHARED HELPERS
    // ════════════════════════════════════════════════════════════════════
    private JPanel sectionCard(String title) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 14, 14);
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
        card.add(Box.createVerticalStrut(12));
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(BORDER);
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(sep);
        card.add(Box.createVerticalStrut(10));
        return card;
    }

    private JScrollPane buildSimpleTable(String[] cols, Object[][] data) {
        DefaultTableModel model = new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(40);
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
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));

        // Amount column color
        int amtCol = -1;
        for (int i = 0; i < cols.length; i++) if (cols[i].contains("Amount")) { amtCol = i; break; }
        final int ac = amtCol;
        if (ac >= 0) {
            table.getColumnModel().getColumn(ac).setCellRenderer(new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable t, Object val,
                        boolean sel, boolean foc, int row, int col) {
                    JLabel l = (JLabel) super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                    String v = val.toString();
                    l.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    if (!sel) l.setForeground(v.startsWith("+") ? SUCCESS : DANGER);
                    l.setBackground(sel ? MINT : (row % 2 == 0 ? WHITE : new Color(0xF7, 0xFB, 0xF9)));
                    l.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                    return l;
                }
            });
        }

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                l.setBackground(sel ? MINT : (row % 2 == 0 ? WHITE : new Color(0xF7, 0xFB, 0xF9)));
                l.setForeground(sel ? SLATE_DK : TEXT_DARK);
                l.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                return l;
            }
        });

        if (ac >= 0) {
            table.getColumnModel().getColumn(ac).setCellRenderer(new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable t, Object val,
                        boolean sel, boolean foc, int row, int col) {
                    JLabel l = (JLabel) super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                    l.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    if (!sel) l.setForeground(val.toString().startsWith("+") ? SUCCESS : DANGER);
                    l.setBackground(sel ? MINT : (row % 2 == 0 ? WHITE : new Color(0xF7, 0xFB, 0xF9)));
                    l.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                    return l;
                }
            });
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(WHITE);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        return scroll;
    }

    // ── Form helpers ──────────────────────────────────────────────────────
    private JLabel formTitle(String t) {
        JLabel l = new JLabel(t); l.setFont(new Font("Segoe UI", Font.BOLD, 18));
        l.setForeground(TEXT_DARK); l.setAlignmentX(Component.LEFT_ALIGNMENT); return l;
    }
    private JLabel formSub(String t) {
        JLabel l = new JLabel(t); l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        l.setForeground(TEXT_GRAY); l.setAlignmentX(Component.LEFT_ALIGNMENT); return l;
    }
    private JLabel formLabel(String t) {
        JLabel l = new JLabel(t); l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(TEXT_DARK); l.setAlignmentX(Component.LEFT_ALIGNMENT); return l;
    }
    private JTextField formField(String ph) {
        JTextField f = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground()); g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
                super.paintComponent(g); g2.dispose();
            }
            @Override protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isFocusOwner() ? SLATE : new Color(0xCC,0xDA,0xD6));
                g2.setStroke(new BasicStroke(isFocusOwner() ? 2f : 1.2f));
                g2.drawRoundRect(1,1,getWidth()-2,getHeight()-2,10,10); g2.dispose();
            }
        };
        f.setOpaque(false); f.setBackground(new Color(0xF4,0xF8,0xF6));
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13)); f.setForeground(TEXT_GRAY);
        f.setText(ph); f.setBorder(BorderFactory.createEmptyBorder(10,14,10,14));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { if(f.getText().equals(ph)){f.setText("");f.setForeground(TEXT_DARK);}}
            public void focusLost (FocusEvent e) { if(f.getText().isEmpty()){f.setText(ph);f.setForeground(TEXT_GRAY);}}
        });
        return f;
    }
    private JComboBox<String> formCombo(String[] items) {
        JComboBox<String> b = new JComboBox<>(items);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        b.setBackground(new Color(0xF4,0xF8,0xF6)); b.setForeground(TEXT_DARK);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        b.setAlignmentX(Component.LEFT_ALIGNMENT); return b;
    }
    private JButton formBtn(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? SLATE_DK : getModel().isRollover() ? SLATE_LT : SLATE);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
                g2.setColor(WHITE); g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),(getWidth()-fm.stringWidth(getText()))/2,
                        (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(180, 44)); btn.setMaximumSize(new Dimension(180, 44));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT); btn.setContentAreaFilled(false);
        btn.setBorderPainted(false); btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); return btn;
    }

    // ════════════════════════════════════════════════════════════════════
    //  MAIN
    // ════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(UserDashboardFrame::new);
    }
}
