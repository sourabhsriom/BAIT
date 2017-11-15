import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sun.misc.BASE64Encoder;

public class Login
{
  private JFrame frmDbHunter;
  private FileBrowser fb;
  public int sdsVersion;
  Connection connect = null;
  DBConnect db = new DBConnect();
  private JTextField textIP;
  private JTextField textUser;
  private JTextField textDB;
  private JPasswordField textPass;
  private JTextField textba;
  private JLabel lblBaFile;
  private JButton btnBrowseFile;
  private JLabel lableFile;
  private JTextField textTasks;
  private JLabel textdone;
  private JLabel label;
  private JButton btnImportUsers;
  private JTextField textUsers;
  private JButton btnAsset;
  private final Action action = new Login.SwingAction();
  private JTextField textPwd;
protected String sqlInstance;

  public static void main(String[] args)
  {
    EventQueue.invokeLater(new Runnable()
    {
      public void run() {
        try {
          Login window = new Login();
          window.frmDbHunter.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  public static String encode(String secret)
    throws Exception
  {
    byte[] kbytes = "jaas is the way".getBytes();
    SecretKeySpec key = new SecretKeySpec(kbytes, "Blowfish");
    Cipher cipher = Cipher.getInstance("Blowfish");
    cipher.init(1, key);
    byte[] encoding = cipher.doFinal(secret.getBytes());
    BigInteger n = new BigInteger(encoding);
    String encodeVal = n.toString(16);
    return encodeVal;
  }

  public static synchronized String encryptDBPassword(String passwordText)
    throws Exception
  {
    MessageDigest md = null;
    try
    {
      md = MessageDigest.getInstance("SHA");
      md.update(passwordText.getBytes("UTF-8"));
    } catch (NoSuchAlgorithmException e) {
      throw new Exception("Couldn't find algorithm", e);
    } catch (UnsupportedEncodingException e) {
      throw new Exception("Couldn't find encoding type", e);
    } catch (Exception e) {
      throw new Exception("Exception while trying to encrypt passowrd.", e);
    }

    byte[] raw = md.digest();
    String hash = new BASE64Encoder().encode(raw);
    return hash;
  }

  public Login()
  {
    initialize();

    DBConnect db = new DBConnect();
  }

  private void initialize()
  {
    this.frmDbHunter = new JFrame();
    this.frmDbHunter.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\ssriom\\Pictures\\dabangg.png"));
    this.frmDbHunter.setTitle("BAIT : Employee Module");
    this.frmDbHunter.setBounds(100, 100, 733, 529);
    this.frmDbHunter.setDefaultCloseOperation(3);
    this.frmDbHunter.getContentPane().setLayout(null);
    
    //code to select default instance
    
//    JLabel lblSQLInstance = new JLabel("SQL Instance");
//    lblSQLInstance.setBounds(75, 328, 96, 16);
//    this.frmDbHunter.getContentPane().add(lblSQLInstance);
//
//    JRadioButton radioButton_sql = new JRadioButton("Default Instance");
//    radioButton_sql.addActionListener(new ActionListener()
//    {
//      public void actionPerformed(ActionEvent e)
//      {
//        Login.this.sqlInstance = "default";
//      }
//    });
//    radioButton_sql.setBounds(297, 324, 73, 24);
//    this.frmDbHunter.getContentPane().add(radioButton_sql);
//
//    JRadioButton radioButton_sql_1 = new JRadioButton("Named Instance");
//    radioButton_sql_1.addActionListener(new ActionListener()
//    {
//      public void actionPerformed(ActionEvent e)
//      {
//        Login.this.sqlInstance = "named";
//      }
//    });
//    

    this.textIP = new JTextField();
    this.textIP.setBounds(203, 48, 250, 20);
    this.frmDbHunter.getContentPane().add(this.textIP);
    this.textIP.setColumns(10);

    JLabel lblDbServerIp = new JLabel("DB Server IP :");
    lblDbServerIp.setBounds(75, 51, 96, 14);
    this.frmDbHunter.getContentPane().add(lblDbServerIp);

    this.textUser = new JTextField();
    this.textUser.setBounds(203, 97, 250, 20);
    this.frmDbHunter.getContentPane().add(this.textUser);
    this.textUser.setColumns(10);

    this.textDB = new JTextField();
    this.textDB.setBounds(203, 186, 250, 20);
    this.frmDbHunter.getContentPane().add(this.textDB);
    this.textDB.setColumns(10);

    JLabel lblDbUserName = new JLabel("DB user name :");
    lblDbUserName.setBounds(75, 100, 96, 14);
    this.frmDbHunter.getContentPane().add(lblDbUserName);

    JLabel lblNewLabel = new JLabel("Password :");
    lblNewLabel.setBounds(75, 145, 73, 14);
    this.frmDbHunter.getContentPane().add(lblNewLabel);

    JLabel lblNewLabel_1 = new JLabel("DB name :");
    lblNewLabel_1.setBounds(75, 189, 73, 14);
    this.frmDbHunter.getContentPane().add(lblNewLabel_1);

    JButton btnConnect = new JButton("Import Tasks");
    btnConnect.setFont(new Font("Dialog", 1, 10));
    btnConnect.addActionListener(new ActionListener()
    {
      private XSSFWorkbook workbook1;

      public void actionPerformed(ActionEvent arg0)
      {
        try
        {
        	
        	//change the DBConnect class to configure for named instances
          Login.this.connect = DBConnect.dbConnector(Login.this.textIP.getText(), Login.this.textUser.getText(), Login.this.textPass.getText(), Login.this.textDB.getText());
          Map functions = new HashMap();
          String taskIDquery = "select * from [USER].[FUNCTION]";

          Statement taskIdMap = Login.this.connect.createStatement();

          ResultSet rsTaskIdMap = taskIdMap.executeQuery(taskIDquery);

          while (rsTaskIdMap.next())
          {
            functions.put(rsTaskIdMap.getString(1), rsTaskIdMap.getString(2));
          }

          FileInputStream file1 = new FileInputStream(Login.this.textba.getText());
          this.workbook1 = new XSSFWorkbook(file1);

          XSSFSheet sheet = this.workbook1.getSheetAt(2);

          for (int cc = 4; cc < 4 + Integer.parseInt(Login.this.textTasks.getText()); cc++) {
            String maxGroup = "select MAX (FUNCGRP_ID) as MAXGROUP from [USER].[FUNCTION_GROUP]";
            Statement sta = Login.this.connect.createStatement();

            ResultSet rs1 = sta.executeQuery(maxGroup);
            int maxGroupID = 0;
            int maxTaskID = 0;
            if (rs1.next())
            {
              maxGroupID = rs1.getInt("MAXGROUP");
            }

            ArrayList tasks = new ArrayList();
            for (int count = 0; count < sheet.getLastRowNum(); count++)
            {
              Row rowIt = sheet.getRow(count);
              Cell cell = rowIt.getCell(cc);
              if (count == 0) {
                System.out.println("Column name : " + cell.getStringCellValue());
                String groupQuery = "insert into [USER].FUNCTION_GROUP values (?, ?, ?, CURRENT_TIMESTAMP, 'Bally' , CURRENT_TIMESTAMP, 10000, null, null )";
                PreparedStatement taskGroup = Login.this.connect.prepareStatement(groupQuery);

                taskGroup.setInt(1, maxGroupID + 1);
                taskGroup.setString(2, cell.getStringCellValue());
                taskGroup.setString(3, cell.getStringCellValue());
                taskGroup.executeUpdate();
              }
              else
              {
                try {
                  if ((cell.getStringCellValue().equals("X")) || (cell.getStringCellValue().equals("x")))
                  {
                    tasks.add(rowIt.getCell(2).toString());
                  }
                }
                catch (Exception e)
                {
                  System.out.println("Some issue at index : " + count);
                }
              }

            }

            String maxTask = "select max (FNGRPFN_ID) as maxID from [USER].FUNCTION_GROUP_FUNCTION";
            Statement sta2 = Login.this.connect.createStatement();
            ResultSet rs2 = sta2.executeQuery(maxTask);

            if (rs2.next()) {
              maxTaskID = rs2.getInt("maxID");
            }

            int[] IDs = new int[tasks.size()];

            for (int i = 0; i < tasks.size(); ) {
              try {
                IDs[i] = Integer.parseInt(getKeyFromValue(functions, tasks.get(i)).toString());
                System.out.println((String)tasks.get(i));
                String tasks1 = "insert into [USER].FUNCTION_GROUP_FUNCTION values (?, ? , ?, CURRENT_TIMESTAMP, 'Bally', null, null, null, null)";
                PreparedStatement populateTasks = Login.this.connect.prepareStatement(tasks1);

                populateTasks.setInt(1, i + maxTaskID + 1);
                populateTasks.setInt(2, maxGroupID + 1);
                populateTasks.setInt(3, IDs[i]);

                populateTasks.executeUpdate();
              }
              catch (Exception e) {
                System.out.println("Task not found in DB");
              }
              finally {
                i++;
              }

            }

            Login.this.textdone.setText("Tasks Imported!");
          }

          file1.close();
        }
        catch (Exception localException1)
        {
        }

        try
        {
          Login.this.connect.close();
        }
        catch (SQLException localSQLException)
        {
        }
      }

      public Object getKeyFromValue(Map<String, String> hm, Object value)
      {
        try
        {
          for (Iterator localIterator = hm.keySet().iterator(); localIterator.hasNext(); ) { Object o = localIterator.next();
            if (((String)hm.get(o)).equals(value))
              return o; }
        }
        catch (Exception e)
        {
          System.out.println("check your file yo!");
        }
        return null;
      }
    });
    btnConnect.setBounds(75, 375, 102, 23);
    this.frmDbHunter.getContentPane().add(btnConnect);

    JLabel OutputLabel = new JLabel("");
    OutputLabel.setBounds(295, 279, 46, 14);
    this.frmDbHunter.getContentPane().add(OutputLabel);

    this.textPass = new JPasswordField();
    this.textPass.setEchoChar('*');
    this.textPass.setBounds(203, 142, 250, 20);
    this.frmDbHunter.getContentPane().add(this.textPass);

    this.textba = new JTextField();
    this.textba.setBounds(203, 229, 250, 20);
    this.frmDbHunter.getContentPane().add(this.textba);
    this.textba.setColumns(10);

    this.lblBaFile = new JLabel("BA File :");
    this.lblBaFile.setBounds(75, 232, 46, 14);
    this.frmDbHunter.getContentPane().add(this.lblBaFile);

    this.btnBrowseFile = new JButton("Browse File");
    this.btnBrowseFile.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0) {
        Login.this.fb = new FileBrowser();
        Login.this.textba.setText(Login.this.fb.fileChooser.getSelectedFile().toString());
      }
    });
    this.btnBrowseFile.setBounds(493, 228, 140, 23);
    this.frmDbHunter.getContentPane().add(this.btnBrowseFile);

    this.lableFile = new JLabel("");
    this.lableFile.setBounds(203, 384, 240, 14);
    this.frmDbHunter.getContentPane().add(this.lableFile);

    this.textTasks = new JTextField();
    this.textTasks.setBounds(203, 276, 74, 20);
    this.frmDbHunter.getContentPane().add(this.textTasks);
    this.textTasks.setColumns(10);

    JLabel lblTotalTasks = new JLabel("Total tasks :");
    lblTotalTasks.setBounds(75, 279, 96, 14);
    this.frmDbHunter.getContentPane().add(lblTotalTasks);

    this.textdone = new JLabel("");
    this.textdone.setBounds(266, 410, 213, 14);
    this.frmDbHunter.getContentPane().add(this.textdone);

    this.label = new JLabel("");
    this.label.setIcon(new ImageIcon("C:\\Users\\ssriom\\Pictures\\roin2.jpg"));
    this.label.setBounds(493, 48, 150, 158);
    this.frmDbHunter.getContentPane().add(this.label);

    JButton btnNewButton = new JButton("Import Jobs");
    btnNewButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0)
      {
        try
        {
          Login.this.connect = DBConnect.dbConnector(Login.this.textIP.getText(), Login.this.textUser.getText(), Login.this.textPass.getText(), Login.this.textDB.getText());

          BidiMap taskGroupBidiMap = new DualHashBidiMap();

          String taskGroupQuery = "select * from [USER].[FUNCTION_GROUP]";

          Statement taskGroupMap = Login.this.connect.createStatement();
          ResultSet rsTaskGroupMap = taskGroupMap.executeQuery(taskGroupQuery);

          while (rsTaskGroupMap.next()) {
            taskGroupBidiMap.put(Integer.valueOf(rsTaskGroupMap.getInt(1)), rsTaskGroupMap.getString(2));
          }

          XSSFWorkbook workbook1 = null;
          try {
            FileInputStream file1 = new FileInputStream(Login.this.textba.getText());
            workbook1 = new XSSFWorkbook(file1);
          }
          catch (FileNotFoundException e) {
            e.printStackTrace();
          }
          catch (IOException e) {
            e.printStackTrace();
          }

          XSSFSheet sheet = workbook1.getSheetAt(1);

          int firstJob = 2;

          for (int cc = firstJob; cc < firstJob + Integer.parseInt(Login.this.textTasks.getText()); cc++) {
            String jobName = null;
            try {
              jobName = sheet.getRow(cc).getCell(0).getStringCellValue();
            }
            catch (Exception e)
            {
              System.out.println("Issue with Job name cell k?");
            }

            String maxJob = "SELECT MAX(ROLE_ID) AS MAXJOB FROM [USER].[ROLE]";
            Statement maxJobQuery = Login.this.connect.createStatement();
            ResultSet maxJobNo = maxJobQuery.executeQuery(maxJob);
            int maxJobID = 0;
            if (maxJobNo.next()) {
              maxJobID = maxJobNo.getInt("MAXJOB");
            }

            String insertJob = " insert into [USER].[ROLE] values (?, ? , ? , CURRENT_TIMESTAMP, 10000, NULL, NULL, NULL,  NULL, 1 )";

            PreparedStatement insertNewJob = Login.this.connect.prepareStatement(insertJob);
            try
            {
              insertNewJob.setInt(1, maxJobID + 1);
              insertNewJob.setString(2, jobName);
              insertNewJob.setString(3, jobName);
              insertNewJob.executeUpdate();
            }
            catch (Exception localException1)
            {
            }
            int jobTaskID = 0;
            int siteID = 0;

            jobTaskID = getJobTaskID(jobName, taskGroupBidiMap);

            String findSiteID = "select SITE_NUMBER AS SITEID from SITECONFIG.SITE where SITE_NUMBER != 99";

            Statement siteIDQuery = Login.this.connect.createStatement();
            ResultSet siteIDSet = siteIDQuery.executeQuery(findSiteID);
            if (siteIDSet.next()) {
              siteID = siteIDSet.getInt("SITEID");
            }

            String maxJobTask = "select MAX(RLFNGRP_ID) AS MAXJOBTASKID from [USER].ROLE_FUNCTION_GROUP";
            Statement maxJobTaskQuery = Login.this.connect.createStatement();

            ResultSet rsMaxJobTask = maxJobTaskQuery.executeQuery(maxJobTask);
            int maxJobTaskID = 0;
            if (rsMaxJobTask.next()) {
              maxJobTaskID = rsMaxJobTask.getInt("MAXJOBTASKID");
            }

            String insertJobTask = "insert into [USER].ROLE_FUNCTION_GROUP values (?,?,?,?,CURRENT_TIMESTAMP,10000, null, null, null, null)";
            PreparedStatement insertNewJobTask = Login.this.connect.prepareStatement(insertJobTask);
            try {
              insertNewJobTask.setInt(1, maxJobTaskID + 1);
              insertNewJobTask.setInt(2, siteID);
              insertNewJobTask.setInt(3, maxJobID + 1);
              insertNewJobTask.setInt(4, jobTaskID);
              insertNewJobTask.executeUpdate();
            }
            catch (Exception localException2)
            {
            }

            for (int param = 1000; param <= 1015; param++) {
              int maxParamID = 0;
              int paramValues = (int)sheet.getRow(cc).getCell(param - 999).getNumericCellValue();
              System.out.println("Parameter ID : " + param + "Parameter Value : " + paramValues);
              String maxParam = "select MAX(RLPR_ID) as MAXPARAM from [USER].ROLE_PARAMETER";
              Statement maxParamQuery = Login.this.connect.createStatement();
              ResultSet rsMaxParam = maxParamQuery.executeQuery(maxParam);
              if (rsMaxParam.next()) {
                maxParamID = rsMaxParam.getInt("MAXPARAM");
              }
              System.out.println("Maximum parameter ID : " + maxParamID);
              String insertParam = "insert into [USER].[ROLE_PARAMETER] values (?,?,?,?,CURRENT_TIMESTAMP, 10000, null, null, null, null, 0)";
              PreparedStatement insertParamValues = Login.this.connect.prepareStatement(insertParam);
              try {
                insertParamValues.setInt(1, maxParamID + 1);
                insertParamValues.setInt(2, maxJobID + 1);
                insertParamValues.setInt(3, param);
                insertParamValues.setInt(4, paramValues);
                insertParamValues.executeUpdate();
              }
              catch (NumberFormatException e) {
                e.printStackTrace();
              }
            }

            String siteRoleId = "select max(SITEROLE_ID) as MAXSITEROLE from [USER].[SITE_ROLE]";
            int maxSiteRoleID = 0;
            Statement siteRoleIDQuery = Login.this.connect.createStatement();
            ResultSet rsSiteRoleID = siteRoleIDQuery.executeQuery(siteRoleId);

            if (rsSiteRoleID.next()) {
              maxSiteRoleID = rsSiteRoleID.getInt("MAXSITEROLE");
            }

            String insertSiteRole = "insert into [USER].[SITE_ROLE] values (?,?,?,CURRENT_TIMESTAMP,10000,null,null,null,null)";
            PreparedStatement insertNewSiteRole = Login.this.connect.prepareStatement(insertSiteRole);
            try {
              insertNewSiteRole.setInt(1, maxSiteRoleID + 1);
              insertNewSiteRole.setInt(2, maxJobID + 1);
              insertNewSiteRole.setInt(3, siteID);
              insertNewSiteRole.executeUpdate();
            }
            catch (Exception e) {
              e.printStackTrace();
            }

          }

          Login.this.textdone.setText("Jobs Imported!");
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
        try {
          Login.this.connect.close();
        }
        catch (SQLException e) {
          e.printStackTrace();
        }
      }

      private int getJobTaskID(String jobName, BidiMap<Integer, String> taskGroupBidiMap)
      {
        MapIterator it = taskGroupBidiMap.mapIterator();

        while (it.hasNext()) {
          Object key = it.next();
          Object value = it.getValue();

          if (jobName.equals(it.getValue())) {
            return ((Integer)it.getKey()).intValue();
          }

        }

        return 0;
      }
    });
    btnNewButton.setFont(new Font("Dialog", 1, 10));
    btnNewButton.setBounds(271, 374, 110, 25);
    this.frmDbHunter.getContentPane().add(btnNewButton);

    this.btnImportUsers = new JButton("Import Users");
    this.btnImportUsers.addActionListener(new ActionListener()
    {
      private String enc_pass;

      public void actionPerformed(ActionEvent arg0)
      {
        String pass = Login.this.textPwd.getText();
        try
        {
          this.enc_pass = Login.encryptDBPassword(pass);
          System.out.println(this.enc_pass);
        }
        catch (Exception e2) {
          e2.printStackTrace();
        }

        try
        {
          int maxUserId = 0;
          int maxRoleId = 0;
          int maxPassId = 0;
          int maxCardID = 0;

          Login.this.connect = DBConnect.dbConnector(Login.this.textIP.getText(), Login.this.textUser.getText(), Login.this.textPass.getText(), Login.this.textDB.getText());

          XSSFWorkbook workbook1 = null;
          try {
            FileInputStream file1 = new FileInputStream(Login.this.textba.getText());
            workbook1 = new XSSFWorkbook(file1);
          }
          catch (FileNotFoundException e) {
            e.printStackTrace();
          }
          catch (IOException e) {
            e.printStackTrace();
          }
          XSSFSheet sheet = workbook1.getSheetAt(0);

          BidiMap jobs = new DualHashBidiMap();
          String taskIDquery = "select * from [USER].[ROLE]";

          Statement roleIdMap = Login.this.connect.createStatement();

          ResultSet rsRoleIdMap = roleIdMap.executeQuery(taskIDquery);

          while (rsRoleIdMap.next())
          {
            jobs.put(Integer.valueOf(rsRoleIdMap.getInt(1)), rsRoleIdMap.getString(2));
          }

          int totalEmp = Integer.parseInt(Login.this.textUsers.getText());

          for (int rowCount = 2; rowCount < totalEmp + 2; rowCount++) {
            String userIdquery = "select MAX(ACTR_ID) as MAXACTORID from [USER].ACTOR";

            Statement userID = Login.this.connect.createStatement();
            ResultSet rs = userID.executeQuery(userIdquery);

            if (rs.next()) {
              maxUserId = rs.getInt("MAXACTORID");
            }

            String cardNoquery = "select MAX(EMPCRD_ID) as MAXCARDID from [USER].EMPLOYEE_CARD";

            Statement cardID = Login.this.connect.createStatement();
            ResultSet cid = cardID.executeQuery(cardNoquery);

            if (cid.next()) {
              maxCardID = cid.getInt("MAXCARDID");
            }

            Row row = sheet.getRow(rowCount);

            String empID = null;
            String cardNo = null;
            String firstName = null;
            String lastName = null;
            String jobName = null;
            try
            {
              row.getCell(0).setCellType(1);
              empID = row.getCell(0).getStringCellValue();

              firstName = row.getCell(1).getStringCellValue();

              lastName = row.getCell(2).getStringCellValue();

              jobName = row.getCell(9).getStringCellValue();

              row.getCell(4).setCellType(1);
              cardNo = row.getCell(4).getStringCellValue();
            }
            catch (Exception localException1)
            {
            }

            String insertEmployee = null;
            if (Login.this.sdsVersion == 125) {
              System.out.println("SDS Version is 12.5");

              insertEmployee = "insert into [USER].[ACTOR] values (? , ?, ?, ' ', ?, ?, '1970-01-01 00:00:00.000', null, CURRENT_TIMESTAMP, 10000, null,  null, null, null,1, ?, 1234, null, null, 0, 1, null, 0, 0)";
              PreparedStatement insertEmp = Login.this.connect.prepareStatement(insertEmployee);
              try {
                insertEmp.setInt(1, maxUserId + 1);
                System.out.println(empID + lastName + firstName + cardNo + empID);

                insertEmp.setString(2, empID);
                insertEmp.setString(3, lastName);
                insertEmp.setString(4, firstName);
                insertEmp.setString(5, this.enc_pass);
                insertEmp.setString(6, cardNo);

                insertEmp.execute();
              }
              catch (Exception e) {
                System.out.println(e);
              }
            }

            if (Login.this.sdsVersion == 13) {
              System.out.println("SDS Version is 13");
              insertEmployee = "insert into [USER].[ACTOR] values (? , ?, ?, ' ', ?, ?, '1970-01-01 00:00:00.000', null, CURRENT_TIMESTAMP, 10000, null,  null, null, null,1, ?, 1234, null, null, 0, 1, null, 0, 0,?)";

              PreparedStatement insertEmp13 = Login.this.connect.prepareStatement(insertEmployee);
              try {
                insertEmp13.setInt(1, maxUserId + 1);
                System.out.println(empID + lastName + firstName + cardNo + empID);

                insertEmp13.setString(2, empID);
                insertEmp13.setString(3, lastName);
                insertEmp13.setString(4, firstName);
                insertEmp13.setString(5, this.enc_pass);
                insertEmp13.setString(6, cardNo);
                insertEmp13.setString(7, empID);
                insertEmp13.execute();
              }
              catch (Exception e) {
                System.out.println("can't insert this employee");
              }
            }

            String insertCard = "insert into [USER].[EMPLOYEE_CARD] values (?, ?, CURRENT_TIMESTAMP, 10000, NULL, NULL, NULL, NULL)";
            PreparedStatement insertCardSta = Login.this.connect.prepareStatement(insertCard);
            try
            {
              insertCardSta.setInt(1, maxCardID + 1);
              insertCardSta.setString(2, cardNo);

              insertCardSta.execute();
            }
            catch (Exception e1) {
              e1.printStackTrace();
            }

            int roleID = ReturnRoleID(jobName, jobs);

            String roleIDquery = "select MAX(AR_ID) as MAXROLEID from [USER].[ACTOR_ROLE]";

            Statement roleIDStatement = Login.this.connect.createStatement();
            ResultSet rsRole = roleIDStatement.executeQuery(roleIDquery);

            if (rsRole.next()) {
              maxRoleId = rsRole.getInt("MAXROLEID");
            }

            String UserRolesQuery = "insert into [USER].ACTOR_ROLE values (?, ?, ?, 0, 0, CURRENT_TIMESTAMP, 10000, null, null, null, null )";
            PreparedStatement UserRoles = Login.this.connect.prepareStatement(UserRolesQuery);
            System.out.println("Role ID : " + roleID);
            try {
              UserRoles.setInt(1, maxRoleId + 1);
              UserRoles.setInt(2, maxUserId + 1);
              System.out.println(maxUserId + 1);
              UserRoles.setInt(3, roleID);
              UserRoles.execute();
            } catch (Exception e) {
              e.printStackTrace();
            }

            String passIDquery = "select MAX (PWDH_ID) as MAXPASSID from [USER].[PASSWORD_HISTORY]";

            Statement passIDStatement = Login.this.connect.createStatement();
            ResultSet rsPass = passIDStatement.executeQuery(passIDquery);

            if (rsPass.next()) {
              maxPassId = rsPass.getInt("MAXPASSID");
            }

            String PassHistoryQuery = "insert into [USER].PASSWORD_HISTORY values (?,?,?,CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 10000,null, null, null, null, 0)";

            PreparedStatement passHistory = Login.this.connect.prepareStatement(PassHistoryQuery);
            try
            {
              passHistory.setInt(1, maxPassId + 1);
              passHistory.setInt(2, maxUserId + 1);
              passHistory.setString(3, this.enc_pass + 1);

              passHistory.execute();
            }
            catch (Exception localException2)
            {
            }
          }
          Login.this.textdone.setText("Users Imported!");
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }

      private int ReturnRoleID(String jobName, BidiMap<Integer, String> jobs)
      {
        MapIterator it = jobs.mapIterator();
        while (it.hasNext()) {
          Object key = it.next();
          Object value = it.getValue();

          if (jobName.equals(value)) {
            return ((Integer)it.getKey()).intValue();
          }

        }

        return 0;
      }
    });
    this.btnImportUsers.setFont(new Font("Dialog", 1, 10));
    this.btnImportUsers.setBounds(454, 373, 111, 27);
    this.frmDbHunter.getContentPane().add(this.btnImportUsers);

    this.textUsers = new JTextField();
    this.textUsers.setBounds(379, 279, 74, 20);
    this.frmDbHunter.getContentPane().add(this.textUsers);
    this.textUsers.setColumns(10);

    JLabel lblTotalUsers = new JLabel("Total Users");
    lblTotalUsers.setBounds(295, 279, 86, 16);
    this.frmDbHunter.getContentPane().add(lblTotalUsers);

    this.btnAsset = new JButton("Asset Module");
    this.btnAsset.setFont(new Font("Dialog", 1, 10));
    this.btnAsset.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        Assets ass = new Assets();
        ass.setVisible(true);
      }
    });
    this.btnAsset.setBounds(542, 437, 124, 26);
    this.frmDbHunter.getContentPane().add(this.btnAsset);

    JLabel lblSdsVersion = new JLabel("SDS Version");
    lblSdsVersion.setBounds(75, 328, 96, 16);
    this.frmDbHunter.getContentPane().add(lblSdsVersion);

    JRadioButton radioButton = new JRadioButton("12.5");
    radioButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Login.this.sdsVersion = 125;
      }
    });
    radioButton.setBounds(297, 324, 73, 24);
    this.frmDbHunter.getContentPane().add(radioButton);

    JRadioButton radioButton_1 = new JRadioButton("13.0");
    radioButton_1.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Login.this.sdsVersion = 13;
      }
    });
    radioButton_1.setBounds(389, 324, 121, 24);
    this.frmDbHunter.getContentPane().add(radioButton_1);

    JRadioButton rdbtnNewRadioButton = new JRadioButton("12.3.3");
    rdbtnNewRadioButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Login.this.sdsVersion = 1233;
      }
    });
    rdbtnNewRadioButton.setBounds(204, 324, 73, 24);
    this.frmDbHunter.getContentPane().add(rdbtnNewRadioButton);

    this.textPwd = new JTextField();
    this.textPwd.setBounds(519, 279, 114, 20);
    this.frmDbHunter.getContentPane().add(this.textPwd);
    this.textPwd.setColumns(10);

    JLabel lblPassword = new JLabel("Pwd");
    lblPassword.setBounds(485, 278, 25, 16);
    this.frmDbHunter.getContentPane().add(lblPassword);
  }
  private class SwingAction extends AbstractAction {
    public SwingAction() {
      putValue("Name", "SwingAction");
      putValue("ShortDescription", "Some short description");
    }

    public void actionPerformed(ActionEvent e)
    {
    }
  }
}