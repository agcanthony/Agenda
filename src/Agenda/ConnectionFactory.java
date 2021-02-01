// situa em qual package ou “pacote” está a classe 
package Agenda; 
// faz as importações de classes necessárias para o funcionamento do programa 
import java.awt.HeadlessException;
import java.sql.Connection; 
// conexão SQL para Java 
import java.sql.DriverManager; 
import java.sql.ResultSet;
// driver de conexão SQL para Java 
import java.sql.SQLException; 
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
// classe para tratamento de exceções 

public class ConnectionFactory {
    public Statement stm;
    public ResultSet rs = null;
    public Connection con = null;
    
    public void conexao(){
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/login?useTimezone=true&serverTimezone=UTC","root","");
            //JOptionPane.showMessageDialog(null, "Conectado com sucesso");
	}catch(SQLException | HeadlessException ex){
            JOptionPane.showMessageDialog(null, "Erro de conexão: "+ex);
	}
    }
    
    public void executaSQL(String sql){
        try {
            stm = con.createStatement(rs.TYPE_SCROLL_SENSITIVE,rs.CONCUR_READ_ONLY);
            rs = stm.executeQuery(sql);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro no método executaSQL: "+ex);
        }
    }
    
    public void desconecta(){
        try{
            con.close();
            JOptionPane.showMessageDialog(null, "Cadastrado com sucesso");
	}catch(SQLException | HeadlessException ex){
            JOptionPane.showMessageDialog(null, "Erro ao fechar  conexão: "+ex);
	}
    }
}