package jrx.data.hub.flink.example.fronted.apprun;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.client.cli.CliFrontend;
import org.apache.flink.client.cli.DefaultCLI;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.shaded.guava18.com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author LW
 * @since 2021/5/25  13:49
 */
public class AppRunnerAgency {

    public static void main(String[] args) {
        ArrayList<String> strings = Lists.newArrayList(args);
        strings.remove(0);
        strings.remove(4);
        ParameterTool parameters = ParameterTool.fromArgs(strings.toArray(new String[0]));
        String projectHome = parameters.get("project_home");
        System.out.println(projectHome);
//        String[] parameters = {"run","-m", "10.0.22.87:8081","-c","com.riveretech.est.cdc.JobCdcApp", "D:\\flink12\\flink-1.12.1-src\\flink-engine\\src\\main\\java\\jrx.data.hub.flink\\example\\fronted\\job-cdc-1.0-SNAPSHOT.jar","--dependency_external","C:\\Users\\liwen\\Desktop\\lib"};
//        String[] parameters = {"run","-m", "10.0.22.87:8081","-c","jrx.anyest.flink.highsea.HighSeaCustomerPushingJob", "D:\\flink12\\flink-1.12.1-src\\flink-engine\\src\\main\\java\\jrx.data.hub.flink\\example\\fronted\\jrx-flink-highsea-1.0.0-RELEASE.jar","--dependency_external","C:\\Users\\liwen\\Desktop\\lib"};
//        String[] parameters = {"run", "-m", "10.0.22.87:8081", "-c", "com.rivertech.est.query.SqlQueryApp", "D:\\flink12\\flink-1.12.1-src\\flink-engine\\src\\main\\java\\jrx.data.hub.flink\\example\\fronted\\flinksql-query-1.0.0-SNAPSHOT.jar", "--dependency_external", "C:\\Users\\liwen\\Desktop\\lib"};
        Configuration configuration = GlobalConfiguration.loadConfiguration(projectHome+"/conf/");
//        Configuration configuration = GlobalConfiguration.loadConfiguration("D:\\flink12\\flink-1.12.1-src\\flink-engine\\src\\main\\resources\\sample\\");
        CliFrontend testFrontend =
                new CliFrontend(configuration, Collections.singletonList(new DefaultCLI()));
//        testFrontend.parseAndRun(args);
        testFrontend.parseAndRun(args);
    }
}
