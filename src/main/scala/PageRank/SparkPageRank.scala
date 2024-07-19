/**
 * Main class to execute the PageRank algorithm using Spark.
 */
package PageRank;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.log4j.{LogManager, Level};
import org.apache.spark.sql.SparkSession;

/**
 * Main object to execute the PageRank algorithm.
 */
object PageRankMain {

  /**
   * Main method to start the PageRank algorithm.
   * @param args Array of command-line arguments: k (int), output directory (String).
   */
  def main(args: Array[String]) {
    val logger = LogManager.getRootLogger;
    logger.setLevel(Level.INFO);

    if (args.length != 2) {
      logger.error("Usage:\nPageRank.PageRankMain <k> <output dir>");
      System.exit(1);
    }

    // Value of k
    val k = args(0).toInt;
    val outputDir = args(1);

    // Spark Context
    val conf = new SparkConf().setAppName("PageRank");
    val sc = new SparkContext(conf);
    val sparkSession = SparkSession.builder().config(conf).getOrCreate();

    // Generate a list of nodes
    val totalNodes = k * k;
    val nodeList = List.range(1, totalNodes + 1);

    val initialPageRankValue = 1.0 / totalNodes;
    val edgeList = nodeList.flatMap { currNode =>
      if (currNode % k == 0) List((currNode, Seq(0)))
      else List((currNode, Seq(currNode + 1)))
    };

    val initialRanks = nodeList.map(nodeId => (nodeId, initialPageRankValue));
    val edgesRDD = sc.parallelize(edgeList).cache();

    // Parallelize ranks into an RDD
    var pageRankRDD = sc.parallelize(initialRanks);

    // Iterations
    val iterations: Int = 10;

    for (iteration <- 1 to iterations) {
      logger.info(s"Iteration $iteration");

      val contribs = edgesRDD.join(pageRankRDD).flatMap { case (pageId, (adjPages, rank)) =>
        adjPages.map(destination => (destination, rank / adjPages.size)) :+ (pageId, 0.0)
      }.reduceByKey(_ + _);

      val danglingNodeMass = contribs.lookup(0).headOption.getOrElse(0.0);
      logger.info(s"Dangling node mass: $danglingNodeMass for iteration $iteration");

      pageRankRDD = contribs.filter(_._1 != 0).mapValues(rank => rank + danglingNodeMass / totalNodes)
        .union(sc.parallelize(List((0, 0.0))));

      logger.warn(s"The total PageRank (PR) for iteration $iteration : ${pageRankRDD.map(_._2).sum()}");

      logger.info("Lineage for Ranks:");
      logger.info(pageRankRDD.toDebugString);
    }
    pageRankRDD.saveAsTextFile(outputDir);

    val sortedByKeyRDD = pageRankRDD.sortByKey(ascending = true);
    val first20RankRDD = sc.parallelize(sortedByKeyRDD.take(20));
    first20RankRDD.saveAsTextFile(outputDir + "_First_20");

    sparkSession.stop();
  }
}