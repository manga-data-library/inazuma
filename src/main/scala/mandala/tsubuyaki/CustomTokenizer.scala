package mandala.tsubuyaki

import com.atilika.kuromoji.TokenizerBase.Mode
import com.atilika.kuromoji.ipadic.{ Token, Tokenizer }

import scala.collection.JavaConverters._
import scala.collection.mutable.Buffer

object CustomTokenizer {

  def tokenize(text: String, dictPath: String): Buffer[Token] = {
    val builder = new Tokenizer.Builder().mode(Mode.SEARCH)
    val tokenizer: Tokenizer = builder.userDictionary(dictPath).build()

    tokenizer.tokenize(text).asScala
  }
}
