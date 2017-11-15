import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class FileBrowser extends JFrame
{
  JFileChooser fileChooser;
  private JPanel contentPane;

  public static void main(String[] args)
  {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          FileBrowser frame = new FileBrowser();
          frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  public FileBrowser()
  {
    setDefaultCloseOperation(3);
    setBounds(100, 100, 450, 300);
    this.contentPane = new JPanel();
    this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    this.contentPane.setLayout(new BorderLayout(0, 0));
    setContentPane(this.contentPane);

    this.fileChooser = new JFileChooser();
    this.contentPane.add(this.fileChooser, "Center");
    int returnval = this.fileChooser.showDialog(this.contentPane, "Pick");
  }
}