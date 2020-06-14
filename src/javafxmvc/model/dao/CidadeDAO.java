package javafxmvc.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafxmvc.model.domain.Cidade;

public class CidadeDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Cidade cidade) {
        String sql = "INSERT INTO cidades(nome, uf) VALUES(?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cidade.getNome());
            stmt.setString(2, cidade.getUf());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CidadeDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Cidade cidade) {
        String sql = "UPDATE cidades SET nome=?, uf = ? WHERE cdCidade=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cidade.getNome());
            stmt.setString(2, cidade.getUf());
            stmt.setInt(3, cidade.getCdCidade());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CidadeDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Cidade cidade) {
        String sql = "DELETE FROM cidades WHERE cdCidade=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cidade.getCdCidade());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CidadeDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Cidade> listar() {
        String sql = "SELECT * FROM cidades";
        List<Cidade> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Cidade cidade = new Cidade();
                cidade.setCdCidade(resultado.getInt("cdCidade"));
                cidade.setNome(resultado.getString("nome"));
                cidade.setUf(resultado.getString("uf"));
                retorno.add(cidade);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CidadeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Cidade buscar(Cidade cidade) {
        String sql = "SELECT * FROM cidades WHERE cdCidade=?";
        Cidade retorno = new Cidade();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cidade.getCdCidade());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                cidade.setNome(resultado.getString("nome"));
                cidade.setUf(resultado.getString("uf"));
                retorno = cidade;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CidadeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
}
