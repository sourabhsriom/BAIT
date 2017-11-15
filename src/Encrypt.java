
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import sun.misc.BASE64Encoder;

public class Encrypt
{
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
}