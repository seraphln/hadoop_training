import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

public class WordCount {
    public static class Map extends MapReduceBase 
        implements Mapper<LongWritable, Text, Text, IntWritable>
    {
        private final static IntWritable one = new IntWritable();
        private Text word = new Text();

        public void map(
            LongWritable key,
            Text value,
            OutputCollector<Text, IntWritable> output,
            Reporter reporter
        ) throws IOException {
            String line = value.toString();
            StringTokenizer tokenizer = new StringTokenizer(line);
            while (tokenizer.hasMoreTokens()) {
                word.set(tokenizer.nextToken());
                output.collect(word, one);
            }
        }
    }

    public static class Reduce extends MapReduceBase
        implements Reducer<Text, IntWritable, Text, IntWritable>
    {
        public void reduce(
            Text key,
            Iterator<IntWritable> values,
            OutputCollector<Text, IntWritable> output,
            Reporter reporter
        ) throws IOException {
            int sum = 0;
            
            while (values.hasNext()) {
                sum += values.next().get();
            }

            output.collect(key, new IntWritable(sum));
        } 
    }

    public static void main(String[] args) throws Exception {
        JobConf job = new JobConf(WordCount.class);
        job.setJobName("wordcount");
        Path in = new Path(args[0]);
        Path out = new Path(args[1]);

        FileInputFormat.setInputPaths(job, in);
        FileOutputFormat.setOutputPath(job, out);
        
        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);
    
        job.setInputFormat(TextInputFormat.class);
        job.setOutputFormat(TextOutputFormat.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        //job.set("key.value.separator.in.input.line", ",");

        JobClient.runJob(job);
    }
}
