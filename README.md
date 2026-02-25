# protovalidate-pgv-java-jmh-benchmark

## Introduction

There are two widely used libraries for validating Protocol Buffers (Protobuf) messages:

### protoc-gen-validate (PGV)

**protoc-gen-validate (PGV)** is a Protobuf plugin that generates static validation code at compile time.  
Validation rules are defined as annotations in `.proto` files, and the plugin produces strongly-typed validation logic for each message.

This approach results in direct method calls with minimal runtime indirection.

### Protovalidate

**Protovalidate** is a modern Protobuf validation framework that evaluates validation rules dynamically at runtime using a CEL (Common Expression Language) engine.

It aims to provide:
- Greater flexibility
- Cross-language consistency
- Improved long-term maintainability

Protovalidate is the successor to protoc-gen-validate (PGV).

---

## Goal of the Experiment

The purpose of this experiment is to measure and compare the validation performance of simple Protobuf messages in Java.

The benchmark focuses exclusively on:

- Validation execution time
- Runtime overhead of each validation approach

The benchmark does NOT measure:

- Protobuf serialization/deserialization
- Message construction
- Network or I/O overhead

All measurements are performed using JMH to ensure reliable and reproducible results.

---

## Benchmark Setup

The benchmark validates structurally identical Protobuf messages:

- A `PersonCard` message
- 20+ fields
- Mixed scalar types
- Nested objects
- Repeated fields
- Timestamps

Two schema variants are used:

- One using PGV validation annotations
- One using Protovalidate standard rules

Both schemas use only validation rules that are supported by *both* libraries to ensure a fair comparison.

---

## Benchmark Methodology

- Pre-generated valid message instances
- Separate benchmarks for:
    - Valid messages
    - Invalid messages
- Warmup and measurement iterations configured via JMH defaults
- Throughput and average time modes

---

## Results

```
Result "io.github.ivanfedorenkov.Benchmark.pgv":
  2647.880 ±(99.9%) 66.746 ns/op [Average]
  (min, avg, max) = (2596.501, 2647.880, 2694.515), stdev = 44.148
  CI (99.9%): [2581.134, 2714.626] (assumes normal distribution)

Result "io.github.ivanfedorenkov.Benchmark.protovalidate":
  70357.347 ±(99.9%) 897.620 ns/op [Average]
  (min, avg, max) = (69459.378, 70357.347, 71091.945), stdev = 593.720
  CI (99.9%): [69459.727, 71254.967] (assumes normal distribution)

Benchmark results (average time mode):
Benchmark Mode Cnt Score Error Units
Benchmark.pgv avgt 10 2647.880 ± 66.746 ns/op
Benchmark.protovalidate avgt 10 70357.347 ± 897.620 ns/op
```