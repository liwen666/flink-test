package jrx.data.hub.flink.example.table;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.PipelineOptions;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.graph.StreamGraph;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableConfig;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author LW
 * @since 2021/3/20  15:39
 */
public class MysqlComplemented {

    public static void main(String[] args) throws Exception {
        final ParameterTool params = ParameterTool.fromArgs(args);
        for (String a : args) {
            System.out.print(a + "  ");
        }
        String script = params.has("script") ? params.get("script") : null;
        if (null == script) {
            System.err.println("请指定脚本的文件夹路径 --script filepath");
            return;
        }
        File file = new File(script);
        List<File> fileList = new ArrayList<>();
        listFists(file, fileList);
        if (fileList.size() == 0) {
            System.err.println("脚本文件数量为0");
            return;
        }
        fileList.stream().forEach(x -> {
            System.out.println(x.getPath() + x.getName());
            System.out.println("-----------------------------------------------------------------------");
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(x);
                byte[] cache = new byte[fileInputStream.available()];
                fileInputStream.read(cache);
                String scriptFile = new String(cache, "utf-8");
                String[] split = scriptFile.split("\\|\\|\\|");
                String source = split[0].split("->")[1];
                String query = split[1].split("->")[1];
                String sink = split[2].split("->")[1];
                String insert = split[3].split("->")[1];
                System.out.println("开始创建environment");
                String jobName = "补数任务";
                Configuration config = new Configuration();
                config.set(PipelineOptions.NAME, jobName);
                StreamExecutionEnvironment blinkStreamEnv = StreamExecutionEnvironment.getExecutionEnvironment(config);
                DataStreamSink<Integer> print = blinkStreamEnv.fromElements(1, 2, 3).print();
                StreamGraph streamGraph = blinkStreamEnv.getStreamGraph();
                System.out.println(streamGraph.getJobName());
                blinkStreamEnv.setParallelism(1);
                EnvironmentSettings blinkStreamSettings = EnvironmentSettings.newInstance()
                        .useBlinkPlanner()
                        .inStreamingMode()
                        .build();
                TableConfig tableConfig = new TableConfig();
                tableConfig.addConfiguration(config);
                StreamTableEnvironment blinkStreamTableEnv = StreamTableEnvironment.create(blinkStreamEnv, blinkStreamSettings);
//                StreamTableEnvironment blinkStreamTableEnv = StreamTableEnvironment.create(blinkStreamEnv, tableConfig);
                System.out.println("创建表" + source);
                blinkStreamTableEnv.executeSql(source);
                TableResult tableResult = blinkStreamTableEnv.executeSql(query);
                tableResult.print();
                System.out.println("-----------------------------------------------------------------------");

                blinkStreamTableEnv.executeSql(sink);

                System.out.println("-----------------------------------------------------------------------");

                blinkStreamTableEnv.executeSql(insert);

                try {
//                    blinkStreamTableEnv.execute("Blink Stream SQL demo PG");
                    blinkStreamTableEnv.execute("Blink Stream SQL demo PG");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void listFists(File file, List<File> fileList) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                listFists(f, fileList);
            }
        } else {
            if (file.getName().endsWith(".script")) {
                fileList.add(file);
            }
        }
    }
}
