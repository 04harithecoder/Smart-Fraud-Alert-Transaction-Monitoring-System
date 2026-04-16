import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class TransactionFrame extends JFrame {

    // ── Colour Palette (same as project) ────────────────────────────────
    private static final Color MINT      = new Color(0xBB, 0xD2, 0xC5);
    private static final Color SLATE     = new Color(0x53, 0x69, 0x76);
    private static final Color SLATE_DK  = new Color(0x3A, 0x4E, 0x58);
    private static final Color SLATE_LT  = new Color(0xE8, 0xF0, 0xED);
    private static final Color WHITE     = Color.WHITE;
    private static final Color BG        = new Color(0xF2, 0xF7, 0xF5);
    private static final Color TEXT_DARK = new Color(0x1C, 0x2B, 0x33);
    private static final Color TEXT_GRAY = new Color(0x7A, 0x90, 0x9C);
    private static final Color BORDER    = new Color(0xDD, 0xE8, 0xE4);
    private static final Color SUCCESS   = new Color(0x4C, 0xAF, 0x7A);
    private static final Color WARN      = new Color(0xE8, 0xA8, 0x30);
    private static final Color DANGER    = new Color(0xD9, 0x53, 0x4F);

    private String activeTab = "All";

    public TransactionFrame() {
        setTitle("SreeHari Transaction Monitoring System – Transactions");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 580));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        root.add(buildTopBar(),  BorderLayout.NORTH);
        root.add(buildContent(), BorderLayout.CENTER);
        add(root);
        setVisible(true);
    }

    // ════════════════════════════════════════════════════════════════════
    //  TOP NAV BAR
    // ════════════════════════════════════════════════════════════════════
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(WHITE);
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
            BorderFactory.createEmptyBorder(0, 28, 0, 28)
        ));
        bar.setPreferredSize(new Dimension(0, 58));

        // Left nav tabs
        JPanel tabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tabs.setOpaque(false);
        for (String nav : new String[]{"Overview", "Reports", "Transactions", "Fraud Alerts"}) {
            tabs.add(navTab(nav, nav.equals("Transactions")));
        }

        // Right — bell + avatar
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        right.setOpaque(false);

        JLabel bell = new JLabel("🔔");
        bell.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        bell.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel avatar = new JLabel("AK") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(SLATE);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth()  - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(34, 34));
        avatar.setOpaque(false);

        right.add(bell);
        right.add(avatar);

        bar.add(tabs,  BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JPanel navTab(String label, boolean active) {
        JPanel tab = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (active) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(SLATE);
                    g2.setStroke(new BasicStroke(2.5f));
                    g2.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
                    g2.dispose();
                }
            }
        };
        tab.setOpaque(false);
        tab.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", active ? Font.BOLD : Font.PLAIN, 13));
        lbl.setForeground(active ? SLATE_DK : TEXT_GRAY);
        tab.add(lbl, BorderLayout.CENTER);
        tab.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return tab;
    }

    // ════════════════════════════════════════════════════════════════════
    //  MAIN CONTENT
    // ════════════════════════════════════════════════════════════════════
    private JPanel buildContent() {
        JPanel content = new JPanel();
        content.setBackground(BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));

        // ── Header row ───────────────────────────────────────────────────
        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setOpaque(false);
        headerRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        JLabel pageTitle = new JLabel("Transactions History");
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        pageTitle.setForeground(TEXT_DARK);

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerRight.setOpaque(false);
        headerRight.add(chipLabel("Apr 1, 2024 – Apr 30, 2024  📅"));
        headerRight.add(outlineButton("Export CSV  ↓"));
        headerRight.add(outlineButton("Download Invoices  ↓"));

        headerRow.add(pageTitle,   BorderLayout.WEST);
        headerRow.add(headerRight, BorderLayout.EAST);

        // ── Summary Cards ────────────────────────────────────────────────
        JPanel summaryRow = new JPanel(new GridLayout(1, 4, 16, 0));
        summaryRow.setOpaque(false);
        summaryRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 96));
        summaryRow.add(summaryCard("💳", "Balance",  "₹78,987", SLATE));
        summaryRow.add(summaryCard("🏦", "Savings",  "₹23,000", new Color(0x5B, 0x9B, 0xD5)));
        summaryRow.add(summaryCard("↓",  "Income",   "₹28,670", SUCCESS));
        summaryRow.add(summaryCard("↑",  "Expenses", "₹3,456",  DANGER));

        // ── Filter tabs + Status ─────────────────────────────────────────
        JPanel filterRow = new JPanel(new BorderLayout());
        filterRow.setOpaque(false);
        filterRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        JPanel tabsLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tabsLeft.setOpaque(false);
        for (String t : new String[]{"All", "Savings", "Income", "Expenses"}) {
            tabsLeft.add(filterTab(t, t.equals(activeTab)));
        }

        JLabel statusFilter = new JLabel("Status: All  ▾");
        statusFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusFilter.setForeground(TEXT_DARK);
        statusFilter.setBorder(BorderFactory.createCompoundBorder(
            new RoundBorder(BORDER, 1, 8),
            BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));
        statusFilter.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        filterRow.add(tabsLeft,     BorderLayout.WEST);
        filterRow.add(statusFilter, BorderLayout.EAST);

        // ── Table ────────────────────────────────────────────────────────
        JScrollPane scroll = buildTable();

        // Assemble
        content.add(headerRow);
        content.add(Box.createVerticalStrut(24));
        content.add(summaryRow);
        content.add(Box.createVerticalStrut(28));
        content.add(filterRow);
        content.add(Box.createVerticalStrut(4));
        content.add(scroll);

        return content;
    }

    // ════════════════════════════════════════════════════════════════════
    //  SUMMARY CARD
    // ════════════════════════════════════════════════════════════════════
    private JPanel summaryCard(String icon, String label, String value, Color accent) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(new Color(0,0,0,8));
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 14, 14);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new FlowLayout(FlowLayout.LEFT, 16, 0));
        card.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 0));

        // Icon bubble
        JLabel iconLbl = new JLabel(icon, SwingConstants.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 30));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        iconLbl.setForeground(accent);
        iconLbl.setPreferredSize(new Dimension(44, 44));
        iconLbl.setOpaque(false);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(TEXT_GRAY);

        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 20));
        val.setForeground(TEXT_DARK);

        info.add(lbl);
        info.add(Box.createVerticalStrut(2));
        info.add(val);

        card.add(iconLbl);
        card.add(info);
        return card;
    }

    // ── Filter Tab ────────────────────────────────────────────────────────
    private JPanel filterTab(String label, boolean active) {
        JPanel tab = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (active) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(SLATE);
                    g2.setStroke(new BasicStroke(2.5f));
                    g2.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
                    g2.dispose();
                }
            }
        };
        tab.setOpaque(false);
        tab.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        tab.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", active ? Font.BOLD : Font.PLAIN, 13));
        lbl.setForeground(active ? SLATE_DK : TEXT_GRAY);
        tab.add(lbl, BorderLayout.CENTER);
        return tab;
    }

    // ════════════════════════════════════════════════════════════════════
    //  TRANSACTION TABLE
    // ════════════════════════════════════════════════════════════════════
    private JScrollPane buildTable() {
        String[] cols = {"Ref ID", "Transaction Date", "From", "Type", "Amount (₹)", "Status", "Actions"};
        Object[][] data = {
            {"TXN#8901", "Apr 1, 2024  09:42 AM",  "hariharan@gmail.com",  "Income",   "+28,670", "Completed", "⋯"},
            {"TXN#8900", "Apr 2, 2024  11:15 AM",  "admin@sreehari.com",   "Transfer", "+5,000",  "Pending",   "⋯"},
            {"TXN#8899", "Apr 3, 2024  02:30 PM",  "vendor@payments.in",   "Expense",  "-3,456",  "Completed", "⋯"},
            {"TXN#8898", "Apr 4, 2024  04:00 PM",  "savings@bank.in",      "Savings",  "+10,000", "Completed", "⋯"},
            {"TXN#8897", "Apr 5, 2024  10:00 AM",  "unknown@mail.com",     "Transfer", "-52,000", "Blocked",   "⋯"},
            {"TXN#8896", "Apr 6, 2024  01:22 PM",  "salary@company.com",   "Income",   "+45,000", "Completed", "⋯"},
            {"TXN#8895", "Apr 7, 2024  03:45 PM",  "shopify@payments.com", "Expense",  "-1,200",  "Pending",   "⋯"},
            {"TXN#8894", "Apr 8, 2024  09:10 AM",  "transfer@upi.in",      "Transfer", "+8,400",  "Completed", "⋯"},
            {"TXN#8893", "Apr 9, 2024  11:55 AM",  "flagged@unknown.net",  "Expense",  "-74,500", "Blocked",   "⋯"},
            {"TXN#8892", "Apr 10, 2024 08:30 AM",  "payroll@sreehari.com", "Income",   "+60,000", "Completed", "⋯"},
        };

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(46);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(WHITE);
        table.setForeground(TEXT_DARK);
        table.setSelectionBackground(MINT);
        table.setSelectionForeground(SLATE_DK);
        table.setFocusable(false);

        // Header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(BG);
        header.setForeground(TEXT_GRAY);
        header.setPreferredSize(new Dimension(0, 42));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);

        // Default renderer — alternating rows + padding
        DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel) c.setBackground(row % 2 == 0 ? WHITE : new Color(0xF7, 0xFB, 0xF9));
                ((JLabel)c).setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                return c;
            }
        };
        table.setDefaultRenderer(Object.class, defaultRenderer);

        // "From" col — avatar circle + email
        table.getColumnModel().getColumn(2).setCellRenderer(new TableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 7));
                p.setBackground(sel ? MINT : row % 2 == 0 ? WHITE : new Color(0xF7, 0xFB, 0xF9));
                String email = val.toString();
                char init = Character.toUpperCase(email.charAt(0));

                JLabel dot = new JLabel(String.valueOf(init), SwingConstants.CENTER) {
                    @Override protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(SLATE);
                        g2.fillOval(0, 0, getWidth(), getHeight());
                        g2.setColor(WHITE);
                        g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                        FontMetrics fm = g2.getFontMetrics();
                        g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                            (getHeight()+fm.getAscent()-fm.getDescent())/2);
                        g2.dispose();
                    }
                };
                dot.setPreferredSize(new Dimension(28, 28));
                dot.setOpaque(false);

                JLabel emailLbl = new JLabel(email);
                emailLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                emailLbl.setForeground(TEXT_DARK);

                p.add(dot); p.add(emailLbl);
                return p;
            }
        });

        // Amount col — green/red
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                String v = val.toString();
                ((JLabel)c).setFont(new Font("Segoe UI", Font.BOLD, 13));
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? WHITE : new Color(0xF7, 0xFB, 0xF9));
                    ((JLabel)c).setForeground(v.startsWith("+") ? SUCCESS : v.startsWith("-") ? DANGER : TEXT_DARK);
                }
                ((JLabel)c).setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                return c;
            }
        });

        // Status col — pill badge
        table.getColumnModel().getColumn(5).setCellRenderer(new TableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                String v = val.toString();
                Color fg = v.equals("Completed") ? SUCCESS : v.equals("Blocked") ? DANGER : WARN;
                Color bgBadge = new Color(fg.getRed(), fg.getGreen(), fg.getBlue(), 28);

                JLabel badge = new JLabel(v, SwingConstants.CENTER) {
                    @Override protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(bgBadge);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
                badge.setForeground(fg);
                badge.setOpaque(false);
                badge.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));

                JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
                wrap.setBackground(sel ? MINT : row % 2 == 0 ? WHITE : new Color(0xF7, 0xFB, 0xF9));
                wrap.add(badge);
                return wrap;
            }
        });

        // Actions col
        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
                lbl.setForeground(TEXT_GRAY);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                if (!sel) lbl.setBackground(row % 2 == 0 ? WHITE : new Color(0xF7, 0xFB, 0xF9));
                lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                return lbl;
            }
        });

        // Column widths
        int[] widths = {110, 190, 230, 100, 120, 130, 70};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createCompoundBorder(
            new RoundBorder(BORDER, 1, 14),
            BorderFactory.createEmptyBorder()
        ));
        scroll.getViewport().setBackground(WHITE);
        scroll.setBackground(WHITE);
        return scroll;
    }

    // ════════════════════════════════════════════════════════════════════
    //  HELPERS
    // ════════════════════════════════════════════════════════════════════
    private JLabel chipLabel(String text) {
        JLabel l = new JLabel(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(SLATE_LT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(TEXT_DARK);
        l.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        l.setOpaque(false);
        return l;
    }

    private JButton outlineButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? SLATE : WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                g2.setColor(getModel().isRollover() ? WHITE : TEXT_DARK);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth()-fm.stringWidth(getText()))/2,
                    (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setPreferredSize(new Dimension(160, 34));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private static class RoundBorder extends AbstractBorder {
        private final Color color; private final int t, r;
        RoundBorder(Color c, int t, int r) { color=c; this.t=t; this.r=r; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color); g2.setStroke(new BasicStroke(t));
            g2.drawRoundRect(x, y, w-1, h-1, r, r); g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(r/2,r/2,r/2,r/2); }
    }

    // ════════════════════════════════════════════════════════════════════
    //  MAIN
    // ════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(TransactionFrame::new);
    }
}
