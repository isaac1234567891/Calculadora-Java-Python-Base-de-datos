import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

// ==========================================
// 1. CLASE PRINCIPAL DE INICIO
// ==========================================
public class CalculadoraIntegrada {
    public static void main(String[] args) {
        // Aplicar diseño moderno base de Java (Nimbus)
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Iniciar la aplicación en el hilo de eventos de Swing[cite: 1]
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}

// ==========================================
// 2. CONEXIÓN A BASE DE DATOS (MySQL)
// ==========================================
class ConexionDB {
    // IMPORTANTE: Cambia "tu_password" por tu contraseña real de MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/base_de_datos_calculadora?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "12345678";

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
            return null;
        }
    }
}

// ==========================================
// 3. UTILIDAD DE ESTILOS UI (Diseño Moderno)
// ==========================================
class EstilosUI {
    public static final Color COLOR_FONDO = Color.decode("#F3F4F6");
    public static final Color COLOR_PRIMARIO = Color.decode("#2563EB");
    public static final Color COLOR_TEXTO = Color.decode("#1F2937");
    public static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);

    public static void aplicarEstiloBoton(JButton boton) {
        boton.setBackground(COLOR_PRIMARIO);
        boton.setForeground(Color.WHITE);
        boton.setFont(FUENTE_NORMAL);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void aplicarEstiloCampo(JTextField campo) {
        campo.setFont(FUENTE_NORMAL);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#D1D5DB")),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
    }
}

