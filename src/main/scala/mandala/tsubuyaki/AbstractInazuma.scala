package mandala.tsubuyaki

import java.io.PrintWriter

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions

abstract class AbstractInazuma {

  /** ランキングの表示閾値 */
  var printRankingNum: Int
  /** 辞書ファイルのパス */
  var dictFilePath: String
  /** 結果を出力するCSVファイルのパス */
  var outputFilePath: String

  def main(args: Array[String]): Unit = {
    // パラメータ反映
    if (args.length >= 2) {
      dictFilePath = args(1)
    }
    if (args.length == 3) {
      printRankingNum = args(2).toInt
    }

    val conf = new SparkConf().setAppName("Inazuma Application")
    conf.setMaster("local[*]")
    
    val sc = new SparkContext(conf)
    val input = sc.textFile(args(0)) // hdfs://

    // kuromoji(形態要素解析)で日本語解析
    val words = analyzeFileWords(input, dictFilePath)

    // ソート方法を定義（必ずソートする前に定義）
    implicit val sortIntegersByString = new Ordering[Int] {
      override def compare(a: Int, b: Int) = a.compare(b) * (-1)
    }
    // ソート
    val result = words.map(x => (x, 1)).reduceByKey((x, y) => x + y).sortBy(_._2)

    // ソート結果から上位を取得し、結果をCSVファイルに保存
    val out = new PrintWriter(outputFilePath)
    try {
      for (r <- result.take(printRankingNum)) {
        println(r._1 + "    " + r._2)  // コンソール出力
        out.println(r._1 + "," + r._2)
      }
    } finally {
      out.close
    }
    
    sc.stop
  }

  def analyzeFileWords(input: RDD[String], dictFilePath: String): RDD[String]
}
