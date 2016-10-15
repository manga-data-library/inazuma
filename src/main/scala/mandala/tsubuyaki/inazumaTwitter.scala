package mandala.tsubuyaki

import java.util.regex.Matcher
import java.util.regex.Pattern

import org.apache.spark.rdd.RDD

import com.atilika.kuromoji.ipadic.Token
import scala.collection.mutable.ArrayBuffer

object inazumaTwitter extends AbstractInazuma {

  override var printRankingNum = 100
  override var dictFilePath = "./dictionary/blank.txt"
  override var outputFilePath = "data.csv"

  override def analyzeFileWords(input: RDD[String], dictFilePath: String): RDD[String] = {

    // ref:http://www.intellilink.co.jp/article/column/bigdata-kk01.html
    val japanese_pattern: Pattern = Pattern.compile("[¥¥u3040-¥¥u309F]+") //「ひらがなが含まれているか？」の正規表現
    val pattern: Pattern = Pattern.compile("^[a-zA-Z]+$|^[0-9]+$") //「英数字か？」の正規表現

    input.flatMap(x => {
      // 不要な文字列の削除
      var text = x.replaceAll("http(s*)://(.*)/", "").replaceAll("¥¥uff57", "")

      val tokens = CustomTokenizer.tokenize(text, dictFilePath)
      val features: ArrayBuffer[String] = new ArrayBuffer[String]()

      if (japanese_pattern.matcher(x).find()) {
        tokens.foreach { token =>
          // 二文字以上の単語を抽出
          if (token.getSurface().length() >= 2) {
            val matcher: Matcher = pattern.matcher(token.getSurface())

            if (!matcher.find()) {
              val tokenFeatures = token.getAllFeaturesArray()
              if ((tokenFeatures(0) == "名詞" &&
                  (tokenFeatures(1) == "一般" || tokenFeatures(1) == "固有名詞")) ||
                token.getPartOfSpeechLevel1 == "カスタム名詞") {
                
                features += token.getSurface
              }
            }
          }
        }
      }

      (features)
    })
  }
}
