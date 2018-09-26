import org.apache.spark.api.java.*;
import org.apache.spark.sql.expressions.Window;
import static org.apache.spark.sql.functions.*;
import org.apache.spark.sql.*;


public class Main {
    private static Integer TIMESTAMP_WINDOW = 15 * 60; // 15 min
    private static String DATA_PATH = "../data/2015_07_22_mktplace_shop_web_log_sample.log.gz";
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder().getOrCreate();
        JavaRDD<LogEntry> logRDD = spark.read().textFile(DATA_PATH).javaRDD()
                .map(record -> {return new LogEntry(record);});

        Dataset<Row> logDF = spark.createDataFrame(logRDD, LogEntry.class);
        Dataset<Row> logWSessionId = logDF.select(col("*"),
                lag("timestamp",1).over(Window.partitionBy("client").orderBy("timestamp")).as("prevTimestamp"))
                .select(col("*"),
                        when(col("timestamp").cast("long").minus(col("prevTimestamp").cast("long"))
                                        .$less(lit(TIMESTAMP_WINDOW)), lit(0))
                                .otherwise(lit(1)).as("isNewSession"))
                .select(col("client"),
                        col("backend"),
                        col("timestamp"),
                        col("url"),
                        sum("isNewSession")
                                .over(Window.partitionBy("client")
                                        .orderBy("client", "timestamp"))
                                .as("sessionId"));

        Dataset<Row> session = logWSessionId.groupBy("client", "sessionId")
                .agg(min("timestamp").as("startTime"),
                        max("timestamp").as("endTime"),
                        count("*").as("count"),
                        countDistinct("url").as("uniqueURLVisits"))
                .select(col("*"),
                        col("endTime").cast("long").minus(col("startTime").cast("long")).as("duration"));
        

        session.write().option("header", "true").csv("../data/session");
        session.show(5);

        Dataset<Row> avgSessionTime = session.filter(col("duration").$greater(0))
                .select(sum("duration").divide(sum("count")).as("avgSessionTime"));
        
        avgSessionTime.show();

        Dataset<Row> userEngaging = session.groupBy("client")
                .agg(sum("duration").as("engaging")).orderBy(col("engaging").desc());
        
        userEngaging.show(5);

    }

}
