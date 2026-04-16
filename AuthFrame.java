import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class AuthFrame extends JFrame {

    // ── Colour Palette ──────────────────────────────────────────────────
    private static final Color MINT      = new Color(0xBB, 0xD2, 0xC5); // #BBD2C5
    private static final Color SLATE     = new Color(0x53, 0x69, 0x76); // #536976
    private static final Color SLATE_DK  = new Color(0x3A, 0x4E, 0x58);
    private static final Color WHITE     = Color.WHITE;
    private static final Color TEXT_DARK = new Color(0x1C, 0x2B, 0x33);
    private static final Color TEXT_GRAY = new Color(0x7A, 0x90, 0x9C);
    private static final Color ERROR_RED = new Color(0xD9, 0x53, 0x4F);
    private static final Color SUCCESS   = new Color(0x4C, 0xAF, 0x7A);

    private CardLayout cardLayout;
    private JPanel     cardPanel;
    private String     role; // "USER" or "ADMIN"

    // ── Constructor ──────────────────────────────────────────────────────
    public AuthFrame(String role) {
        this.role = role;
        setTitle("SreeHari Transaction Monitering System – " + role + " Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        cardPanel  = new JPanel(cardLayout);
        cardPanel.add(buildLoginPanel(),  "LOGIN");
        cardPanel.add(buildSignupPanel(), "SIGNUP");

        add(cardPanel);
        cardLayout.show(cardPanel, "LOGIN");
        setVisible(true);
    }

    // ════════════════════════════════════════════════════════════════════
    //  LOGIN PANEL
    // ════════════════════════════════════════════════════════════════════
    private JPanel buildLoginPanel() {
        JPanel root = new JPanel(new GridLayout(1, 2));

        // ── Left  – branding ────────────────────────────────────────────
        JPanel left = new BrandPanel();
        root.add(left);

        // ── Right – form ─────────────────────────────────────────────────
        JPanel right = new JPanel();
        right.setBackground(WHITE);
        right.setLayout(new GridBagLayout());

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setPreferredSize(new Dimension(320, 420));

        // Title
        JLabel title = styledLabel("Welcome Back", 26, Font.BOLD, TEXT_DARK);
        JLabel sub   = styledLabel("Sign in to your account", 13, Font.PLAIN, TEXT_GRAY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Fields
        JTextField     emailField = roundedField("Email Address");
        JPasswordField passField  = roundedPassField("Password");

        // Forgot password
        JLabel forgot = new JLabel("Forgot password?");
        forgot.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        forgot.setForeground(SLATE);
        forgot.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgot.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // Message label
        JLabel msgLabel = new JLabel(" ");
        msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        msgLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Login button
        JButton loginBtn = primaryButton("Sign In");
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String pass  = new String(passField.getPassword());
            if (email.isEmpty() || pass.isEmpty()) {
                msgLabel.setForeground(ERROR_RED);
                msgLabel.setText("⚠  Please fill in all fields.");
            } else {
                msgLabel.setForeground(SUCCESS);
                msgLabel.setText("✔  Login successful! Redirecting…");
                // TODO: Hook into your authentication logic here
            }
        });

        // Switch to signup
        JPanel switchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        switchRow.setOpaque(false);
        switchRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel noAcc  = small("Don't have an account?  ");
        JLabel signUp = link("Create one");
        signUp.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { cardLayout.show(cardPanel, "SIGNUP"); }
        });
        switchRow.add(noAcc);
        switchRow.add(signUp);

        // Assemble
        form.add(title);
        form.add(Box.createVerticalStrut(4));
        form.add(sub);
        form.add(Box.createVerticalStrut(28));
        form.add(labelFor("Email Address"));
        form.add(Box.createVerticalStrut(6));
        form.add(emailField);
        form.add(Box.createVerticalStrut(16));
        form.add(labelFor("Password"));
        form.add(Box.createVerticalStrut(6));
        form.add(passField);
        form.add(Box.createVerticalStrut(6));
        form.add(forgot);
        form.add(Box.createVerticalStrut(8));
        form.add(msgLabel);
        form.add(Box.createVerticalStrut(10));
        form.add(loginBtn);
        form.add(Box.createVerticalStrut(20));
        form.add(divider());
        form.add(Box.createVerticalStrut(20));
        form.add(switchRow);

        right.add(form);
        root.add(right);
        return root;
    }

    // ════════════════════════════════════════════════════════════════════
    //  SIGNUP PANEL
    // ════════════════════════════════════════════════════════════════════
    private JPanel buildSignupPanel() {
        JPanel root = new JPanel(new GridLayout(1, 2));

        // ── Left  – form ─────────────────────────────────────────────────
        JPanel left = new JPanel();
        left.setBackground(WHITE);
        left.setLayout(new GridBagLayout());

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setPreferredSize(new Dimension(320, 480));

        JLabel title = styledLabel("Create Account", 26, Font.BOLD, TEXT_DARK);
        JLabel sub   = styledLabel("Start monitoring your transactions", 13, Font.PLAIN, TEXT_GRAY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Name row
        JPanel nameRow = new JPanel(new GridLayout(1, 2, 10, 0));
        nameRow.setOpaque(false);
        nameRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameRow.setMaximumSize(new Dimension(320, 40));
        JTextField fnField = roundedField("First Name");
        JTextField lnField = roundedField("Last Name");
        nameRow.add(fnField);
        nameRow.add(lnField);

        JTextField     emailField = roundedField("Email Address");
        JPasswordField passField  = roundedPassField("Password");
        JPasswordField confField  = roundedPassField("Confirm Password");

        JLabel msgLabel = new JLabel(" ");
        msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        msgLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton regBtn = primaryButton("Create Account");
        regBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        regBtn.addActionListener(e -> {
            String fn    = fnField.getText().trim();
            String ln    = lnField.getText().trim();
            String email = emailField.getText().trim();
            String pass  = new String(passField.getPassword());
            String conf  = new String(confField.getPassword());

            if (fn.isEmpty() || ln.isEmpty() || email.isEmpty() || pass.isEmpty() || conf.isEmpty()) {
                msgLabel.setForeground(ERROR_RED);
                msgLabel.setText("⚠  Please fill in all fields.");
            } else if (!pass.equals(conf)) {
                msgLabel.setForeground(ERROR_RED);
                msgLabel.setText("⚠  Passwords do not match.");
            } else if (!email.contains("@")) {
                msgLabel.setForeground(ERROR_RED);
                msgLabel.setText("⚠  Enter a valid email address.");
            } else {
                msgLabel.setForeground(SUCCESS);
                msgLabel.setText("✔  Account created successfully!");
                // TODO: Hook into your user registration logic here
            }
        });

        // Switch to login
        JPanel switchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        switchRow.setOpaque(false);
        switchRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel haveAcc = small("Already have an account?  ");
        JLabel signIn  = link("Sign in");
        signIn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { cardLayout.show(cardPanel, "LOGIN"); }
        });
        switchRow.add(haveAcc);
        switchRow.add(signIn);

        // Assemble
        form.add(title);
        form.add(Box.createVerticalStrut(4));
        form.add(sub);
        form.add(Box.createVerticalStrut(24));
        form.add(labelFor("Full Name"));
        form.add(Box.createVerticalStrut(6));
        form.add(nameRow);
        form.add(Box.createVerticalStrut(14));
        form.add(labelFor("Email Address"));
        form.add(Box.createVerticalStrut(6));
        form.add(emailField);
        form.add(Box.createVerticalStrut(14));
        form.add(labelFor("Password"));
        form.add(Box.createVerticalStrut(6));
        form.add(passField);
        form.add(Box.createVerticalStrut(14));
        form.add(labelFor("Confirm Password"));
        form.add(Box.createVerticalStrut(6));
        form.add(confField);
        form.add(Box.createVerticalStrut(6));
        form.add(msgLabel);
        form.add(Box.createVerticalStrut(10));
        form.add(regBtn);
        form.add(Box.createVerticalStrut(20));
        form.add(divider());
        form.add(Box.createVerticalStrut(20));
        form.add(switchRow);

        left.add(form);
        root.add(left);

        // ── Right – branding ────────────────────────────────────────────
        root.add(new BrandPanel());
        return root;
    }

    // ════════════════════════════════════════════════════════════════════
    //  BRAND PANEL (painted left/right panel with logo & tagline)
    // ════════════════════════════════════════════════════════════════════
    private class BrandPanel extends JPanel {
        BrandPanel() {
            setLayout(new GridBagLayout());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Gradient background
            GradientPaint gp = new GradientPaint(0, 0, SLATE_DK, getWidth(), getHeight(), SLATE);
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());

            // Decorative circles
            g2.setColor(new Color(0xFF, 0xFF, 0xFF, 18));
            g2.fillOval(-60, -60, 260, 260);
            g2.fillOval(getWidth() - 140, getHeight() - 140, 260, 260);
            g2.setColor(new Color(0xBB, 0xD2, 0xC5, 30));
            g2.fillOval(getWidth() - 80, 20, 160, 160);

            g2.dispose();

            // Text drawn via Swing labels added in constructor
        }

        // No branding content — clean gradient panel only
    }

    // ════════════════════════════════════════════════════════════════════
    //  COMPONENT HELPERS
    // ════════════════════════════════════════════════════════════════════

    private JTextField roundedField(String placeholder) {
        JTextField f = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2.dispose();
            }
            @Override protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isFocusOwner() ? SLATE : new Color(0xCC, 0xDA, 0xD6));
                g2.setStroke(new BasicStroke(isFocusOwner() ? 2f : 1.2f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 10, 10);
                g2.dispose();
            }
        };
        f.setOpaque(false);
        f.setBackground(new Color(0xF4, 0xF8, 0xF6));
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setForeground(TEXT_DARK);
        f.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        f.setPreferredSize(new Dimension(320, 40));
        f.setMaximumSize(new Dimension(320, 40));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Placeholder
        f.setText(placeholder);
        f.setForeground(TEXT_GRAY);
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (f.getText().equals(placeholder)) { f.setText(""); f.setForeground(TEXT_DARK); }
            }
            public void focusLost(FocusEvent e) {
                if (f.getText().isEmpty()) { f.setText(placeholder); f.setForeground(TEXT_GRAY); }
            }
        });
        return f;
    }

    private JPasswordField roundedPassField(String placeholder) {
        JPasswordField f = new JPasswordField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2.dispose();
            }
            @Override protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isFocusOwner() ? SLATE : new Color(0xCC, 0xDA, 0xD6));
                g2.setStroke(new BasicStroke(isFocusOwner() ? 2f : 1.2f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 10, 10);
                g2.dispose();
            }
        };
        f.setOpaque(false);
        f.setBackground(new Color(0xF4, 0xF8, 0xF6));
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setForeground(TEXT_GRAY);
        f.setEchoChar((char) 0);       // show placeholder as text
        f.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        f.setPreferredSize(new Dimension(320, 40));
        f.setMaximumSize(new Dimension(320, 40));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Fake placeholder
        f.setText(placeholder);
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (String.valueOf(f.getPassword()).equals(placeholder)) {
                    f.setText(""); f.setEchoChar('●'); f.setForeground(TEXT_DARK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (f.getPassword().length == 0) {
                    f.setEchoChar((char) 0); f.setText(placeholder); f.setForeground(TEXT_GRAY);
                }
            }
        });
        return f;
    }

    private JButton primaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = getModel().isPressed()  ? SLATE_DK :
                             getModel().isRollover() ? SLATE.brighter() : SLATE;
                g2.setColor(base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth()  - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.setColor(WHITE);
                g2.setFont(getFont());
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        btn.setPreferredSize(new Dimension(320, 44));
        btn.setMaximumSize (new Dimension(320, 44));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JLabel styledLabel(String text, int size, int style, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", style, size));
        l.setForeground(color);
        return l;
    }

    private JLabel labelFor(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(TEXT_DARK);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JLabel small(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(TEXT_GRAY);
        return l;
    }

    private JLabel link(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(SLATE);
        l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return l;
    }

    private JSeparator divider() {
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(320, 1));
        sep.setForeground(new Color(0xDD, 0xE8, 0xE4));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        return sep;
    }

    // ════════════════════════════════════════════════════════════════════
    //  MAIN
    // ════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        // Start from Role Selection screen
        SwingUtilities.invokeLater(RoleSelectFrame::new);
    }
}