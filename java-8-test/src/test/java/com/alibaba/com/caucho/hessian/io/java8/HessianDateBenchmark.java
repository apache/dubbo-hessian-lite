package com.alibaba.com.caucho.hessian.io.java8;

import com.alibaba.com.caucho.hessian.io.base.SerializeTestBase;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author wuwen
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup
@Fork(1)
public abstract class HessianDateBenchmark extends SerializeTestBase {

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(HessianDateBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
    
    @State(Scope.Thread)
    public static class JavaUtilDateBenchmark extends HessianDateBenchmark {
        protected Date javaUtilDate;
        @Setup
        public void setup() {
            javaUtilDate = new Date();
        }
        @Benchmark
        public Object serializeDeserializeJavaUtilDate() throws Exception {
            return baseHessian2Serialize(javaUtilDate);
        }
    }

    @State(Scope.Thread)
    public static class LocalDateTimeBenchmark extends HessianDateBenchmark {
        @Param({"true", "false"})
        public String compactMode;
        
        protected LocalDateTime javaTimeLocalDateTime;
        
        @Setup(Level.Iteration)
        public void setUp() {
            javaTimeLocalDateTime = LocalDateTime.now();
            System.setProperty("com.caucho.hessian.io.java.time.serializer.compactMode", compactMode);
        }

        @Benchmark
        public Object serializeDeserializeLocalDateTime() throws Exception {
            return baseHessian2Serialize(javaTimeLocalDateTime);
        }
    }

    @State(Scope.Thread)
    public static class LocalDateBenchmark extends HessianDateBenchmark {
        protected LocalDate javaTimeLocalDate;

        @Param({"true", "false"})
        public String compactMode;
        
        @Setup(Level.Iteration)
        public void setUp() {
            javaTimeLocalDate = LocalDate.now();
            System.setProperty("com.caucho.hessian.io.java.time.serializer.compactMode", compactMode);
        }

        @Benchmark
        public Object serializeDeserializeLocalDate() throws Exception {
            return baseHessian2Serialize(javaTimeLocalDate);
        }
    }
}
