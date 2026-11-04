import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculadoraIntegrada {

    public static void main(String[] args) {
        // Iniciar la aplicación mostrando la pantalla de Login
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}

// ==========================================
// PANTALLA DE INICIO DE SESIÓN
// ==========================================
class LoginFrame extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtPassword;

    public LoginFrame() {
        setTitle("Módulo de Acceso - Iniciar Sesión");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        // Componentes de la interfaz para ingresar email y contraseña[cite: 1]
        add(new JLabel("  Email:"));
        txtEmail = new JTextField();
        add(txtEmail);

        add(new JLabel("  Contraseña:"));
        txtPassword = new JPasswordField();
        add(txtPassword);

        JButton btnLogin = new JButton("Iniciar Sesión");
        JButton btnRegister = new JButton("Registrarse");

        add(btnLogin);
        add(btnRegister);

        // Acción de Iniciar Sesión -> Validar credenciales[cite: 1]
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = txtEmail.getText();
                String password = new String(txtPassword.getPassword());

                // NOTA: Aquí se debe implementar la consulta SQLite para validar credenciales[cite: 1]
                if (!email.isEmpty() && !password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "¿Credenciales Válidas? SI. Cargando catálogo...");
                    dispose(); // Cerrar login
                    new MainMenuFrame().setVisible(true); // Cargar Catálogo de funciones[cite: 1]
                } else {
                    JOptionPane.showMessageDialog(null, "¿Credenciales Válidas? NO. Ingrese datos válidos.");
                }
            }
        });

        // Acción de Registrarse -> Abrir pantalla de registro si no tiene cuenta[cite: 1]
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterFrame().setVisible(true);
            }
        });
    }
}

// ==========================================
// PANTALLA DE REGISTRO
// ==========================================
class RegisterFrame extends JFrame {
    private JTextField txtNombre, txtEmail;
    private JPasswordField txtPassword;

    public RegisterFrame() {
        setTitle("Registrarse");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        // Ingresar Datos (nombre, email, pass)[cite: 1]
        add(new JLabel("  Nombre:"));
        txtNombre = new JTextField();
        add(txtNombre);

        add(new JLabel("  Email:"));
        txtEmail = new JTextField();
        add(txtEmail);

        add(new JLabel("  Contraseña:"));
        txtPassword = new JPasswordField();
        add(txtPassword);

        JButton btnSubmit = new JButton("Guardar Registro");
        add(new JLabel("")); // Espaciador
        add(btnSubmit);

        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // NOTA: Aquí Java crea cuenta y estado de activación, luego SQLite guarda el registro de usuario[cite: 1]
                JOptionPane.showMessageDialog(null, "Registro guardado en SQLite. Puede iniciar sesión.");
                dispose(); // Cerrar registro y volver al login
            }
        });
    }
}

// ==========================================
// MENÚ PRINCIPAL (Catálogo de Funciones)
// ==========================================
class MainMenuFrame extends JFrame {

    public MainMenuFrame() {
        setTitle("Catálogo de Funciones");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Seleccione el tipo de operación", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Opciones del Módulo de validación: Operación básica o avanzada/trigonométrica[cite: 1]
        JButton btnBasica = new JButton("Operación Básica");
        JButton btnTrigonometrica = new JButton("Op. Trigonométrica");

        panelBotones.add(btnBasica);
        panelBotones.add(btnTrigonometrica);
        add(panelBotones, BorderLayout.CENTER);

        // Flujo hacia la calculadora Básica (Módulo de Cálculo Java)[cite: 1]
        btnBasica.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Abriendo interfaz de Módulo de Cálculo Java (+, -, *, /)");
                // Aquí se instanciaría el JFrame de la calculadora básica
            }
        });

        // Flujo hacia la calculadora Trigonométrica (Módulo de Cálculo Python)[cite: 1]
        btnTrigonometrica.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Abriendo interfaz para invocar subproceso de Python");
                // Aquí se instanciaría el JFrame que pedirá los datos para pasarlos a Python
            }
        });
        
        // Módulo de Cierre[cite: 1]
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        add(btnCerrarSesion, BorderLayout.SOUTH);
        btnCerrarSesion.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null, "¿Desea cerrar sesión?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });
    }
}
