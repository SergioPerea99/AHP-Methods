/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ahp_algorithm;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author Sergio
 */
public class AHP_Algorithm extends javax.swing.JFrame {
    
    //ATRIBUTOS
    private AHP_Interface interfaz;
    private ArrayList<String> alternativas, criterios;
    private ArrayList<ArrayList<Double>> matriz_c_c, matriz_a_c;
    private ArrayList<ArrayList<ArrayList<Double>>> vector_matrices_a_c;
    private Integer contador_pasos;
    private ArrayList<Double> pesosCriterios;
    private Double CI, CR;
    private ArrayList<Double> v_CI, v_CR;
    private RealVector autoVector;
    private Double[] CR_SAATY = {0.58, 0.9, 1.12, 1.24, 1.32, 1.41, 1.45, 1.49}; 
    
    
    //DATOS ESTÁTICOS: EJERCICIO 1
    ArrayList<ArrayList<ArrayList<Double>>> criterios_prioridades_locales = new ArrayList<>();
        //CRITERIO 1
    private Double[] vector_RPs_CPs_cr1_ej1 = {0.0, 20.0, 41.2, 100.0, 82.4, 160.0, 123.6, 164.8, 206.0 };
    private Double[] vector_prio_local_cr1_ej1 = {0.336, 0.277, 0.132, 0.058, 0.086, 0.029, 0.032, 0.026, 0.023};
        //CRITERIO 2
    private Double[] vector_RPs_CPs_cr2_ej1 = {0.0, 5000.0, 50914.4, 95000.0, 101828.8, 180000.0, 152743.2, 203657.6, 254573.0};
    private Double[] vector_prio_local_cr2_ej1 = {0.32, 0.261, 0.139, 0.075, 0.074, 0.032, 0.047, 0.031, 0.022};
        //CRITERIO 3
    private Double[] vector_RPs_CPs_cr3_ej1 = {0.0, 5000.0, 7025.4, 19000.0, 14050.8, 27000.0, 21076.2, 28101.6, 35127.0};
    private Double[] vector_prio_local_cr3_ej1 = {0.459, 0.138, 0.124, 0.063, 0.069, 0.04, 0.048, 0.034, 0.026};
        //CRITERIO 4
    private Double[] vector_RPs_CPs_cr4_ej1 = {0.0, 300.0, 764.8, 1800.0, 1529.6, 2500.0, 2294.4, 3059.2, 3824.0};
    private Double[] vector_prio_local_cr4_ej1 = {0.308, 0.259, 0.149, 0.066, 0.071, 0.034, 0.06, 0.032, 0.023};
        //CRITERIO 5
    private Double[] vector_RPs_CPs_cr5_ej1 = {0.00, 300.0, 321.6, 600.0, 643.2, 900.0, 964.8, 1286.4, 1608.0};
    private Double[] vector_prio_local_cr5_ej1 = {0.385, 0.262, 0.112, 0.07, 0.062, 0.035, 0.033, 0.023, 0.018};
    
        //CP PRIORIDADES LOCALES.
    Double[] cp1_ej1 = {0.277, 0.261, 0.138, 0.259, 0.262}; //Prioridades locales de CP1 por cada criterio.
    Double[] cp2_ej1 = {0.058, 0.075, 0.063, 0.066, 0.07}; //Prioridades locales de CP2 por cada criterio.
    Double[] cp3_ej1 = {0.029, 0.032, 0.04, 0.034, 0.035}; //Prioridades locales de CP3 por cada criterio.
    ArrayList<Double> v_cp1_ej1, v_cp2_ej1, v_cp3_ej1;
    
    
    // DATOS ESTÁTICOS EJERCICIO 2.
    //CRITERIO 1
    private Double[] vector_RPs_CPs_cr1_ej2 = {0.0, 0.102804448, 0.211777163, 0.423554327, 0.514022241, 0.63533149, 0.822435586, 0.847108653, 1.058885817};
    private Double[] vector_prio_local_cr1_ej2 = {0.451518896, 0.248480424, 0.136744047, 0.075253149, 0.041413404, 0.020706702, 0.010353351, 0.010353351, 0.005176676};
        //CRITERIO 2
    private Double[] vector_RPs_CPs_cr2_ej2 = {0.0, 8.120327532, 82.68832082, 154.2862231, 165.3766416, 248.0649625, 292.3317912, 330.7532833, 413.4416041};
    private Double[] vector_prio_local_cr2_ej2 = {0.401907655, 0.401907655, 0.100476914, 0.037678843, 0.037678843, 0.011868092, 0.004984286, 0.002742958, 0.000754754};
        //CRITERIO 3
    private Double[] vector_RPs_CPs_cr3_ej2 = {0.0, 9.428529413, 13.24783811, 26.49567622, 35.82841177, 39.74351432, 50.91405883, 52.99135243, 66.23919054};
    private Double[] vector_prio_local_cr3_ej2 = {0.658940706, 0.181314523, 0.099781227, 0.029176208, 0.01706235, 0.009765757, 0.001863167, 0.001863167, 0.000232896};
        //CRITERIO 4
    private Double[] vector_RPs_CPs_cr4_ej2 = {0.0, 1.535569639, 3.914678867, 7.829357733, 9.213417835, 11.7440366, 12.79641366, 15.65871547, 19.57339433};
    private Double[] vector_prio_local_cr4_ej2 = {0.494208494, 0.247104247, 0.123552124, 0.061776062, 0.030888031, 0.015444015, 0.015444015, 0.007722008, 0.003861004};
        //CRITERIO 5
    private Double[] vector_RPs_CPs_cr5_ej2 = {0.0, 4.982733839, 5.341490676, 9.965467679, 10.68298135, 14.94820152, 16.02447203, 21.3659627, 26.70745338};
    private Double[] vector_prio_local_cr5_ej2 = {0.641881438, 0.187687271, 0.109760182, 0.032094072, 0.018768727, 0.005488009, 0.003209407, 0.000837627, 0.000273267};
    
