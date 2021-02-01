

package conexao;

import java.awt.HeadlessException;
import java.sql.*;
import javax.swing.JOptionPane;
import java.sql.DriverManager;
 
import java.sql.SQLException; 


public class Bdados
{
    public Statement stm;
    public ResultSet rs = null;
    public Connection con = null;
    
    public static Connection Bdados() throws ClassNotFoundException{
            try{
        Class.forName("org.gjt.mm.mysql.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/login","root","");
        /*JOptionPane.showMessageDialog(null,"Banco conectado");*/
        
        return con;
    }catch(SQLException erro){
       JOptionPane.showMessageDialog(null,"Erro");
        return null;
    }
}
    public com.mysql.jdbc.PreparedStatement ps;

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
