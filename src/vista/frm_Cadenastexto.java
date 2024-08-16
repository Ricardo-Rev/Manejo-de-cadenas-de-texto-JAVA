/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;
// Importación de las librerías necesarias
import javax.swing.*; // Importa todas las clases del paquete javax.swing, necesarias para crear interfaces gráficas con Swing
import java.awt.event.*; // Importa todas las clases del paquete java.awt.event, necesarias para manejar eventos en la interfaz gráfica
import java.io.File; // Importa la clase File del paquete java.io, utilizada para representar archivos y directorios
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException; // Importa la clase IOException del paquete java.io, utilizada para manejar errores de entrada/salida
import modelo.Manejo_archivos; // Importa la clase Manejo_archivos del paquete modelo, utilizada para manejar operaciones de archivo
import modelo.Texto_analizador; // Importa la clase Texto_analizador del paquete modelo, utilizada para analizar texto
import java.util.Map; // Importa la interfaz Map del paquete java.util, utilizada para trabajar con colecciones de pares clave-valor

/**
 *
 * @author DELL
 */
public class frm_Cadenastexto extends javax.swing.JFrame {
    // Variable para almacenar el archivo actual
    private File archivoActual;
    
    // Instancia de la clase Manejo_archivos para manejar operaciones de archivo
    private final Manejo_archivos manejoArchivos = new Manejo_archivos();

    /**
     * Creates new form frm_Cadenastexto
     */
    // Constructor de la clase frm_Cadenastexto
    public frm_Cadenastexto() {
        initComponents(); // Inicializa los componentes de la interfaz gráfica
        setAtajos(); // Configura los atajos de teclado
    }
    
