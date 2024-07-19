# PageRank Implementation in Spark and MapReduce

Welcome to the PageRank Implementation project! This repository contains the implementation of the PageRank algorithm using both Apache Spark and the MapReduce programming model. The project aims to provide insights into Spark's lazy evaluation, actions, transformations, and the MapReduce paradigm.

## Goals

1. **Understand Spark's Execution Model**: Gain a deeper understanding of actions, transformations, and lazy evaluation in Spark.
2. **Implement PageRank**: Implement the PageRank algorithm in both Spark and MapReduce to explore different big data processing frameworks.

## Features

- **PageRank Algorithm in Spark**: Efficiently compute PageRank using Spark's RDD-based API.
- **Handling Dangling Pages**: Implement the single-dummy-page approach to handle dangling pages in the PageRank algorithm.
- **Synthetic Data Generation**: Easily generate synthetic graphs for testing and debugging.
- **RDD Lineage Exploration**: Analyze the lineage of RDDs to understand the implications of Spark's lazy evaluation.
- **MapReduce Implementation**: Implement PageRank using the MapReduce programming model.

## Getting Started

### Prerequisites

- **Apache Spark**: Ensure you have Apache Spark installed. [Download Spark](https://spark.apache.org/downloads.html)
- **Hadoop**: For running the MapReduce implementation, ensure you have a Hadoop environment set up. [Download Hadoop](https://hadoop.apache.org/releases.html)
- **Scala**: The Spark implementation is written in Scala. [Download Scala](https://www.scala-lang.org/download/)

### Installation

1. **Clone the Repository**:
   ```sh
   git clone https://github.com/YOUR-USERNAME/YOUR-REPO-NAME.git
   cd YOUR-REPO-NAME
   ```

2. **Build the Project**:
   ```sh
   sbt clean compile
   ```

### Running the PageRank Program in Spark

1. **Generate Synthetic Data**:
   ```sh
   sbt "runMain com.example.PageRankGenerator --k 100"
   ```

2. **Run the PageRank Program**:
   ```sh
   sbt "runMain com.example.PageRank --k 100 --iterations 10"
   ```

3. **Explore Lineage**:
   Modify the loop conditions in the code to explore the lineage of RDDs for 1, 2, and 3 iterations.

4. **Caching Behavior**:
   Experiment with caching the Ranks RDD using `cache()` or `persist()` to observe the effects on execution behavior and performance.

### Running the PageRank Program in MapReduce

1. **Generate Synthetic Data**:
   Use the provided script to generate synthetic graph data:
   ```sh
   python generate_synthetic_data.py --k 100
   ```

2. **Run the PageRank Program**:
   ```sh
   hadoop jar PageRank.jar com.example.PageRank /path/to/input /path/to/output
   ```

## Project Structure

- `src/main/scala/com/example/`: Contains the source code for the PageRank implementation in Spark.
- `src/main/java/com/example/`: Contains the source code for the PageRank implementation in MapReduce.
- `data/`: Contains scripts for generating synthetic data.
- `logs/`: Contains log files from the program executions.
- `results/`: Contains the output files with PageRank values.
- `README.md`: This file, providing an overview and instructions for the project.

## Contributing

We welcome contributions from the community! To contribute, follow these steps:

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Make your changes and commit them with clear messages.
4. Push your changes to your fork.
5. Open a pull request to the main repository.

Please ensure your code is well-documented and follows the project's coding standards.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

Execution
---------
All of the build & execution commands are organized in the Makefile.
1) Unzip project file.
2) Open command prompt.
3) Navigate to directory where project files unzipped.
4) Edit the Makefile to customize the environment at the top.
	Sufficient for standalone: hadoop.root, jar.name, local.input
	Other defaults acceptable for running standalone.
5) Standalone Hadoop:
	- `make switch-standalone`		-- set standalone Hadoop environment (execute once)
	- `make local`
6) Pseudo-Distributed Hadoop: (https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-common/SingleCluster.html#Pseudo-Distributed_Operation)
	- `make switch-pseudo`			-- set pseudo-clustered Hadoop environment (execute once)
	- `make pseudo`					-- first execution
	- `make pseudoq`				-- later executions since namenode and datanode already running 
7) AWS EMR Hadoop: (you must configure the emr.* config parameters at top of Makefile)
	- `make make-bucket`			-- only before first execution
	- `make upload-input-aws`		-- only before first execution
	- `make aws`					-- check for successful execution with web interface (aws.amazon.com)
	- `download-output-aws`		-- after successful execution & termination