        //CP PRIORIDADES LOCALES.
    Double[] cp1_ej2 = {0.248480424, 0.401907655, 0.181314523, 0.247104247, 0.187687271}; //Prioridades locales de CP1 por cada criterio.
    Double[] cp2_ej2 = {0.041413404, 0.037678843, 0.01706235, 0.030888031, 0.018768727}; //Prioridades locales de CP2 por cada criterio.
    Double[] cp3_ej2 = {0.010353351, 0.004984286, 0.001863167, 0.015444015, 0.003209407}; //Prioridades locales de CP3 por cada criterio.
    ArrayList<Double> v_cp1_ej2, v_cp2_ej2, v_cp3_ej2;
    
    
    //Matriz de prioridades locales y globales 
    ArrayList<ArrayList<Double>> matriz_prioridades_locales_globales = new ArrayList<>();
    
    //ATRIBUTO FINAL
    private ArrayList<String> ranking_alternativas;
    
    /**
     * Creates new form AHP_Algorithm
     */
    public AHP_Algorithm() {
        matriz_a_c =  new ArrayList<>();
        matriz_c_c = new ArrayList<>();
        vector_matrices_a_c = new ArrayList<ArrayList<ArrayList<Double>>>();
        pesosCriterios = new ArrayList<>();
        ranking_alternativas = new ArrayList<>();
        contador_pasos = 0;
        CR = CI = -1.0;
        v_CI = new ArrayList<>();
        v_CR = new ArrayList<>();
        
        initComponents();
        jTable1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() == 1) {
                    jTable1 = (JTable) e.getSource();
                    final int fila = jTable1.getSelectedRow();
                    final int columna = jTable1.getSelectedColumn();

                    final String urObjctInCell = (String) jTable1.getValueAt(fila, columna);
                    
                    if (jTable1.getValueAt(fila, columna) == null && jTable1.getValueAt(columna - 1, fila + 1) != null)
                        jTable1.setValueAt("1/" + jTable1.getValueAt(columna - 1, fila + 1), fila, columna);
                    
                }
            }
        }
        );
        
    }
    
    public void setAHP(AHP_Interface _interfaz, ArrayList<String> _alternativas, ArrayList<String> _criterios){
        interfaz = _interfaz;
        alternativas = _alternativas;
        criterios = _criterios;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        SIGUIENTE = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        SIGUIENTE.setText("SIGUIENTE");
        SIGUIENTE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SIGUIENTEActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(165, Short.MAX_VALUE)
                .addComponent(SIGUIENTE, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(162, 162, 162))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(SIGUIENTE, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SIGUIENTEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SIGUIENTEActionPerformed
        // TODO add your handling code here:
        
        if (contador_pasos == 0){ //SE TIENE QUE RELLENAR LA MATRIZ DE COMPARACIÓN DE CRITERIOS
            v_CR.clear();
            ObtenerMatriz(matriz_c_c, criterios.size(), criterios.size());
            
            //Una vez termina de obtener la matriz, calcula el Wj.
            
            pesosCriterios = calcula_pesos_CI_CR(matriz_c_c,matriz_c_c.size(), matriz_c_c.size());
            System.out.println(pesosCriterios);
            
            /*calcula_prioridades_criterios_ej1();
            calcula_prioridades_alternativas_ej1(pesosCriterios);
            calcula_prioridades_CP_ej1(pesosCriterios);
            mostrarClasificaicion_alternativas_clases_ej1();*/
            
            calcula_prioridades_criterios_ej2();
            calcula_prioridades_alternativas_ej2(pesosCriterios);
            calcula_prioridades_CP_ej2(pesosCriterios);
            mostrarClasificaicion_alternativas_clases_ej2();
            
            interfaz.setVisible(true);
            dispose();
            //generarMatriz(false, criterios.get(contador_pasos));
            contador_pasos++;
        }
        
        if (contador_pasos > 1){ //SE TIENE QUE RELLENAR EN BUCLE LAS MATRICES DE COMPARACIÓN DE TODAS LAS ALTERNATIVAS RESPECTO A UN CRITERIO
            matriz_a_c.clear();
            ObtenerMatriz(matriz_a_c, alternativas.size(), alternativas.size());
            vector_matrices_a_c.add((ArrayList<ArrayList<Double>>) matriz_a_c.clone());
            pesosCriterios = calcula_pesos_CI_CR(matriz_a_c,matriz_a_c.size(), matriz_a_c.size());
            
            if (contador_pasos <= criterios.size())
                generarMatriz(false, criterios.get(contador_pasos-1)); //SEGUIMOS AÑADIENDO RELACIONES DE PREFERENCIA ENTRE ALTERNATIVAS PARA UN CRITERIO...
            else{
                //YA SE HA TERMINADO TODAS LAS MATRICES A RELLENAR...TOCA CALCULAR 
                System.out.println("NºMatrices de comparaciones de alternativas resp. criterios : "+vector_matrices_a_c.size());
                System.out.println(vector_matrices_a_c);
                interfaz.set_visibilidad_relaciones(false);
                interfaz.setVisible(true);
                dispose();
                
            }
            contador_pasos++;          
        }
        
        else //PARA REAJUSTAR AL RITMO QUE SE HACE CLICK EN EL BUTTON.
            contador_pasos++;
    }//GEN-LAST:event_SIGUIENTEActionPerformed
    
    private void ObtenerMatriz(ArrayList<ArrayList<Double>> matriz, int filas, int columnas){
        ArrayList<Double> vector_aux = new ArrayList<>();
        Double a;
        for (int i = 0; i < filas; i++) {
            vector_aux = new ArrayList<>();
            for (int j = 1; j < columnas + 1; j++) {
                String e = jTable1.getValueAt(i, j).toString();
                if (e.contains("/")) {
                    String[] spl = e.split("/");
                    if (spl.length == 2){
                        Double n1 = Double.parseDouble(spl[0]);
                        Double n2 = Double.parseDouble(spl[1]);
                        a = (n1 / n2);
                    }else{
                        Double n1 = Double.parseDouble(spl[0]);
                        Double n2 = Double.parseDouble(spl[1]);
                        Double n3 = Double.parseDouble(spl[2]);
                        a = (n1 / (n2 / n3) );
                    }
                } 
                else
                    a = Double.parseDouble(e);
                vector_aux.add(a);
            }
           matriz.add(vector_aux);
        }
    }
    
    
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
            java.util.logging.Logger.getLogger(AHP_Algorithm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AHP_Algorithm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AHP_Algorithm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AHP_Algorithm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AHP_Interface().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton SIGUIENTE;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
    
    public void generarMatriz(boolean compara_criterios, String nombre_tabla) {
        
        if (compara_criterios) { //Comprobar si es para generar la matriz de comparación entre criterios.
            DefaultTableModel tabla_auxiliar = new DefaultTableModel();
            tabla_auxiliar.addColumn(nombre_tabla);

            for (int i = 0; i < criterios.size(); i++)
                tabla_auxiliar.addColumn(criterios.get(i));
            
            for (int i = 0; i < criterios.size(); i++) {
                tabla_auxiliar.addRow(new Object[criterios.size()]);
                tabla_auxiliar.setValueAt(criterios.get(i), i, 0);
            }
            for (int i = 0; i < tabla_auxiliar.getRowCount(); i++) {
                for (int j = 0; j < tabla_auxiliar.getColumnCount(); j++) {
                    if (i == j) {
                        tabla_auxiliar.setValueAt(1, i, j + 1);
                    }
                }
            }
            jTable1.setModel(tabla_auxiliar);
        } else { //Matriz de alternativas por cada uno de los criterios.
            
            DefaultTableModel tabla_auxiliar = new DefaultTableModel();
            tabla_auxiliar.addColumn(nombre_tabla);

            for (int i = 0; i < alternativas.size(); i++)
                tabla_auxiliar.addColumn(alternativas.get(i));
            
            for (int i = 0; i < alternativas.size(); i++) {
                tabla_auxiliar.addRow(new Object[alternativas.size()]);
                tabla_auxiliar.setValueAt(alternativas.get(i), i, 0);
            }
            for (int i = 0; i < tabla_auxiliar.getRowCount(); i++) {
                for (int j = 0; j < tabla_auxiliar.getColumnCount(); j++) {
                    if (i == j) {
                        tabla_auxiliar.setValueAt(1, i, j + 1);
                    }
                }
            }
            jTable1.setModel(tabla_auxiliar);
        }

    }
    
    public ArrayList<Double> getV_CR() {
        return v_CR;
    }
    
    public ArrayList<Double> getV_CI(){
        return v_CI;
    }
    
    private ArrayList<Double> calcula_pesos_CI_CR(ArrayList<ArrayList<Double>> matriz,int n_filas, int n_columnas){
    
        //OBTENER LOS AUTOVALORES DE LA MATRIZ.
        double[][] values = new double[n_filas][n_columnas];
        
        for (int i = 0; i < n_filas; i++)
            for (int j = 0; j < n_columnas; j++)
                values[i][j] = matriz.get(i).get(j);

        RealMatrix matrix = MatrixUtils.createRealMatrix(values);
        EigenDecomposition descomposition = new EigenDecomposition(matrix);
        double[] eigenValues = descomposition.getRealEigenvalues(); //Obtenemos los autovalores
        
        //CALCULAR CUÁL ES EL MÁXIMO AUTOVALOR.
        double maxAutoValor = -1.0;
        int indice_AV_max = -1;
        for (int i = 0; i < eigenValues.length; i++) {
            if (maxAutoValor < eigenValues[i]) {
                maxAutoValor = eigenValues[i];
                indice_AV_max = i;
            }
        }
        
        //AHORA SE OBTIENE EL ÍNDICE DE CONSISTENCIA, TRAS HABER OBTENIDO EL MÁXIMO AUTOVALOR.
        CI = (maxAutoValor - matriz.size()) / (matriz.size() - 1); //Fórmula del CI.
        
        
        //OBTENER EL AUTOVECTOR, A PARTIR DEL AUTOVALOR MÁXIMO OBTENIDO.
        autoVector = descomposition.getEigenvector(indice_AV_max);
        double[] _autoVector = autoVector.toArray();
        ArrayList<Double> vector_pesos = new ArrayList<>();
        for (int i = 0; i < _autoVector.length; i++) 
            vector_pesos.add(_autoVector[i]);
        
        //NORMALIZAR EL AUTOVECTOR. --> NO HABRÍA PORQUÉ NORMALIZARLO.
        ArrayList<Double> v_pesos_norm = normalizarPesos(vector_pesos);
        
        if (matriz.size() >= 3)
            CR = CI / CR_SAATY[matriz.size() - 3]; //SI ES MENOR QUE 3 --> NO PUEDE EXISTIR INCONSISTENCIA.
        System.out.println("CR = "+CR);
        if (CR < 0.1) System.out.println("--------------------   CONSISTENTE       --------------------");
//        System.out.println("VECTOR DE CRITERIOS = "+criterios);
//        System.out.println("VECTOR DE PESOS = "+vector_pesos);
        v_CI.add(CI);
        v_CR.add(CR);
        return v_pesos_norm; //DEVOLVER EL VECTOR DE PESOS NORMALIZADOS.
    }
    
    private ArrayList<Double> normalizarPesos(ArrayList<Double> v_pesos) {
        Double sum = 0.0;
        for (int i = 0; i < v_pesos.size(); i++)
            sum += v_pesos.get(i);
        
        for (int i = 0; i < v_pesos.size(); i++)
            v_pesos.set(i, v_pesos.get(i) / sum);
        
        return v_pesos;
    }
    
    
    
    //----------- PREPARACIÓN DE LAS TABLAS DE PRIORIDADES LOCALES PARA LOS CRITERIOS ------------
    
    private void calcula_prioridades_criterios_ej1(){
        
        ArrayList<Double> v1 = new ArrayList<>();
        
        //CRITERIO 1
        criterios_prioridades_locales.add(new ArrayList<>());
        v1.clear(); v1.addAll(Arrays.asList(vector_RPs_CPs_cr1_ej1)); Collections.sort(v1);
        criterios_prioridades_locales.get(0).add(new ArrayList<>(v1)); //Añado espacio para el RP/CP.
        v1.clear(); v1.addAll(Arrays.asList(vector_prio_local_cr1_ej1)); Collections.sort(v1); Collections.reverse(v1);
        criterios_prioridades_locales.get(0).add(new ArrayList<>(v1)); //Añado espacio para el de Prioridades locales.
        
        ArrayList<Double> v2 = new ArrayList<>();
        //CRITERIO 2
        criterios_prioridades_locales.add(new ArrayList<>()); //Añado espacio al criterio (donde poner 2 vectores: RP/CP y el de Prioridades locales)
        v2.clear(); v2.addAll(Arrays.asList(vector_RPs_CPs_cr2_ej1)); Collections.sort(v2);
        criterios_prioridades_locales.get(1).add(new ArrayList<>(v2)); //Añado espacio para el RP/CP.
        v2.clear(); v2.addAll(Arrays.asList(vector_prio_local_cr2_ej1)); Collections.sort(v2); Collections.reverse(v2);
        criterios_prioridades_locales.get(1).add(new ArrayList<>(v2)); //Añado espacio para el de Prioridades locales.
        
        ArrayList<Double> v3 = new ArrayList<>();
        //CRITERIO 3
        criterios_prioridades_locales.add(new ArrayList<>()); //Añado espacio al criterio (donde poner 2 vectores: RP/CP y el de Prioridades locales)
        v3.clear(); v3.addAll(Arrays.asList(vector_RPs_CPs_cr3_ej1)); Collections.sort(v3);
        criterios_prioridades_locales.get(2).add(new ArrayList<>(v3)); //Añado espacio para el RP/CP.
        v3.clear(); v3.addAll(Arrays.asList(vector_prio_local_cr3_ej1)); Collections.sort(v3); Collections.reverse(v3);
        criterios_prioridades_locales.get(2).add(new ArrayList<>(v3)); //Añado espacio para el de Prioridades locales.
        
        ArrayList<Double> v4 = new ArrayList<>();
        //CRITERIO 4
        criterios_prioridades_locales.add(new ArrayList<>()); //Añado espacio al criterio (donde poner 2 vectores: RP/CP y el de Prioridades locales)
        v4.clear(); v4.addAll(Arrays.asList(vector_RPs_CPs_cr4_ej1)); Collections.sort(v4);
        criterios_prioridades_locales.get(3).add(new ArrayList<>(v4)); //Añado espacio para el RP/CP.
        v4.clear(); v4.addAll(Arrays.asList(vector_prio_local_cr4_ej1)); Collections.sort(v4); Collections.reverse(v4);
        criterios_prioridades_locales.get(3).add(new ArrayList<>(v4)); //Añado espacio para el de Prioridades locales.
        
        ArrayList<Double> v5 = new ArrayList<>();
        //CRITERIO 5
        criterios_prioridades_locales.add(new ArrayList<>()); //Añado espacio al criterio (donde poner 2 vectores: RP/CP y el de Prioridades locales)
        v5.clear(); v5.addAll(Arrays.asList(vector_RPs_CPs_cr5_ej1)); Collections.sort(v5);
        criterios_prioridades_locales.get(4).add(new ArrayList<>(v5)); //Añado espacio para el RP/CP.
        v5.clear(); v5.addAll(Arrays.asList(vector_prio_local_cr5_ej1)); Collections.sort(v5); Collections.reverse(v5);
        criterios_prioridades_locales.get(4).add(new ArrayList<>(v5)); //Añado espacio para el de Prioridades locales.
        
        System.out.println("Prioridades locales para los criterios: "+criterios_prioridades_locales);
    }
    
    private void calcula_prioridades_alternativas_ej1(ArrayList<Double> pesos_criterios){
        ArrayList<Double> v_1 = new ArrayList<>();
        Double[] v1 = {23.0, 17284.0, 2101.0, 387.0, 267.0, 0.0};
        v_1.clear(); v_1.addAll(Arrays.asList(v1));
        matriz_prioridades_locales_globales.add(v_1);
        
        ArrayList<Double> v_2 = new ArrayList<>();
        Double[] v2 = {61.0, 26571.0, 3945.0, 722.0, 818.0, 0.0};
        v_2.clear(); v_2.addAll(Arrays.asList(v2));
        matriz_prioridades_locales_globales.add(v_2);
        
        ArrayList<Double> v_3 = new ArrayList<>();
        Double[] v3 = {20.0, 14508.0, 1691.0, 219.0, 148.0, 0.0};
        v_3.clear(); v_3.addAll(Arrays.asList(v3));
        matriz_prioridades_locales_globales.add(v_3);
        
        ArrayList<Double> v_4 = new ArrayList<>();
        Double[] v4 = {32.0, 19139.0, 2299.0, 368.0, 622.0, 0.0};
        v_4.clear(); v_4.addAll(Arrays.asList(v4));
        matriz_prioridades_locales_globales.add(v_4);
        
        ArrayList<Double> v_5 = new ArrayList<>();
        Double[] v5 = {8.0, 11222.0, 1409.0, 156.0, 139.0, 0.0};
        v_5.clear(); v_5.addAll(Arrays.asList(v5));
        matriz_prioridades_locales_globales.add(v_5);
        
        ArrayList<Double> v_6 = new ArrayList<>();
        Double[] v6 = {17.0, 7548.0, 718.0, 175.0, 129.0, 0.0};
        v_6.clear(); v_6.addAll(Arrays.asList(v6));
        matriz_prioridades_locales_globales.add(v_6);
        
        ArrayList<Double> v_7 = new ArrayList<>();
        Double[] v7 = {58.0, 46137.0, 6377.0, 830.0, 934.0, 0.0};
        v_7.clear(); v_7.addAll(Arrays.asList(v7));
        matriz_prioridades_locales_globales.add(v_7);
        
        ArrayList<Double> v_8 = new ArrayList<>();
        Double[] v8 = {86.0, 52849.0, 9662.0, 716.0, 415.0, 0.0};
        v_8.clear(); v_8.addAll(Arrays.asList(v8));
        matriz_prioridades_locales_globales.add(v_8);
        
        
        boolean salir;
        double sum_prio_localesXpesos_crit;
        
        for (int i = 0; i < matriz_prioridades_locales_globales.size(); i++) { //Primero recorre la Alternativa...
            sum_prio_localesXpesos_crit = 0.0;
            for (int j = 0; j < matriz_prioridades_locales_globales.get(i).size(); j++) { //Segundo recorre cada valor de esa alternativa para el criterio j...
                salir = false;
                if (j < matriz_prioridades_locales_globales.get(i).size() - 1){
                    for (int c = 0; c < criterios_prioridades_locales.get(j).get(0).size() && !salir; c++) { //
                        if (matriz_prioridades_locales_globales.get(i).get(j) < criterios_prioridades_locales.get(j).get(0).get(c)){ //Ha encontrado 1 valor que es justo el mayor al valor de la alternativa para el criterio.
                            System.out.print(matriz_prioridades_locales_globales.get(i).get(j) +" -----> ");
                            matriz_prioridades_locales_globales.get(i).set(j,  criterios_prioridades_locales.get(j).get(1).get(c-1) + ( (criterios_prioridades_locales.get(j).get(1).get(c) - criterios_prioridades_locales.get(j).get(1).get(c-1)) / (criterios_prioridades_locales.get(j).get(0).get(c) - criterios_prioridades_locales.get(j).get(0).get(c-1))) * (matriz_prioridades_locales_globales.get(i).get(j) - criterios_prioridades_locales.get(j).get(0).get(c-1)));
                            sum_prio_localesXpesos_crit += matriz_prioridades_locales_globales.get(i).get(j) * pesos_criterios.get(j);
                            salir = true;
                            System.out.println(matriz_prioridades_locales_globales.get(i).get(j));
                        }
                    }
                }
                else{
                    //Una vez recorrido todo el proceso de la Alternativa "i" para todos los criterios ---> calculo su peso global y lo añado a la columna final.
                    matriz_prioridades_locales_globales.get(i).set(j, sum_prio_localesXpesos_crit);
                    System.out.println("Peso global de la alternativa "+i+" = "+matriz_prioridades_locales_globales.get(i).get(j));
                }
            }
        }
        
        System.out.println("Nº filas = "+matriz_prioridades_locales_globales.size()+" ::: Nºcolumnas = "+matriz_prioridades_locales_globales.get(0).size());
        System.out.println(matriz_prioridades_locales_globales);
    }
    
    private void calcula_prioridades_CP_ej1(ArrayList<Double> pesosCriterios){
        Double sum  = 0.0;
        
        //PARTIENDO DE LOS VALORES DE LAS PRIORIDADES LOCALES QUE YA APARECEN EN LA TABLA 6. SÓLO NECESITA CALCULAR LA PRIORIDAD GLOBAL DE CADA CP.
        v_cp1_ej1 = new ArrayList<>();
        v_cp1_ej1.addAll(Arrays.asList(cp1_ej1));
        for (int i = 0; i < v_cp1_ej1.size(); i++)
            sum += v_cp1_ej1.get(i) * pesosCriterios.get(i);
        v_cp1_ej1.add(sum);
        System.out.println("Prioridad CP1 = "+v_cp1_ej1);
        
        v_cp2_ej1 = new ArrayList<>();
        v_cp2_ej1.addAll(Arrays.asList(cp2_ej1));
        sum = 0.0;
        for (int i = 0; i < v_cp2_ej1.size(); i++)
            sum += v_cp2_ej1.get(i) * pesosCriterios.get(i);
        v_cp2_ej1.add(sum);
        System.out.println("Prioridad CP2 = "+v_cp2_ej1);
        
        
        v_cp3_ej1 = new ArrayList<>();
        v_cp3_ej1.addAll(Arrays.asList(cp3_ej1));
        sum = 0.0;
        for (int i = 0; i < v_cp3_ej1.size(); i++)
            sum += v_cp3_ej1.get(i) * pesosCriterios.get(i);
        v_cp3_ej1.add(sum);
        System.out.println("Prioridad CP3 = "+v_cp3_ej1);
        
        System.out.println("");
        System.out.println("");
    }
    
    
    //----------- MÉTODOS DE CLASIFICACIÓN A LA CLASE CORRESPONDIENTE ---------------
    private void mostrarClasificaicion_alternativas_clases_ej1(){
        double dif1 = 0.0, dif2 = 0.0, dif3 = 0.0;
        for (int i = 0; i < alternativas.size(); i++) {
            System.out.print(alternativas.get(i)+" --> P.Global = "+matriz_prioridades_locales_globales.get(i).get(matriz_prioridades_locales_globales.get(i).size()-1)+" ::: Clase = ");
            dif3 = Math.abs(matriz_prioridades_locales_globales.get(i).get(matriz_prioridades_locales_globales.get(i).size()-1) - v_cp3_ej1.get(v_cp3_ej1.size()-1));
            dif2 = Math.abs(matriz_prioridades_locales_globales.get(i).get(matriz_prioridades_locales_globales.get(i).size()-1) - v_cp2_ej1.get(v_cp2_ej1.size()-1));
            dif1 = Math.abs(matriz_prioridades_locales_globales.get(i).get(matriz_prioridades_locales_globales.get(i).size()-1) - v_cp1_ej1.get(v_cp1_ej1.size()-1));
            if (dif3 < dif2 && dif3 < dif1)
                System.out.println("Clase 3 ---- "+dif3);
            else if (dif2 < dif1)
                System.out.println("Clase 2 ---- "+dif2);
            else
                System.out.println("Clase 1 ---- "+dif1);
        }
    }
    
    
    /** ---------------------- EJERCICIO 2 --------------------------------*/
    
    
    private void calcula_prioridades_criterios_ej2(){
        
        ArrayList<Double> v1 = new ArrayList<>();
        criterios_prioridades_locales.clear();
        //CRITERIO 1
        criterios_prioridades_locales.add(new ArrayList<>());
        v1.clear(); v1.addAll(Arrays.asList(vector_RPs_CPs_cr1_ej2)); Collections.sort(v1);
        criterios_prioridades_locales.get(0).add(new ArrayList<>(v1)); //Añado espacio para el RP/CP.
        v1.clear(); v1.addAll(Arrays.asList(vector_prio_local_cr1_ej2)); Collections.sort(v1); Collections.reverse(v1);
        criterios_prioridades_locales.get(0).add(new ArrayList<>(v1)); //Añado espacio para el de Prioridades locales.
        
        ArrayList<Double> v2 = new ArrayList<>();
        //CRITERIO 2
        criterios_prioridades_locales.add(new ArrayList<>()); //Añado espacio al criterio (donde poner 2 vectores: RP/CP y el de Prioridades locales)
        v2.clear(); v2.addAll(Arrays.asList(vector_RPs_CPs_cr2_ej2)); Collections.sort(v2);
        criterios_prioridades_locales.get(1).add(new ArrayList<>(v2)); //Añado espacio para el RP/CP.
        v2.clear(); v2.addAll(Arrays.asList(vector_prio_local_cr2_ej2)); Collections.sort(v2); Collections.reverse(v2);
        criterios_prioridades_locales.get(1).add(new ArrayList<>(v2)); //Añado espacio para el de Prioridades locales.
        
        ArrayList<Double> v3 = new ArrayList<>();
        //CRITERIO 3
        criterios_prioridades_locales.add(new ArrayList<>()); //Añado espacio al criterio (donde poner 2 vectores: RP/CP y el de Prioridades locales)
        v3.clear(); v3.addAll(Arrays.asList(vector_RPs_CPs_cr3_ej2)); Collections.sort(v3);
        criterios_prioridades_locales.get(2).add(new ArrayList<>(v3)); //Añado espacio para el RP/CP.
        v3.clear(); v3.addAll(Arrays.asList(vector_prio_local_cr3_ej2)); Collections.sort(v3); Collections.reverse(v3);
        criterios_prioridades_locales.get(2).add(new ArrayList<>(v3)); //Añado espacio para el de Prioridades locales.
        
        ArrayList<Double> v4 = new ArrayList<>();
        //CRITERIO 4
        criterios_prioridades_locales.add(new ArrayList<>()); //Añado espacio al criterio (donde poner 2 vectores: RP/CP y el de Prioridades locales)
        v4.clear(); v4.addAll(Arrays.asList(vector_RPs_CPs_cr4_ej2)); Collections.sort(v4);
        criterios_prioridades_locales.get(3).add(new ArrayList<>(v4)); //Añado espacio para el RP/CP.
        v4.clear(); v4.addAll(Arrays.asList(vector_prio_local_cr4_ej2)); Collections.sort(v4); Collections.reverse(v4);
        criterios_prioridades_locales.get(3).add(new ArrayList<>(v4)); //Añado espacio para el de Prioridades locales.
        
        ArrayList<Double> v5 = new ArrayList<>();
        //CRITERIO 5
        criterios_prioridades_locales.add(new ArrayList<>()); //Añado espacio al criterio (donde poner 2 vectores: RP/CP y el de Prioridades locales)
        v5.clear(); v5.addAll(Arrays.asList(vector_RPs_CPs_cr5_ej2)); Collections.sort(v5);
        criterios_prioridades_locales.get(4).add(new ArrayList<>(v5)); //Añado espacio para el RP/CP.
        v5.clear(); v5.addAll(Arrays.asList(vector_prio_local_cr5_ej2)); Collections.sort(v5); Collections.reverse(v5);
        criterios_prioridades_locales.get(4).add(new ArrayList<>(v5)); //Añado espacio para el de Prioridades locales.
        
        System.out.println("Prioridades locales para los criterios: "+criterios_prioridades_locales);
    }
    
    private void calcula_prioridades_alternativas_ej2(ArrayList<Double> pesosCriterios) {
        matriz_prioridades_locales_globales.clear(); //Limpiar los vectores del ejercicio 1.
        
        Double[] v = {
0.21,209.57,2.02,4.16,1.44,
0.33,183.27,12.31,4.51,2.05,
0.20,322.00,35.19,5.74,3.43,
0.33,244.58,29.73,5.48,3.78,
0.12,129.56,7.65,3.17,2.12,
0.29,149.42,11.93,2.74,1.87,
0.32,399.87,41.33,5.68,4.33,
0.37,406.46,46.59,3.06,2.58,
0.03,158.14,12.76,4.10,2.07,
0.30,93.69,6.75,2.37,1.85,
0.49,214.38,31.83,5.83,6.60,
0.24,254.73,21.74,4.08,2.52,
0.32,132.78,11.82,2.96,1.91,
0.25,184.06,21.45,2.78,1.88,
0.24,151.98,16.99,2.53,1.00,
0.05,159.07,14.44,1.86,1.71,
0.32,324.49,45.65,4.86,5.78,
0.35,209.64,25.18,4.03,6.81,
0.12,188.62,24.12,3.40,1.46,
0.25,174.83,3.86,5.20,1.78,
0.15,216.25,27.15,3.01,2.68,
0.36,111.56,7.01,3.19,1.96,
0.26,117.30,11.16,2.72,2.00,
0.21,167.81,10.70,2.84,0.85,
0.44,207.96,26.11,2.68,2.06,
0.19,150.23,9.86,4.00,2.54,
0.09,99.18,9.77,2.61,1.50,
0.25,391.22,53.98,5.88,2.47,
0.36,282.94,39.11,5.09,5.73,
0.40,248.16,30.72,3.74,1.99,
0.58,197.75,12.95,4.57,2.08,
0.19,112.62,7.28,2.73,1.28,
0.20,119.07,8.49,3.95,1.26,
0.00,103.56,3.92,3.79,1.53,
0.34,215.14,25.30,4.23,3.12,
0.18,147.17,14.43,2.94,1.79,
0.24,168.82,12.65,2.37,1.83,
0.39,268.50,36.59,4.18,2.52,
0.17,165.48,11.94,2.84,1.09,
0.13,126.28,6.36,3.11,1.75,
0.44,272.48,49.82,3.69,2.14,
0.00,127.67,7.42,1.91,1.24,
0.32,289.89,40.86,4.43,3.88,
0.74,76.42,2.88,2.73,0.96,
0.36,235.40,24.98,2.93,2.61,
0.38,272.84,29.64,4.92,3.23,
0.31,169.15,9.13,2.23,0.56,
0.18,270.95,5.08,3.37,1.37,
0.23,135.12,7.38,2.59,1.86,
0.14,175.48,15.78,3.84,2.24,
1.06,187.86,66.27,18.71,26.72,
0.93,294.01,56.67,19.28,7.43
};
        
        for (int i = 0; i < v.length; i += 5) {
            ArrayList<Double> v_1 = new ArrayList<>();
            for (int j = i; j < i+5; j++) {
                v_1.add(v[j]);
            }
            matriz_prioridades_locales_globales.add(v_1);
        }
        
        System.out.println(matriz_prioridades_locales_globales);
        
        
        boolean salir;
        double sum_prio_localesXpesos_crit;
        
        
        
        for (int i = 0; i < matriz_prioridades_locales_globales.size(); i++) { //Primero recorre la Alternativa...
            sum_prio_localesXpesos_crit = 0.0;
            for (int j = 0; j < matriz_prioridades_locales_globales.get(i).size(); j++) { //Segundo recorre cada valor de esa alternativa para el criterio j...
                salir = false;
                if (j < matriz_prioridades_locales_globales.get(i).size() - 1){
                    for (int l = 0; l < criterios_prioridades_locales.get(j).get(0).size() && !salir; l++) { //
                        if (matriz_prioridades_locales_globales.get(i).get(j) < criterios_prioridades_locales.get(j).get(0).get(l)){ //Ha encontrado 1 valor que es justo el mayor al valor de la alternativa para el criterio.
                            System.out.print(matriz_prioridades_locales_globales.get(i).get(j) +" -----> ");
                            matriz_prioridades_locales_globales.get(i).set(j,  criterios_prioridades_locales.get(j).get(1).get(l-1) + ( (criterios_prioridades_locales.get(j).get(1).get(l) - criterios_prioridades_locales.get(j).get(1).get(l-1)) / (criterios_prioridades_locales.get(j).get(0).get(l) - criterios_prioridades_locales.get(j).get(0).get(l-1))) * (matriz_prioridades_locales_globales.get(i).get(j) - criterios_prioridades_locales.get(j).get(0).get(l-1)));
                            sum_prio_localesXpesos_crit += matriz_prioridades_locales_globales.get(i).get(j) * pesosCriterios.get(j);
                            salir = true;
                            System.out.println(matriz_prioridades_locales_globales.get(i).get(j));
                        }
                    }
                }
                else{
                    //Una vez recorrido todo el proceso de la Alternativa "i" para todos los criterios ---> calculo su peso global y lo añado a la columna final.
                    matriz_prioridades_locales_globales.get(i).set(j, sum_prio_localesXpesos_crit);
                    System.out.println("Peso global de la alternativa "+i+" = "+matriz_prioridades_locales_globales.get(i).get(j));
                }
            }
        }
        
        System.out.println("Nº filas = "+matriz_prioridades_locales_globales.size()+" ::: Nºcolumnas = "+matriz_prioridades_locales_globales.get(0).size());
        System.out.println(matriz_prioridades_locales_globales);
    }
    
    private void calcula_prioridades_CP_ej2(ArrayList<Double> pesosCriterios){
        Double sum  = 0.0;
        
        //PARTIENDO DE LOS VALORES DE LAS PRIORIDADES LOCALES QUE YA APARECEN EN LA TABLA 6. SÓLO NECESITA CALCULAR LA PRIORIDAD GLOBAL DE CADA CP.
        v_cp1_ej2 = new ArrayList<>();
        v_cp1_ej2.addAll(Arrays.asList(cp1_ej2));
        for (int i = 0; i < v_cp1_ej2.size(); i++)
            sum += v_cp1_ej2.get(i) * pesosCriterios.get(i);
        v_cp1_ej2.add(sum);
        System.out.println("Prioridad CP1 = "+v_cp1_ej2);
        
        v_cp2_ej2 = new ArrayList<>();
        v_cp2_ej2.addAll(Arrays.asList(cp2_ej2));
        sum = 0.0;
        for (int i = 0; i < v_cp2_ej2.size(); i++)
            sum += v_cp2_ej2.get(i) * pesosCriterios.get(i);
        v_cp2_ej2.add(sum);
        System.out.println("Prioridad CP2 = "+v_cp2_ej2);
        
        
        v_cp3_ej2 = new ArrayList<>();
        v_cp3_ej2.addAll(Arrays.asList(cp3_ej2));
        sum = 0.0;
        for (int i = 0; i < v_cp3_ej2.size(); i++)
            sum += v_cp3_ej2.get(i) * pesosCriterios.get(i);
        v_cp3_ej2.add(sum);
        System.out.println("Prioridad CP3 = "+v_cp3_ej2);
        
        System.out.println("");
        System.out.println("");
    }
    
    
    private void mostrarClasificaicion_alternativas_clases_ej2(){
        System.out.println("\n\n\n\n CLASIFICACIÓN GLOBAL DE LAS ALTERNATIVAS RESPECTO A LOS CPs GLOBALES. \n");
        
        double dif1 = 0.0, dif2 = 0.0, dif3 = 0.0;
        for (int i = 0; i < alternativas.size(); i++) {
            System.out.print(alternativas.get(i)+" --> P.Global = "+matriz_prioridades_locales_globales.get(i).get(matriz_prioridades_locales_globales.get(i).size()-1)+" ::: Clase = ");
            dif3 = Math.abs(matriz_prioridades_locales_globales.get(i).get(matriz_prioridades_locales_globales.get(i).size()-1) - v_cp3_ej2.get(v_cp3_ej2.size()-1));
            dif2 = Math.abs(matriz_prioridades_locales_globales.get(i).get(matriz_prioridades_locales_globales.get(i).size()-1) - v_cp2_ej2.get(v_cp2_ej2.size()-1));
            dif1 = Math.abs(matriz_prioridades_locales_globales.get(i).get(matriz_prioridades_locales_globales.get(i).size()-1) - v_cp1_ej2.get(v_cp1_ej2.size()-1));
            if (dif3 < dif2 && dif3 < dif1)
                System.out.println("Clase 3 ---- "+dif3);
            else if (dif2 < dif1)
                System.out.println("Clase 2 ---- "+dif2);
            else
                System.out.println("Clase 1 ---- "+dif1);
        }
        
        
        System.out.println("\n\n\n CLASIFICACIÓN LOCAL POR CADA UNO DE LOS CRITERIOS RESPECTO A LOS CPs CORRESPONDIENTES A CADA CRITERIO.");
        dif1 =  dif2 = dif3 = 0.0;
        for (int c = 0; c < criterios.size(); c++){
            System.out.println("CRITERIO --> "+criterios.get(c)+":");
            for (int i = 0; i < alternativas.size(); i++) {
                System.out.print(alternativas.get(i)+" --> P.Global = "+matriz_prioridades_locales_globales.get(i).get(c)+" ::: Clase = ");
                dif3 = Math.abs(matriz_prioridades_locales_globales.get(i).get(c) - v_cp3_ej2.get(c));
                dif2 = Math.abs(matriz_prioridades_locales_globales.get(i).get(c) - v_cp2_ej2.get(c));
                dif1 = Math.abs(matriz_prioridades_locales_globales.get(i).get(c) - v_cp1_ej2.get(c));
                if (dif3 < dif2 && dif3 < dif1)
                    System.out.println("Clase 3 ---- "+dif3);
                else if (dif2 < dif1)
                    System.out.println("Clase 2 ---- "+dif2);
                else
                    System.out.println("Clase 1 ---- "+dif1);
            }
        }
        
        
    }
    
    
    
    
}
