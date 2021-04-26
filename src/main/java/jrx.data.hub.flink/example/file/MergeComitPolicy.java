package jrx.data.hub.flink.example.file;

import com.alibaba.fastjson.JSON;

import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.table.filesystem.PartitionCommitPolicy;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author jie.lan
 * @Date 2021/3/26 16:31
 */
public class MergeComitPolicy implements PartitionCommitPolicy {
    @Override
    public void commit(Context context) throws Exception {

        String inputPath = context.partitionPath().toString();
        List<String> partitionValues = context.partitionValues();
        String db=partitionValues.get(0);
        String sysName= "";
        switch(db.split("_")[0]){
            case "test":
            case "anytxn":
                sysName="txn";
                break;
            case "anytask":
                sysName="task";
                break;
            default:
                break;
        }
        String tb=partitionValues.get(1);
        String bizDate = partitionValues.get(2);
        String outPath = inputPath.replace("raw_binlog", "merged_binlog") + "/" + bizDate;
        System.out.println("======开始"+tb+"表的merge任务========partition="+bizDate);
        //获取运行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //获取文件中的内容
        DataSource<String> text = env.readTextFile(inputPath);

        //排序后合并成一个大文件
        text.map(
                (String value) -> {
                    Long ts = JSON.parseObject(value).getJSONObject("source").getLong("ts_ms");
                    return new Tuple2<>(value, ts);
                }
        ).returns(new TypeHint<Tuple2<String, Long>>() {
        })
                .sortPartition(1, Order.ASCENDING)
                .setParallelism(1)
                .map((Tuple2<String, Long> value) -> value.f0)
                .returns(Types.STRING)
                .writeAsText(outPath, FileSystem.WriteMode.OVERWRITE);
        //这任务是同步的。会等待任务结束后再往下执行
        env.execute("mergeBinlogFile");

        //如果是初始化数据。那么把快照任务也做了。
        if("1970-01-01".equals(bizDate)){
            //把租户号找出来
            String pattern = "^\\S+/(\\S+\\d+)/raw_binlog/\\S+$";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(inputPath);
            if(m.matches()){
                String tenant=m.group(1);
                //为此表做快照任务。
//                DailySnapshotForOneTable.main(new String[]{tenant,"1970-01-01",sysName,tb});


            }
        }
    }


}
