# MEC Virtual Resource Allocation

[![maven](https://img.shields.io/badge/Maven-v3.9.8-cyan?logo=apachemaven)](https://maven.apache.org/docs/3.9.8/release-notes.html)
[![style](https://img.shields.io/badge/style-Google%20Java%20Style-informational)](https://google.github.io/styleguide/javaguide.html)
[![Javadoc](https://img.shields.io/badge/JavaDoc-Online-green)](https://alessiobugetti.github.io/MECVirtualResourceAllocation-Javadoc)
[![License](https://img.shields.io/github/license/AlessioBugetti/MECVirtualResourceAllocation)](https://opensource.org/licenses/GPL-3.0)

Welcome to the MEC Virtual Resource Allocation repository. This project implements a hypergraph matching approach for efficient virtual machine (VM) placement and resource allocation in Mobile Edge Computing (MEC) environments. The solution aims to minimize energy consumption through an optimized VM placement matrix using both sequential and local search algorithms.

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Architecture](#architecture)
- [Installation](#installation)
- [Usage](#usage)
- [Code Structure](#code-structure)
- [Documentation](#documentation)
- [Development Process](#development-process)
- [Dependencies and Plugins](#dependencies-and-plugins)
- [License](#license)

## Introduction

With the proliferation of IoT and 5G, MEC has become a vital solution to extend computation and storage resources to the network edge, reducing latency and improving user experience. This project addresses the VM placement and virtual resource allocation problem by modeling it as a hypergraph and solving it using a hypergraph matching algorithm. This code implements the algorithm proposed in the paper L. Zhang, H. Zhang, L. Yu, H. Xu, L. Song and Z. Han, *Virtual Resource Allocation for Mobile Edge Computing: A Hypergraph Matching Approach,* 2019 IEEE Global Communications Conference (GLOBECOM), Waikoloa, HI, USA, 2019, pp. 1-6, doi: [10.1109/GLOBECOM38437.2019.9013384](https://doi.org/10.1109/GLOBECOM38437.2019.9013384).

**Keywords:** Data centers; Computational modeling; Energy consumption; Task analysis; Optimization; Cloud computing; Servers

## Features

- **Hypergraph Modeling:** Representation of VM and PM relationships as a hypergraph.
- **VM Placement Optimization:** Algorithms to minimize energy consumption through optimized VM placement.
- **Conflict Graph Generation:** Creation of conflict graphs to handle hypergraph conflicts.
- **Visualization:** Graphical representation of hypergraphs and conflict graphs using Java Swing.
- **Evaluation:** Execution time and energy consumption evaluation of different algorithms.

## Architecture

The project is structured into several packages, each responsible for different aspects of the hypergraph-based resource allocation:

- `org.unifi.mecvirtualresourceallocation.graph`: Core classes for hypergraph and conflict graph modeling.
- `org.unifi.mecvirtualresourceallocation.algorithm`: Implementation of VM placement algorithms.
- `org.unifi.mecvirtualresourceallocation.evaluation`: Evaluation classes for assessing algorithm performance.
- `org.unifi.mecvirtualresourceallocation.visualization`: Visualization components for displaying graphs.

## Installation

To build and run the project, ensure you have Maven and Java installed. Follow these steps:

1. **Clone the repository:**
```sh
git clone https://github.com/alessiobugetti/MECVirtualResourceAllocation.git
cd MECVirtualResourceAllocation
```

2. **Build the project:**
```sh
mvn clean install
```

## Usage

### Running an Evaluator

To run an evaluator, you need to define a `main` method, create an instance of the evaluator, and execute the `execute()` method. Here is an example of how to run the `ExecutionTimeEvaluator`:

```java
public class Main {

  public static void main(String[] args) {
    Evaluator evaluator = new ExecutionTimeEvaluator();
    SwingUtilities.invokeLater(evaluator::execute);
  }
}
```

Similarly, you can run other evaluators by creating instances of their respective classes:

**Energy Consumption Reduction Evaluator:**

```java
public class Main {

   public static void main(String[] args) {
      Evaluator evaluator = new EnergyConsumptionReductionEvaluator();
      SwingUtilities.invokeLater(evaluator::execute);
   }
}
```

**Energy Consumption Comparison Evaluator:**

```java
public class Main {

   public static void main(String[] args) {
      Evaluator evaluator = new EnergyConsumptionComparisonEvaluator();
      SwingUtilities.invokeLater(evaluator::execute);
   }
}
```

After defining the main method, you can run the application using:

```sh
mvn exec:java -Dexec.mainClass="org.unifi.mecvirtualresourceallocation.Main"
```

### Running Tests

To run the tests, use:

```sh
mvn test
```

## Code Structure

The code is organized into several key packages:

### Graph Package (`org.unifi.mecvirtualresourceallocation.graph`):
- `Vertex.java`: Represents a vertex in the hypergraph.
- `HyperEdge.java`: Represents a hyperedge in the hypergraph.
- `HyperGraph.java`: Manages vertices and hyperedges.
- `ConflictGraph.java`: Generates and manages conflict graphs.

### Algorithm Package (`org.unifi.mecvirtualresourceallocation.algorithm`):
- `AllocationStrategy.java`: Interface for allocation strategies.
- `SequentialSearchStrategy.java`: Implementation of sequential search strategy.
- `LocalSearchStrategy.java`: Implementation of local search strategy.

### Evaluation Package (`org.unifi.mecvirtualresourceallocation.evaluation`):
- `Evaluator.java`: Interface for evaluators.
- `ExecutionTimeEvaluator.java`: Evaluates execution time of algorithms.
- `EnergyConsumptionEvaluator.java`: Abstract class for energy consumption evaluation.
- `EnergyConsumptionReductionEvaluator.java`: Evaluates energy consumption reduction.
- `EnergyConsumptionComparisonEvaluator.java`: Compares energy consumption between algorithms.

### Visualization Package (`org.unifi.mecvirtualresourceallocation.visualization`):
- `GraphPanel.java`: Abstract class for graph visualization panels.
- `HyperGraphPanel.java`: Visualization panel for hypergraphs.
- `ConflictGraphPanel.java`: Visualization panel for conflict graphs.

## Documentation

The Javadoc documentation of the code is available in the `target/apidocs` directory after building the project. Open the `index.html` file in a web browser to view the documentation.

Alternatively, the Javadoc documentation can be accessed at: [alessiobugetti.github.io/MECVirtualResourceAllocation-Javadoc](https://alessiobugetti.github.io/MECVirtualResourceAllocation-Javadoc)

## Development Process

### Code Formatting

`google-java-format` is used for consistent code formatting. The formatting is enforced using the Spotless plugin. Run the following command to format the code:

```sh
mvn spotless:apply
```

### Static Analysis

Static code analysis is performed using SpotBugs and Checkstyle. To run the static analysis tools, use:

```sh
mvn spotbugs:check
```

or

```sh
mvn checkstyle:check
```

### Code Coverage

Code coverage is measured using JaCoCo. To generate the coverage report, run:

```sh
mvn test jacoco:report
```

## Dependencies and Plugins

The project leverages several Maven plugins and dependencies to streamline development and ensure code quality:

- **JUnit Jupiter**: For unit testing.
- **Spotless**: For code formatting.
- **SpotBugs**: For static code analysis.
- **Checkstyle**: For code style checks.
- **JaCoCo**: For code coverage reports.
- **JFreeChart**: For generating evaluation charts.

## License

This project is licensed under the GPL-3.0 License. See the LICENSE file for more details.
