package io.github.ivanfedorenkov;

import build.buf.protovalidate.Validator;
import build.buf.protovalidate.ValidatorFactory;
import com.google.protobuf.Timestamp;
import io.envoyproxy.pgv.ReflectiveValidatorIndex;
import io.envoyproxy.pgv.ValidatorIndex;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * JMH benchmark for comparing Protobuf validation performance in Java.
 *
 * <p>There are two widely used libraries for validating Protocol Buffers messages:
 *
 * <ul>
 *   <li><b>protoc-gen-validate (PGV)</b> – a code-generation-based validation library
 *   that generates static validation logic during compilation time.</li>
 *
 *   <li><b>Protovalidate</b> – a modern validation framework for Protobuf that
 *   evaluates validation rules dynamically at runtime using a CEL-based engine.</li>
 * </ul>
 *
 * <p>Protovalidate is the successor to protoc-gen-validate (PGV) and is designed
 * to provide improved flexibility, consistency across languages, and long-term evolution.
 *
 * <p>The goal of this benchmark is to measure and compare the validation
 * performance of simple Protobuf messages in Java using both approaches.
 *
 * <p>The benchmark focuses strictly on validation execution time and does not
 * measure serialization, deserialization, or message construction overhead.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(2)
@State(Scope.Thread)
public class Benchmark {
    private Validator protovalidateValidator;
    private ValidatorIndex pgvValidatorIndex;

    private io.github.ivanfedorenkov.pgv.PersonCard pgvPersonCard;
    private io.github.ivanfedorenkov.protovalidate.PersonCard protoValidatePersonCard;

    @Setup(Level.Trial)
    public void setup() {
        protovalidateValidator = ValidatorFactory.newBuilder().build();
        pgvValidatorIndex = new ReflectiveValidatorIndex();

        pgvPersonCard = buildPgvPersonCard();
        protoValidatePersonCard = buildProtovalidatePersonCard();
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void protovalidate(Blackhole blackhole) throws Exception {
        protovalidateValidator.validate(protoValidatePersonCard);
        blackhole.consume(protoValidatePersonCard);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void pgv(Blackhole blackhole) throws Exception {
        pgvValidatorIndex.validatorFor(io.github.ivanfedorenkov.pgv.PersonCard.class).assertValid(pgvPersonCard);
        blackhole.consume(pgvPersonCard);
    }

    public static void main(String[] args) throws Throwable {
        Options opts = new OptionsBuilder()
                .include(Benchmark.class.getSimpleName())
                .build();
        new Runner(opts).run();
    }

    private static io.github.ivanfedorenkov.pgv.PersonCard buildPgvPersonCard() {
        return io.github.ivanfedorenkov.pgv.PersonCard.newBuilder()
                .setId("1")
                .setFirstName("First Name")
                .setLastName("Last Name")
                .setMiddleName("Middle Name")
                .setEmail("example@example.com")
                .setPhone("0123456789")
                .setAge(50)
                .setSalary(100500)
                .setHeightCm(170)
                .setWeightKg(70)
                .setActive(true)
                .setCreatedAt(Timestamp.newBuilder().setSeconds(System.currentTimeMillis() / 1000).build())
                .setCreatedAt(Timestamp.newBuilder().setSeconds(System.currentTimeMillis() / 1000).build())
                .setAddress(io.github.ivanfedorenkov.pgv.Address.newBuilder()
                        .setStreet("Street")
                        .setCity("City")
                        .setCountry("Country")
                        .setPostalCode("99999")
                        .build())
                .addTags("Tag")
                .addRoles("Role")
                .setCompany("Company")
                .setPosition("Position")
                .setDepartment("Department")
                .setYearsOfExperience(20)
                .setBio("Biography")
                .build();
    }

    private static io.github.ivanfedorenkov.protovalidate.PersonCard buildProtovalidatePersonCard() {
        return io.github.ivanfedorenkov.protovalidate.PersonCard.newBuilder()
                .setId("1")
                .setFirstName("First Name")
                .setLastName("Last Name")
                .setMiddleName("Middle Name")
                .setEmail("example@example.com")
                .setPhone("0123456789")
                .setAge(50)
                .setSalary(100500)
                .setHeightCm(170)
                .setWeightKg(70)
                .setActive(true)
                .setCreatedAt(Timestamp.newBuilder().setSeconds(System.currentTimeMillis() / 1000).build())
                .setCreatedAt(Timestamp.newBuilder().setSeconds(System.currentTimeMillis() / 1000).build())
                .setAddress(io.github.ivanfedorenkov.protovalidate.Address.newBuilder()
                        .setStreet("Street")
                        .setCity("City")
                        .setCountry("Country")
                        .setPostalCode("99999")
                        .build())
                .addTags("Tag")
                .addRoles("Role")
                .setCompany("Company")
                .setPosition("Position")
                .setDepartment("Department")
                .setYearsOfExperience(20)
                .setBio("Biography")
                .build();
    }
}
