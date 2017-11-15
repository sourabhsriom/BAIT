import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class JobImporter extends JFrame
{
  private JPanel contentPane;
  private JTextField textField;

  public static void main(String[] args)
  {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          JobImporter frame = new JobImporter();
          frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  public JobImporter()
  {
    setDefaultCloseOperation(3);
    setBounds(100, 100, 450, 300);
    this.contentPane = new JPanel();
    this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(this.contentPane);
    this.contentPane.setLayout(null);

    this.textField = new JTextField();
    this.textField.setBounds(195, 30, 86, 20);
    this.contentPane.add(this.textField);
    this.textField.setColumns(10);
  }
}