import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by apu on 03.11.2016.
 */
@XmlType(propOrder = {"gsmServer","gsmMoneyQuery","gprsServer",
                      "gprsPort","gprsPage","gprsTimeout","gprsOnOff"})
@XmlRootElement(name = "Configuration")
public class Configuration {
    private String  gsmServer;
    private String  gsmMoneyQuery;
    private String  gprsServer;
    private int     gprsPort;
    private String  gprsPage;
    private int     gprsTimeout;
    private boolean gprsOnOff;
    //private ConfigOutput[]  output;
    //private ConfigInput[]   input;
    //private ConfigUser[]    user;


    public Configuration() {
        gsmServer = "www.kyivstar.net";
        gsmMoneyQuery = "*111#";
        gprsServer = "primula.net.ua";
        gprsPort = 80;
        gprsPage = "test.php";
        gprsTimeout = 30;
        gprsOnOff = true;
    }

    public String getGsmServer() {
        return gsmServer;
    }

    @XmlElement
    public void setGsmServer(String gsmServer) {
        this.gsmServer = gsmServer;
    }

    public String getGsmMoneyQuery() {
        return gsmMoneyQuery;
    }

    @XmlElement
    public void setGsmMoneyQuery(String gsmMoneyQuery) {
        this.gsmMoneyQuery = gsmMoneyQuery;
    }

    public String getGprsServer() {
        return gprsServer;
    }

    @XmlElement
    public void setGprsServer(String gprsServer) {
        this.gprsServer = gprsServer;
    }

    public int getGprsPort() {
        return gprsPort;
    }

    @XmlElement
    public void setGprsPort(int gprsPort) {
        this.gprsPort = gprsPort;
    }

    public String getGprsPage() {
        return gprsPage;
    }

    @XmlElement
    public void setGprsPage(String gprsPage) {
        this.gprsPage = gprsPage;
    }

    public int getGprsTimeout() {
        return gprsTimeout;
    }

    @XmlElement
    public void setGprsTimeout(int gprsTimeout) {
        this.gprsTimeout = gprsTimeout;
    }

    public boolean isGprsOnOff() {
        return gprsOnOff;
    }

    @XmlElement
    public void setGprsOnOff(boolean gprsOnOff) {
        this.gprsOnOff = gprsOnOff;
    }
}
