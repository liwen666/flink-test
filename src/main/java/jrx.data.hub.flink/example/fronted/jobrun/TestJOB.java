package jrx.data.hub.flink.example.fronted.jobrun;

import jrx.data.hub.flink.example.fronted.CliFrontendTestUtils;
import jrx.data.hub.flink.example.fronted.TestLogger;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.client.cli.CliFrontend;
import org.apache.flink.client.cli.CliFrontendParser;
import org.apache.flink.client.cli.DefaultCLI;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.GlobalConfiguration;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.Collections;

import static jrx.data.hub.flink.example.fronted.CliFrontendTestUtils.getTestJarPath;
//import apache.flink.kafka.shaded.org.apache.kafka.clients.producer.ProducerRecord
/**
 * <p>
 * 描述
 * </p>
 *
 * @author LW
 * @since 2021/3/27  17:17
 * job的参数请看
 * @see CliFrontendParser
 */
public class TestJOB implements Serializable {


    private static final long serialVersionUID = 4462038708572094628L;

    public static void main(String[] args) throws FileNotFoundException, MalformedURLException {
        System.setProperty("project.basedir", "D:\\flink12\\flink-1.12.1-src\\flink-engine");
        String[] parameters = {"-v", "-p", "text", getTestJarPath(null)};
//        final ParameterTool params = ParameterTool.fromArgs(parameters);

        Configuration configuration = GlobalConfiguration.loadConfiguration(CliFrontendTestUtils.getConfigDir());

        CliFrontend testFrontend =
                new CliFrontend(configuration, Collections.singletonList(new DefaultCLI()));
        testFrontend.parseAndRun(parameters);
    }

    /**
     * 非集群JOB测试
     * @throws FileNotFoundException
     * @throws MalformedURLException
     */
    @Test
    public  void init() throws FileNotFoundException, MalformedURLException {
        System.setProperty("project.basedir", "D:\\flink12\\flink-1.12.1-src\\flink-engine");
//        String[] parameters = {"run","-h", "-m", "11.11.1.79:8081","-c","jrx.data.hub.flink.example.scoket.SocketWindowWordCount", getTestJarPath(null)};
//        String[] parameters = {"run","-m", "11.11.1.79:8081","-c","jrx.data.hub.flink.example.scoket.SocketWindowWordCount", getTestJarPath(null)};
//        String[] parameters = {"run","-m", "10.0.22.87:8081","-c","jrx.data.hub.flink.example.scoket.SocketWindowWordCount", getTestJarPath(null)};
        String[] parameters = {"run","-m", "localhost:8081","-c","jrx.data.hub.flink.example.scoket.SocketWindowWordCount", getTestJarPath(null)};
//        final ParameterTool params = ParameterTool.fromArgs(parameters);

//        Configuration configuration = GlobalConfiguration.loadConfiguration(CliFrontendTestUtils.getConfigDir());
        Configuration configuration = GlobalConfiguration.loadConfiguration(CliFrontendTestUtils.getSampleCfg());

        CliFrontend testFrontend =
                new CliFrontend(configuration, Collections.singletonList(new DefaultCLI()));

        testFrontend.parseAndRun(parameters);
    }



    @Test
    public  void testCdc() throws FileNotFoundException, MalformedURLException {
        System.setProperty("project.basedir", "D:\\workspace\\strategy-topology\\job-cdc");
//        String[] parameters = {"run","-h", "-m", "11.11.1.79:8081","-c","jrx.data.hub.flink.example.scoket.SocketWindowWordCount", getTestJarPath(null)};
//        String[] parameters = {"run","-m", "11.11.1.79:8081"
        String[] parameters = {"run","-m", "localhost:8081"
//        String[] parameters = {"run","-m", "10.0.22.87:8081"
                ,"-c","com.riveretech.est.cdc.JobCdcApp"
                , getTestJarPath("job-cdc-1.0.0-SNAPSHOT.jar"),"--cdc_config_version","1","--dependency_external","C:\\Users\\liwen\\Desktop\\lib"};
//        final ParameterTool params = ParameterTool.fromArgs(parameters);

//        Configuration configuration = GlobalConfiguration.loadConfiguration(CliFrontendTestUtils.getConfigDir());
        Configuration configuration = GlobalConfiguration.loadConfiguration(CliFrontendTestUtils.getSampleCfg());

        CliFrontend testFrontend =
                new CliFrontend(configuration, Collections.singletonList(new DefaultCLI()));

        testFrontend.parseAndRun(parameters);
    }

}
