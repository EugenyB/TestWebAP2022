package ua.mk.berkut.server;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ResultService {
    private DataSource ds;

    public ResultService(DataSource ds) {
        this.ds = ds;
    }

    public void writeToDB(Result result) {
        String fio = result.getFio();
        String ngr = result.getGroup();
        int correct = result.getCorrect();
        int wrong = result.getWrong();
        Timestamp starttime = Timestamp.valueOf(result.getStartTime());
        Timestamp finishtime = Timestamp.valueOf(result.getFinishTime());
        try(Connection con = ds.getConnection()) {
            PreparedStatement ps = con.prepareStatement("insert into RESULT (fio, ngr, correct, wrong, starttime, finishtime) VALUES (?,?,?,?,?,?)");
            ps.setString(1, fio);
            ps.setString(2, ngr);
            ps.setInt(3, correct);
            ps.setInt(4, wrong);
            ps.setTimestamp(5, starttime);
            ps.setTimestamp(6, finishtime);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
