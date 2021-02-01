/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agenda;
import javax.swing.ImageIcon;
import conexao.Bdados;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import java.util.Date;
import java.text.SimpleDateFormat;
import Agenda.ModeloTabela;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
/**
 *
 * @author Anthony
 */
public class Agenda extends javax.swing.JFrame {

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Bdados conecta  = new Bdados();
    public Agenda() {
        initComponents();
        pesquisa_data.setText("");
        datas.setText(""); // Inicializa o campo data em Branco
        conecta.conexao();
        jButton3.setEnabled(false);
        //preencherTabela("SELECT * FROM agenda ORDER BY COD");
        
        
        this.setLocationRelativeTo(null);
        
        this.setLocationRelativeTo(null);
        try {
            con = Bdados.Bdados();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, ex);
        }
            mostrar();  
    }
    
    
    
//    private void iniciaTabela() {
//        
//    jScrollPane1 = new javax.swing.JScrollPane();
//    Tabela = new javax.swing.JTable();
//    
//    Tabela.setBounds(50,50,1000,450);//dimensões da tabela
//    
//    //setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
//    
//    Tabela.setModel(new javax.swing.table.DefaultTableModel(
//    new Object [][] { {},{},{},{},{} },
//    new String [] { }
//    ));
//    
//    jScrollPane1.setViewportView(Tabela);
//    }
    public void mostrar(){
        String sql = "SELECT * From agenda";
        
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            Tabela.setModel(DbUtils.resultSetToTableModel(rs));
            
       } 
        catch (SQLException error){
            JOptionPane.showMessageDialog(null, error);
        }
        
    }
    public void preencherTabela(String SQL){
        ArrayList dados = new ArrayList();
        
        String[] Colunas = new String[]{"COD","DATA","NOME","DESCRIÇÂO"};

        try{
            conecta.executaSQL(SQL);
            conecta.rs.first();
            do{
                dados.add(new Object[]{conecta.rs.getString("COD"),                				
                						conecta.rs.getString("DATA"),
                						conecta.rs.getString("NOME"),                						
                						conecta.rs.getString("DESCRICAO")});
            }while(conecta.rs.next());
	}catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "Erro ao preencher Arraylist: "+ex);
	}
        ModeloTabela modelo  = new ModeloTabela(dados, Colunas);
        Tabela.setModel(modelo);
        Tabela.getColumnModel().getColumn(0).setPreferredWidth(40);
        Tabela.getColumnModel().getColumn(0).setResizable(false);
        Tabela.getColumnModel().getColumn(1).setPreferredWidth(100);
        Tabela.getColumnModel().getColumn(1).setResizable(false);
        Tabela.getColumnModel().getColumn(2).setPreferredWidth(140);
        Tabela.getColumnModel().getColumn(2).setResizable(false);
        Tabela.getColumnModel().getColumn(3).setPreferredWidth(308);
        Tabela.getColumnModel().getColumn(3).setResizable(false);
                
        Tabela.getTableHeader().setReorderingAllowed(true);
        Tabela.setAutoResizeMode(Tabela.AUTO_RESIZE_OFF);
        Tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    
    public void salvar(){
        String sql = "INSERT INTO agenda "+"(DATA,NOME,DESCRICAO)"+"VALUES(?,?,?)";
        
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, cod.getText());
            ps.setString(1, datas.getText());
            ps.setString(2, nome.getText());
            ps.setString(3, descricao.getText());
            
            cod.setText("");
            datas.setText("");
            nome.setText("");
            descricao.setText("");
            
            
            ps.execute();
            ps.close();
            mostrar();
           // preencherTabela("SELECT * FROM agenda ORDER BY COD");
            
            JOptionPane.showMessageDialog(null,"Tarefa cadastrada");
        } catch (SQLException error){
            JOptionPane.showMessageDialog(null,error);
        }
        
    }
    
    public void pesquisa_usuario(){
    String sql = "SELECT * From agenda WHERE DATA like ?";
    try{
    
    ps = con.prepareStatement(sql);
    ps.setString(1, pesquisa_data.getText() + "%");
    rs = ps.executeQuery();
    Tabela.setModel(DbUtils.resultSetToTableModel(rs));
    
    
    }
    catch (SQLException error ){
    JOptionPane.showMessageDialog(null, error);
    }
    }
        
    public void atualiza(){
        String sql = "UPDATE agenda SET  DATA=?,NOME=?,DESCRICAO=? where COD=?";
            try {
                
                ps = con.prepareStatement(sql);
            
            ps.setString(1, datas.getText());
            ps.setString(2, nome.getText());
            ps.setString(3, descricao.getText());
            ps.setString(4, cod.getText());
                      
            
            
            ps.executeUpdate();
            ps.close();
            mostrar();
           // preencherTabela("SELECT * FROM agenda ORDER BY COD");
            
            JOptionPane.showMessageDialog(null,"Cliente Atualizado");
        } catch (SQLException error){
            JOptionPane.showMessageDialog(null,error);
        }
        
        }
    
    public void setar(){
        
        int setar = Tabela.getSelectedRow();
        cod.setText(Tabela.getModel().getValueAt(setar, 0).toString());
        datas.setText(Tabela.getModel().getValueAt(setar, 1).toString());
        nome.setText(Tabela.getModel().getValueAt(setar, 2).toString());
        descricao.setText(Tabela.getModel().getValueAt(setar, 3).toString());
        jButton3.setEnabled(true);
    }
    
    public void limpacampos(){
            cod.setText("");
            datas.setText("");
            nome.setText("");
            descricao.setText("");
            pesquisa_data.setText("");
            mostrar();
           // preencherTabela("SELECT * FROM agenda ORDER BY COD");
            
    }
    
    public void delete(){
        String sql = "DELETE from agenda where COD='"+cod.getText()+"'";
        
        try {
            ps = con.prepareStatement(sql);
            cod.getText();
            
            
            ps.executeUpdate();
            ps.close();
            
           // preencherTabela("SELECT * FROM agenda ORDER BY COD");
            
            JOptionPane.showMessageDialog(null,"Excluido com Sucesso");
        } catch (SQLException error) {
            Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, error);
        }
        limpacampos();
        jButton3.setEnabled(false);
        mostrar();
        //preencherTabela("SELECT * FROM agenda ORDER BY COD");
        
            
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Principal = new javax.swing.JPanel();
        Dados = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cod = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        nome = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        datas = new javax.swing.JFormattedTextField();
        descricao = new javax.swing.JTextField();
        calendario = new com.toedter.calendar.JCalendar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabela = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        pesquisa_data = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        dia = new javax.swing.JLabel();
        diaN = new javax.swing.JLabel();
        semana = new javax.swing.JLabel();
        frase = new javax.swing.JLabel();
        jhora = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Agenda 1.0");
        setBackground(new java.awt.Color(0, 0, 0));
        setIconImage(new ImageIcon(getClass().getResource("icons8_copybook_96px.png")).getImage());
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        Principal.setBackground(new java.awt.Color(54, 53, 53));
        Principal.setToolTipText("");

        Dados.setBackground(new java.awt.Color(54, 53, 53));

        jLabel2.setFont(new java.awt.Font("Verdana", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Tarefas");

        jLabel3.setFont(new java.awt.Font("Verdana", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Cod:");

        cod.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        cod.setEnabled(false);
        cod.setMinimumSize(new java.awt.Dimension(30, 30));

        jLabel4.setFont(new java.awt.Font("Verdana", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Nome:");

        nome.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        nome.setMinimumSize(new java.awt.Dimension(30, 30));

        jLabel5.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Data:");

        datas.setBorder(null);
        datas.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        datas.setEnabled(false);
        datas.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        datas.setMinimumSize(new java.awt.Dimension(30, 30));
        datas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                datasKeyReleased(evt);
            }
        });

        descricao.setBackground(new java.awt.Color(204, 204, 204));
        descricao.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        descricao.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Descrição", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Verdana", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N

        calendario.setBackground(new java.awt.Color(0, 0, 0));
        calendario.setDate(new java.util.Date(1558184113000L));
        calendario.setDecorationBackgroundColor(new java.awt.Color(0, 0, 204));
        calendario.setDecorationBackgroundVisible(false);
        calendario.setDecorationBordersVisible(true);
        calendario.setFont(new java.awt.Font("Verdana", 0, 18)); // NOI18N
        calendario.setSundayForeground(new java.awt.Color(255, 36, 36));
        calendario.setWeekdayForeground(new java.awt.Color(0, 51, 153));
        calendario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                calendarioMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                calendarioMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                calendarioMouseReleased(evt);
            }
        });
        calendario.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                calendarioPropertyChange(evt);
            }
        });
        calendario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                calendarioKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calendarioKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                calendarioKeyTyped(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(6, 72, 115));
        jButton1.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Gravar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(6, 72, 115));
        jButton2.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Atualiza");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(6, 72, 115));
        jButton3.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Deletar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(6, 72, 115));
        jButton4.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Pesquisar");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(6, 72, 115));
        jButton5.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Limpar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout DadosLayout = new javax.swing.GroupLayout(Dados);
        Dados.setLayout(DadosLayout);
        DadosLayout.setHorizontalGroup(
            DadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DadosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(211, 211, 211))
            .addGroup(DadosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(descricao)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DadosLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(calendario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(DadosLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cod, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(datas, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(DadosLayout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        DadosLayout.setVerticalGroup(
            DadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DadosLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel2)
                .addGap(12, 12, 12)
                .addGroup(DadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cod, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(datas, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(DadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(nome, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(descricao, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(calendario, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(DadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        jScrollPane1.setForeground(new java.awt.Color(255, 255, 255));

        Tabela.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        Tabela.setForeground(new java.awt.Color(0, 0, 0));
        Tabela.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        Tabela.setGridColor(new java.awt.Color(255, 255, 255));
        Tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Tabela);

        jLabel1.setFont(new java.awt.Font("Verdana", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Agenda");

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agenda/icons8_mechanical_spring_125px.png"))); // NOI18N

        pesquisa_data.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        pesquisa_data.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                pesquisa_dataFocusLost(evt);
            }
        });
        pesquisa_data.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pesquisa_dataActionPerformed(evt);
            }
        });
        pesquisa_data.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pesquisa_dataKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pesquisa_dataKeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Pesquisar Agenda");

        dia.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        dia.setForeground(new java.awt.Color(255, 255, 255));
        dia.setText("Dia");

        diaN.setFont(new java.awt.Font("Verdana", 0, 36)); // NOI18N
        diaN.setForeground(new java.awt.Color(255, 255, 255));
        diaN.setText("08");

        semana.setFont(new java.awt.Font("Verdana", 0, 24)); // NOI18N
        semana.setForeground(new java.awt.Color(255, 255, 255));
        semana.setText("Sexta - Feira");

        frase.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        frase.setForeground(new java.awt.Color(255, 255, 255));
        frase.setText("frase");

        jhora.setFont(new java.awt.Font("Verdana", 0, 18)); // NOI18N
        jhora.setForeground(new java.awt.Color(255, 255, 255));
        jhora.setText("hora");

        javax.swing.GroupLayout PrincipalLayout = new javax.swing.GroupLayout(Principal);
        Principal.setLayout(PrincipalLayout);
        PrincipalLayout.setHorizontalGroup(
            PrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PrincipalLayout.createSequentialGroup()
                .addGroup(PrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(PrincipalLayout.createSequentialGroup()
                        .addGroup(PrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PrincipalLayout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addComponent(dia)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(diaN))
                            .addGroup(PrincipalLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(PrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(frase, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(PrincipalLayout.createSequentialGroup()
                                        .addComponent(semana)
                                        .addGap(18, 18, 18)
                                        .addComponent(jhora)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(PrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pesquisa_data, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)))
                    .addGroup(PrincipalLayout.createSequentialGroup()
                        .addGap(291, 291, 291)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PrincipalLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Dados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );
        PrincipalLayout.setVerticalGroup(
            PrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PrincipalLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel1)
                .addGap(5, 5, 5)
                .addComponent(frase)
                .addGroup(PrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PrincipalLayout.createSequentialGroup()
                        .addGroup(PrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(diaN)
                            .addComponent(dia))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(semana)
                            .addComponent(jhora))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PrincipalLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pesquisa_data, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(7, 7, 7)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(PrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Dados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 744, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Principal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Principal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TabelaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelaMouseClicked
        setar();
    }//GEN-LAST:event_TabelaMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        atualiza();
        //preencherTabela("SELECT * FROM agenda ORDER BY COD");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void calendarioPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_calendarioPropertyChange
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); //DEFINE FORMATO DE DATA  
        String data = formato.format(calendario.getDate()); //CONVERTE PRA STRING  
        datas.setText(data);
        pesquisa_data.setText(data);
        
    }//GEN-LAST:event_calendarioPropertyChange

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        salvar();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void calendarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_calendarioMouseClicked
       
    }//GEN-LAST:event_calendarioMouseClicked

    private void calendarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_calendarioKeyTyped
      
    }//GEN-LAST:event_calendarioKeyTyped

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        limpacampos();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
       delete();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        pesquisa_usuario();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void calendarioMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_calendarioMousePressed
       
    }//GEN-LAST:event_calendarioMousePressed

    private void calendarioMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_calendarioMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_calendarioMouseReleased

    private void datasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_datasKeyReleased
      //  pesquisa_usuario();
    }//GEN-LAST:event_datasKeyReleased

    private void calendarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_calendarioKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_calendarioKeyReleased

    private void calendarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_calendarioKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_calendarioKeyPressed

    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseClicked
       //pesquisa_usuario();
       
    }//GEN-LAST:event_jButton4MouseClicked

    private void pesquisa_dataKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pesquisa_dataKeyReleased
       //pesquisa_usuario();

    }//GEN-LAST:event_pesquisa_dataKeyReleased

    private void pesquisa_dataKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pesquisa_dataKeyPressed
       
    }//GEN-LAST:event_pesquisa_dataKeyPressed

    private void pesquisa_dataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pesquisa_dataActionPerformed

    }//GEN-LAST:event_pesquisa_dataActionPerformed

    private void pesquisa_dataFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pesquisa_dataFocusLost
       
    }//GEN-LAST:event_pesquisa_dataFocusLost

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
       Timer timer = new Timer(1000, new hora());
        timer.start();
        
        int 
            ds, dia, mes, ano;
	Calendar 
            data;
	String 
            diasemana[]= {"Domingo","Segunda - Feira","Terça - Feira","Quarta - Feira",
                "Quinta - Feira","Sexta - Feira","Sábado"};
	String 
		meses[] = {"Janeiro","Fevereiro","Março","Abril","Maio","Junho","Julho",
                    "Agosto","Setembro","Outubro","Novembro","Dezembro"};
        
        data = Calendar.getInstance();
        ds = data.get(Calendar.DAY_OF_WEEK);
        dia = data.get(Calendar.DAY_OF_MONTH);
        mes = data.get(Calendar.MONTH);
        ano = data.get(Calendar.YEAR);
        
        frase.setText("Barretos, "+meses[mes]+" de "+ano);
	semana.setText(" "+diasemana[ds-1]);
        diaN.setText(""+dia);
    }//GEN-LAST:event_formWindowOpened

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
            java.util.logging.Logger.getLogger(Agenda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Agenda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Agenda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Agenda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Agenda().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Dados;
    private javax.swing.JPanel Principal;
    private javax.swing.JTable Tabela;
    private com.toedter.calendar.JCalendar calendario;
    private javax.swing.JTextField cod;
    private javax.swing.JFormattedTextField datas;
    private javax.swing.JTextField descricao;
    private javax.swing.JLabel dia;
    private javax.swing.JLabel diaN;
    private javax.swing.JLabel frase;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jhora;
    private javax.swing.JTextField nome;
    private javax.swing.JTextField pesquisa_data;
    private javax.swing.JLabel semana;
    // End of variables declaration//GEN-END:variables

    class hora implements ActionListener{
        public void actionPerformed(ActionEvent e){
            Calendar now = Calendar.getInstance();
            jhora.setText(String.format("%1$tH:%1$tM:%1$tS", now));
        }
    }
}