    // Método para mostrar mensajes de error
    private void mostrarError(String mensaje) {
        // Muestra un cuadro de diálogo con el mensaje de error
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void setAtajos() {
        // Llama a configureAtajos() con el JMenuItem mni_x, el código de tecla x, el modificador CTRL, y la acción()
        configureAtajos(mni_abrir, KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK, e -> abrirArchivo()); // CTRL + A para "Abrir"
        configureAtajos(mni_guardar_como, KeyEvent.VK_F12, 0, e -> guardarArchivoComo()); // F12 para "Guardar Como"
        configureAtajos(mni_guardar, KeyEvent.VK_G, KeyEvent.CTRL_DOWN_MASK, e -> guardarArchivo()); // CTRL + G para "Guardar"
        configureAtajos(mni_copiar, KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK, e -> txta_ingreso.copy()); // CTRL + C para "Copiar"
        configureAtajos(mni_cortar, KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK, e -> txta_ingreso.cut()); // CTRL + X para "Cortar"
        configureAtajos(mni_pegar, KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK, e -> txta_ingreso.paste()); // CTRL + V para "Pegar"
        configureAtajos(mni_buscar, KeyEvent.VK_B, KeyEvent.CTRL_DOWN_MASK, e -> buscarTexto()); // CTRL + B para "Buscar"
        configureAtajos(mni_reemplazar, KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK, e -> reemplazarTexto()); // CTRL + R para "Reemplazar"
    }

    // Método para configurar un atajo de teclado
    private void configureAtajos(JMenuItem menuItem, int keyCode, int modifiers, ActionListener action) {
        // Establece el atajo de teclado para el JMenuItem
        // KeyStroke.getKeyStroke() crea un atajo de teclado con el código de tecla y los modificadores especificados
        menuItem.setAccelerator(KeyStroke.getKeyStroke(keyCode, modifiers));
        // Asocia el ActionListener al JMenuItem para manejar la acción cuando se activa el atajo de teclado
        menuItem.addActionListener(action);
    }

    // Abre un archivo y carga su contenido en el JTextArea
    private void abrirArchivo() {
        // Crea un JFileChooser para seleccionar el archivo
        JFileChooser fileChooser = new JFileChooser();
        // Muestra el cuadro de diálogo para abrir un archivo
        int seleccion = fileChooser.showOpenDialog(this);
        // Si el usuario aprueba la selección del archivo
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            // Guarda el archivo seleccionado en una variable
            archivoActual = fileChooser.getSelectedFile();
            try {
                // Lee el contenido del archivo usando manejoArchivos
                String contenido = manejoArchivos.leerArchivo(archivoActual);
                // Establece el contenido en el JTextArea
                txta_ingreso.setText(contenido);
            } catch (IOException ex) {
                // Muestra un mensaje de error si ocurre una excepción al leer el archivo
                JOptionPane.showMessageDialog(this, "Error al abrir el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Guarda el contenido del JTextArea en el archivo actual
    private void guardarArchivo() {
        // Si hay un archivo actual seleccionado
        if (archivoActual != null) {
            try {
                // Escribe el contenido del JTextArea en el archivo actual
                manejoArchivos.escribirArchivo(archivoActual, txta_ingreso.getText());
            } catch (IOException ex) {
                // Muestra un mensaje de error si ocurre una excepción al escribir en el archivo
                JOptionPane.showMessageDialog(this, "Error al guardar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Si no hay un archivo actual, llama al método para guardar el archivo con un nuevo nombre
            guardarArchivoComo();
        }
    }

    // Muestra un cuadro de diálogo para seleccionar un archivo para guardar
    private void guardarArchivoComo() {
        // Crea un JFileChooser para seleccionar el archivo
        JFileChooser fileChooser = new JFileChooser();

        // Establece el filtro para que solo se muestren archivos .txt
        FileNameExtensionFilter filtroTxt = new FileNameExtensionFilter("Archivos de texto (*.txt)", "txt");
        fileChooser.setFileFilter(filtroTxt);

        // Muestra el cuadro de diálogo para guardar un archivo
        int seleccion = fileChooser.showSaveDialog(this);

        // Si el usuario aprueba la selección del archivo
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            // Obtiene el archivo seleccionado
            File archivoSeleccionado = fileChooser.getSelectedFile();

            // Si el archivo no tiene la extensión .txt, la agrega
            if (!archivoSeleccionado.getName().endsWith(".txt")) {
                archivoSeleccionado = new File(archivoSeleccionado.getAbsolutePath() + ".txt");
            }

            // Guarda el archivo seleccionado en una variable
            archivoActual = archivoSeleccionado;

            // Llama al método para guardar el archivo en la ubicación seleccionada
            guardarArchivo();
        }
    }

    // Busca un texto en el JTextArea y lo selecciona si se encuentra
    private void buscarTexto() {
        // Muestra un cuadro de diálogo para ingresar el texto a buscar
        String textoBuscado = JOptionPane.showInputDialog(this, "Ingrese el texto a buscar:");
        // Si el texto buscado no es nulo ni vacío
        if (textoBuscado != null && !textoBuscado.isEmpty()) {
            // Obtiene el contenido del JTextArea
            String contenido = txta_ingreso.getText();
            // Busca el índice de la primera aparición del texto buscado
            int index = contenido.indexOf(textoBuscado);
            // Si el texto buscado se encuentra en el contenido
            if (index >= 0) {
                // Selecciona el texto encontrado en el JTextArea
                txta_ingreso.select(index, index + textoBuscado.length());
            } else {
                // Muestra un mensaje si el texto buscado no se encuentra
                JOptionPane.showMessageDialog(this, "Texto no encontrado.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    // Reemplaza el texto en el JTextArea con un nuevo texto
    private void reemplazarTexto() {
        // Crea campos de texto para buscar y reemplazar
        JTextField txtBuscar = new JTextField();
        JTextField txtReemplazar = new JTextField();
        // Muestra un cuadro de diálogo con los campos de texto para buscar y reemplazar
        Object[] mensaje = {
            "Buscar:", txtBuscar,
            "Reemplazar con:", txtReemplazar
        };
        int opcion = JOptionPane.showConfirmDialog(this, mensaje, "Reemplazar texto", JOptionPane.OK_CANCEL_OPTION);
        // Si el usuario confirma la acción
        if (opcion == JOptionPane.OK_OPTION) {
            // Obtiene el texto a buscar y el texto de reemplazo
            String textoBuscado = txtBuscar.getText();
            String textoReemplazo = txtReemplazar.getText();
            // Si el texto a buscar no está vacío
            if (!textoBuscado.isEmpty()) {
                // Obtiene el contenido del JTextArea
                String contenido = txta_ingreso.getText();
                // Reemplaza todas las ocurrencias del texto buscado por el texto de reemplazo
                contenido = contenido.replace(textoBuscado, textoReemplazo);
                // Establece el contenido modificado en el JTextArea
                txta_ingreso.setText(contenido);
            }
        }
    }

    // Procesa el texto ingresado y muestra los resultados
    private void procesarTexto() {
        // Obtiene el texto del JTextArea
        String texto = txta_ingreso.getText();

        // Verifica si el texto está vacío
        if (texto == null || texto.trim().isEmpty()) {
            // Muestra una advertencia si el texto está vacío
            JOptionPane.showMessageDialog(this, "Por favor ingrese un texto.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Crea una instancia del analizador de texto
        Texto_analizador analizador = new Texto_analizador(texto);

        // Muestra los resultados en los JLabel correspondientes
        lbl_resultado1.setText(String.valueOf(analizador.obtenerLongitud()));        // Longitud del texto
        lbl_resultado2.setText(String.valueOf(analizador.contarPalabras()));        // Número de palabras
        lbl_resultado3.setText(analizador.obtenerPrimeraLetra());                   // Primera letra
        lbl_resultado4.setText(analizador.obtenerUltimaLetra());                    // Última letra
        lbl_resultado5.setText(analizador.obtenerLetraCentral());                   // Letra central
        lbl_resultado6.setText(analizador.obtenerPrimeraPalabra());                 // Primera palabra
        lbl_resultado7.setText(analizador.obtenerPalabraCentral());                 // Palabra central
        lbl_resultado8.setText(analizador.obtenerUltimaPalabra());                  // Última palabra

        // Muestra las repeticiones de vocales considerando tildes y mayúsculas
        Map<Character, Integer> conteoVocales = analizador.contarRepeticionesVocales();
        lbl_resultado_a.setText(String.valueOf(conteoVocales.getOrDefault('a', 0)));
        lbl_resultado_e.setText(String.valueOf(conteoVocales.getOrDefault('e', 0)));
        lbl_resultado_i.setText(String.valueOf(conteoVocales.getOrDefault('i', 0)));
        lbl_resultado_o.setText(String.valueOf(conteoVocales.getOrDefault('o', 0)));
        lbl_resultado_u.setText(String.valueOf(conteoVocales.getOrDefault('u', 0)));

        // Muestra la cantidad de palabras con longitud par e impar
        lbl_resultado_par.setText(String.valueOf(analizador.contarPalabrasPar()));
        lbl_resultado_impar.setText(String.valueOf(analizador.contarPalabrasImpar()));

        // Traduce el texto a la clave "Murciélago" y lo muestra en un JTextArea
        txta_traduccion.setText(analizador.traducirAMurcielago());
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lbl_titulo1 = new javax.swing.JLabel();
        lbl_intrucciones1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txta_ingreso = new javax.swing.JTextArea();
        bt_procesar = new javax.swing.JButton();
        lbl_longitud_text = new javax.swing.JLabel();
        lbl_resultado1 = new javax.swing.JLabel();
        lbl_repeticion_a = new javax.swing.JLabel();
        lbl_total_palabras = new javax.swing.JLabel();
        lbl_resultado2 = new javax.swing.JLabel();
        lbl_repeticion_e = new javax.swing.JLabel();
        lbl_primer_letra = new javax.swing.JLabel();
        lbl_resultado3 = new javax.swing.JLabel();
        lbl_repeticion_i = new javax.swing.JLabel();
        lbl_resultado4 = new javax.swing.JLabel();
        lbl_repeticion_o = new javax.swing.JLabel();
        lbl_ultima_letra = new javax.swing.JLabel();
        lbl_resultado5 = new javax.swing.JLabel();
        lbl_repeticion_u = new javax.swing.JLabel();
        lbl_letra_central = new javax.swing.JLabel();
        lbl_resultado6 = new javax.swing.JLabel();
        lbl_par = new javax.swing.JLabel();
        lbl_primer_palabra = new javax.swing.JLabel();
        lbl_resultado_a = new javax.swing.JLabel();
        lbl_resultado_e = new javax.swing.JLabel();
        lbl_resultado_i = new javax.swing.JLabel();
        lbl_resultado_o = new javax.swing.JLabel();
        lbl_resultado_u = new javax.swing.JLabel();
        lbl_resultado_par = new javax.swing.JLabel();
        lbl_resultado_impar = new javax.swing.JLabel();
        lbl_resultado7 = new javax.swing.JLabel();
        lbl_impar = new javax.swing.JLabel();
        lbl_palabra_central = new javax.swing.JLabel();
        lbl_titulo2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txta_traduccion = new javax.swing.JTextArea();
        lbl_ultima_palabra = new javax.swing.JLabel();
        lbl_resultado8 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        mnb_archivo = new javax.swing.JMenu();
        mni_abrir = new javax.swing.JMenuItem();
        mni_guardar = new javax.swing.JMenuItem();
        mni_guardar_como = new javax.swing.JMenuItem();
        mnb_editar = new javax.swing.JMenu();
        mni_copiar = new javax.swing.JMenuItem();
        mni_cortar = new javax.swing.JMenuItem();
        mni_pegar = new javax.swing.JMenuItem();
        mni_buscar = new javax.swing.JMenuItem();
        mni_reemplazar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lbl_titulo1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lbl_titulo1.setText("MANEJO DE CADENAS");

        lbl_intrucciones1.setText("Ingrese un texto o abra un archivo");

        txta_ingreso.setColumns(20);
        txta_ingreso.setRows(5);
        jScrollPane1.setViewportView(txta_ingreso);

        bt_procesar.setBackground(new java.awt.Color(0, 0, 0));
        bt_procesar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        bt_procesar.setForeground(new java.awt.Color(255, 255, 255));
        bt_procesar.setText("PROCESAR");
        bt_procesar.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 153, 0)));
        bt_procesar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_procesarActionPerformed(evt);
            }
        });

        lbl_longitud_text.setText("Longitud del texto...........");

        lbl_resultado1.setText("----");
        lbl_resultado1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        lbl_repeticion_a.setText("Repeticiones de \"A\", \"a\" ó \"á\"");

        lbl_total_palabras.setText("Total de palabras.............");

        lbl_resultado2.setText("----");
        lbl_resultado2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        lbl_repeticion_e.setText("Repeticiones de \"E\", \"e\" ó \"é\"");

        lbl_primer_letra.setText("Primer letra del texto......");

        lbl_resultado3.setText("----");
        lbl_resultado3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        lbl_repeticion_i.setText("Repeticiones de \"I\", \"i\" ó \"í\"");

        lbl_resultado4.setText("----");
        lbl_resultado4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        lbl_repeticion_o.setText("Repeticiones de \"O\", \"o\" ó \"ó\"");

        lbl_ultima_letra.setText("Última letra del texto......");

        lbl_resultado5.setText("----");
        lbl_resultado5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        lbl_repeticion_u.setText("Repeticiones de \"U\", \"u\" ó \"ú\"");

        lbl_letra_central.setText("Letra central del texto.....");

        lbl_resultado6.setText("----");
        lbl_resultado6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        lbl_par.setText("Palabras con cantidad de caracter par");

        lbl_primer_palabra.setText("Primer palabra.................");

        lbl_resultado_a.setText("----");
        lbl_resultado_a.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        lbl_resultado_e.setText("----");
        lbl_resultado_e.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        lbl_resultado_i.setText("----");
        lbl_resultado_i.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        lbl_resultado_o.setText("----");
        lbl_resultado_o.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        lbl_resultado_u.setText("----");
        lbl_resultado_u.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        lbl_resultado_par.setText("----");
        lbl_resultado_par.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        lbl_resultado_impar.setText("----");
        lbl_resultado_impar.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        lbl_resultado7.setText("----");
        lbl_resultado7.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        lbl_impar.setText("Palabras con cantidad de caracter impar");

        lbl_palabra_central.setText("Palabra central................");

        lbl_titulo2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lbl_titulo2.setText("TRADUCCIÓN A CLAVE MURCIELAGO");

        txta_traduccion.setColumns(20);
        txta_traduccion.setRows(5);
        jScrollPane2.setViewportView(txta_traduccion);

        lbl_ultima_palabra.setText("Última palabra.................");

        lbl_resultado8.setText("----");
        lbl_resultado8.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbl_intrucciones1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(lbl_ultima_letra)
                                            .addGap(21, 21, 21)
                                            .addComponent(lbl_resultado4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(lbl_primer_letra)
                                            .addGap(21, 21, 21)
                                            .addComponent(lbl_resultado3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(lbl_total_palabras)
                                            .addGap(21, 21, 21)
                                            .addComponent(lbl_resultado2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(lbl_longitud_text)
                                            .addGap(19, 19, 19)
                                            .addComponent(lbl_resultado1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                                    .addComponent(lbl_ultima_palabra)
                                                    .addGap(21, 21, 21)
                                                    .addComponent(lbl_resultado8, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                                    .addComponent(lbl_palabra_central)
                                                    .addGap(23, 23, 23)
                                                    .addComponent(lbl_resultado7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                                    .addComponent(lbl_primer_palabra)
                                                    .addGap(21, 21, 21)
                                                    .addComponent(lbl_resultado6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(lbl_letra_central)
                                                .addGap(18, 18, 18)
                                                .addComponent(lbl_resultado5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(63, 63, 63)))
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addGap(103, 103, 103)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(lbl_resultado_par, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(lbl_repeticion_e)
                                                            .addComponent(lbl_repeticion_a)
                                                            .addComponent(lbl_repeticion_i)
                                                            .addComponent(lbl_repeticion_o)
                                                            .addComponent(lbl_repeticion_u))
                                                        .addGap(26, 26, 26)
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                            .addComponent(lbl_resultado_a, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(lbl_resultado_e, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(lbl_resultado_i, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(lbl_resultado_o, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(lbl_resultado_u, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                    .addComponent(lbl_resultado_impar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(40, 40, 40))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(58, 58, 58)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(lbl_par)
                                                    .addComponent(lbl_impar)))))))
                            .addComponent(jScrollPane2)
                            .addComponent(jScrollPane1)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(199, 199, 199)
                        .addComponent(lbl_titulo1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(111, 111, 111)
                        .addComponent(lbl_titulo2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(281, 281, 281)
                        .addComponent(bt_procesar, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lbl_titulo1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_intrucciones1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(bt_procesar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_longitud_text)
                    .addComponent(lbl_resultado1)
                    .addComponent(lbl_repeticion_a)
                    .addComponent(lbl_resultado_a))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_total_palabras)
                    .addComponent(lbl_resultado2)
                    .addComponent(lbl_repeticion_e)
                    .addComponent(lbl_resultado_e))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_primer_letra)
                    .addComponent(lbl_resultado3)
                    .addComponent(lbl_repeticion_i)
                    .addComponent(lbl_resultado_i))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_ultima_letra)
                    .addComponent(lbl_resultado4)
                    .addComponent(lbl_repeticion_o)
                    .addComponent(lbl_resultado_o))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_resultado5, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl_repeticion_u)
                        .addComponent(lbl_resultado_u, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_letra_central)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_resultado6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl_primer_palabra)
                        .addComponent(lbl_par)
                        .addComponent(lbl_resultado_par)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_palabra_central)
                    .addComponent(lbl_resultado7)
                    .addComponent(lbl_impar)
                    .addComponent(lbl_resultado_impar))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_ultima_palabra)
                    .addComponent(lbl_resultado8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_titulo2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mnb_archivo.setText("Archivo");

        mni_abrir.setText("Abrir");
        mni_abrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mni_abrirActionPerformed(evt);
            }
        });
        mnb_archivo.add(mni_abrir);

