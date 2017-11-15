
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Assets extends JFrame
{
  private JPanel contentPane;
  private JTextField textBA = new JTextField();
  protected Connection connect;
  protected XSSFWorkbook workbook1;
  private JTextField textIP;
  private JTextField textUserName;
  private JTextField textDB;
  private JPasswordField textPassword;
  private JTextField textAreas;
  private JTextField textZones;
  private JTextField textBanks;

  public static void main(String[] args)
  {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          Assets frame = new Assets();
          frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  public Assets()
  {
    setIconImage(Toolkit.getDefaultToolkit().getImage(
      "C:\\Users\\ssriom\\Pictures\\baby.jpg"));
    setTitle("Asset Module");
    setDefaultCloseOperation(3);
    setTitle("BAIT :  Asset Module");
    setBounds(100, 100, 733, 529);
    this.contentPane = new JPanel();
    this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(this.contentPane);
    this.contentPane.setLayout(null);

    JButton btnNewButton = new JButton("Browse AZB File");
    btnNewButton.setFont(new Font("Dialog", 1, 10));
    btnNewButton.addActionListener(new ActionListener()
    {
      private FileBrowser fb;

      public void actionPerformed(ActionEvent e)
      {
        this.fb = new FileBrowser();
        Assets.this.textBA.setText(this.fb.fileChooser.getSelectedFile().toString());
      }
    });
    btnNewButton.setBounds(511, 252, 129, 23);
    this.contentPane.add(btnNewButton);

    this.textBA.setBounds(130, 252, 319, 22);
    this.contentPane.add(this.textBA);

    JButton btnNewButton_1 = new JButton("Import AZB");
    btnNewButton_1.setFont(new Font("Dialog", 1, 10));

    btnNewButton_1.addActionListener(new ActionListener() { private Integer maxAreaID;
      private int siteID;
      private int noOfAreas;
      private int noOfZones;
      private long maxID;
      private int noOfBanks;

      public void actionPerformed(ActionEvent e) { try { Assets.this.connect = DBConnect.dbConnector(Assets.this.textIP.getText(), Assets.this.textUserName.getText(), 
            Assets.this.textPassword.getText(), Assets.this.textDB.getText());
          FileInputStream file1 = new FileInputStream(Assets.this.textBA
            .getText());
          Assets.this.workbook1 = new XSSFWorkbook(file1);
          XSSFSheet sheet = Assets.this.workbook1.getSheetAt(0);

          this.noOfAreas = Integer.parseInt(Assets.this.textAreas.getText());
          this.noOfZones = Integer.parseInt(Assets.this.textZones.getText());

          this.noOfBanks = Integer.parseInt(Assets.this.textBanks.getText());
          Random r = new Random();
          for (int areas = 1; areas <= this.noOfAreas; areas++) {
            Row rowArea = sheet.getRow(areas);
            Cell cellArea = rowArea.getCell(0);

            System.out.println("Area number : " + 
              rowArea.getCell(0));
            String siteIQ = "select (SITE_ID) as SITEID  from SITECONFIG.SITE where SITE_ID <> 99";

            ResultSet rsSite = Assets.this.connect.createStatement()
              .executeQuery(siteIQ);

            if (rsSite.next())
            {
              this.siteID = rsSite.getInt("SITEID");
            }

            String areaQ = "select MAX(AREA_SHORT_NAME) as MAXAREAID, MAX(AREA_ID) as MAXID from ASSET.AREA where AREA_LONG_NAME like '%AREA%' and SITE_ID = 99";

            ResultSet rs = Assets.this.connect.createStatement().executeQuery(
              areaQ);

            if (rs.next()) {
              this.maxAreaID = Integer.valueOf(rs.getInt("MAXAREAID"));
              this.maxID = rs.getLong("MAXID");

              System.out.println("Older Area : " + this.maxID);
            }

            int i1 = r.nextInt(200) + 10;

            long newAreaID = i1 + 1;
            System.out.println("new Area ID : " + newAreaID);
            try
            {
              String iAreaQ = 
                "insert into ASSET.AREA  values( ?, NULL, 1, ?, ? , ?, CURRENT_TIMESTAMP, 10000, NULL, NULL, NULL, NULL) ";
              PreparedStatement areaSt = 
                Assets.this.connect.prepareStatement(iAreaQ);

              areaSt.setLong(1, newAreaID);
              areaSt.setInt(2, this.siteID);
              areaSt.setInt(3, areas+ 1);

              areaSt.setString(4, cellArea.getStringCellValue());
              areaSt.executeQuery();
            }
            catch (Exception e1) {
              e1.printStackTrace();
            }
          }

          for (int zones = 1; zones <= this.noOfZones; zones++) {
            Row rowIt = sheet.getRow(zones);

            Cell cellZone = rowIt.getCell(1);
            Cell cellArea = rowIt.getCell(2);

            String zone = cellZone.getStringCellValue();

            String area = cellArea.getStringCellValue();

            System.out.println("Zone : " + zone + 
              " Parent Area : " + area);

            long AIDZone = Assets.this.findAreaIDOfZone(
              cellZone.getStringCellValue(), 
              cellArea.getStringCellValue());

            long maxID = Assets.this.getMaxID(rowIt.getCell(1)
              .getStringCellValue());

            String iZoneAreaQ = "insert into ASSET.AREA values (?,?,3, ? , ?, ?, CURRENT_TIMESTAMP, 10000, NULL, NULL, NULL, NULL)";

            PreparedStatement iZAQ = Assets.this.connect
              .prepareStatement(iZoneAreaQ);

            iZAQ.setLong(1, maxID);
            iZAQ.setLong(2, AIDZone);
            iZAQ.setInt(3, this.siteID);
            iZAQ.setInt(4, zones);
            iZAQ.setString(5, zone);
            try
            {
              iZAQ.executeQuery();
            }
            catch (Exception e1) {
              System.out.println("check error");
            }

          }

          for (int banks = 0; banks <= this.noOfBanks; banks++)
          {
            Row rowBanks = sheet.getRow(banks);
            Cell cellBank = rowBanks.getCell(3);
            Cell cellZone = rowBanks.getCell(4);

            String bank = cellBank.getStringCellValue();
            String zone = cellZone.getStringCellValue();

            long zoneID = Assets.this.findAreaIDOfZone(cellBank.getStringCellValue(), cellZone.getStringCellValue());
            long bankID = Assets.this.getMaxID(bank);

            String iBZQ = "insert into ASSET.AREA values(?,?,2,?,?,?, CURRENT_TIMESTAMP, 10000, NULL, NULL, NULL, NULL)";
            try
            {
              PreparedStatement ps = Assets.this.connect.prepareStatement(iBZQ);

              ps.setLong(1, bankID);
              ps.setLong(2, zoneID);
              ps.setInt(3, this.siteID);
              ps.setInt(4, banks);
              ps.setString(5, bank);

              ps.executeQuery();
            }
            catch (Exception e1)
            {
              e1.printStackTrace();
            }

          }

          System.out.println("AZB Imported!");
        }
        catch (Exception e1)
        {
          e1.printStackTrace();
        }
      }
    });
    btnNewButton_1.setBounds(230, 376, 130, 23);
    this.contentPane.add(btnNewButton_1);

    this.textIP = new JTextField();
    this.textIP.setBounds(130, 62, 319, 20);
    this.contentPane.add(this.textIP);
    this.textIP.setColumns(10);

    JLabel lblDbIp = new JLabel("DB IP");
    lblDbIp.setBounds(52, 61, 52, 23);
    this.contentPane.add(lblDbIp);

    this.textUserName = new JTextField();
    this.textUserName.setBounds(130, 109, 319, 20);
    this.contentPane.add(this.textUserName);
    this.textUserName.setColumns(10);

    JLabel lblUsername = new JLabel("username");
    lblUsername.setBounds(52, 108, 89, 23);
    this.contentPane.add(lblUsername);

    this.textDB = new JTextField();
    this.textDB.setBounds(130, 205, 319, 20);
    this.contentPane.add(this.textDB);
    this.textDB.setColumns(10);

    JLabel lblNewLabel = new JLabel("Password");
    lblNewLabel.setBounds(52, 142, 124, 50);
    this.contentPane.add(lblNewLabel);

    JLabel lblNewLabel_1 = new JLabel("DB name");
    lblNewLabel_1.setBounds(52, 190, 52, 50);
    this.contentPane.add(lblNewLabel_1);

    this.textPassword = new JPasswordField();
    this.textPassword.setEchoChar('*');
    this.textPassword.setBounds(130, 159, 319, 20);
    this.contentPane.add(this.textPassword);

    this.textAreas = new JTextField();
    this.textAreas.setBounds(130, 325, 76, 20);
    this.contentPane.add(this.textAreas);
    this.textAreas.setColumns(10);

    this.textZones = new JTextField();
    this.textZones.setColumns(10);
    this.textZones.setBounds(254, 325, 76, 20);
    this.contentPane.add(this.textZones);

    this.textBanks = new JTextField();
    this.textBanks.setColumns(10);
    this.textBanks.setBounds(373, 325, 76, 20);
    this.contentPane.add(this.textBanks);

    JLabel lblNewLabel_2 = new JLabel("Areas");
    lblNewLabel_2.setBounds(151, 302, 52, 23);
    this.contentPane.add(lblNewLabel_2);

    JLabel lblZones = new JLabel("Zones");
    lblZones.setBounds(270, 302, 52, 23);
    this.contentPane.add(lblZones);

    JLabel lblBanks = new JLabel("Banks");
    lblBanks.setBounds(397, 302, 52, 23);
    this.contentPane.add(lblBanks);

    JLabel lblNewLabel_3 = new JLabel("AZB File");
    lblNewLabel_3.setBounds(52, 252, 76, 23);
    this.contentPane.add(lblNewLabel_3);

    JLabel label = new JLabel("");
    label.setIcon(new ImageIcon("C:\\Users\\ssriom\\Pictures\\aarven2.jpg"));
    label.setBounds(511, 29, 150, 210);
    this.contentPane.add(label);
  }

  protected long getMaxID(String area)
  {
    String areaQ = "select  MAX(AREA_ID) as MAXID from ASSET.AREA ";
    try
    {
      ResultSet rs = this.connect.createStatement().executeQuery(areaQ);
      if (rs.next())
      {
        long maxID = rs.getLong("MAXID");

        System.out.println("Older Area : " + maxID);

        return maxID + 1L;
      }
    }
    catch (SQLException e) {
      e.printStackTrace();
    }

    return 0L;
  }

  protected long findAreaIDOfZone(String zone, String area) {
    String areaId = "select (AREA_ID) as AREAID from ASSET.AREA where AREA_LONG_NAME = ? and SITE_ID <> 99";
    try
    {
      PreparedStatement ps4 = this.connect.prepareStatement(areaId);
      ps4.setString(1, area);

      ResultSet rs4 = ps4.executeQuery();
      if (rs4.next())
      {
        return rs4.getLong("AREAID");
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }

    return 0L;
  }
}