// ==========================================
// 4. PANTALLA DE INICIO DE SESIÓN
// ==========================================
class LoginFrame extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtPassword;

    public LoginFrame() {
        setTitle("Acceso al Sistema");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(EstilosUI.COLOR_FONDO);
        setLayout(new GridBagLayout());

        JPanel panelCentral = new JPanel(new GridLayout(6, 1, 10, 10));
        panelCentral.setBackground(EstilosUI.COLOR_FONDO);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel lblTitulo = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        lblTitulo.setFont(EstilosUI.FUENTE_TITULO);
        lblTitulo.setForeground(EstilosUI.COLOR_TEXTO);
        
        txtEmail = new JTextField();
        EstilosUI.aplicarEstiloCampo(txtEmail);
        
        txtPassword = new JPasswordField();
        EstilosUI.aplicarEstiloCampo(txtPassword);

        JButton btnLogin = new JButton("Ingresar");
        EstilosUI.aplicarEstiloBoton(btnLogin);
        
        JButton btnRegister = new JButton("Crear Cuenta nueva");
        btnRegister.setContentAreaFilled(false);
        btnRegister.setForeground(EstilosUI.COLOR_PRIMARIO);
        btnRegister.setBorderPainted(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelCentral.add(lblTitulo);
        panelCentral.add(new JLabel("Correo Electrónico:"));
        panelCentral.add(txtEmail);
        panelCentral.add(new JLabel("Contraseña:"));
        panelCentral.add(txtPassword);
        panelCentral.add(new JLabel("")); // Espaciador

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 40, 5, 40);
        
        add(panelCentral, gbc);
        add(btnLogin, gbc);
        add(btnRegister, gbc);

        // Flujo: Iniciar Sesión -> Validar credenciales[cite: 1]
        btnLogin.addActionListener((ActionEvent e) -> {
            String email = txtEmail.getText();
            String password = new String(txtPassword.getPassword());

            try (Connection conn = ConexionDB.conectar()) {
                if (conn == null) {
                    JOptionPane.showMessageDialog(this, "Error de conexión a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String sql = "SELECT id_usuario, Nombre FROM Usuarios WHERE Email = ? AND Contraseña = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, email);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    int idUsuario = rs.getInt("id_usuario");
                    JOptionPane.showMessageDialog(this, "Bienvenido, " + rs.getString("Nombre"));
                    dispose();
                    new MainMenuFrame(idUsuario).setVisible(true); // Cargar Catálogo de funciones[cite: 1]
                } else {
                    JOptionPane.showMessageDialog(this, "Credenciales inválidas.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Flujo: Registrarse -> No tiene cuenta[cite: 1]
        btnRegister.addActionListener(e -> {
            dispose();
            new RegisterFrame().setVisible(true);
        });
    }
}

// ==========================================
// 5. PANTALLA DE REGISTRO
// ==========================================
class RegisterFrame extends JFrame {
    private JTextField txtNombre, txtEmail;
    private JPasswordField txtPassword;

    public RegisterFrame() {
        setTitle("Registro de Usuario");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(EstilosUI.COLOR_FONDO);
        setLayout(new GridBagLayout());

        JPanel panelCentral = new JPanel(new GridLayout(8, 1, 5, 5));
        panelCentral.setBackground(EstilosUI.COLOR_FONDO);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel lblTitulo = new JLabel("Registrarse", SwingConstants.CENTER);
        lblTitulo.setFont(EstilosUI.FUENTE_TITULO);
        
        txtNombre = new JTextField();
        EstilosUI.aplicarEstiloCampo(txtNombre);
        txtEmail = new JTextField();
        EstilosUI.aplicarEstiloCampo(txtEmail);
        txtPassword = new JPasswordField();
        EstilosUI.aplicarEstiloCampo(txtPassword);

        JButton btnSubmit = new JButton("Guardar Registro");
        EstilosUI.aplicarEstiloBoton(btnSubmit);
        
        JButton btnVolver = new JButton("Volver al Login");
        btnVolver.setContentAreaFilled(false);
        btnVolver.setForeground(EstilosUI.COLOR_TEXTO);
        btnVolver.setBorderPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelCentral.add(lblTitulo);
        panelCentral.add(new JLabel("Nombre Completo:"));
        panelCentral.add(txtNombre);
        panelCentral.add(new JLabel("Correo Electrónico:"));
        panelCentral.add(txtEmail);
        panelCentral.add(new JLabel("Contraseña:"));
        panelCentral.add(txtPassword);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 40, 5, 40);
        
        add(panelCentral, gbc);
        add(btnSubmit, gbc);
        add(btnVolver, gbc);

        // Flujo: Ingresar Datos -> Java Crea cuenta y estado de activación -> SQL db (guardar)[cite: 1]
        btnSubmit.addActionListener(e -> {
            String nombre = txtNombre.getText();
            String email = txtEmail.getText();
            String password = new String(txtPassword.getPassword());

            try (Connection conn = ConexionDB.conectar()) {
                if (conn == null) {
                    JOptionPane.showMessageDialog(this, "Error de conexión a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                conn.setAutoCommit(false); // Transacción para insertar en ambas tablas

                // 1. Insertar en la tabla Usuarios principal
                String sqlUsuario = "INSERT INTO Usuarios (Nombre, Email, Contraseña) VALUES (?, ?, ?)";
                PreparedStatement stmtUser = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS);
                stmtUser.setString(1, nombre);
                stmtUser.setString(2, email);
                stmtUser.setString(3, password);
                stmtUser.executeUpdate();

                ResultSet rsKeys = stmtUser.getGeneratedKeys();
                if (rsKeys.next()) {
                    int newUserId = rsKeys.getInt(1);
                    
                    // 2. Insertar en Registros_Usuarios (Estado de activación y token)[cite: 1]
                    String sqlRegistro = "INSERT INTO Registros_Usuarios (id_usuario, estado_cuenta, token_activado) VALUES (?, ?, ?)";
                    PreparedStatement stmtReg = conn.prepareStatement(sqlRegistro);
                    stmtReg.setInt(1, newUserId);
                    stmtReg.setString(2, "ACTIVO");
                    stmtReg.setString(3, UUID.randomUUID().toString());
                    stmtReg.executeUpdate();
                }

                conn.commit();
                JOptionPane.showMessageDialog(this, "Registro exitoso. Ya puede iniciar sesión.");
                dispose();
                new LoginFrame().setVisible(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnVolver.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
    }
}

// ==========================================
// 6. MENÚ PRINCIPAL (MÓDULO DE VALIDACIÓN)
// ==========================================
class MainMenuFrame extends JFrame {
    private int idUsuarioActual;

    public MainMenuFrame(int idUsuario) {
        this.idUsuarioActual = idUsuario; // Identificador necesario para el Módulo de Cálculo[cite: 1]
        
        setTitle("Catálogo de Funciones");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(EstilosUI.COLOR_FONDO);
        setLayout(new BorderLayout(20, 20));

        JLabel lblTitulo = new JLabel("Seleccione el Módulo de Cálculo", SwingConstants.CENTER);
        lblTitulo.setFont(EstilosUI.FUENTE_TITULO);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 20, 20));
        panelBotones.setBackground(EstilosUI.COLOR_FONDO);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Módulo de validación: Decisión entre Operación Básica o Trigonométrica[cite: 1]
        JButton btnBasica = new JButton("<html><center>Básica<br>(+, -, *, /)</center></html>");
        EstilosUI.aplicarEstiloBoton(btnBasica);
        btnBasica.setPreferredSize(new Dimension(150, 100));

        JButton btnTrigonometrica = new JButton("<html><center>Trigonométrica<br>(sin, cos, tan...)</center></html>");
        EstilosUI.aplicarEstiloBoton(btnTrigonometrica);
        btnTrigonometrica.setBackground(Color.decode("#10B981")); // Verde moderno

        panelBotones.add(btnBasica);
        panelBotones.add(btnTrigonometrica);
        add(panelBotones, BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Cerrar Sesión");
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setForeground(Color.RED);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(btnCerrar, BorderLayout.SOUTH);

        // Derivar al Módulo de Cálculo Java[cite: 1]
        btnBasica.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Redirigiendo a Módulo de Cálculo Java...");
            // Aquí se instanciará la UI para la calculadora básica en los siguientes pasos
        });

        // Derivar al Módulo de Cálculo Python[cite: 1]
        btnTrigonometrica.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Redirigiendo a Módulo de Cálculo Python...");
            // Aquí se instanciará la UI para la calculadora trigonométrica
        });

        // Módulo de Cierre[cite: 1]
        btnCerrar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Desea cerrar sesión?", "Cerrar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });
    }
}