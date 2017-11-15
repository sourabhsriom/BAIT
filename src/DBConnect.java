
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class DBConnect
{
  private static String IP;
  private static String user;
  private static String pass;
  private static String DBname;
  Connection conn = null;

  public static Connection dbConnector(String IP, String user, String pass, String DBname) {
    try {
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
      //use for default instance
      String url = "jdbc:sqlserver://" + IP + ":1433" + ";" + "database=" + DBname;
      
      //String url = "jdbc:sqlserver://" + IP +"\\SQL2014:57503" + ";" + "database=" + DBname;
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
      return DriverManager.getConnection(url, user, pass);
    }
    catch (Exception e)
    {
      JOptionPane.showMessageDialog(null, e);
    }return null;
  }

  public static String getIP()
  {
    return IP;
  }
  public static void setIP(String iP) {
    IP = iP;
  }
  public static String getUser() {
    return user;
  }
  public static void setUser(String user) {
    user = user;
  }
  public static String getPass() {
    return pass;
  }
  public static void setPass(String pass) {
    pass = pass;
  }
  public static String getDBname() {
    return DBname;
  }
  public static void setDBname(String dBname) {
    DBname = dBname;
  }
  public Connection getConn() {
    return this.conn;
  }
  public void setConn(Connection conn) {
    this.conn = conn;
  }
}