        mni_guardar.setText("Guardar");
        mni_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mni_guardarActionPerformed(evt);
            }
        });
        mnb_archivo.add(mni_guardar);

        mni_guardar_como.setText("Guardar como");
        mni_guardar_como.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mni_guardar_comoActionPerformed(evt);
            }
        });
        mnb_archivo.add(mni_guardar_como);

        jMenuBar1.add(mnb_archivo);

        mnb_editar.setText("Editar");

        mni_copiar.setText("Copiar");
        mni_copiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mni_copiarActionPerformed(evt);
            }
        });
        mnb_editar.add(mni_copiar);

        mni_cortar.setText("Cortar");
        mni_cortar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mni_cortarActionPerformed(evt);
            }
        });
        mnb_editar.add(mni_cortar);

        mni_pegar.setText("Pegar");
        mni_pegar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mni_pegarActionPerformed(evt);
            }
        });
        mnb_editar.add(mni_pegar);

        mni_buscar.setText("Buscar");
        mni_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mni_buscarActionPerformed(evt);
            }
        });
        mnb_editar.add(mni_buscar);

        mni_reemplazar.setText("Reemplazar");
        mni_reemplazar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mni_reemplazarActionPerformed(evt);
            }
        });
        mnb_editar.add(mni_reemplazar);

        jMenuBar1.add(mnb_editar);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 636, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mni_abrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mni_abrirActionPerformed

    }//GEN-LAST:event_mni_abrirActionPerformed

    private void mni_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mni_guardarActionPerformed

    }//GEN-LAST:event_mni_guardarActionPerformed

    private void mni_guardar_comoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mni_guardar_comoActionPerformed

    }//GEN-LAST:event_mni_guardar_comoActionPerformed

    private void mni_copiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mni_copiarActionPerformed

    }//GEN-LAST:event_mni_copiarActionPerformed

    private void mni_cortarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mni_cortarActionPerformed

    }//GEN-LAST:event_mni_cortarActionPerformed

    private void mni_pegarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mni_pegarActionPerformed

    }//GEN-LAST:event_mni_pegarActionPerformed

    private void mni_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mni_buscarActionPerformed

    }//GEN-LAST:event_mni_buscarActionPerformed

    private void mni_reemplazarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mni_reemplazarActionPerformed

    }//GEN-LAST:event_mni_reemplazarActionPerformed

    private void bt_procesarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_procesarActionPerformed
        procesarTexto();
    }//GEN-LAST:event_bt_procesarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frm_Cadenastexto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frm_Cadenastexto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frm_Cadenastexto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frm_Cadenastexto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frm_Cadenastexto().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_procesar;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_impar;
    private javax.swing.JLabel lbl_intrucciones1;
    private javax.swing.JLabel lbl_letra_central;
    private javax.swing.JLabel lbl_longitud_text;
    private javax.swing.JLabel lbl_palabra_central;
    private javax.swing.JLabel lbl_par;
    private javax.swing.JLabel lbl_primer_letra;
    private javax.swing.JLabel lbl_primer_palabra;
    private javax.swing.JLabel lbl_repeticion_a;
    private javax.swing.JLabel lbl_repeticion_e;
    private javax.swing.JLabel lbl_repeticion_i;
    private javax.swing.JLabel lbl_repeticion_o;
    private javax.swing.JLabel lbl_repeticion_u;
    private javax.swing.JLabel lbl_resultado1;
    private javax.swing.JLabel lbl_resultado2;
    private javax.swing.JLabel lbl_resultado3;
    private javax.swing.JLabel lbl_resultado4;
    private javax.swing.JLabel lbl_resultado5;
    private javax.swing.JLabel lbl_resultado6;
    private javax.swing.JLabel lbl_resultado7;
    private javax.swing.JLabel lbl_resultado8;
    private javax.swing.JLabel lbl_resultado_a;
    private javax.swing.JLabel lbl_resultado_e;
    private javax.swing.JLabel lbl_resultado_i;
    private javax.swing.JLabel lbl_resultado_impar;
    private javax.swing.JLabel lbl_resultado_o;
    private javax.swing.JLabel lbl_resultado_par;
    private javax.swing.JLabel lbl_resultado_u;
    private javax.swing.JLabel lbl_titulo1;
    private javax.swing.JLabel lbl_titulo2;
    private javax.swing.JLabel lbl_total_palabras;
    private javax.swing.JLabel lbl_ultima_letra;
    private javax.swing.JLabel lbl_ultima_palabra;
    private javax.swing.JMenu mnb_archivo;
    private javax.swing.JMenu mnb_editar;
    private javax.swing.JMenuItem mni_abrir;
    private javax.swing.JMenuItem mni_buscar;
    private javax.swing.JMenuItem mni_copiar;
    private javax.swing.JMenuItem mni_cortar;
    private javax.swing.JMenuItem mni_guardar;
    private javax.swing.JMenuItem mni_guardar_como;
    private javax.swing.JMenuItem mni_pegar;
    private javax.swing.JMenuItem mni_reemplazar;
    private javax.swing.JTextArea txta_ingreso;
    private javax.swing.JTextArea txta_traduccion;
    // End of variables declaration//GEN-END:variables

}
