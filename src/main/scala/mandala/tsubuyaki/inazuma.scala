package mandala.tsubuyaki

import org.apache.spark.rdd.RDD

import com.atilika.kuromoji.ipadic.Token
import scala.collection.mutable.ArrayBuffer

object inazuma extends AbstractInazuma {

  override var printRankingNum = 50
  override var dictFilePath = "./dictionary/blank.txt"
  override var outputFilePath = "data.csv"
  
  override def analyzeFileWords(input: RDD[String], dictFilePath: String): RDD[String] = {
    
    input.flatMap(x => {
      val tokens = CustomTokenizer.tokenize(x, dictFilePath)
      val features: ArrayBuffer[String] = new ArrayBuffer[String]()

      tokens.foreach { token =>
        // 二文字以上の単語を抽出
        if (token.getSurface().length() >= 2) {

          val tokenFeatures = token.getAllFeaturesArray()
          if ((tokenFeatures(0) == "名詞" &&
              (tokenFeatures(1) == "一般" || tokenFeatures(1) == "固有名詞")) ||
            token.getPartOfSpeechLevel1 == "カスタム名詞") {
            
            features += token.getSurface
          }
        }
      }

      (features)
    })
  }
}
